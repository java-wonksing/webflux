# README

## 테스트

### CommandLineRunner 실행 문제
- CommandLineRunner가 테스트할 때 실행되서 `@Profile("!test")`와 `@ActiveProfiles("test")` 어노테이션을 추가함.
    - `DemoApplication.java`
    ```java
    @Profile("!test")
    @SpringBootApplication
    public class DemoApplication implements CommandLineRunner{
    ```

    - `DemoApplicationTests.java`
    ```java
    @ActiveProfiles("test")
    @SpringBootTest
    class DemoApplicationTests {

    	@Test
    	void contextLoads() {
    	}

    }
    ```