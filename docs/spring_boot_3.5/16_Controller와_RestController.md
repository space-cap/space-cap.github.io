# Controller와 RestController

## 이번 장에서 배울 것

이 장에서는 Spring MVC에서 HTTP 요청을 받는 Controller를 배운다.

Spring Boot로 REST API를 만든다는 것은 결국 "HTTP 요청을 Java 메서드에 연결하고, Java 객체를 HTTP 응답으로 돌려주는 일"이다. 그 중심에 `@RestController`가 있다.

## Controller의 역할

Controller는 HTTP 요청과 애플리케이션 내부 로직 사이의 입구다.

Controller가 담당하는 일은 다음과 같다.

- 요청 URL과 HTTP 메서드를 받는다.
- 경로 변수, 쿼리 파라미터, 요청 본문을 읽는다.
- Service를 호출한다.
- 적절한 응답 상태 코드와 응답 본문을 돌려준다.

Controller가 하지 않는 편이 좋은 일도 있다.

- 복잡한 비즈니스 규칙 처리
- 데이터베이스 직접 접근
- 너무 긴 조건문으로 정책 처리

이런 일은 Service와 Repository로 나누어야 한다.

## `@Controller`와 `@RestController`

`@Controller`는 주로 HTML 화면을 반환하는 MVC Controller에 사용한다.

`@RestController`는 REST API처럼 응답 본문을 직접 반환하는 Controller에 사용한다.

`@RestController`는 다음 조합과 비슷하다.

```text
@Controller + @ResponseBody
```

즉 메서드가 반환한 객체나 문자열이 View 이름이 아니라 HTTP 응답 본문으로 사용된다.

REST API를 만들 때는 대부분 `@RestController`를 사용한다.

## 첫 Todo Controller

다음 파일을 만든다.

```text
src/main/java/com/example/todo/todo/TodoController.java
```

```java
package com.example.todo.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @GetMapping
    public List<TodoResponse> findAll() {
        return List.of(
                new TodoResponse(1L, "Spring Boot 공부", false),
                new TodoResponse(2L, "REST API 만들기", true)
        );
    }

    @GetMapping("/{id}")
    public TodoResponse findById(@PathVariable Long id) {
        return new TodoResponse(id, "Spring Boot 공부", false);
    }

    @PostMapping
    public TodoResponse create(@RequestBody TodoCreateRequest request) {
        return new TodoResponse(1L, request.title(), false);
    }
}
```

같은 패키지에 DTO를 만든다.

```java
package com.example.todo.todo;

public record TodoCreateRequest(
        String title
) {
}
```

```java
package com.example.todo.todo;

public record TodoResponse(
        Long id,
        String title,
        boolean completed
) {
}
```

## `@RequestMapping`

클래스 위의 `@RequestMapping("/todos")`는 공통 경로를 지정한다.

```java
@RestController
@RequestMapping("/todos")
public class TodoController {
}
```

이 Controller 안의 모든 메서드는 `/todos`로 시작하는 요청을 처리한다.

## `@GetMapping`

`@GetMapping`은 HTTP GET 요청을 처리한다.

```java
@GetMapping
public List<TodoResponse> findAll() {
    return List.of();
}
```

이 메서드는 다음 요청을 처리한다.

```text
GET /todos
```

## `@PathVariable`

경로에 포함된 값을 메서드 인자로 받을 때 사용한다.

```java
@GetMapping("/{id}")
public TodoResponse findById(@PathVariable Long id) {
    return new TodoResponse(id, "Spring Boot 공부", false);
}
```

요청:

```text
GET /todos/10
```

이때 `id` 값은 `10`이 된다.

## `@RequestBody`

요청 본문 JSON을 Java 객체로 받을 때 사용한다.

```java
@PostMapping
public TodoResponse create(@RequestBody TodoCreateRequest request) {
    return new TodoResponse(1L, request.title(), false);
}
```

요청:

```bash
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Spring Boot 공부"}'
```

Spring MVC는 JSON을 `TodoCreateRequest` 객체로 변환해 메서드에 전달한다.

## `@RequestParam`

쿼리 파라미터를 받을 때 사용한다.

```java
@GetMapping("/search")
public List<TodoResponse> search(@RequestParam String keyword) {
    return List.of(new TodoResponse(1L, keyword, false));
}
```

요청:

```text
GET /todos/search?keyword=spring
```

`keyword` 값은 `spring`이 된다.

필수가 아닌 값으로 만들려면 다음처럼 쓴다.

```java
@RequestParam(defaultValue = "") String keyword
```

## 상태 코드 제어하기

생성 API는 보통 `201 Created`를 반환하는 것이 좋다.

간단히 `@ResponseStatus`를 사용할 수 있다.

```java
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CREATED)
@PostMapping
public TodoResponse create(@RequestBody TodoCreateRequest request) {
    return new TodoResponse(1L, request.title(), false);
}
```

더 세밀하게 제어하려면 `ResponseEntity`를 사용한다.

```java
import org.springframework.http.ResponseEntity;
import java.net.URI;

@PostMapping
public ResponseEntity<TodoResponse> create(@RequestBody TodoCreateRequest request) {
    TodoResponse response = new TodoResponse(1L, request.title(), false);
    return ResponseEntity
            .created(URI.create("/todos/" + response.id()))
            .body(response);
}
```

처음에는 `@ResponseStatus`로 시작하고, 헤더까지 제어해야 할 때 `ResponseEntity`를 쓰면 된다.

## Controller와 Service 분리

Controller가 모든 일을 직접 하면 금방 복잡해진다.

좋은 흐름은 다음과 같다.

```text
HTTP 요청 -> Controller -> Service -> Repository
HTTP 응답 <- Controller <- Service <- Repository
```

초기 예제는 Controller만으로도 동작하지만, 다음 장 이후부터는 Service를 분리하는 습관을 들인다.

## 자주 하는 실수

### `@Controller`를 붙이고 JSON이 안 나온다

REST API에서는 `@RestController`를 사용한다. `@Controller`만 쓰면 반환 문자열을 View 이름으로 해석할 수 있다.

### `@RequestBody`를 빼먹는다

POST 요청의 JSON 본문을 객체로 받으려면 `@RequestBody`가 필요하다.

### URL 경로와 애너테이션 경로를 중복한다

클래스에 `@RequestMapping("/todos")`가 있는데 메서드에 또 `@GetMapping("/todos")`를 쓰면 실제 경로가 `/todos/todos`가 된다.

## 확인 문제

1. `@Controller`와 `@RestController`의 차이는 무엇인가?
2. `GET /todos/3`에서 `3`을 받으려면 어떤 애너테이션을 쓰는가?
3. 요청 본문 JSON을 Java 객체로 받을 때 쓰는 애너테이션은 무엇인가?
4. Controller에 비즈니스 로직을 많이 넣으면 어떤 문제가 생길까?

## 참고 공식 자료

- Spring Framework Reference: Mapping Requests  
  https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html
- Spring Framework Reference: `@RequestBody`  
  https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/requestbody.html

## 다음 장으로

다음 장에서는 요청 DTO와 응답 DTO를 나누는 법을 배운다.

