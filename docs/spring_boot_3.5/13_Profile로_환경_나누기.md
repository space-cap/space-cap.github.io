# Profile로 환경 나누기

## 이번 장에서 배울 것

이 장에서는 Spring Profile을 사용해 환경별 설정을 나누는 법을 배운다.

개발할 때와 운영할 때는 설정이 다르다. 로컬에서는 H2를 쓰고, 운영에서는 PostgreSQL을 쓸 수 있다. 로컬에서는 로그를 자세히 보고, 운영에서는 필요한 수준만 남길 수 있다.

Profile은 이런 차이를 깔끔하게 분리하는 기능이다.

## Profile이 필요한 이유

하나의 애플리케이션도 실행 환경에 따라 설정이 달라진다.

```text
local: 내 컴퓨터에서 개발
dev: 팀 개발 서버
test: 테스트 자동화 환경
prod: 실제 사용자 운영 환경
```

환경마다 다른 값은 보통 다음과 같다.

- 서버 포트
- DB 주소
- DB 계정
- 로그 레벨
- 외부 API 주소
- 캐시 사용 여부
- 테스트용 더미 데이터 사용 여부

Profile을 쓰면 코드는 그대로 두고 설정만 환경별로 바꿀 수 있다.

## 기본 Profile

활성화된 Profile이 없으면 Spring Boot는 `default` Profile을 사용한다.

실행 로그에서 다음과 비슷한 문장을 볼 수 있다.

```text
No active profile set, falling back to 1 default profile: "default"
```

이 메시지는 오류가 아니다. 아무 Profile도 지정하지 않았으니 기본 Profile로 실행한다는 뜻이다.

## Profile별 파일 만들기

기본 설정:

```text
src/main/resources/application.yml
```

Profile별 설정:

```text
src/main/resources/application-local.yml
src/main/resources/application-dev.yml
src/main/resources/application-prod.yml
```

파일 이름 규칙은 다음과 같다.

```text
application-{profile}.yml
```

예를 들어 `local` Profile을 켜면 `application.yml`과 `application-local.yml`이 함께 사용된다.

## 기본 설정 작성

`application.yml`:

```yaml
spring:
  application:
    name: todo

server:
  port: 8080

logging:
  level:
    root: info
```

공통 설정은 `application.yml`에 둔다.

## local 설정 작성

`application-local.yml`:

```yaml
server:
  port: 8081

logging:
  level:
    com.example.todo: debug

todo:
  data-seed: true
```

로컬 개발에서만 필요한 값을 둔다.

## prod 설정 작성

`application-prod.yml`:

```yaml
server:
  port: 8080

logging:
  level:
    com.example.todo: info

todo:
  data-seed: false
```

운영 환경에서는 디버그 로그와 더미 데이터를 끄는 식으로 설정할 수 있다.

## Profile 활성화하기

명령행 인자로 활성화할 수 있다.

```bash
java -jar todo.jar --spring.profiles.active=local
```

Gradle로 실행할 때는 다음처럼 할 수 있다.

Windows PowerShell:

```powershell
.\gradlew.bat bootRun --args="--spring.profiles.active=local"
```

macOS 또는 Linux:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

실행 로그에서 활성 Profile을 확인한다.

```text
The following 1 profile is active: "local"
```

## 환경 변수로 Profile 활성화하기

운영에서는 환경 변수로 Profile을 주는 경우가 많다.

```bash
SPRING_PROFILES_ACTIVE=prod
```

Windows PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
```

## `spring.profiles.active`를 설정 파일에 적어도 될까?

가능하다.

```yaml
spring:
  profiles:
    active: local
```

하지만 실무에서는 주의가 필요하다. 운영 배포 파일 안에 `local`이 박혀 있으면 위험하다. 보통은 기본 파일에 고정하지 않고 실행 환경에서 지정하는 방식을 선호한다.

공식 문서 기준으로 `spring.profiles.active`와 `spring.profiles.default`는 Profile 전용 파일이나 `spring.config.activate.on-profile`로 활성화되는 문서 안에 둘 수 없다. 즉 `application-prod.yml` 안에서 다시 active Profile을 바꾸는 식의 사용은 피해야 한다.

## Profile 이름 규칙

Spring Boot 3.5 기준으로 Profile 이름은 기본적으로 다음 문자를 사용할 수 있다.

- 영어 문자
- 숫자
- `-`
- `_`
- `.`
- `+`
- `@`

또한 문자나 숫자로 시작하고 끝나야 한다.

처음에는 단순하게 `local`, `dev`, `test`, `prod`를 쓰는 것이 좋다.

## Profile Group

운영 설정이 복잡해지면 여러 Profile을 묶고 싶을 수 있다.

예:

```yaml
spring:
  profiles:
    group:
      production:
        - proddb
        - prodcache
        - prodmetrics
```

이제 다음처럼 실행하면:

```bash
java -jar todo.jar --spring.profiles.active=production
```

`production`, `proddb`, `prodcache`, `prodmetrics`가 함께 활성화된다.

초보 단계에서는 Profile Group을 당장 쓰지 않아도 된다. 먼저 `local`, `dev`, `prod` 분리부터 익숙해지자.

## 자주 하는 실수

### 운영 파일에 local Profile을 고정한다

`application.yml`에 `spring.profiles.active: local`을 넣고 그대로 운영에 배포하면 운영 설정이 적용되지 않을 수 있다.

### Profile 파일 이름을 틀린다

`application-local.yml`이어야 한다. `local-application.yml`이 아니다.

### 설정 우선순위를 오해한다

Profile별 파일은 기본 파일을 보완하고 덮어쓴다. 같은 설정이 있으면 활성 Profile의 설정이 우선 적용된다.

## 확인 문제

1. Profile을 사용하는 이유는 무엇인가?
2. `local` Profile용 설정 파일 이름은 무엇인가?
3. Profile을 명령행 인자로 활성화하는 설정 이름은 무엇인가?
4. 활성 Profile이 없을 때 사용되는 기본 Profile 이름은 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Profiles  
  https://docs.spring.io/spring-boot/3.5/reference/features/profiles.html

## 다음 장으로

다음 장에서는 로그를 읽고 남기는 법을 배운다. 로그는 Spring Boot 개발자의 첫 번째 진단 도구다.

---

## 문서 이동

- [이전: 12. 설정 파일과 외부 설정](./12_설정_파일과_외부_설정.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 14. 로깅 기초](./14_로깅_기초.md)
