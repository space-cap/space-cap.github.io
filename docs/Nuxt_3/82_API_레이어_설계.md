# API 레이어 설계

## 이번 장에서 배울 것

- API 레이어가 무엇인지
- `$fetch` wrapper를 만드는 이유
- 에러 처리와 인증 헤더를 공통화하는 방법
- 도메인별 API 함수를 분리하는 패턴

## API 레이어란

API 레이어는 프론트엔드에서 서버 API를 호출하는 코드를 한곳에 정리한 구조다.

페이지나 컴포넌트마다 `$fetch`를 직접 쓰면 코드가 흩어진다.

```ts
await $fetch('/api/posts')
await $fetch('/api/posts', { method: 'POST', body })
await $fetch('/api/users/me')
```

작은 프로젝트에서는 괜찮지만, API가 많아지면 다음 문제가 생긴다.

- URL 문자열이 여러 곳에 반복된다.
- 에러 처리가 제각각이다.
- 인증 헤더 처리 방식이 흩어진다.
- API 응답 타입을 관리하기 어렵다.
- API 경로 변경 시 수정 범위가 커진다.

API 레이어는 이런 문제를 줄이기 위한 구조다.

## useApi composable 만들기

공통 `$fetch` wrapper를 만들 수 있다.

`composables/useApi.ts`

```ts
export const useApi = () => {
  const config = useRuntimeConfig()

  const api = $fetch.create({
    baseURL: config.public.apiBase || '/api',
    onRequest({ options }) {
      options.headers = {
        ...options.headers
      }
    },
    onResponseError({ response }) {
      console.error('API 오류:', response.status)
    }
  })

  return api
}
```

사용 예시:

```ts
const api = useApi()

const posts = await api('/posts')
```

`baseURL`을 공통화하면 API 주소 변경이 쉬워진다.

## 인증 헤더 추가하기

토큰 기반 인증을 사용하는 경우 공통 헤더를 추가할 수 있다.

```ts
export const useApi = () => {
  const config = useRuntimeConfig()
  const token = useState<string | null>('access-token', () => null)

  return $fetch.create({
    baseURL: config.public.apiBase || '/api',
    onRequest({ options }) {
      if (token.value) {
        options.headers = {
          ...options.headers,
          Authorization: `Bearer ${token.value}`
        }
      }
    }
  })
}
```

단, 토큰을 어디에 저장할지는 보안 요구사항에 따라 신중하게 결정해야 한다.

민감한 인증은 httpOnly 쿠키 기반으로 처리하는 편이 더 안전할 수 있다.

## 도메인별 API 함수

게시글 API 함수를 분리해 보자.

`services/posts.ts`

```ts
import type { Post } from '~/types/post'

export const usePostApi = () => {
  const api = useApi()

  const getPosts = () => {
    return api<Post[]>('/posts')
  }

  const getPost = (id: number | string) => {
    return api<Post>(`/posts/${id}`)
  }

  const createPost = (body: { title: string; content: string }) => {
    return api<Post>('/posts', {
      method: 'POST',
      body
    })
  }

  return {
    getPosts,
    getPost,
    createPost
  }
}
```

페이지에서는 API 경로를 직접 몰라도 된다.

```vue
<script setup lang="ts">
const postApi = usePostApi()

const { data: posts } = await useAsyncData('posts', () => {
  return postApi.getPosts()
})
</script>
```

## useFetch와 API 레이어

`useFetch`는 SSR payload와 상태 관리를 제공한다.

API 레이어를 만들더라도 페이지 초기 데이터에는 `useAsyncData`와 조합하는 방식이 좋다.

```ts
const postApi = usePostApi()

const { data } = await useAsyncData('posts', () => postApi.getPosts())
```

단순히 버튼 클릭 후 요청하는 경우는 API 함수만 직접 호출해도 된다.

```ts
await postApi.createPost({
  title: '새 글',
  content: '내용'
})
```

## 에러 형식 통일하기

서버 API 응답 형식을 통일하면 클라이언트 처리가 쉬워진다.

예를 들어 서버에서 다음 구조를 사용한다고 하자.

```json
{
  "ok": false,
  "message": "제목은 필수입니다."
}
```

API 레이어에서 이 형식을 해석해 사용자에게 같은 방식으로 오류를 보여 줄 수 있다.

하지만 Nuxt/H3의 `createError` 형식과 직접 만든 응답 형식을 섞으면 혼란스러울 수 있다.

프로젝트 안에서 오류 형식 규칙을 정하는 것이 좋다.

## server API와 외부 API 구분

API 레이어는 두 방향으로 나눌 수 있다.

- Nuxt 내부 API 호출: `/api/...`
- 외부 백엔드 API 호출: `https://api.example.com/...`

외부 API secret이 필요한 요청은 브라우저에서 직접 호출하지 말고 Nuxt server API를 프록시로 두는 것이 안전하다.

## 자주 하는 실수

API wrapper를 너무 복잡하게 만드는 실수가 많다.

처음부터 retry, cache, auth refresh, toast, loading, error mapping을 모두 넣으면 이해하기 어렵다.

먼저 baseURL과 기본 에러 처리 정도만 공통화하고, 필요해질 때 기능을 추가하는 것이 좋다.

또 페이지 초기 데이터에 wrapper만 쓰고 `useAsyncData`를 빼먹으면 SSR payload 최적화를 놓칠 수 있다.

## 정리

API 레이어는 API 호출 코드를 정리하고 공통 설정을 모으는 구조다. `$fetch.create`로 기본 wrapper를 만들고, 도메인별 API 함수를 분리하면 페이지 코드가 단순해진다. 페이지 초기 데이터는 `useAsyncData`와 조합하는 것이 좋다.

## 다음 장으로

다음 장에서는 Nuxt 실무에서 자주 하는 실수들을 정리한다.
