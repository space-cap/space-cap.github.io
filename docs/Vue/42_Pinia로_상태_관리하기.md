# Pinia로 상태 관리하기

## 이번 장에서 배울 것

Pinia는 Vue의 공식 상태 관리 라이브러리다. 여러 컴포넌트가 함께 사용하는 상태를 한곳에 모아 관리할 때 사용한다.

예를 들어 로그인 사용자, 장바구니, 테마 설정, 알림 목록 같은 상태는 여러 화면에서 필요할 수 있다.

---

## 왜 상태 관리가 필요할까?

가까운 부모-자식 관계에서는 Props와 Emit으로 충분하다.

하지만 아래처럼 멀리 떨어진 컴포넌트들이 같은 상태를 사용하면 전달이 복잡해진다.

```text
AppHeader -> 로그인 사용자 이름
Sidebar -> 권한별 메뉴
CartButton -> 장바구니 개수
CheckoutPage -> 장바구니 목록
```

Pinia를 사용하면 공통 상태를 store에 두고 필요한 컴포넌트에서 가져다 쓸 수 있다.

---

## 설치

프로젝트 생성 시 Pinia 옵션을 선택하지 않았다면 직접 설치한다.

```bash
npm install pinia
```

---

## 앱에 Pinia 등록하기

`src/main.js`

```js
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'

const app = createApp(App)

app.use(createPinia())

app.mount('#app')
```

Pinia도 Vue 플러그인이므로 `mount` 전에 등록한다.

---

## Store 만들기

`src/stores/counter.js`

```js
import { computed, ref } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore('counter', () => {
  const count = ref(0)

  const doubleCount = computed(() => count.value * 2)

  function increment() {
    count.value++
  }

  function reset() {
    count.value = 0
  }

  return {
    count,
    doubleCount,
    increment,
    reset
  }
})
```

이 방식은 setup store라고 부른다. Composition API와 비슷하게 작성한다.

---

## Store 사용하기

```vue
<script setup>
import { useCounterStore } from './stores/counter'

const counter = useCounterStore()
</script>

<template>
  <p>count: {{ counter.count }}</p>
  <p>double: {{ counter.doubleCount }}</p>
  <button @click="counter.increment">증가</button>
  <button @click="counter.reset">초기화</button>
</template>
```

store의 상태와 함수는 컴포넌트에서 바로 사용할 수 있다.

---

## state, getters, actions

Pinia 문서에서는 다음 용어를 자주 사용한다.

| 용어 | setup store에서의 형태 | 의미 |
| --- | --- | --- |
| state | `ref`, `reactive` | 저장되는 상태 |
| getters | `computed` | 상태에서 계산된 값 |
| actions | `function` | 상태를 바꾸거나 비동기 작업 수행 |

setup store에서는 Vue Composition API와 거의 같은 감각으로 작성하면 된다.

---

## 장바구니 Store 예제

`src/stores/cart.js`

```js
import { computed, ref } from 'vue'
import { defineStore } from 'pinia'

export const useCartStore = defineStore('cart', () => {
  const items = ref([])

  const totalCount = computed(() => {
    return items.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  const totalPrice = computed(() => {
    return items.value.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0
    )
  })

  function addItem(product) {
    const found = items.value.find((item) => item.id === product.id)

    if (found) {
      found.quantity++
      return
    }

    items.value.push({
      ...product,
      quantity: 1
    })
  }

  function removeItem(id) {
    items.value = items.value.filter((item) => item.id !== id)
  }

  return {
    items,
    totalCount,
    totalPrice,
    addItem,
    removeItem
  }
})
```

---

## Store를 구조 분해할 때 주의

store를 일반 구조 분해하면 반응성이 깨질 수 있다.

```js
const cart = useCartStore()
const { totalCount } = cart
```

상태를 구조 분해해야 한다면 `storeToRefs`를 사용한다.

```js
import { storeToRefs } from 'pinia'

const cart = useCartStore()
const { items, totalCount, totalPrice } = storeToRefs(cart)
```

함수는 그냥 구조 분해해도 된다.

```js
const { addItem, removeItem } = cart
```

---

## 언제 Pinia를 사용할까?

다음 상태는 Pinia에 어울린다.

- 로그인 사용자 정보
- 장바구니
- 전역 알림
- 테마 설정
- 여러 페이지가 공유하는 필터 조건

한 컴포넌트 안에서만 쓰는 상태는 굳이 Pinia에 넣지 않아도 된다.

---

## 자주 하는 실수

### 모든 상태를 Store에 넣음

지역 상태는 컴포넌트 안에 두는 것이 더 단순하다.

### Store 함수 이름을 일반 함수처럼 지음

Pinia store 함수는 보통 `useSomethingStore` 형태로 짓는다.

```js
export const useCartStore = defineStore('cart', () => {})
```

### mount 후에 Pinia를 등록함

```js
app.use(createPinia())
app.mount('#app')
```

---

## 작은 실습

장바구니 store를 만들어보자.

- 상품 추가
- 상품 삭제
- 총 개수 계산
- 총 가격 계산
- `CartButton.vue`에서 총 개수 표시
- `CartPage.vue`에서 상품 목록 표시

---

## 이번 장 요약

- Pinia는 Vue 공식 상태 관리 라이브러리다.
- 여러 컴포넌트가 공유하는 상태를 store에 둔다.
- setup store에서는 `ref`, `computed`, `function`으로 상태, 계산값, 동작을 만든다.
- store 상태를 구조 분해할 때는 `storeToRefs`를 사용한다.

---

## 다음 장으로

다음 장에서는 API 통신과 비동기 상태를 다루는 방법을 배운다.

