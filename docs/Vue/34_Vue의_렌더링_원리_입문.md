# Vue의 렌더링 원리 입문

## 이번 장에서 배울 것

이번 장에서는 Vue가 데이터를 화면으로 바꾸고, 상태가 바뀌었을 때 어떻게 필요한 부분을 갱신하는지 큰 그림을 배운다.

깊은 내부 구현을 외우는 것이 목표는 아니다. Vue 코드를 작성할 때 왜 `key`가 필요한지, 왜 반응형 상태를 바꿔야 화면이 갱신되는지 이해하는 것이 목표다.

---

## 렌더링이란?

렌더링은 데이터를 화면에 그리는 과정이다.

Vue에서는 템플릿과 상태를 바탕으로 화면을 만든다.

```vue
<script setup>
import { ref } from 'vue'

const message = ref('안녕하세요')
</script>

<template>
  <p>{{ message }}</p>
</template>
```

Vue는 `message` 값을 읽고 `<p>안녕하세요</p>` 형태의 화면을 만든다.

---

## 상태가 바뀌면 화면이 갱신된다

```vue
<script setup>
import { ref } from 'vue'

const count = ref(0)
</script>

<template>
  <button @click="count++">
    {{ count }}
  </button>
</template>
```

버튼을 누르면 `count`가 바뀐다. Vue는 `count`를 사용하는 화면 부분을 다시 계산하고 브라우저 화면을 갱신한다.

개발자는 `document.querySelector`로 직접 텍스트를 바꾸지 않아도 된다.

---

## 템플릿은 렌더 함수로 컴파일된다

Vue 템플릿은 브라우저가 그대로 실행하는 것이 아니다. Vue는 템플릿을 JavaScript 렌더 함수로 변환한다.

```html
<p>{{ message }}</p>
```

개념적으로는 다음처럼 바뀐다고 볼 수 있다.

```js
render() {
  return createElement('p', message.value)
}
```

실제 코드는 더 복잡하지만, 중요한 것은 템플릿이 JavaScript로 변환되어 실행된다는 점이다.

---

## Virtual DOM

Vue는 화면 구조를 JavaScript 객체 형태로 표현한다. 이것을 Virtual DOM이라고 부른다.

Virtual DOM은 실제 DOM을 바로 조작하기 전에 "화면이 이렇게 생겨야 한다"는 가벼운 설계도처럼 생각할 수 있다.

상태가 바뀌면 Vue는 새로운 Virtual DOM을 만들고 이전 Virtual DOM과 비교한다. 그리고 실제 DOM에서 필요한 부분만 갱신한다.

---

## 패치 과정

상태 변경 후 화면 갱신은 대략 다음 흐름이다.

```text
상태 변경
  ↓
렌더 함수 다시 실행
  ↓
새 Virtual DOM 생성
  ↓
이전 Virtual DOM과 비교
  ↓
필요한 DOM만 수정
```

이 과정을 패치라고 부른다.

---

## key가 중요한 이유

리스트 렌더링에서 `key`를 쓰는 이유도 렌더링 원리와 관련 있다.

```html
<li v-for="todo in todos" :key="todo.id">
  {{ todo.title }}
</li>
```

`key`는 Vue가 각 항목을 안정적으로 구분할 수 있게 돕는다.

항목이 추가, 삭제, 정렬될 때 `key`가 없거나 부정확하면 Vue가 어떤 DOM을 재사용해야 할지 판단하기 어려워질 수 있다.

그래서 `v-for`에서는 가능한 한 고유한 id를 `key`로 사용한다.

---

## 반응형 의존성 추적

Vue는 렌더링 중에 어떤 반응형 값이 사용되었는지 추적한다.

```vue
<template>
  <p>{{ count }}</p>
</template>
```

이 템플릿은 `count`를 사용한다. Vue는 이 사실을 알고 있다가 `count`가 바뀌면 해당 컴포넌트를 다시 렌더링한다.

반대로 렌더링에 사용되지 않는 값이 바뀌어도 화면에는 영향이 없다.

---

## 업데이트는 비동기로 모인다

Vue는 상태가 여러 번 바뀔 때마다 즉시 DOM을 하나씩 바꾸지 않는다. 여러 변경을 모아서 효율적으로 처리한다.

```js
count.value++
count.value++
count.value++
```

이렇게 연속으로 바뀌어도 Vue는 업데이트를 모아 처리한다. 그래서 상태를 바꾼 직후 실제 DOM이 아직 갱신되지 않았을 수 있다.

DOM 갱신 이후가 필요할 때는 `nextTick`을 사용할 수 있다.

```js
import { nextTick } from 'vue'

async function updateAndFocus() {
  isEditing.value = true
  await nextTick()
  inputRef.value.focus()
}
```

---

## 렌더링 성능을 위한 습관

초보 단계에서 복잡한 최적화는 필요 없다. 대신 다음 습관을 지키면 좋다.

- `v-for`에는 안정적인 `key`를 사용한다.
- 템플릿에 복잡한 계산을 직접 넣지 않고 `computed`를 사용한다.
- 화면에 필요 없는 큰 데이터를 반응형으로 만들지 않는다.
- 컴포넌트를 적절히 나누어 변경 범위를 작게 유지한다.
- DOM 직접 조작보다 상태 변경으로 화면을 표현한다.

---

## 자주 하는 오해

### Vue가 모든 DOM을 매번 새로 만든다?

아니다. Vue는 이전 결과와 새 결과를 비교하고 필요한 부분만 수정한다.

### ref 값만 바꾸면 언제나 화면이 바뀐다?

그 값이 화면이나 computed/watch 등에서 사용되고 있어야 의미가 있다.

### DOM을 직접 바꾸면 Vue가 알아서 상태도 바꾼다?

아니다. Vue의 기준은 반응형 상태다. DOM을 직접 바꾸면 상태와 화면이 어긋날 수 있다.

---

## 작은 실습

다음 실험을 해보자.

1. Todo 목록을 `v-for`로 출력한다.
2. `:key="todo.id"`를 넣고 항목을 삭제해본다.
3. `:key`를 제거하거나 index로 바꿔보고 차이를 관찰한다.
4. 입력 요소가 포함된 목록에서 index key가 왜 위험할 수 있는지 생각해본다.

---

## 이번 장 요약

- 렌더링은 상태와 템플릿을 화면으로 바꾸는 과정이다.
- Vue 템플릿은 렌더 함수로 컴파일된다.
- Vue는 Virtual DOM을 사용해 변경 전후를 비교하고 필요한 DOM만 수정한다.
- 반응형 값이 바뀌면 그 값을 사용하는 컴포넌트가 업데이트된다.
- `v-for`의 안정적인 `key`는 올바른 렌더링에 중요하다.

---

## 다음 장으로

다음 장부터는 Vue의 내장 컴포넌트를 배운다. 먼저 요소가 나타나고 사라질 때 애니메이션을 적용하는 `Transition`을 살펴본다.

