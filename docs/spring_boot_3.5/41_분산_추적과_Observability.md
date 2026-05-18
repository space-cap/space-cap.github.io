# 분산 추적과 Observability

## 이번 장에서 배울 것

이 장에서는 Observability와 분산 추적을 배운다.

운영에서 중요한 질문은 단순하다.

```text
무슨 일이 일어나고 있는가?
왜 느린가?
어디서 실패했는가?
```

Observability는 이 질문에 답하기 위한 관찰 가능성을 말한다.

## Observability의 세 축

운영 관찰의 대표 축은 세 가지다.

| 축 | 설명 |
| --- | --- |
| Logs | 개별 사건 기록 |
| Metrics | 숫자로 보는 상태 |
| Traces | 요청이 지나간 경로 |

로그는 자세하지만 전체 경향을 보기 어렵다. 메트릭은 경향을 잘 보여주지만 개별 요청의 흐름을 알기 어렵다. 트레이스는 한 요청이 여러 컴포넌트를 지나가는 길을 보여준다.

## Trace와 Span

분산 추적에서 자주 나오는 단어가 trace와 span이다.

### Trace

하나의 요청 전체 흐름이다.

예:

```text
사용자 요청 -> API 서버 -> DB -> 외부 결제 API -> 응답
```

이 전체가 하나의 trace다.

### Span

trace 안의 개별 작업 단위다.

예:

```text
HTTP 요청 처리 span
DB 조회 span
외부 API 호출 span
```

trace는 여러 span으로 구성된다.

## Micrometer Tracing

Spring Boot Actuator는 Micrometer Tracing 자동 설정을 제공한다. Micrometer Tracing은 여러 추적 라이브러리를 추상화하는 facade다.

Spring Boot 3.5 공식 문서 기준으로 다음 tracer 자동 설정을 지원한다.

- OpenTelemetry
- OpenZipkin Brave

추적 데이터는 Zipkin, OTLP, Wavefront 같은 백엔드로 보낼 수 있다.

## OpenTelemetry와 Zipkin 예제

의존성:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'io.micrometer:micrometer-tracing-bridge-otel'
    implementation 'io.opentelemetry:opentelemetry-exporter-zipkin'
}
```

설정:

```yaml
management:
  tracing:
    sampling:
      probability: 1.0
```

`probability: 1.0`은 모든 요청을 샘플링한다는 뜻이다. 학습용으로는 좋지만 운영에서는 비용을 고려해 낮출 수 있다.

## Zipkin 실행

Docker로 Zipkin을 실행할 수 있다.

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```

Zipkin UI:

```text
http://localhost:9411
```

Spring Boot 애플리케이션에서 요청을 발생시킨 뒤 Zipkin에서 trace를 확인한다.

## 로그와 trace id

Tracing을 설정하면 로그에 trace id와 span id를 함께 남길 수 있다.

이 값이 있으면 다음 흐름이 가능하다.

```text
에러 로그 발견
-> trace id 확인
-> Zipkin/Grafana Tempo/Jaeger에서 같은 trace 검색
-> 어떤 구간에서 느렸는지 확인
```

장애 분석에서 매우 강력하다.

## HTTP 클라이언트와 추적

분산 추적은 서버 내부만 보는 것이 아니다. 애플리케이션이 다른 서비스로 HTTP 요청을 보낼 때도 trace context가 전파되어야 한다.

Spring Boot와 Micrometer Observation을 잘 연동하면 `RestClient`, `WebClient` 같은 클라이언트 호출에서도 관찰 정보가 이어질 수 있다.

초보 단계에서는 먼저 서버 요청 trace를 보고, 이후 외부 API 호출 trace를 확장해도 충분하다.

## 샘플링

모든 요청을 추적하면 비용이 커질 수 있다.

샘플링 확률:

```yaml
management:
  tracing:
    sampling:
      probability: 0.1
```

`0.1`은 대략 10% 요청을 추적한다는 의미다.

운영에서는 트래픽, 저장 비용, 장애 분석 필요성을 기준으로 조절한다.

## 로그, 메트릭, 트레이스 함께 보기

세 도구는 서로 보완한다.

예:

```text
메트릭: 5xx 오류율 증가 발견
로그: 어떤 예외가 발생했는지 확인
트레이스: 어느 서비스와 DB 호출에서 느려졌는지 확인
```

하나만으로 모든 것을 해결하려 하지 않는다.

## 자주 하는 실수

### tracing을 켰는데 아무 곳에서도 보지 않는다

추적 데이터는 Zipkin, Tempo, Jaeger 같은 백엔드에 보내고 검색할 수 있어야 가치가 있다.

### 운영에서 샘플링을 무조건 1.0으로 둔다

트래픽이 많으면 저장 비용과 성능 비용이 커질 수 있다.

### 로그, 메트릭, 트레이스를 따로 본다

trace id를 중심으로 서로 연결해서 보는 습관이 중요하다.

## 확인 문제

1. Observability의 대표 세 축은 무엇인가?
2. trace와 span의 차이는 무엇인가?
3. Spring Boot Actuator가 제공하는 tracing 추상화는 무엇인가?
4. 운영에서 tracing sampling probability를 조절해야 하는 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Tracing  
  https://docs.spring.io/spring-boot/3.5/reference/actuator/tracing.html

## 다음 장으로

다음 장에서는 운영 환경에서 설정을 안전하게 관리하는 방법을 배운다.

---

## 문서 이동

- [이전: 40. 모니터링과 메트릭](./40_모니터링과_메트릭.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 42. 운영용 설정 관리](./42_운영용_설정_관리.md)
