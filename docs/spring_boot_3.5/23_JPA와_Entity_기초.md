# JPA와 Entity 기초

## 이번 장에서 배울 것

이 장에서는 JPA와 Entity의 기본 개념을 배운다.

JDBC는 SQL을 직접 작성한다. 반면 JPA는 Java 객체와 관계형 DB 테이블을 매핑해서 객체 중심으로 데이터를 다룰 수 있게 해준다.

## JPA란 무엇인가

JPA는 Java Persistence API의 줄임말이다. Java 객체를 관계형 데이터베이스에 저장하고 조회하기 위한 표준 명세다.

중요한 점은 JPA가 구현체가 아니라 명세라는 것이다. 실제 구현체로는 Hibernate가 가장 널리 사용된다.

Spring Boot에서 `spring-boot-starter-data-jpa`를 추가하면 보통 Hibernate가 함께 들어온다.

## JPA가 해결하는 문제

JDBC로 데이터를 다루면 다음 작업을 반복해야 한다.

- SQL 작성
- ResultSet에서 값 꺼내기
- Java 객체로 매핑하기
- insert, update, delete SQL 직접 작성하기

JPA는 Entity 객체의 상태 변화를 추적하고, 필요한 SQL을 만들어 실행한다.

예를 들어 Entity의 제목을 바꾸면:

```java
todo.changeTitle("새 제목");
```

트랜잭션이 끝날 때 JPA가 변경을 감지해 update SQL을 실행할 수 있다.

## 의존성

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'com.h2database:h2'
}
```

`spring-boot-starter-data-jpa`에는 JPA, Hibernate, Spring Data JPA, JDBC 관련 의존성이 함께 포함된다.

## Entity 만들기

```java
package com.example.todo.todo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Todo() {
    }

    public Todo(String title) {
        this.title = title;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void complete() {
        this.completed = true;
    }
}
```

## Entity 애너테이션

### `@Entity`

이 클래스가 JPA Entity임을 나타낸다.

### `@Table`

매핑할 테이블 이름을 지정한다.

```java
@Table(name = "todos")
```

생략하면 클래스 이름을 기준으로 테이블 이름을 추론한다. 하지만 초보 단계에서는 명시하는 것이 이해하기 좋다.

### `@Id`

기본 키를 나타낸다.

### `@GeneratedValue`

ID 생성 전략을 지정한다.

`GenerationType.IDENTITY`는 DB의 자동 증가 기능을 사용한다.

### `@Column`

컬럼 제약을 지정한다.

```java
@Column(nullable = false, length = 100)
private String title;
```

이 설정은 JPA 매핑 정보다. 운영 DB 스키마 관리까지 전부 이것에 맡길지는 별도의 판단이 필요하다.

## 기본 생성자가 필요한 이유

JPA는 Entity 객체를 만들 때 기본 생성자를 필요로 한다.

```java
protected Todo() {
}
```

외부에서 마음대로 쓰지 못하게 `protected`로 두는 것이 일반적이다.

## Entity는 record로 만들지 않는다

DTO는 record로 만들기 좋다. 하지만 JPA Entity는 일반 클래스로 만드는 것이 좋다.

JPA Entity는 다음 특성이 필요하다.

- 기본 생성자
- 변경 감지
- 프록시 생성 가능성
- 식별자 관리

record는 불변 데이터 표현에 적합하지만 JPA Entity의 요구와 잘 맞지 않는다.

## JPA 설정

학습용 H2 설정:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:todo
    username: sa
    password:
    driver-class-name: org.h2.Driver

  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true

  h2:
    console:
      enabled: true
```

`ddl-auto: create-drop`은 애플리케이션 시작 시 테이블을 만들고 종료 시 삭제한다. 학습에는 편하지만 운영에서는 조심해야 한다.

운영에서는 보통 다음 중 하나를 사용한다.

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

또는:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none
```

스키마 변경은 Flyway나 Liquibase 같은 마이그레이션 도구로 관리하는 편이 안전하다.

## 영속성 컨텍스트

JPA를 이해하려면 영속성 컨텍스트를 알아야 한다.

영속성 컨텍스트는 JPA가 Entity를 관리하는 공간이다.

쉽게 말하면:

```text
DB에서 가져온 Entity를 JPA가 잠시 관리하는 작업 공간
```

이 공간 안에서 Entity의 값이 바뀌면 JPA가 변경을 감지할 수 있다. 트랜잭션이 끝날 때 필요한 SQL이 실행된다.

## 변경 감지

```java
@Transactional
public void changeTitle(Long id, String title) {
    Todo todo = todoRepository.findById(id)
            .orElseThrow();
    todo.changeTitle(title);
}
```

위 코드에는 `save`가 없다. 그래도 트랜잭션 안에서 조회한 Entity라면 JPA가 변경을 감지해 update SQL을 실행할 수 있다.

이것을 변경 감지 또는 dirty checking이라고 부른다.

## 자주 하는 실수

### Entity에 기본 생성자를 만들지 않는다

JPA Entity에는 기본 생성자가 필요하다. 보통 `protected`로 만든다.

### Entity를 record로 만든다

DTO는 record가 좋지만 JPA Entity는 일반 클래스로 만든다.

### 운영에서 `ddl-auto: update`를 무심코 쓴다

운영 DB 스키마를 애플리케이션 시작 시 자동 변경하는 것은 위험할 수 있다. 운영에서는 Flyway나 Liquibase를 검토한다.

## 확인 문제

1. JPA는 구현체인가, 명세인가?
2. JPA의 대표 구현체는 무엇인가?
3. JPA Entity에 기본 생성자가 필요한 이유는 무엇인가?
4. 변경 감지는 어떤 상황에서 동작하는가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: SQL Databases  
  https://docs.spring.io/spring-boot/3.5/reference/data/sql.html
- Spring Data JPA Reference: Getting Started  
  https://docs.spring.io/spring-data/jpa/reference/jpa/getting-started.html

## 다음 장으로

다음 장에서는 Spring Data JPA를 사용해 Repository 인터페이스만으로 기본 CRUD를 처리하는 방법을 배운다.

---

## 문서 이동

- [이전: 22. JDBC와 JdbcClient](./22_JDBC와_JdbcClient.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 24. Spring Data JPA](./24_Spring_Data_JPA.md)
