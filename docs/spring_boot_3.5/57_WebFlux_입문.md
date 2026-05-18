# WebFlux 입문

## 이번 장에서 배울 것

이번 장에서는 Spring WebFlux가 무엇이고, 기존 Spring MVC와 어떻게 다른지 배운다.

WebFlux는 reactive, non-blocking 웹 애플리케이션을 만들기 위한 Spring의 웹 스택이다. 하지만 모든 프로젝트에 필요한 기술은 아니다. 이번 장의 목표는 "언제 WebFlux가 필요한지"를 구분하는 것이다.

## MVC와 WebFlux의 차이

Spring MVC는 전통적인 Servlet 기반 웹 프레임워크다.

```text
요청 하나 -> 스레드 하나가 처리 -> 응답
```

WebFlux는 reactive 기반으로 동작한다.

```text
요청 -> 이벤트 루프와 non-blocking I/O -> 응답
```

단순히 "WebFlux가 더 빠르다"라고 외우면 안 된다. 데이터베이스, 외부 API, 파일 I/O까지 모두 non-blocking으로 설계되어야 WebFlux의 장점이 살아난다.

## WebFlux가 어울리는 상황

WebFlux가 잘 맞는 상황:

- 외부 API 호출이 많고 기다리는 시간이 긴 서비스
- 서버 자원을 적게 쓰면서 많은 연결을 유지해야 하는 서비스
- streaming 응답이 필요한 서비스
- reactive database driver를 사용하는 서비스
- 팀이 Reactor와 reactive 개념에 익숙한 경우

WebFlux가 굳이 필요하지 않은 상황:

- 일반 CRUD API
- JPA 중심 애플리케이션
- 팀이 reactive에 익숙하지 않은 경우
- blocking 라이브러리가 많은 경우
- 단순한 관리자 페이지

초보자는 MVC를 충분히 익힌 뒤 WebFlux를 배우는 것이 좋다.

## 의존성 추가

