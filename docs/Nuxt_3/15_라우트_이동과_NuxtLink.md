# 라우트 이동과 NuxtLink

## 이번 장에서 배울 것

- `<NuxtLink>`로 페이지 이동하기
- 일반 `<a>` 태그와의 차이
- 코드에서 페이지를 이동하는 방법
- 동적 라우트로 이동하는 방법

## 페이지 이동의 기본

웹사이트에서는 링크를 눌러 다른 페이지로 이동한다.

Nuxt에서는 내부 페이지 이동에 `<NuxtLink>`를 사용한다.

```vue
<template>
  <NuxtLink to="/about">소개 페이지로 이동</NuxtLink>
</template>
```

브라우저에서는 `<a>` 태그처럼 보이지만, Nuxt 앱 내부에서는 라우터를 이용해 부드럽게 이동한다.

## a 태그와 NuxtLink

일반 HTML 링크는 다음처럼 작성한다.

```html
<a href="/about">소개</a>
```

Nuxt 내부 페이지로 이동할 때도 동작은 한다. 하지만 페이지 전체를 다시 불러올 수 있다.

Nuxt 앱 안의 페이지 이동은 다음처럼 작성하는 것이 좋다.

```vue
<NuxtLink to="/about">소개</NuxtLink>
```

외부 사이트로 이동할 때는 일반 `<a>` 태그를 사용해도 된다.

```vue
<a href="https://nuxt.com" target="_blank" rel="noopener noreferrer">
  Nuxt 공식 사이트
</a>
```

## 메뉴 만들기

간단한 메뉴를 만들어 보자.

```vue
<template>
  <nav>
    <NuxtLink to="/">홈</NuxtLink>
    <NuxtLink to="/about">소개</NuxtLink>
    <NuxtLink to="/posts">게시글</NuxtLink>
  </nav>
</template>
```

이런 메뉴는 나중에 레이아웃이나 헤더 컴포넌트로 분리하면 좋다.

## 동적 라우트로 이동하기

게시글 목록에서 상세 페이지로 이동해 보자.

```vue
<script setup>
const posts = [
  { id: 1, title: '첫 번째 글' },
  { id: 2, title: '두 번째 글' }
]
</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <NuxtLink :to="`/posts/${post.id}`">
        {{ post.title }}
      </NuxtLink>
    </li>
  </ul>
</template>
```

`:to`처럼 콜론을 붙이면 JavaScript 표현식을 사용할 수 있다.

## 객체 방식으로 이동하기

문자열 대신 객체를 사용할 수도 있다.

```vue
<NuxtLink :to="{ path: '/posts/1' }">
  게시글 보기
</NuxtLink>
```

query string이 필요할 때도 객체 방식이 유용하다.

```vue
<NuxtLink :to="{ path: '/posts', query: { page: 2 } }">
  2페이지
</NuxtLink>
```

이 링크는 `/posts?page=2`로 이동한다.

## 코드에서 이동하기

버튼을 눌렀을 때 코드로 이동하고 싶다면 `navigateTo`를 사용할 수 있다.

```vue
<script setup>
const goHome = () => {
  return navigateTo('/')
}
</script>

<template>
  <button @click="goHome">홈으로 이동</button>
</template>
```

로그인 성공 후 마이페이지로 이동하거나, 저장 후 상세 페이지로 이동할 때 자주 사용한다.

## 자주 하는 실수

내부 이동에 항상 `<a>` 태그만 사용하는 실수를 자주 한다.

외부 링크는 `<a>`, Nuxt 앱 내부 링크는 `<NuxtLink>`를 기본으로 생각하면 좋다.

또 `to`에 변수를 넣을 때 콜론을 빼먹는 경우가 많다.

```vue
<!-- 문자열 그대로 해석된다 -->
<NuxtLink to="`/posts/${post.id}`">잘못된 예</NuxtLink>

<!-- JavaScript 표현식으로 해석된다 -->
<NuxtLink :to="`/posts/${post.id}`">올바른 예</NuxtLink>
```

## 정리

Nuxt 내부 페이지 이동에는 `<NuxtLink>`를 사용한다. 버튼 클릭이나 특정 로직 후 이동이 필요하면 `navigateTo`를 사용할 수 있다.

## 다음 장으로

다음 장에서는 페이지 이동 전에 조건을 검사하는 라우트 미들웨어를 배운다.
