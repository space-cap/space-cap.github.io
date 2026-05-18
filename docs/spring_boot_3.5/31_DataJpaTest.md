# DataJpaTest

## 이번 장에서 배울 것

이 장에서는 `@DataJpaTest`를 배운다.

Repository는 데이터베이스와 직접 연결되는 계층이다. 쿼리 메서드, JPQL, Entity 매핑이 제대로 동작하는지 확인하려면 Repository 테스트가 필요하다.

## `@DataJpaTest`란 무엇인가

`@DataJpaTest`는 JPA 관련 Bean만 로드하는 슬라이스 테스트다.

주로 다음을 테스트한다.

- Entity 매핑
- Repository 기본 CRUD
- 메서드 이름 기반 쿼리
- `@Query` JPQL
- 제약 조건과 저장 동작

전체 애플리케이션을 띄우는 `@SpringBootTest`보다 가볍다.

## 기본 특징

`@DataJpaTest`의 일반적인 특징은 다음과 같다.

- JPA 관련 설정만 로드한다.
- Repository를 테스트하기 좋다.
- 테스트마다 트랜잭션이 적용되고 기본적으로 롤백된다.
- 임베디드 DB가 있으면 테스트 DB로 사용하기 쉽다.

처음에는 H2로 Repository 테스트를 시작하고, 나중에는 Testcontainers로 PostgreSQL 테스트를 추가하면 좋다.

## 의존성

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'com.h2database:h2'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## Entity와 Repository

Entity:

```java
package com.example.todo.todo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    protected Todo() {
    }

    public Todo(String title) {
        this.title = title;
        this.completed = false;
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

    public void complete() {
        this.completed = true;
    }
}
```

Repository:

```java
package com.example.todo.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByCompleted(boolean completed);

    List<Todo> findByTitleContaining(String keyword);
}
```

## 저장과 조회 테스트

```java
package com.example.todo.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Test
    void saveAndFindById() {
        Todo todo = new Todo("Repository 테스트");

        Todo saved = todoRepository.save(todo);

        Todo found = todoRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getTitle()).isEqualTo("Repository 테스트");
        assertThat(found.isCompleted()).isFalse();
    }
}
```

## 쿼리 메서드 테스트

```java
@Test
void findByCompleted() {
    Todo todo1 = new Todo("미완료 할 일");
    Todo todo2 = new Todo("완료 할 일");
    todo2.complete();

    todoRepository.save(todo1);
    todoRepository.save(todo2);

    assertThat(todoRepository.findByCompleted(false))
            .extracting(Todo::getTitle)
            .containsExactly("미완료 할 일");
}
```

쿼리 메서드는 이름이 맞으면 자동으로 쿼리가 생성된다. 하지만 이름이 복잡해질수록 테스트가 중요하다.

## TestEntityManager

`@DataJpaTest`에서는 `TestEntityManager`를 사용할 수 있다.

```java
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@Autowired
TestEntityManager entityManager;
```

예:

```java
@Test
void findByTitleContaining() {
    entityManager.persist(new Todo("Spring Boot"));
    entityManager.persist(new Todo("JPA"));
    entityManager.flush();

    assertThat(todoRepository.findByTitleContaining("Spring"))
            .extracting(Todo::getTitle)
            .containsExactly("Spring Boot");
}
```

`TestEntityManager`는 테스트에서 Entity를 직접 저장하고 flush하는 데 유용하다.

## flush와 clear

JPA 테스트에서 중요한 습관이 있다.

```java
entityManager.flush();
entityManager.clear();
```

- `flush`: 영속성 컨텍스트의 변경을 DB에 반영한다.
- `clear`: 영속성 컨텍스트를 비운다.

이렇게 하면 1차 캐시에 가려진 문제를 줄일 수 있다.

예:

```java
@Test
void saveAndReload() {
    Todo saved = todoRepository.save(new Todo("다시 조회"));

    entityManager.flush();
    entityManager.clear();

    Todo found = todoRepository.findById(saved.getId()).orElseThrow();

    assertThat(found.getTitle()).isEqualTo("다시 조회");
}
```

## H2 테스트의 한계

H2는 빠르고 편하지만 실제 운영 DB와 다를 수 있다.

차이가 날 수 있는 부분:

- SQL 문법
- 컬럼 타입
- 인덱스 동작
- 대소문자 처리
- 날짜 함수
- 제약 조건

단순 Repository 테스트는 H2로 충분할 수 있다. 하지만 운영 DB와 같은 동작을 보장해야 하는 쿼리는 Testcontainers로 실제 PostgreSQL을 사용하는 것이 좋다.

## 자주 하는 실수

### Repository 테스트에 `@SpringBootTest`를 사용한다

Repository만 테스트한다면 `@DataJpaTest`가 더 가볍고 의도가 분명하다.

### flush 없이 테스트해서 착각한다

영속성 컨텍스트 안에서만 맞는 것처럼 보일 수 있다. DB 반영까지 확인하려면 flush와 clear를 고려한다.

### H2 통과를 운영 DB 통과로 믿는다

중요한 쿼리는 실제 DB 기반 테스트가 필요하다.

## 확인 문제

1. `@DataJpaTest`는 어떤 계층을 테스트하기 위한 애너테이션인가?
2. `TestEntityManager`는 어떤 상황에서 유용한가?
3. `flush`와 `clear`를 사용하는 이유는 무엇인가?
4. H2 테스트의 한계는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Testing Spring Boot Applications  
  https://docs.spring.io/spring-boot/3.5/reference/testing/spring-boot-applications.html

## 다음 장으로

다음 장에서는 Testcontainers로 실제 PostgreSQL 컨테이너를 띄워 테스트하는 방법을 배운다.

---

## 문서 이동

- [이전: 30. WebMvcTest와 슬라이스 테스트](./30_WebMvcTest와_슬라이스_테스트.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 32. Testcontainers](./32_Testcontainers.md)
