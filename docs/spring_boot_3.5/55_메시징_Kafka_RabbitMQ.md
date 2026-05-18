# 메시징 Kafka/RabbitMQ

## 이번 장에서 배울 것

이번 장에서는 메시징의 기본 개념과 Spring Boot에서 RabbitMQ, Kafka를 사용하는 큰 흐름을 배운다.

메시징은 시스템끼리 직접 호출하지 않고 중간의 메시지 브로커를 통해 데이터를 주고받는 방식이다.

```text
서비스 A -> 메시지 브로커 -> 서비스 B
```

HTTP API가 "지금 바로 대답해 줘"에 가깝다면, 메시징은 "이 일을 맡겨 둘게. 가능한 쪽에서 처리해 줘"에 가깝다.

## 메시징이 필요한 이유

예를 들어 회원 가입 API가 있다고 하자.

회원 가입 후 해야 할 일이 많다.

- 환영 메일 발송
- 가입 이벤트 로그 저장
- 추천 쿠폰 발급
- CRM 시스템에 고객 정보 전달
- 알림 발송

이 모든 일을 회원 가입 요청 안에서 동기적으로 처리하면 응답이 느려지고, 하나가 실패해도 전체가 실패할 수 있다.

메시징을 사용하면 가입 처리는 먼저 끝내고, 후속 작업은 메시지로 분리할 수 있다.

## 핵심 용어

메시지:

```text
전달할 데이터
```

Producer:

```text
메시지를 보내는 쪽
```

Consumer:

```text
메시지를 받는 쪽
```

Broker:

```text
메시지를 보관하고 전달하는 중간 서버
```

Queue:

```text
메시지가 줄 서서 기다리는 공간
```

Topic:

```text
메시지를 주제별로 나누는 이름
```

## RabbitMQ와 Kafka의 차이

둘 다 메시징에 쓰이지만 철학이 다르다.

| 구분 | RabbitMQ | Kafka |
| --- | --- | --- |
| 중심 개념 | Queue, Exchange | Topic, Partition, Log |
| 강점 | 작업 분배, 라우팅 | 대용량 이벤트 스트림 |
| 흔한 사용처 | 비동기 작업 큐 | 이벤트 저장과 재처리 |
| 메시지 보관 | 소비되면 보통 제거 | 일정 기간 로그로 보관 |

초보자는 이렇게 기억하면 된다.

```text
RabbitMQ: 작업을 나눠 처리하는 큐에 강하다.
Kafka: 이벤트를 계속 쌓고 여러 소비자가 읽는 구조에 강하다.
```

## RabbitMQ 시작하기

의존성:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-amqp'
}
```

설정:

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

메시지 보내기:

```java
package com.example.todo.message;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TodoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public TodoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishTodoCreated(TodoCreatedEvent event) {
        rabbitTemplate.convertAndSend("todo.exchange", "todo.created", event);
    }
}
```

메시지 받기:

```java
package com.example.todo.message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TodoEventListener {

    @RabbitListener(queues = "todo.created.queue")
    public void handle(TodoCreatedEvent event) {
        System.out.println("할 일 생성 이벤트 수신: " + event.todoId());
    }
}
```

이벤트 DTO:

```java
public record TodoCreatedEvent(
        Long todoId,
        Long userId,
        String title
) {
}
```

## Kafka 시작하기

의존성:

```groovy
dependencies {
    implementation 'org.springframework.kafka:spring-kafka'
}
```

설정:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      group-id: todo-service
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: com.example.todo.message
```

메시지 보내기:

```java
package com.example.todo.message;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TodoKafkaPublisher {

    private final KafkaTemplate<String, TodoCreatedEvent> kafkaTemplate;

    public TodoKafkaPublisher(KafkaTemplate<String, TodoCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(TodoCreatedEvent event) {
        kafkaTemplate.send("todo-created", String.valueOf(event.todoId()), event);
    }
}
```

