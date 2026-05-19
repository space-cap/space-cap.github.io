# Fallthrough Attributes

## 이번 장에서 배울 것

Fallthrough Attributes는 컴포넌트에 전달했지만 props로 선언되지 않은 속성이 자식 컴포넌트의 루트 요소로 자동 전달되는 기능이다.

처음에는 이름이 어렵지만, 실제로는 자주 만나는 동작이다.

---

## 기본 예제

`BaseButton.vue`

```vue
<template>
  <button class="base-button">
    <slot>버튼</slot>
  </button>
</template>
```

부모:

```vue
<BaseButton id="save-button" class="primary" @click="save">
  저장
</BaseButton>
```

`id`, `class`, `click` 이벤트는 `BaseButton`의 props로 선언하지 않았지만 내부의 루트 요소인 `button`에 전달된다.

결과적으로 내부 버튼은 다음과 비슷하게 동작한다.

```html
<button id="save-button" class="base-button primary">저장</button>
```

---

## class와 style 병합

부모가 넘긴 `class`는 자식 루트 요소의 기존 class와 병합된다.

자식:

```vue
<template>
  <button class="base-button">
    <slot />
  </button>
</template>
```

부모:

```html
<BaseButton class="danger">삭제</BaseButton>
```

최종 버튼에는 `base-button`과 `danger`가 함께 적용된다.

---

## 이벤트도 전달된다

부모에서 `@click`을 넘기면 자식 루트 요소에 연결된다.

```html
<BaseButton @click="save">저장</BaseButton>
```

`BaseButton`의 루트가 실제 `<button>`이라면 자연스럽게 클릭 이벤트가 동작한다.

---

## 루트 요소가 여러 개인 경우

컴포넌트에 루트 요소가 하나라면 Vue가 속성을 자동으로 어디에 붙일지 알 수 있다.

```vue
<template>
  <button>저장</button>
</template>
```

하지만 루트 요소가 여러 개면 Vue가 어느 요소에 붙여야 할지 알 수 없다.

```vue
<template>
  <label>이름</label>
  <input>
</template>
```

이 경우 `$attrs`를 사용해 직접 지정할 수 있다.

```vue
<template>
  <label>이름</label>
  <input v-bind="$attrs">
</template>
```

부모가 넘긴 `id`, `class`, `placeholder` 같은 속성이 input에 붙는다.

---

## inheritAttrs 끄기

속성이 루트 요소로 자동 전달되는 것을 막고 싶다면 `inheritAttrs: false`를 사용할 수 있다.

`<script setup>`에서는 `defineOptions`를 사용할 수 있다.

```vue
<script setup>
defineOptions({
  inheritAttrs: false
})
</script>

<template>
  <div class="field">
    <input v-bind="$attrs">
  </div>
</template>
```

이렇게 하면 속성을 원하는 요소에 직접 전달할 수 있다.

---

## 입력 컴포넌트 예제

`BaseInput.vue`

```vue
<script setup>
defineOptions({
  inheritAttrs: false
})

const model = defineModel()
</script>

<template>
  <label class="field">
    <span>입력</span>
    <input v-model="model" v-bind="$attrs">
  </label>
</template>
```

부모:

```html
<BaseInput
  v-model="name"
  placeholder="이름을 입력하세요"
  autocomplete="name"
/>
```

`placeholder`, `autocomplete`은 내부 input으로 전달된다.

---

## Props와 Fallthrough Attributes 차이

| 구분 | Props | Fallthrough Attributes |
| --- | --- | --- |
| 선언 여부 | `defineProps`로 선언 | 선언하지 않음 |
| 목적 | 컴포넌트 데이터 계약 | 일반 HTML 속성 전달 |
| 예 | `title`, `user`, `items` | `id`, `class`, `style`, `placeholder` |

컴포넌트의 핵심 데이터는 Props로 받고, 일반 HTML 속성은 fallthrough로 넘기는 경우가 많다.

---

## 자주 하는 실수

### 여러 루트 요소에서 속성이 어디로 가는지 기대함

루트 요소가 여러 개면 자동 전달이 애매하다. `$attrs`로 명시하자.

### props로 받아야 할 값을 attrs에 의존함

컴포넌트 동작에 꼭 필요한 값은 `defineProps`로 명확히 선언한다.

### input 래퍼 컴포넌트에서 placeholder가 바깥 div에 붙음

`inheritAttrs: false`와 `v-bind="$attrs"`로 실제 input에 전달한다.

---

## 작은 실습

`BaseInput.vue`를 만들어보자.

- `v-model`로 값 연결
- 부모가 넘긴 `placeholder`, `disabled`, `class`를 실제 input에 전달
- label을 감싸는 구조 사용

---

## 이번 장 요약

- Fallthrough Attributes는 props로 선언하지 않은 속성이 루트 요소로 전달되는 기능이다.
- `class`와 `style`은 기존 값과 병합된다.
- 루트 요소가 여러 개면 `$attrs`로 전달 위치를 직접 지정한다.
- `inheritAttrs: false`로 자동 상속을 끌 수 있다.

---

## 다음 장으로

다음 장에서는 컴포넌트 안쪽 내용을 부모가 채워 넣는 Slot을 배운다.

