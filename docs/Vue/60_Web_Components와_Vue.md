# Web Components와 Vue

## 이번 장에서 배울 것

Web Components는 브라우저 표준 기술을 사용해 재사용 가능한 커스텀 엘리먼트를 만드는 방식이다.

Vue는 Web Components를 사용할 수도 있고, Vue 컴포넌트를 커스텀 엘리먼트로 만들 수도 있다.

---

## Web Components란?

Web Components는 여러 브라우저 표준을 묶어 부르는 말이다.

주요 기술:

- Custom Elements
- Shadow DOM
- HTML Templates

예를 들어 `<my-button>` 같은 직접 만든 HTML 태그를 브라우저에서 사용할 수 있다.

```html
<my-button>저장</my-button>
```

---

## Vue 컴포넌트와 Web Components 차이

| 구분 | Vue 컴포넌트 | Web Components |
| --- | --- | --- |
| 실행 환경 | Vue 앱 안 | 브라우저 표준 |
| 문법 | `.vue`, template, props, emit | custom element API |
| 재사용 범위 | Vue 프로젝트 중심 | 여러 프레임워크에서 사용 가능 |
| 개발 경험 | Vue 기능 활용 | 표준 기반 |

일반 Vue 앱에서는 Vue 컴포넌트를 사용하면 된다. Web Components는 프레임워크와 무관하게 배포해야 할 때 고려한다.

---

## Vue에서 Web Component 사용하기

외부에서 제공하는 커스텀 엘리먼트를 Vue 템플릿에서 사용할 수 있다.

```vue
<template>
  <my-calendar selected-date="2026-05-20"></my-calendar>
</template>
```

Vue가 이것을 Vue 컴포넌트로 해석하려고 할 수 있으므로, Vite 설정에서 custom element로 처리할 태그를 알려줄 수 있다.

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [
    vue({
      template: {
        compilerOptions: {
          isCustomElement: (tag) => tag.startsWith('my-')
        }
      }
    })
  ]
})
```

---

## Vue 컴포넌트를 Custom Element로 만들기

Vue는 `defineCustomElement`를 제공한다.

```js
import { defineCustomElement } from 'vue'
import MyButton from './MyButton.ce.vue'

const MyButtonElement = defineCustomElement(MyButton)

customElements.define('my-button', MyButtonElement)
```

이렇게 등록하면 HTML에서 사용할 수 있다.

```html
<my-button></my-button>
```

---

## .ce.vue 파일

Vue에서 custom element 용도로 SFC를 만들 때 `.ce.vue` 확장자 패턴을 사용할 수 있다. 빌드 설정과 프로젝트 구조에 따라 다를 수 있으므로 공식 문서와 프로젝트 설정을 함께 확인한다.

예:

```vue
<script setup>
defineProps({
  label: String
})
</script>

<template>
  <button>{{ label }}</button>
</template>
```

---

## Props와 이벤트

Custom Element도 속성이나 property로 값을 받을 수 있다.

```html
<my-button label="저장"></my-button>
```

Vue 컴포넌트의 emit은 CustomEvent로 변환되어 외부에서 들을 수 있다.

```js
document
  .querySelector('my-button')
  .addEventListener('save', (event) => {
    console.log(event.detail)
  })
```

---

## 언제 사용할까?

Web Components는 다음 상황에 적합하다.

- Vue, React, Angular 등 여러 환경에서 같은 UI를 써야 한다.
- 디자인 시스템 컴포넌트를 프레임워크 독립적으로 배포해야 한다.
- 기존 웹 페이지에 작은 독립 컴포넌트를 붙여야 한다.
- 외부 서비스에 위젯 형태로 배포해야 한다.

일반 Vue 앱 내부 UI는 Vue 컴포넌트가 더 단순하다.

---

## 주의할 점

Web Components는 좋은 선택지가 될 수 있지만 모든 문제의 답은 아니다.

고려할 점:

- 번들 크기
- 스타일 캡슐화 방식
- 폼과 이벤트 연동
- 브라우저 지원 범위
- 팀의 경험
- 접근성 책임

Vue 앱 하나 안에서만 쓸 컴포넌트라면 일반 Vue 컴포넌트가 보통 더 쉽다.

---

## 자주 하는 실수

### Web Components가 Vue 컴포넌트를 대체한다고 생각함

Vue 앱 내부에서는 Vue 컴포넌트가 기본 선택이다.

### custom element 태그를 Vue 컴포넌트로 해석하게 둠

필요하면 `isCustomElement` 설정을 추가한다.

### 속성과 property 차이를 무시함

문자열 속성과 JavaScript property는 다를 수 있다. 외부 Web Component 문서를 확인한다.

---

## 작은 실습

Vue에서 외부 custom element를 사용한다고 가정해보자.

- `my-widget` 태그를 템플릿에 추가
- Vite 설정에서 `tag.startsWith('my-')`를 custom element로 처리
- Vue 컴포넌트와 custom element의 차이를 정리

---

## 이번 장 요약

- Web Components는 브라우저 표준 기반 커스텀 엘리먼트 기술이다.
- Vue는 Web Components를 사용할 수 있고, Vue 컴포넌트를 Custom Element로 만들 수도 있다.
- 일반 Vue 앱 내부에서는 Vue 컴포넌트가 기본 선택이다.
- 프레임워크 독립 배포가 필요할 때 Web Components를 고려한다.

---

## 다음 장으로

다음 장에서는 SSR, SSG, Nuxt의 큰 그림을 배운다.

