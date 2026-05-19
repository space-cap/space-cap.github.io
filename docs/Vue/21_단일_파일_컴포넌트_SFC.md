# 단일 파일 컴포넌트 SFC

## 이번 장에서 배울 것

SFC는 Single-File Component의 줄임말이다. Vue에서 `.vue` 확장자를 가진 파일을 말한다.

Vue 프로젝트를 만들면 `App.vue` 같은 파일을 보게 된다. 이 파일 안에는 한 컴포넌트의 화면, 로직, 스타일이 함께 들어간다.

---

## SFC 기본 구조

```vue
<script setup>
import { ref } from 'vue'

const message = ref('안녕하세요 Vue')
</script>

<template>
  <p class="message">{{ message }}</p>
</template>

<style scoped>
.message {
  color: #2563eb;
  font-weight: 700;
}
</style>
```

SFC는 보통 세 영역으로 나뉜다.

| 영역 | 역할 |
| --- | --- |
| `<script setup>` | JavaScript 로직 |
| `<template>` | 화면 구조 |
| `<style scoped>` | 스타일 |

---

## script setup

`<script setup>`은 Vue 3에서 Composition API를 간단하게 작성할 수 있게 해주는 문법이다.

```vue
<script setup>
const title = 'Vue 컴포넌트'
</script>

<template>
  <h1>{{ title }}</h1>
</template>
```

`<script setup>` 안에서 선언한 변수와 함수는 템플릿에서 바로 사용할 수 있다.

```vue
<script setup>
function sayHello() {
  alert('Hello')
}
</script>

<template>
  <button @click="sayHello">인사</button>
</template>
```

---

## template

`<template>`은 컴포넌트의 HTML 구조를 작성하는 영역이다.

```vue
<template>
  <article>
    <h2>게시글 제목</h2>
    <p>게시글 내용</p>
  </article>
</template>
```

Vue 템플릿은 HTML을 기반으로 하지만 `v-if`, `v-for`, `:class`, `@click` 같은 Vue 문법도 함께 사용할 수 있다.

---

## style scoped

`<style>`에는 CSS를 작성한다.

```vue
<style scoped>
.card {
  padding: 16px;
  border: 1px solid #ddd;
}
</style>
```

`scoped`를 붙이면 이 스타일은 현재 컴포넌트 안에만 적용된다. 여러 컴포넌트에서 같은 class 이름을 써도 스타일 충돌을 줄일 수 있다.

---

## 컴포넌트 파일 만들기

`src/components/AppCard.vue` 파일을 만들어보자.

```vue
<template>
  <article class="card">
    <h2>카드 제목</h2>
    <p>카드 내용입니다.</p>
  </article>
</template>

<style scoped>
.card {
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
}
</style>
```

그리고 `App.vue`에서 가져와 사용한다.

```vue
<script setup>
import AppCard from './components/AppCard.vue'
</script>

<template>
  <AppCard />
</template>
```

---

## SFC를 사용하는 이유

SFC는 빌드 도구가 필요하지만, 실무 프로젝트에서는 장점이 크다.

- 화면, 로직, 스타일을 컴포넌트 단위로 묶을 수 있다.
- `.vue` 파일 하나만 보면 해당 UI의 핵심을 파악하기 쉽다.
- `scoped` 스타일로 충돌을 줄일 수 있다.
- VS Code 확장으로 자동 완성과 오류 확인을 받을 수 있다.
- Vite의 빠른 새로고침으로 개발 경험이 좋다.

---

## 관심사의 분리

처음에는 HTML, CSS, JavaScript가 한 파일에 섞여 보일 수 있다. 하지만 SFC의 목적은 파일 종류별로 나누는 것이 아니라 **기능 단위로 묶는 것**이다.

예를 들어 `TodoItem.vue`에는 Todo 항목 하나의 구조, 동작, 스타일이 함께 들어간다. 그래서 해당 UI를 수정할 때 여러 파일을 헤매지 않아도 된다.

---

## 자주 하는 실수

### 컴포넌트 이름을 소문자로 시작함

SFC 컴포넌트 파일은 보통 PascalCase를 사용한다.

```text
AppCard.vue
TodoItem.vue
BaseButton.vue
```

### style에 scoped를 항상 잊음

컴포넌트 전용 스타일이라면 `scoped`를 붙이는 습관이 좋다.

### App.vue에 모든 코드를 계속 넣음

처음에는 `App.vue`에서 시작해도 되지만, 코드가 길어지면 역할별 컴포넌트로 나눈다.

---

## 작은 실습

`src/components/UserProfile.vue` 파일을 만들고 다음 내용을 작성해보자.

- 이름을 보여주는 `h2`
- 소개 문장을 보여주는 `p`
- 프로필 박스 스타일

그다음 `App.vue`에서 import해 화면에 표시한다.

---

## 이번 장 요약

- SFC는 `.vue` 확장자를 가진 단일 파일 컴포넌트다.
- `<script setup>`, `<template>`, `<style scoped>`로 구성된다.
- 컴포넌트 단위로 화면, 로직, 스타일을 묶어 관리한다.
- Vite 기반 Vue 프로젝트에서는 SFC를 기본 방식으로 사용한다.

---

## 다음 장으로

다음 장에서는 만든 컴포넌트를 어떻게 등록하고 사용하는지 배운다.

