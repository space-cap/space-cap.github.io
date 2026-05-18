# Spring Boot 3.4에서 3.5로 업그레이드

## 이번 장에서 배울 것

이번 장에서는 Spring Boot 3.4.x에서 3.5.x로 올릴 때 확인해야 할 변경 사항을 배운다.

2.x에서 3.x로 넘어갈 때만큼 큰 충격은 아니지만, 운영 설정과 테스트에 영향을 줄 수 있는 변화가 있다. 특히 Actuator, profile 이름, boolean 설정값, Redis, TestRestTemplate 같은 항목을 확인해야 한다.

## 업그레이드 전 기본 원칙

Spring Boot minor 버전 업그레이드는 다음 순서로 진행한다.

1. 현재 3.4.x의 최신 패치로 올린다.
2. deprecated 경고를 제거한다.
3. Spring Boot 3.5.x로 올린다.
4. release notes와 configuration changelog를 확인한다.
5. 테스트를 실행한다.
6. 운영 설정을 점검한다.

버전만 바꾸고 끝내면 작은 설정 변화가 운영 장애로 이어질 수 있다.

## 최소 요구 사항

Spring Boot 3.5.14 기준:

- Java 17 이상
- Spring Framework 6.2.18 이상
- Maven 3.6.3 이상
- Gradle 7.6.4 이상 또는 8.4 이상

Spring Boot 3.4에서 이미 Java 17 이상을 사용하고 있다면 대부분 그대로 갈 수 있다. 하지만 CI 이미지, Docker base image, Gradle wrapper는 함께 확인하자.

## heapdump endpoint 접근 변경

Spring Boot 3.5에서는 Actuator `heapdump` endpoint의 기본 access가 `NONE`이 되었다. 민감 정보 유출 가능성을 줄이기 위한 변화다.

이제 heapdump를 쓰려면 노출뿐 아니라 access도 설정해야 한다.

```yaml
management:
  endpoints:
    web:
      exposure:
        include: heapdump
  endpoint:
    heapdump:
      access: unrestricted
```

운영에서는 이 설정을 매우 조심해야 한다. heap dump에는 민감 정보가 들어갈 수 있다. 인증, 네트워크 제한, 임시 활성화 절차를 함께 준비하자.

## boolean 설정값 엄격화

Spring Boot 3.5에서는 `.enabled` 같은 boolean 설정값이 더 엄격하게 처리된다.

이전에는 다음처럼 애매한 값이 우연히 동작할 수 있었다.

```yaml
feature:
  enabled: yes
```

이제는 명확하게 쓰자.

```yaml
feature:
  enabled: true
```

또는:

```yaml
feature:
  enabled: false
```

운영 설정 파일에서 `yes`, `no`, `on`, `off`, `1`, `0` 같은 값을 사용하고 있다면 모두 점검하는 것이 좋다.

## Profile 이름 검증

Spring Boot 3.5에서는 profile 이름 검증이 강화되었다. 3.5.1부터는 `.`, `+`, `@`도 허용되도록 일부 완화되었지만, 그래도 profile 이름은 단순하게 유지하는 것이 좋다.

권장:

```text
local
dev
test
staging
prod
prod-blue
prod_green
```

피하는 편이 좋은 이름:

```text
_prod
prod_
-dev
dev-
```

특별한 이유가 없다면 영문 소문자, 숫자, `-` 정도만 사용하는 것이 가장 안전하다.

검증을 끌 수도 있다.

```yaml
spring:
  profiles:
    validate: false
```

하지만 학습용이나 신규 프로젝트라면 검증을 끄기보다 profile 이름을 정리하는 편이 좋다.

## TestRestTemplate redirect 동작

Spring Boot 3.5에서는 `TestRestTemplate`이 일반 `RestTemplate`과 같은 follow redirects 설정을 사용한다.

리다이렉트 관련 테스트가 갑자기 실패한다면 다음을 확인하자.

- 기존 테스트가 302 응답을 기대했는가?
- 자동 redirect를 따라간 최종 응답을 받게 되었는가?
- `withRedirects(...)`를 사용해 테스트 의도를 명확히 해야 하는가?

