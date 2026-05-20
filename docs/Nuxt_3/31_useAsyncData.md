# useAsyncData

## 이번 장에서 배울 것

- `useAsyncData`가 무엇인지
- `useFetch`와의 차이
- key의 역할
- `$fetch`와 함께 사용하는 방법

## useAsyncData란

`useAsyncData`는 비동기 데이터를 Nuxt의 SSR 흐름에 맞게 가져오는 composable이다.

`useFetch`가 URL 요청에 특화되어 있다면, `useAsyncData`는 더 일반적인 비동기 작업을 다룰 수 있다.

예를 들어 다음과 같은 작업에 사용할 수 있다.

- API 요청
- 여러 API 결과 조합
- 비동기 계산
- 서버에서 가져온 데이터를 가공한 결과

## 기본 사용법

```vue
<script setup>
const { data, status, error, refresh } = await useAsyncData(
  'posts',
  () => $fetch('/api/posts')
)
</script>

<template>
  <p v-if="status === 'pending'">불러오는 중...</p>
  <p v-else-if="error">게시글을 가져오지 못했습니다.</p>
  <ul v-else>
    <li v-for="post in data" :key="post.id">
      {{ post.title }}
    </li>
  </ul>
</template>
```

첫 번째 인자인 `'posts'`는 이 데이터의 key다. 두 번째 인자는 데이터를 가져오는 비동기 함수다.

## key가 필요한 이유

key는 Nuxt가 데이터를 구분하기 위한 이름이다.

```js
await useAsyncData('posts', () => $fetch('/api/posts'))
```

Nuxt는 이 key를 이용해 데이터를 캐시하고, 같은 key의 데이터를 다시 사용할 수 있다.

명확한 key를 붙이면 나중에 `useNuxtData`, `refreshNuxtData` 같은 기능을 사용할 때도 편하다.

## useFetch와 useAsyncData 비교

`useFetch`는 내부적으로 `useAsyncData`와 `$fetch`를 편하게 감싼 도구라고 볼 수 있다.

| 구분 | `useFetch` | `useAsyncData` |
| --- | --- | --- |
| 주 용도 | URL에서 데이터 가져오기 | 모든 비동기 데이터 처리 |
| key | 자동 생성 가능 | 직접 지정하는 경우가 많음 |
| 요청 함수 | 내부에서 `$fetch` 사용 | 직접 작성 |
| 적합한 예 | `/api/posts` 호출 | 여러 요청 조합, 가공 로직 |

단순 API 요청이라면 `useFetch`가 쉽다. 비동기 로직을 더 세밀하게 다루고 싶다면 `useAsyncData`가 좋다.

## 여러 요청 조합하기

`useAsyncData` 안에서 여러 요청을 조합할 수 있다.

```vue
<script setup>
const { data } = await useAsyncData('home-data', async () => {
  const [posts, notices] = await Promise.all([
    $fetch('/api/posts'),
    $fetch('/api/notices')
  ])

  return {
    posts,
    notices
  }
})
</script>
```

홈 화면에서 여러 데이터를 한 번에 준비해야 할 때 사용할 수 있다.

## 데이터 가공하기

가져온 데이터를 화면에 맞게 가공할 수도 있다.

```vue
<script setup>
const { data: postTitles } = await useAsyncData('post-titles', async () => {
  const posts = await $fetch('/api/posts')

  return posts.map((post) => post.title)
})
</script>
```

이처럼 `useAsyncData`의 반환값은 꼭 API 응답 원본일 필요가 없다.

## refresh 사용하기

데이터를 다시 가져오고 싶을 때 `refresh`를 사용한다.

```vue
<script setup>
const { data, refresh } = await useAsyncData(
  'posts',
  () => $fetch('/api/posts')
)
</script>

<template>
  <button @click="refresh">새로고침</button>
</template>
```

게시글 작성 후 목록을 다시 불러오거나, 사용자가 직접 새로고침 버튼을 누를 때 유용하다.

## 자주 하는 실수

key를 너무 대충 정하면 나중에 헷갈린다.

```js
await useAsyncData('data', () => $fetch('/api/posts'))
```

`'data'` 같은 이름은 의미가 약하다. `'posts'`, `'home-posts'`, `'user-profile'`처럼 데이터의 역할이 드러나는 key를 사용하는 것이 좋다.

또 custom wrapper를 만들 때 composable 안에서 `await useAsyncData(...)`를 바로 해 버리면 예상치 못한 동작이 생길 수 있다. 처음에는 페이지나 컴포넌트에서 직접 사용하는 방식에 익숙해지자.

## 정리

`useAsyncData`는 Nuxt에서 비동기 데이터를 SSR 친화적으로 다루는 composable이다. 단순 API 요청은 `useFetch`, 더 복잡한 비동기 로직은 `useAsyncData`를 사용한다고 생각하면 쉽다.

## 다음 장으로

다음 장에서는 페이지 이동을 막지 않고 데이터를 늦게 가져오는 `useLazyFetch`와 로딩 처리를 배운다.
