package org.smartchoice.seviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartchoice.controller.SearchController;
import org.smartchoice.dto.ProductDto;
import org.smartchoice.entity.AppUser;
import org.smartchoice.entity.HistorySearch;
import org.smartchoice.entity.ProductLookup;
import org.smartchoice.entity.ThirdParty;
import org.smartchoice.factory.SearchFactory;
import org.smartchoice.factory.SearchResult;
import org.smartchoice.repo.*;
import org.smartchoice.service.SearchService;
import org.smartchoice.utils.ApiResponse;
import org.smartchoice.utils.Constants;
import org.smartchoice.utils.ThirdPartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    public static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private ThirdPartyRepo thirdPartyRepo;

    @Autowired
    private TokenStoreRepo tokenStoreRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private HistorySearchRepo historySearchRepo;

    @Autowired
    private ProductLookupRepo productLookupRepo;

    @Autowired
    private EntityManager entityManager;

    @Override
    public ApiResponse searchByKeyword(String keyword, List<String> categories, List<String> brands,
                                       List<String> thirdParties, String token) {

        ApiResponse apiResponse = ApiResponse.getFailureResponse();

        List<ThirdParty> thirdPartyList;
        if (thirdParties == null || thirdParties.isEmpty()) {
            thirdPartyList = thirdPartyRepo.findAll();
        } else {
            List<ThirdPartyType> thirdPartyTypeList = new ArrayList<>();
            thirdParties.forEach(i -> {
                ThirdPartyType thirdPartyType = ThirdPartyType.valueOf(i);
                thirdPartyTypeList.add(thirdPartyType);
            });
            thirdPartyList = thirdPartyRepo.findByNameIn(thirdPartyTypeList);
        }

        List<ProductDto> productDtoList = new ArrayList<>();

        if (productLookupRepo.existsByKeyword(keyword)) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<ProductLookup> root = cq.from(ProductLookup.class);
            List<Predicate> predicateList = new ArrayList<>();

            predicateList.add(cb.like(cb.upper(root.get("keyword").as(String.class)), "%"+keyword.toUpperCase()+"%"));

            if (categories != null && !categories.isEmpty()) {
                predicateList.add(root.get("category").in(categories));
            }
            if (brands != null && !brands.isEmpty()) {
                predicateList.add(root.get("brand").in(brands));
            }

            cq.select(root).distinct(true).where(predicateList.toArray(new Predicate[0]));
            TypedQuery<ProductLookup> typedQuery = entityManager.createQuery(cq);

            //TODO: if have a lot of result, we can use limit. In demo, I using get all list
            List<ProductLookup> productsList = typedQuery.getResultList();

            productDtoList = productsList.stream().map(ProductDto::new).collect(Collectors.toList());
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate();

            List<ProductDto> finalProductDtoList = productDtoList;
            thirdPartyList.parallelStream().forEach(thirdParty -> {
                headers.add(Constants.X_API_KEY_HEADER_NAME, thirdParty.getXApiKey());
                String url = thirdParty.getUrl() + "/product/search";
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam("name", keyword)
                        .queryParam("categories", categories)
                        .queryParam("brands", brands);
                System.out.println(builder.build().encode().toUri());
                HttpEntity<?> entity = new HttpEntity<>(headers);
                ResponseEntity<ApiResponse> response = restTemplate.exchange(builder.toUriString(),
                        HttpMethod.GET, entity, ApiResponse.class);

                if (response.getStatusCode().equals(HttpStatus.OK) && Objects.requireNonNull(response.getBody()).getData() != null) {
                    SearchResult searchResult = SearchFactory.getSearch(thirdParty.getName());
                    List<ProductDto> temp = searchResult.getProduct(response.getBody().getData());

                    //Save keyword and products that not exist on lookup table
                    for (ProductDto productDto: temp) {
                        ProductLookup productLookup = new ProductLookup(productDto);
                        productLookup.setKeyword(keyword);
                        productLookup.setPulledAt(LocalDateTime.now());
                        productLookup.setThirdParty(thirdParty);
                        productLookupRepo.save(productLookup);
                    }

                    finalProductDtoList.addAll(temp);
                }
            });
            productDtoList = finalProductDtoList;
        }

        // Save search history of user
        try {
            token = token.replace(Constants.AUTH_HEADER_TOKEN_PREFIX, "");
            AppUser appUser = appUserRepo.findByUserName(tokenStoreRepo.findByTokenAndIsExpFalse(token).get().getUser()).get();

            HistorySearch historySearch = new HistorySearch();
            historySearch.setAppUser(appUser);
            historySearch.setKeyword(keyword);
            historySearchRepo.save(historySearch);
        } catch (Exception e) {
            e.printStackTrace();
        }

        apiResponse = ApiResponse.getSuccessResponse();
        apiResponse.setData(productDtoList);
        return apiResponse;
    }

    @Override
    public ApiResponse getItemDetail(Long id, String thirdParty) throws Exception {
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
        ProductLookup productLookup = productLookupRepo
                .findByProductIdAndThirdPartyName(id, ThirdPartyType.valueOf(thirdParty))
                .orElseThrow(() -> new Exception("Item not found"));
        ProductDto productDto = new ProductDto(productLookup);
        apiResponse.setData(productDto);
        return apiResponse;
    }
}
