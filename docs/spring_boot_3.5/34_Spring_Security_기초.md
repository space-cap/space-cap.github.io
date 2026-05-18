# Spring Security 기초

## 이번 장에서 배울 것

이 장에서는 Spring Security의 큰 그림을 배운다.

Spring Security는 강력하지만 처음 만나면 당황스럽다. 의존성 하나를 추가했을 뿐인데 모든 API가 갑자기 막히고, 콘솔에는 임시 비밀번호가 출력된다. 이 장의 목표는 그 당황스러운 첫 경험을 이해 가능한 구조로 바꾸는 것이다.

## Spring Security가 필요한 이유

대부분의 웹 애플리케이션에는 보안이 필요하다.

- 로그인한 사용자만 접근
- 관리자만 접근
- 본인의 데이터만 수정
- 비밀번호 안전하게 저장
- CSRF, 세션 고정, 클릭재킹 같은 공격 방어
- 인증 실패와 권한 부족 응답 처리

이 모든 것을 직접 구현하는 것은 위험하다. Spring Security는 인증과 인가를 포함한 보안 기능을 표준적인 방식으로 제공한다.

## 인증과 인가

보안에서 가장 먼저 구분해야 할 단어는 인증과 인가다.

### 인증 Authentication

인증은 "당신이 누구인가"를 확인하는 일이다.

예:

```text
아이디와 비밀번호가 맞는가?
JWT 토큰이 유효한가?
세션에 로그인 정보가 있는가?
```

### 인가 Authorization

인가는 "당신이 이 일을 할 권한이 있는가"를 확인하는 일이다.

예:

```text
관리자 페이지에 접근할 수 있는가?
이 게시글을 수정할 수 있는가?
이 API를 호출할 권한이 있는가?
```

인증이 먼저이고 인가는 그 다음이다. 누군지 알아야 권한을 판단할 수 있다.

## 의존성 추가

Gradle:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
}
```

의존성을 추가하고 애플리케이션을 실행하면 Spring Boot는 기본 보안 설정을 적용한다.

기본 동작:

- 대부분의 요청에 인증 필요
- 기본 로그인 페이지 제공
- HTTP Basic 지원
- `user`라는 기본 사용자 생성
- 임시 비밀번호를 로그에 출력

로그 예:

```text
Using generated security password: ...
```

이 비밀번호는 개발용이다. 운영에서 사용하면 안 된다.

## 기본 사용자 설정

학습용으로 기본 사용자를 설정할 수 있다.

`application.yml`:

```yaml
spring:
  security:
    user:
      name: user
      password: password
```

이제 기본 로그인에서 `user` / `password`로 로그인할 수 있다.

다시 강조하지만 이것은 학습용이다. 운영에서는 사용자 정보를 DB에 저장하고 비밀번호를 해시해야 한다.

## SecurityFilterChain

Spring Security의 웹 보안은 필터 체인을 중심으로 동작한다.

Spring Boot 3.x와 Spring Security 6 이후에는 `SecurityFilterChain` Bean으로 보안 설정을 작성하는 방식이 표준이다.

```java
package com.example.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/hello", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
```

이 설정은 다음 의미다.

- `/hello`, `/h2-console/**`은 누구나 접근 가능
- 그 외 모든 요청은 인증 필요
- Form Login 사용
- HTTP Basic 사용

## 요청 권한 규칙

가장 많이 쓰는 패턴은 다음과 같다.

```java
http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/api/**").authenticated()
        .anyRequest().denyAll()
);
```

규칙은 위에서 아래로 읽힌다. 더 구체적인 규칙을 먼저 두는 것이 좋다.

마지막에 `anyRequest().denyAll()` 또는 `anyRequest().authenticated()`를 두면 빠진 경로를 줄일 수 있다.

## 비밀번호 인코딩

비밀번호를 평문으로 저장하면 안 된다.

Spring Security에서는 `PasswordEncoder`를 사용한다.

```java
package com.example.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

회원가입 시:

```java
String encodedPassword = passwordEncoder.encode(rawPassword);
```

로그인 검증 시 Spring Security가 `matches`를 사용해 비교한다.

## InMemoryUserDetailsManager

학습용으로 메모리 사용자를 만들 수 있다.

```java
package com.example.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfig {

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder
    ) {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
```

실무에서는 DB 기반 `UserDetailsService`를 구현하는 경우가 많다.

## role과 authority

Spring Security에는 role과 authority 개념이 있다.

```java
.roles("ADMIN")
```

이렇게 설정하면 내부적으로 `ROLE_ADMIN` authority가 만들어진다.

```java
.hasRole("ADMIN")
```

은 `ROLE_ADMIN` 권한이 있는지 확인한다.

반면:

```java
.authorities("todo:read")
```

처럼 더 세밀한 권한 이름을 직접 사용할 수도 있다.

처음에는 role 기반으로 시작하고, 복잡한 권한이 필요해지면 authority를 세분화한다.

## 옛날 방식 주의

예전 블로그에는 다음 방식이 자주 나온다.

```java
public class SecurityConfig extends WebSecurityConfigurerAdapter {
}
```

Spring Boot 3.x와 Spring Security 6 기준에서는 이 방식을 사용하지 않는다. `SecurityFilterChain` Bean을 정의하는 방식으로 학습한다.

## 자주 하는 실수

### Security 의존성을 추가하고 모든 API가 막혀서 당황한다

정상이다. Spring Security가 기본으로 전체 애플리케이션을 보호한다.

### 운영에서 로그의 임시 비밀번호를 사용한다

임시 비밀번호는 개발용이다. 운영에서는 반드시 사용자 관리와 비밀번호 해시를 구현한다.

### 비밀번호를 평문 저장한다

비밀번호는 `PasswordEncoder`로 해시해서 저장한다.

### 최신 Spring Security에서 예전 설정 방식을 따라 한다

`WebSecurityConfigurerAdapter` 기반 글은 오래된 글일 가능성이 높다.

## 확인 문제

1. 인증과 인가의 차이는 무엇인가?
2. `spring-boot-starter-security`를 추가하면 기본적으로 어떤 일이 생기는가?
3. 최신 Spring Security에서 웹 보안 설정을 위해 주로 정의하는 Bean은 무엇인가?
4. 비밀번호를 저장할 때 `PasswordEncoder`가 필요한 이유는 무엇인가?

## 참고 공식 자료

- Spring Boot 3.5 Reference: Spring Security  
  https://docs.spring.io/spring-boot/3.5/reference/web/spring-security.html
- Spring Security Reference: Getting Started  
  https://docs.spring.io/spring-security/reference/servlet/getting-started.html

## 다음 장으로

다음 장에서는 세션 기반 로그인을 배운다. Form Login, 세션, CSRF가 어떻게 함께 동작하는지 살펴본다.

---

## 문서 이동

- [이전: 33. 테스트하기 좋은 구조](./33_테스트하기_좋은_구조.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 35. 세션 기반 로그인](./35_세션_기반_로그인.md)
