# 실행 가능한 JAR 만들기

## 이번 장에서 배울 것

이 장에서는 Spring Boot 애플리케이션을 실행 가능한 JAR 파일로 패키징하는 방법을 배운다.

개발 중에는 IDE나 `bootRun`으로 실행한다. 하지만 배포할 때는 보통 하나의 실행 가능한 파일로 묶어 서버나 컨테이너에서 실행한다.

## 실행 가능한 JAR란 무엇인가

일반 Java JAR는 클래스와 리소스를 묶은 파일이다. 하지만 실행하려면 클래스패스와 의존성 설정을 따로 맞춰야 한다.

Spring Boot의 실행 가능한 JAR는 애플리케이션 코드, 의존성 라이브러리, 실행에 필요한 부트스트랩 코드를 함께 담는다.

그래서 다음처럼 실행할 수 있다.

```bash
java -jar build/libs/todo-0.0.1-SNAPSHOT.jar
```

내장 Tomcat도 JAR 안의 의존성으로 포함되어 별도 WAS 설치 없이 실행된다.

## Gradle로 빌드하기

Windows:

```powershell
.\gradlew.bat clean bootJar
```

macOS 또는 Linux:

```bash
./gradlew clean bootJar
```

결과물은 보통 다음 위치에 생긴다.

```text
build/libs/
```

예:

```text
build/libs/todo-0.0.1-SNAPSHOT.jar
```

## JAR 실행하기

```bash
java -jar build/libs/todo-0.0.1-SNAPSHOT.jar
```

Profile 지정:

```bash
java -jar build/libs/todo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

포트 변경:

```bash
java -jar build/libs/todo-0.0.1-SNAPSHOT.jar --server.port=8081
```

환경 변수도 사용할 수 있다.

```bash
SPRING_PROFILES_ACTIVE=prod java -jar build/libs/todo-0.0.1-SNAPSHOT.jar
```

Windows PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
java -jar build/libs/todo-0.0.1-SNAPSHOT.jar
```

## `bootJar`와 `jar`

Gradle에는 보통 `jar`와 `bootJar`가 있다.

| 작업 | 설명 |
| --- | --- |
| `jar` | 일반 JAR 생성 |
| `bootJar` | Spring Boot 실행 가능한 JAR 생성 |

Spring Boot 애플리케이션 배포에는 보통 `bootJar`를 사용한다.

## Maven으로 빌드하기

Maven 프로젝트라면 다음 명령을 사용한다.

```bash
./mvnw clean package
```

Windows:

```powershell
.\mvnw.cmd clean package
```

결과물은 보통 다음 위치에 생긴다.

```text
target/
```

실행:

```bash
java -jar target/todo-0.0.1-SNAPSHOT.jar
```

## 빌드 전 테스트

`bootJar`만 실행하면 테스트를 건너뛸 수 있다. 배포 전에는 테스트를 함께 실행하는 습관이 좋다.

Gradle:

```bash
./gradlew clean test bootJar
```

또는:

```bash
./gradlew clean build
```

`build`는 테스트와 패키징을 함께 수행한다.

테스트를 임시로 건너뛰는 명령도 있지만, 운영 배포에서는 조심해야 한다.

```bash
./gradlew build -x test
```

## JAR 안에는 무엇이 들어 있을까?

Spring Boot 실행 JAR는 내부적으로 다음과 비슷한 구조를 가진다.

```text
BOOT-INF/
  classes/
  lib/
META-INF/
org/springframework/boot/loader/
```

- `BOOT-INF/classes`: 애플리케이션 클래스와 리소스
- `BOOT-INF/lib`: 의존성 라이브러리
- boot loader: 실행을 도와주는 코드

이 구조 덕분에 `java -jar`로 실행할 수 있다.

## 배포 시 필요한 것

JAR 배포에 필요한 기본 요소:

- JDK 또는 JRE
- 실행 가능한 JAR 파일
- 환경 변수 또는 외부 설정 파일
- 로그 수집 방식
- 프로세스 관리 방식

Linux 서버에서는 systemd 같은 프로세스 관리자를 사용하거나, 컨테이너 환경에서는 Docker/Kubernetes가 프로세스 관리를 맡는다.

## 자주 하는 실수

### 일반 JAR를 실행하려 한다

Spring Boot 배포용은 보통 `bootJar` 결과물을 사용한다.

### 운영 설정을 JAR 안에 고정한다

JAR는 환경과 독립적이어야 한다. 운영 DB 비밀번호 같은 값은 환경 변수나 외부 설정으로 주입한다.

### 테스트를 건너뛰고 배포한다

급할수록 테스트를 건너뛰고 싶지만, 그럴 때일수록 테스트가 필요하다.

## 확인 문제

1. Spring Boot 실행 가능한 JAR의 장점은 무엇인가?
2. Gradle에서 실행 가능한 JAR를 만드는 대표 작업은 무엇인가?
3. `bootJar`와 `jar`의 차이는 무엇인가?
4. 운영 설정을 JAR 안에 고정하지 않는 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Packaging Spring Boot Applications  
  https://docs.spring.io/spring-boot/3.5/reference/packaging/index.html
- Spring Boot Gradle Plugin: Packaging Executable Archives  
  https://docs.spring.io/spring-boot/3.5/gradle-plugin/packaging.html

## 다음 장으로

다음 장에서는 실행 가능한 JAR를 Docker 이미지로 만드는 방법을 배운다.

---

## 문서 이동

- [이전: 44. 운영에서 자주 나는 문제](./44_운영에서_자주_나는_문제.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 46. Docker 이미지 만들기](./46_Docker_이미지_만들기.md)
