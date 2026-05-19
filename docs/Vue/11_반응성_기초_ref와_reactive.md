# 반응성 기초: ref와 reactive

## 이번 장에서 배울 것

반응성은 Vue의 핵심이다. 값이 바뀌면 그 값을 사용하는 화면도 자동으로 다시 그려진다.

이번 장에서는 Composition API에서 가장 자주 사용하는 `ref`와 `reactive`를 배운다.

---

## 반응성이란?

일반 JavaScript 변수는 값이 바뀌어도 화면이 자동으로 바뀌지 않는다.

```js
let count = 0
count++
```

Vue는 `ref`나 `reactive`로 만든 값을 추적한다. 그래서 값이 바뀌면 Vue가 필요한 화면을 갱신한다.

---

## ref

`ref`는 숫자, 문자열, boolean, 객체, 배열 등 거의 모든 값을 반응형으로 만들 수 있다.

```vue
<script setup>
import { ref } from 'vue'

const count = ref(0)

function increase() {
  count.value++
}
</script>

<template>
  <p>{{ count }}</p>
  <button @click="increase">증가</button>
</template>
```

JavaScript 코드에서는 `.value`를 사용한다.

```js
count.value++
```

템플릿에서는 `.value`를 쓰지 않는다.

```html
{{ count }}
```

---

## 왜 .value가 필요할까?

`ref(0)`은 숫자 `0` 자체를 반환하는 것이 아니라, 값을 감싼 객체를 반환한다.

```js
const count = ref(0)

console.log(count.value)
```

Vue는 이 감싼 객체를 통해 값의 변경을 추적한다. 템플릿에서는 Vue가 자동으로 풀어주기 때문에 `.value`를 생략할 수 있다.

---

## reactive

`reactive`는 객체를 반응형으로 만든다.

```vue
<script setup>
import { reactive } from 'vue'

const user = reactive({
  name: 'Kim',
  age: 20
})

function growOlder() {
  user.age++
}
</script>

<template>
  <p>{{ user.name }}: {{ user.age }}세</p>
  <button @click="growOlder">나이 증가</button>
</template>
```

`reactive`로 만든 객체는 `.value` 없이 속성에 접근한다.

```js
user.age++
```

---

## ref와 reactive 비교

| 구분 | ref | reactive |
| --- | --- | --- |
| 주 용도 | 단일 값 또는 어떤 값이든 | 객체 |
| 접근 방식 | JavaScript에서 `.value` 필요 | `.value` 없음 |
| 템플릿 | `.value` 생략 | 그대로 사용 |
| 초보자 추천 | 대부분의 경우 먼저 사용 | 객체 상태가 명확할 때 사용 |

초보자는 우선 `ref`에 익숙해지는 것이 좋다. Vue 공식 예제와 실무 코드에서도 `ref`를 널리 사용한다.

---

## 배열에 ref 사용하기

배열도 `ref`로 만들 수 있다.

```vue
<script setup>
import { ref } from 'vue'

const todos = ref([])

function addTodo() {
  todos.value.push({
    id: Date.now(),
    title: '새 할 일'
  })
}
</script>

<template>
  <button @click="addTodo">추가</button>
  <ul>
    <li v-for="todo in todos" :key="todo.id">
      {{ todo.title }}
    </li>
  </ul>
</template>
```

JavaScript에서는 `todos.value.push(...)`처럼 사용한다.

---

## reactive 사용 시 주의점

`reactive`로 만든 객체를 통째로 구조 분해하면 반응성이 끊길 수 있다.

```js
const user = reactive({
  name: 'Kim',
  age: 20
})

const { age } = user
```

이렇게 꺼낸 `age`는 원래 `user.age`와 같은 방식으로 추적되지 않는다. 초보자는 처음에는 객체 자체를 유지해서 사용하는 것이 안전하다.

```js
user.age++
```

---

## 자주 하는 실수

### JavaScript에서 .value를 빼먹음

```js
const count = ref(0)

// 잘못된 예
count++

// 올바른 예
count.value++
```

### 템플릿에서 .value를 붙임

```html
<!-- 굳이 이렇게 쓰지 않는다 -->
{{ count.value }}

<!-- 일반적으로 이렇게 쓴다 -->
{{ count }}
```

### reactive에 원시값을 넣으려 함

`reactive`는 객체에 사용한다. 숫자나 문자열 하나에는 `ref`를 사용한다.

```js
const count = ref(0)
```

---

## 작은 실습

아래 기능을 가진 프로필 편집 예제를 만들어보자.

- 이름 `name`은 `ref`로 만든다.
- 나이 `age`도 `ref`로 만든다.
- 버튼을 누르면 나이가 1 증가한다.
- 입력 칸에 이름을 입력하면 화면의 이름도 바뀐다.

힌트:

```vue
<script setup>
import { ref } from 'vue'

const name = ref('Kim')
const age = ref(20)
</script>

<template>
  <input v-model="name">
  <p>{{ name }} / {{ age }}세</p>
  <button @click="age++">나이 증가</button>
</template>
```

템플릿에서는 `age++`처럼 `.value` 없이 사용할 수 있다.

---

## 이번 장 요약

- 반응성은 값 변경에 따라 화면이 자동으로 갱신되는 성질이다.
- `ref`는 거의 모든 값을 반응형으로 만들 수 있다.
- `ref` 값은 JavaScript에서 `.value`로 접근한다.
- `reactive`는 객체를 반응형으로 만든다.
- 초보자는 `ref`를 중심으로 익히면 좋다.

---

## 다음 장으로

다음 장에서는 상태에서 새로운 값을 계산하는 `computed`를 배운다.

