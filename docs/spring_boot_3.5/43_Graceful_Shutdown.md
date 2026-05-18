# Graceful Shutdown

## 이번 장에서 배울 것

이 장에서는 Graceful Shutdown을 배운다.

서버를 종료할 때 실행 중인 요청을 갑자기 끊으면 사용자는 오류를 겪고, 데이터 처리 중간에 문제가 생길 수 있다. Graceful Shutdown은 종료 중에도 기존 요청이 마무리될 시간을 주는 기능이다.

## Graceful Shutdown이란 무엇인가

Graceful Shutdown은 애플리케이션 종료 시 다음처럼 동작하는 방식이다.

```text
1. 종료 신호를 받는다.
2. 새 요청은 더 이상 받지 않는다.
3. 이미 처리 중인 요청은 일정 시간 기다린다.
4. 시간이 지나거나 요청이 끝나면 애플리케이션을 종료한다.
```

즉 "지금 하던 일은 마무리하고 문을 닫자"에 가깝다.

## Spring Boot 3.5 기준

Spring Boot 3.5 공식 문서 기준으로 Graceful Shutdown은 Jetty, Reactor Netty, Tomcat, Undertow 네 가지 내장 웹 서버에서 기본적으로 활성화되어 있다.

Servlet 기반 웹 애플리케이션과 Reactive 웹 애플리케이션 모두 지원한다.

종료는 ApplicationContext가 닫히는 과정에서 수행된다.

## 종료 대기 시간 설정

종료 대기 시간은 다음 설정으로 조정한다.

```yaml
spring:
  lifecycle:
    timeout-per-shutdown-phase: 20s
```

이 설정은 각 shutdown phase마다 최대 20초까지 기다린다는 뜻이다.

너무 짧으면 긴 요청이 중간에 끊길 수 있고, 너무 길면 배포나 스케일 다운이 느려질 수 있다.

## 즉시 종료로 바꾸기

Graceful Shutdown을 끄고 즉시 종료하려면 다음처럼 설정한다.

```yaml
server:
  shutdown: immediate
```

일반적인 운영 서비스에서는 즉시 종료보다 Graceful Shutdown이 더 안전하다.

## SIGTERM

운영 환경에서는 보통 프로세스에 `SIGTERM` 신호를 보내 종료한다.

컨테이너 환경에서도 Kubernetes나 Docker가 종료 시 `SIGTERM`을 보낸다. Spring Boot는 이 신호를 받아 ApplicationContext를 닫고 종료 절차를 진행한다.

주의할 점은 IDE에서 정지 버튼을 누를 때는 환경에 따라 graceful하게 종료되지 않을 수 있다는 것이다. 공식 문서도 IDE가 적절한 `SIGTERM`을 보내지 않으면 즉시 종료될 수 있다고 설명한다.

## Kubernetes와 readiness

Kubernetes 같은 환경에서는 종료 중인 인스턴스로 새 요청이 들어가지 않게 해야 한다.

이를 위해 readiness probe를 사용한다.

Actuator health probe를 사용할 수 있다.

```yaml
management:
  endpoint:
    health:
      probes:
        enabled: true
  endpoints:
    web:
      exposure:
        include: health
```

대표 endpoint:

```text
/actuator/health/liveness
/actuator/health/readiness
```

- liveness: 애플리케이션이 살아 있는가
- readiness: 요청을 받을 준비가 되었는가

종료 시점에는 readiness가 먼저 내려가고, 로드밸런서가 새 요청을 보내지 않도록 구성하는 것이 좋다.

## 긴 요청과 비동기 작업

Graceful Shutdown은 기존 요청이 끝나도록 기다리지만, 모든 상황을 자동으로 완벽히 해결하지는 않는다.

주의할 작업:

- 긴 파일 업로드
- 대용량 다운로드
- 외부 API 호출
- 배치 작업
- 메시지 소비
- 비동기 스레드 작업

이런 작업은 timeout, 중단 가능성, 재시도 전략을 함께 설계해야 한다.

## 종료 로그 확인

운영에서는 종료 로그를 확인할 수 있어야 한다.

예:

```java
package com.example.todo.lifecycle;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ShutdownLogger {

    private static final Logger log =
            LoggerFactory.getLogger(ShutdownLogger.class);

    @PreDestroy
    public void onShutdown() {
        log.info("Application is shutting down");
    }
}
```

종료 시 필요한 정리 작업이 있다면 `@PreDestroy`나 lifecycle hook을 사용할 수 있다. 단, 너무 오래 걸리는 작업은 shutdown timeout과 충돌할 수 있다.

## 자주 하는 실수

### 로컬 IDE 종료만 보고 운영 종료를 판단한다

IDE 정지 버튼은 운영 종료 신호와 다를 수 있다.

### timeout을 너무 짧게 둔다

긴 요청이 많은 서비스에서는 요청이 중간에 끊길 수 있다.

### readiness 없이 graceful shutdown만 믿는다

로드밸런서나 Kubernetes가 종료 중인 인스턴스로 새 요청을 보내지 않도록 함께 구성해야 한다.

## 확인 문제

1. Graceful Shutdown은 무엇을 목표로 하는가?
2. 종료 대기 시간을 설정하는 Spring Boot 속성은 무엇인가?
3. Kubernetes에서 새 요청을 받을 준비 상태를 나타내는 probe는 무엇인가?
4. IDE 종료와 운영 종료 신호가 다를 수 있는 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Graceful Shutdown  
  https://docs.spring.io/spring-boot/3.5/reference/web/graceful-shutdown.html
- Spring Boot 3.5 Reference: Endpoints  
  https://docs.spring.io/spring-boot/3.5/reference/actuator/endpoints.html

## 다음 장으로

다음 장에서는 운영에서 자주 만나는 문제와 진단 순서를 정리한다.

---

## 문서 이동

- [이전: 42. 운영용 설정 관리](./42_운영용_설정_관리.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 44. 운영에서 자주 나는 문제](./44_운영에서_자주_나는_문제.md)
