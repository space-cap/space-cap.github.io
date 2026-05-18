# Spring Data JPA

## 이번 장에서 배울 것

이 장에서는 Spring Data JPA를 배운다.

JPA만 사용해도 JDBC보다 편리하지만, Repository 구현 코드를 직접 작성해야 하는 부분이 있다. Spring Data JPA는 Repository 인터페이스만으로 기본 CRUD 구현체를 자동으로 만들어 준다.

## Spring Data JPA가 하는 일

Spring Data JPA는 다음을 제공한다.

- 기본 CRUD 메서드
- 페이징과 정렬
- 메서드 이름 기반 쿼리 생성
- `@Query`를 통한 직접 쿼리 작성
- Repository 구현체 자동 생성

개발자는 인터페이스만 선언한다.

```java
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
```

그러면 Spring Data JPA가 런타임에 구현체를 만들어 Bean으로 등록한다.

## 의존성

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'com.h2database:h2'
}
```

## Repository 만들기

```java
package com.example.todo.todo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
```

`JpaRepository<Todo, Long>`에서 첫 번째 타입은 Entity, 두 번째 타입은 ID 타입이다.

이제 다음 메서드를 바로 사용할 수 있다.

- `save`
- `findById`
- `findAll`
- `deleteById`
- `existsById`
- `count`

## Service에서 사용하기

```java
package com.example.todo.todo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> findAll() {
        return todoRepository.findAll()
                .stream()
                .map(TodoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TodoResponse findById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        return TodoResponse.from(todo);
    }

    @Transactional
    public TodoResponse create(TodoCreateRequest request) {
        Todo todo = new Todo(request.title());
        Todo saved = todoRepository.save(todo);
        return TodoResponse.from(saved);
    }

    @Transactional
    public TodoResponse complete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        todo.complete();
        return TodoResponse.from(todo);
    }
}
```

`complete` 메서드에는 `save`가 없다. 트랜잭션 안에서 조회한 Entity를 변경했기 때문에 JPA 변경 감지가 동작한다.

## DTO 변환

```java
package com.example.todo.todo;

public record TodoResponse(
        Long id,
        String title,
        boolean completed
) {

    public static TodoResponse from(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.isCompleted()
        );
    }
}
```

Entity를 그대로 Controller 응답으로 내보내지 않고 DTO로 변환한다.

## 메서드 이름으로 쿼리 만들기

Spring Data JPA는 메서드 이름을 분석해 쿼리를 만들 수 있다.

```java
package com.example.todo.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByCompleted(boolean completed);

    List<Todo> findByTitleContaining(String keyword);

    boolean existsByTitle(String title);
}
```

사용 예:

```java
@Transactional(readOnly = true)
public List<TodoResponse> findCompletedTodos() {
    return todoRepository.findByCompleted(true)
            .stream()
            .map(TodoResponse::from)
            .toList();
}
```

메서드 이름이 길어지고 복잡해지면 `@Query`나 Querydsl 같은 다른 방식을 검토한다.

## `@Query` 사용하기

```java
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("""
            select t
            from Todo t
            where t.title like concat('%', :keyword, '%')
            order by t.id desc
            """)
    List<Todo> search(@Param("keyword") String keyword);
}
```

여기서 쿼리는 SQL이 아니라 JPQL이다. JPQL은 테이블이 아니라 Entity와 필드를 대상으로 쓴다.

```text
테이블명 todos -> Entity명 Todo
컬럼명 created_at -> 필드명 createdAt
```

## 페이징과 정렬

Spring Data JPA는 페이징을 쉽게 처리한다.

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Page<Todo> findByCompleted(boolean completed, Pageable pageable);
}
```

Service:

```java
@Transactional(readOnly = true)
public Page<TodoResponse> findByCompleted(
        boolean completed,
        Pageable pageable
) {
    return todoRepository.findByCompleted(completed, pageable)
            .map(TodoResponse::from);
}
```

Controller:

```java
@GetMapping("/search")
public Page<TodoResponse> search(
        @RequestParam boolean completed,
        Pageable pageable
) {
    return todoService.findByCompleted(completed, pageable);
}
```

요청 예:

```text
GET /todos/search?completed=false&page=0&size=10&sort=id,desc
```

실무에서는 `Page`를 그대로 응답하기보다 별도 응답 DTO로 감싸기도 한다.

## 자주 하는 실수

### Repository 구현 클래스를 직접 만든다

기본 CRUD만 필요하다면 인터페이스만 만들면 된다. Spring Data JPA가 구현체를 생성한다.

### JPQL을 SQL처럼 쓴다

JPQL은 테이블과 컬럼이 아니라 Entity와 필드를 기준으로 쓴다.

### 조회 메서드에 트랜잭션을 안 붙인다

조회도 `@Transactional(readOnly = true)`를 붙이는 습관이 좋다. 성능 최적화와 의도를 드러내는 데 도움이 된다.

## 확인 문제

1. `JpaRepository<Todo, Long>`에서 `Todo`와 `Long`은 각각 무엇을 의미하는가?
2. Spring Data JPA가 Repository 구현체를 어떻게 제공하는가?
3. 메서드 이름 기반 쿼리의 장점과 단점은 무엇인가?
4. JPQL과 SQL의 기준 대상은 어떻게 다른가?

## 참고 공식 자료

- Spring Data JPA Reference: Getting Started  
  https://docs.spring.io/spring-data/jpa/reference/jpa/getting-started.html

## 다음 장으로

다음 장에서는 트랜잭션을 배운다. JPA를 제대로 쓰려면 트랜잭션 이해가 필수다.

---

## 문서 이동

- [이전: 23. JPA와 Entity 기초](./23_JPA와_Entity_기초.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 25. 트랜잭션](./25_트랜잭션.md)
