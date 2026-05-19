# Props와 Emit에 타입 입히기

## 이번 장에서 배울 것

TypeScript를 Vue에서 사용할 때 가장 먼저 효과를 보는 곳은 Props와 Emit이다. 부모와 자식 컴포넌트 사이의 데이터 계약을 타입으로 명확히 만들 수 있기 때문이다.

이번 장에서는 `defineProps`, `defineEmits`, `withDefaults`를 사용해 컴포넌트 입력과 이벤트에 타입을 적용한다.

---

## Props 타입 선언 기본

```vue
<script setup lang="ts">
const props = defineProps<{
  title: string
  count: number
  active: boolean
}>()
</script>

<template>
  <article>
    <h2>{{ props.title }}</h2>
    <p>{{ props.count }}</p>
  </article>
</template>
```

부모가 잘못된 타입을 전달하면 IDE나 타입 검사에서 오류를 확인할 수 있다.

---

## 선택 Props

필수가 아닌 Props는 `?`를 붙인다.

```ts
const props = defineProps<{
  title: string
  description?: string
}>()
```

`description`은 `string | undefined`로 취급된다.

템플릿에서는 조건부로 표시한다.

```html
<p v-if="props.description">{{ props.description }}</p>
```

---

## type 분리하기

Props 타입이 길어지면 별도 타입으로 분리한다.

```vue
<script setup lang="ts">
type UserCardProps = {
  name: string
  email: string
  age?: number
}

const props = defineProps<UserCardProps>()
</script>
```

여러 파일에서 재사용해야 한다면 `src/types` 폴더에 둘 수도 있다.

---

## 기본값 지정하기

타입 기반 props 선언에서 기본값을 지정하려면 `withDefaults`를 사용할 수 있다.

```vue
<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    size?: 'small' | 'medium' | 'large'
    disabled?: boolean
  }>(),
  {
    size: 'medium',
    disabled: false
  }
)
</script>
```

`size`는 세 값 중 하나만 받을 수 있다.

---

## 배열과 객체 Props

```vue
<script setup lang="ts">
type Todo = {
  id: number
  title: string
  done: boolean
}

defineProps<{
  todos: Todo[]
  selectedTodo?: Todo
}>()
</script>
```

객체 구조가 명확해지면 컴포넌트 내부에서 자동 완성이 좋아진다.

---

## Emit 타입 선언 기본

`defineEmits`에도 타입을 적용할 수 있다.

```vue
<script setup lang="ts">
const emit = defineEmits<{
  remove: [id: number]
  toggle: [id: number, done: boolean]
}>()

function removeTodo(id: number) {
  emit('remove', id)
}
</script>
```

`remove` 이벤트는 number id 하나를 payload로 받는다. 잘못된 타입으로 emit하면 타입 오류가 난다.

---

## 이벤트 payload가 없는 경우

```ts
const emit = defineEmits<{
  close: []
  submit: [value: string]
}>()
```

사용:

```ts
emit('close')
emit('submit', 'hello')
```

---

## TodoItem 예제

```vue
<script setup lang="ts">
type Todo = {
  id: number
  title: string
  done: boolean
}

const props = defineProps<{
  todo: Todo
}>()

const emit = defineEmits<{
  toggle: [id: number]
  remove: [id: number]
}>()
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

---

## defineModel 타입

컴포넌트 `v-model`에도 타입을 줄 수 있다.

```vue
<script setup lang="ts">
const model = defineModel<string>({
  required: true
})
</script>

<template>
  <input v-model="model">
</template>
```

이제 부모에서 연결하는 값도 string이어야 한다.

---

## 런타임 선언과 타입 선언

Props는 객체 문법으로 런타임 선언을 할 수도 있고, 제네릭으로 타입 선언을 할 수도 있다.

런타임 선언:

```ts
defineProps({
  title: {
    type: String,
    required: true
  }
})
```

타입 선언:

```ts
defineProps<{
  title: string
}>()
```

TypeScript 프로젝트에서는 타입 선언 방식이 간결하다. 다만 런타임 검증이 필요한 상황에서는 객체 문법도 이해해두면 좋다.

---

## 자주 하는 실수

### 선택 Props를 항상 있다고 가정함

`description?: string`은 없을 수 있다. 사용 전 조건을 확인한다.

### Emit payload 타입을 빼먹음

이벤트 이름뿐 아니라 전달 값까지 타입으로 잡으면 실수가 줄어든다.

### 부모와 자식 타입을 따로따로 관리함

공통 타입은 `src/types`에 분리해 함께 쓰는 것이 좋다.

---

## 작은 실습

`ProductCard.vue`를 TypeScript로 바꿔보자.

- `Product` 타입 작성
- `product` prop 타입 지정
- `add-cart` 이벤트에 product id 전달
- `soldOut`이 true이면 버튼 비활성화

---

## 이번 장 요약

- `defineProps<T>()`로 Props 타입을 지정할 수 있다.
- 선택 Props는 `?`를 사용한다.
- 기본값은 `withDefaults`로 줄 수 있다.
- `defineEmits<T>()`로 이벤트 이름과 payload 타입을 지정한다.
- `defineModel<T>()`로 컴포넌트 v-model 타입을 지정할 수 있다.

---

## 다음 장으로

다음 장에서는 Composable에 타입을 적용하는 방법을 배운다.

