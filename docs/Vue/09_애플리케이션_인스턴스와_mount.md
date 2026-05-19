# 애플리케이션 인스턴스와 mount

## 이번 장에서 배울 것

이번 장에서는 Vue 앱이 어떻게 시작되는지 배운다. 핵심은 `createApp`, 루트 컴포넌트, `mount`다.

Vue 프로젝트를 만들면 `src/main.js`에서 다음 코드를 자주 보게 된다.

```js
import { createApp } from 'vue'
import App from './App.vue'

createApp(App).mount('#app')
```

이 짧은 코드가 Vue 앱의 출발점이다.

---

## createApp이란?

`createApp`은 Vue 애플리케이션 인스턴스를 만드는 함수다.

```js
import { createApp } from 'vue'

const app = createApp(App)
```

애플리케이션 인스턴스는 Vue 앱 하나를 대표한다. 컴포넌트 등록, 플러그인 등록, 전역 설정 같은 일을 이 인스턴스를 통해 처리한다.

처음에는 어렵게 생각하지 말고, `createApp(App)`은 "`App` 컴포넌트를 루트로 하는 Vue 앱을 만든다"라고 이해하면 된다.

---

## 루트 컴포넌트란?

루트 컴포넌트는 앱의 가장 위에 있는 컴포넌트다. Vite로 만든 Vue 프로젝트에서는 보통 `App.vue`가 루트 컴포넌트다.

```text
App.vue
├─ AppHeader.vue
├─ TodoList.vue
│  └─ TodoItem.vue
└─ AppFooter.vue
```

큰 앱도 결국 하나의 루트 컴포넌트에서 시작한다. 그 아래에 여러 컴포넌트가 나무처럼 붙는다.

---

## mount란?

`mount`는 Vue 앱을 실제 HTML 요소에 연결하는 메서드다.

```js
createApp(App).mount('#app')
```

여기서 `#app`은 `index.html`에 있는 요소를 가리킨다.

```html
<div id="app"></div>
```

Vue는 이 요소 안에 `App.vue`의 내용을 렌더링한다.

---

## 흐름을 그림처럼 보기

```text
index.html
└─ <div id="app"></div>
        ▲
        │ mount('#app')
        │
src/main.js
└─ createApp(App)
        ▲
        │ import
        │
src/App.vue
```

브라우저는 `index.html`을 읽고, `main.js`를 실행한다. `main.js`는 `App.vue`를 가져와 Vue 앱을 만들고 `#app`에 연결한다.

---

## app 인스턴스를 변수에 담기

처음에는 한 줄로 쓰는 경우가 많다.

```js
createApp(App).mount('#app')
```

전역 설정이나 플러그인을 추가할 때는 변수에 담아 사용한다.

```js
import { createApp } from 'vue'
import App from './App.vue'

const app = createApp(App)

app.config.errorHandler = (error) => {
  console.error(error)
}

app.mount('#app')
```

중요한 점은 설정을 마친 뒤 마지막에 `mount`를 호출한다는 것이다.

---

## 한 페이지에 여러 Vue 앱 만들기

Vue는 한 페이지에 여러 앱을 붙일 수도 있다.

```html
<div id="profile"></div>
<div id="cart"></div>
```

```js
createApp(ProfileApp).mount('#profile')
createApp(CartApp).mount('#cart')
```

기존 서버 렌더링 페이지의 일부에만 Vue 기능을 붙일 때 유용하다. 다만 일반적인 SPA 프로젝트에서는 하나의 루트 앱으로 시작하는 경우가 많다.

---

## 자주 하는 실수

### mount 대상이 HTML에 없음

```js
createApp(App).mount('#app')
```

이 코드를 사용하려면 `index.html`에 다음 요소가 있어야 한다.

```html
<div id="app"></div>
```

### mount를 너무 일찍 호출함

전역 컴포넌트나 플러그인을 등록해야 한다면 `mount` 전에 처리한다.

```js
const app = createApp(App)

app.use(router)
app.use(pinia)

app.mount('#app')
```

### App.vue와 index.html을 헷갈림

`index.html`은 앱이 붙을 자리다. 실제 화면 대부분은 `App.vue`와 그 아래 컴포넌트에 작성한다.

---

## 작은 실습

`src/main.js`를 열고 다음 세 가지를 찾아보자.

1. `createApp`을 import하는 코드
2. `App.vue`를 import하는 코드
3. `mount('#app')`를 호출하는 코드

그리고 `index.html`에서 `id="app"`인 요소도 찾아보자.

---

## 이번 장 요약

- `createApp`은 Vue 애플리케이션 인스턴스를 만든다.
- `App.vue`는 보통 루트 컴포넌트 역할을 한다.
- `mount('#app')`는 Vue 앱을 HTML 요소에 연결한다.
- 앱 설정과 플러그인 등록은 `mount` 전에 한다.

---

## 다음 장으로

다음 장에서는 Vue 템플릿 문법을 배운다. `{{ }}`, `v-bind`, 디렉티브 같은 문법이 등장한다.

