# Spring Boot 2에서 3으로 마이그레이션

## 이번 장에서 배울 것

이번 장에서는 Spring Boot 2.x 애플리케이션을 Spring Boot 3.x 계열로 올릴 때 무엇을 조심해야 하는지 배운다.

Spring Boot 2에서 3으로의 업그레이드는 단순한 버전 숫자 변경이 아니다. Java 버전, Jakarta EE 패키지, Spring Framework 6, Spring Security 설정 방식, Hibernate 6 등 여러 기반 기술이 함께 바뀐다.

목표는 한 번에 모든 문제를 외우는 것이 아니라, 마이그레이션을 안전하게 진행하는 순서를 익히는 것이다.

## 먼저 2.7 최신으로 올리기

Spring Boot 2.x에서 바로 3.x로 점프하면 문제가 한꺼번에 터진다.

권장 흐름:

```text
현재 2.x
  -> Spring Boot 2.7 최신 패치
  -> deprecated 경고 제거
  -> Spring Boot 3.x
  -> Spring Boot 3.5.x
```

2.7은 3.x로 넘어가기 전 준비 단계로 중요하다. 2.7에서 이미 새 자동 설정 등록 파일인 `AutoConfiguration.imports`가 도입되었고, 일부 변화에 미리 적응할 수 있다.

## Java 17 이상

Spring Boot 3.x는 Java 17 이상이 필요하다. Spring Boot 3.5.14 기준으로는 Java 17 이상이 필요하고 Java 25까지 호환된다.

먼저 다음을 확인한다.

```bash
java -version
```

Gradle 예:

```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
```

Java 8이나 11에서 오래 운영하던 프로젝트라면 코드보다 빌드, CI, Docker 이미지, 운영 서버 JDK를 먼저 확인해야 한다.

## javax에서 jakarta로 변경

가장 눈에 띄는 변화는 패키지 이름이다.

Spring Boot 2.x:

```java
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.servlet.Filter;
```

Spring Boot 3.x:

```java
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import jakarta.servlet.Filter;
```

단순 검색/치환으로 해결되는 부분도 있지만, 라이브러리 호환성까지 함께 봐야 한다.

확인할 것:

- JPA 관련 import
- Bean Validation 관련 import
- Servlet API 관련 import
- JAXB, Mail, Annotation API 관련 import
- 외부 라이브러리가 Jakarta를 지원하는지

## Spring Framework 6

Spring Boot 3.x는 Spring Framework 6 기반이다.

영향이 큰 부분:

- Java 17 baseline
- Jakarta EE 9+ 패키지
- URL trailing slash 매칭 기본 동작 변화
- 일부 deprecated API 제거
- HTTP client 관련 변경

예를 들어 Spring Framework 6부터는 기본적으로 다음 두 경로를 같게 보지 않는다.

```text
/todos
/todos/
```

기존에 둘 다 허용된다고 가정한 API가 있다면 404가 날 수 있다.

## Hibernate 6과 JPA

Spring Boot 3.x는 Hibernate 6 계열을 사용한다.

확인할 것:

- custom dialect
- JPQL 문법
- native query 결과 매핑
- `@Type` 같은 Hibernate 전용 애너테이션
- 날짜/시간 타입 매핑
- generated value 전략

마이그레이션 중 DB 관련 테스트가 가장 중요한 이유가 여기에 있다. 컴파일은 성공해도 쿼리가 런타임에 실패할 수 있다.

## Spring Security 설정 변경

Spring Security 5.7 이후로 `WebSecurityConfigurerAdapter` 방식은 더 이상 권장되지 않는다.

예전 방식:

```java
public class SecurityConfig extends WebSecurityConfigurerAdapter {
}
```

새 방식:

```java
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated())
                .build();
    }
}
```

보안 설정은 컴파일 오류를 고치는 것으로 끝나지 않는다. 실제 인증, 인가, CSRF, CORS 테스트를 반드시 실행해야 한다.

## 자동 설정 등록 방식

Spring Boot 3에서는 자동 설정을 `spring.factories`의 `EnableAutoConfiguration` 키로 등록하는 방식이 제거되었다.

Spring Boot 3.x 방식:

```text
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

라이브러리나 사내 starter를 직접 만들고 있다면 이 파일을 확인해야 한다.

## 설정 프로퍼티 점검

Spring Boot는 버전이 올라가면서 설정 프로퍼티 이름이 바뀌거나 제거될 수 있다.

확인 방법:

- IDE의 `application.yml` 경고 확인
- Spring Boot configuration changelog 확인
- 실행 시 startup warning 확인
- deprecated properties 제거

예:

```yaml
server:
  max-http-header-size: 8KB
```

Spring Boot 3.0에서는 요청 헤더 크기 설정의 일관성을 위해 `server.max-http-request-header-size`가 도입되었다.

## 마이그레이션 순서

실무에서 추천하는 순서:

1. 테스트를 먼저 확보한다.
2. Spring Boot 2.7 최신 패치로 올린다.
3. deprecated API와 설정을 제거한다.
4. JDK를 17 이상으로 올린다.
5. 빌드 도구와 플러그인을 갱신한다.
6. `javax.*`를 `jakarta.*`로 변경한다.
7. Spring Security 설정을 새 방식으로 바꾼다.
8. JPA/Hibernate 쿼리 테스트를 실행한다.
9. Actuator, logging, metrics 설정을 확인한다.
10. staging 환경에서 실제 트래픽과 가까운 테스트를 한다.

## 자주 하는 실수

### import만 바꾸면 끝난다고 생각한다

`javax`에서 `jakarta`로 바꾸는 것은 시작일 뿐이다. 의존성, 런타임 동작, 쿼리, 보안 설정까지 봐야 한다.

### 테스트 없이 버전만 올린다

마이그레이션은 테스트가 거의 전부다. 특히 DB, 보안, 외부 연동 테스트가 중요하다.

### 서드파티 라이브러리를 확인하지 않는다

내 코드는 Jakarta로 바뀌었는데 라이브러리가 아직 Javax 기반이면 런타임 충돌이 생길 수 있다.

### 운영 JDK를 빼먹는다

로컬은 Java 21인데 운영 Docker 이미지가 Java 11이면 배포 후 바로 실패한다.

## 확인 문제

1. Spring Boot 3.x에서 필요한 최소 Java 버전은 무엇인가?
2. `javax.persistence.Entity`는 어떤 패키지로 바뀌는가?
3. Spring Boot 3.x에서 자동 설정 클래스를 등록하는 파일은 무엇인가?
4. 마이그레이션 전에 테스트를 확보해야 하는 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.0 Migration Guide  
  https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide
- Spring Boot 3.5 System Requirements  
  https://docs.spring.io/spring-boot/3.5/system-requirements.html

## 다음 장으로

다음 장에서는 Spring Boot 3.4에서 3.5로 올릴 때 확인해야 할 변경 사항을 정리한다.
