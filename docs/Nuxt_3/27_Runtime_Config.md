# Runtime Config

## 이번 장에서 배울 것

- Runtime Config가 무엇인지
- 공개 설정과 비공개 설정의 차이
- `useRuntimeConfig` 사용 방법
- 환경 변수와 연결하는 기본 흐름

## Runtime Config란

Runtime Config는 Nuxt 앱에서 사용할 설정값을 관리하는 기능이다.

예를 들어 다음 값들은 설정으로 관리하는 것이 좋다.

- API 기본 주소
- 서버에서만 사용하는 API 비밀 키
- 서비스 이름
- 외부 서비스 연결 정보

이런 값을 코드에 직접 박아 두면 환경이 바뀔 때 수정하기 어렵다. 개발, 테스트, 운영 환경마다 값이 달라질 수도 있다.

## nuxt.config.ts에 작성하기

`nuxt.config.ts`에 `runtimeConfig`를 작성한다.

```ts
export default defineNuxtConfig({
  runtimeConfig: {
    apiSecret: '',
    public: {
      apiBase: '/api'
    }
  }
})
```

여기서 중요한 구분이 있다.

- `runtimeConfig.public`: 브라우저에서도 접근 가능
- `runtimeConfig`의 public 밖 값: 서버에서만 접근 가능

## 공개 설정 읽기

페이지에서 `useRuntimeConfig`를 사용한다.

```vue
<script setup>
const config = useRuntimeConfig()
</script>

<template>
  <p>API 주소: {{ config.public.apiBase }}</p>
</template>
```

`public` 안의 값은 클라이언트와 서버 양쪽에서 사용할 수 있다.

## 비공개 설정 읽기

비공개 설정은 서버에서만 안전하게 사용해야 한다.

예를 들어 `server/api/secret.get.ts`에서 사용할 수 있다.

```ts
export default defineEventHandler(() => {
  const config = useRuntimeConfig()

  return {
    hasSecret: Boolean(config.apiSecret)
  }
})
```

`apiSecret` 같은 값은 브라우저로 노출되면 안 된다.

## 환경 변수로 값 넣기

개발 중에는 `.env` 파일을 사용할 수 있다.

```txt
NUXT_PUBLIC_API_BASE=https://api.example.com
NUXT_API_SECRET=my-secret
```

Nuxt는 `runtimeConfig`에 정의된 값과 환경 변수를 연결해 사용할 수 있다.

```ts
export default defineNuxtConfig({
  runtimeConfig: {
    apiSecret: '',
    public: {
      apiBase: ''
    }
  }
})
```

환경 변수 이름은 Nuxt 규칙에 맞춰 작성해야 한다. 공개 값은 보통 `NUXT_PUBLIC_` 접두사를 사용한다.

## public에 넣으면 안 되는 값

다음 값은 `public` 안에 넣으면 안 된다.

- DB 비밀번호
- 서버 API 비밀 키
- 관리자 토큰
- 결제 secret key
- 외부 서비스 private key

`public` 안의 값은 브라우저에서 볼 수 있다고 생각해야 한다.

## app.config와의 차이

`runtimeConfig`는 환경에 따라 달라질 수 있는 설정과 비밀 값을 다루는 데 적합하다.

반면 `app.config.ts`는 앱의 공개적인 설정, 예를 들어 테마 이름이나 UI 옵션 같은 값을 다루는 데 어울린다.

둘의 차이는 다음 장에서 더 자세히 배운다.

## 자주 하는 실수

가장 위험한 실수는 비밀 값을 `public`에 넣는 것이다.

```ts
export default defineNuxtConfig({
  runtimeConfig: {
    public: {
      apiSecret: '절대 넣으면 안 되는 값'
    }
  }
})
```

이 값은 브라우저로 전달될 수 있다.

## 정리

Runtime Config는 Nuxt 앱의 실행 설정을 관리하는 기능이다. `public` 안의 값은 클라이언트에 공개되고, public 밖의 값은 서버 전용 비공개 설정으로 다루어야 한다.

## 다음 장으로

다음 장에서는 공개적인 앱 설정을 다루는 App Config를 배운다.
