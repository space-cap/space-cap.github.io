# Actuator 기초

## 이번 장에서 배울 것

이 장에서는 Spring Boot Actuator를 배운다.

애플리케이션을 운영하려면 "서버가 떠 있는가"만 봐서는 부족하다. DB 연결은 정상인지, 어떤 Bean이 등록되었는지, 어떤 설정이 적용되었는지, 메트릭은 어떤지 확인할 수 있어야 한다.

Spring Boot Actuator는 이런 운영용 관찰과 관리 기능을 제공한다.

## Actuator란 무엇인가

Actuator는 Spring Boot 애플리케이션의 운영 상태를 확인하고 관리하기 위한 기능 모음이다.

대표 기능:

- 상태 확인
- 애플리케이션 정보 확인
- 메트릭 확인
- 로그 레벨 확인과 변경
- 환경 설정 확인
- Bean 목록 확인
- 자동 설정 조건 확인
- Flyway, Liquibase 적용 내역 확인

공식 문서 기준으로 Actuator endpoint는 HTTP 또는 JMX로 노출할 수 있다. 대부분의 웹 애플리케이션은 HTTP endpoint를 사용한다.

## 의존성 추가

Gradle:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
}
```

애플리케이션을 실행하고 다음 주소에 접속한다.

```text
http://localhost:8080/actuator/health
```

응답 예:

```json
{
  "status": "UP"
}
```

`UP`은 애플리케이션이 정상 상태라는 뜻이다.

## 기본 경로

Actuator HTTP endpoint는 기본적으로 `/actuator` 아래에 매핑된다.

예:

```text
/actuator/health
/actuator/info
/actuator/metrics
/actuator/loggers
```

endpoint ID가 URL 경로가 된다.

## endpoint 노출

중요한 점이 있다. Actuator endpoint가 존재한다고 해서 모두 HTTP로 외부에 노출되는 것은 아니다.

Spring Boot 3.5 기준으로 HTTP와 JMX에는 기본적으로 `health` endpoint만 노출된다. 다른 endpoint를 HTTP로 보려면 명시적으로 노출해야 한다.

개발 환경 예:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,loggers
```

모든 endpoint 노출:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

학습 중에는 편하지만 운영에서는 매우 조심해야 한다. endpoint에는 민감한 정보가 포함될 수 있다.

## 자주 쓰는 endpoint

| endpoint | 설명 |
| --- | --- |
| `/actuator/health` | 애플리케이션 상태 |
| `/actuator/info` | 애플리케이션 정보 |
| `/actuator/metrics` | 메트릭 목록 |
| `/actuator/metrics/{name}` | 특정 메트릭 상세 |
| `/actuator/loggers` | 로거 목록과 로그 레벨 |
| `/actuator/env` | Environment 속성 |
| `/actuator/configprops` | ConfigurationProperties |
| `/actuator/beans` | Bean 목록 |
| `/actuator/conditions` | 자동 설정 조건 평가 |
| `/actuator/flyway` | Flyway 마이그레이션 정보 |

운영에서는 `health`, `info`, `metrics`, `prometheus` 정도만 제한적으로 노출하는 경우가 많다.

## health endpoint

`health`는 애플리케이션의 상태를 보여준다.

```bash
curl http://localhost:8080/actuator/health
```

기본 응답:

```json
{
  "status": "UP"
}
```

상세 정보를 보고 싶으면 다음 설정을 사용할 수 있다.

```yaml
management:
  endpoint:
    health:
      show-details: when-authorized
```

학습용으로 항상 보이게 할 수도 있다.

```yaml
management:
  endpoint:
    health:
      show-details: always
```

운영에서 `always`를 사용할 때는 보안 설정을 반드시 함께 고려한다.

## 커스텀 HealthIndicator

외부 API나 내부 조건을 health에 포함하고 싶을 수 있다.

```java
package com.example.todo.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TodoHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean available = true;

        if (available) {
            return Health.up()
                    .withDetail("todoService", "available")
                    .build();
        }

        return Health.down()
                .withDetail("todoService", "unavailable")
                .build();
    }
}
```

Bean 이름이 `todoHealthIndicator`라면 health 정보에는 보통 `todo`라는 이름으로 나타난다.

## info endpoint

`info` endpoint에 애플리케이션 정보를 넣을 수 있다.

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info

info:
  app:
    name: todo-api
    description: Spring Boot 3.5 study application
    version: 1.0.0
```

요청:

```bash
curl http://localhost:8080/actuator/info
```

## Actuator와 보안

Actuator endpoint는 운영 정보가 담길 수 있다.

특히 다음 endpoint는 노출에 주의한다.

- `env`
- `configprops`
- `beans`
- `heapdump`
- `threaddump`
- `loggers`

Spring Security를 사용한다면 Actuator endpoint에 별도 권한을 둘 수 있다.

```java
http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
        .requestMatchers("/actuator/**").hasRole("ADMIN")
        .anyRequest().authenticated()
);
```

## 자주 하는 실수

### 모든 endpoint를 운영에 그대로 노출한다

`include: "*"`는 학습과 로컬 확인에는 편하지만 운영에서는 위험할 수 있다.

### health만 보고 모든 것이 정상이라고 생각한다

health는 시작점이다. 메트릭, 로그, 트레이스와 함께 봐야 한다.

### Actuator endpoint 보안을 빼먹는다

운영 endpoint는 접근 제어와 네트워크 제한을 함께 고려한다.

## 확인 문제

1. Actuator는 어떤 목적의 기능인가?
2. Actuator HTTP endpoint의 기본 경로는 무엇인가?
3. 기본적으로 HTTP로 노출되는 대표 endpoint는 무엇인가?
4. 운영에서 `env`, `heapdump` 같은 endpoint 노출을 조심해야 하는 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Production-ready Features  
  https://docs.spring.io/spring-boot/3.5/reference/actuator/index.html
- Spring Boot 3.5 Reference: Endpoints  
  https://docs.spring.io/spring-boot/3.5/reference/actuator/endpoints.html

## 다음 장으로

다음 장에서는 Micrometer 기반 메트릭과 Prometheus, Grafana 흐름을 배운다.

