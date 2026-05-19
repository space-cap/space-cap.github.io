# Props로 데이터 전달하기

## 이번 장에서 배울 것

Props는 부모 컴포넌트가 자식 컴포넌트에 데이터를 전달하는 방법이다.

예를 들어 `App.vue`가 `UserCard.vue`에게 사용자 이름과 이메일을 전달할 수 있다.

---

## Props 기본 흐름

부모 컴포넌트:

```vue
<script setup>
import UserCard from './components/UserCard.vue'
</script>

<template>
  <UserCard name="Kim" email="kim@example.com" />
</template>
```

자식 컴포넌트 `UserCard.vue`:

```vue
<script setup>
defineProps(['name', 'email'])
</script>

<template>
  <article>
    <h2>{{ name }}</h2>
    <p>{{ email }}</p>
  </article>
</template>
```

자식은 `defineProps`로 받을 props를 선언한다.

---

## 동적 Props

문자열 고정값이 아니라 JavaScript 상태를 전달하려면 `:`를 사용한다.

```vue
<script setup>
import { ref } from 'vue'
import UserCard from './components/UserCard.vue'

const user = ref({
  name: 'Lee',
  email: 'lee@example.com'
})
</script>

<template>
  <UserCard :name="user.name" :email="user.email" />
</template>
```

`name="user.name"`은 문자열 `"user.name"`을 전달한다. 상태 값을 전달하려면 `:name="user.name"`처럼 써야 한다.

---

## 타입 선언

배열보다 객체 문법을 사용하면 props 타입을 표시할 수 있다.

```vue
<script setup>
defineProps({
  name: String,
  age: Number,
  active: Boolean
})
</script>
```

타입이 맞지 않으면 개발 중 브라우저 콘솔에 경고가 나올 수 있다.

---

## 필수값과 기본값

props에 더 자세한 옵션을 줄 수 있다.

```vue
<script setup>
defineProps({
  title: {
    type: String,
    required: true
  },
  likes: {
    type: Number,
    default: 0
  }
})
</script>
```

| 옵션 | 의미 |
| --- | --- |
| `type` | 기대하는 값의 타입 |
| `required` | 반드시 전달해야 하는지 |
| `default` | 전달되지 않았을 때 기본값 |

---

## Props는 읽기 전용

자식 컴포넌트에서 props를 직접 바꾸면 안 된다.

```vue
<script setup>
const props = defineProps({
  count: Number
})

// 피해야 하는 코드
props.count++
</script>
```

Props는 부모가 내려주는 값이다. 자식이 바꾸고 싶다면 부모에게 이벤트로 알려야 한다. 이 흐름은 다음 장 Emit에서 배운다.

---

## 객체 Props

객체를 통째로 전달할 수도 있다.

부모:

```vue
<UserCard :user="user" />
```

자식:

```vue
<script setup>
defineProps({
  user: {
    type: Object,
    required: true
  }
})
</script>

<template>
  <h2>{{ user.name }}</h2>
  <p>{{ user.email }}</p>
</template>
```

객체 전체를 넘기면 편하지만, 컴포넌트가 어떤 값을 필요로 하는지 덜 명확해질 수 있다. 작은 컴포넌트에서는 필요한 값만 props로 받는 방식도 좋다.

---

## v-for와 Props

목록을 컴포넌트로 분리할 때 props를 자주 사용한다.

`App.vue`

```vue
<script setup>
import TodoItem from './components/TodoItem.vue'

const todos = [
  { id: 1, title: 'Props 배우기', done: true },
  { id: 2, title: 'Emit 배우기', done: false }
]
</script>

<template>
  <TodoItem
    v-for="todo in todos"
    :key="todo.id"
    :todo="todo"
  />
</template>
```

`TodoItem.vue`

```vue
<script setup>
defineProps({
  todo: {
    type: Object,
    required: true
  }
})
</script>

<template>
  <li>
    {{ todo.title }}
  </li>
</template>
```

---

## 자주 하는 실수

### 동적 값을 문자열로 전달함

```html
<!-- 문자열 "age" 전달 -->
<UserCard age="age" />

<!-- 변수 age의 값 전달 -->
<UserCard :age="age" />
```

### 자식에서 props를 직접 수정함

자식이 부모 데이터를 바꿔야 한다면 직접 수정하지 말고 이벤트를 발생시킨다.

### Boolean props를 헷갈림

```html
<UserCard active />
```

이렇게 쓰면 `active`는 참으로 전달된다.

---

## 작은 실습

`ProductCard.vue`를 만들어보자.

받을 props:

- `name`: 상품명
- `price`: 가격
- `soldOut`: 품절 여부

부모 컴포넌트에서 상품 배열을 만들고 `v-for`로 `ProductCard`를 여러 개 렌더링해보자.

---

## 이번 장 요약

- Props는 부모에서 자식으로 데이터를 전달하는 방법이다.
- 자식은 `defineProps`로 받을 값을 선언한다.
- 동적 값은 `:`를 붙여 전달한다.
- Props는 자식에서 직접 수정하지 않는다.
- 자식이 변경을 요청하려면 Emit을 사용한다.

---

## 다음 장으로

다음 장에서는 자식 컴포넌트가 부모 컴포넌트에 이벤트를 올리는 Emit을 배운다.

