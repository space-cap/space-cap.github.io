# WebMvcTest와 슬라이스 테스트

## 이번 장에서 배울 것

이 장에서는 슬라이스 테스트와 `@WebMvcTest`를 배운다.

`@SpringBootTest`는 애플리케이션 전체를 띄운다. 하지만 Controller 하나의 요청 매핑, 검증, 응답 JSON, 상태 코드만 확인하고 싶다면 전체 Context가 필요하지 않다.

이때 사용하는 것이 슬라이스 테스트다.

## 슬라이스 테스트란 무엇인가

슬라이스 테스트는 애플리케이션의 특정 계층만 잘라서 테스트하는 방식이다.

Spring Boot는 여러 테스트 슬라이스를 제공한다.

- `@WebMvcTest`: Spring MVC Controller 테스트
- `@DataJpaTest`: JPA Repository 테스트
- `@JsonTest`: JSON 직렬화/역직렬화 테스트
- `@JdbcTest`: JDBC 관련 테스트

이 장에서는 `@WebMvcTest`를 다룬다.

## `@WebMvcTest`

`@WebMvcTest`는 Spring MVC 테스트에 필요한 Bean만 로드한다.

주로 다음을 테스트한다.

- URL 매핑
- HTTP 메서드
- 요청 파라미터
- 요청 본문 JSON
- Bean Validation
- 응답 상태 코드
- 응답 JSON
- Controller Advice

Service, Repository, DB는 기본적으로 로드하지 않는다. Controller가 Service에 의존한다면 가짜 Bean으로 대체해야 한다.

## 예제 Controller

```java
package com.example.todo.todo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoResponse> findAll() {
        return todoService.findAll();
    }

    @GetMapping("/{id}")
    public TodoResponse findById(@PathVariable Long id) {
        return todoService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TodoResponse create(@Valid @RequestBody TodoCreateRequest request) {
        return todoService.create(request);
    }
}
```

## MockMvc란 무엇인가

`MockMvc`는 실제 서버를 띄우지 않고 Spring MVC 요청과 응답을 테스트하게 해주는 도구다.

실제 HTTP 포트를 열지 않지만, Controller 매핑과 JSON 변환, Validation 같은 Spring MVC 흐름을 검증할 수 있다.

## `@WebMvcTest` 작성

```java
package com.example.todo.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TodoService todoService;

    @Test
    void findAll() throws Exception {
        given(todoService.findAll()).willReturn(List.of(
                new TodoResponse(1L, "Spring Boot 공부", false),
                new TodoResponse(2L, "테스트 작성", true)
        ));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Spring Boot 공부"))
                .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    void create() throws Exception {
        TodoCreateRequest request = new TodoCreateRequest("새 할 일");

        given(todoService.create(request))
                .willReturn(new TodoResponse(1L, "새 할 일", false));

        mockMvc.perform(post("/todos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("새 할 일"));
    }
}
```

## 요청 검증 테스트

DTO:

```java
package com.example.todo.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoCreateRequest(
        @NotBlank(message = "제목은 비어 있을 수 없습니다.")
        @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
        String title
) {
}
```

테스트:

```java
@Test
void failWhenTitleIsBlank() throws Exception {
    TodoCreateRequest request = new TodoCreateRequest("");

    mockMvc.perform(post("/todos")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
}
```

이 테스트는 Service를 호출하기 전에 Validation이 동작하는지 확인한다.

## 예외 처리 테스트

`@RestControllerAdvice`가 있다면 Controller 테스트에서 에러 응답까지 확인할 수 있다.

```java
@Test
void return404WhenTodoNotFound() throws Exception {
    given(todoService.findById(999L))
            .willThrow(new TodoNotFoundException(999L));

    mockMvc.perform(get("/todos/{id}", 999L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("TODO_NOT_FOUND"));
}
```

만약 Advice가 자동으로 포함되지 않는 구조라면 `@Import(GlobalExceptionHandler.class)`로 명시할 수 있다.

## `@WebMvcTest`와 Service Mock

`@WebMvcTest`는 Controller 계층을 테스트한다. 따라서 Service의 실제 비즈니스 로직을 검증하는 자리가 아니다.

Service는 가짜로 두고 Controller의 역할만 확인한다.

Controller 테스트에서 확인할 것:

- 요청 경로가 맞는가
- 상태 코드가 맞는가
- 요청 DTO 검증이 되는가
- 응답 JSON 모양이 맞는가
- 예외 응답이 맞는가

Service 로직은 Service 테스트에서 따로 확인한다.

## 자주 하는 실수

### `@WebMvcTest`에서 Repository Bean이 없다고 당황한다

정상이다. `@WebMvcTest`는 웹 계층만 로드한다. 필요한 Service는 Mock으로 대체한다.

### Controller 테스트에서 비즈니스 로직까지 검증한다

Controller 테스트는 HTTP 경계에 집중한다.

### JSON 문자열을 직접 손으로 만든다

가능하면 `ObjectMapper`로 요청 객체를 JSON으로 변환한다. 오타와 이스케이프 문제를 줄일 수 있다.

## 확인 문제

1. 슬라이스 테스트란 무엇인가?
2. `@WebMvcTest`는 주로 어떤 계층을 테스트하는가?
3. `MockMvc`는 실제 서버를 띄우는가?
4. `@WebMvcTest`에서 Service 의존성은 보통 어떻게 처리하는가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Testing Spring Boot Applications  
  https://docs.spring.io/spring-boot/3.5/reference/testing/spring-boot-applications.html

## 다음 장으로

다음 장에서는 JPA Repository를 가볍게 테스트하는 `@DataJpaTest`를 배운다.

---

## 문서 이동

- [이전: 29. Spring Boot Test](./29_Spring_Boot_Test.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 31. DataJpaTest](./31_DataJpaTest.md)
