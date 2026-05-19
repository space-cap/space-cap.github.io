# Vue Router 기초

## 이번 장에서 배울 것

Vue Router는 Vue의 공식 클라이언트 사이드 라우터다. URL 경로에 따라 어떤 컴포넌트를 보여줄지 정해준다.

이번 장에서는 Vue Router를 설치하고, 페이지를 나누고, 링크로 이동하는 기본 흐름을 배운다.

---

## 라우팅이란?

라우팅은 URL과 화면을 연결하는 일이다.

예:

| URL | 보여줄 화면 |
| --- | --- |
| `/` | 홈 |
| `/about` | 소개 |
| `/posts` | 게시글 목록 |
| `/posts/1` | 1번 게시글 상세 |

Vue Router를 사용하면 페이지를 새로고침하지 않고도 URL과 화면을 바꿀 수 있다. 이런 방식을 SPA 라우팅이라고 부른다.

---

## 설치

프로젝트 생성 시 Vue Router 옵션을 선택하지 않았다면 직접 설치한다.

```bash
npm install vue-router
```

---

## 기본 폴더 구조

보통 페이지 역할을 하는 컴포넌트는 `views` 또는 `pages` 폴더에 둔다.

```text
src/
├─ router/
│  └─ index.js
├─ views/
│  ├─ HomeView.vue
│  └─ AboutView.vue
├─ App.vue
└─ main.js
```

Vue Router 공식 예제에서는 `views`라는 이름을 자주 사용한다. 팀에 따라 `pages`를 쓰기도 한다.

---

## 라우터 만들기

`src/router/index.js`

```js
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AboutView from '../views/AboutView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: AboutView
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
```

`routes`는 URL 경로와 컴포넌트를 연결하는 배열이다.

---

## 앱에 라우터 등록하기

`src/main.js`

```js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(router)

app.mount('#app')
```

`app.use(router)`는 `app.mount('#app')`보다 먼저 호출한다.

---

## RouterView

`RouterView`는 현재 URL에 맞는 컴포넌트가 렌더링될 자리다.

`App.vue`

```vue
<template>
  <header>
    <h1>Vue Router 예제</h1>
  </header>

  <main>
    <RouterView />
  </main>
</template>
```

현재 URL이 `/`이면 `HomeView`, `/about`이면 `AboutView`가 `RouterView` 위치에 표시된다.

---

## RouterLink

일반 `<a>` 태그 대신 `RouterLink`를 사용하면 페이지 전체를 새로고침하지 않고 이동할 수 있다.

```vue
<template>
  <nav>
    <RouterLink to="/">홈</RouterLink>
    <RouterLink to="/about">소개</RouterLink>
  </nav>

  <RouterView />
</template>
```

`RouterLink`는 내부적으로 적절한 링크를 만들고, 클릭 시 Vue Router가 화면을 바꾼다.

---

## 동적 라우트

게시글 상세처럼 id가 달라지는 페이지는 동적 라우트를 사용한다.

```js
{
  path: '/posts/:id',
  name: 'post-detail',
  component: () => import('../views/PostDetailView.vue')
}
```

`PostDetailView.vue`

```vue
<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<template>
  <h1>게시글 상세</h1>
  <p>게시글 id: {{ route.params.id }}</p>
</template>
```

`/posts/10`으로 접속하면 `route.params.id`는 `"10"`이 된다. URL 파라미터는 문자열로 들어온다는 점을 기억하자.

---

## 프로그래밍 방식 이동

코드에서 직접 이동하려면 `useRouter`를 사용한다.

```vue
<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function goHome() {
  router.push('/')
}
</script>

<template>
  <button @click="goHome">홈으로</button>
</template>
```

이동할 때 이름 있는 라우트를 사용할 수도 있다.

```js
router.push({ name: 'post-detail', params: { id: 10 } })
```

---

## createWebHistory와 createWebHashHistory

라우터 history 모드는 URL 형태를 결정한다.

| 방식 | URL 예 | 특징 |
| --- | --- | --- |
| `createWebHistory` | `/about` | 깔끔한 URL, 서버 설정 필요 |
| `createWebHashHistory` | `/#/about` | 서버 설정 부담이 적음 |

Vite와 일반 배포 환경에서는 `createWebHistory`를 많이 사용한다. GitHub Pages처럼 새로고침 설정이 까다로운 환경에서는 hash 방식도 고려할 수 있다.

---

## 자주 하는 실수

### RouterView를 빼먹음

라우트를 설정해도 `RouterView`가 없으면 화면이 표시되지 않는다.

### app.use(router)를 mount 뒤에 호출함

라우터는 `mount` 전에 등록한다.

### route와 router를 헷갈림

| 이름 | 의미 |
| --- | --- |
| `route` | 현재 라우트 정보 |
| `router` | 이동을 수행하는 라우터 인스턴스 |

---

## 작은 실습

다음 페이지를 만들어보자.

- `/`: 홈
- `/about`: 소개
- `/posts`: 게시글 목록
- `/posts/:id`: 게시글 상세

`RouterLink`로 각 페이지를 이동하고, 상세 페이지에서는 `route.params.id`를 출력한다.

---

## 이번 장 요약

- Vue Router는 URL에 따라 컴포넌트를 보여준다.
- `createRouter`로 라우터를 만들고 `app.use(router)`로 등록한다.
- `RouterView`는 현재 라우트 컴포넌트가 표시될 자리다.
- `RouterLink`는 새로고침 없이 라우트 이동을 만든다.
- 동적 라우트는 `/posts/:id`처럼 작성한다.

---

## 다음 장으로

다음 장에서는 중첩 라우트, 라우터 가드, 404 페이지 같은 실무 라우터 패턴을 배운다.

