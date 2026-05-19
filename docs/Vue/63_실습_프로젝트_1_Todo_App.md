# 실습 프로젝트 1: Todo App

## 이번 프로젝트에서 만들 것

첫 번째 실습 프로젝트는 Todo App이다. Vue 입문에서 가장 좋은 프로젝트 중 하나다. 입력, 목록, 조건부 렌더링, 이벤트, localStorage까지 기본기를 한 번에 연습할 수 있다.

---

## 학습 목표

이 프로젝트를 마치면 다음을 할 수 있어야 한다.

- `ref`로 상태를 만든다.
- `v-model`로 입력값을 연결한다.
- `v-for`로 목록을 렌더링한다.
- `v-if`로 빈 목록 안내를 보여준다.
- `computed`로 완료 개수와 남은 개수를 계산한다.
- localStorage로 데이터를 저장하고 복원한다.
- 컴포넌트를 분리한다.

---

## 완성 기능

Todo App은 다음 기능을 가진다.

- 할 일 추가
- 할 일 완료/미완료 전환
- 할 일 삭제
- 전체/진행 중/완료 필터
- 완료 개수와 남은 개수 표시
- localStorage 저장
- 빈 목록 안내

---

## 추천 파일 구조

```text
src/
├─ App.vue
├─ components/
│  ├─ TodoForm.vue
│  ├─ TodoFilter.vue
│  ├─ TodoItem.vue
│  └─ TodoStats.vue
└─ composables/
   └─ useTodos.js
```

처음에는 `App.vue` 한 파일로 만들고, 기능이 동작하면 컴포넌트와 Composable로 분리해도 된다.

---

## 1단계: 기본 상태 만들기

```vue
<script setup>
import { ref } from 'vue'

const newTodo = ref('')
const todos = ref([
  { id: 1, title: 'Vue 기본기 복습', done: true },
  { id: 2, title: 'Todo App 만들기', done: false }
])
</script>
```

Todo 객체는 `id`, `title`, `done`을 가진다.

---

## 2단계: 목록 출력하기

```vue
<template>
  <main>
    <h1>Todo App</h1>

    <ul>
      <li v-for="todo in todos" :key="todo.id">
        <label>
          <input type="checkbox" v-model="todo.done">
          <span>{{ todo.title }}</span>
        </label>
      </li>
    </ul>
  </main>
</template>
```

`v-for`에는 고유한 `key`를 넣는다.

---

## 3단계: 할 일 추가하기

```vue
<script setup>
import { ref } from 'vue'

const newTodo = ref('')
const todos = ref([])

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
</script>

<template>
  <form @submit.prevent="addTodo">
    <input v-model="newTodo" placeholder="할 일을 입력하세요">
    <button type="submit">추가</button>
  </form>
</template>
```

빈 문자열은 추가하지 않는다.

---

## 4단계: 삭제 기능 만들기

```js
function removeTodo(id) {
  todos.value = todos.value.filter((todo) => todo.id !== id)
}
```

템플릿:

```html
<button type="button" @click="removeTodo(todo.id)">삭제</button>
```

배열에서 특정 항목을 제거할 때는 `filter`를 자주 사용한다.

---

## 5단계: 계산된 값 만들기

```js
import { computed, ref } from 'vue'

const totalCount = computed(() => todos.value.length)
const doneCount = computed(() => {
  return todos.value.filter((todo) => todo.done).length
})
const remainingCount = computed(() => totalCount.value - doneCount.value)
```

템플릿:

```html
<p>
  전체 {{ totalCount }}개 /
  완료 {{ doneCount }}개 /
  남음 {{ remainingCount }}개
</p>
```

---

## 6단계: 필터 만들기

```js
const filter = ref('all')

const filteredTodos = computed(() => {
  if (filter.value === 'active') {
    return todos.value.filter((todo) => !todo.done)
  }

  if (filter.value === 'done') {
    return todos.value.filter((todo) => todo.done)
  }

  return todos.value
})
```

템플릿:

```html
<button @click="filter = 'all'">전체</button>
<button @click="filter = 'active'">진행 중</button>
<button @click="filter = 'done'">완료</button>

<ul>
  <li v-for="todo in filteredTodos" :key="todo.id">
    {{ todo.title }}
  </li>
</ul>
```

---

## 7단계: localStorage 저장하기

```js
import { ref, watch } from 'vue'

const savedTodos = localStorage.getItem('todos')
const todos = ref(savedTodos ? JSON.parse(savedTodos) : [])

watch(
  todos,
  (newTodos) => {
    localStorage.setItem('todos', JSON.stringify(newTodos))
  },
  { deep: true }
)
```

Todo가 추가, 삭제, 완료 전환될 때 localStorage에 저장된다.

---

## 8단계: 컴포넌트 분리하기

기능이 동작하면 컴포넌트로 나눈다.

| 컴포넌트 | 역할 |
| --- | --- |
| `TodoForm.vue` | 새 Todo 입력과 추가 |
| `TodoItem.vue` | Todo 하나 표시, 완료 전환, 삭제 |
| `TodoFilter.vue` | 전체/진행 중/완료 필터 |
| `TodoStats.vue` | 개수 표시 |

`TodoItem.vue`는 props와 emit을 연습하기 좋다.

```vue
<script setup>
defineProps({
  todo: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['toggle', 'remove'])
</script>

<template>
  <li>
    <label>
      <input
        type="checkbox"
        :checked="todo.done"
        @change="emit('toggle', todo.id)"
      >
      {{ todo.title }}
    </label>

    <button @click="emit('remove', todo.id)">삭제</button>
  </li>
</template>
```

---

## 확장 과제

기본 기능을 완성했다면 다음을 추가해보자.

- Todo 수정 기능
- 전체 완료 처리
- 완료된 Todo 모두 삭제
- 필터 상태도 localStorage에 저장
- TransitionGroup으로 목록 애니메이션
- TypeScript 적용
- Vitest로 TodoItem 테스트 작성

---

## 점검 체크리스트

- 빈 값은 추가되지 않는다.
- 새로고침해도 Todo가 유지된다.
- 삭제 버튼이 올바른 항목만 삭제한다.
- 완료 개수와 남은 개수가 정확하다.
- 필터가 정상 동작한다.
- `v-for`에 안정적인 `key`가 있다.

---

## 이번 프로젝트 요약

Todo App은 Vue의 핵심 기본기를 연결하는 프로젝트다. 입력, 이벤트, 목록, computed, watch, localStorage, 컴포넌트 분리까지 모두 연습할 수 있다.

---

## 다음 프로젝트로

다음 프로젝트에서는 Vue Router와 API 통신을 사용해 게시판을 만든다.

