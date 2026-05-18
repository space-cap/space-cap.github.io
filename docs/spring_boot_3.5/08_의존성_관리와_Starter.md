# 의존성 관리와 Starter

## 이번 장에서 배울 것

이 장에서는 Spring Boot의 Starter와 의존성 관리 방식을 배운다.

Spring Boot를 처음 쓰면 `spring-boot-starter-web` 한 줄로 웹 서버가 뜨는 것이 신기하다. 이 장의 목표는 그 한 줄 뒤에서 어떤 일이 일어나는지 이해하는 것이다.

## 의존성이란 무엇인가

의존성은 프로젝트가 사용하는 외부 라이브러리다.

웹 API를 만들려면 다음과 같은 기능이 필요하다.

- HTTP 요청 처리
- JSON 변환
- 내장 서버
- 로깅
- 테스트 도구

이 기능을 모두 직접 만들 수는 없다. 그래서 검증된 라이브러리를 가져와 사용한다.

Gradle에서는 `dependencies` 블록에 의존성을 적는다.

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## Starter란 무엇인가

Starter는 특정 기능을 개발할 때 필요한 의존성을 묶어 둔 패키지다.

`spring-boot-starter-web`은 웹 애플리케이션 개발에 필요한 대표 의존성을 묶어 둔다.

포함되는 대표 기능은 다음과 같다.

- Spring MVC
- 내장 Tomcat
- Jackson JSON 처리
- Validation 관련 연동
- 기본 로깅

개발자는 라이브러리 조합을 일일이 맞추지 않고 Starter 하나를 추가해 시작할 수 있다.

## 왜 Starter가 중요한가

Spring Boot 이전에는 라이브러리 버전 조합을 직접 맞추는 일이 흔했다.

예를 들어 Spring MVC 버전, Jackson 버전, Tomcat 버전이 서로 맞지 않으면 실행 중 오류가 날 수 있다.

Spring Boot는 검증된 조합을 제공한다. 그래서 초보자는 버전 충돌에 너무 일찍 빠지지 않고 기능 구현에 집중할 수 있다.

## 버전을 직접 쓰지 않는 이유

Spring Boot 프로젝트의 의존성에는 버전을 직접 쓰지 않는 경우가 많다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-web'
```

버전이 없는데도 동작하는 이유는 Spring Boot가 의존성 버전을 관리하기 때문이다.

Spring Boot Gradle Plugin과 dependency management 설정이 함께 작동하면서 Spring Boot 버전에 맞는 라이브러리 버전을 선택한다.

즉 `Spring Boot 3.5.x`를 선택하면, 그 버전에 맞는 Spring Framework, Jackson, Tomcat 등의 버전이 따라온다.

## 자주 쓰는 Starter

처음 자주 만나는 Starter는 다음과 같다.

| Starter | 용도 |
| --- | --- |
| `spring-boot-starter-web` | Spring MVC 기반 웹 API 개발 |
| `spring-boot-starter-validation` | 요청 값 검증 |
| `spring-boot-starter-data-jpa` | JPA와 Spring Data JPA |
| `spring-boot-starter-jdbc` | JDBC 기반 DB 접근 |
| `spring-boot-starter-security` | 인증과 인가 |
| `spring-boot-starter-test` | 테스트 |
| `spring-boot-starter-actuator` | 운영 상태 확인 |

## 의존성 범위

Gradle에는 의존성을 어떤 상황에서 쓸지 나타내는 범위가 있다.

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    runtimeOnly 'com.h2database:h2'
}
```

각 의미는 다음과 같다.

- `implementation`: 애플리케이션 코드에서 사용
- `developmentOnly`: 개발 중에만 사용
- `testImplementation`: 테스트 코드에서 사용
- `runtimeOnly`: 실행 시점에만 필요

처음에는 `implementation`과 `testImplementation`만 확실히 알아도 된다.

## 의존성 트리 확인

어떤 라이브러리가 실제로 들어왔는지 확인하려면 Gradle의 dependencies 명령을 사용한다.

Windows:

```powershell
.\gradlew.bat dependencies
```

macOS 또는 Linux:

```bash
./gradlew dependencies
```

출력이 길기 때문에 처음에는 전부 이해하지 않아도 된다. 중요한 것은 Starter 하나가 여러 라이브러리를 함께 가져온다는 사실이다.

## 자주 하는 실수

### Starter와 실제 라이브러리를 구분하지 못한다

Starter는 보통 기능 묶음이다. Starter 자체가 모든 기능을 직접 구현한다기보다, 필요한 라이브러리들을 모아 준다.

### 버전을 아무거나 추가한다

블로그에서 본 의존성을 복사하면서 버전을 직접 지정하면 Spring Boot가 관리하는 버전 조합이 깨질 수 있다. 특별한 이유가 없다면 버전을 생략한다.

### 필요 없는 Starter를 많이 넣는다

Security Starter를 추가하면 기본 보안 설정이 활성화되어 모든 요청에 로그인이 필요해질 수 있다. 필요한 시점에 추가한다.

## 확인 문제

1. Starter는 무엇인가?
2. `spring-boot-starter-web`이 제공하는 대표 기능은 무엇인가?
3. Spring Boot 프로젝트에서 의존성 버전을 생략할 수 있는 이유는 무엇인가?
4. `testImplementation`은 언제 쓰는가?

## 다음 장으로

다음 장에서는 Spring Boot의 가장 중요한 기능인 자동 설정을 배운다.

