# Spring Boot 3.5.x에서 알아야 할 점

## 이번 장에서 배울 것

이 장에서는 Spring Boot 3.5.x를 공부할 때 반드시 알아야 할 기준을 정리한다. 초보자에게 가장 중요한 것은 "내가 보는 예제가 지금 버전에 맞는가"를 판단하는 눈이다.

## 기준 버전

이 문서 모음은 Spring Boot 3.5.x를 기준으로 한다.

2026-05-18 기준 Spring Boot 3.5 공식 문서는 `3.5.14`를 가리킨다. 공식 문서에는 최신 안정 버전으로 4.0.6도 표시되어 있지만, 여기서는 3.5.x 계열의 사용법을 중심으로 설명한다.

실무에서는 최신 메이저 버전으로 바로 넘어가지 않는 경우가 많다. 기존 라이브러리 호환성, 운영 안정성, 팀의 업그레이드 일정 때문이다. 그래서 3.5.x를 잘 배우는 것은 여전히 의미가 크다.

## Java 17 이상이 필요하다

Spring Boot 3.5.x는 Java 17 이상이 필요하다.

문서 예제는 Java 21 LTS를 권장한다. Java 21은 장기 지원 버전이고, 현재 신규 프로젝트에서 선택하기 좋은 기준이다.

버전 확인은 다음 명령으로 한다.

```bash
java -version
```

예상 형태는 다음과 비슷하다.

```text
openjdk version "21..."
```

Java 8이나 Java 11이 나온다면 Spring Boot 3.5.x 프로젝트를 제대로 실행할 수 없다.

## Spring Framework 6.2 기반

Spring Boot는 내부적으로 Spring Framework를 사용한다. Spring Boot 3.5.x는 Spring Framework 6.2 이상을 기반으로 한다.

따라서 Spring Framework 5.x 기준 글을 그대로 따라 하면 맞지 않는 부분이 생길 수 있다.

특히 다음 부분을 조심한다.

- 오래된 XML 설정 중심 예제
- `javax.*` 패키지를 사용하는 예제
- Spring Security의 예전 설정 방식
- 오래된 Gradle 또는 Maven 설정

## Jakarta EE 전환

Spring Boot 3.x의 가장 큰 변화 중 하나는 Jakarta EE 전환이다.

과거 Java EE 계열 패키지는 `javax.*` 이름을 많이 사용했다. 그러나 현재는 `jakarta.*`로 바뀌었다.

예를 들어 Servlet 관련 패키지는 다음처럼 바뀐다.

```java
// 과거 방식
import javax.servlet.http.HttpServletRequest;

// Spring Boot 3.x 방식
import jakarta.servlet.http.HttpServletRequest;
```

Bean Validation도 마찬가지다.

```java
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
```

초보자는 오류 메시지에서 `package javax... does not exist`를 만나면 "라이브러리가 빠졌나?"라고 생각하기 쉽다. Spring Boot 3.x에서는 먼저 패키지 이름이 현재 버전에 맞는지 확인해야 한다.

## 빌드 도구 기준

공식 문서 기준으로 Spring Boot 3.5.x는 다음 빌드 도구 버전을 지원한다.

- Maven 3.6.3 이상
- Gradle 7.6.4 이상 또는 8.4 이상

신규 프로젝트라면 Gradle 8.x 또는 Maven 3.9.x를 사용하는 것이 좋다.

이 문서 모음의 예제는 Gradle을 기본으로 진행하되, 필요한 곳에서는 Maven 명령도 함께 보여준다.

## 내장 서버 기준

Spring Boot 웹 애플리케이션은 별도 Tomcat 설치 없이 실행할 수 있다. `spring-boot-starter-web`을 추가하면 기본적으로 내장 Tomcat이 들어온다.

Spring Boot 3.5.x에서 공식적으로 언급되는 Servlet 컨테이너 기준은 다음과 같다.

- Tomcat 10.1
- Jetty 12.0
- Undertow 2.3

초보자는 처음에는 Tomcat만 이해해도 충분하다. Jetty와 Undertow는 운영 요구사항이나 팀 표준에 따라 선택하는 대안으로 보면 된다.

## 3.5.x에서 공부할 때의 기준선

이 문서 모음에서는 다음 기준을 사용한다.

```text
Java: 21
Spring Boot: 3.5.x
Build Tool: Gradle 8.x
Language: Java
Packaging: Jar
Web Stack: Spring MVC
Database: H2 -> PostgreSQL
```

Kotlin, WebFlux, GraalVM Native Image, Kafka 같은 주제는 뒤쪽 심화 파트에서 다룬다. 처음부터 함께 넣으면 초보자에게 부담이 커진다.

## 자주 하는 실수

### Java 버전을 확인하지 않는다

프로젝트 생성은 됐는데 실행이 안 된다면 가장 먼저 Java 버전을 확인한다.

```bash
java -version
```

### Spring Boot 2.x 예제를 그대로 복사한다

예제의 작성 날짜와 사용 버전을 확인한다. 특히 `javax.*`가 보이면 Spring Boot 3.x 기준으로 바꿔야 할 가능성이 높다.

### Gradle Wrapper를 무시한다

프로젝트에 `gradlew` 또는 `gradlew.bat`가 있다면 시스템에 설치된 Gradle보다 Wrapper를 우선 사용한다.

Windows에서는 다음처럼 실행한다.

```powershell
.\gradlew.bat bootRun
```

macOS나 Linux에서는 다음처럼 실행한다.

```bash
./gradlew bootRun
```

## 확인 문제

1. Spring Boot 3.5.x를 실행하려면 최소 Java 버전은 무엇인가?
2. Spring Boot 3.x에서 `javax.validation` 대신 주로 사용하는 패키지는 무엇인가?
3. 신규 예제에서 Gradle Wrapper를 쓰는 이유는 무엇인가?
4. `spring-boot-starter-web`을 추가하면 기본적으로 어떤 내장 서버를 사용하게 되는가?

## 다음 장으로

다음 장에서는 실제 개발 환경을 설치한다. 버전이 맞지 않으면 이후 모든 실습이 흔들리므로 천천히 확인하고 넘어간다.

---

## 문서 이동

- [이전: 02. Spring과 Spring Boot의 탄생](./02_Spring과_Spring_Boot의_탄생.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 04. 개발 환경 설치](./04_개발_환경_설치.md)
