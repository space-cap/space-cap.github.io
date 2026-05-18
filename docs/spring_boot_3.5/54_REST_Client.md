# REST Client

## 이번 장에서 배울 것

이번 장에서는 Spring Boot 애플리케이션에서 외부 REST API를 호출하는 방법을 배운다.

서버 개발을 하다 보면 내 서비스가 다른 서비스를 호출해야 하는 일이 많다.

예:

- 결제 승인 API 호출
- 주소 검색 API 호출
- 환율 API 호출
- 사내 사용자 서비스 호출
- Slack, Discord, Telegram 알림 API 호출

Spring Boot 3.5 기준으로는 blocking 방식에는 `RestClient`, reactive 방식에는 `WebClient`를 주로 사용한다. 오래된 `RestTemplate`도 여전히 볼 수 있지만, 새 코드에서는 `RestClient`나 `WebClient`를 먼저 고려하자.

## RestClient와 WebClient

간단히 구분하면 다음과 같다.

| 도구 | 방식 | 주 사용처 |
| --- | --- | --- |
| `RestClient` | blocking | Spring MVC 기반 일반 애플리케이션 |
| `WebClient` | non-blocking reactive | WebFlux, reactive 흐름 |
| `RestTemplate` | blocking | 기존 레거시 코드 |

blocking은 호출 결과가 올 때까지 현재 스레드가 기다리는 방식이다.

non-blocking은 결과를 기다리는 동안 스레드를 붙잡아 두지 않는 방식이다.

초보자는 먼저 `RestClient`를 이해하면 된다.

## RestClient 사용 준비

`spring-boot-starter-web`이 있는 일반 MVC 애플리케이션이라면 `RestClient`를 사용할 수 있다.

서비스에서 `RestClient.Builder`를 주입받아 클라이언트를 만든다.

```java
package com.example.todo.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class QuoteClient {

    private final RestClient restClient;

    public QuoteClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.example.com")
                .build();
    }

    public QuoteResponse getQuote() {
        return restClient.get()
                .uri("/quotes/today")
                .retrieve()
                .body(QuoteResponse.class);
    }
}
```

응답 DTO:

```java
public record QuoteResponse(
        String text,
        String author
) {
}
```

## 요청 파라미터 보내기

```java
public WeatherResponse getWeather(String city) {
    return restClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/weather")
                    .queryParam("city", city)
                    .build())
            .retrieve()
            .body(WeatherResponse.class);
}
```

문자열을 직접 이어 붙이는 대신 `uriBuilder`를 사용하면 인코딩 실수를 줄일 수 있다.

## POST 요청 보내기

```java
public PaymentResponse approve(PaymentRequest request) {
    return restClient.post()
            .uri("/payments/approve")
            .body(request)
            .retrieve()
            .body(PaymentResponse.class);
}
```

요청 DTO:

```java
public record PaymentRequest(
        String orderId,
        long amount
) {
}
```

## 헤더 설정

```java
public UserProfile getProfile(String token) {
    return restClient.get()
            .uri("/me")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .body(UserProfile.class);
}
```

모든 요청에 공통 헤더가 필요하다면 Builder에서 기본 헤더를 설정할 수 있다.

```java
this.restClient = builder
        .baseUrl("https://api.example.com")
        .defaultHeader("X-Client-Name", "todo-api")
        .build();
```

## 에러 처리

외부 API는 언제든 실패할 수 있다.

```java
public QuoteResponse getQuote() {
    return restClient.get()
            .uri("/quotes/today")
            .retrieve()
            .onStatus(status -> status.value() == 404, (request, response) -> {
                throw new IllegalStateException("오늘의 문구를 찾을 수 없습니다.");
            })
            .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                throw new IllegalStateException("외부 문구 서비스가 불안정합니다.");
            })
            .body(QuoteResponse.class);
}
```

초보자는 "외부 API 호출 코드는 항상 실패 가능성을 포함해야 한다"는 사실을 먼저 기억하자.

## 타임아웃 설정

외부 API 호출에서 타임아웃은 필수다. 기본값에만 의존하면 요청 스레드가 오래 묶일 수 있다.

