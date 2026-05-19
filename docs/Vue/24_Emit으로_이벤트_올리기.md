# Emit으로 이벤트 올리기

## 이번 장에서 배울 것

Props가 부모에서 자식으로 데이터를 내려보내는 방법이라면, Emit은 자식이 부모에게 사건을 알리는 방법이다.

예를 들어 Todo 항목의 삭제 버튼은 자식 컴포넌트 안에 있지만, 실제 목록 데이터는 부모가 가지고 있을 수 있다. 이때 자식은 "삭제해 주세요"라는 이벤트를 부모에게 보낸다.

---

## 기본 흐름

부모 `App.vue`:

```vue
<script setup>
import ChildButton from './components/ChildButton.vue'

function handleSave() {
  alert('부모에서 저장 처리')
}
</script>

<template>
  <ChildButton @save="handleSave" />
</template>
```

자식 `ChildButton.vue`:

```vue
<script setup>
const emit = defineEmits(['save'])

function clickButton() {
  emit('save')
}
</script>

<template>
  <button @click="clickButton">저장</button>
</template>
```

자식이 `emit('save')`를 실행하면 부모의 `@save`가 반응한다.

---

## 값 함께 전달하기

이벤트와 함께 값을 전달할 수 있다.

자식:

```vue
<script setup>
const props = defineProps({
  id: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['remove'])

function remove() {
  emit('remove', props.id)
}
</script>

<template>
  <button @click="remove">삭제</button>
</template>
```

부모:

```vue
<TodoItem @remove="removeTodo" />
```

```js
function removeTodo(id) {
  todos.value = todos.value.filter((todo) => todo.id !== id)
}
```

---

## TodoItem 예제

`TodoItem.vue`

```vue
<script setup>
const props = defineProps({
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
        :checked="props.todo.done"
        @change="emit('toggle', props.todo.id)"
      >
      {{ props.todo.title }}
    </label>

    <button @click="emit('remove', props.todo.id)">삭제</button>
  </li>
</template>
```

`App.vue`

```vue
<script setup>
import { ref } from 'vue'
import TodoItem from './components/TodoItem.vue'

const todos = ref([
  { id: 1, title: 'Props 복습', done: true },
  { id: 2, title: 'Emit 배우기', done: false }
])

function toggleTodo(id) {
  const todo = todos.value.find((item) => item.id === id)
  if (todo) {
    todo.done = !todo.done
  }
}

function removeTodo(id) {
  todos.value = todos.value.filter((todo) => todo.id !== id)
}
</script>

<template>
  <ul>
    <TodoItem
      v-for="todo in todos"
      :key="todo.id"
      :todo="todo"
      @toggle="toggleTodo"
      @remove="removeTodo"
    />
  </ul>
</template>
```

---

## 이벤트 이름

컴포넌트 이벤트 이름은 템플릿에서 kebab-case로 듣는 것이 일반적이다.

자식:

```js
emit('savePost')
```

부모:

```html
<PostForm @save-post="handleSave" />
```

초보 단계에서는 이벤트 이름을 처음부터 kebab-case로 통일해도 이해하기 쉽다.

```js
emit('save-post')
```

---

## Emit은 버블링되지 않는다

컴포넌트 이벤트는 DOM 이벤트처럼 여러 부모를 타고 자동으로 올라가지 않는다. 직접 부모만 들을 수 있다.

깊은 컴포넌트나 형제 컴포넌트 사이에서 데이터를 공유해야 한다면 나중에 배울 Provide/Inject나 Pinia 같은 상태 관리를 고려한다.

---

## 자주 하는 실수

### Props를 직접 수정함

```js
props.todo.done = true
```

단순 예제에서는 동작할 수 있지만, 데이터 흐름이 흐려진다. 변경 요청은 이벤트로 부모에게 올리는 습관이 좋다.

### defineEmits를 함수 안에서 호출함

`defineEmits`는 `<script setup>` 최상위에서 호출한다.

```js
const emit = defineEmits(['remove'])
```

### 부모에서 이벤트 이름을 다르게 씀

자식이 `emit('remove')`를 보냈다면 부모는 `@remove`로 들어야 한다.

---

## 작은 실습

`ProductCard.vue`에 삭제 버튼을 추가해보자.

- 부모는 상품 목록을 가진다.
- 자식은 `product` prop을 받는다.
- 자식의 삭제 버튼을 누르면 `remove` 이벤트와 상품 id를 emit한다.
- 부모는 해당 id의 상품을 삭제한다.

---

## 이번 장 요약

- Emit은 자식이 부모에게 사건을 알리는 방법이다.
- `defineEmits`로 발생시킬 이벤트를 선언한다.
- `emit('event-name', payload)`로 이벤트와 값을 전달한다.
- Props는 내려가고, 이벤트는 올라간다.

---

## 다음 장으로

다음 장에서는 사용자 입력 컴포넌트를 만들 때 자주 쓰는 컴포넌트 `v-model`을 배운다.
