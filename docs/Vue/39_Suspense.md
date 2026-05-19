# Suspense

## 이번 장에서 배울 것

`Suspense`는 컴포넌트 트리 안의 비동기 의존성이 준비될 때까지 로딩 화면을 보여주는 Vue 내장 컴포넌트다.

중요한 점은 `Suspense`가 현재 Vue에서 실험적 기능이라는 것이다. 사용할 수는 있지만 API가 바뀔 수 있으므로 실무 도입 전 공식 문서를 확인하는 습관이 필요하다.

---

## 왜 필요할까?

비동기 데이터를 불러오는 컴포넌트는 준비되기 전까지 로딩 UI가 필요하다.

일반적으로는 컴포넌트 안에서 `loading` 상태를 직접 관리한다.

```vue
<template>
  <p v-if="loading">불러오는 중...</p>
  <PostList v-else :posts="posts" />
</template>
```

`Suspense`는 비동기 setup이나 비동기 컴포넌트를 감싸고, 준비될 때까지 fallback UI를 보여줄 수 있다.

---

## 기본 구조

```vue
<Suspense>
  <template #default>
    <AsyncContent />
  </template>

  <template #fallback>
    <p>불러오는 중...</p>
  </template>
</Suspense>
```

| Slot | 역할 |
| --- | --- |
| `default` | 실제 보여줄 비동기 컴포넌트 |
| `fallback` | 준비되는 동안 보여줄 로딩 UI |

---

## async setup 예제

`AsyncPostList.vue`

```vue
<script setup>
const response = await fetch('/api/posts')
const posts = await response.json()
</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      {{ post.title }}
    </li>
  </ul>
</template>
```

부모:

```vue
<script setup>
import AsyncPostList from './components/AsyncPostList.vue'
</script>

<template>
  <Suspense>
    <template #default>
      <AsyncPostList />
    </template>

    <template #fallback>
      <p>게시글을 불러오는 중...</p>
    </template>
  </Suspense>
</template>
```

`AsyncPostList`의 비동기 작업이 끝날 때까지 fallback이 표시된다.

---

## 비동기 컴포넌트와 함께 사용하기

`defineAsyncComponent`로 불러오는 컴포넌트도 `Suspense`와 함께 사용할 수 있다.

```vue
<script setup>
import { defineAsyncComponent } from 'vue'

const AdminPanel = defineAsyncComponent(() =>
  import('./components/AdminPanel.vue')
)
</script>

<template>
  <Suspense>
    <AdminPanel />

    <template #fallback>
      <p>관리자 화면을 불러오는 중...</p>
    </template>
  </Suspense>
</template>
```

---

## 에러 처리는 별도로 생각하기

`Suspense`는 로딩 fallback을 다루지만, 모든 에러 처리를 자동으로 예쁘게 해결해주는 도구는 아니다.

API 실패, 권한 오류, 네트워크 오류는 여전히 명확하게 처리해야 한다.

```vue
<script setup>
try {
  const response = await fetch('/api/posts')
  if (!response.ok) {
    throw new Error('요청 실패')
  }
} catch (error) {
  console.error(error)
}
</script>
```

실무에서는 데이터 요청 라이브러리나 라우터, 에러 경계 전략과 함께 설계한다.

---

## 언제 사용할까?

`Suspense`는 다음 상황에서 고려할 수 있다.

- 비동기 setup을 사용하는 컴포넌트
- 비동기 컴포넌트 로딩 경계를 깔끔하게 만들고 싶을 때
- 여러 비동기 자식 컴포넌트가 준비될 때까지 공통 fallback을 보여주고 싶을 때

다만 실험적 기능이므로, 초보자는 먼저 `loading`, `error`, `data` 상태를 직접 관리하는 방식을 충분히 익히는 것이 좋다.

---

## 일반 loading 상태와 비교

| 방식 | 장점 | 주의점 |
| --- | --- | --- |
| 직접 loading 상태 관리 | 명확하고 어디서나 사용 가능 | 반복 코드가 생길 수 있음 |
| Suspense | 로딩 경계를 선언적으로 표현 | 실험적 기능, 에러 설계 필요 |

처음 실무에서는 직접 상태 관리 방식이 더 예측하기 쉽다. `Suspense`는 비동기 컴포넌트 구조가 복잡해질 때 검토해도 된다.

---

## 자주 하는 실수

### Suspense가 안정 API라고 생각함

Vue 공식 문서에서 `Suspense`는 실험적 기능으로 안내된다. 버전과 공식 문서를 확인해야 한다.

### 에러 처리를 fallback으로 대신하려 함

fallback은 로딩 UI다. 에러 UI는 별도로 설계해야 한다.

### 모든 API 호출을 Suspense로 처리하려 함

폼 제출, 검색, 페이지네이션처럼 사용자 상호작용에 따른 요청은 직접 loading/error 상태로 관리하는 편이 명확한 경우가 많다.

---

## 작은 실습

비동기 컴포넌트를 만들어보자.

- `AsyncUserCard.vue`에서 `await`로 사용자 데이터를 불러온다고 가정한다.
- 부모에서 `Suspense`로 감싼다.
- fallback slot에 "사용자 정보를 불러오는 중..."을 표시한다.
- 실패 상황을 어떻게 보여줄지도 별도로 생각해본다.

---

## 이번 장 요약

- `Suspense`는 비동기 의존성이 준비될 때까지 fallback UI를 보여준다.
- `default` slot에는 실제 콘텐츠, `fallback` slot에는 로딩 UI를 둔다.
- 현재 Vue에서 실험적 기능이므로 도입 전 공식 문서를 확인해야 한다.
- 에러 처리는 별도로 설계해야 한다.

---

## 다음 장으로

다음 장부터는 실무 프로젝트로 확장하는 구간이다. 먼저 Vue Router로 페이지를 나누는 방법을 배운다.

