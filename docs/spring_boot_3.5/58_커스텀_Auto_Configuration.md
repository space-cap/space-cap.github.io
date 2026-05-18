# 커스텀 Auto Configuration

## 이번 장에서 배울 것

이번 장에서는 Spring Boot의 자동 설정을 직접 만드는 방법을 배운다.

앞에서 우리는 Spring Boot가 많은 것을 자동으로 설정해 준다고 배웠다. 이제는 반대로, 우리가 만든 라이브러리도 Spring Boot 프로젝트에서 자동으로 설정되게 만드는 방법을 살펴본다.

이 주제는 초보자에게 조금 어렵다. 하지만 자동 설정의 원리를 이해하면 Spring Boot가 더 이상 마법처럼 보이지 않는다.

## 언제 커스텀 자동 설정이 필요한가

일반 애플리케이션 개발에서는 직접 만들 일이 많지 않다.

하지만 다음 상황에서는 유용하다.

- 회사 공통 라이브러리를 만든다.
- 여러 서비스가 같은 설정을 반복한다.
- 특정 SDK를 Spring Boot에서 쉽게 쓰게 만들고 싶다.
- 공통 로깅, 인증, 추적, 클라이언트 설정을 starter로 배포한다.
- 오픈소스 라이브러리에 Spring Boot starter를 제공한다.

예를 들어 사내 결제 클라이언트가 있다고 하자. 모든 서비스마다 다음 설정을 반복하면 번거롭다.

```java
@Bean
PaymentClient paymentClient(...) {
    ...
}
```

커스텀 자동 설정을 만들면 의존성만 추가해도 `PaymentClient` Bean이 자동 등록되게 할 수 있다.

## 자동 설정의 핵심 원칙

자동 설정은 다음 태도를 가져야 한다.

```text
필요한 조건이 맞으면 기본값을 제공한다.
사용자가 직접 설정하면 물러난다.
```

Spring Boot 자동 설정에서 자주 보는 조건:

- `@ConditionalOnClass`: 특정 클래스가 classpath에 있을 때
- `@ConditionalOnMissingBean`: 사용자가 같은 Bean을 직접 만들지 않았을 때
- `@ConditionalOnProperty`: 특정 설정 값이 켜져 있을 때
- `@ConditionalOnWebApplication`: 웹 애플리케이션일 때

## 예제 목표

간단한 `GreetingClient` 자동 설정을 만들어 보자.

사용자는 의존성을 추가하고 설정만 쓰면 된다.

```yaml
greeting:
  enabled: true
  prefix: Hello
```

그리고 애플리케이션에서 바로 주입받는다.

```java
@RestController
public class GreetingController {

    private final GreetingClient greetingClient;

    public GreetingController(GreetingClient greetingClient) {
        this.greetingClient = greetingClient;
    }

    @GetMapping("/greeting")
    public String greeting() {
        return greetingClient.greet("Spring Boot");
    }
}
```

## 라이브러리 클래스 만들기

```java
package com.example.greeting;

public class GreetingClient {

    private final String prefix;

    public GreetingClient(String prefix) {
        this.prefix = prefix;
    }

    public String greet(String name) {
        return prefix + ", " + name;
    }
}
```

## 설정 프로퍼티 만들기

```java
package com.example.greeting.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "greeting")
public class GreetingProperties {

    private boolean enabled = true;

    private String prefix = "Hello";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
```

## Auto Configuration 클래스 만들기

```java
package com.example.greeting.autoconfigure;

import com.example.greeting.GreetingClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(GreetingClient.class)
@EnableConfigurationProperties(GreetingProperties.class)
@ConditionalOnProperty(prefix = "greeting", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GreetingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GreetingClient greetingClient(GreetingProperties properties) {
        return new GreetingClient(properties.getPrefix());
    }
}
```

중요한 부분은 `@ConditionalOnMissingBean`이다. 사용자가 직접 `GreetingClient` Bean을 만들었다면 자동 설정은 물러나야 한다.

## AutoConfiguration.imports 등록

Spring Boot 3.x 방식에서는 다음 파일에 자동 설정 클래스를 등록한다.