Spring Boot 3.5는 HTTP client 설정을 자동 감지하고 여러 클라이언트 구현을 지원한다. 프로젝트에서 사용하는 HTTP 클라이언트에 맞춰 connect timeout, read timeout을 설정하자.

개념적으로 필요한 값은 다음 두 가지다.

- connect timeout: 연결을 맺는 데 기다릴 최대 시간
- read timeout: 응답 데이터를 읽는 데 기다릴 최대 시간

운영에서는 "느린 외부 API 때문에 내 서비스까지 멈추지 않게 하는 것"이 핵심이다.

## WebClient 사용하기

WebFlux가 classpath에 있으면 Spring Boot는 `WebClient.Builder`를 미리 설정해서 제공한다.

```java
package com.example.todo.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ReactiveQuoteClient {

    private final WebClient webClient;

    public ReactiveQuoteClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://api.example.com")
                .build();
    }

    public Mono<QuoteResponse> getQuote() {
        return webClient.get()
                .uri("/quotes/today")
                .retrieve()
                .bodyToMono(QuoteResponse.class);
    }
}
```

`Mono<QuoteResponse>`는 "나중에 하나의 `QuoteResponse`가 올 수 있다"는 reactive 타입이다.

## RestClient를 선택할까 WebClient를 선택할까

일반적인 Spring MVC CRUD API:

```text
RestClient 추천
```

WebFlux 기반 reactive 애플리케이션:

```text
WebClient 추천
```

외부 API 호출이 매우 많고 non-blocking 이점이 필요한 서비스:

```text
WebClient 고려
```

이미 MVC 기반이고 팀이 reactive에 익숙하지 않다면 무리해서 WebClient를 도입하지 않아도 된다. 기술 선택은 팀이 운영할 수 있어야 한다.

## 클라이언트 코드를 분리하기

컨트롤러나 서비스에 외부 API 호출 코드를 직접 넣으면 테스트와 유지보수가 어려워진다.

권장 구조:

```text
controller
  -> service
      -> external client
```

예:

```java
@Service
public class TodoSummaryService {

    private final QuoteClient quoteClient;

    public TodoSummaryService(QuoteClient quoteClient) {
        this.quoteClient = quoteClient;
    }

    public TodoSummary getSummary() {
        QuoteResponse quote = quoteClient.getQuote();
        return new TodoSummary(quote.text());
    }
}
```

테스트에서는 `QuoteClient`를 mock으로 바꿀 수 있다.

## 재시도와 회로 차단

외부 API 호출은 실패할 수 있다. 하지만 무조건 재시도하면 더 큰 장애가 된다.

실무에서는 다음 도구를 함께 고려한다.

- timeout
- retry
- circuit breaker
- rate limit
- fallback

초보 단계에서는 먼저 timeout과 명확한 예외 처리를 넣자. 그 다음 Resilience4j 같은 도구를 학습하면 된다.

## 자주 하는 실수

### URL 문자열을 직접 이어 붙인다

쿼리 파라미터 인코딩 문제가 생길 수 있다. `uriBuilder`를 사용하자.

### 외부 API 실패를 생각하지 않는다

외부 서비스는 내 코드보다 더 자주, 더 이상한 방식으로 실패할 수 있다.

### 타임아웃 없이 호출한다

외부 API가 응답하지 않으면 내 요청 스레드도 묶인다.

### WebClient를 쓰면서 `block()`을 남발한다

reactive 흐름에서 `block()`을 남발하면 WebClient를 쓰는 장점이 줄어든다. MVC 애플리케이션이면 차라리 `RestClient`가 더 단순할 수 있다.

## 확인 문제

1. `RestClient`와 `WebClient`의 가장 큰 차이는 무엇인가?
2. 외부 API 호출에 타임아웃이 필요한 이유는 무엇인가?
3. 컨트롤러에서 직접 외부 API를 호출하지 않는 것이 좋은 이유는 무엇인가?
4. WebFlux가 아닌 일반 MVC 애플리케이션에서 먼저 고려할 수 있는 클라이언트는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Calling REST Services  
  https://docs.spring.io/spring-boot/3.5/reference/io/rest-client.html

## 다음 장으로

다음 장에서는 HTTP 요청/응답을 넘어 메시지 기반으로 시스템을 연결하는 Kafka와 RabbitMQ 입문을 배운다.