메시지 받기:

```java
package com.example.todo.message;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TodoKafkaListener {

    @KafkaListener(topics = "todo-created", groupId = "todo-service")
    public void handle(TodoCreatedEvent event) {
        System.out.println("Kafka 이벤트 수신: " + event.todoId());
    }
}
```

## 메시지는 DTO로 분리하자

Entity를 그대로 메시지로 보내지 말자.

좋지 않은 예:

```java
kafkaTemplate.send("todo-created", todoEntity);
```

좋은 예:

```java
new TodoCreatedEvent(todo.getId(), todo.getUserId(), todo.getTitle())
```

메시지는 서비스 간 계약이다. Entity는 내부 구현이다. 둘을 분리해야 변경에 강해진다.

## 메시징에서 중요한 것

메시징은 보내기만 하면 끝이 아니다.

운영에서 봐야 할 것:

- 메시지 중복 처리
- 메시지 순서
- 실패한 메시지 재시도
- Dead Letter Queue
- 소비자 처리 속도
- 메시지 스키마 변경
- 모니터링과 알림

특히 "메시지는 한 번만 처리된다"고 가정하면 위험하다. 대부분의 시스템에서는 중복 처리가 일어날 수 있다고 보고 멱등성을 설계한다.

## 멱등성

멱등성은 같은 작업을 여러 번 실행해도 결과가 한 번 실행한 것과 같은 성질이다.

예:

```text
쿠폰 발급 이벤트를 두 번 받아도 쿠폰은 한 번만 발급된다.
```

구현 방법:

- 이벤트 ID를 저장하고 중복 처리 여부 확인
- DB unique constraint 사용
- 처리 상태 테이블 사용
- 외부 API 호출 전 이미 처리했는지 확인

메시징을 실무에서 쓰려면 멱등성은 필수 개념이다.

## 트랜잭션과 메시지 발행

DB 저장과 메시지 발행을 함께 할 때는 조심해야 한다.

나쁜 상황:

```text
DB 저장 성공
메시지 발행 실패
```

또는:

```text
메시지 발행 성공
DB 저장 롤백
```

이런 문제를 줄이기 위해 실무에서는 outbox pattern을 사용하기도 한다. 먼저 DB에 이벤트를 함께 저장하고, 별도 프로세스가 이벤트 테이블을 읽어 메시지를 발행하는 방식이다.

## 자주 하는 실수

### 메시징을 쓰면 무조건 안정적이라고 생각한다

브로커도 장애가 날 수 있고, 메시지도 실패할 수 있다. 모니터링이 필요하다.

### 메시지 중복을 고려하지 않는다

consumer는 같은 메시지를 두 번 받을 수 있다고 생각하고 설계해야 한다.

### Entity를 메시지로 그대로 보낸다

서비스 내부 모델이 외부 계약이 되어 버린다.

### RabbitMQ와 Kafka를 이름만 보고 선택한다

작업 큐가 필요한지, 이벤트 로그가 필요한지 먼저 판단해야 한다.

## 확인 문제

1. Producer, Consumer, Broker는 각각 어떤 역할을 하는가?
2. RabbitMQ와 Kafka의 대표적인 차이는 무엇인가?
3. 메시지 DTO를 Entity와 분리해야 하는 이유는 무엇인가?
4. 멱등성이 메시징에서 중요한 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: AMQP  
  https://docs.spring.io/spring-boot/3.5/reference/messaging/amqp.html
- Spring Boot 3.5 Reference: Apache Kafka Support  
  https://docs.spring.io/spring-boot/3.5/reference/messaging/kafka.html

## 다음 장으로

다음 장에서는 대량 데이터를 안정적으로 처리하는 Spring Batch 입문을 배운다.

---

## 문서 이동

- [이전: 54. REST Client](./54_REST_Client.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 56. Batch 입문](./56_Batch_입문.md)
