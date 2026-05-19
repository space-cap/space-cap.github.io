# Vue 2에서 Vue 3로 마이그레이션

## 이번 장에서 배울 것

새로 Vue를 배우는 사람은 Vue 3부터 시작하면 된다. 하지만 실무에서는 Vue 2 프로젝트를 만날 수 있다.

이번 장에서는 Vue 2에서 Vue 3로 넘어갈 때 큰 방향과 주요 변경점을 입문자 관점에서 정리한다.

---

## Vue 2의 현재 상태

Vue 2는 2023년 12월 31일에 공식 지원이 종료되었다. 새 프로젝트는 Vue 3로 시작하는 것이 좋다.

기존 Vue 2 프로젝트는 보안, 라이브러리 호환성, 유지보수 비용을 고려해 마이그레이션 계획을 세워야 한다.

---

## 마이그레이션은 단순 버전 업이 아니다

Vue 2에서 Vue 3로 이동할 때는 다음을 함께 확인해야 한다.

- Vue 코어 버전
- 빌드 도구
- Vue Router 버전
- Vuex 또는 Pinia
- UI 라이브러리 호환성
- 테스트 도구
- 브라우저 지원 범위
- 전역 API 사용 방식
- 삭제되거나 변경된 문법

작은 프로젝트는 빠르게 이동할 수 있지만, 큰 프로젝트는 단계적으로 접근해야 한다.

---

## 주요 변화 요약

| 영역 | Vue 2 | Vue 3 |
| --- | --- | --- |
| 앱 생성 | `new Vue()` | `createApp()` |
| 상태 로직 | Options API 중심 | Composition API 추가 |
| 다중 루트 | 불가 | 가능 |
| v-model | `value`/`input` 중심 | `modelValue`/`update:modelValue`, `defineModel` |
| 상태 관리 | Vuex 많이 사용 | Pinia 권장 |
| 빌드 도구 | Vue CLI 많이 사용 | create-vue + Vite 권장 |

---

## 앱 생성 방식 변경

Vue 2:

```js
import Vue from 'vue'
import App from './App.vue'

new Vue({
  render: (h) => h(App)
}).$mount('#app')
```

Vue 3:

```js
import { createApp } from 'vue'
import App from './App.vue'

createApp(App).mount('#app')
```

Vue 3에서는 앱 인스턴스를 만들고, 플러그인과 전역 설정을 앱 단위로 등록한다.

---

## 전역 API 변경

Vue 2에서는 전역 Vue 객체에 설정하는 코드가 많았다.

```js
Vue.component('BaseButton', BaseButton)
Vue.use(plugin)
Vue.mixin(mixin)
```

Vue 3에서는 앱 인스턴스에 등록한다.

```js
const app = createApp(App)

app.component('BaseButton', BaseButton)
app.use(plugin)
app.mixin(mixin)

app.mount('#app')
```

---

## 다중 루트 컴포넌트

Vue 2에서는 template에 루트 요소가 하나만 가능했다.

```vue
<template>
  <div>
    <Header />
    <main>본문</main>
  </div>
</template>
```

Vue 3에서는 여러 루트 요소를 둘 수 있다.

```vue
<template>
  <Header />
  <main>본문</main>
  <Footer />
</template>
```

---

## v-model 변경

Vue 2의 컴포넌트 `v-model`은 기본적으로 `value` prop과 `input` 이벤트를 사용했다.

Vue 3에서는 `modelValue` prop과 `update:modelValue` 이벤트를 기반으로 한다.

Vue 3.4 이후에는 `defineModel()`로 더 간결하게 작성할 수 있다.

```vue
<script setup>
const model = defineModel()
</script>

<template>
  <input v-model="model">
</template>
```

기존 Vue 2 입력 컴포넌트를 마이그레이션할 때 이 차이를 반드시 확인한다.

---

## Vuex와 Pinia

