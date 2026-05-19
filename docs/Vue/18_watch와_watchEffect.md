# watch와 watchEffect

## 이번 장에서 배울 것

`watch`와 `watchEffect`는 상태 변화를 감시하고, 값이 바뀌었을 때 특정 작업을 실행하는 기능이다. API 호출, localStorage 저장, 검색어 변경 감지 같은 곳에서 사용한다.

---

## computed와 watch의 차이

`computed`는 값을 계산할 때 사용한다.

```js
const fullName = computed(() => `${lastName.value} ${firstName.value}`)
```

`watch`는 값이 바뀌었을 때 어떤 일을 실행할 때 사용한다.

```js
watch(keyword, () => {
  search()
})
```

간단히 말하면 다음과 같다.

| 기능 | 목적 |
| --- | --- |
| `computed` | 새 값을 계산한다. |
| `watch` | 값 변경에 반응해 작업을 실행한다. |

---

## watch 기본

```vue
<script setup>
import { ref, watch } from 'vue'

const keyword = ref('')

watch(keyword, (newValue, oldValue) => {
  console.log('이전 값:', oldValue)
  console.log('새 값:', newValue)
})
</script>

<template>
  <input v-model="keyword" placeholder="검색어">
</template>
```

`keyword`가 바뀔 때마다 콜백 함수가 실행된다.

---

## 여러 값 감시하기

배열로 여러 값을 감시할 수 있다.

```js
const firstName = ref('')
const lastName = ref('')

watch([firstName, lastName], ([newFirstName, newLastName]) => {
  console.log(newFirstName, newLastName)
})
```

---

## 객체 속성 감시하기

`reactive` 객체의 특정 속성을 감시하려면 함수로 감싼다.

```js
const user = reactive({
  name: 'Kim',
  age: 20
})

watch(
  () => user.age,
  (newAge) => {
    console.log('나이 변경:', newAge)
  }
)
```

---

## immediate 옵션

기본적으로 `watch`는 값이 바뀔 때 실행된다. 처음에도 바로 실행하고 싶다면 `immediate`를 사용한다.

```js
watch(
  keyword,
  (newKeyword) => {
    console.log('검색:', newKeyword)
  },
  { immediate: true }
)
```

API 데이터를 처음 불러오고, 이후 조건이 바뀔 때 다시 불러오는 패턴에서 자주 사용한다.

---

## deep 옵션

객체 내부의 깊은 변경까지 감시하려면 `deep` 옵션을 사용할 수 있다.

```js
watch(
  user,
  (newUser) => {
    console.log('사용자 변경:', newUser)
  },
  { deep: true }
)
```

다만 깊은 감시는 비용이 커질 수 있다. 가능하면 필요한 속성만 감시하는 편이 좋다.

---

## watchEffect

`watchEffect`는 내부에서 사용한 반응형 값을 자동으로 추적한다.

```vue
<script setup>
import { ref, watchEffect } from 'vue'

const keyword = ref('')

watchEffect(() => {
  console.log('현재 검색어:', keyword.value)
})
</script>

<template>
  <input v-model="keyword">
</template>
```

콜백 안에서 `keyword.value`를 사용했기 때문에 Vue가 자동으로 의존성을 추적한다.

---

## watch와 watchEffect 선택하기

| 상황 | 추천 |
| --- | --- |
| 특정 값 하나를 명확히 감시 | `watch` |
| 이전 값과 새 값이 필요 | `watch` |
| 여러 반응형 값을 자동 추적 | `watchEffect` |
| 실행 조건을 명확히 통제 | `watch` |

초보자는 먼저 `watch`에 익숙해지는 것이 좋다. 감시 대상이 명확해서 코드의 의도를 파악하기 쉽다.

---

## localStorage 저장 예제

```vue
<script setup>
import { ref, watch } from 'vue'

const memo = ref(localStorage.getItem('memo') || '')

watch(memo, (newMemo) => {
  localStorage.setItem('memo', newMemo)
})
</script>

<template>
  <textarea v-model="memo" placeholder="메모"></textarea>
</template>
```

`memo`가 바뀔 때마다 localStorage에 저장된다.

---

## 자주 하는 실수

### 계산할 값을 watch로 만듦

화면에 보여줄 파생 값은 `computed`가 더 적합하다.

### deep을 무조건 사용함

`deep: true`는 편하지만 객체가 커질수록 부담이 될 수 있다. 필요한 속성만 감시하는 방식을 먼저 고려한다.

### watch 안에서 감시 중인 값을 계속 바꿈

무한 반복이 생길 수 있다.

```js
watch(count, () => {
  count.value++
})
```

이런 코드는 조심해야 한다.

---

## 작은 실습

검색어 입력 예제를 만들어보자.

- `keyword`를 `ref`로 만든다.
- `watch`로 `keyword` 변경을 감시한다.
- 검색어가 바뀌면 콘솔에 `"검색어 변경: 값"`을 출력한다.
- 빈 문자열이면 아무것도 하지 않는다.

---

## 이번 장 요약

- `watch`는 특정 값의 변화를 감시한다.
- `watch` 콜백에서는 새 값과 이전 값을 받을 수 있다.
- `watchEffect`는 콜백 안에서 사용한 반응형 값을 자동 추적한다.
- 계산된 값은 `computed`, 변화에 따른 작업은 `watch`를 사용한다.

---

## 다음 장으로

다음 장에서는 Vue에서 DOM 요소나 컴포넌트 인스턴스에 접근하는 템플릿 참조를 배운다.

