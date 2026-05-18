# Spring Boot 3.5.x 학습 문서

Spring Boot 3.5.x를 초보자도 차근차근 배울 수 있도록 정리한 입문서형 문서 모음입니다.

한 파일에 모든 내용을 몰아넣지 않고, 책의 장처럼 `00~65`번 문서로 나누었습니다. 설치와 첫 실행부터 REST API, 데이터베이스, 테스트, 보안, 운영, 배포, 심화 주제, 마이그레이션, 최종 프로젝트까지 순서대로 학습할 수 있습니다.

## 추천 학습 방법

처음 배우는 사람은 `00`번부터 `65`번까지 순서대로 읽는 것을 추천합니다.

이미 Spring Boot를 사용해 본 사람은 필요한 Part부터 읽어도 됩니다.

- 빠르게 큰 그림을 잡고 싶다면: Part 0, Part 1, Part 2
- REST API를 만들고 싶다면: Part 3, Part 4, Part 5
- 실무 프로젝트 품질을 올리고 싶다면: Part 5, Part 6, Part 7
- 배포와 운영을 배우고 싶다면: Part 7, Part 8
- 한 단계 더 깊게 보고 싶다면: Part 9, Part 10

## 전체 목차

### Part 0. 학습 안내와 큰 그림

Spring Boot를 왜 배우는지, 어떤 순서로 공부하면 좋은지, Spring과 Spring Boot가 어떤 문제를 해결하기 위해 등장했는지 이해합니다.

- [00. 학습 문서 작성 계획](./00_학습_문서_작성_계획.md)
- [01. Spring Boot 3.5.x 학습 로드맵](./01_Spring_Boot_3_5_학습_로드맵.md)
- [02. Spring과 Spring Boot의 탄생](./02_Spring과_Spring_Boot의_탄생.md)
- [03. Spring Boot 3.5.x에서 달라진 점](./03_Spring_Boot_3_5에서_달라진_점.md)

### Part 1. 설치와 첫 실행

개발 환경을 준비하고, Spring Initializr로 프로젝트를 만든 뒤 첫 Spring Boot 애플리케이션을 실행합니다.

- [04. 개발 환경 설치](./04_개발_환경_설치.md)
- [05. Spring Initializr로 프로젝트 만들기](./05_Spring_Initializr로_프로젝트_만들기.md)
- [06. 첫 번째 Spring Boot 애플리케이션](./06_첫_번째_Spring_Boot_애플리케이션.md)
- [07. 프로젝트 구조 읽는 법](./07_프로젝트_구조_읽는_법.md)

### Part 2. Spring Boot 기본기

Starter, 자동 설정, Bean, 의존성 주입, 설정 파일, Profile, 로깅처럼 Spring Boot를 읽고 쓰는 데 필요한 기본기를 다집니다.

- [08. 의존성 관리와 Starter](./08_의존성_관리와_Starter.md)
- [09. 자동 설정 Auto Configuration](./09_자동_설정_Auto_Configuration.md)
- [10. 컴포넌트와 의존성 주입](./10_컴포넌트와_의존성_주입.md)
- [11. `@SpringBootApplication` 이해하기](./11_SpringBootApplication_이해하기.md)
- [12. 설정 파일과 외부 설정](./12_설정_파일과_외부_설정.md)
- [13. Profile로 환경 나누기](./13_Profile로_환경_나누기.md)
- [14. 로깅 기초](./14_로깅_기초.md)

### Part 3. 웹 애플리케이션 만들기

HTTP와 REST API의 기본을 배우고, Controller, DTO, 검증, 예외 처리, Filter와 Interceptor를 익힙니다.

- [15. HTTP와 REST API 기초](./15_HTTP와_REST_API_기초.md)
- [16. Controller와 RestController](./16_Controller와_RestController.md)
- [17. 요청과 응답 DTO](./17_요청과_응답_DTO.md)
- [18. 검증 Validation](./18_검증_Validation.md)
- [19. 예외 처리](./19_예외_처리.md)
- [20. 필터, 인터셉터, 서블릿 기초](./20_필터_인터셉터_서블릿_기초.md)

### Part 4. 데이터 다루기

H2, JDBC, JPA, Spring Data JPA, 트랜잭션, DB 마이그레이션, PostgreSQL 전환까지 데이터 계층의 핵심을 학습합니다.

- [21. H2로 DB 입문하기](./21_H2로_DB_입문하기.md)
- [22. JDBC와 JdbcClient](./22_JDBC와_JdbcClient.md)
- [23. JPA와 Entity 기초](./23_JPA와_Entity_기초.md)
- [24. Spring Data JPA](./24_Spring_Data_JPA.md)
- [25. 트랜잭션](./25_트랜잭션.md)
- [26. DB 마이그레이션 Flyway/Liquibase](./26_DB_마이그레이션_Flyway_Liquibase.md)
- [27. PostgreSQL로 전환하기](./27_PostgreSQL로_전환하기.md)

### Part 5. 테스트

JUnit, AssertJ, Spring Boot Test, slice test, Testcontainers를 사용해 신뢰할 수 있는 테스트 구조를 만듭니다.

