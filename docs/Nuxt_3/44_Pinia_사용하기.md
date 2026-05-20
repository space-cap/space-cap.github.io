# Pinia 사용하기

## 이번 장에서 배울 것

- Pinia가 무엇인지
- `useState`와 Pinia의 차이
- Nuxt에서 Pinia를 설치하는 방법
- store를 만들고 사용하는 방법

## Pinia란

Pinia는 Vue 공식 생태계에서 많이 사용하는 상태 관리 라이브러리다.

Nuxt의 `useState`는 간단한 공유 상태에 좋다. 하지만 상태가 많아지고, 상태를 바꾸는 로직과 계산된 값이 함께 필요해지면 Pinia가 더 편하다.

Pinia는 다음을 store라는 단위로 관리한다.

- state
- getters
- actions

## useState와 Pinia 비교

| 구분 | useState | Pinia |
| --- | --- | --- |
| 용도 | 간단한 공유 상태 | 구조화된 전역 상태 관리 |
| 상태 단위 | key 기반 ref | store |
| 계산 값 | 직접 computed 작성 | getters |
| 상태 변경 로직 | composable 함수로 구성 | actions |
| 규모 | 작고 단순한 상태 | 중대형 앱 상태 |

처음에는 `useState`로 시작해도 된다. 하지만 장바구니, 사용자, 권한, 설정처럼 상태와 로직이 많아지면 Pinia가 좋다.

## Pinia 설치

Nuxt에서는 Pinia 모듈을 사용할 수 있다.

```bash
npx nuxi@latest module add pinia
```

이 명령은 Nuxt 프로젝트에 Pinia 모듈을 추가한다.

수동으로 설정한다면 `nuxt.config.ts`에 모듈을 등록한다.

```ts
export default defineNuxtConfig({
  modules: ['@pinia/nuxt']
})
```

## stores 폴더 만들기

프로젝트 루트에 `stores` 폴더를 만든다.

```txt
stores/
  counter.ts
```

`stores/counter.ts`

```ts
export const useCounterStore = defineStore('counter', {
  state: () => ({
    count: 0
  }),
  getters: {
    double: (state) => state.count * 2
  },
  actions: {
    increase() {
      this.count++
    }
  }
})
```

Pinia store 이름은 앱 안에서 고유해야 한다. 여기서는 `'counter'`를 사용했다.

## store 사용하기

페이지나 컴포넌트에서 store를 사용한다.

```vue
<script setup>
const counterStore = useCounterStore()
</script>

<template>
  <section>
    <p>숫자: {{ counterStore.count }}</p>
    <p>두 배: {{ counterStore.double }}</p>
    <button @click="counterStore.increase">
      증가
    </button>
  </section>
</template>
```

`@pinia/nuxt`를 사용하면 `stores` 폴더의 store를 자동으로 import할 수 있다.

## storeToRefs

store에서 값을 구조 분해할 때 반응성을 유지하려면 `storeToRefs`를 사용한다.

```vue
<script setup>
const counterStore = useCounterStore()
const { count, double } = storeToRefs(counterStore)

const { increase } = counterStore
</script>
```

state와 getters는 `storeToRefs`로 꺼내고, actions는 그대로 꺼내면 된다.

## setup store 방식

Pinia는 setup 함수 스타일로도 store를 만들 수 있다.

```ts
export const useCounterStore = defineStore('counter', () => {
  const count = ref(0)
  const double = computed(() => count.value * 2)

  const increase = () => {
    count.value++
  }

  return {
    count,
    double,
    increase
  }
})
```

Vue Composition API에 익숙하다면 이 방식이 편할 수 있다.

## Nuxt에서 데이터 불러오기

페이지에서 store action을 실행해 데이터를 가져올 수 있다.

```ts
export const useUserStore = defineStore('user', {
  state: () => ({
    user: null
  }),
  actions: {
    async fetchUser() {
      this.user = await $fetch('/api/me')
    }
  }
})
```

페이지에서는 Nuxt의 `callOnce`를 함께 사용할 수 있다.

```vue
<script setup>
const userStore = useUserStore()

await callOnce('user', () => userStore.fetchUser())
</script>
```

`callOnce`를 사용하면 같은 데이터를 불필요하게 반복해서 가져오는 일을 줄이는 데 도움이 된다.

## 언제 Pinia를 쓰면 좋을까

다음 상황에서는 Pinia를 고려한다.

- 여러 페이지에서 공유하는 상태가 많다.
- 상태 변경 로직이 복잡하다.
- getters와 actions로 구조화하고 싶다.
- DevTools로 상태를 추적하고 싶다.
- 장바구니, 로그인 사용자, 권한, 설정 같은 도메인 상태가 있다.

## 자주 하는 실수

store에서 꺼낸 값을 일반 구조 분해하면 반응성이 깨질 수 있다.

```ts
const { count } = useCounterStore()
```

state와 getters를 구조 분해하려면 `storeToRefs`를 사용한다.

```ts
const store = useCounterStore()
const { count } = storeToRefs(store)
```

## 정리

Pinia는 Nuxt 앱에서 구조화된 상태 관리를 할 때 유용하다. 간단한 상태는 `useState`, 로직과 상태가 커지는 경우는 Pinia를 선택하면 좋다.

## 다음 장으로

다음 장에서는 로그인 상태 유지와 사용자 설정 저장에 자주 쓰는 Cookie를 배운다.
