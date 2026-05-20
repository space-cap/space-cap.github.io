# Server Routes 기초

## 이번 장에서 배울 것

- `server/api`와 `server/routes`의 차이
- `defineEventHandler` 사용 방법
- API 응답을 반환하는 방법
- 페이지에서 서버 API를 호출하는 방법

## Server Routes란

Server Routes는 Nuxt 프로젝트 안에 서버에서 실행되는 라우트를 만드는 기능이다.

프론트엔드 페이지는 `pages` 폴더에 만든다. 서버 API는 `server` 폴더에 만든다.

```txt
pages/
  index.vue

server/
  api/
    hello.get.ts
```

`server/api/hello.get.ts`는 `/api/hello` 주소로 접근할 수 있는 서버 API가 된다.

## server/api 만들기

`server/api/hello.get.ts` 파일을 만든다.

```ts
export default defineEventHandler(() => {
  return {
    message: 'Hello Nuxt Server'
  }
})
```

브라우저에서 다음 주소를 열어 본다.

```txt
http://localhost:3000/api/hello
```

JSON 응답을 볼 수 있다.

```json
{
  "message": "Hello Nuxt Server"
}
```

## defineEventHandler

서버 라우트 파일은 기본적으로 `defineEventHandler`로 감싼 함수를 export한다.

```ts
export default defineEventHandler((event) => {
  return {
    ok: true
  }
})
```

`event`에는 요청과 응답을 다룰 수 있는 정보가 들어 있다.

처음에는 응답 객체를 반환하는 방식부터 익히면 된다.

## 페이지에서 API 호출하기

페이지에서는 `useFetch`로 API를 호출할 수 있다.

```vue
<script setup>
const { data } = await useFetch('/api/hello')
</script>

<template>
  <pre>{{ data }}</pre>
</template>
```

이 코드는 서버 API 결과를 페이지에 표시한다.

## server/api와 server/routes의 차이

`server/api` 안의 파일은 자동으로 `/api` 접두사가 붙는다.

```txt
server/api/hello.get.ts -> /api/hello
```

반면 `server/routes` 안의 파일은 `/api` 접두사가 붙지 않는다.

```txt
server/routes/health.get.ts -> /health
```

대부분의 데이터 API는 `server/api`에 두는 것이 이해하기 쉽다.

`server/routes`는 `/api`가 아닌 별도 서버 경로가 필요할 때 사용한다.

## 문자열도 반환할 수 있다

서버 라우트는 객체뿐 아니라 문자열도 반환할 수 있다.

```ts
export default defineEventHandler(() => {
  return 'OK'
})
```

하지만 일반 API는 객체를 반환하는 것이 확장하기 쉽다.

```ts
export default defineEventHandler(() => {
  return {
    ok: true,
    message: 'OK'
  }
})
```

## 비동기 처리

서버 라우트 안에서 비동기 작업을 할 수 있다.

```ts
export default defineEventHandler(async () => {
  const posts = await $fetch('https://example.com/api/posts')

  return {
    posts
  }
})
```

외부 API를 호출하거나 DB에서 데이터를 가져오는 작업은 보통 비동기로 처리한다.

## 자주 하는 실수

`server/api/hello.get.ts` 파일을 만들고 `/hello`로 접근하는 실수를 한다.

`server/api` 안의 파일은 `/api` 접두사가 붙는다.

```txt
올바른 주소: /api/hello
```

`/hello` 주소가 필요하다면 `server/routes/hello.get.ts`에 만들어야 한다.

## 정리

Nuxt에서는 `server/api` 폴더에 서버 API를 만들 수 있다. 각 파일은 `defineEventHandler`를 기본 export하며, 반환한 객체는 JSON 응답으로 전달된다.

## 다음 장으로

다음 장에서는 GET, POST, PUT, DELETE 같은 HTTP 메서드와 API 설계 기본을 배운다.
