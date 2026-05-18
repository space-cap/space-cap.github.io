# Docker Compose로 로컬 환경 구성

## 이번 장에서 배울 것

이 장에서는 Docker Compose로 Spring Boot 애플리케이션과 PostgreSQL을 함께 실행하는 방법을 배운다.

실무 애플리케이션은 혼자 실행되지 않는다. 보통 DB, Redis, 메시지 브로커 같은 주변 서비스가 필요하다. Docker Compose는 이런 로컬 개발 환경을 한 파일로 정의하고 실행하게 해준다.

## Docker Compose란 무엇인가

Docker Compose는 여러 컨테이너를 함께 정의하고 실행하는 도구다.

예:

```text
todo-api
postgres
redis
```

각 컨테이너를 따로 `docker run`으로 실행하지 않고 `compose.yml`에 정의한 뒤 한 번에 실행할 수 있다.

```bash
docker compose up
```

## PostgreSQL만 Compose로 실행하기

처음에는 애플리케이션은 IDE에서 실행하고, DB만 Compose로 띄워도 좋다.

`compose.yml`:

```yaml
services:
  postgres:
    image: postgres:16
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: todo
      POSTGRES_USER: todo
      POSTGRES_PASSWORD: todo
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

실행:

```bash
docker compose up -d
```

상태 확인:

```bash
docker compose ps
```

종료:

```bash
docker compose down
```

데이터까지 삭제:

```bash
docker compose down -v
```

`-v`는 볼륨을 삭제하므로 조심한다.

## Spring Boot 설정

`application-local.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/todo
    username: todo
    password: todo

  jpa:
    hibernate:
      ddl-auto: validate

  flyway:
    enabled: true
```

로컬에서 애플리케이션 실행:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

Windows:

```powershell
.\gradlew.bat bootRun --args="--spring.profiles.active=local"
```

## 애플리케이션까지 Compose로 실행하기

먼저 이미지를 만든다.

```bash
./gradlew clean bootJar
docker build -t todo-api:0.0.1 .
```

`compose.yml`:

```yaml
services:
  app:
    image: todo-api:0.0.1
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: docker
      DB_URL: jdbc:postgresql://postgres:5432/todo
      DB_USERNAME: todo
      DB_PASSWORD: todo
    depends_on:
      - postgres

  postgres:
    image: postgres:16
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: todo
      POSTGRES_USER: todo
      POSTGRES_PASSWORD: todo
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

`application-docker.yml`:

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

중요한 차이:

```text
로컬 IDE에서 DB 접속: localhost:5432
Compose 안 app 컨테이너에서 DB 접속: postgres:5432
```

Compose 네트워크 안에서는 서비스 이름 `postgres`가 호스트 이름처럼 동작한다.

## depends_on의 한계

`depends_on`은 컨테이너 시작 순서를 제어한다. 하지만 PostgreSQL이 완전히 준비될 때까지 기다려준다는 뜻은 아니다.

애플리케이션이 DB 준비 전에 시작하면 일시적으로 연결 실패가 날 수 있다.

대응 방법:

- 애플리케이션의 DB 연결 재시도 설정
- healthcheck 사용
- 초기 시작 순서 조정
- Spring Boot Docker Compose 지원 활용 검토

## Spring Boot Docker Compose 지원

Spring Boot는 개발 시 Docker Compose를 감지하고 관련 서비스를 연결하는 기능을 제공한다.

의존성:

```groovy
developmentOnly 'org.springframework.boot:spring-boot-docker-compose'
```

이 기능은 로컬 개발 편의를 위한 것이다. 운영 배포에서 Compose를 그대로 쓰는 것과는 다르다.

처음에는 직접 Compose 파일과 datasource 설정을 이해한 뒤, 편의 기능을 사용하는 편이 좋다.

## Redis 추가 예시

나중에 캐시를 배울 때 Redis가 필요할 수 있다.

```yaml
services:
  redis:
    image: redis:7
    ports:
      - "6379:6379"
```

이처럼 Compose는 개발에 필요한 주변 서비스를 계속 확장하기 좋다.

## 자주 하는 실수

### 컨테이너 안에서 localhost를 DB로 사용한다

app 컨테이너 안의 `localhost`는 app 컨테이너 자신이다. DB 컨테이너에 접근하려면 서비스 이름을 사용한다.

### 볼륨을 삭제하고 데이터가 사라져 당황한다

`docker compose down -v`는 볼륨을 삭제한다.

### depends_on이 DB 준비 완료까지 보장한다고 믿는다

시작 순서와 준비 완료는 다르다.

## 확인 문제

1. Docker Compose는 어떤 문제를 해결하는가?
2. Compose 네트워크 안에서 app 컨테이너가 PostgreSQL에 접근할 때 호스트 이름으로 무엇을 사용할 수 있는가?
3. `docker compose down -v`가 위험할 수 있는 이유는 무엇인가?
4. `depends_on`의 한계는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 How-to: Docker Compose  
  https://docs.spring.io/spring-boot/3.5/how-to/docker-compose.html

## 다음 장으로

다음 장에서는 클라우드 배포 선택지를 큰 그림으로 정리한다.

