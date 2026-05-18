# Spring과 Spring Boot의 탄생

## 이번 장에서 배울 것

이 장에서는 Spring Framework와 Spring Boot가 왜 등장했는지 살펴본다. 역사 이야기는 단순한 배경지식이 아니다. 어떤 문제가 있었고, Spring이 무엇을 해결했으며, Spring Boot가 다시 무엇을 단순하게 만들었는지를 알면 지금의 코드가 훨씬 자연스럽게 읽힌다.

## Spring 이전의 Java 서버 개발

Java는 오래전부터 기업용 서버 개발에 많이 쓰였다. 안정적이고, 타입이 강하고, 대규모 팀 개발에 적합했기 때문이다.

하지만 초기 Java 엔터프라이즈 개발은 무겁고 복잡했다. 객체 하나를 만들고 연결하는 데도 많은 설정이 필요했고, 애플리케이션 서버에 배포하는 과정도 번거로웠다.

개발자는 비즈니스 로직보다 다음과 같은 일에 많은 시간을 썼다.

- 객체 생성과 연결
- 트랜잭션 관리
- 데이터베이스 연결
- 보안 설정
- 서버 배포 설정
- XML 설정 관리

좋은 프레임워크는 개발자가 반복적인 기반 작업보다 중요한 비즈니스 문제에 집중하게 해준다. Spring은 바로 이 지점에서 등장했다.

## Spring Framework가 해결한 문제

Spring의 핵심 아이디어는 간단하다.

"객체를 직접 만들고 연결하지 말고, Spring Container가 대신 관리하게 하자."

이 개념을 IoC, 즉 제어의 역전이라고 부른다. 개발자가 `new`로 모든 객체를 직접 만들지 않고, Spring에게 객체의 생성과 연결을 맡긴다.

예를 들어 Controller가 Service를 필요로 한다면, Controller가 직접 Service를 만들지 않는다. Spring이 Service 객체를 만들고 Controller에 넣어 준다. 이것이 의존성 주입, 즉 DI다.

Spring은 다음 문제들을 크게 줄였다.

- 객체 간 결합도 감소
- 트랜잭션 처리 단순화
- 테스트하기 좋은 구조 제공
- 웹, 데이터, 보안 기능의 일관된 추상화
- 엔터프라이즈 개발의 반복 코드 감소

## 그런데 왜 Spring Boot가 필요했을까?

Spring Framework는 강력하지만, 초기 설정이 만만치 않았다.

웹 애플리케이션 하나를 만들려면 다음을 직접 준비해야 했다.

- 어떤 Spring MVC 라이브러리를 쓸지
- 어떤 버전의 Jackson을 쓸지
- 어떤 Servlet 컨테이너에 배포할지
- 어떤 설정 클래스를 만들지
- 어떤 XML 또는 Java Config를 작성할지
- 실행 가능한 패키지는 어떻게 만들지

Spring은 유연했지만, 그 유연함 때문에 초보자는 시작하기 어려웠다. 실무 팀도 프로젝트마다 비슷한 설정을 반복했다.

Spring Boot는 이 문제를 해결하기 위해 등장했다.

## Spring Boot의 핵심 철학

Spring Boot의 철학은 다음 문장으로 요약할 수 있다.

"대부분의 프로젝트에 맞는 좋은 기본값을 제공하고, 필요할 때만 바꾸게 하자."

이 철학은 다음 기능으로 나타난다.

- Starter로 관련 라이브러리를 한 번에 추가
- Auto Configuration으로 필요한 Bean 자동 등록
- 내장 Tomcat으로 별도 WAS 없이 실행
- 실행 가능한 JAR 패키징
- `application.yml` 또는 `application.properties`를 통한 간단한 설정
- Actuator로 운영 상태 확인

즉 Spring Boot는 Spring을 대체하는 별개의 프레임워크가 아니다. Spring을 더 빠르고 안전하게 시작하도록 돕는 도구다.

## Spring Boot 3.x의 중요한 변화

Spring Boot 3.x는 단순한 버전 업그레이드가 아니다. Java 생태계의 큰 변화가 반영되었다.

중요한 변화는 다음과 같다.

- Java 17 이상 필요
- Spring Framework 6 기반
- `javax.*`에서 `jakarta.*` 패키지로 전환
- 최신 관찰 가능성, 네이티브 이미지, 컨테이너 환경 지원 강화

특히 `javax.servlet`을 쓰던 과거 코드는 Spring Boot 3.x에서 `jakarta.servlet`로 바뀌어야 한다. 이 변화는 Spring Boot 2.x에서 3.x로 넘어올 때 가장 자주 만나는 문제 중 하나다.

## Spring Boot 3.5.x는 어디쯤에 있을까?

Spring Boot 3.5.x는 Spring Boot 3 계열의 성숙한 버전이다. Java 17 이상을 요구하고, Spring Framework 6.2 이상을 기반으로 한다.

2026-05-18 기준 공식 3.5 문서는 Spring Boot 3.5.14를 가리킨다. 공식 문서에는 최신 안정 버전으로 Spring Boot 4.0.6도 표시되지만, 이 문서 모음은 실무에서 많이 유지되는 3.x 계열을 안정적으로 배우는 데 초점을 둔다.

## 핵심 정리

- Spring은 객체 생성, 의존성 연결, 트랜잭션 같은 반복적인 엔터프라이즈 문제를 해결했다.
- Spring Boot는 Spring 프로젝트의 초기 설정과 운영 준비를 크게 단순화했다.
- Spring Boot는 Spring을 대체하지 않는다. Spring을 더 쉽게 쓰게 해주는 시작점이다.
- Spring Boot 3.x부터는 Java 17 이상과 Jakarta EE 기반 변화를 반드시 이해해야 한다.

## 자주 하는 실수

### Spring과 Spring Boot를 완전히 다른 기술로 생각한다

Spring Boot는 Spring 위에서 동작한다. Controller, Service, Bean, DI, Transaction 같은 핵심 개념은 Spring Framework의 개념이다.

### Spring Boot가 모든 것을 자동으로 해결한다고 생각한다

Spring Boot는 좋은 기본값을 제공하지만, 애플리케이션의 구조와 책임 분리는 개발자가 결정해야 한다.

### 예전 블로그 글을 그대로 따라 한다

Spring Boot 2.x 글에는 `javax.*` 패키지가 자주 나온다. Spring Boot 3.5.x에서는 `jakarta.*`를 사용해야 하는 경우가 많다.

## 확인 문제

1. Spring Framework가 해결한 가장 중요한 문제는 무엇인가?
2. Spring Boot가 Spring보다 먼저 배워도 되는 이유는 무엇인가?
3. Spring Boot 3.x에서 `javax.*`와 `jakarta.*` 중 어느 쪽을 주로 사용하는가?
4. Spring Boot가 내장 Tomcat을 제공하면 배포 방식이 어떻게 단순해지는가?

## 다음 장으로

다음 장에서는 Spring Boot 3.5.x에서 특히 알아야 할 기준 환경과 변화 지점을 정리한다.

---

## 문서 이동

- [이전: 01. Spring Boot 3.5.x 학습 로드맵](./01_Spring_Boot_3_5_학습_로드맵.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 03. Spring Boot 3.5.x에서 달라진 점](./03_Spring_Boot_3_5에서_달라진_점.md)
