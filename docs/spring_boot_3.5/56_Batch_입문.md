# Batch 입문

## 이번 장에서 배울 것

이번 장에서는 Spring Batch의 기본 개념과 Spring Boot에서 Batch 애플리케이션을 시작하는 방법을 배운다.

Batch는 사용자의 요청에 즉시 응답하는 API와 다르다. 많은 데이터를 정해진 절차에 따라 안정적으로 처리하는 작업이다.

예:

- 매일 새벽 정산 파일 생성
- 오래된 데이터 아카이빙
- 대량 쿠폰 발급
- CSV 파일 읽고 DB 저장
- 외부 시스템에서 받은 데이터를 변환
- 실패한 작업 재처리

## Batch와 API의 차이

API는 보통 다음 성격을 가진다.

```text
사용자 요청 -> 빠른 응답
```

Batch는 다음 성격을 가진다.

```text
많은 데이터 -> 오래 걸려도 정확하게 처리
```

API에서는 응답 속도가 중요하다. Batch에서는 재시작, 실패 복구, 처리 이력, 데이터 정합성이 중요하다.

## Spring Batch 핵심 용어

Job:

```text
하나의 배치 작업 전체
```

Step:

```text
Job을 구성하는 한 단계
```

ItemReader:

```text
데이터를 읽는다
```

ItemProcessor:

```text
읽은 데이터를 가공한다
```

ItemWriter:

```text
처리한 데이터를 쓴다
```

JobRepository:

```text
배치 실행 상태와 이력을 저장한다
```

## 의존성 추가

Gradle:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-batch'
    runtimeOnly 'com.h2database:h2'
}
```

Spring Boot는 `spring-boot-starter-batch`가 classpath에 있으면 Spring Batch 자동 설정을 활성화한다.

## 간단한 Job 만들기

다음은 아주 단순한 Tasklet 기반 Job이다.

```java
package com.example.todo.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TodoBatchConfig {

    @Bean
    public Job cleanupJob(JobRepository jobRepository, Step cleanupStep) {
        return new JobBuilder("cleanupJob", jobRepository)
                .start(cleanupStep)
                .build();
    }

    @Bean
    public Step cleanupStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("오래된 할 일 정리");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
```

이 예제는 실제 데이터를 처리하지는 않지만 Job과 Step의 관계를 보여준다.

## 애플리케이션 시작 시 Job 실행

Spring Boot 공식 문서 기준으로 `spring-boot-starter-batch`를 추가하고 단일 `Job` Bean이 있으면 애플리케이션 시작 시 Job이 실행된다.

Job이 여러 개라면 실행할 Job 이름을 지정해야 한다.

```yaml
spring:
  batch:
    job:
      name: cleanupJob
```

Job 자동 실행을 끄고 싶다면:

```yaml
spring:
  batch:
    job:
      enabled: false
```

API 서버와 Batch 서버를 같은 코드베이스에서 운영할 때는 자동 실행 설정을 특히 조심해야 한다.

## Chunk 기반 처리

대량 데이터는 보통 chunk 방식으로 처리한다.

```text
10개 읽기 -> 10개 처리 -> 10개 쓰기 -> 커밋
다음 10개 읽기 -> 처리 -> 쓰기 -> 커밋
```

chunk는 "한 번에 몇 개씩 처리하고 커밋할 것인가"를 정하는 단위다.

개념 예:

```java
@Bean
public Step sampleStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager,
        ItemReader<TodoItem> reader,
        ItemProcessor<TodoItem, TodoSummary> processor,
        ItemWriter<TodoSummary> writer
) {
    return new StepBuilder("sampleStep", jobRepository)
            .<TodoItem, TodoSummary>chunk(100, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
}
```

100개씩 읽고 처리하고 저장한다.

## JobRepository와 데이터베이스

Spring Batch는 실행 이력을 저장해야 한다. 그래서 JobRepository가 필요하다.

저장되는 정보:

- 어떤 Job이 실행되었는지
- 시작 시간과 종료 시간
- 성공/실패 상태
- Step별 처리 건수
- 재시작에 필요한 상태

Spring Boot를 사용할 때도 실제 데이터 저장소가 필요하다. H2 같은 인메모리 DB도 가능하지만 운영에서는 PostgreSQL, MySQL 같은 DB를 사용한다.

## Job Parameter

Batch Job은 실행할 때 파라미터를 받을 수 있다.

명령행 예:

```bash
java -jar todo-batch.jar targetDate=2026-05-19
```

Spring Boot 공식 문서에서는 batch job arguments는 `--` 없이 일반 형식으로 전달하라고 안내한다.

좋은 Job Parameter:

- 처리 기준 날짜
- 파일 경로
- 실행 모드
- 대상 tenant ID

## 실패와 재시작

Batch는 실패할 수 있다.

예:

- 100만 건 중 50만 건 처리 후 DB 장애
- 외부 파일 형식 오류
- 특정 데이터만 검증 실패
- 네트워크 장애

Spring Batch는 JobRepository를 통해 실행 상태를 기록하고, 재시작 가능한 구조를 만들 수 있다.

초보 단계에서는 Batch를 "대량 반복문"으로만 보지 말고 "실패와 재시작을 관리하는 프레임워크"로 이해하자.

## API 서버와 Batch 서버 분리

작은 프로젝트에서는 같은 애플리케이션에서 Batch를 실행할 수 있다. 하지만 운영에서는 분리하는 경우가 많다.

```text
todo-api
todo-batch
```

분리하는 이유:

- Batch가 CPU와 DB 자원을 많이 쓴다.
- API 응답 성능에 영향을 줄 수 있다.
- 배포와 실행 주기가 다르다.
- 스케줄링/재시도 정책이 다르다.

## 자주 하는 실수

### Batch를 그냥 큰 for문으로 만든다

처리 이력, 실패 복구, 재시작이 필요하면 Spring Batch의 가치를 살려야 한다.

### Job 이름과 파라미터를 대충 정한다

같은 Job이 같은 파라미터로 이미 실행되었는지 여부가 중요하다.

### API 서버 시작과 동시에 Batch가 실행되는 것을 모른다

`spring.batch.job.enabled`와 `spring.batch.job.name`을 확인하자.

### 운영 DB와 Batch 부하를 분리하지 않는다

Batch가 큰 쿼리를 실행하면 API도 느려질 수 있다.

## 확인 문제

1. Job과 Step의 차이는 무엇인가?
2. ItemReader, ItemProcessor, ItemWriter는 각각 어떤 역할인가?
3. JobRepository가 필요한 이유는 무엇인가?
4. API와 Batch를 운영에서 분리하는 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 How-to: Batch Applications  
  https://docs.spring.io/spring-boot/3.5/how-to/batch.html

## 다음 장으로

다음 장에서는 Spring MVC와 다른 reactive 웹 기술인 WebFlux를 입문 수준에서 살펴본다.
