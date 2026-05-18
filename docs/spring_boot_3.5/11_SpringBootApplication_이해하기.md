# `@SpringBootApplication` 이해하기

## 이번 장에서 배울 것

이 장에서는 Spring Boot 애플리케이션의 시작점에 붙는 `@SpringBootApplication`을 자세히 살펴본다.

초보자는 이 애너테이션을 "그냥 붙이는 것"으로 외우기 쉽다. 하지만 이 애너테이션 하나가 자동 설정, 컴포넌트 스캔, 설정 클래스 등록을 함께 켜기 때문에 Spring Boot의 출발점이라고 할 수 있다.

## 기본 코드

Spring Initializr로 프로젝트를 만들면 보통 다음 코드가 생성된다.

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

여기서 가장 중요한 부분은 두 줄이다.

```java
@SpringBootApplication
SpringApplication.run(TodoApplication.class, args);
```

첫 번째 줄은 Spring Boot 애플리케이션임을 선언한다. 두 번째 줄은 실제로 애플리케이션을 실행한다.

## `@SpringBootApplication`이 하는 일

공식 문서 기준으로 `@SpringBootApplication`은 다음 세 가지 기능을 묶은 애너테이션이다.

```text
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
```

각각의 의미를 하나씩 보자.

## `@SpringBootConfiguration`

`@SpringBootConfiguration`은 이 클래스가 Spring Boot 설정 클래스임을 나타낸다.

일반 Spring의 `@Configuration`과 비슷하지만, Spring Boot 애플리케이션의 주요 설정 클래스를 찾을 때 도움이 된다.

초보자 관점에서는 이렇게 이해하면 충분하다.

"이 클래스는 Spring Boot 애플리케이션의 대표 설정 클래스다."

## `@EnableAutoConfiguration`

`@EnableAutoConfiguration`은 자동 설정을 켠다.

예를 들어 `spring-boot-starter-web`이 있으면 Spring Boot는 웹 애플리케이션에 필요한 설정을 자동으로 준비한다.

- 내장 Tomcat
- Spring MVC
- JSON 변환
- 기본 에러 처리
- 정적 리소스 처리

자동 설정은 무조건 모든 것을 켜는 기능이 아니다. 클래스패스, 설정 값, 이미 등록된 Bean 등을 보고 조건에 맞는 설정만 적용한다.

## `@ComponentScan`

`@ComponentScan`은 `@Component`, `@Service`, `@Repository`, `@Controller`, `@RestController` 같은 애너테이션이 붙은 클래스를 찾아 Bean으로 등록한다.

기본적으로 `@SpringBootApplication`이 붙은 클래스의 패키지와 그 하위 패키지를 스캔한다.

예를 들어 메인 클래스가 `com.example.todo` 패키지에 있다면 다음 구조가 좋다.

```text
com.example.todo
  TodoApplication
  todo
    TodoController
    TodoService
  config
    WebConfig
```

하지만 다음 구조는 좋지 않다.

```text
com.example.todo
  TodoApplication

com.example.web
  TodoController
```

`com.example.web`은 `com.example.todo`의 하위 패키지가 아니기 때문에 기본 스캔 대상에서 빠질 수 있다.

## 메인 클래스는 어디에 두는 것이 좋을까?

메인 클래스는 루트 패키지에 두는 것이 가장 좋다.

추천 구조:

```text
src/main/java/com/example/todo/TodoApplication.java
src/main/java/com/example/todo/controller/TodoController.java
src/main/java/com/example/todo/service/TodoService.java
src/main/java/com/example/todo/repository/TodoRepository.java
```

이렇게 두면 `com.example.todo` 아래의 모든 클래스를 자연스럽게 스캔할 수 있다.

## `SpringApplication.run()`은 무엇을 할까?

`SpringApplication.run()`은 Spring Boot 애플리케이션을 시작한다.

실행 과정은 단순화하면 다음과 같다.

1. 애플리케이션 타입을 판단한다.
2. 설정 파일을 읽는다.
3. Spring ApplicationContext를 만든다.
4. 자동 설정을 적용한다.
5. 컴포넌트를 스캔해서 Bean을 등록한다.
6. 내장 웹 서버를 시작한다.
7. 애플리케이션 실행 완료 로그를 출력한다.

이 과정이 끝나면 HTTP 요청을 받을 준비가 된다.

## 직접 분리해서 쓸 수도 있을까?

가능하다. `@SpringBootApplication` 대신 세 애너테이션을 직접 조합할 수도 있다.

```java
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
public class TodoApplication {
}
```

하지만 일반적인 Spring Boot 애플리케이션에서는 `@SpringBootApplication` 하나를 쓰는 것이 읽기 쉽다.

## 자주 하는 실수

### 메인 클래스를 너무 깊은 패키지에 둔다

메인 클래스를 `com.example.todo.app`에 두고 Controller를 `com.example.todo.controller`에 두면 Controller가 스캔되지 않을 수 있다. 메인 클래스는 가능하면 가장 위 패키지에 둔다.

### `@SpringBootApplication`을 여러 곳에 붙인다

일반적인 애플리케이션에서는 하나만 둔다. 테스트나 특수 설정에서 추가 설정 클래스를 만들 수는 있지만, 초보 단계에서는 메인 클래스 하나만 기준으로 잡는 것이 좋다.

### 자동 설정과 컴포넌트 스캔을 혼동한다

자동 설정은 Spring Boot가 조건에 따라 기본 Bean과 설정을 준비하는 일이다. 컴포넌트 스캔은 개발자가 만든 클래스를 찾아 Bean으로 등록하는 일이다.

## 확인 문제

1. `@SpringBootApplication`은 어떤 세 가지 기능을 묶고 있는가?
2. `@ComponentScan`은 기본적으로 어디를 스캔하는가?
3. 메인 클래스를 루트 패키지에 두는 이유는 무엇인가?
4. `SpringApplication.run()`이 하는 일을 간단히 설명해 보자.

## 참고 공식 자료

- Spring Boot 3.5 Reference: Using the `@SpringBootApplication` Annotation  
  https://docs.spring.io/spring-boot/3.5/reference/using/using-the-springbootapplication-annotation.html

## 다음 장으로

다음 장에서는 `application.yml`을 중심으로 설정 파일과 외부 설정을 배운다.

