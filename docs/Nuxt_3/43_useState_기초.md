# useState 기초

## 이번 장에서 배울 것

- `useState`가 무엇인지
- `ref`와 `useState`의 차이
- SSR에서 전역 상태를 안전하게 다루는 방법
- `useState`를 composable로 감싸는 패턴

## 상태 관리란

상태는 화면이나 앱이 기억해야 하는 값이다.

예를 들어 다음 값들은 상태다.

- 로그인한 사용자 정보
- 장바구니 상품 목록
- 다크 모드 여부
- 선택된 탭
- 검색 조건
- 알림 메시지

Vue에서는 보통 `ref`나 `reactive`로 상태를 만든다. Nuxt에서도 컴포넌트 내부 상태는 `ref`로 충분하다.

하지만 여러 컴포넌트가 함께 쓰고, SSR에서도 안전하게 유지해야 하는 상태라면 `useState`를 사용할 수 있다.

## useState란

`useState`는 Nuxt가 제공하는 SSR 친화적인 공유 상태 composable이다.

기본 형태는 다음과 같다.

```vue
<script setup>
const count = useState('count', () => 0)
</script>

<template>
  <button @click="count++">
    {{ count }}
  </button>
</template>
```

첫 번째 인자인 `'count'`는 상태를 구분하는 key다.

두 번째 인자인 `() => 0`은 초기값을 만드는 함수다.

## ref와 useState의 차이

`ref`는 보통 현재 컴포넌트 안에서만 사용하는 상태에 적합하다.

```vue
<script setup>
const count = ref(0)
</script>
```

`useState`는 같은 key를 사용하는 여러 곳에서 공유된다.

```vue
<script setup>
const count = useState('count', () => 0)
</script>
```

다른 컴포넌트에서도 같은 key로 `useState`를 호출하면 같은 상태를 바라볼 수 있다.

```vue
<script setup>
const count = useState('count', () => 0)
</script>
```

## SSR에서 왜 useState가 필요할까

Nuxt는 서버에서 여러 사용자의 요청을 처리할 수 있다.

서버 파일의 최상단에 `ref`를 만들면 여러 요청 사이에서 상태가 공유될 위험이 있다.

```ts
// 피해야 하는 예
export const user = ref(null)
```

서버에서 이런 전역 상태를 잘못 사용하면 한 사용자의 데이터가 다른 사용자 요청에 섞일 수 있다.

Nuxt 공식 문서에서도 `ref`를 `<script setup>`이나 `setup()` 밖의 전역 위치에 정의하지 말라고 안내한다.

SSR 친화적인 공유 상태가 필요하면 `useState`를 사용한다.

## 직렬화 가능한 값만 넣기

`useState`의 값은 서버에서 클라이언트로 전달될 수 있다. 이 과정에서 JSON으로 직렬화된다.

따라서 다음처럼 직렬화하기 어려운 값은 넣지 않는 것이 좋다.

- 함수
- 클래스 인스턴스
- Symbol
- 복잡한 브라우저 객체

다음처럼 단순한 값이 좋다.

- 문자열
- 숫자
- boolean
- 배열
- plain object
- null

## composable로 감싸기

`useState`를 매번 직접 쓰기보다 composable로 감싸면 관리하기 쉽다.

`composables/useTheme.ts`

```ts
export const useTheme = () => {
  return useState('theme', () => 'light')
}
```

컴포넌트에서 사용한다.

```vue
<script setup>
const theme = useTheme()

const toggleTheme = () => {
  theme.value = theme.value === 'light' ? 'dark' : 'light'
}
</script>

<template>
  <button @click="toggleTheme">
    현재 테마: {{ theme }}
  </button>
</template>
```

이렇게 하면 key를 여러 파일에 흩뿌리지 않아도 된다.

## 사용자 상태 예시

간단한 사용자 상태를 만들어 보자.

`composables/useCurrentUser.ts`

```ts
export const useCurrentUser = () => {
  return useState('current-user', () => null)
}
```

페이지에서 사용한다.

```vue
<script setup>
const user = useCurrentUser()

const login = () => {
  user.value = {
    name: '홍길동'
  }
}

const logout = () => {
  user.value = null
}
</script>
```

이 예시는 학습용이다. 실제 로그인 인증은 서버 검증, 쿠키, 토큰 처리와 함께 설계해야 한다.

## 언제 useState를 쓰면 좋을까

`useState`는 다음 경우에 적합하다.

- 여러 컴포넌트에서 공유하는 작은 상태
- SSR과 hydration 사이에 유지되어야 하는 상태
- 간단한 전역 상태
- composable로 감싼 앱 공통 상태

상태가 커지고 로직이 복잡해지면 Pinia 같은 상태 관리 라이브러리를 고려한다.

## 자주 하는 실수

`useState` key를 중복해서 엉뚱한 상태를 공유하는 실수가 있다.

```ts
useState('data', () => [])
```

`data`처럼 너무 일반적인 key는 피하는 것이 좋다. `cart-items`, `current-user`, `theme-mode`처럼 의미가 분명한 key를 사용하자.

## 정리

`useState`는 Nuxt에서 SSR 친화적인 공유 상태를 만들 때 사용하는 composable이다. 전역 위치에 `ref`를 만드는 대신 `useState`를 사용하고, 직렬화 가능한 값만 저장하는 것이 중요하다.

## 다음 장으로

다음 장에서는 상태가 더 복잡해질 때 사용하는 Pinia를 배운다.
