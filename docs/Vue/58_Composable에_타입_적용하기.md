# Composable에 타입 적용하기

## 이번 장에서 배울 것

Composable은 여러 컴포넌트에서 재사용하는 로직이다. TypeScript를 적용하면 인자와 반환값이 명확해져 더 안전하게 사용할 수 있다.

이번 장에서는 `useCounter`, `useFetch`, `useLocalStorage` 같은 Composable에 타입을 적용한다.

---

## 함수 인자와 반환값 타입

```ts
import { ref } from 'vue'

export function useCounter(initialValue: number = 0) {
  const count = ref(initialValue)

  function increase(): void {
    count.value++
  }

  function reset(): void {
    count.value = initialValue
  }

  return {
    count,
    increase,
    reset
  }
}
```

`initialValue`는 number이고, `increase`와 `reset`은 반환값이 없는 함수다.

---

## 타입 추론 활용하기

TypeScript는 반환값을 잘 추론하므로 모든 반환 타입을 직접 적을 필요는 없다.

```ts
export function useCounter(initialValue = 0) {
  const count = ref(initialValue)

  function increase() {
    count.value++
  }

  return {
    count,
    increase
  }
}
```

초보자는 인자 타입부터 명확히 적고, 반환 타입은 필요할 때 추가하면 된다.

---

## 제네릭이란?

제네릭은 사용하는 쪽에서 타입을 정할 수 있게 하는 문법이다.

예를 들어 API 응답 데이터는 화면마다 다르다.

```ts
type Post = {
  id: number
  title: string
}

type User = {
  id: number
  name: string
}
```

`useFetch<Post>()`, `useFetch<User>()`처럼 사용할 수 있으면 재사용성이 좋아진다.

---

## useFetch에 제네릭 적용하기

```ts
import { ref } from 'vue'

export function useFetch<T>() {
  const data = ref<T | null>(null)
  const error = ref<Error | null>(null)
  const loading = ref(false)

  async function execute(url: string): Promise<void> {
    data.value = null
    error.value = null
    loading.value = true

    try {
      const response = await fetch(url)

      if (!response.ok) {
        throw new Error('요청에 실패했습니다.')
      }

      data.value = await response.json() as T
    } catch (err) {
      error.value = err instanceof Error ? err : new Error('알 수 없는 오류')
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

```ts
type Post = {
  id: number
  title: string
}

const { data, loading, error, execute } = useFetch<Post[]>()
```

이제 `data.value`는 `Post[] | null`로 다뤄진다.

---

## API 응답 타입 주의

`as T`는 TypeScript에게 "이 값은 T라고 믿어"라고 알려주는 것이다. 실제 서버 응답을 런타임에 검증하는 것은 아니다.

중요한 API에서는 Zod 같은 런타임 검증 도구를 사용할 수 있다. 초보 단계에서는 타입이 컴파일 시점 도움이라는 점을 기억하자.

---

## useLocalStorage 타입 적용하기

```ts
import { ref, watch } from 'vue'

export function useLocalStorage<T>(key: string, initialValue: T) {
  const storedValue = localStorage.getItem(key)
  const value = ref<T>(
    storedValue ? JSON.parse(storedValue) as T : initialValue
  )

  watch(value, (newValue) => {
    localStorage.setItem(key, JSON.stringify(newValue))
  }, { deep: true })

  return value
}
```

사용:

```ts
const name = useLocalStorage<string>('name', '')
const settings = useLocalStorage('settings', {
  darkMode: false,
  language: 'ko'
})
```

두 번째 예시는 초기값으로 타입을 추론할 수 있다.

---

## MaybeRef 패턴

Composable 인자로 일반 값과 ref를 모두 받고 싶을 때가 있다. Vue는 `MaybeRef`와 `toValue`를 제공한다.

```ts
import { toValue, watchEffect, type MaybeRef } from 'vue'

export function useTitle(title: MaybeRef<string>) {
  watchEffect(() => {
    document.title = toValue(title)
  })
}
```

사용:

```ts
useTitle('고정 제목')
useTitle(pageTitle)
```

초보 단계에서는 이런 패턴이 있다는 정도만 알아두면 된다.

---

## 타입 export하기

Composable에서 사용하는 타입을 밖으로 내보낼 수 있다.

```ts
export type UseFetchResult<T> = {
  data: Ref<T | null>
  error: Ref<Error | null>
  loading: Ref<boolean>
  execute: (url: string) => Promise<void>
}
```

다만 처음부터 너무 많은 타입을 만들 필요는 없다. 실제로 여러 곳에서 필요할 때 분리한다.

---

## 자주 하는 실수

### any를 반환함

Composable은 여러 곳에서 재사용되므로 `any`가 퍼지면 타입 안정성이 크게 떨어진다.

### API 응답 타입을 실제 검증으로 오해함

TypeScript 타입은 런타임 검증이 아니다. 서버 응답이 타입과 다를 수 있다.

### 제네릭을 너무 어렵게 만듦

처음에는 `useFetch<T>()` 정도만 익혀도 충분하다.

---

## 작은 실습

`usePagination<T>`를 만들어보자.

요구사항:

- 전체 목록 `items: T[]`를 받는다.
- 현재 페이지와 페이지 크기를 관리한다.
- 현재 페이지에 보여줄 `pagedItems`를 computed로 반환한다.
- 다음 페이지, 이전 페이지 함수 제공

---

## 이번 장 요약

- Composable에 타입을 적용하면 재사용 로직을 더 안전하게 사용할 수 있다.
- 인자 타입부터 명확히 작성하면 좋다.
- 제네릭을 사용하면 데이터 타입을 사용하는 쪽에서 정할 수 있다.
- TypeScript 타입은 런타임 검증을 대신하지 않는다.

---

## 다음 장으로

다음 장에서는 템플릿 대신 JavaScript로 화면을 만드는 Render Function과 JSX를 배운다.

