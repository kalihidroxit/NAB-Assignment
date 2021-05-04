package org.smartchoice.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartchoice.controller.SearchController;
import org.smartchoice.dto.ProductDto;

import java.util.ArrayList;
import java.util.List;

public class Lozado implements SearchResult {

    public static final Logger logger = LoggerFactory.getLogger(Lozado.class);

    @Override
    public List<ProductDto> getProduct(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        List<ProductDto> productDtoList = mapper.convertValue(o, new TypeReference<List<ProductDto>>() {});
        return productDtoList;
    }
}
