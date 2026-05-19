# 템플릿 참조와 DOM 접근

## 이번 장에서 배울 것

Vue에서는 대부분의 화면 처리를 상태와 템플릿으로 해결한다. 하지만 가끔 실제 DOM 요소에 직접 접근해야 할 때가 있다.

예:

- 입력 칸에 자동 포커스 주기
- 스크롤 위치 조정하기
- 캔버스나 외부 UI 라이브러리 초기화하기

이때 템플릿 참조를 사용한다.

---

## 템플릿 참조 기본

템플릿에서 `ref` 속성을 붙이고, script에서 같은 이름의 `ref`를 만든다.

```vue
<script setup>
import { onMounted, ref } from 'vue'

const inputRef = ref(null)

onMounted(() => {
  inputRef.value.focus()
})
</script>

<template>
  <input ref="inputRef" placeholder="자동 포커스">
</template>
```

컴포넌트가 화면에 붙은 뒤 `inputRef.value`에 실제 input 요소가 들어온다.

---

## 왜 onMounted 안에서 접근할까?

컴포넌트가 생성되기 전에는 DOM 요소가 아직 없다. 따라서 `inputRef.value`는 처음에는 `null`이다.

DOM에 안전하게 접근하려면 `onMounted` 이후에 접근한다.

```js
onMounted(() => {
  inputRef.value.focus()
})
```

---

## null 확인하기

조건부 렌더링과 함께 쓰면 참조가 없을 수 있다. 안전하게 확인하는 습관을 들이자.

```js
onMounted(() => {
  if (inputRef.value) {
    inputRef.value.focus()
  }
})
```

---

## 버튼으로 포커스 주기

```vue
<script setup>
import { ref } from 'vue'

const inputRef = ref(null)

function focusInput() {
  if (inputRef.value) {
    inputRef.value.focus()
  }
}
</script>

<template>
  <input ref="inputRef" placeholder="이름">
  <button @click="focusInput">입력 칸으로 이동</button>
</template>
```

---

## v-for와 템플릿 참조

목록에서 여러 요소를 참조해야 할 때는 배열 형태로 다룰 수 있다. 다만 초보 단계에서는 가능한 한 DOM 직접 접근을 줄이고, 상태 중심으로 해결하는 것이 좋다.

목록 항목에 직접 접근해야 하는 경우는 드물다. 먼저 `v-for`, `:class`, `computed`로 해결할 수 있는지 생각해보자.

---

## 컴포넌트 참조

`ref`는 DOM 요소뿐 아니라 자식 컴포넌트에도 붙일 수 있다.

```html
<UserForm ref="userFormRef" />
```

컴포넌트 참조는 나중에 컴포넌트 심화에서 다시 다룬다. 컴포넌트 사이 통신은 보통 Props와 Emit을 먼저 사용한다.

---

## DOM 직접 조작을 남발하지 않기

Vue에서는 아래처럼 DOM을 직접 바꾸는 코드를 자주 쓰지 않는다.

```js
document.querySelector('.message').textContent = '변경'
```

대부분은 상태를 바꾸면 된다.

```vue
<script setup>
import { ref } from 'vue'

const message = ref('변경')
</script>

<template>
  <p>{{ message }}</p>
</template>
```

템플릿 참조는 꼭 필요한 경우에만 사용한다.

---

## 자주 하는 실수

### ref 이름이 서로 다름

```js
const inputRef = ref(null)
```

```html
<input ref="nameInput">
```

script의 이름과 template의 이름이 다르면 연결되지 않는다.

### mounted 전에 DOM에 접근함

```js
inputRef.value.focus()
```

이 코드를 script 최상단에서 바로 실행하면 아직 DOM이 없을 수 있다. `onMounted` 안에서 실행한다.

### DOM 조작으로 상태를 대신하려 함

Vue에서는 상태가 화면의 기준이다. DOM 직접 조작은 포커스, 스크롤, 외부 라이브러리 연동처럼 필요한 경우에만 사용한다.

---

## 작은 실습

아래 기능을 만들어보자.

- 페이지가 열리면 검색 입력 칸에 자동 포커스를 준다.
- "검색창으로 이동" 버튼을 누르면 다시 입력 칸에 포커스를 준다.
- 입력 값은 `v-model`로 상태와 연결한다.

---

## 이번 장 요약

- 템플릿 참조는 DOM 요소나 컴포넌트에 접근할 때 사용한다.
- `ref="inputRef"`와 `const inputRef = ref(null)`를 연결한다.
- DOM 요소는 `onMounted` 이후 안전하게 접근한다.
- 대부분의 화면 변경은 DOM 조작보다 상태 변경으로 해결한다.

---

## 다음 장으로

다음 장에서는 컴포넌트가 생성되고 사라지는 과정에서 실행되는 생명주기 훅을 배운다.

