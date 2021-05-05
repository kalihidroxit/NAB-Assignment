# **Java Development Challenge**

**Problem Statement**

A startup company name `"Smart Choice"` build a website that can compare the
price of a product from `"Lozado"`.

<ol>
<li>Structure</li>
<ol>

Web app using Spring Boot to develop. 

H2 Database is chosen because it is suitable for local demo.

General of structure folder of `src/main/java` including:

<ul>
<li>Controller: All controller is defined in this package</li>
<li>Dto: This is Data Transfer Object that are response to client</li>
<li>Entity: Define entity that is mapped into database</li>
<li>Repo: Using JPA Repository</li>
<li>Security: Define all methods to authorize user</li>
<li>Utils: Include utilizes: constants, enums, ...</li>
</ul>

Resources structure:
<ul>
<li>application.properties: config file</li>
<li>Sql files: auto generate data</li>
</ul>

Directory `/test` : include unit test files

<li>Lozado struture</li>

![img_1.png](img_1.png)


<li>Smart Choice structure</li>

![img_2.png](img_2.png)


<li>Dependencies</li>

Including: spring-web, spring-data-jpa, spring-security, H2, jsonwebtoken, lombok, ... 
</ol>
<li>Database relationship</li>

* Entity Relationship of Lozado
![img_3.png](img_3.png)
  
* Entity Relationship of Smart Choice
![img_4.png](img_4.png)
  
<li>General</li>
<ol>
<li>Lozado</li>

This simulates website similar to Tiki, Lazada, .... 
You can search any item, and you need the x-api-key to access this features. 

Each user has an x-api-key, and it stored in `App User` table. 

Each request to Lozado server, APIKeyFilter will check x-api-key in request header. If it has permission, you can continue your work.

