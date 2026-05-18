# JWT 인증

## 이번 장에서 배울 것

이 장에서는 JWT 기반 인증을 배운다.

JWT는 JSON Web Token의 줄임말이다. 서버가 인증 정보를 토큰으로 발급하고, 클라이언트는 이후 요청마다 그 토큰을 `Authorization` 헤더에 담아 보낸다.

## JWT 인증 흐름

일반적인 흐름은 다음과 같다.

```text
1. 사용자가 아이디와 비밀번호로 로그인
2. 서버가 사용자를 인증
3. 서버가 JWT 발급
4. 클라이언트가 JWT 저장
5. 이후 요청마다 Authorization: Bearer <token> 전송
6. 서버가 JWT 검증 후 사용자 인증 처리
```

세션 방식과 달리 서버는 로그인 상태를 세션 저장소에 보관하지 않을 수 있다.

## JWT의 구조

JWT는 점으로 구분된 세 부분으로 이루어진다.

```text
header.payload.signature
```

예:

```text
xxxxx.yyyyy.zzzzz
```

각 부분의 의미:

- header: 토큰 타입과 서명 알고리즘
- payload: 사용자 식별자, 권한, 만료 시간 같은 claim
- signature: 토큰이 변조되지 않았는지 검증하기 위한 서명

payload는 암호화가 아니라 Base64URL 인코딩이다. 민감한 정보를 넣으면 안 된다.

## Bearer 토큰

JWT는 보통 Authorization 헤더에 Bearer 토큰으로 전달한다.

```http
GET /api/todos HTTP/1.1
Authorization: Bearer eyJhbGciOi...
```

Spring Security의 OAuth2 Resource Server 기능은 이 형식을 표준적으로 처리한다.

## 의존성

Spring Security에서 JWT Resource Server를 사용하려면 다음 의존성이 필요하다.

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'
}
```

JWT 디코딩과 검증에는 `spring-security-oauth2-jose`가 필요하며, Resource Server starter를 통해 함께 구성된다.

## Resource Server 방식

실무에서 권장되는 방향은 JWT를 직접 파싱하는 필터를 손으로 만드는 것보다 Spring Security의 OAuth2 Resource Server 기능을 사용하는 것이다.

설정 예:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://idp.example.com/issuer
```

이 방식은 별도의 인증 서버가 JWT를 발급하고, API 서버는 JWT를 검증하는 Resource Server로 동작할 때 적합하다.

Spring Security는 JWT의 서명, 만료 시간, issuer 등을 검증한다.

## SecurityFilterChain 설정

```java
package com.example.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                }));

        return http.build();
    }
}
```

핵심:

- 토큰 기반 API이므로 세션을 만들지 않는다.
- Authorization 헤더의 Bearer JWT를 검증한다.
- 인증된 요청만 보호 API에 접근한다.

## `issuer-uri`와 `jwk-set-uri`

Resource Server는 JWT 서명을 검증하기 위해 공개키를 알아야 한다.

일반적으로는 `issuer-uri`를 설정한다.

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://idp.example.com/issuer
```

인증 서버가 표준 메타데이터 엔드포인트를 제공하지 않는다면 `jwk-set-uri`를 직접 지정할 수도 있다.

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://idp.example.com
          jwk-set-uri: https://idp.example.com/.well-known/jwks.json
```

## 권한 매핑

Spring Security Resource Server는 기본적으로 JWT의 scope를 `SCOPE_` 접두사가 붙은 authority로 매핑한다.

예를 들어 토큰에 다음 scope가 있으면:

```json
{
  "scope": "todo:read todo:write"
}
```

다음 authority로 변환될 수 있다.

```text
SCOPE_todo:read
SCOPE_todo:write
```

요청 권한 설정:

```java
http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/todos/**").hasAuthority("SCOPE_todo:read")
        .anyRequest().authenticated()
);
```

## 자체 JWT 발급 방식에 대한 주의

많은 입문 예제는 애플리케이션 안에서 직접 JWT를 발급하고 직접 검증하는 코드를 보여준다.

작은 프로젝트에서는 가능하지만, 실무에서는 다음을 신중하게 다뤄야 한다.

- 서명 키 관리
- 만료 시간
- Refresh Token
- 토큰 폐기
- 탈취된 토큰 대응
- 권한 변경 반영
- 로그아웃 처리
- 키 교체

JWT는 세션 저장소가 없어서 편해 보이지만, 운영 보안은 더 복잡해질 수 있다.

## 토큰 저장 위치

클라이언트에서 JWT를 어디에 저장할지도 중요하다.

대표 선택:

- 메모리
- 브라우저 storage
- HttpOnly Secure Cookie

각 선택에는 보안 트레이드오프가 있다.

브라우저 storage는 XSS에 취약할 수 있고, Cookie는 CSRF를 다시 고려해야 한다. 토큰 인증이라고 해서 모든 보안 문제가 사라지는 것은 아니다.

## 자주 하는 실수

### JWT payload에 민감 정보를 넣는다

payload는 쉽게 디코딩할 수 있다. 비밀번호, 주민등록번호, API Key 같은 값은 넣지 않는다.

### 만료 시간이 없는 토큰을 발급한다

토큰에는 만료 시간이 있어야 한다.

### JWT면 로그아웃이 쉽다고 생각한다

서버가 상태를 저장하지 않으면 이미 발급된 토큰을 즉시 무효화하기 어렵다. 별도 블랙리스트나 짧은 만료 시간, Refresh Token 전략이 필요할 수 있다.

### 직접 만든 JWT 필터를 먼저 선택한다

가능하면 Spring Security의 Resource Server 기능을 먼저 검토한다.

## 확인 문제

1. JWT는 어떤 세 부분으로 구성되는가?
2. Bearer 토큰은 보통 어떤 HTTP 헤더로 전달되는가?
3. Spring Security에서 JWT Resource Server를 구성할 때 사용하는 대표 설정은 무엇인가?
4. JWT payload에 민감 정보를 넣으면 안 되는 이유는 무엇인가?

## 참고 공식 자료

- Spring Security Reference: OAuth2 Resource Server JWT  
  https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html

## 다음 장으로

다음 장에서는 URL 권한과 메서드 권한을 다루는 방법을 배운다.

---

## 문서 이동

- [이전: 35. 세션 기반 로그인](./35_세션_기반_로그인.md)
- [목차: Spring Boot 3.5.x 학습 문서](./README.md)
- [다음: 37. 권한과 메서드 보안](./37_권한과_메서드_보안.md)
