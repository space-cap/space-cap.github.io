# Docker 이미지 만들기

## 이번 장에서 배울 것

이 장에서는 Spring Boot 애플리케이션을 Docker 이미지로 만드는 방법을 배운다.

JAR는 Java가 설치된 환경에서 실행된다. Docker 이미지는 애플리케이션과 실행 환경을 함께 묶는다. 그래서 개발, 테스트, 운영 환경의 차이를 줄일 수 있다.

## Docker 이미지란 무엇인가

Docker 이미지는 컨테이너를 실행하기 위한 템플릿이다.

이미지 안에는 보통 다음이 들어간다.

- 운영체제 기반 레이어
- JDK 또는 JRE
- 애플리케이션 JAR
- 실행 명령

컨테이너는 이 이미지를 실행한 프로세스다.

```text
Docker image -> docker run -> Docker container
```

## 기본 Dockerfile

프로젝트 루트에 `Dockerfile`을 만든다.

```dockerfile
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

빌드 전 JAR를 먼저 만든다.

```bash
./gradlew clean bootJar
```

Windows:

```powershell
.\gradlew.bat clean bootJar
```

이미지 빌드:

```bash
docker build -t todo-api:0.0.1 .
```

컨테이너 실행:

```bash
docker run --rm -p 8080:8080 todo-api:0.0.1
```

확인:

```bash
curl http://localhost:8080/actuator/health
```

## 환경 변수 전달

컨테이너 실행 시 환경 변수를 줄 수 있다.

```bash
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/todo \
  -e DB_USERNAME=todo \
  -e DB_PASSWORD=secret \
  todo-api:0.0.1
```

운영 secret은 이미지에 넣지 않는다. 이미지에 들어간 값은 회수하기 어렵다.

## `.dockerignore`

Docker build context에 불필요한 파일이 들어가지 않게 `.dockerignore`를 만든다.

```text
.git
.gradle
build
out
*.iml
```

단, 위 Dockerfile은 `build/libs/*.jar`를 복사하므로 `build`를 무시하면 안 된다. 이런 경우에는 두 가지 선택이 있다.

1. `build`를 `.dockerignore`에서 제외한다.
2. multi-stage build로 Docker 안에서 빌드한다.

처음에는 JAR를 로컬에서 빌드하고 Docker 이미지에 복사하는 방식을 사용한다.

## Multi-stage Dockerfile

Docker 안에서 빌드까지 처리하려면 multi-stage build를 사용할 수 있다.

```dockerfile
FROM eclipse-temurin:21-jdk AS build

WORKDIR /workspace
COPY . .
RUN ./gradlew clean bootJar

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

이 방식은 빌드 환경과 실행 환경을 분리한다.

주의: Linux 컨테이너에서 `./gradlew`를 실행하려면 실행 권한이 필요하다. Windows에서 만든 프로젝트는 권한 문제가 생길 수 있다.

## Layered JAR

Spring Boot는 Docker 이미지 레이어 최적화를 위해 layered JAR 구조를 지원한다.

일반적으로 의존성은 자주 바뀌지 않고, 애플리케이션 클래스는 자주 바뀐다. Docker 레이어를 잘 나누면 이미지 재빌드와 배포가 빨라질 수 있다.

초보 단계에서는 먼저 단순 Dockerfile로 흐름을 익히고, 이후 레이어 최적화를 학습해도 충분하다.

## 이미지 태그 전략

태그는 이미지를 식별한다.

예:

```text
todo-api:0.0.1
todo-api:2026.05.19
todo-api:git-abc1234
todo-api:latest
```

운영에서는 `latest`만 사용하는 것을 피하는 편이 좋다. 어떤 버전이 배포되었는지 추적하기 어렵다.

## 자주 하는 실수

### 이미지에 비밀번호를 넣는다

Dockerfile의 `ENV DB_PASSWORD=...` 같은 방식은 피한다. secret은 실행 환경에서 주입한다.

### 포트 매핑을 헷갈린다

`-p 8081:8080`은 호스트의 8081을 컨테이너의 8080에 연결한다.

### JDK 이미지만 사용한다

실행만 할 이미지라면 JRE 기반 이미지를 사용할 수 있다. 단, 운영 표준과 보안 정책에 맞춰 선택한다.

## 확인 문제

1. Docker 이미지와 컨테이너의 차이는 무엇인가?
2. `docker run -p 8081:8080`에서 앞과 뒤 포트는 각각 무엇을 의미하는가?
3. 운영 secret을 Docker 이미지에 넣으면 안 되는 이유는 무엇인가?
4. multi-stage Dockerfile의 장점은 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Dockerfiles  
  https://docs.spring.io/spring-boot/3.5/reference/packaging/container-images/dockerfiles.html

## 다음 장으로

다음 장에서는 Dockerfile 없이도 이미지를 만들 수 있는 Cloud Native Buildpacks를 배운다.

---

## 문서 이동

- [이전: 45. 실행 가능한 JAR 만들기](./45_실행_가능한_JAR_만들기.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 47. Cloud Native Buildpacks](./47_Cloud_Native_Buildpacks.md)