The system has controller for search item method by keyword, brands and categories. For this demo, I'm not limit the result length.

    @RestController
    @RequestMapping("/product")
    public class SearchController {

        @Autowired
        private SearchService searchService;
    
        @GetMapping("/search")
        public ResponseEntity<ApiResponse> getProductByName(@RequestParam String name,
                                                            @RequestParam(required = false) List<String> brands,
                                                            @RequestParam(required = false) List<String> categories) throws Exception{
            ApiResponse apiResponse = searchService.getProductByName(name, brands, categories);
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }

<li>Smart Choice</li>

First, you need login by username and password. You can check in `SmartChoiceApplication` , and you can be custom username and password.

    @SpringBootApplication
    public class SmartChoiceApplication implements CommandLineRunner {

        @Autowired
        private AppUserRepo appUserRepo;
    
        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;
    
        public static void main(String[] args) {
    
            SpringApplication.run(SmartChoiceApplication.class, args);
        }
    
        @Override
        public void run(String... args) {
            AppUser appUser = new AppUser();
            appUser.setUserName("mvp_user");
            appUser.setPassword(bCryptPasswordEncoder.encode("123456"));
            appUserRepo.save(appUser);
        }
    } 
Password is encrypted before it stored to DB. If you are not login, all method can't access. After login, user has a token, and it will expire after 30 minute.

Then, you can search item and get item's detail. Assume the first times, you're only searching by keyword. After that, you can sort by category, brand and third party. 

    @RestController
    public class SearchController {

        @Autowired
        private SearchService searchService;
    
        @GetMapping("/search")
        public ResponseEntity<ApiResponse> search(@RequestParam String keyword,
                                                  @RequestParam(required = false) List<String> categories,
                                                  @RequestParam(required = false) List<String> brands,
                                                  @RequestParam(required = false) List<String> thirdParties,
                                                  @RequestHeader(Constants.AUTH_HEADER_NAME) String token){
            ApiResponse apiResponse = searchService.searchByKeyword(keyword, categories, brands, thirdParties, token);
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    
        @GetMapping("/item")
        public ResponseEntity<ApiResponse> search(@RequestParam Long id,
                                                  @RequestParam String thirdParty) throws Exception{
            ApiResponse apiResponse = searchService.getItemDetail(id, thirdParty);
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
    }

When you're searching, the key when send to third parties, that store in `Third Party` table, by url to get result. In demo, I not encrypt the x-api-key (if needed, we can use KMS to encrypt what sensitive information).

Because each result may have different format, I used Factory Method to map data into `ProductDto` class.

* Interface

    

        public interface SearchResult {
            List<ProductDto> getProduct(Object o);
        }

* Implement for Lozado
  
    

        public class Lozado implements SearchResult {
    
            public static final Logger logger = LoggerFactory.getLogger(Lozado.class);
        
            @Override
            public List<ProductDto> getProduct(Object o) {
                ObjectMapper mapper = new ObjectMapper();
                List<ProductDto> productDtoList = mapper.convertValue(o, new TypeReference<List<ProductDto>>() {});
                return productDtoList;
            }
        }

* Search Factory

    

        public class SearchFactory {
            public static final SearchResult getSearch(ThirdPartyType thirdPartyType) {
            switch (thirdPartyType) {
            
                    case Lozado:
                        return new Lozado();
            
                    default:
                        throw new IllegalArgumentException("This third party is unsupported");
                }
            }
        }


Each request sent to third party will be charged for a fee, so I created `Product Lookup` table to save all product from third party.
Actually, we can use Redis or call cron job (AWS EventBridge, ...) to update by schedule. But in the demo, I not using it. 

I have check keyword in `Product Lookup` first. If the keyword not exists, the system will call to third party, by http request, to search product. After that, they will be saved to `Product Lookup` table  

For audit support, the system keep track of the searching history of
the customer and save to `Search History` table. Failure to store customer activity should have no impact on the
function or performance of the Rest APIs.
</ol>
<li>Run app</li>

* First, we need build both of Lozado and Smart Choice: `mvn clean install`

* Then, run command `mvn package` to build jar file

* Next, run command `java -jar <service>/target/<service>-0.0.1-SANPSHOT.jar` to run

<li>CURL</li>

<ol>
<li>Login to Smart Choice</li>

    curl --location --request POST 'http://localhost:7072/auth/login?userName=mvp_user&password=123456' \
    --header 'Connection: keep-alive' \
    --header 'Cache-Control: max-age=0' \
    --header 'sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="90", "Microsoft Edge";v="90"' \
    --header 'sec-ch-ua-mobile: ?0' \
    --header 'Upgrade-Insecure-Requests: 1' \
    --header 'User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36 Edg/90.0.818.49' \
    --header 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9' \
    --header 'Sec-Fetch-Site: none' \
    --header 'Sec-Fetch-Mode: navigate' \
    --header 'Sec-Fetch-User: ?1' \
    --header 'Sec-Fetch-Dest: document' \
    --header 'Accept-Language: en-US,en;q=0.9,vi;q=0.8' \
    --header 'Cookie: uuid=3fdce89b-4d86-4234-b7d2-56d73452a0a6; JSESSIONID=19B350CF865865BD186FFA996F002CCC'

The response has a token that used to access another method.

    {
        "status": "OK",
        "message": "",
        "data": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdnBfdXNlciIsImlhdCI6MTYyMDIxNjc4NCwiZXhwIjoxNjIwMjE4NTg0fQ.J2K3KthZyu3LTd2oXOQF3FtKgzGDOZWdSkSra0MKcJc",
        "timestamp": 1620216784483,
        "statusCode": 200
    }
    
<li>Search Item</li>
You can copy and paste the token to the field Token like image below, or insert Authorization header and token.

![img_5.png](img_5.png)

* CURL for method search


    curl --location --request GET 'http://localhost:7072/search?keyword=shoe' \
    --header 'Connection: keep-alive' \
    --header 'Cache-Control: max-age=0' \
    --header 'sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="90", "Microsoft Edge";v="90"' \
    --header 'sec-ch-ua-mobile: ?0' \
    --header 'Upgrade-Insecure-Requests: 1' \
    --header 'User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36 Edg/90.0.818.49' \
    --header 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9' \
    --header 'Sec-Fetch-Site: none' \
    --header 'Sec-Fetch-Mode: navigate' \
    --header 'Sec-Fetch-User: ?1' \
    --header 'Sec-Fetch-Dest: document' \
    --header 'Accept-Language: en-US,en;q=0.9,vi;q=0.8' \
    --header 'Cookie: uuid=3fdce89b-4d86-4234-b7d2-56d73452a0a6; JSESSIONID=19B350CF865865BD186FFA996F002CCC' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdnBfdXNlciIsImlhdCI6MTYyMDIxNjc4NCwiZXhwIjoxNjIwMjE4NTg0fQ.J2K3KthZyu3LTd2oXOQF3FtKgzGDOZWdSkSra0MKcJc'
</ol>
</ol>



