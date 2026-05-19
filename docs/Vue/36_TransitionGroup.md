# TransitionGroup

## 이번 장에서 배울 것

`TransitionGroup`은 `v-for`로 렌더링하는 목록에 전환 효과를 적용하는 Vue 내장 컴포넌트다.

항목이 추가되거나 삭제되거나 위치가 바뀔 때 자연스럽게 움직이는 UI를 만들 수 있다.

---

## Transition과 TransitionGroup 차이

| 구분 | Transition | TransitionGroup |
| --- | --- | --- |
| 대상 | 단일 요소 또는 컴포넌트 | 목록의 여러 요소 |
| 주 사용처 | 모달, 알림, 드롭다운 | Todo 목록, 카드 목록 |
| key 필요 | 상황에 따라 필요 | 각 항목에 반드시 필요 |
| 기본 렌더링 | 실제 요소를 만들지 않음 | 기본적으로 wrapper를 만들지 않음 |

목록 애니메이션은 `TransitionGroup`을 사용한다고 기억하자.

---

## 기본 예제

```vue
<script setup>
import { ref } from 'vue'

const nextId = ref(3)
const items = ref([
  { id: 1, text: 'Vue' },
  { id: 2, text: 'Vite' }
])

function addItem() {
  items.value.push({
    id: nextId.value++,
    text: '새 항목'
  })
}

function removeItem(id) {
  items.value = items.value.filter((item) => item.id !== id)
}
</script>

<template>
  <button @click="addItem">추가</button>

  <TransitionGroup name="list" tag="ul">
    <li v-for="item in items" :key="item.id">
      {{ item.text }}
      <button @click="removeItem(item.id)">삭제</button>
    </li>
  </TransitionGroup>
</template>

<style scoped>
.list-enter-active,
.list-leave-active {
  transition: all 0.25s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(20px);
}
</style>
```

`tag="ul"`을 지정하면 `TransitionGroup`이 `ul` 요소로 렌더링된다.

---

## key는 필수

`TransitionGroup` 안의 각 항목에는 반드시 고유한 `key`가 필요하다.

```html
<li v-for="item in items" :key="item.id">
  {{ item.text }}
</li>
```

index를 key로 쓰면 항목이 추가, 삭제, 정렬될 때 애니메이션과 DOM 재사용이 어긋날 수 있다. 가능한 한 고유 id를 사용한다.

---

## 이동 애니메이션

항목의 순서가 바뀔 때도 애니메이션을 줄 수 있다. 이때 move class가 사용된다.

```css
.list-move {
  transition: transform 0.25s ease;
}
```

예를 들어 목록을 섞거나 정렬할 때 항목이 부드럽게 이동한다.

---

## 위치 문제와 position absolute

삭제되는 항목이 공간을 차지하는 동안 이동 애니메이션이 어색할 수 있다. 이럴 때 leave 상태에 `position: absolute`를 주기도 한다.

```css
.list-leave-active {
  position: absolute;
}
```

다만 레이아웃에 따라 영향이 있을 수 있으므로 실제 화면에서 확인하면서 적용한다.

---

## Todo 목록 예제

```vue
<TransitionGroup name="todo" tag="ul" class="todo-list">
  <li v-for="todo in todos" :key="todo.id" class="todo-item">
    <span>{{ todo.title }}</span>
    <button @click="removeTodo(todo.id)">삭제</button>
  </li>
</TransitionGroup>
```

```css
.todo-enter-active,
.todo-leave-active,
.todo-move {
  transition: all 0.2s ease;
}

.todo-enter-from,
.todo-leave-to {
  opacity: 0;
  transform: translateY(8px);
}
```

---

## 자주 하는 실수

### key를 빼먹음

`TransitionGroup`은 목록 항목 구분이 중요하므로 고유한 `key`가 필요하다.

### index를 key로 사용함

삭제와 정렬이 있는 목록에서는 index key를 피한다.

### tag를 지정하지 않아 구조가 예상과 다름

목록이면 `tag="ul"`처럼 명시하면 HTML 구조가 더 분명하다.

---

## 작은 실습

Todo 목록에 애니메이션을 추가해보자.

- `TransitionGroup` 사용
- `tag="ul"` 지정
- 항목 추가 시 아래에서 나타나기
- 항목 삭제 시 흐려지며 사라지기
- 각 항목의 `key`는 `todo.id` 사용

---

## 이번 장 요약

- `TransitionGroup`은 목록 전환에 사용하는 내장 컴포넌트다.
- `v-for` 항목에는 고유한 `key`가 필요하다.
- `tag`로 wrapper 요소를 지정할 수 있다.
- `*-move` class로 이동 애니메이션을 적용할 수 있다.

---

## 다음 장으로

다음 장에서는 동적 컴포넌트의 상태를 보존하는 `KeepAlive`를 배운다.

