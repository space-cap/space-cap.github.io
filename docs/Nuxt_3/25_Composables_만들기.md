# Composables 만들기

## 이번 장에서 배울 것

- composable이 무엇인지
- `composables` 폴더에 재사용 로직을 만드는 방법
- `useCounter`, `useApi`, `useAuth` 같은 composable 예시
- composable을 만들 때 주의할 점

## composable이란

composable은 Vue Composition API를 이용해 재사용 가능한 로직을 함수로 분리한 것이다.

컴포넌트 안에 모든 코드를 넣으면 파일이 점점 커진다. 여러 컴포넌트에서 같은 로직이 반복되면 수정도 어려워진다.

이럴 때 composable로 분리한다.

예를 들어 다음 로직은 composable로 만들기 좋다.

- 카운터 상태
- 로그인 사용자 정보
- API 호출 기본 설정
- 다크 모드 상태
- 페이지네이션 상태
- 폼 입력 상태

## composables 폴더

Nuxt에서는 프로젝트 루트에 `composables` 폴더를 만든다.

```txt
composables/
  useCounter.ts
```

`use`로 시작하는 이름을 자주 사용한다.

```ts
export const useCounter = () => {
  const count = ref(0)

  const increase = () => {
    count.value++
  }

  const decrease = () => {
    count.value--
  }

  return {
    count,
    increase,
    decrease
  }
}
```

페이지에서 사용한다.

```vue
<script setup>
const { count, increase, decrease } = useCounter()
</script>

<template>
  <button @click="decrease">-</button>
  <span>{{ count }}</span>
  <button @click="increase">+</button>
</template>
```

## 이름을 use로 시작하는 이유

Vue와 Nuxt 생태계에서는 composable 이름을 보통 `use`로 시작한다.

```txt
useCounter
useAuth
useApi
usePagination
```

이름만 봐도 "상태나 로직을 꺼내 쓰는 함수"라는 의미가 드러난다.

## API 기본 경로 composable

API 기본 경로를 runtime config에서 읽는 composable을 만들어 보자.

`composables/useApiBase.ts`

```ts
export const useApiBase = () => {
  const config = useRuntimeConfig()

  return config.public.apiBase
}
```

페이지에서 사용한다.

```vue
<script setup>
const apiBase = useApiBase()
</script>

<template>
  <p>API 기본 경로: {{ apiBase }}</p>
</template>
```

이처럼 Nuxt composable을 감싸서 프로젝트에 맞는 도구를 만들 수 있다.

## 로그인 상태 composable 예시

간단한 로그인 상태를 composable로 만들 수 있다.

```ts
export const useAuth = () => {
  const user = useState('auth:user', () => null)

  const login = (name: string) => {
    user.value = { name }
  }

  const logout = () => {
    user.value = null
  }

  return {
    user,
    login,
    logout
  }
}
```

`useState`는 Nuxt에서 SSR을 고려해 전역 상태를 다룰 때 사용할 수 있는 composable이다. 인증 파트에서 더 자세히 배운다.

## composable을 너무 크게 만들지 않기

composable은 재사용 로직을 담는 도구다. 하지만 너무 많은 일을 한 함수에 넣으면 오히려 이해하기 어려워진다.

좋은 composable은 역할이 분명하다.

- `useCounter`: 카운터 상태 관리
- `usePagination`: 페이지 번호와 이동
- `useAuth`: 로그인 사용자 상태
- `useApi`: API 호출 공통 처리

이름만 보고 역할을 알 수 있게 만드는 것이 중요하다.

## 자주 하는 실수

composable 파일의 최상단에서 Nuxt composable을 바로 호출하는 실수를 조심해야 한다.

```ts
// 피하는 것이 좋은 예
const config = useRuntimeConfig()

export const useApi = () => {
  return config.public.apiBase
}
```

대신 composable 함수 안에서 호출한다.

```ts
export const useApi = () => {
  const config = useRuntimeConfig()

  return config.public.apiBase
}
```

## 정리

composable은 재사용 가능한 상태와 로직을 함수로 분리하는 방법이다. Nuxt에서는 `composables` 폴더에 만든 함수를 자동으로 사용할 수 있다.

## 다음 장으로

다음 장에서는 앱 시작 시 외부 라이브러리나 전역 기능을 연결하는 Plugins를 배운다.