- [28. 테스트 기초 JUnit AssertJ](./28_테스트_기초_JUnit_AssertJ.md)
- [29. Spring Boot Test](./29_Spring_Boot_Test.md)
- [30. WebMvcTest와 슬라이스 테스트](./30_WebMvcTest와_슬라이스_테스트.md)
- [31. DataJpaTest](./31_DataJpaTest.md)
- [32. Testcontainers](./32_Testcontainers.md)
- [33. 테스트하기 좋은 구조](./33_테스트하기_좋은_구조.md)

### Part 6. 보안

Spring Security의 기본 개념부터 세션 로그인, JWT, 권한 처리, 보안 테스트까지 단계적으로 배웁니다.

- [34. Spring Security 기초](./34_Spring_Security_기초.md)
- [35. 세션 기반 로그인](./35_세션_기반_로그인.md)
- [36. JWT 인증](./36_JWT_인증.md)
- [37. 권한과 메서드 보안](./37_권한과_메서드_보안.md)
- [38. 보안 테스트](./38_보안_테스트.md)

### Part 7. 운영 준비

Actuator, 메트릭, 분산 추적, 운영 설정, graceful shutdown, 운영 장애 패턴을 정리합니다.

- [39. Actuator 기초](./39_Actuator_기초.md)
- [40. 모니터링과 메트릭](./40_모니터링과_메트릭.md)
- [41. 분산 추적과 Observability](./41_분산_추적과_Observability.md)
- [42. 운영용 설정 관리](./42_운영용_설정_관리.md)
- [43. Graceful Shutdown](./43_Graceful_Shutdown.md)
- [44. 운영에서 자주 나는 문제](./44_운영에서_자주_나는_문제.md)

### Part 8. 빌드와 배포

실행 가능한 JAR, Docker 이미지, Buildpacks, Docker Compose, 클라우드 배포, Native Image를 배웁니다.

- [45. 실행 가능한 JAR 만들기](./45_실행_가능한_JAR_만들기.md)
- [46. Docker 이미지 만들기](./46_Docker_이미지_만들기.md)
- [47. Cloud Native Buildpacks](./47_Cloud_Native_Buildpacks.md)
- [48. Docker Compose로 로컬 환경 구성](./48_Docker_Compose로_로컬_환경_구성.md)
- [49. 클라우드 배포 개요](./49_클라우드_배포_개요.md)
- [50. GraalVM Native Image 입문](./50_GraalVM_Native_Image_입문.md)

### Part 9. 심화 주제

캐싱, 스케줄링, 비동기, 이메일, REST Client, 메시징, Batch, WebFlux, 커스텀 자동 설정을 입문 수준에서 정리합니다.

- [51. 캐싱](./51_캐싱.md)
- [52. 스케줄링과 비동기](./52_스케줄링과_비동기.md)
- [53. 이메일 전송](./53_이메일_전송.md)
- [54. REST Client](./54_REST_Client.md)
- [55. 메시징 Kafka/RabbitMQ](./55_메시징_Kafka_RabbitMQ.md)
- [56. Batch 입문](./56_Batch_입문.md)
- [57. WebFlux 입문](./57_WebFlux_입문.md)
- [58. 커스텀 Auto Configuration](./58_커스텀_Auto_Configuration.md)

### Part 10. 마이그레이션과 부록

Spring Boot 버전 업그레이드, 자주 쓰는 애너테이션과 설정, 오류 해결법, 공식 문서 읽는 법, 최종 프로젝트 설계를 정리합니다.

- [59. Spring Boot 2에서 3으로 마이그레이션](./59_Spring_Boot_2에서_3으로_마이그레이션.md)
- [60. Spring Boot 3.4에서 3.5로 업그레이드](./60_Spring_Boot_3_4에서_3_5로_업그레이드.md)
- [61. 자주 쓰는 애너테이션 정리](./61_자주_쓰는_애너테이션_정리.md)
- [62. 자주 쓰는 설정 프로퍼티](./62_자주_쓰는_설정_프로퍼티.md)
- [63. 오류 메시지로 문제 해결하기](./63_오류_메시지로_문제_해결하기.md)
- [64. 공식 문서 읽는 법](./64_공식_문서_읽는_법.md)
- [65. 최종 프로젝트](./65_최종_프로젝트.md)

## 문서 구성 원칙

각 문서는 다음 흐름을 기본으로 합니다.

1. 왜 이 내용을 배우는가
2. 꼭 알아야 할 개념
3. 직접 따라 하는 예제
4. 자주 하는 실수
5. 다음 문서로 넘어가기 전에 확인할 것

## 전체 구성 완료

현재 계획한 `00~65`번 문서 작성을 완료했습니다.

다음 단계에서는 필요에 따라 다음 작업을 이어갈 수 있습니다.

- 각 문서의 예제 코드를 실제 Spring Boot 프로젝트로 구현
- 문서 간 이전/다음 링크와 관련 문서 링크 보강
- 오탈자와 용어 통일 검수
- 최종 프로젝트용 샘플 코드 작성
- GitHub Pages에서 보기 좋은 인덱스 구성
