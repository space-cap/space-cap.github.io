# Testcontainers

## 이번 장에서 배울 것

이 장에서는 Testcontainers를 배운다.

H2는 빠르고 편하지만 PostgreSQL, MySQL 같은 실제 DB와 완전히 같지 않다. Testcontainers는 테스트 실행 중 Docker 컨테이너로 실제 DB를 띄워 테스트할 수 있게 해준다.

## Testcontainers가 필요한 이유

다음 상황을 생각해 보자.

```text
H2 테스트는 통과했다.
PostgreSQL 운영 환경에서는 SQL 문법 오류가 났다.
```

이런 일은 실제로 자주 생긴다.

원인은 다양하다.

- H2와 PostgreSQL의 SQL 문법 차이
- 컬럼 타입 차이
- 자동 증가 ID 문법 차이
- 인덱스와 제약 조건 차이
- 날짜 함수 차이

Testcontainers는 테스트에서 실제 PostgreSQL 컨테이너를 띄워 이런 차이를 줄인다.

## 준비 조건

Testcontainers를 사용하려면 Docker가 필요하다.

확인:

```bash
docker version
```

Docker Desktop 또는 Docker Engine이 실행 중이어야 한다.

## 의존성

Gradle 예:

```groovy
dependencies {
    testImplementation 'org.springframework.boot:spring-boot-testcontainers'
    testImplementation 'org.testcontainers:junit-jupiter'
    testImplementation 'org.testcontainers:postgresql'
    runtimeOnly 'org.postgresql:postgresql'
}
```

JPA 테스트와 함께 사용한다면 다음도 필요하다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

## `@ServiceConnection`

Spring Boot 3.1 이후에는 Testcontainers 연동이 더 편해졌다. `@ServiceConnection`을 사용하면 컨테이너 연결 정보를 Spring Boot 자동 설정에 전달할 수 있다.

```java
package com.example.todo.todo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
class TodoRepositoryPostgresTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16");

    @Test
    void containerIsRunning() {
        assertThat(postgres.isRunning()).isTrue();
    }
}
```

`@ServiceConnection` 덕분에 `spring.datasource.url`, `username`, `password`를 직접 동적으로 설정하지 않아도 된다.

## Repository 테스트 예제

```java
package com.example.todo.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
class TodoRepositoryPostgresTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16");

    @Autowired
    TodoRepository todoRepository;

    @Test
    void saveAndFindById() {
        Todo saved = todoRepository.save(new Todo("PostgreSQL 테스트"));

        Todo found = todoRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getTitle()).isEqualTo("PostgreSQL 테스트");
    }
}
```

이 테스트는 H2가 아니라 PostgreSQL 컨테이너를 사용한다.

## Flyway와 함께 사용하기

실무형 테스트에서는 Entity의 `ddl-auto`보다 Flyway 마이그레이션을 사용해 스키마를 만든다.

테스트 설정:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate

  flyway:
    enabled: true
```

테스트가 시작되면 PostgreSQL 컨테이너가 뜨고, Spring Boot가 Flyway 마이그레이션을 적용한 뒤 Repository 테스트를 실행할 수 있다.

이 흐름은 운영 DB 스키마와 테스트 스키마를 맞추는 데 큰 도움이 된다.

## `@SpringBootTest`와 Testcontainers

전체 애플리케이션 통합 테스트에서도 사용할 수 있다.

```java
package com.example.todo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class TodoApplicationIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16");

    @Test
    void contextLoads() {
    }
}
```

이 테스트는 전체 Spring Boot Context가 실제 PostgreSQL 컨테이너와 함께 뜨는지 확인한다.

## 테스트 속도

Testcontainers는 실제 컨테이너를 띄우므로 H2보다 느리다.

추천 전략:

- 빠른 단위 테스트를 많이 둔다.
- H2 기반 슬라이스 테스트로 기본 흐름을 확인한다.
- 중요한 DB 호환성은 Testcontainers로 확인한다.
- 전체 통합 테스트는 핵심 시나리오 위주로 둔다.

모든 테스트를 Testcontainers로 만들면 피드백이 느려질 수 있다.

## CI에서의 주의점

CI 서버에서 Testcontainers를 사용하려면 Docker 실행이 가능해야 한다.

확인할 것:

- Docker 사용 가능 여부
- 컨테이너 이미지 다운로드 가능 여부
- 테스트 시간 증가
- 병렬 테스트 시 리소스 사용량

CI 환경이 Docker를 지원하지 않으면 Testcontainers 테스트가 실패한다.

## 자주 하는 실수

### Docker를 켜지 않고 테스트를 실행한다

Testcontainers는 Docker가 필요하다. 먼저 Docker가 실행 중인지 확인한다.

### 모든 테스트를 컨테이너 기반으로 만든다

테스트가 느려질 수 있다. 단위 테스트와 슬라이스 테스트를 함께 사용한다.

### H2와 PostgreSQL 스키마를 따로 관리한다

가능하면 Flyway 같은 마이그레이션으로 실제 DB 기준 스키마를 관리한다.

## 확인 문제

1. Testcontainers가 해결하려는 문제는 무엇인가?
2. Spring Boot에서 컨테이너 연결 정보를 자동 설정에 전달하기 위해 사용할 수 있는 애너테이션은 무엇인가?
3. Testcontainers 테스트가 H2 테스트보다 느릴 수 있는 이유는 무엇인가?
4. CI에서 Testcontainers를 사용할 때 확인해야 할 것은 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Testcontainers  
  https://docs.spring.io/spring-boot/3.5/reference/testing/testcontainers.html
- Testcontainers 공식 문서  
  https://testcontainers.com/

## 다음 장으로

다음 장에서는 테스트하기 좋은 구조를 배운다. 테스트 도구보다 중요한 것은 테스트하기 쉬운 설계다.

---

## 문서 이동

- [이전: 31. DataJpaTest](./31_DataJpaTest.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 33. 테스트하기 좋은 구조](./33_테스트하기_좋은_구조.md)
