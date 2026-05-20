# useLazyFetch와 로딩 처리

## 이번 장에서 배울 것

- `useLazyFetch`가 무엇인지
- 일반 `useFetch`와의 차이
- 로딩 상태를 화면에 표시하는 방법
- `pending`, `status`, `error`를 다루는 방법

## 왜 lazy가 필요할까

`useFetch`를 `await`와 함께 사용하면 데이터 요청이 끝날 때까지 페이지 렌더링이나 이동이 기다릴 수 있다.

중요한 데이터라면 이 방식이 좋다. 하지만 모든 데이터를 반드시 기다릴 필요는 없다.

예를 들어 다음 데이터는 페이지가 먼저 보인 뒤 천천히 불러와도 괜찮을 수 있다.

- 추천 게시글
- 인기 검색어
- 사이드바 통계
- 부가 정보

이럴 때 lazy 데이터 가져오기를 사용할 수 있다.

## useLazyFetch 기본 사용법

```vue
<script setup>
const { data, status, error } = await useLazyFetch('/api/posts')
</script>

<template>
  <section>
    <h1>게시글</h1>

    <p v-if="status === 'pending'">게시글을 불러오는 중입니다.</p>
    <p v-else-if="error">게시글을 불러오지 못했습니다.</p>
    <ul v-else>
      <li v-for="post in data" :key="post.id">
        {{ post.title }}
      </li>
    </ul>
  </section>
</template>
```

`useLazyFetch`는 페이지 이동을 즉시 진행하고, 데이터는 뒤따라 가져오는 방식으로 사용할 수 있다.

## useFetch의 lazy 옵션

`useLazyFetch`는 `useFetch`에 `lazy: true`를 적용한 편의 기능으로 볼 수 있다.

```vue
<script setup>
const { data, status } = await useFetch('/api/posts', {
  lazy: true
})
</script>
```

처음에는 `useLazyFetch`라는 이름이 더 직관적이므로 이쪽으로 익혀도 좋다.

## 로딩 상태 표시하기

데이터가 늦게 도착한다면 사용자는 빈 화면을 보게 될 수 있다. 그래서 로딩 상태를 표시해야 한다.

```vue
<template>
  <div>
    <p v-if="status === 'pending'">불러오는 중...</p>
    <p v-else-if="status === 'error'">오류가 발생했습니다.</p>
    <p v-else-if="status === 'success'">불러오기 완료</p>
  </div>
</template>
```

로딩 UI는 거창할 필요가 없다. 초보 단계에서는 짧은 문장만 표시해도 충분하다.

## 빈 상태 UI

요청은 성공했지만 데이터가 없을 수 있다.

```vue
<script setup>
const { data: posts, status, error } = await useLazyFetch('/api/posts', {
  default: () => []
})
</script>

<template>
  <p v-if="status === 'pending'">불러오는 중...</p>
  <p v-else-if="error">오류가 발생했습니다.</p>
  <p v-else-if="posts.length === 0">게시글이 없습니다.</p>
  <ul v-else>
    <li v-for="post in posts" :key="post.id">
      {{ post.title }}
    </li>
  </ul>
</template>
```

좋은 화면은 로딩, 오류, 빈 상태를 모두 고려한다.

## 즉시 요청하지 않기

처음부터 요청하지 않고 사용자가 버튼을 눌렀을 때 요청하고 싶다면 `immediate: false`를 사용할 수 있다.

```vue
<script setup>
const { data, status, execute } = await useFetch('/api/report', {
  immediate: false
})
</script>

<template>
  <button @click="execute">보고서 불러오기</button>
  <p v-if="status === 'pending'">불러오는 중...</p>
  <pre>{{ data }}</pre>
</template>
```

`execute`는 요청을 실행하는 함수다. `refresh`와 비슷하게 사용할 수 있다.

## 언제 lazy를 쓰면 좋을까

다음 기준으로 생각하면 된다.

- 페이지 핵심 데이터: 일반 `useFetch`
- 없어도 먼저 화면을 보여 줄 수 있는 데이터: `useLazyFetch`
- 사용자 행동 후 가져오는 데이터: `$fetch` 또는 `useFetch`의 `immediate: false`

처음에는 핵심 데이터에는 일반 `useFetch`, 부가 데이터에는 `useLazyFetch`를 사용해 보자.

## 자주 하는 실수

lazy 요청을 사용하면서 로딩 상태를 표시하지 않는 경우가 많다.

사용자는 데이터가 없는 것인지, 아직 불러오는 중인지 알 수 없다. lazy를 사용한다면 로딩 상태와 빈 상태를 반드시 구분해 주는 것이 좋다.

## 정리

`useLazyFetch`는 페이지 이동을 막지 않고 데이터를 늦게 가져올 때 사용한다. lazy 요청을 사용할 때는 로딩, 오류, 빈 상태 UI를 함께 준비해야 한다.

## 다음 장으로

다음 장에서는 서버와 클라이언트에서 데이터 요청이 어떻게 다르게 동작하는지 살펴본다.
