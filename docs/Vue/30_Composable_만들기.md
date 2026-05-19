# Composable 만들기

## 이번 장에서 배울 것

Composable은 Vue의 Composition API를 사용해 **상태가 있는 로직을 재사용 가능한 함수로 분리한 것**이다.

예를 들어 여러 컴포넌트에서 마우스 위치, 카운터, API 요청, localStorage 저장 같은 로직을 반복해서 사용한다면 Composable로 분리할 수 있다.

---

## 왜 Composable이 필요할까?

컴포넌트가 커지면 화면 코드와 로직이 뒤섞이기 쉽다.

```vue
<script setup>
import { onMounted, onUnmounted, ref } from 'vue'

const x = ref(0)
const y = ref(0)

function update(event) {
  x.value = event.pageX
  y.value = event.pageY
}

onMounted(() => {
  window.addEventListener('mousemove', update)
})

onUnmounted(() => {
  window.removeEventListener('mousemove', update)
})
</script>
```

이 로직을 여러 컴포넌트에서 쓰고 싶다면 매번 복사해야 한다. Composable로 빼면 재사용할 수 있다.

---

## 첫 Composable 만들기

`src/composables/useMouse.js` 파일을 만든다.

```js
import { onMounted, onUnmounted, ref } from 'vue'

export function useMouse() {
  const x = ref(0)
  const y = ref(0)

  function update(event) {
    x.value = event.pageX
    y.value = event.pageY
  }

  onMounted(() => {
    window.addEventListener('mousemove', update)
  })

  onUnmounted(() => {
    window.removeEventListener('mousemove', update)
  })

  return {
    x,
    y
  }
}
```

컴포넌트에서 사용한다.

```vue
<script setup>
import { useMouse } from './composables/useMouse'

const { x, y } = useMouse()
</script>

<template>
  <p>마우스 위치: {{ x }}, {{ y }}</p>
</template>
```

---

## 이름 규칙

Composable 함수 이름은 보통 `use`로 시작한다.

```text
useMouse
useCounter
useFetch
useLocalStorage
```

이 규칙은 "이 함수가 Vue 상태나 생명주기 같은 조합 가능한 로직을 담고 있다"는 신호가 된다.

---

## useCounter 만들기

간단한 카운터 로직을 분리해보자.

`src/composables/useCounter.js`

```js
import { ref } from 'vue'

export function useCounter(initialValue = 0) {
  const count = ref(initialValue)

  function increase() {
    count.value++
  }

  function decrease() {
    count.value--
  }

  function reset() {
    count.value = initialValue
  }

  return {
    count,
    increase,
    decrease,
    reset
  }
}
```

사용:

```vue
<script setup>
import { useCounter } from './composables/useCounter'

const { count, increase, decrease, reset } = useCounter(10)
</script>

<template>
  <p>{{ count }}</p>
  <button @click="increase">증가</button>
  <button @click="decrease">감소</button>
  <button @click="reset">초기화</button>
</template>
```

---

## useFetch 만들기

API 요청 로직도 Composable로 만들 수 있다.

```js
import { ref } from 'vue'

export function useFetch() {
  const data = ref(null)
  const error = ref(null)
  const loading = ref(false)

  async function execute(url) {
    data.value = null
    error.value = null
    loading.value = true

    try {
      const response = await fetch(url)

      if (!response.ok) {
        throw new Error('요청에 실패했습니다.')
      }

      data.value = await response.json()
    } catch (err) {
      error.value = err
    } finally {
      loading.value = false
    }
  }

  return {
    data,
    error,
    loading,
    execute
  }
}
```

사용:

```vue
<script setup>
import { onMounted } from 'vue'
import { useFetch } from './composables/useFetch'

const { data, error, loading, execute } = useFetch()

onMounted(() => {
  execute('/api/posts')
})
</script>
```

---

## Composable을 만들 때의 기준

모든 코드를 무조건 Composable로 분리할 필요는 없다.

다음 조건에 해당하면 분리를 고려한다.

- 여러 컴포넌트에서 같은 로직을 사용한다.
- 컴포넌트가 너무 길어져서 읽기 어렵다.
- 화면 구조와 무관한 상태 로직이다.
- 테스트하거나 독립적으로 이해하고 싶은 로직이다.

반대로 한 컴포넌트에서만 쓰는 짧은 로직은 그대로 두는 편이 더 읽기 좋을 수 있다.

---

## Composable과 컴포넌트의 차이

| 구분 | 컴포넌트 | Composable |
| --- | --- | --- |
| 목적 | 화면 조각 재사용 | 상태 로직 재사용 |
| 파일 | `.vue` | 보통 `.js` 또는 `.ts` |
| 포함 내용 | template, script, style | 함수와 반응형 상태 |
| 예 | `BaseButton.vue` | `useCounter.js` |

---

## 자주 하는 실수

### Composable 안에서 템플릿을 다루려고 함

Composable은 화면 구조가 아니라 로직을 담는다. HTML은 컴포넌트에 둔다.

### return을 빼먹음

컴포넌트에서 사용할 값과 함수는 반환해야 한다.

```js
return {
  count,
  increase
}
```

### 너무 일찍 분리함

처음부터 모든 로직을 분리하면 오히려 파일 이동이 많아진다. 반복이 보이거나 컴포넌트가 길어질 때 분리해도 늦지 않다.

---

## 작은 실습

`useLocalStorage`를 만들어보자.

요구사항:

- key와 초기값을 인자로 받는다.
- localStorage에서 기존 값을 읽는다.
- 값이 바뀌면 localStorage에 저장한다.
- 컴포넌트에서는 `const name = useLocalStorage('name', '')`처럼 사용한다.

힌트:

```js
import { ref, watch } from 'vue'

export function useLocalStorage(key, initialValue) {
  const storedValue = localStorage.getItem(key)
  const value = ref(storedValue ? JSON.parse(storedValue) : initialValue)

  watch(value, (newValue) => {
    localStorage.setItem(key, JSON.stringify(newValue))
  })

  return value
}
```

---

## 이번 장 요약

- Composable은 상태가 있는 로직을 재사용하는 함수다.
- 이름은 보통 `use`로 시작한다.
- `ref`, `computed`, `watch`, 생명주기 훅을 Composable 안에서 사용할 수 있다.
- 화면 재사용은 컴포넌트, 로직 재사용은 Composable로 생각하면 쉽다.

---

## 다음 장으로

다음 장에서는 DOM을 직접 다뤄야 할 때 사용하는 커스텀 디렉티브를 배운다.

