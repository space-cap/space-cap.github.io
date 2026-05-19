# 컴포넌트 v-model

## 이번 장에서 배울 것

`v-model`은 input 같은 기본 폼 요소뿐 아니라 직접 만든 컴포넌트에도 사용할 수 있다.

예를 들어 아래처럼 사용할 수 있는 `BaseInput` 컴포넌트를 만들 수 있다.

```vue
<BaseInput v-model="username" />
```

---

## defineModel 기본

Vue 3.4 이후에는 `defineModel()`을 사용하면 컴포넌트 `v-model`을 간단하게 만들 수 있다.

`BaseInput.vue`

```vue
<script setup>
const model = defineModel()
</script>

<template>
  <input v-model="model">
</template>
```

부모:

```vue
<script setup>
import { ref } from 'vue'
import BaseInput from './components/BaseInput.vue'

const username = ref('')
</script>

<template>
  <BaseInput v-model="username" />
  <p>{{ username }}</p>
</template>
```

자식 입력 칸에 값을 입력하면 부모의 `username`도 함께 바뀐다.

---

## defineModel은 ref처럼 동작한다

`defineModel()`이 반환한 값은 ref처럼 사용할 수 있다.

```vue
<script setup>
const count = defineModel()

function increase() {
  count.value++
}
</script>

<template>
  <button @click="increase">{{ count }}</button>
</template>
```

JavaScript에서는 `.value`를 사용하고, 템플릿에서는 생략할 수 있다.

---

## 이름 있는 v-model

하나의 컴포넌트에서 여러 값을 양방향으로 연결할 수도 있다.

부모:

```vue
<UserNameInput
  v-model:first-name="firstName"
  v-model:last-name="lastName"
/>
```

자식 `UserNameInput.vue`:

```vue
<script setup>
const firstName = defineModel('firstName')
const lastName = defineModel('lastName')
</script>

<template>
  <input v-model="firstName" placeholder="이름">
  <input v-model="lastName" placeholder="성">
</template>
```

---

## defineModel 옵션

필수값이나 기본값을 지정할 수 있다.

```js
const model = defineModel({
  required: true
})
```

```js
const page = defineModel('page', {
  default: 1
})
```

단, 기본값을 자식에만 두고 부모가 값을 주지 않으면 부모와 자식 값이 어긋날 수 있다. 부모에서도 초기값을 명확히 두는 편이 좋다.

---

## 내부 동작 이해하기

컴포넌트 `v-model`은 내부적으로 prop과 event 조합이다.

```vue
<CustomInput v-model="searchText" />
```

대략 다음과 같은 의미다.

```vue
<CustomInput
  :model-value="searchText"
  @update:model-value="(value) => searchText = value"
/>
```

그래서 `defineModel()`을 쓰기 전에는 `modelValue` prop과 `update:modelValue` emit을 직접 작성했다.

```vue
<script setup>
defineProps({
  modelValue: String
})

const emit = defineEmits(['update:modelValue'])
</script>

<template>
  <input
    :value="modelValue"
    @input="emit('update:modelValue', $event.target.value)"
  >
</template>
```

새 프로젝트에서는 `defineModel()`을 먼저 사용하고, 기존 코드를 읽을 때 이 구조를 이해하면 된다.

---

## 언제 컴포넌트 v-model을 사용할까?

다음 같은 재사용 입력 컴포넌트를 만들 때 좋다.

- `BaseInput`
- `BaseTextarea`
- `SearchBox`
- `DatePicker`
- `QuantityStepper`
- `ToggleSwitch`

단순 버튼이나 카드처럼 값을 직접 편집하지 않는 컴포넌트에는 보통 Props와 Emit만으로 충분하다.

---

## 자주 하는 실수

### 모든 컴포넌트에 v-model을 쓰려고 함

`v-model`은 양방향 입력 컴포넌트에 적합하다. 단순 표시 컴포넌트는 props로 충분하다.

### 부모 초기값을 빼먹음

부모에서 연결할 ref를 명확히 만든다.

```js
const username = ref('')
```

### Vue 3.4 이전 코드와 혼동함

오래된 예제에서는 `modelValue`와 `update:modelValue`를 직접 쓴다. 최신 Vue에서는 `defineModel()`이 더 간결하다.

---

## 작은 실습

`SearchInput.vue`를 만들어보자.

- 부모에서 `<SearchInput v-model="keyword" />`로 사용한다.
- 자식은 내부 input에 `v-model`을 연결한다.
- 부모는 입력된 검색어를 화면에 표시한다.

---

## 이번 장 요약

- 직접 만든 컴포넌트에도 `v-model`을 사용할 수 있다.
- Vue 3.4 이후에는 `defineModel()`을 권장한다.
- `defineModel()` 값은 ref처럼 동작한다.
- 내부적으로는 prop과 `update:*` 이벤트 조합이다.

---

## 다음 장으로

다음 장에서는 컴포넌트에 전달된 일반 HTML 속성이 어떻게 처리되는지 배우는 Fallthrough Attributes를 다룬다.

