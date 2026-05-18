# Cloud Native Buildpacks

## 이번 장에서 배울 것

이 장에서는 Cloud Native Buildpacks를 배운다.

Docker 이미지를 만들 때 꼭 Dockerfile을 직접 작성해야 하는 것은 아니다. Spring Boot는 Buildpacks를 사용해 애플리케이션을 컨테이너 이미지로 만들 수 있다.

## Buildpacks란 무엇인가

Buildpacks는 애플리케이션 소스나 빌드 결과물을 분석해서 실행 가능한 컨테이너 이미지를 만들어 주는 기술이다.

Spring Boot 애플리케이션의 경우 Buildpacks는 다음을 자동으로 처리할 수 있다.

- Java 런타임 선택
- 애플리케이션 레이어 구성
- 실행 명령 구성
- 메모리 설정 도움
- 이미지 메타데이터 생성

개발자는 Dockerfile을 직접 쓰지 않고도 이미지를 만들 수 있다.

## Gradle로 이미지 만들기

Spring Boot Gradle Plugin을 사용하면 `bootBuildImage` 작업을 사용할 수 있다.

```bash
./gradlew bootBuildImage
```

Windows:

```powershell
.\gradlew.bat bootBuildImage
```

이미지 이름을 지정할 수도 있다.

```bash
./gradlew bootBuildImage --imageName=todo-api:0.0.1
```

Docker가 실행 중이어야 한다.

## Maven으로 이미지 만들기

Maven:

```bash
./mvnw spring-boot:build-image
```

Windows:

```powershell
.\mvnw.cmd spring-boot:build-image
```

이미지 이름 지정:

```bash
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=todo-api:0.0.1
```

## 생성된 이미지 실행

```bash
docker run --rm -p 8080:8080 todo-api:0.0.1
```

환경 변수 전달:

```bash
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  todo-api:0.0.1
```

## Dockerfile 방식과 비교

| 구분 | Dockerfile | Buildpacks |
| --- | --- | --- |
| 제어력 | 높음 | 상대적으로 낮음 |
| 시작 난이도 | 직접 작성 필요 | 명령 하나로 가능 |
| 표준화 | 팀마다 다를 수 있음 | 일관된 빌드 방식 |
| 최적화 | 직접 책임 | Buildpack이 상당 부분 처리 |
| 디버깅 | Dockerfile을 보면 명확 | Buildpack 동작 이해 필요 |

처음에는 Buildpacks가 편하다. 하지만 회사 표준 이미지, 특수 보안 설정, OS 패키지 설치가 필요하면 Dockerfile이 더 적합할 수 있다.

## build.gradle 설정

이미지 이름을 빌드 파일에 고정할 수 있다.

```groovy
tasks.named('bootBuildImage') {
    imageName = 'todo-api:0.0.1'
}
```

환경에 따라 태그를 바꾸려면 CI에서 값을 주입하는 편이 좋다.

## Builder와 Run Image

Buildpacks는 builder image를 사용해 애플리케이션 이미지를 만든다.

기본 builder를 그대로 써도 되지만, 조직 표준이 있으면 builder를 지정할 수 있다.

```groovy
tasks.named('bootBuildImage') {
    builder = 'paketobuildpacks/builder-jammy-base'
}
```

처음에는 기본값으로 시작하고, 필요할 때 builder를 조정한다.

## Native Image와 Buildpacks

Buildpacks는 JVM 이미지뿐 아니라 Native Image 생성에도 사용할 수 있다.

Native Image는 뒤 장에서 따로 다룬다. 지금은 `bootBuildImage`가 컨테이너 이미지 생성을 자동화하는 Spring Boot의 중요한 선택지라는 점만 기억하자.

## CI/CD에서 사용하기

CI에서 Buildpacks를 사용할 때 확인할 것:

- Docker 사용 가능 여부
- 이미지 registry 로그인
- 이미지 태그 규칙
- 빌드 캐시
- 빌드 시간

예:

```bash
./gradlew clean test bootBuildImage --imageName=registry.example.com/todo-api:${GIT_SHA}
docker push registry.example.com/todo-api:${GIT_SHA}
```

## 자주 하는 실수

### Docker를 켜지 않고 실행한다

`bootBuildImage`는 Docker 데몬이 필요하다.

### 이미지 태그를 항상 latest로만 둔다

운영 추적이 어려워진다. Git SHA나 버전 태그를 함께 사용한다.

### Buildpacks와 Dockerfile 중 하나만 정답이라고 생각한다

둘 다 좋은 도구다. 제어가 필요하면 Dockerfile, 표준화와 편의가 중요하면 Buildpacks가 좋다.

## 확인 문제

1. Buildpacks는 어떤 역할을 하는가?
2. Gradle에서 Buildpacks로 이미지를 만드는 작업 이름은 무엇인가?
3. Dockerfile 방식과 Buildpacks 방식의 차이는 무엇인가?
4. CI/CD에서 이미지 태그를 명확히 관리해야 하는 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Cloud Native Buildpacks  
  https://docs.spring.io/spring-boot/3.5/reference/packaging/container-images/cloud-native-buildpacks.html

## 다음 장으로

다음 장에서는 Docker Compose로 애플리케이션과 DB를 함께 실행하는 로컬 환경을 구성한다.

---

## 문서 이동

- [이전: 46. Docker 이미지 만들기](./46_Docker_이미지_만들기.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 48. Docker Compose로 로컬 환경 구성](./48_Docker_Compose로_로컬_환경_구성.md)
