# Spring Initializr로 프로젝트 만들기

## 이번 장에서 배울 것

이 장에서는 Spring Initializr를 사용해서 Spring Boot 3.5.x 프로젝트를 만든다.

Spring Initializr는 프로젝트 생성기다. 웹 화면에서 몇 가지 선택을 하면, 필요한 빌드 파일과 기본 클래스가 들어 있는 압축 파일을 만들어 준다.

## Spring Initializr 접속

브라우저에서 다음 주소로 이동한다.

```text
https://start.spring.io/
```

여기서 프로젝트의 기본 정보를 선택한다.

## 추천 설정

처음 실습에서는 다음 설정을 권장한다.

```text
Project: Gradle - Groovy
Language: Java
Spring Boot: 3.5.x
Group: com.example
Artifact: todo
Name: todo
Description: Todo API project for Spring Boot study
Package name: com.example.todo
Packaging: Jar
Java: 21
```

`Group`은 보통 회사나 조직의 도메인을 거꾸로 쓴다. 개인 학습에서는 `com.example`을 사용해도 된다.

`Artifact`는 빌드 결과물과 프로젝트 이름에 영향을 준다. 여기서는 `todo`라고 한다.

## Dependencies 선택

처음 프로젝트에는 다음 의존성을 추가한다.

- Spring Web
- Spring Boot DevTools
- Validation

각 의존성의 의미는 다음과 같다.

### Spring Web

REST API를 만들기 위한 기본 의존성이다. Spring MVC, 내장 Tomcat, JSON 처리 라이브러리 등이 함께 들어온다.

### Spring Boot DevTools

개발 중 편의 기능을 제공한다. 코드가 바뀌면 애플리케이션을 빠르게 재시작하는 기능 등이 있다.

운영 배포용 기능이 아니라 개발 편의 기능이라고 생각하면 된다.

### Validation

요청 값 검증에 사용한다. 예를 들어 제목이 비어 있으면 요청을 거절하는 기능을 만들 때 사용한다.

## 프로젝트 다운로드와 압축 해제

설정을 마쳤다면 Generate 버튼을 눌러 압축 파일을 내려받는다.

압축을 풀면 다음과 비슷한 구조가 나온다.

```text
todo/
  build.gradle
  settings.gradle
  gradlew
  gradlew.bat
  src/
    main/
      java/
      resources/
    test/
      java/
```

IDE에서 이 폴더를 프로젝트로 연다.

## 처음 실행하기

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

macOS 또는 Linux:

```bash
./gradlew bootRun
```

처음 실행할 때는 인터넷에서 의존성을 내려받기 때문에 시간이 걸릴 수 있다.

정상 실행되면 로그에 다음과 비슷한 문장이 보인다.

```text
Tomcat started on port 8080
Started TodoApplication
```

아직 Controller를 만들지 않았다면 브라우저에서 `http://localhost:8080`에 접속했을 때 404가 나올 수 있다. 이것은 실패가 아니다. 서버는 떴지만 루트 주소에 응답할 Controller가 없다는 뜻이다.

## `build.gradle` 살펴보기

처음 생성한 `build.gradle`에는 대략 다음 요소가 있다.

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.x'
    id 'io.spring.dependency-management' version '1.1.x'
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

정확한 버전은 프로젝트를 생성한 시점에 따라 달라질 수 있다.

중요한 것은 `spring-boot-starter-web` 하나로 웹 애플리케이션에 필요한 여러 라이브러리가 함께 들어온다는 점이다.

## 자주 하는 실수

### Snapshot 버전을 선택한다

처음 공부할 때는 정식 릴리스 버전을 선택한다. Snapshot은 아직 개발 중인 버전이라 예제가 달라질 수 있다.

### Java 버전을 17 미만으로 고른다

Spring Boot 3.5.x는 Java 17 이상이 필요하다. 이 문서에서는 Java 21을 기준으로 한다.

### 의존성을 너무 많이 추가한다

처음부터 JPA, Security, OAuth2, Kafka를 모두 추가하면 프로젝트가 복잡해진다. 필요한 순간에 하나씩 추가하는 편이 좋다.

## 확인 문제

1. Spring Initializr는 어떤 도구인가?
2. `spring-boot-starter-web`을 추가하면 어떤 기능들이 함께 들어오는가?
3. 처음 `http://localhost:8080`에서 404가 나와도 괜찮은 이유는 무엇인가?
4. 처음 프로젝트에 의존성을 적게 넣는 것이 좋은 이유는 무엇인가?

## 다음 장으로

다음 장에서는 직접 Controller를 작성해서 첫 번째 HTTP 응답을 만든다.

---

## 문서 이동

- [이전: 04. 개발 환경 설치](./04_개발_환경_설치.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 06. 첫 번째 Spring Boot 애플리케이션](./06_첫_번째_Spring_Boot_애플리케이션.md)
