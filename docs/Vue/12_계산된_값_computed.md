# 계산된 값 computed

## 이번 장에서 배울 것

`computed`는 기존 상태를 바탕으로 새로운 값을 계산할 때 사용한다. 예를 들어 Todo 목록에서 완료된 개수, 필터링된 목록, 총 가격 같은 값을 만들 때 유용하다.

---

## 왜 computed가 필요할까?

아래처럼 템플릿에 계산식을 직접 넣을 수도 있다.

```html
<p>완료된 개수: {{ todos.filter((todo) => todo.done).length }}</p>
```

하지만 템플릿이 길어지고 읽기 어려워진다. 이럴 때 `computed`로 계산 로직을 분리한다.

---

## 기본 사용법

```vue
<script setup>
import { computed, ref } from 'vue'

const firstName = ref('Gildong')
const lastName = ref('Hong')

const fullName = computed(() => {
  return `${lastName.value} ${firstName.value}`
})
</script>

<template>
  <p>{{ fullName }}</p>
</template>
```

`fullName`은 `firstName`이나 `lastName`이 바뀔 때 자동으로 다시 계산된다.

---

## Todo 완료 개수 계산하기

```vue
<script setup>
import { computed, ref } from 'vue'

const todos = ref([
  { id: 1, title: 'Vue 공부', done: true },
  { id: 2, title: 'computed 배우기', done: false },
  { id: 3, title: '실습하기', done: false }
])

const doneCount = computed(() => {
  return todos.value.filter((todo) => todo.done).length
})
</script>

<template>
  <p>완료: {{ doneCount }} / 전체: {{ todos.length }}</p>
</template>
```

템플릿에서는 `doneCount.value`가 아니라 `doneCount`로 사용한다.

---

## computed와 함수의 차이

함수로도 같은 결과를 만들 수 있다.

```js
function getDoneCount() {
  return todos.value.filter((todo) => todo.done).length
}
```

템플릿:

```html
<p>{{ getDoneCount() }}</p>
```

`computed`와 함수의 가장 큰 차이는 캐싱이다. `computed`는 의존하는 값이 바뀌지 않으면 이전 계산 결과를 재사용한다. 반면 함수는 렌더링될 때마다 다시 실행될 수 있다.

복잡한 계산이거나 여러 곳에서 재사용하는 값은 `computed`로 만드는 것이 좋다.

---

## 필터링된 목록 만들기

검색어로 목록을 걸러보자.

```vue
<script setup>
import { computed, ref } from 'vue'

const keyword = ref('')
const products = ref([
  { id: 1, name: '키보드' },
  { id: 2, name: '마우스' },
  { id: 3, name: '모니터' }
])

const filteredProducts = computed(() => {
  return products.value.filter((product) =>
    product.name.includes(keyword.value)
  )
})
</script>

<template>
  <input v-model="keyword" placeholder="검색어">

  <ul>
    <li v-for="product in filteredProducts" :key="product.id">
      {{ product.name }}
    </li>
  </ul>
</template>
```

`keyword`가 바뀌면 `filteredProducts`가 다시 계산되고 화면 목록도 바뀐다.

---

## writable computed

대부분의 `computed`는 읽기 전용으로 사용한다. 필요하면 getter와 setter를 가진 계산 값도 만들 수 있다.

```js
const firstName = ref('Gildong')
const lastName = ref('Hong')

const fullName = computed({
  get() {
    return `${lastName.value} ${firstName.value}`
  },
  set(value) {
    const parts = value.split(' ')
    lastName.value = parts[0]
    firstName.value = parts[1]
  }
})
```

초보 단계에서는 읽기 전용 `computed(() => ...)`만 잘 써도 충분하다.

---

## 자주 하는 실수

### computed 안에서 값을 직접 변경함

`computed`는 기존 상태에서 새 값을 계산하는 용도다. 내부에서 상태를 바꾸는 코드는 피한다.

```js
// 피해야 하는 예
const badCount = computed(() => {
  count.value++
  return count.value
})
```

상태 변경은 이벤트 함수에서 처리하고, `computed`는 계산만 하도록 두자.

### JavaScript에서 computed 값을 .value 없이 읽음

```js
console.log(doneCount.value)
```

템플릿에서는 `.value`를 생략하지만 JavaScript에서는 필요하다.

### 모든 함수를 computed로 만들려고 함

클릭했을 때 실행되는 동작은 함수로 만든다. 기존 상태에서 파생되는 값은 `computed`로 만든다.

---

## 작은 실습

Todo 목록에 다음 계산 값을 추가해보자.

- 전체 Todo 개수
- 완료된 Todo 개수
- 남은 Todo 개수
- 완료율

힌트:

```js
const totalCount = computed(() => todos.value.length)
const doneCount = computed(() => todos.value.filter((todo) => todo.done).length)
const remainingCount = computed(() => totalCount.value - doneCount.value)
```

---

## 이번 장 요약

- `computed`는 상태에서 파생된 값을 만들 때 사용한다.
- 의존하는 값이 바뀌면 자동으로 다시 계산된다.
- 템플릿을 복잡하게 만들지 않고 계산 로직을 분리할 수 있다.
- `computed`는 가능한 한 부작용 없이 계산만 해야 한다.

---

## 다음 장으로

다음 장에서는 상태에 따라 class와 style을 동적으로 바꾸는 방법을 배운다.