Vue 2 프로젝트에서는 Vuex를 많이 사용했다. Vue 3에서도 Vuex를 사용할 수 있지만, 현재 Vue 공식 상태 관리 흐름은 Pinia를 권장한다.

마이그레이션 시 선택지는 두 가지다.

- 우선 Vuex를 유지하며 Vue 3로 이동
- 상태 관리도 함께 Pinia로 전환

큰 프로젝트에서는 한 번에 모두 바꾸기보다 위험을 나누어 진행하는 것이 좋다.

---

## Vue Router 변경

Vue Router도 Vue 3에 맞는 버전을 사용해야 한다.

Vue Router 4에서는 라우터 생성 방식이 다음과 같다.

```js
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes
})
```

Vue 2 + Vue Router 3 코드와 API 차이를 확인해야 한다.

---

## Composition API 도입

Vue 3에서 Composition API가 추가되었지만, 모든 Options API 코드를 즉시 바꿀 필요는 없다.

Vue 3에서도 Options API를 사용할 수 있다. 따라서 마이그레이션 전략은 다음처럼 잡을 수 있다.

1. Vue 3에서 동작하도록 먼저 이동한다.
2. 새 기능은 Composition API로 작성한다.
3. 복잡한 기존 컴포넌트는 필요할 때 점진적으로 리팩터링한다.

---

## 빌드 도구 전환

Vue 2 프로젝트는 Vue CLI 또는 webpack 기반인 경우가 많다. 새 Vue 3 프로젝트는 create-vue와 Vite를 권장한다.

기존 프로젝트를 바로 Vite로 옮기는 것은 영향이 클 수 있다.

확인할 것:

- webpack 전용 loader와 plugin
- 환경 변수 이름
- alias 설정
- 정적 파일 경로
- 테스트 설정
- 배포 설정

---

## 마이그레이션 접근 순서

큰 프로젝트라면 다음 순서로 접근한다.

1. 현재 의존성 목록 확인
2. Vue 3 호환 안 되는 라이브러리 확인
3. 테스트 또는 주요 화면 수동 체크리스트 준비
4. Vue Router, 상태 관리, UI 라이브러리 전략 결정
5. 작은 브랜치에서 마이그레이션 시도
6. 빌드와 테스트 오류 해결
7. 주요 사용자 흐름 검증
8. 점진적 리팩터링

---

## 자주 하는 실수

### Vue 2 코드를 모두 Composition API로 바꾸려 함

마이그레이션의 첫 목표는 안정적으로 Vue 3에서 동작하게 만드는 것이다. 스타일 변경은 나중에 해도 된다.

### 라이브러리 호환성을 늦게 확인함

UI 라이브러리, 차트, 에디터, 날짜 라이브러리의 Vue 3 호환 여부를 먼저 확인한다.

### 테스트 없이 큰 변경을 진행함

테스트가 없다면 최소한 핵심 화면 수동 체크리스트라도 만든다.

---

## 작은 실습

아래 Vue 2 코드를 Vue 3 스타일로 바꿔보자.

```js
import Vue from 'vue'
import App from './App.vue'
import router from './router'

Vue.use(router)

new Vue({
  render: (h) => h(App)
}).$mount('#app')
```

힌트:

```js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

createApp(App)
  .use(router)
  .mount('#app')
```

---

## 이번 장 요약

- 새 학습자는 Vue 3부터 시작하면 된다.
- Vue 2는 2023년 12월 31일 공식 지원이 종료되었다.
- 마이그레이션은 Vue 코어뿐 아니라 Router, 상태 관리, 빌드 도구, UI 라이브러리를 함께 봐야 한다.
- Vue 3에서도 Options API를 사용할 수 있으므로 점진적 이전이 가능하다.
- 큰 프로젝트는 테스트와 체크리스트를 준비하고 단계적으로 진행한다.

---

## 다음 장으로

다음 장부터는 실습 프로젝트 구간이다. 먼저 Todo 앱을 완성하며 지금까지 배운 기본기를 연결한다.

