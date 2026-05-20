# Cookie 다루기

## 이번 장에서 배울 것

- 쿠키가 무엇인지
- `useCookie` 사용 방법
- 클라이언트와 서버에서 쿠키를 다루는 차이
- 인증 쿠키를 다룰 때 주의할 점

## 쿠키란

쿠키는 브라우저가 저장하고 요청할 때 서버로 함께 보내는 작은 데이터다.

다음과 같은 용도로 사용된다.

- 로그인 세션 식별
- 사용자 설정 저장
- 언어 설정 저장
- 장바구니 식별자 저장
- 간단한 추적 정보 저장

쿠키는 클라이언트와 서버가 함께 사용할 수 있다는 점에서 Nuxt의 SSR 흐름과 잘 맞는다.

## useCookie란

Nuxt의 `useCookie`는 쿠키를 SSR 친화적으로 읽고 쓰는 composable이다.

```vue
<script setup>
const theme = useCookie('theme', {
  default: () => 'light'
})

const toggleTheme = () => {
  theme.value = theme.value === 'light' ? 'dark' : 'light'
}
</script>

<template>
  <button @click="toggleTheme">
    현재 테마: {{ theme }}
  </button>
</template>
```

`useCookie`는 ref를 반환한다. 값을 바꾸면 쿠키도 갱신된다.

## 기본값 설정

쿠키가 없을 때 사용할 기본값은 `default`로 지정한다.

```ts
const locale = useCookie('locale', {
  default: () => 'ko'
})
```

이렇게 하면 처음 방문한 사용자는 `ko` 값을 사용한다.

## 만료 시간 설정

`maxAge`로 쿠키 유지 시간을 설정할 수 있다. 단위는 초다.

```ts
const theme = useCookie('theme', {
  default: () => 'light',
  maxAge: 60 * 60 * 24 * 30
})
```

이 예시는 30일 동안 유지되는 쿠키를 만든다.

## 서버 API에서 쿠키 다루기

서버 API에서는 H3의 `getCookie`, `setCookie`를 사용할 수 있다.

```ts
export default defineEventHandler((event) => {
  const theme = getCookie(event, 'theme')

  setCookie(event, 'visited', 'true', {
    httpOnly: true,
    sameSite: 'lax'
  })

  return {
    theme
  }
})
```

서버에서 설정한 쿠키는 응답 헤더로 브라우저에 전달된다.

## httpOnly

`httpOnly` 쿠키는 브라우저 JavaScript에서 읽을 수 없다.

```ts
setCookie(event, 'session', 'secret-session-id', {
  httpOnly: true,
  secure: true,
  sameSite: 'lax'
})
```

로그인 세션처럼 민감한 값을 저장할 때는 `httpOnly` 쿠키를 고려한다.

주의할 점은 `httpOnly: true`인 쿠키는 `useCookie`로 클라이언트에서 값을 읽을 수 없다는 것이다. 서버 요청에는 쿠키가 포함되지만, 브라우저 JavaScript에서는 접근할 수 없다.

## secure와 sameSite

보안 쿠키 설정에서 자주 보는 옵션은 다음과 같다.

| 옵션 | 의미 |
| --- | --- |
| `httpOnly` | JavaScript에서 쿠키를 읽지 못하게 함 |
| `secure` | HTTPS 연결에서만 쿠키 전송 |
| `sameSite` | 다른 사이트에서 시작된 요청에 쿠키를 보낼지 제한 |

운영 환경에서는 로그인 세션 쿠키에 `httpOnly`, `secure`, `sameSite`를 신중하게 설정해야 한다.

개발 환경이 HTTP라면 `secure: true` 쿠키가 제대로 전송되지 않을 수 있다. 이 때문에 로컬 개발과 운영 환경 설정을 구분해야 한다.

## 쿠키에 넣으면 안 되는 것

쿠키는 용량이 작고 요청마다 서버로 전송된다. 많은 데이터를 넣으면 좋지 않다.

다음 값은 쿠키에 넣지 않는 것이 좋다.

- 큰 객체
- 긴 목록 데이터
- 민감한 개인정보 원문
- 암호
- 클라이언트에서 읽을 수 있는 토큰

쿠키에는 필요한 최소한의 식별자나 설정값만 넣는다.

## 자주 하는 실수

로그인 토큰을 클라이언트 JavaScript에서 쉽게 읽을 수 있는 쿠키나 localStorage에 저장하는 실수를 조심해야 한다.

보안이 중요한 인증 값은 서버가 설정하는 `httpOnly` 쿠키로 관리하는 방식을 우선 검토한다.

또 쿠키에 큰 데이터를 넣으면 모든 요청에 그 데이터가 함께 전송되어 성능에 좋지 않다.

## 정리

`useCookie`는 Nuxt에서 쿠키를 SSR 친화적으로 읽고 쓰는 composable이다. 서버 API에서는 `getCookie`, `setCookie`를 사용할 수 있다. 인증 쿠키는 `httpOnly`, `secure`, `sameSite` 같은 보안 옵션을 신중하게 설정해야 한다.

## 다음 장으로

다음 장에서는 로그인과 로그아웃이 전체적으로 어떤 흐름으로 동작하는지 배운다.
