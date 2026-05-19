# API 통신과 비동기 처리

## 이번 장에서 배울 것

실무 Vue 앱은 서버에서 데이터를 가져오고 저장한다. 이번 장에서는 `fetch`를 사용해 API를 호출하고, 로딩 상태와 에러 상태를 함께 관리하는 방법을 배운다.

---

## 기본 흐름

API 통신에서는 보통 세 가지 상태가 필요하다.

| 상태 | 의미 |
| --- | --- |
| `loading` | 요청 중인지 |
| `data` | 성공 응답 데이터 |
| `error` | 실패 정보 |

이 세 가지를 명확히 관리하면 사용자에게 현재 상황을 잘 보여줄 수 있다.

---

## fetch 기본

```js
async function loadPosts() {
  const response = await fetch('/api/posts')
  const posts = await response.json()
  return posts
}
```

`fetch`는 브라우저 내장 API다. 외부 라이브러리 없이 사용할 수 있다.

---

## 컴포넌트에서 데이터 불러오기

```vue
<script setup>
import { onMounted, ref } from 'vue'

const posts = ref([])
const loading = ref(false)
const error = ref(null)

async function loadPosts() {
  loading.value = true
  error.value = null

  try {
    const response = await fetch('/api/posts')

    if (!response.ok) {
      throw new Error('게시글을 불러오지 못했습니다.')
    }

    posts.value = await response.json()
  } catch (err) {
    error.value = err
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadPosts()
})
</script>

<template>
  <p v-if="loading">불러오는 중...</p>
  <p v-else-if="error">{{ error.message }}</p>

  <ul v-else>
    <li v-for="post in posts" :key="post.id">
      {{ post.title }}
    </li>
  </ul>
</template>
```

---

## GET 요청

GET은 데이터를 조회할 때 사용한다.

```js
const response = await fetch('/api/posts')
const posts = await response.json()
```

검색어를 query string으로 보낼 수도 있다.

```js
const response = await fetch(`/api/posts?keyword=${encodeURIComponent(keyword)}`)
```

사용자 입력을 URL에 넣을 때는 `encodeURIComponent`를 사용하자.

---

## POST 요청

POST는 데이터를 생성할 때 자주 사용한다.

```js
async function createPost(post) {
  const response = await fetch('/api/posts', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(post)
  })

  if (!response.ok) {
    throw new Error('저장에 실패했습니다.')
  }

  return await response.json()
}
```

---

## PUT, PATCH, DELETE

| 메서드 | 용도 |
| --- | --- |
| `GET` | 조회 |
| `POST` | 생성 |
| `PUT` | 전체 수정 |
| `PATCH` | 일부 수정 |
| `DELETE` | 삭제 |

예:

```js
await fetch(`/api/posts/${id}`, {
  method: 'DELETE'
})
```

---

## API 함수 분리하기

컴포넌트 안에 모든 API 코드를 넣으면 길어진다. 별도 파일로 분리할 수 있다.

`src/api/posts.js`

```js
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

export async function fetchPosts() {
  const response = await fetch(`${API_BASE_URL}/posts`)

  if (!response.ok) {
    throw new Error('게시글을 불러오지 못했습니다.')
  }

  return await response.json()
}
```

컴포넌트:

```js
import { fetchPosts } from './api/posts'
```

---

## Axios를 써야 할까?

Vue는 특정 HTTP 라이브러리를 강제하지 않는다. `fetch`만으로도 충분히 시작할 수 있다.

Axios는 요청/응답 인터셉터, 기본 설정, JSON 처리 편의성 등 장점이 있다. 프로젝트에서 이미 Axios를 쓰고 있다면 그대로 사용해도 된다.

초보자는 먼저 브라우저 내장 `fetch`로 HTTP 흐름을 이해하는 것을 추천한다.

---

## 요청 취소와 경쟁 상태

검색처럼 입력이 빠르게 바뀌면 이전 요청이 늦게 도착해 최신 결과를 덮어쓸 수 있다. 이런 문제를 경쟁 상태라고 부른다.

실무에서는 다음 방법을 고려한다.

- 요청마다 id를 부여해 최신 요청만 반영
- `AbortController`로 이전 요청 취소
- debounce로 요청 빈도 줄이기

초보 단계에서는 이 문제가 있다는 것을 알고 넘어가면 충분하다.

---

## 자주 하는 실수

### response.ok를 확인하지 않음

`fetch`는 HTTP 404나 500이어도 네트워크 자체가 성공하면 Promise를 reject하지 않는다. `response.ok`를 확인하자.

### loading을 실패 시 false로 돌리지 않음

`finally`에서 loading을 정리하면 성공과 실패 모두 처리된다.

### 에러 메시지를 사용자에게 보여주지 않음

실패했을 때 빈 화면만 보이면 사용자가 상황을 알 수 없다.

---

## 작은 실습

게시글 목록 화면을 만들어보자.

- `posts`, `loading`, `error` 상태 만들기
- `onMounted`에서 게시글 불러오기
- 로딩 중 문구 표시
- 실패 시 에러 문구 표시
- 성공 시 목록 표시

---

## 이번 장 요약

- API 통신에는 `loading`, `data`, `error` 상태가 필요하다.
- `fetch`는 브라우저 내장 HTTP 요청 API다.
- `response.ok`로 성공 여부를 확인한다.
- API 함수는 `src/api` 같은 폴더로 분리할 수 있다.
- Axios는 선택 사항이며, 먼저 `fetch`로 흐름을 익히면 좋다.

---

## 다음 장으로

다음 장에서는 폼 검증과 사용자 입력 처리 패턴을 배운다.

