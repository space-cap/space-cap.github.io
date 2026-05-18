# Todo API

Spring Boot 3.5.x 학습 문서의 최종 프로젝트를 구현하기 위한 예제 프로젝트입니다.

현재 단계에서는 가장 작은 실행 가능한 뼈대를 제공합니다.

- Spring Boot 3.5.14
- Java 21
- Spring Web
- Spring Validation
- Spring Data JPA
- Flyway
- H2 local database
- PostgreSQL profile
- Docker Compose for PostgreSQL
- Actuator
- 기본 Todo CRUD API
- 공통 예외 응답

## 실행

Java 21과 Gradle 8.x를 권장합니다. Spring Boot 3.5.x는 Gradle 7.6.4 이상 또는 Gradle 8.4 이상 계열을 지원합니다.

Gradle이 설치되어 있다면 다음 명령으로 실행합니다.

```bash
gradle bootRun
```

Windows:

```powershell
gradle bootRun
```

애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

## PostgreSQL로 실행

PostgreSQL을 Docker Compose로 실행합니다.

```bash
docker compose up -d
```

`postgres` profile로 애플리케이션을 실행합니다.

```bash
gradle bootRun --args='--spring.profiles.active=postgres'
```

Windows PowerShell:

```powershell
gradle bootRun --args='--spring.profiles.active=postgres'
```

## API 확인

Health check:

```bash
curl http://localhost:8080/actuator/health
```

Todo 생성:

```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Spring Boot 공부하기\",\"description\":\"최종 프로젝트 뼈대 실행\"}"
```

Todo 목록:

```bash
curl http://localhost:8080/api/todos
```

Todo 상세:

```bash
curl http://localhost:8080/api/todos/1
```

Todo 수정:

```bash
curl -X PATCH http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Spring Boot 3.5 공부하기\",\"description\":\"DTO와 예외 처리까지 확인\"}"
```

Todo 완료:

```bash
curl -X PATCH http://localhost:8080/api/todos/1/complete
```

Todo 삭제:

```bash
curl -X DELETE http://localhost:8080/api/todos/1
```

## H2 Console

로컬 실행 시 H2 Console을 사용할 수 있습니다.

```text
http://localhost:8080/h2-console
```

접속 정보:

```text
JDBC URL: jdbc:h2:mem:todo
User Name: sa
Password:
```

## 다음 구현 후보

이 뼈대 위에 다음 기능을 순서대로 붙이면 됩니다.

1. 사용자 도메인 추가
2. Spring Security 기본 설정
3. 로그인 또는 JWT 인증
4. 사용자별 Todo 분리
5. PostgreSQL profile
6. Docker Compose
7. Testcontainers 통합 테스트
8. 운영용 Actuator 설정 강화