Gradle:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
}
```

주의할 점이 있다. `spring-boot-starter-web`과 `spring-boot-starter-webflux`가 둘 다 있으면 Spring Boot는 기본적으로 Spring MVC를 자동 설정한다. 많은 개발자가 MVC 애플리케이션에서 `WebClient`만 쓰려고 WebFlux 의존성을 추가하기 때문이다.

정말 reactive 웹 애플리케이션으로 강제하고 싶다면 `WebApplicationType.REACTIVE`를 설정할 수 있다.

## Mono와 Flux

WebFlux에서 자주 보는 타입은 두 가지다.

`Mono<T>`:

```text
0개 또는 1개의 결과
```

`Flux<T>`:

```text
0개 이상의 여러 결과
```

예:

```java
Mono<TodoResponse> findOne(Long id)
Flux<TodoResponse> findAll()
```

처음에는 `Mono`는 Optional과 Future 사이 어딘가, `Flux`는 비동기 Stream처럼 느껴도 괜찮다. 정확한 이해는 Reactor를 공부하면서 깊어진다.

## 애너테이션 기반 컨트롤러

WebFlux에서도 `@RestController`를 사용할 수 있다.

```java
package com.example.todo.webflux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReactiveTodoController {

    @GetMapping("/reactive/todos/{id}")
    public Mono<TodoResponse> findById(@PathVariable Long id) {
        return Mono.just(new TodoResponse(id, "WebFlux 공부하기"));
    }

    @GetMapping("/reactive/todos")
    public Flux<TodoResponse> findAll() {
        return Flux.just(
                new TodoResponse(1L, "Mono 이해하기"),
                new TodoResponse(2L, "Flux 이해하기")
        );
    }
}
```

DTO:

```java
public record TodoResponse(
        Long id,
        String title
) {
}
```

## 함수형 라우터

WebFlux는 함수형 스타일도 지원한다.

```java
package com.example.todo.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TodoRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(TodoHandler handler) {
        return route(GET("/fn/todos"), handler::findAll);
    }
}
```

Handler:

```java
package com.example.todo.webflux;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TodoHandler {

    public Mono<ServerResponse> findAll(ServerRequest request) {
        Flux<TodoResponse> todos = Flux.just(
                new TodoResponse(1L, "함수형 라우터")
        );

        return ServerResponse.ok().body(todos, TodoResponse.class);
    }
}
```

초보 단계에서는 애너테이션 방식부터 시작해도 충분하다.

## WebClient와 WebFlux

`WebClient`는 WebFlux의 reactive HTTP client다. 하지만 MVC 애플리케이션에서도 외부 API 호출용으로 사용할 수 있다.

중요한 점:

```text
WebClient를 쓴다고 내 애플리케이션 전체가 WebFlux가 되는 것은 아니다.
```

Spring Boot 공식 문서도 MVC 애플리케이션에서 WebClient만 쓰기 위해 WebFlux starter를 추가하는 경우가 많다고 설명한다.

## Blocking 코드를 조심하자

WebFlux에서 가장 흔한 실수는 blocking 코드를 그대로 넣는 것이다.

좋지 않은 예:

```java
@GetMapping("/reactive/todos/{id}")
public Mono<TodoResponse> findById(@PathVariable Long id) {
    Todo todo = todoRepository.findById(id).orElseThrow(); // JPA blocking
    return Mono.just(TodoResponse.from(todo));
}
```

JPA는 blocking 방식이다. WebFlux 안에서 JPA를 직접 사용하면 reactive 이점이 줄어든다.

WebFlux를 제대로 쓰려면 R2DBC 같은 reactive 데이터 접근 기술을 함께 고려해야 한다.

## backpressure 맛보기

Reactive Streams의 중요한 개념 중 하나가 backpressure다.

쉽게 말하면:

```text
소비자가 처리할 수 있는 만큼만 생산자에게 달라고 요청하는 흐름 제어
```

데이터를 너무 빨리 밀어 넣으면 소비자가 감당하지 못한다. reactive 시스템은 이런 흐름을 제어하는 모델을 제공한다.

처음부터 깊게 이해하려고 애쓰지 않아도 된다. 다만 WebFlux는 단순한 컨트롤러 문법이 아니라 Reactive Streams 생태계 위에 있다는 점을 기억하자.

## 테스트

WebFlux 컨트롤러 테스트에는 `WebTestClient`를 사용할 수 있다.

```java
@WebFluxTest(ReactiveTodoController.class)
class ReactiveTodoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void findById() {
        webTestClient.get()
                .uri("/reactive/todos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }
}
```

## 자주 하는 실수

### WebFlux를 쓰면 무조건 성능이 좋아진다고 생각한다

blocking 코드가 많으면 오히려 복잡도만 증가할 수 있다.

### JPA와 WebFlux를 무심코 섞는다

JPA는 blocking이다. 일반 CRUD 서비스라면 MVC + JPA가 더 단순하다.

### Mono와 Flux를 단순한 값처럼 다룬다

`Mono<T>`는 `T`가 아니다. reactive 흐름을 표현하는 타입이다.

### MVC와 WebFlux starter를 함께 넣고 왜 MVC로 뜨는지 헷갈린다

둘 다 있으면 Spring Boot는 기본적으로 MVC를 선택한다.

## 확인 문제

1. `Mono`와 `Flux`는 각각 어떤 의미인가?
2. Spring MVC와 WebFlux의 큰 차이는 무엇인가?
3. WebFlux에서 blocking 코드를 조심해야 하는 이유는 무엇인가?
4. `spring-boot-starter-web`과 `spring-boot-starter-webflux`가 둘 다 있으면 기본적으로 무엇이 선택되는가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Reactive Web Applications  
  https://docs.spring.io/spring-boot/3.5/reference/web/reactive.html

## 다음 장으로

다음 장에서는 Spring Boot의 자동 설정을 직접 만들어 보는 커스텀 Auto Configuration을 배운다.
