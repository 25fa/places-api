# Places Api Server - kako, naver places api result merge (kakao bank 사전과제)

## 실행방법
  1. 프로젝트 폴더에서 아래 명령어 수행
```
  % docker-compose up -d
```
  2. docker가 서버가 부팅되도록 충분히(5분정도) 대기 (아래 명령어로 로그를 확인)
```
  % docker logs spring-app
```

```
2022-01-13 09:38:12.569  INFO 189 --- [           main] c.b.telescope.TelescopeApplicationKt     : Starting TelescopeApplicationKt using Java 11.0.13 on 01c76d9cf4d9 with PID 189 (/app/build/classes/kotlin/main started by root in /app)
2022-01-13 09:38:12.577  INFO 189 --- [           main] c.b.telescope.TelescopeApplicationKt     : No active profile set, falling back to default profiles: default
2022-01-13 09:38:15.992  INFO 189 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode!
2022-01-13 09:38:16.000  INFO 189 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data Redis repositories in DEFAULT mode.
2022-01-13 09:38:16.208  INFO 189 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 162 ms. Found 1 Redis repository interfaces.
2022-01-13 09:38:17.452  INFO 189 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2022-01-13 09:38:17.476  INFO 189 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-01-13 09:38:17.476  INFO 189 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.56]
2022-01-13 09:38:17.622  INFO 189 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-01-13 09:38:17.622  INFO 189 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 4914 ms
2022-01-13 09:38:20.156  INFO 189 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-01-13 09:38:20.166  INFO 189 --- [           main] c.b.telescope.TelescopeApplicationKt     : Started TelescopeApplicationKt in 8.957 seconds (JVM running for 9.563)
```

## Test

- 장소 검색
```
  % curl --location --request GET 'localhost:8080/v1/place/search?keyword=조선옥'
```
- 검색 키워드 목록
```
  % curl --location --request GET 'localhost:8080/v1/place/search/recommend'
```

- Bulk Test
```
  HttpSearchTest.http 파일로 대량 입력하고 HttpRecommendTest.http파일로 추천검색어를 확인할수 있음.
```

## Liblary

- Rest 서비스를 사용하기 위해서 추가
```
  implementation("org.springframework.boot:spring-boot-starter-web")
```

- Redis Database를 사용하기 위해서 추가 : 대용량 처리를 위해서 rest call을 캐시 하고(i) 검색어 횟수 증가가 io가 빈번하기(ii) 때문에 사용함.
```
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
```

- http call을 쉽게하기 위해서 추가 : 가독성이 좋은 코드를 작성할 수 있으며 구현이 쉬움.
```
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
```
