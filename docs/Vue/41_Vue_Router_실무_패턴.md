# Vue Router 실무 패턴

## 이번 장에서 배울 것

이번 장에서는 Vue Router를 실제 앱에서 사용할 때 자주 만나는 패턴을 배운다.

- 중첩 라우트
- 404 페이지
- 리다이렉트
- 라우터 가드
- meta 필드
- 라우트 지연 로딩

---

## 중첩 라우트

관리자 화면처럼 공통 레이아웃 안에 여러 하위 페이지가 들어가는 경우 중첩 라우트를 사용한다.

```js
const routes = [
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    children: [
      {
        path: '',
        name: 'admin-dashboard',
        component: () => import('../views/admin/DashboardView.vue')
      },
      {
        path: 'users',
        name: 'admin-users',
        component: () => import('../views/admin/UserListView.vue')
      }
    ]
  }
]
```

부모 컴포넌트인 `AdminLayout.vue`에는 자식 라우트가 표시될 `RouterView`가 필요하다.

```vue
<template>
  <aside>관리자 메뉴</aside>
  <main>
    <RouterView />
  </main>
</template>
```

---

## 404 페이지

어떤 라우트에도 맞지 않는 URL은 catch-all 라우트로 처리한다.

```js
{
  path: '/:pathMatch(.*)*',
  name: 'not-found',
  component: () => import('../views/NotFoundView.vue')
}
```

이 라우트는 보통 routes 배열의 마지막에 둔다.

---

## 리다이렉트

특정 경로를 다른 경로로 보내고 싶을 때 `redirect`를 사용한다.

```js
{
  path: '/home',
  redirect: '/'
}
```

이름 있는 라우트로 보낼 수도 있다.

```js
{
  path: '/dashboard',
  redirect: { name: 'admin-dashboard' }
}
```

---

## route meta

라우트에 추가 정보를 붙이고 싶을 때 `meta`를 사용한다.

```js
{
  path: '/admin',
  name: 'admin',
  component: () => import('../views/AdminView.vue'),
  meta: {
    requiresAuth: true,
    title: '관리자'
  }
}
```

`meta`는 라우터 가드나 문서 제목 변경 등에 활용할 수 있다.

---

## 전역 라우터 가드

모든 이동 전에 검사하려면 `router.beforeEach`를 사용한다.

```js
router.beforeEach((to) => {
  const isLoggedIn = Boolean(localStorage.getItem('accessToken'))

  if (to.meta.requiresAuth && !isLoggedIn) {
    return {
      name: 'login',
      query: { redirect: to.fullPath }
    }
  }
})
```

반환값으로 이동을 허용하거나, 다른 경로로 보낼 수 있다.

| 반환 | 의미 |
| --- | --- |
| `undefined` 또는 `true` | 이동 허용 |
| `false` | 이동 취소 |
| 경로 문자열 또는 route 객체 | 다른 곳으로 리다이렉트 |

최신 Vue Router에서는 예전 방식의 `next`보다 값을 반환하는 방식을 먼저 익히는 것이 좋다.

---

## 로그인 후 원래 페이지로 돌아가기

보호된 페이지에 접근했다가 로그인 페이지로 이동할 때 원래 목적지를 query에 저장할 수 있다.

```js
return {
  name: 'login',
  query: { redirect: to.fullPath }
}
```

로그인 성공 후:

```js
const redirect = route.query.redirect || '/'
router.push(redirect)
```

---

## 페이지 제목 변경

`afterEach`를 사용해 라우트 이동 후 문서 제목을 바꿀 수 있다.

```js
router.afterEach((to) => {
  document.title = to.meta.title
    ? `${to.meta.title} | Vue App`
    : 'Vue App'
})
```

`afterEach`는 이동을 막거나 변경하지 않고, 이동 후 부가 작업을 할 때 사용한다.

---

## 라우트 지연 로딩

페이지 컴포넌트는 동적 import로 지연 로딩하는 경우가 많다.

```js
{
  path: '/reports',
  name: 'reports',
  component: () => import('../views/ReportsView.vue')
}
```

초기 번들 크기를 줄이고, 해당 페이지에 들어갈 때 컴포넌트를 불러올 수 있다.

---

## 자주 하는 실수

### 404 라우트를 앞에 둠

catch-all 라우트는 대부분 마지막에 둔다. 앞에 있으면 다른 라우트보다 먼저 매칭될 수 있다.

### 중첩 라우트 부모에 RouterView를 빼먹음

자식 라우트가 보일 자리가 필요하다.

### 가드에서 무한 리다이렉트 발생

로그인 페이지로 보내는 가드에서는 이미 로그인 페이지인지 확인해야 한다.

```js
if (to.name !== 'login' && to.meta.requiresAuth && !isLoggedIn) {
  return { name: 'login' }
}
```

---

## 작은 실습

관리자 라우트를 구성해보자.

- `/admin`: 관리자 레이아웃
- `/admin/users`: 사용자 목록
- `/admin/settings`: 설정
- `meta.requiresAuth`가 있으면 로그인 여부 검사
- 없는 경로는 404 페이지로 이동

---

## 이번 장 요약

- 중첩 라우트는 공통 레이아웃 안에 하위 페이지를 넣을 때 사용한다.
- 404 페이지는 catch-all 라우트로 만든다.
- `meta`는 라우트에 추가 정보를 붙인다.
- `router.beforeEach`로 이동 전 검사를 할 수 있다.
- 페이지 컴포넌트는 지연 로딩을 자주 사용한다.

---

## 다음 장으로

다음 장에서는 Pinia로 앱 상태를 관리하는 방법을 배운다.

