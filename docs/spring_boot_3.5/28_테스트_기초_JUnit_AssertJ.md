# 테스트 기초 JUnit AssertJ

## 이번 장에서 배울 것

이 장에서는 Spring Boot 테스트의 가장 밑바탕인 JUnit과 AssertJ를 배운다.

테스트는 "코드가 맞는지 확인하는 코드"다. 처음에는 귀찮아 보이지만, 프로젝트가 커질수록 테스트는 개발자의 안전망이 된다. 테스트가 있으면 코드를 고칠 때 마음이 훨씬 가벼워진다.

## 테스트가 필요한 이유

버그는 보통 이런 순간에 생긴다.

- 기능을 조금 고쳤다.
- 예전 기능이 조용히 깨졌다.
- 브라우저나 Postman으로는 확인하지 않은 경로에서 문제가 터졌다.
- 배포 후에야 알게 됐다.

테스트는 이런 일을 줄여준다.

좋은 테스트는 다음 질문에 답한다.

```text
이 코드가 기대한 입력을 받으면 기대한 결과를 내는가?
잘못된 입력을 받으면 올바르게 실패하는가?
수정 후에도 기존 동작이 유지되는가?
```

## Spring Boot의 테스트 의존성

Spring Initializr로 프로젝트를 만들면 보통 다음 의존성이 들어 있다.

```groovy
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

`spring-boot-starter-test`는 테스트에 필요한 여러 라이브러리를 함께 가져온다.

- JUnit Jupiter
- AssertJ
- Hamcrest
- Mockito
- Spring Test
- Spring Boot Test

처음에는 JUnit과 AssertJ부터 익히면 된다.

## JUnit이란 무엇인가

JUnit은 Java 테스트를 작성하고 실행하는 대표 프레임워크다.

테스트 메서드에는 `@Test`를 붙인다.

```java
package com.example.todo.todo;

import org.junit.jupiter.api.Test;

class TodoTitleTest {

    @Test
    void createTitle() {
        // test code
    }
}
```

JUnit Jupiter는 JUnit 5의 테스트 작성 모델이다. Spring Boot 3.5 프로젝트에서는 JUnit Jupiter를 기본으로 사용한다고 생각하면 된다.

## AssertJ란 무엇인가

AssertJ는 검증문을 읽기 좋게 쓰도록 도와주는 라이브러리다.

```java
import static org.assertj.core.api.Assertions.assertThat;

assertThat(result).isEqualTo("Spring Boot");
```

JUnit의 기본 assertion도 사용할 수 있지만, Spring Boot 프로젝트에서는 AssertJ를 많이 사용한다. 실패 메시지가 읽기 좋고 컬렉션, 예외, 객체 검증이 편하다.

## 첫 단위 테스트

간단한 클래스를 만들자.

```java
package com.example.todo.todo;

public class TodoTitle {

    private final String value;

    public TodoTitle(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("제목은 100자 이하여야 합니다.");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
```

테스트:

```java
package com.example.todo.todo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TodoTitleTest {

    @Test
    void createTitle() {
        TodoTitle title = new TodoTitle("Spring Boot 공부");

        assertThat(title.value()).isEqualTo("Spring Boot 공부");
    }
}
```

## 테스트 이름 짓기

테스트 이름은 읽는 사람에게 의도를 알려야 한다.

나쁜 예:

```java
@Test
void test1() {
}
```

좋은 예:

```java
@Test
void createTitle() {
}

@Test
void failWhenTitleIsBlank() {
}
```

한글 이름도 사용할 수 있다.

```java
@Test
void 제목이_비어_있으면_예외가_발생한다() {
}
```

팀 규칙에 맞춰 영어 또는 한글 중 하나를 선택하면 된다.

## 예외 테스트

잘못된 입력에서 예외가 발생하는지 확인한다.

```java
package com.example.todo.todo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TodoTitleTest {

    @Test
    void failWhenTitleIsBlank() {
        assertThatThrownBy(() -> new TodoTitle(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 비어 있을 수 없습니다.");
    }
}
```

성공 경로만 테스트하면 반쪽짜리 테스트가 된다. 실패해야 할 때 실패하는지도 확인해야 한다.

## given, when, then

테스트는 보통 세 구간으로 나누면 읽기 쉽다.

```java
@Test
void createTitle() {
    // given
    String value = "Spring Boot 공부";

    // when
    TodoTitle title = new TodoTitle(value);

    // then
    assertThat(title.value()).isEqualTo(value);
}
```

각 의미는 다음과 같다.

- given: 테스트 준비
- when: 실제 행동
- then: 결과 검증

항상 주석을 붙일 필요는 없다. 테스트가 길어질 때 구조를 잡는 데 도움이 된다.

## 컬렉션 검증

AssertJ는 컬렉션 검증이 편하다.

```java
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodoCollectionTest {

    @Test
    void containsTodos() {
        List<String> titles = List.of("Spring", "JPA", "Test");

        assertThat(titles)
                .hasSize(3)
                .contains("JPA")
                .doesNotContain("Security");
    }
}
```

객체 목록에서 특정 필드만 검증할 수도 있다.

```java
assertThat(todos)
        .extracting(TodoResponse::title)
        .containsExactly("Spring", "JPA");
```

## 테스트 실행

Gradle:

```bash
./gradlew test
```

Windows:

```powershell
.\gradlew.bat test
```

IDE에서도 테스트 클래스나 메서드 옆의 실행 버튼으로 실행할 수 있다.

## 단위 테스트의 장점

지금 작성한 테스트는 Spring을 띄우지 않는다. 그래서 빠르다.

단위 테스트는 다음에 좋다.

- 도메인 규칙 검증
- 계산 로직 검증
- 문자열, 날짜, 금액 같은 값 객체 검증
- Service 로직 중 외부 의존성이 적은 부분 검증

모든 테스트에 Spring Context를 띄우면 느려진다. 순수 Java로 검증할 수 있는 것은 순수 단위 테스트로 작성하는 것이 좋다.

## 자주 하는 실수

### 테스트가 너무 많은 것을 한 번에 검증한다

하나의 테스트는 하나의 중요한 행동을 검증하는 것이 좋다.

### 성공 케이스만 테스트한다

실패해야 하는 입력도 테스트한다.

### 테스트 이름이 의도를 말하지 않는다

테스트 이름은 작은 문서다. 실패했을 때 이름만 봐도 무엇이 깨졌는지 알 수 있어야 한다.

## 확인 문제

1. `spring-boot-starter-test`가 제공하는 대표 테스트 라이브러리는 무엇인가?
2. JUnit에서 테스트 메서드에 붙이는 애너테이션은 무엇인가?
3. AssertJ에서 값을 검증할 때 주로 사용하는 시작 메서드는 무엇인가?
4. Spring을 띄우지 않는 단위 테스트의 장점은 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Testing  
  https://docs.spring.io/spring-boot/3.5/reference/testing/index.html
- JUnit User Guide  
  https://docs.junit.org/
- AssertJ Documentation  
  https://assertj.github.io/doc/

## 다음 장으로

다음 장에서는 `@SpringBootTest`로 Spring Boot 애플리케이션 전체를 테스트하는 방법을 배운다.

---

## 문서 이동

- [이전: 27. PostgreSQL로 전환하기](./27_PostgreSQL로_전환하기.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 29. Spring Boot Test](./29_Spring_Boot_Test.md)