테스트 실패가 나쁘다는 뜻은 아니다. 오히려 테스트가 "우리가 무엇을 기대했는지"를 다시 말해 주는 신호다.

## Redis URL과 database 설정

Spring Boot 3.5에서는 `spring.data.redis.url`을 설정하면 Redis database는 URL에 의해 결정된다. URL에 database가 없으면 기본값 `0`이 사용된다. 이때 `spring.data.redis.database`는 무시된다.

혼동되는 설정:

```yaml
spring:
  data:
    redis:
      url: redis://localhost:6379
      database: 3
```

위 설정에서는 URL에 database가 없으므로 `0`이 사용될 수 있다.

명확한 설정:

```yaml
spring:
  data:
    redis:
      url: redis://localhost:6379/3
```

Redis를 캐시, 세션, 메시지 용도로 쓰고 있다면 꼭 확인하자.

## Prometheus Pushgateway 변경

Prometheus Pushgateway를 사용한다면 의존성과 설정 이름이 바뀐 부분을 확인해야 한다.

기존 `simpleclient_pushgateway` 대신 새 Prometheus metrics exporter pushgateway 의존성이 필요하다.

또한 `base-url` 대신 `address`를 사용하는 설정 변경이 있다.

```yaml
management:
  prometheus:
    metrics:
      export:
        pushgateway:
          address: localhost:9091
```

Prometheus Pushgateway를 쓰지 않는 프로젝트라면 영향이 없다.

## 새 기능에서 눈여겨볼 것

Spring Boot 3.5에는 여러 개선이 있다.

- 환경 변수에서 설정을 가져오는 `env:` config import
- structured logging stack trace 커스터마이징
- HTTP client connector 설정 개선
- scheduled task의 task decoration
- OpenTelemetry 관련 개선
- Spring Batch 관련 개선
- Bean background initialization 자동 설정
- Buildpacks와 Docker 인증 관련 개선

초보자는 모든 새 기능을 바로 쓰려고 하기보다, 현재 프로젝트에 영향을 주는 항목부터 확인하면 된다.

## 업그레이드 체크리스트

```text
[ ] Java, Gradle, Maven 버전 확인
[ ] deprecated 경고 제거
[ ] profile 이름 확인
[ ] .enabled 설정값이 true/false인지 확인
[ ] Actuator heapdump 설정 확인
[ ] Redis URL/database 설정 확인
[ ] TestRestTemplate redirect 테스트 확인
[ ] Prometheus Pushgateway 사용 여부 확인
[ ] configuration changelog 확인
[ ] 전체 테스트 실행
```

## 자주 하는 실수

### minor 업그레이드는 안전하다고 가정한다

대부분은 안전하지만 설정 기본값 하나가 운영에 영향을 줄 수 있다.

### 운영 설정 파일을 확인하지 않는다

코드는 그대로여도 `application-prod.yml`, 환경 변수, Helm values에서 문제가 생길 수 있다.

### Actuator endpoint 노출을 가볍게 본다

heapdump, env, configprops 같은 endpoint는 민감 정보와 연결될 수 있다.

### 릴리스 노트를 읽지 않는다

업그레이드 문서는 문제를 피하기 위한 가장 싼 보험이다.

## 확인 문제

1. Spring Boot 3.5에서 `heapdump` endpoint를 사용하려면 무엇을 설정해야 하는가?
2. `.enabled` 계열 설정값은 어떤 값을 사용하는 것이 안전한가?
3. `spring.data.redis.url`과 `spring.data.redis.database`를 함께 쓸 때 무엇을 조심해야 하는가?
4. minor 버전 업그레이드에서도 release notes를 읽어야 하는 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Release Notes  
  https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.5-Release-Notes
- Spring Boot 3.5 System Requirements  
  https://docs.spring.io/spring-boot/3.5/system-requirements.html

## 다음 장으로

다음 장에서는 Spring Boot 학습과 실무에서 자주 만나는 애너테이션을 한 번에 정리한다.

---

## 문서 이동

- [이전: 59. Spring Boot 2에서 3으로 마이그레이션](./59_Spring_Boot_2에서_3으로_마이그레이션.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 61. 자주 쓰는 애너테이션 정리](./61_자주_쓰는_애너테이션_정리.md)
