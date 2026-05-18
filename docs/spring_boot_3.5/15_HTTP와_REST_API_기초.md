# HTTP와 REST API 기초

## 이번 장에서 배울 것

이 장에서는 Spring Boot로 REST API를 만들기 전에 알아야 할 HTTP 기본기를 배운다.

Controller 코드를 쓰는 것은 어렵지 않다. 하지만 HTTP 메서드, 상태 코드, 요청 본문, 응답 본문을 모르면 API가 왜 그렇게 설계되는지 이해하기 어렵다.

## HTTP란 무엇인가

HTTP는 클라이언트와 서버가 요청과 응답을 주고받는 규칙이다.

웹 브라우저, 모바일 앱, 프론트엔드 애플리케이션, 다른 서버는 HTTP 요청을 보낸다. Spring Boot 애플리케이션은 그 요청을 받아 처리하고 HTTP 응답을 돌려준다.

단순화하면 흐름은 다음과 같다.

```text
Client -> HTTP Request -> Spring Boot Server
Client <- HTTP Response <- Spring Boot Server
```

## 요청의 구성

HTTP 요청에는 보통 다음 요소가 있다.

- Method
- URL
- Header
- Body

예:

```http
POST /todos HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "title": "Spring Boot 공부"
}
```

각 의미는 다음과 같다.

- `POST`: 어떤 동작을 원하는지 나타내는 메서드
- `/todos`: 요청 대상 경로
- `Content-Type`: 요청 본문의 형식
- JSON 본문: 서버에 보낼 데이터

## 응답의 구성

HTTP 응답에는 보통 다음 요소가 있다.

- Status Code
- Header
- Body

예:

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "title": "Spring Boot 공부",
  "completed": false
}
```

`201 Created`는 새 리소스가 생성됐다는 뜻이다.

## HTTP 메서드

REST API에서 자주 쓰는 HTTP 메서드는 다음과 같다.

| 메서드 | 주 용도 | 예 |
| --- | --- | --- |
| `GET` | 조회 | 할 일 목록 조회 |
| `POST` | 생성 | 새 할 일 등록 |
| `PUT` | 전체 수정 또는 대체 | 할 일 전체 수정 |
| `PATCH` | 일부 수정 | 완료 여부만 변경 |
| `DELETE` | 삭제 | 할 일 삭제 |

중요한 것은 URL에 동사를 넣지 않는 것이다.

좋은 예:

```text
GET /todos
POST /todos
GET /todos/1
PATCH /todos/1
DELETE /todos/1
```

아쉬운 예:

```text
GET /getTodos
POST /createTodo
POST /deleteTodo
```

HTTP 메서드가 동작을 나타내고, URL은 대상을 나타내는 것이 REST 스타일에 가깝다.

## 리소스란 무엇인가

REST에서 리소스는 API가 다루는 대상이다.

할 일 API의 리소스는 `todo`다. 보통 URL에서는 복수형을 사용한다.

```text
/todos
/todos/1
```

사용자라면:

```text
/users
/users/10
```

게시글이라면:

```text
/posts
/posts/100
```

## 상태 코드

상태 코드는 요청 처리 결과를 숫자로 표현한다.

| 상태 코드 | 의미 | 사용 예 |
| --- | --- | --- |
| `200 OK` | 성공 | 조회, 수정 성공 |
| `201 Created` | 생성 성공 | 새 할 일 생성 |
| `204 No Content` | 성공했지만 응답 본문 없음 | 삭제 성공 |
| `400 Bad Request` | 요청이 잘못됨 | 필수 값 누락 |
| `401 Unauthorized` | 인증 필요 | 로그인하지 않음 |
| `403 Forbidden` | 권한 없음 | 접근 권한 부족 |
| `404 Not Found` | 리소스 없음 | 없는 할 일 ID 조회 |
| `409 Conflict` | 상태 충돌 | 이미 존재하는 값 |
| `500 Internal Server Error` | 서버 오류 | 예상하지 못한 예외 |

초보자는 처음에 `200`, `201`, `400`, `404`, `500`만 확실히 알아도 좋다.

## JSON

REST API는 데이터를 주고받을 때 JSON을 많이 사용한다.

예:

```json
{
  "id": 1,
  "title": "Spring Boot 공부",
  "completed": false
}
```

JSON은 다음 타입을 표현할 수 있다.

- 문자열
- 숫자
- 불리언
- 배열
- 객체
- null

Spring Boot는 Jackson을 사용해 Java 객체와 JSON을 서로 변환한다.

## Spring MVC 애너테이션 맛보기

다음 장부터 자세히 보겠지만, HTTP 요소는 Spring MVC 애너테이션과 연결된다.

```java
@RestController
@RequestMapping("/todos")
public class TodoController {

