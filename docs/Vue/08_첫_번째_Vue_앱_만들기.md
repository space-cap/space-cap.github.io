# 첫 번째 Vue 앱 만들기

## 이번 장에서 배울 것

이번 장에서는 Vite로 만든 Vue 프로젝트에서 첫 번째 작은 앱을 만든다. 목표는 Vue의 기본 흐름을 한 번에 경험하는 것이다.

사용할 기능은 다음과 같다.

- 상태 만들기
- 화면에 상태 표시하기
- 버튼 클릭 처리하기
- 입력 값 연결하기
- 목록 출력하기
- 조건에 따라 화면 바꾸기

---

## 시작 파일 정리

`src/App.vue` 파일을 열고 기존 내용을 모두 지운 뒤 아래 코드부터 시작한다.

```vue
<script setup>
import { ref } from 'vue'

const title = ref('나의 첫 Vue 앱')
</script>

<template>
  <main>
    <h1>{{ title }}</h1>
  </main>
</template>
```

브라우저에 `나의 첫 Vue 앱`이 보이면 준비가 된 것이다.

---

## ref로 상태 만들기

Vue에서 화면과 연결되는 값을 만들 때 `ref`를 자주 사용한다.

```js
import { ref } from 'vue'

const count = ref(0)
```

`count`는 그냥 숫자가 아니라 Vue가 변화를 추적할 수 있는 값이다.

JavaScript 코드 안에서 값을 읽거나 바꿀 때는 `.value`를 사용한다.

```js
count.value++
```

하지만 템플릿에서는 `.value`를 쓰지 않아도 된다.

```html
<p>{{ count }}</p>
```

---

## 카운터 만들기

`App.vue`를 다음처럼 수정한다.

```vue
<script setup>
import { ref } from 'vue'

const count = ref(0)

function increase() {
  count.value++
}

function decrease() {
  count.value--
}

function reset() {
  count.value = 0
}
</script>

<template>
  <main>
    <h1>카운터</h1>
    <p>현재 숫자: {{ count }}</p>

    <button type="button" @click="increase">1 증가</button>
    <button type="button" @click="decrease">1 감소</button>
    <button type="button" @click="reset">초기화</button>
  </main>
</template>
```

버튼을 누르면 숫자가 바뀐다. 이 예제에는 Vue의 중요한 흐름이 모두 들어 있다.

| 코드 | 의미 |
| --- | --- |
| `ref(0)` | 반응형 상태를 만든다. |
| `{{ count }}` | 상태를 화면에 표시한다. |
| `@click` | 클릭 이벤트를 연결한다. |
| `count.value++` | 상태 값을 변경한다. |

---

## 입력 값 연결하기

이번에는 이름을 입력하면 인사 문장이 바뀌는 기능을 추가한다.

```vue
<script setup>
import { ref } from 'vue'

const name = ref('')
</script>

<template>
  <main>
    <h1>인사하기</h1>

    <input v-model="name" placeholder="이름을 입력하세요">
    <p>안녕하세요, {{ name }}님!</p>
  </main>
</template>
```

`v-model`은 입력 요소와 상태를 양방향으로 연결한다.

- 사용자가 입력하면 `name`이 바뀐다.
- `name`이 바뀌면 화면도 바뀐다.

---

## Todo 목록 만들기

이제 카운터보다 조금 더 실제 앱에 가까운 Todo 목록을 만든다.

```vue
<script setup>
import { ref } from 'vue'

const newTodo = ref('')
const todos = ref([
  { id: 1, title: 'Vue 프로젝트 실행하기', done: true },
  { id: 2, title: '첫 컴포넌트 만들기', done: false }
])

function addTodo() {
  const title = newTodo.value.trim()

  if (!title) {
    return
  }

  todos.value.push({
    id: Date.now(),
    title,
    done: false
  })

  newTodo.value = ''
}

function toggleTodo(todo) {
  todo.done = !todo.done
}
</script>

<template>
  <main>
    <h1>Todo 목록</h1>

    <form @submit.prevent="addTodo">
      <input v-model="newTodo" placeholder="할 일을 입력하세요">
      <button type="submit">추가</button>
    </form>

    <p v-if="todos.length === 0">아직 할 일이 없습니다.</p>

    <ul v-else>
      <li v-for="todo in todos" :key="todo.id">
        <label>
          <input
            type="checkbox"
            :checked="todo.done"
            @change="toggleTodo(todo)"
          >
          <span>{{ todo.title }}</span>
        </label>
      </li>
    </ul>
  </main>
</template>
```

---

## Todo 예제에서 사용한 Vue 문법

### `v-model`

입력 값과 상태를 연결한다.

```html
<input v-model="newTodo">
```

### `@submit.prevent`

폼 제출 이벤트를 처리하고, 브라우저의 기본 새로고침 동작을 막는다.

