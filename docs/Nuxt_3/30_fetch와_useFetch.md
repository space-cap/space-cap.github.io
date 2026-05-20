# $fetch와 useFetch

## 이번 장에서 배울 것

- `$fetch`가 무엇인지
- `useFetch`가 무엇인지
- 둘의 차이
- Nuxt에서 데이터를 가져올 때 왜 `useFetch`를 자주 쓰는지

## 데이터를 가져온다는 것

웹 애플리케이션은 화면에 보여 줄 데이터를 어딘가에서 가져온다.

예를 들어 다음과 같은 데이터가 필요할 수 있다.

- 게시글 목록
- 상품 목록
- 로그인 사용자 정보
- 검색 결과
- 공지사항

Nuxt에서는 이런 데이터를 가져올 때 `$fetch`, `useFetch`, `useAsyncData` 같은 도구를 사용한다.

## $fetch란

`$fetch`는 HTTP 요청을 보내는 함수다.

브라우저의 `fetch`와 비슷하지만, Nuxt에서 사용하기 좋게 준비된 도구라고 생각하면 된다.

```vue
<script setup>
const posts = await $fetch('/api/posts')
</script>
```

`$fetch('/api/posts')`는 `/api/posts` 주소로 요청을 보내고 결과를 가져온다.

## $fetch를 버튼 클릭에서 사용하기

사용자 행동 이후 데이터를 요청할 때 `$fetch`를 사용할 수 있다.

```vue
<script setup>
const message = ref('')

const loadMessage = async () => {
  message.value = await $fetch('/api/hello')
}
</script>

<template>
  <button @click="loadMessage">메시지 불러오기</button>
  <p>{{ message }}</p>
</template>
```

버튼 클릭처럼 "나중에 실행되는 요청"에는 `$fetch`가 이해하기 쉽다.

## useFetch란

`useFetch`는 Nuxt에서 페이지나 컴포넌트 데이터를 가져올 때 자주 사용하는 composable이다.

```vue
<script setup>
const { data, status, error, refresh } = await useFetch('/api/posts')
</script>

<template>
  <p v-if="status === 'pending'">불러오는 중...</p>
  <p v-else-if="error">오류가 발생했습니다.</p>
  <ul v-else>
    <li v-for="post in data" :key="post.id">
      {{ post.title }}
    </li>
  </ul>
</template>
```

`useFetch`는 단순히 요청만 보내는 것이 아니라 로딩 상태, 에러 상태, 새로고침 함수까지 함께 제공한다.

## useFetch가 반환하는 값

`useFetch`는 주로 다음 값을 반환한다.

| 값 | 의미 |
| --- | --- |
| `data` | 요청 결과 |
| `status` | 요청 상태 |
| `error` | 오류 정보 |
| `refresh` | 데이터를 다시 가져오는 함수 |
| `clear` | 현재 데이터를 초기화하는 함수 |

`status`는 보통 다음 값 중 하나가 된다.

- `idle`: 아직 요청이 시작되지 않음
- `pending`: 요청 중
- `success`: 요청 성공
- `error`: 요청 실패

## 왜 useFetch를 사용할까

Nuxt는 서버에서도 페이지를 렌더링할 수 있다.

페이지를 렌더링할 때 필요한 데이터를 서버에서 먼저 가져오고, 그 결과를 브라우저에 전달하면 사용자는 더 빠르게 내용이 있는 화면을 볼 수 있다.

`useFetch`는 이런 SSR 흐름에 맞게 동작한다. 서버에서 가져온 데이터를 Nuxt payload에 담아 브라우저로 전달하므로, hydration 과정에서 같은 데이터를 다시 요청하는 일을 줄일 수 있다.

이 점이 단순 `$fetch`와 중요한 차이다.

## $fetch와 useFetch 비교

| 구분 | `$fetch` | `useFetch` |
| --- | --- | --- |
| 목적 | HTTP 요청 함수 | SSR 친화 데이터 요청 composable |
| 반환값 | 요청 결과 | `data`, `status`, `error`, `refresh` 등 |
| 로딩 상태 | 직접 만들어야 함 | 기본 제공 |
| SSR payload 처리 | 직접 처리하지 않음 | Nuxt가 처리 |
| 주 사용 위치 | 이벤트 핸들러, 서버 API, 직접 요청 | 페이지/컴포넌트 초기 데이터 |

처음에는 이렇게 기억하면 된다.

- 화면을 그릴 때 필요한 데이터: `useFetch`
- 버튼 클릭 후 실행하는 단순 요청: `$fetch`

## query 사용하기

검색어를 query string으로 전달할 수 있다.

```vue
<script setup>
const keyword = ref('nuxt')

const { data } = await useFetch('/api/search', {
  query: {
    q: keyword
  }
})
</script>
```

반응형 값이 query에 들어가면 값이 바뀔 때 다시 요청될 수 있다.

## 데이터 기본값

데이터가 아직 없을 때 `null` 때문에 오류가 날 수 있다.

이럴 때 `default`를 사용할 수 있다.

```vue
<script setup>
const { data: posts } = await useFetch('/api/posts', {
  default: () => []
})
</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      {{ post.title }}
    </li>
  </ul>
</template>
```

`posts`의 기본값이 빈 배열이므로 `v-for`를 안전하게 사용할 수 있다.

## 자주 하는 실수

페이지 초기 데이터에 `$fetch`만 사용하는 실수를 자주 한다.

```vue
<script setup>
const posts = await $fetch('/api/posts')
</script>
```

이 코드가 항상 틀린 것은 아니지만, SSR과 hydration 흐름에서 중복 요청이나 상태 관리 문제가 생길 수 있다.

페이지를 렌더링하는 데 필요한 데이터라면 먼저 `useFetch`를 고려하자.

## 정리

`$fetch`는 HTTP 요청 함수이고, `useFetch`는 Nuxt의 SSR 흐름에 맞게 데이터를 가져오는 composable이다. 페이지나 컴포넌트가 처음 렌더링될 때 필요한 데이터는 `useFetch`를 사용하는 것이 좋다.

## 다음 장으로

다음 장에서는 `useAsyncData`를 사용해 더 자유롭게 비동기 데이터를 다루는 방법을 배운다.