    @GetMapping
    public List<TodoResponse> findAll() {
        return List.of();
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

각 애너테이션의 의미는 다음과 같다.

- `@RestController`: REST API Controller
- `@RequestMapping("/todos")`: 공통 URL 경로
- `@GetMapping`: GET 요청 처리
- `@PostMapping`: POST 요청 처리
- `@PathVariable`: URL 경로의 값을 메서드 인자로 받기
- `@RequestBody`: 요청 본문 JSON을 Java 객체로 받기

## 간단한 DTO 예제

Java 17 이상에서는 record를 DTO로 사용할 수 있다.

```java
public record TodoCreateRequest(
        String title
) {
}
```

```java
public record TodoResponse(
        Long id,
        String title,
        boolean completed
) {
}
```

record는 불변 데이터를 표현하기 좋다. 처음에는 요청 DTO와 응답 DTO에 사용하기 쉽다.

## curl로 요청해 보기

GET 요청:

```bash
curl http://localhost:8080/todos
```

POST 요청:

```bash
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Spring Boot 공부"}'
```

Windows PowerShell에서는 처음에 한 줄로 실행하는 편이 덜 헷갈린다.

```powershell
curl.exe -X POST http://localhost:8080/todos -H "Content-Type: application/json" -d "{\"title\":\"Spring Boot 공부\"}"
```

## REST API 설계의 첫 원칙

처음에는 다음 원칙만 지켜도 충분하다.

- URL은 명사 중심으로 쓴다.
- 동작은 HTTP 메서드로 표현한다.
- 요청과 응답은 JSON으로 주고받는다.
- 성공과 실패를 상태 코드로 구분한다.
- 에러 응답 형식을 일관되게 만든다.

## 자주 하는 실수

### 모든 요청을 POST로 만든다

조회는 `GET`, 생성은 `POST`, 삭제는 `DELETE`처럼 HTTP 메서드의 의미를 살린다.

### URL에 동사를 넣는다

`/createTodo`보다 `POST /todos`가 REST 스타일에 가깝다.

### 실패해도 200을 반환한다

요청이 잘못됐으면 `400`, 리소스가 없으면 `404`, 서버 오류면 `500`처럼 상태 코드를 구분한다.

### 요청과 응답 DTO를 구분하지 않는다

처음에는 같은 객체를 써도 되는 것처럼 보이지만, 프로젝트가 커지면 요청과 응답의 모양이 달라진다. DTO를 분리하는 습관이 좋다.

## 확인 문제

1. HTTP 요청의 주요 구성 요소 네 가지는 무엇인가?
2. 새 리소스를 만들 때 주로 사용하는 HTTP 메서드와 상태 코드는 무엇인가?
3. `GET /todos/1`에서 `1`을 Spring MVC에서 받기 위해 사용하는 애너테이션은 무엇인가?
4. URL에 동사를 넣는 방식보다 HTTP 메서드를 활용하는 방식이 좋은 이유는 무엇인가?

## 참고 공식 자료

- Spring Framework Reference: Mapping Requests  
  https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html
- Spring Framework Reference: `@ResponseBody`  
  https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/responsebody.html

## 다음 장으로

다음 장에서는 `Controller`와 `RestController`를 본격적으로 다룬다. HTTP 기본기를 Spring MVC 코드와 연결해 보자.

