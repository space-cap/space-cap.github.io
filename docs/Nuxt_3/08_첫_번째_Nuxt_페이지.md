# 첫 번째 Nuxt 페이지

## 이번 장에서 배울 것

- `app.vue`의 역할
- `pages/index.vue`로 첫 페이지 만들기
- Nuxt의 페이지 렌더링 흐름

## app.vue 이해하기

Nuxt 프로젝트를 처음 만들면 `app.vue` 파일이 있다.

`app.vue`는 Nuxt 앱의 가장 바깥 컴포넌트다. 모든 페이지는 결국 이 컴포넌트 안에서 렌더링된다.

처음에는 다음처럼 간단히 작성할 수 있다.

```vue
<template>
  <div>
    <h1>안녕하세요 Nuxt 3</h1>
    <p>첫 번째 Nuxt 화면입니다.</p>
  </div>
</template>
```

이 상태에서는 앱 전체가 하나의 화면처럼 동작한다.

## pages 폴더 만들기

Nuxt의 강력한 기능 중 하나는 파일 기반 라우팅이다.

프로젝트 루트에 `pages` 폴더를 만들고 그 안에 `index.vue` 파일을 만든다.

```txt
hello-nuxt/
  pages/
    index.vue
```

`pages/index.vue`는 `/` 주소에 해당한다.

## NuxtPage 사용하기

`pages` 폴더를 사용하려면 `app.vue`에서 `<NuxtPage />`를 렌더링해야 한다.

```vue
<template>
  <NuxtPage />
</template>
```

`<NuxtPage />`는 현재 주소에 맞는 페이지 컴포넌트를 보여 주는 자리다.

## 첫 페이지 작성하기

`pages/index.vue`에 다음 코드를 작성한다.

```vue
<template>
  <main>
    <h1>Nuxt 3 학습 시작</h1>
    <p>이 페이지는 pages/index.vue 파일에서 만들어졌습니다.</p>
  </main>
</template>
```

브라우저에서 `http://localhost:3000`을 열면 이 화면이 보인다.

## 간단한 상태 추가하기

Vue 문법을 사용해 간단한 카운터를 추가해 보자.

```vue
<script setup>
const count = ref(0)
</script>

<template>
  <main>
    <h1>Nuxt 3 학습 시작</h1>
    <p>현재 숫자: {{ count }}</p>
    <button @click="count++">증가</button>
  </main>
</template>
```

여기서 `ref`를 import하지 않았는데도 동작한다. Nuxt는 자주 쓰는 composable과 Vue API를 자동으로 가져오는 Auto Imports 기능을 제공하기 때문이다.

자동 import는 편리하지만, 처음 배우는 동안에는 "이 함수가 어디에서 온 것인지"를 의식하는 것이 좋다.

## 페이지 파일 이름과 주소

Nuxt에서는 파일 이름이 주소가 된다.

```txt
pages/index.vue      -> /
pages/about.vue      -> /about
pages/contact.vue    -> /contact
```

아직은 `index.vue` 하나만 만들었지만, 다음 장들에서 여러 페이지를 만들고 이동하는 방법을 배운다.

## 자주 하는 실수

`pages/index.vue`를 만들었는데 화면이 보이지 않는다면 `app.vue`에 `<NuxtPage />`가 있는지 확인한다.

또 파일 이름이 `Index.vue`처럼 대문자로 시작하면 의도한 대로 라우팅되지 않을 수 있다. 페이지 파일은 소문자 중심으로 작성하는 습관을 들이는 것이 좋다.

## 정리

`app.vue`는 앱의 가장 바깥 컴포넌트이고, `<NuxtPage />`는 현재 주소에 맞는 페이지를 보여 주는 자리다. `pages/index.vue`를 만들면 `/` 주소의 첫 페이지가 된다.

## 다음 장으로

다음 장에서는 Nuxt 프로젝트의 주요 폴더 구조를 살펴본다.
