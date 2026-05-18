# Spring Boot Test

## 이번 장에서 배울 것

이 장에서는 `@SpringBootTest`를 배운다.

단위 테스트는 빠르지만 Spring Bean 연결, 설정 파일, 자동 설정까지 확인하지는 않는다. 애플리케이션이 실제로 Spring Boot 환경에서 뜨는지 확인하려면 Spring Context를 로드하는 테스트가 필요하다.

## `@SpringBootTest`란 무엇인가

`@SpringBootTest`는 Spring Boot 애플리케이션 테스트를 위한 대표 애너테이션이다.

이 애너테이션을 붙이면 Spring Boot가 실제 애플리케이션과 비슷하게 Spring ApplicationContext를 만든다.

```java
package com.example.todo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TodoApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

`contextLoads` 테스트는 비어 있어도 의미가 있다. Spring Context가 정상적으로 로드되면 통과하고, Bean 충돌이나 설정 오류가 있으면 실패한다.

## 언제 사용할까?

`@SpringBootTest`는 다음을 확인할 때 사용한다.

- 애플리케이션 Context가 정상적으로 뜨는지
- 여러 Bean이 실제로 연결되는지
- 설정 파일이 올바르게 적용되는지
- 전체 흐름에 가까운 통합 테스트가 필요한지

하지만 모든 테스트를 `@SpringBootTest`로 만들면 느려질 수 있다. Controller만 테스트할 때는 `@WebMvcTest`, Repository만 테스트할 때는 `@DataJpaTest` 같은 슬라이스 테스트를 먼저 고려한다.

## Service 통합 테스트 예제

예제 Service:

```java
package com.example.todo.todo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public TodoResponse create(TodoCreateRequest request) {
        Todo todo = new Todo(request.title());
        Todo saved = todoRepository.save(todo);
        return TodoResponse.from(saved);
    }
}
```

테스트:

```java
package com.example.todo.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TodoServiceTest {

    @Autowired
    TodoService todoService;

    @Test
    void createTodo() {
        TodoCreateRequest request = new TodoCreateRequest("테스트 작성");

        TodoResponse response = todoService.create(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("테스트 작성");
        assertThat(response.completed()).isFalse();
    }
}
```

테스트 클래스에 `@Transactional`을 붙이면 테스트가 끝난 뒤 기본적으로 롤백된다. 그래서 테스트 데이터가 다른 테스트에 영향을 덜 준다.

## 테스트용 설정 파일

테스트 전용 설정은 다음 위치에 둘 수 있다.

```text
src/test/resources/application.yml
```

예:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: create-drop
```

`src/test/resources`의 설정은 테스트 실행 시 클래스패스에 포함된다.

## Profile을 사용한 테스트

테스트 전용 Profile을 만들 수도 있다.

```text
src/test/resources/application-test.yml
```

테스트 클래스:

```java
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TodoServiceTest {
}
```

`@ActiveProfiles("test")`를 붙이면 테스트에서 `test` Profile이 활성화된다.

## Mock Bean 사용

전체 Spring Context는 띄우되 특정 Bean만 가짜로 바꾸고 싶을 때가 있다.

Spring Framework의 테스트 지원에서는 Mockito 기반 Bean 대체 기능을 사용할 수 있다. Spring Boot 3.5 문서에서는 `@MockitoBean` 예제를 확인할 수 있다.

```java
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class TodoNotificationTest {

    @MockitoBean
    TodoNotifier todoNotifier;
}
```

가짜 Bean을 많이 쓰는 테스트는 설계를 다시 볼 신호일 수도 있다. 진짜 통합 테스트인지, 단위 테스트로 충분한지 판단한다.

## 웹 환경 옵션

`@SpringBootTest`는 웹 환경을 지정할 수 있다.

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
```

대표 옵션:

| 옵션 | 의미 |
| --- | --- |
| `MOCK` | 실제 서버를 띄우지 않고 Mock 웹 환경 사용 |
| `RANDOM_PORT` | 임의 포트로 실제 서버 실행 |
| `DEFINED_PORT` | 설정된 포트로 실제 서버 실행 |
| `NONE` | 웹 환경 없이 실행 |

처음에는 기본값인 `MOCK`으로 충분한 경우가 많다. 실제 HTTP 서버까지 띄워야 하면 `RANDOM_PORT`를 검토한다.

## `@SpringBootTest`의 비용

`@SpringBootTest`는 강력하지만 무겁다.

비용이 드는 이유:

- 많은 Bean을 생성한다.
- 자동 설정을 적용한다.
- DB 연결이나 외부 설정을 읽을 수 있다.
- 테스트 Context를 만드는 시간이 든다.

그래서 다음 기준을 추천한다.

- 순수 로직: 단위 테스트
- Controller: `@WebMvcTest`
- Repository: `@DataJpaTest`
- 전체 흐름: `@SpringBootTest`

## 자주 하는 실수

### 모든 테스트에 `@SpringBootTest`를 붙인다

테스트가 느려진다. 필요한 범위만 띄우는 테스트를 먼저 생각한다.

### 테스트 데이터가 서로 영향을 준다

테스트는 독립적이어야 한다. 트랜잭션 롤백, 데이터 초기화, 고유한 테스트 데이터 사용을 고려한다.

### 운영 설정으로 테스트한다

테스트는 테스트용 DB와 설정을 사용해야 한다. 실수로 운영 DB에 연결되지 않도록 Profile과 환경 변수를 조심한다.

## 확인 문제

1. `@SpringBootTest`는 무엇을 로드하는가?
2. `contextLoads` 테스트가 비어 있어도 의미가 있는 이유는 무엇인가?
3. 테스트 전용 Profile을 활성화할 때 사용하는 애너테이션은 무엇인가?
4. 모든 테스트를 `@SpringBootTest`로 만들면 어떤 문제가 생길 수 있는가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Testing Spring Boot Applications  
  https://docs.spring.io/spring-boot/3.5/reference/testing/spring-boot-applications.html

## 다음 장으로

다음 장에서는 Controller만 가볍게 테스트하는 `@WebMvcTest`와 슬라이스 테스트를 배운다.

