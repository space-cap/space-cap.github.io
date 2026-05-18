# GraalVM Native Image 입문

## 이번 장에서 배울 것

이 장에서는 GraalVM Native Image의 기본 개념을 배운다.

Spring Boot 애플리케이션은 보통 JVM 위에서 실행된다. Native Image는 JVM 없이 실행 가능한 플랫폼별 실행 파일을 미리 만들어 배포하는 방식이다.

## Native Image란 무엇인가

GraalVM Native Image는 Java 애플리케이션을 ahead-of-time 방식으로 컴파일해 독립 실행 파일로 만든다.

일반 JVM 실행:

```text
JAR + JVM -> 실행
```

Native Image 실행:

```text
native executable -> 실행
```

공식 문서 기준으로 Native Image는 JVM과 비교해 더 빠른 시작 시간과 더 작은 메모리 사용량을 기대할 수 있다.

## 언제 유용할까?

Native Image가 특히 유용한 상황:

- 서버리스 환경
- 짧게 실행되는 작업
- 빠른 scale-out이 중요한 서비스
- 메모리 제한이 엄격한 컨테이너
- 콜드 스타트가 중요한 API

반대로 항상 정답은 아니다. 일반적인 장시간 실행 서버에서는 JVM의 JIT 최적화가 유리할 수 있다.

## 중요한 차이

Native Image는 빌드 시점에 애플리케이션을 정적으로 분석한다.

이 때문에 JVM 실행과 차이가 있다.

공식 문서에서 강조하는 주요 차이:

- 빌드 시점에 정적 분석이 수행된다.
- 도달할 수 없는 코드는 실행 파일에 포함되지 않을 수 있다.
- reflection, resource, serialization, dynamic proxy는 힌트가 필요할 수 있다.
- classpath는 빌드 시점에 고정된다.
- lazy class loading이 없다.
- 일부 Java 기능이나 라이브러리에 제한이 있을 수 있다.

Spring Boot는 AOT 처리를 통해 Native Image에 필요한 정보를 생성한다.

## Spring AOT

Spring 애플리케이션은 원래 런타임 동적 구성이 많다.

예:

- 컴포넌트 스캔
- 자동 설정 조건 평가
- reflection 기반 Bean 생성
- 프록시 생성

Native Image에서는 이런 동적 요소를 빌드 시점에 최대한 분석하고 준비해야 한다.

Spring AOT는 빌드 시점에 Bean 정의와 reflection hint 등을 생성해 GraalVM이 이해할 수 있도록 돕는다.

## Gradle Native Build Tools

Spring Initializr에서 GraalVM Native Support를 선택하면 관련 플러그인이 추가된다.

Gradle 예:

```groovy
plugins {
    id 'org.graalvm.buildtools.native' version '0.10.6'
}
```

실제 버전은 프로젝트 생성 시점에 따라 달라질 수 있다.

Native 실행 파일 빌드:

```bash
./gradlew nativeCompile
```

Windows:

```powershell
.\gradlew.bat nativeCompile
```

결과물은 보통 다음 위치에 생긴다.

```text
build/native/nativeCompile/
```

## Buildpacks로 Native Image 만들기

Docker가 있다면 Buildpacks로 native container image를 만들 수도 있다.

Gradle 예:

```bash
./gradlew bootBuildImage --imageName=todo-api-native:0.0.1
```

Native Image 빌드를 활성화하는 설정은 프로젝트 구성과 플러그인에 따라 달라질 수 있다. Spring Initializr에서 GraalVM Native Support를 선택하고 시작하는 것이 가장 쉽다.

## Native Image의 장점

장점:

- 시작 시간이 빠르다.
- 메모리 사용량이 낮을 수 있다.
- JVM 없이 단일 실행 파일로 배포할 수 있다.
- 서버리스와 scale-to-zero 환경에 유리하다.

## Native Image의 비용

비용:

- 빌드 시간이 길다.
- 빌드 환경 요구사항이 늘어난다.
- 일부 라이브러리 호환성을 확인해야 한다.
- reflection, resource hint 문제가 생길 수 있다.
- JVM과 성능 특성이 다르다.

Native Image는 "무조건 더 빠른 Java"가 아니다. 시작 시간과 메모리에는 강하지만, 모든 장시간 처리 성능이 JVM보다 낫다고 단정할 수 없다.

## 호환성 확인

Native Image를 고려할 때 확인할 것:

- 사용하는 라이브러리가 Native Image를 지원하는가
- reflection을 많이 쓰는가
- 동적 class loading이 필요한가
- 프록시와 serialization 사용이 많은가
- 테스트가 native 환경에서도 통과하는가

특히 DB, 보안, JSON, HTTP client, 메시징 라이브러리는 Native Image 호환성을 확인한다.

## native 테스트

Native Image는 JVM 테스트가 통과해도 native 실행에서 문제가 날 수 있다.

가능하면 native image를 빌드한 뒤 실제로 실행하고 기본 API를 호출해 본다.

```bash
./gradlew nativeCompile
./build/native/nativeCompile/todo
```

또는 native container image를 실행한다.

```bash
docker run --rm -p 8080:8080 todo-api-native:0.0.1
```

## 언제 선택할까?

추천:

- 서버리스 함수나 scale-to-zero 서비스
- 시작 시간이 매우 중요한 서비스
- 메모리 비용이 큰 문제인 서비스
- Native Image 호환성이 검증된 의존성 조합

신중:

- 복잡한 reflection 기반 라이브러리 사용
- 동적 플러그인 구조
- 빌드 시간이 매우 중요한 개발 환경
- JVM 튜닝으로 이미 충분한 장시간 실행 서비스

## 자주 하는 실수

### Native Image가 항상 더 빠르다고 생각한다

시작 시간은 빠르지만 모든 상황에서 처리량이 더 좋다고 단정할 수 없다.

### JVM 테스트만 믿는다

Native Image는 빌드와 실행 모델이 다르다. native 빌드와 실행 테스트를 별도로 확인한다.

### reflection 문제를 뒤늦게 만난다

사용 라이브러리의 Native Image 지원 여부를 미리 확인한다.

## 확인 문제

1. Native Image는 JVM 실행과 무엇이 다른가?
2. Native Image가 특히 유용한 배포 환경은 무엇인가?
3. Spring AOT는 어떤 역할을 하는가?
4. Native Image를 선택할 때 고려해야 할 비용은 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Introducing GraalVM Native Images  
  https://docs.spring.io/spring-boot/3.5/reference/packaging/native-image/introducing-graalvm-native-images.html

## 다음 장으로

다음 파트에서는 캐싱, 스케줄링, REST Client, 메시징, Batch, WebFlux, 커스텀 자동 설정 같은 심화 주제를 다룬다.

---

## 문서 이동

- [이전: 49. 클라우드 배포 개요](./49_클라우드_배포_개요.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 51. 캐싱](./51_캐싱.md)
