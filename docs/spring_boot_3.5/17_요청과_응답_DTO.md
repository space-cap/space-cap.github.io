# 요청과 응답 DTO

## 이번 장에서 배울 것

이 장에서는 DTO를 배운다.

DTO는 Data Transfer Object의 줄임말이다. 이름 그대로 데이터를 옮기기 위한 객체다. REST API에서는 클라이언트가 보낸 JSON을 받을 때, 서버가 JSON 응답을 돌려줄 때 DTO를 자주 사용한다.

## DTO가 필요한 이유

처음에는 Entity나 도메인 객체를 그대로 요청과 응답에 사용하고 싶어진다.

하지만 실무에서는 요청, 응답, 내부 모델의 모양이 서로 다르다.

예를 들어 할 일을 생성할 때 클라이언트가 보내는 값은 제목뿐일 수 있다.

```json
{
  "title": "Spring Boot 공부"
}
```

하지만 서버가 응답할 때는 ID, 완료 여부, 생성 시각을 함께 내려줄 수 있다.

```json
{
  "id": 1,
  "title": "Spring Boot 공부",
  "completed": false,
  "createdAt": "2026-05-19T10:30:00"
}
```

요청과 응답의 모양이 다르므로 DTO를 분리하는 것이 자연스럽다.

## 요청 DTO

요청 DTO는 클라이언트가 서버에 보내는 데이터를 담는다.

```java
package com.example.todo.todo;

public record TodoCreateRequest(
        String title
) {
}
```

수정 요청은 생성 요청과 다를 수 있다.

```java
package com.example.todo.todo;

public record TodoUpdateRequest(
        String title,
        boolean completed
) {
}
```

완료 여부만 바꾸는 요청이라면 더 작게 만들 수도 있다.

```java
package com.example.todo.todo;

public record TodoCompleteRequest(
        boolean completed
) {
}
```

## 응답 DTO

응답 DTO는 서버가 클라이언트에 돌려주는 데이터를 담는다.

```java
package com.example.todo.todo;

import java.time.LocalDateTime;

public record TodoResponse(
        Long id,
        String title,
        boolean completed,
        LocalDateTime createdAt
) {
}
```

목록 응답에서 모든 필드가 필요하지 않다면 목록 전용 DTO를 만들 수도 있다.

```java
package com.example.todo.todo;

public record TodoSummaryResponse(
        Long id,
        String title,
        boolean completed
) {
}
```

DTO는 API 계약을 표현한다. 클라이언트와 서버가 주고받는 약속이므로 이름과 필드를 신중하게 정한다.

## record를 DTO로 사용하기

Java 17 이상에서는 `record`를 DTO로 쓰기 좋다.

```java
public record TodoResponse(
        Long id,
        String title,
        boolean completed
) {
}
```

record는 다음 코드를 자동으로 만들어 준다.

- 생성자
- getter와 비슷한 접근 메서드
- `equals`
- `hashCode`
- `toString`

record의 접근 메서드는 `getTitle()`이 아니라 `title()`이다.

```java
TodoResponse response = new TodoResponse(1L, "공부", false);
String title = response.title();
```

## Controller에서 DTO 사용하기

```java
package com.example.todo.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @GetMapping
    public List<TodoSummaryResponse> findAll() {
        return List.of(
                new TodoSummaryResponse(1L, "Spring Boot 공부", false),
                new TodoSummaryResponse(2L, "DTO 정리", true)
        );
    }

    @GetMapping("/{id}")
    public TodoResponse findById(@PathVariable Long id) {
        return new TodoResponse(
                id,
                "Spring Boot 공부",
                false,
                LocalDateTime.now()
        );
    }

    @PostMapping
    public TodoResponse create(@RequestBody TodoCreateRequest request) {
        return new TodoResponse(
                1L,
                request.title(),
                false,
                LocalDateTime.now()
        );
    }
}
```

## Entity를 그대로 노출하지 않기

나중에 JPA를 배우면 `Todo` Entity가 등장한다.

나쁜 예:

```java
@GetMapping("/{id}")
public Todo findById(@PathVariable Long id) {
    return todoService.findById(id);
}
```

이 방식은 간단하지만 문제가 생긴다.

- Entity의 내부 구조가 API에 그대로 노출된다.
- 민감한 필드가 실수로 응답될 수 있다.
- JPA 연관관계 때문에 JSON 변환 문제가 생길 수 있다.
- API 변경과 DB 모델 변경이 강하게 묶인다.

좋은 방향:

```java
@GetMapping("/{id}")
public TodoResponse findById(@PathVariable Long id) {
    Todo todo = todoService.findById(id);
    return TodoResponse.from(todo);
}
```

## 정적 팩터리 메서드

DTO 변환 로직이 반복되면 DTO 안에 정적 메서드를 둘 수 있다.

```java
package com.example.todo.todo;

import java.time.LocalDateTime;

public record TodoResponse(
        Long id,
        String title,
        boolean completed,
        LocalDateTime createdAt
) {

    public static TodoResponse of(
            Long id,
            String title,
            boolean completed,
            LocalDateTime createdAt
    ) {
        return new TodoResponse(id, title, completed, createdAt);
    }
}
```

나중에 Entity가 생기면 `from(Todo todo)` 형태로 바꿀 수 있다.

## 날짜와 시간 응답

Spring Boot는 기본적으로 Jackson을 통해 `LocalDateTime`을 JSON 문자열로 변환한다.

응답 예:

```json
{
  "id": 1,
  "title": "Spring Boot 공부",
  "completed": false,
  "createdAt": "2026-05-19T10:30:00"
}
```

실무에서는 시간대와 포맷 정책을 팀에서 정해야 한다. 초보 단계에서는 ISO-8601 형태의 문자열 응답에 익숙해지면 된다.

## DTO 이름 짓기

추천 이름:

```text
TodoCreateRequest
TodoUpdateRequest
TodoResponse
TodoSummaryResponse
TodoDetailResponse
```

피하고 싶은 이름:

```text
TodoDto
TodoVo
TodoData
```

항상 나쁜 것은 아니지만, 요청용인지 응답용인지 이름에서 드러나지 않는다.

## 자주 하는 실수

### 요청 DTO와 응답 DTO를 하나로 합친다

처음에는 편하지만 곧 필드가 어긋난다. 생성 요청에는 `id`가 없고, 응답에는 `id`가 있다.

### Entity를 그대로 응답한다

작은 예제에서는 동작하지만 실무 프로젝트에서는 유지보수와 보안 문제가 생긴다.

### record 접근자를 getter처럼 부른다

record는 `request.getTitle()`이 아니라 `request.title()`이다.

## 확인 문제

1. DTO는 무엇의 약자인가?
2. 요청 DTO와 응답 DTO를 분리하는 이유는 무엇인가?
3. Java record의 접근 메서드는 어떤 형태인가?
4. Entity를 API 응답으로 그대로 노출하면 어떤 문제가 생길 수 있는가?

## 다음 장으로

다음 장에서는 요청 DTO에 검증 규칙을 붙여 잘못된 요청을 거절하는 방법을 배운다.

