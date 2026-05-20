# 외부 API 프록시

## 이번 장에서 배울 것

- 외부 API 프록시가 무엇인지
- 서버에서 외부 API를 호출하는 이유
- API 키를 숨기는 방법
- `event.$fetch`와 `$fetch`의 기본 차이

## 외부 API 프록시란

프록시는 중간에서 요청을 대신 전달하는 역할이다.

브라우저가 외부 API를 직접 호출하지 않고, Nuxt 서버 API가 대신 외부 API를 호출한 뒤 결과를 브라우저에 전달할 수 있다.

```txt
브라우저 -> Nuxt 서버 API -> 외부 API
```

이런 구조를 외부 API 프록시처럼 사용할 수 있다.

## 왜 서버에서 외부 API를 호출할까

서버에서 외부 API를 호출하면 다음 장점이 있다.

- API 비밀 키를 브라우저에 노출하지 않을 수 있다.
- 외부 API 응답 형식을 우리 앱에 맞게 바꿀 수 있다.
- CORS 문제를 줄일 수 있다.
- 공통 에러 처리를 서버에서 할 수 있다.
- 요청 로그나 캐싱 전략을 적용할 수 있다.

특히 비밀 키가 필요한 API는 브라우저에서 직접 호출하면 안 된다.

## runtimeConfig에 비밀 키 넣기

`nuxt.config.ts`

```ts
export default defineNuxtConfig({
  runtimeConfig: {
    githubToken: '',
    public: {
      apiBase: '/api'
    }
  }
})
```

`githubToken`은 public 밖에 있으므로 서버에서만 사용하는 값이다.

환경 변수로는 다음처럼 설정할 수 있다.

```txt
NUXT_GITHUB_TOKEN=your-token
```

## 서버 API에서 외부 API 호출하기

`server/api/github.get.ts`

```ts
export default defineEventHandler(async (event) => {
  const config = useRuntimeConfig(event)

  const repo = await $fetch('https://api.github.com/repos/nuxt/nuxt', {
    headers: {
      Authorization: `Bearer ${config.githubToken}`
    }
  })

  return repo
})
```

브라우저에서는 우리 서버 API만 호출한다.

```vue
<script setup>
const { data } = await useFetch('/api/github')
</script>
```

브라우저 코드는 `githubToken`을 알 필요가 없다.

## 응답 형식 가공하기

외부 API 응답 전체를 그대로 넘기지 않고 필요한 값만 반환하는 것이 좋다.

```ts
export default defineEventHandler(async (event) => {
  const config = useRuntimeConfig(event)

  const repo = await $fetch('https://api.github.com/repos/nuxt/nuxt', {
    headers: {
      Authorization: `Bearer ${config.githubToken}`
    }
  })

  return {
    name: repo.name,
    stars: repo.stargazers_count,
    url: repo.html_url
  }
})
```

이렇게 하면 클라이언트는 필요한 데이터만 받게 된다.

## 에러 처리하기

외부 API는 실패할 수 있다.

```ts
export default defineEventHandler(async (event) => {
  try {
    const repo = await $fetch('https://api.github.com/repos/nuxt/nuxt')

    return {
      name: repo.name
    }
  } catch {
    throw createError({
      statusCode: 502,
      statusMessage: '외부 API 요청에 실패했습니다.'
    })
  }
})
```

502는 서버가 다른 서버에서 올바른 응답을 받지 못했을 때 자주 사용하는 상태 코드다.

## event.$fetch는 언제 사용할까

서버 라우트 안에서 다른 내부 API를 호출할 때는 `event.$fetch`를 사용할 수 있다.

```ts
export default defineEventHandler((event) => {
  return event.$fetch('/api/me')
})
```

`event.$fetch`는 요청 context와 일부 헤더를 전달하는 데 도움이 된다.

외부 API 호출에는 일반 `$fetch`를 쓰는 경우가 많고, 현재 요청의 context를 유지하며 내부 API를 호출해야 할 때 `event.$fetch`를 고려한다.

## CORS와 프록시

브라우저에서 외부 API를 직접 호출하면 CORS 정책 때문에 막힐 수 있다.

Nuxt 서버가 외부 API를 대신 호출하면 브라우저는 같은 Nuxt 서버의 `/api/...`만 호출하므로 CORS 문제를 피하기 쉬워진다.

다만 프록시를 만든다고 모든 보안 문제가 사라지는 것은 아니다. 서버에서 권한 확인, 입력 검증, 요청 제한 등을 함께 고려해야 한다.

## 자주 하는 실수

가장 위험한 실수는 외부 API 비밀 키를 `runtimeConfig.public`이나 클라이언트 코드에 넣는 것이다.

```ts
export default defineNuxtConfig({
  runtimeConfig: {
    public: {
      githubToken: '절대 넣으면 안 되는 값'
    }
  }
})
```

public 값은 브라우저에 노출될 수 있다. 비밀 키는 반드시 서버 전용 설정에 둔다.

## 정리

외부 API 프록시는 Nuxt 서버가 브라우저 대신 외부 API를 호출하는 구조다. 비밀 키를 숨기고, 응답 형식을 가공하며, 에러 처리를 통일할 수 있다.

## 다음 장으로

다음 장부터는 `useState`, Pinia, Cookie, 인증 흐름 같은 상태 관리와 인증을 배운다.