```text
src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

파일 내용:

```text
com.example.greeting.autoconfigure.GreetingAutoConfiguration
```

공식 문서 기준으로 Spring Boot는 published jar 안에서 이 파일을 찾아 자동 설정 후보를 읽는다.

## starter와 autoconfigure 분리

실무에서는 보통 두 모듈로 나눈다.

```text
greeting-spring-boot-autoconfigure
greeting-spring-boot-starter
```

`autoconfigure` 모듈:

```text
자동 설정 코드
ConfigurationProperties
조건부 Bean 등록
```

`starter` 모듈:

```text
사용자가 추가할 의존성 묶음
autoconfigure 모듈 의존
필요 라이브러리 의존
```

사용자는 starter 하나만 추가한다.

```groovy
dependencies {
    implementation 'com.example:greeting-spring-boot-starter:1.0.0'
}
```

## 자동 설정 테스트

자동 설정은 테스트가 중요하다. `ApplicationContextRunner`를 사용하면 작은 컨텍스트를 띄워 조건을 검증할 수 있다.

```java
package com.example.greeting.autoconfigure;

import com.example.greeting.GreetingClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class GreetingAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(GreetingAutoConfiguration.class));

    @Test
    void createsGreetingClientByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(GreetingClient.class);
            assertThat(context.getBean(GreetingClient.class).greet("Boot"))
                    .isEqualTo("Hello, Boot");
        });
    }

    @Test
    void backsOffWhenUserProvidesBean() {
        contextRunner
                .withBean(GreetingClient.class, () -> new GreetingClient("Hi"))
                .run(context -> {
                    assertThat(context).hasSingleBean(GreetingClient.class);
                    assertThat(context.getBean(GreetingClient.class).greet("Boot"))
                            .isEqualTo("Hi, Boot");
                });
    }
}
```

자동 설정은 조건 조합이 많기 때문에 테스트 없이 만들면 예상하지 못한 Bean 충돌이 생기기 쉽다.

## 조건 애너테이션 읽는 법

예:

```java
@ConditionalOnClass(GreetingClient.class)
@ConditionalOnMissingBean(GreetingClient.class)
@ConditionalOnProperty(prefix = "greeting", name = "enabled", havingValue = "true")
```

이 말은 다음과 같다.

```text
GreetingClient 클래스가 있고,
사용자가 GreetingClient Bean을 직접 만들지 않았고,
greeting.enabled=true이면,
기본 GreetingClient를 만든다.
```

자동 설정은 결국 조건문이다. 조건을 읽을 수 있으면 Spring Boot 내부 자동 설정도 이해할 수 있다.

## 자동 설정 순서

자동 설정 간 순서가 필요할 수 있다.

```java
@AutoConfiguration(after = SomeOtherAutoConfiguration.class)
public class GreetingAutoConfiguration {
}
```

또는:

```java
@AutoConfigureAfter(SomeOtherAutoConfiguration.class)
```

순서는 Bean 생성 순서를 무조건 보장한다는 뜻은 아니다. 자동 설정 클래스가 적용되는 순서를 조정하는 데 사용한다.

## 자주 하는 실수

### `@ComponentScan`을 자동 설정 안에서 사용한다

자동 설정은 특정 패키지 전체를 스캔하기보다 필요한 Bean을 명시적으로 등록하는 편이 좋다.

### `@ConditionalOnMissingBean`을 빼먹는다

사용자가 직접 설정한 Bean을 덮어쓰면 Spring Boot다운 자동 설정이 아니다.

### imports 파일을 등록하지 않는다

`AutoConfiguration.imports`에 등록하지 않으면 자동 설정 후보로 발견되지 않는다.

### 애플리케이션 코드와 starter 코드를 섞는다

starter는 여러 프로젝트에서 재사용하는 라이브러리다. 특정 애플리케이션의 도메인 코드와 분리해야 한다.

## 확인 문제

1. 커스텀 자동 설정은 어떤 상황에서 유용한가?
2. `@ConditionalOnMissingBean`이 중요한 이유는 무엇인가?
3. Spring Boot 3.x에서 자동 설정 클래스를 등록하는 파일 이름은 무엇인가?
4. starter 모듈과 autoconfigure 모듈을 분리하는 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Creating Your Own Auto-configuration  
  https://docs.spring.io/spring-boot/3.5/reference/features/developing-auto-configuration.html

## 다음 장으로

다음 장부터는 마이그레이션과 부록 파트로 넘어간다. Spring Boot 2에서 3으로 넘어갈 때 바뀌는 지점, 자주 쓰는 애너테이션, 오류 메시지 읽는 법을 정리한다.
