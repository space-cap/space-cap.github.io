# 검증 Validation

## 이번 장에서 배울 것

이 장에서는 요청 값 검증을 배운다.

API는 클라이언트를 믿으면 안 된다. 제목이 비어 있을 수도 있고, 너무 길 수도 있고, 숫자 범위를 벗어날 수도 있다. 잘못된 요청은 Service까지 들어가기 전에 Controller 입구에서 걸러내는 것이 좋다.

## Validation 의존성

Spring Initializr에서 `Validation`을 추가했다면 다음 의존성이 들어 있다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

이 의존성이 있어야 `@NotBlank`, `@Size`, `@Valid` 같은 검증 기능을 사용할 수 있다.

## 요청 DTO에 검증 규칙 붙이기

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

Spring Boot 3.x에서는 `javax.validation`이 아니라 `jakarta.validation` 패키지를 사용한다.

## Controller에서 `@Valid` 사용하기

DTO에 검증 규칙을 붙였다고 자동으로 검증되는 것은 아니다. 요청 본문 앞에 `@Valid`를 붙여야 한다.

```java
package com.example.todo.todo;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @PostMapping
    public TodoResponse create(@Valid @RequestBody TodoCreateRequest request) {
        return new TodoResponse(1L, request.title(), false);
    }
}
```

요청:

```bash
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"title":""}'
```

검증에 실패하면 Spring MVC는 예외를 발생시키고 기본 에러 응답을 반환한다. 다음 장에서는 이 에러 응답을 우리가 원하는 형식으로 바꾼다.

## 자주 쓰는 검증 애너테이션

| 애너테이션 | 의미 |
| --- | --- |
| `@NotNull` | null이면 안 됨 |
| `@NotEmpty` | null 또는 빈 값이면 안 됨 |
| `@NotBlank` | null, 빈 문자열, 공백 문자열이면 안 됨 |
| `@Size` | 문자열, 컬렉션 등의 크기 제한 |
| `@Min` | 숫자 최소값 |
| `@Max` | 숫자 최대값 |
| `@Positive` | 양수 |
| `@Email` | 이메일 형식 |
| `@Pattern` | 정규식 패턴 |

문자열 제목에는 보통 `@NotBlank`를 사용한다. `@NotEmpty`는 `"   "` 같은 공백 문자열을 허용할 수 있기 때문이다.

## 수정 요청 검증

```java
package com.example.todo.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoUpdateRequest(

        @NotBlank(message = "제목은 비어 있을 수 없습니다.")
        @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
        String title,

        boolean completed
) {
}
```

```java
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@PatchMapping("/{id}")
public TodoResponse update(
        @PathVariable Long id,
        @Valid @RequestBody TodoUpdateRequest request
) {
    return new TodoResponse(id, request.title(), request.completed());
}
```

## 중첩 객체 검증

DTO 안에 또 다른 DTO가 있으면 내부 객체에도 `@Valid`를 붙여야 한다.

```java
package com.example.todo.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(

        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @Valid
        AddressRequest address
) {
}
```

```java
package com.example.todo.user;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(

        @NotBlank(message = "도시는 필수입니다.")
        String city
) {
}
```

## PathVariable과 RequestParam 검증

요청 본문뿐 아니라 경로 변수와 쿼리 파라미터도 검증할 수 있다.

```java
package com.example.todo.todo;

import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/todos")
public class TodoController {

    @GetMapping("/{id}")
    public TodoResponse findById(@Positive @PathVariable Long id) {
        return new TodoResponse(id, "Spring Boot 공부", false);
    }
}
```

메서드 파라미터 검증에는 Controller 클래스에 `@Validated`가 필요하다.

## 검증은 어디까지 해야 할까?

검증은 두 종류로 나눌 수 있다.

- 형식 검증
- 비즈니스 검증

형식 검증은 DTO에서 처리하기 좋다.

```text
제목이 비어 있지 않은가?
제목이 100자 이하인가?
이메일 형식인가?
```

비즈니스 검증은 Service에서 처리하는 것이 좋다.

```text
이미 완료된 할 일을 다시 완료 처리할 수 있는가?
삭제된 할 일을 수정할 수 있는가?
해당 사용자가 이 할 일을 수정할 권한이 있는가?
```

DTO 검증에 모든 규칙을 넣으려 하면 코드가 어색해진다.

## 자주 하는 실수

### `@Valid`를 빼먹는다

DTO에 `@NotBlank`를 붙여도 Controller에서 `@Valid`를 붙이지 않으면 요청 본문 검증이 실행되지 않는다.

### `javax.validation`을 import한다

Spring Boot 3.x에서는 `jakarta.validation`을 사용한다.

### 모든 검증을 DTO에 넣는다

권한, 상태 전이 같은 비즈니스 규칙은 Service에서 처리하는 것이 자연스럽다.

## 확인 문제

1. Spring Boot 3.x에서 Validation 애너테이션은 어떤 패키지를 사용하는가?
2. 요청 본문 DTO 검증을 실행하려면 Controller 파라미터에 어떤 애너테이션을 붙여야 하는가?
3. `@NotEmpty`와 `@NotBlank`의 차이는 무엇인가?
4. DTO 검증과 Service 비즈니스 검증은 어떻게 나눌 수 있는가?

## 참고 공식 자료

- Spring Framework Reference: Validation  
  https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-validation.html

## 다음 장으로

다음 장에서는 검증 실패와 비즈니스 오류를 일관된 JSON 에러 응답으로 바꾸는 예외 처리를 배운다.

