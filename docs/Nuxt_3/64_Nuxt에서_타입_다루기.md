# Nuxt에서 타입 다루기

## 이번 장에서 배울 것

- API 응답에 타입을 적용하는 방법
- `useFetch`와 TypeScript
- composable 반환 타입을 다루는 방법
- runtime config 타입 확장의 기본

## 왜 Nuxt에서 타입이 중요할까

Nuxt 앱은 페이지, 컴포넌트, composable, server API, runtime config가 서로 연결된다.

타입이 없으면 다음 실수를 놓치기 쉽다.

- API 응답 필드 이름 오타
- null 가능성을 처리하지 않음
- 잘못된 파라미터 전달
- config 값 이름 오타
- store 상태 구조 오해

TypeScript를 사용하면 이런 문제를 개발 중에 더 빨리 발견할 수 있다.

## API 응답 타입 만들기

게시글 타입을 만든다.

```ts
type Post = {
  id: number
  title: string
  description: string
}
```

`useFetch`에 타입을 전달한다.

```vue
<script setup lang="ts">
type Post = {
  id: number
  title: string
  description: string
}

const { data: posts } = await useFetch<Post[]>('/api/posts', {
  default: () => []
})
</script>
```

이제 템플릿에서 `post.title` 같은 필드에 자동 완성을 받을 수 있다.

## 상세 데이터 타입

상세 페이지에서는 데이터가 없을 수도 있다.

```vue
<script setup lang="ts">
type Post = {
  id: number
  title: string
  content: string
}

const route = useRoute()

const { data: post } = await useFetch<Post>(`/api/posts/${route.params.id}`)
</script>
```

`post.value`가 `null`일 수 있으므로 사용할 때 확인해야 한다.

```ts
if (!post.value) {
  throw createError({
    statusCode: 404,
    statusMessage: '게시글을 찾을 수 없습니다.'
  })
}
```

## 공통 타입 파일

여러 파일에서 쓰는 타입은 공통 위치에 둘 수 있다.

```txt
types/
  post.ts
```

`types/post.ts`

```ts
export type Post = {
  id: number
  title: string
  description: string
  content: string
}
```

사용하는 곳에서 import한다.

```ts
import type { Post } from '~/types/post'
```

`import type`은 타입만 가져올 때 사용한다.

## composable 타입

composable의 파라미터와 반환값도 타입을 줄 수 있다.

```ts
type UsePaginationOptions = {
  initialPage?: number
  pageSize?: number
}

export const usePagination = (options: UsePaginationOptions = {}) => {
  const page = ref(options.initialPage ?? 1)
  const pageSize = ref(options.pageSize ?? 10)

  const next = () => {
    page.value++
  }

  return {
    page,
    pageSize,
    next
  }
}
```

호출할 때 옵션 이름과 타입을 자동 완성 받을 수 있다.

```ts
const { page, next } = usePagination({
  initialPage: 1,
  pageSize: 20
})
```

## server API 타입

서버 API에서도 타입을 사용할 수 있다.

```ts
type CreatePostBody = {
  title: string
  content: string
}

export default defineEventHandler(async (event) => {
  const body = await readBody<CreatePostBody>(event)

  return {
    id: Date.now(),
    title: body.title,
    content: body.content
  }
})
```

단, TypeScript 타입은 실행 중 검증이 아니다.

사용자가 보낸 body가 실제로 올바른지는 서버에서 런타임 검증도 해야 한다.

## runtime config 타입

`useRuntimeConfig()`도 타입 정보를 활용할 수 있다.

Nuxt는 `nuxt.config.ts`의 `runtimeConfig` 구조를 바탕으로 타입을 추론할 수 있다.

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

사용할 때 자동 완성을 받을 수 있다.

```ts
const config = useRuntimeConfig()

config.public.apiBase
```

더 복잡한 타입 확장이 필요하면 Nuxt의 타입 선언 확장 방식을 사용할 수 있다. 초보 단계에서는 `nuxt.config.ts`에 구조를 명확히 적는 것부터 시작하면 된다.

## 타입과 런타임 검증의 차이

TypeScript는 개발 중에만 도움을 준다. 사용자가 API로 잘못된 값을 보내는 것을 자동으로 막아 주지는 않는다.

```ts
const body = await readBody<CreatePostBody>(event)
```

이 코드는 body가 `CreatePostBody`라고 믿고 쓰겠다는 뜻이지, 실제 값을 검사한다는 뜻은 아니다.

서버 입력값은 Zod, Valibot 같은 라이브러리나 직접 검증으로 확인해야 한다.

## 자주 하는 실수

API 응답 타입을 만들었지만 실제 서버 응답과 다르게 유지하는 실수가 많다.

타입은 문서이기도 하다. 서버 응답 구조가 바뀌면 타입도 함께 바꿔야 한다.

또 `as Type`을 남발하면 실제 오류를 숨길 수 있다.

```ts
const post = data as Post
```

정말 확실한 경우가 아니라면 타입 단언보다 정확한 타입 선언과 검증을 우선하자.

## 정리

Nuxt에서 TypeScript는 API 응답, composable, server API, runtime config의 연결을 더 안전하게 만든다. 하지만 타입은 런타임 검증이 아니므로, 사용자 입력은 서버에서 별도로 검증해야 한다.

## 다음 장으로

다음 장에서는 Vitest와 Nuxt Test Utils를 이용한 단위 테스트를 배운다.
