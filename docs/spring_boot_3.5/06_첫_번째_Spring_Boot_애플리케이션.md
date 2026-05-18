# 첫 번째 Spring Boot 애플리케이션

## 이번 장에서 배울 것

이 장에서는 첫 REST API를 만든다. 목표는 단순하다.

브라우저나 curl로 다음 주소에 요청했을 때 문자열 응답을 받는다.

```text
http://localhost:8080/hello
```

## 기본 애플리케이션 클래스

Spring Initializr로 프로젝트를 만들면 다음과 비슷한 클래스가 있다.

```java
package com.example.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }
}
```

이 클래스가 애플리케이션의 시작점이다.

`main` 메서드를 실행하면 Spring Boot가 시작되고, 내장 Tomcat이 뜨고, Spring Container가 Bean을 준비한다.

## Hello Controller 만들기

다음 파일을 만든다.

```text
src/main/java/com/example/todo/HelloController.java
```

내용은 다음과 같다.

```java
package com.example.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot 3.5";
    }
}
```

## 코드 설명

### `@RestController`

이 클래스가 HTTP 요청을 처리하는 Controller임을 Spring에게 알려준다.

`@RestController`를 사용하면 메서드의 반환값이 View 이름이 아니라 HTTP 응답 본문으로 사용된다. 문자열을 반환하면 문자열이 그대로 응답된다. 객체를 반환하면 보통 JSON으로 변환된다.

### `@GetMapping("/hello")`

HTTP GET 요청 중 `/hello` 경로로 들어오는 요청을 이 메서드에 연결한다.

즉 다음 요청은 `hello()` 메서드가 처리한다.

```text
GET /hello
```

### `return "Hello Spring Boot 3.5";`

응답 본문으로 사용할 문자열이다.

## 실행하기

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

macOS 또는 Linux:

```bash
./gradlew bootRun
```

실행 후 브라우저에서 접속한다.

```text
http://localhost:8080/hello
```

또는 curl을 사용한다.

```bash
curl http://localhost:8080/hello
```

예상 응답:

```text
Hello Spring Boot 3.5
```

## JSON 응답 만들기

문자열만 반환하면 API 느낌이 약하다. 간단한 JSON 응답도 만들어 보자.

```java
package com.example.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot 3.5";
    }

    @GetMapping("/hello-json")
    public Map<String, Object> helloJson() {
        return Map.of(
                "message", "Hello Spring Boot 3.5",
                "time", LocalDateTime.now()
        );
    }
}
```

요청:

```bash
curl http://localhost:8080/hello-json
```

응답은 다음과 비슷하다.

```json
{
  "message": "Hello Spring Boot 3.5",
  "time": "2026-05-18T23:59:00.123456"
}
```

Spring Boot는 `spring-boot-starter-web`에 포함된 Jackson을 사용해서 Java 객체를 JSON으로 바꿔 준다.

## 자주 하는 실수

### Controller 패키지 위치가 이상하다

`TodoApplication`이 `com.example.todo` 패키지에 있다면 Controller도 그 하위 패키지에 두는 것이 좋다.

예:

```text
com.example.todo
com.example.todo.controller
```

`com.other` 같은 완전히 다른 패키지에 두면 컴포넌트 스캔 대상에서 빠질 수 있다.

### 포트 8080이 이미 사용 중이다

다른 프로그램이 8080 포트를 쓰고 있으면 실행에 실패한다. 임시로 포트를 바꾸려면 `src/main/resources/application.yml`에 다음을 넣는다.

```yaml
server:
  port: 8081
```

### `curl` 요청은 성공했는데 브라우저는 캐시된 화면을 보여준다

API 확인은 가능하면 curl이나 HTTP Client를 사용한다.

## 확인 문제

1. `@RestController`는 어떤 역할을 하는가?
2. `@GetMapping("/hello")`는 어떤 요청을 어떤 메서드에 연결하는가?
3. Java 객체가 JSON으로 변환되는 데 관여하는 대표 라이브러리는 무엇인가?
4. Controller가 스캔되지 않을 때 가장 먼저 확인할 것은 무엇인가?

## 다음 장으로

다음 장에서는 프로젝트 폴더 구조를 읽는 법을 배운다. Spring Boot 프로젝트는 파일 위치가 곧 의미다.

---

## 문서 이동

- [이전: 05. Spring Initializr로 프로젝트 만들기](./05_Spring_Initializr로_프로젝트_만들기.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 07. 프로젝트 구조 읽는 법](./07_프로젝트_구조_읽는_법.md)