```html
<form @submit.prevent="addTodo">
```

### `v-if`

조건이 참일 때만 화면에 보여준다.

```html
<p v-if="todos.length === 0">아직 할 일이 없습니다.</p>
```

### `v-else`

앞의 `v-if` 조건이 거짓일 때 보여준다.

```html
<ul v-else>
```

### `v-for`

배열을 반복해서 화면에 그린다.

```html
<li v-for="todo in todos" :key="todo.id">
```

### `:key`

Vue가 목록 항목을 안정적으로 구분할 수 있게 돕는다. `v-for`를 사용할 때는 가능하면 고유한 `key`를 함께 사용한다.

---

## 약간의 스타일 추가하기

아래 스타일을 같은 파일의 아래쪽에 추가해보자.

```vue
<style scoped>
main {
  max-width: 640px;
  margin: 40px auto;
  padding: 0 20px;
  font-family: system-ui, sans-serif;
}

form {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

input[type='text'],
input:not([type]) {
  flex: 1;
  padding: 8px 10px;
}

button {
  padding: 8px 12px;
  cursor: pointer;
}

li {
  margin: 8px 0;
}
</style>
```

`scoped`가 붙어 있으므로 이 스타일은 현재 컴포넌트에만 적용된다.

---

## 완성 코드

아래는 이번 장에서 만든 Todo 앱의 전체 코드다.

```vue
<script setup>
import { ref } from 'vue'

const newTodo = ref('')
const todos = ref([
  { id: 1, title: 'Vue 프로젝트 실행하기', done: true },
  { id: 2, title: '첫 컴포넌트 만들기', done: false }
])

function addTodo() {
  const title = newTodo.value.trim()

  if (!title) {
    return
  }

  todos.value.push({
    id: Date.now(),
    title,
    done: false
  })

  newTodo.value = ''
}

function toggleTodo(todo) {
  todo.done = !todo.done
}
</script>

<template>
  <main>
    <h1>Todo 목록</h1>

    <form @submit.prevent="addTodo">
      <input v-model="newTodo" placeholder="할 일을 입력하세요">
      <button type="submit">추가</button>
    </form>

    <p v-if="todos.length === 0">아직 할 일이 없습니다.</p>

    <ul v-else>
      <li v-for="todo in todos" :key="todo.id">
        <label>
          <input
            type="checkbox"
            :checked="todo.done"
            @change="toggleTodo(todo)"
          >
          <span>{{ todo.title }}</span>
        </label>
      </li>
    </ul>
  </main>
</template>

<style scoped>
main {
  max-width: 640px;
  margin: 40px auto;
  padding: 0 20px;
  font-family: system-ui, sans-serif;
}

form {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

input[type='text'],
input:not([type]) {
  flex: 1;
  padding: 8px 10px;
}

button {
  padding: 8px 12px;
  cursor: pointer;
}

li {
  margin: 8px 0;
}
</style>
```

---

## 자주 하는 실수

### JavaScript에서 `.value`를 빼먹음

`ref`로 만든 값은 JavaScript 코드에서 `.value`로 접근한다.

```js
newTodo.value = ''
```

템플릿에서는 `.value`를 쓰지 않는다.

```html
{{ newTodo }}
```

### v-for에 key를 넣지 않음

목록에는 고유한 `key`를 넣는 습관을 들인다.

```html
<li v-for="todo in todos" :key="todo.id">
```

### form 제출 시 화면이 새로고침됨

폼 제출 기본 동작을 막으려면 `.prevent`를 사용한다.

```html
<form @submit.prevent="addTodo">
```

---

## 작은 실습

Todo 앱에 다음 기능을 추가해보자.

1. 완료된 Todo 개수를 화면에 표시한다.
2. 각 Todo 옆에 삭제 버튼을 추가한다.
3. 할 일이 없을 때만 "아직 할 일이 없습니다." 문장을 보여준다.

힌트:

```js
function removeTodo(id) {
  todos.value = todos.value.filter((todo) => todo.id !== id)
}
```

완료 개수는 다음 장 이후 `computed`를 배우면 더 깔끔하게 만들 수 있다. 지금은 함수나 간단한 표현식으로 시도해도 좋다.

---

## 이번 장 요약

- `ref`로 Vue 상태를 만들 수 있다.
- 템플릿에서는 `{{ }}`로 상태를 표시한다.
- `@click`, `@submit`으로 이벤트를 처리한다.
- `v-model`로 입력 값과 상태를 연결한다.
- `v-if`, `v-else`로 조건부 화면을 만든다.
- `v-for`로 배열을 목록으로 출력한다.

---

## 다음 장으로

다음 장부터는 Vue 기본기를 하나씩 더 깊게 배운다. 먼저 애플리케이션 인스턴스와 `mount`가 어떤 역할을 하는지 살펴본다.

