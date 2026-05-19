# Vue 프로젝트 폴더 구조

## 이번 장에서 배울 것

이번 장에서는 `create-vue`로 만든 프로젝트 안에 어떤 파일과 폴더가 있는지 살펴본다. 처음에는 파일이 많아 보여도 역할을 알면 겁낼 필요가 없다.

---

## 기본 구조 예시

프로젝트를 만들면 대략 다음과 같은 구조가 생긴다.

```text
vue-basic-app/
├─ public/
├─ src/
│  ├─ assets/
│  ├─ components/
│  ├─ App.vue
│  └─ main.js
├─ index.html
├─ package.json
├─ vite.config.js
└─ README.md
```

옵션 선택에 따라 파일이 조금 다를 수 있다. Router, Pinia, TypeScript, 테스트를 선택하면 관련 파일이 더 생긴다.

---

## index.html

`index.html`은 브라우저가 처음 읽는 HTML 파일이다.

```html
<div id="app"></div>
<script type="module" src="/src/main.js"></script>
```

중요한 부분은 두 가지다.

- `id="app"`인 요소가 있다.
- `/src/main.js`를 불러온다.

Vue 앱은 이 `#app` 요소 안에 연결된다.

---

## src/main.js

`main.js`는 Vue 앱의 시작점이다.

```js
import { createApp } from 'vue'
import App from './App.vue'

createApp(App).mount('#app')
```

코드를 순서대로 보면 다음과 같다.

1. Vue에서 `createApp`을 가져온다.
2. 루트 컴포넌트인 `App.vue`를 가져온다.
3. Vue 앱을 만들고 `#app`에 연결한다.

CDN 예제에서 사용한 `createApp(...).mount('#app')`와 같은 역할이다. 다만 이번에는 `App.vue`라는 컴포넌트를 루트로 사용한다.

---

## src/App.vue

`App.vue`는 Vue 앱의 가장 위에 있는 루트 컴포넌트다. 보통 전체 레이아웃의 시작점 역할을 한다.

예:

```vue
<script setup>
const message = 'Vue 프로젝트 시작'
</script>

<template>
  <main>
    <h1>{{ message }}</h1>
  </main>
</template>

<style scoped>
h1 {
  color: #2563eb;
}
</style>
```

`.vue` 파일은 보통 세 부분으로 나뉜다.

| 영역 | 역할 |
| --- | --- |
| `<script setup>` | JavaScript 로직 |
| `<template>` | 화면 구조 |
| `<style scoped>` | 스타일 |

---

## src/components

`components` 폴더는 재사용할 컴포넌트를 넣는 곳이다.

예:

```text
src/components/
├─ BaseButton.vue
├─ TodoItem.vue
└─ AppHeader.vue
```

컴포넌트는 화면의 작은 부품이다. 처음에는 `App.vue`에 코드를 작성하다가, 코드가 길어지면 적절히 컴포넌트로 분리한다.

---

## src/assets

`assets` 폴더에는 이미지, CSS, 아이콘 같은 정적 자원을 넣는다.

예:

```text
src/assets/
├─ logo.svg
└─ main.css
```

JavaScript나 Vue 컴포넌트에서 import해서 사용할 수 있다.

```js
import './assets/main.css'
```

---

## public

`public` 폴더에 있는 파일은 빌드할 때 그대로 복사된다.

예:

```text
public/favicon.ico
```

`src/assets`와 달리 JavaScript에서 import하지 않고, 고정 경로로 접근할 때 사용한다.

```html
<img src="/favicon.ico" alt="">
```

초보자는 대부분의 이미지와 CSS를 `src/assets`에서 시작해도 된다. 특별히 그대로 복사되어야 하는 파일만 `public`에 둔다.

---

## package.json

`package.json`은 프로젝트 정보와 명령을 담고 있다.

```json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  }
}
```

자주 쓰는 명령은 다음과 같다.

```bash
npm run dev
npm run build
npm run preview
```

---

## vite.config.js

`vite.config.js`는 Vite 설정 파일이다.

처음에는 거의 수정하지 않아도 된다. 나중에 경로 별칭, 플러그인, 배포 설정 등을 다룰 때 다시 보게 된다.

예:

```js
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()]
})
```

---

## 처음에는 어디를 수정해야 할까?

초보자는 다음 두 파일부터 보면 된다.

- `src/main.js`
- `src/App.vue`

대부분의 첫 실습은 `App.vue`에서 시작한다. 이후 코드가 길어지면 `src/components` 폴더에 컴포넌트를 만든다.

---

## 자주 하는 실수

### index.html에 직접 화면을 많이 작성함

Vue 프로젝트에서는 대부분의 화면을 `src/App.vue`와 컴포넌트 파일에 작성한다. `index.html`은 앱이 붙을 빈 자리와 기본 메타 정보만 두는 경우가 많다.

### App.vue를 지웠는데 main.js를 수정하지 않음

`main.js`에서 `App.vue`를 import하고 있다면 파일이 존재해야 한다.

```js
import App from './App.vue'
```

파일명을 바꾸면 import 경로도 함께 바꾸어야 한다.

### components 폴더에 모든 파일을 넣음

컴포넌트가 아닌 유틸 함수, API 함수, 타입 파일은 역할에 맞는 다른 폴더로 분리하는 것이 좋다. 초반에는 단순하게 시작하고, 프로젝트가 커질 때 구조를 정리한다.

---

## 작은 실습

`src/App.vue`의 기본 내용을 지우고 아래 코드로 바꾸어보자.

```vue
<script setup>
const title = 'Vue 폴더 구조 살펴보기'
</script>

<template>
  <main>
    <h1>{{ title }}</h1>
    <p>이 화면은 App.vue에서 렌더링됩니다.</p>
  </main>
</template>
```

브라우저 화면이 바뀌면 `App.vue`가 루트 화면이라는 것을 확인한 것이다.

---

## 이번 장 요약

- `index.html`은 Vue 앱이 붙을 HTML 시작점이다.
- `src/main.js`는 Vue 앱을 생성하고 연결하는 파일이다.
- `src/App.vue`는 루트 컴포넌트다.
- `src/components`에는 재사용 컴포넌트를 둔다.
- `src/assets`에는 프로젝트에서 import할 정적 자원을 둔다.
- 처음에는 `App.vue`를 중심으로 실습하면 된다.

---

## 다음 장으로

다음 장에서는 `App.vue`를 수정해 첫 번째 Vue 앱을 직접 만들어본다.

