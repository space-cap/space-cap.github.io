# Auto Imports 이해하기

## 이번 장에서 배울 것

- Auto Imports가 무엇인지
- Nuxt가 자동으로 가져오는 것들
- 명시적 import가 필요한 경우
- Auto Imports를 사용할 때 주의할 점

## Auto Imports란

Auto Imports는 자주 쓰는 함수, 컴포넌트, composable을 직접 import하지 않아도 사용할 수 있게 해 주는 기능이다.

일반 Vue 파일에서는 보통 다음처럼 import한다.

```vue
<script setup>
import { ref, computed } from 'vue'

const count = ref(0)
const double = computed(() => count.value * 2)
</script>
```

Nuxt에서는 다음처럼 바로 사용할 수 있다.

```vue
<script setup>
const count = ref(0)
const double = computed(() => count.value * 2)
</script>
```

`ref`와 `computed`를 import하지 않았지만 Nuxt가 자동으로 처리한다.

## Nuxt가 자동으로 가져오는 것

Nuxt는 다음 항목을 자동으로 가져올 수 있다.

- Vue의 주요 API: `ref`, `computed`, `watch`, `onMounted` 등
- Nuxt composable: `useRoute`, `useRouter`, `useFetch`, `useAsyncData` 등
- `components` 폴더의 컴포넌트
- `composables` 폴더의 composable
- `utils` 폴더의 유틸 함수

이 기능 덕분에 파일마다 반복적인 import 코드가 줄어든다.

## 예제: useRoute

라우트 정보를 읽을 때 `useRoute`를 사용한다.

```vue
<script setup>
const route = useRoute()
</script>

<template>
  <p>현재 경로: {{ route.path }}</p>
</template>
```

`useRoute`도 별도 import 없이 사용할 수 있다.

## 예제: 직접 만든 composable

`composables/useCounter.ts` 파일을 만든다.

```ts
export const useCounter = () => {
  const count = ref(0)

  const increase = () => {
    count.value++
  }

  return {
    count,
    increase
  }
}
```

페이지에서 바로 사용할 수 있다.

```vue
<script setup>
const { count, increase } = useCounter()
</script>

<template>
  <button @click="increase">
    {{ count }}
  </button>
</template>
```

Nuxt가 `composables` 폴더를 스캔해 자동으로 사용할 수 있게 해 준다.

## 명시적 import가 필요한 경우

Auto Imports가 편리하다고 해서 항상 import를 생략해야 하는 것은 아니다.

코드의 출처를 명확히 하고 싶거나, 테스트 파일이나 Nuxt 컨텍스트 밖에서 사용할 때는 명시적으로 import하는 것이 좋을 수 있다.

Nuxt는 자동 import 항목을 `#imports`에서 명시적으로 가져올 수 있게 한다.

```ts
import { ref, computed } from '#imports'
```

초보 단계에서는 자동 import를 사용하되, "이 함수가 Nuxt에서 자동으로 들어온 것"이라는 사실을 기억하면 된다.

## 올바른 위치에서 호출하기

일부 Nuxt composable은 올바른 실행 컨텍스트 안에서 호출해야 한다.

예를 들어 `useRuntimeConfig()`를 파일 최상단에서 바로 호출하면 문제가 될 수 있다.

```ts
// 좋지 않은 예
const config = useRuntimeConfig()

export const useApi = () => {
  return config.public.apiBase
}
```

대신 composable 함수 안에서 호출한다.

```ts
export const useApi = () => {
  const config = useRuntimeConfig()

  return {
    apiBase: config.public.apiBase
  }
}
```

Nuxt가 현재 앱의 실행 문맥을 알고 있는 위치에서 호출해야 안전하다.

## Auto Imports의 장점

Auto Imports의 장점은 다음과 같다.

- 반복적인 import 코드가 줄어든다.
- Nuxt 디렉터리 구조와 잘 맞는다.
- TypeScript 자동 완성과 타입 추론을 유지할 수 있다.
- 실제 사용하는 것만 빌드에 포함하는 데 도움이 된다.

## 자주 하는 실수

자동으로 import된다고 해서 모든 파일에서 모든 함수를 아무 때나 호출할 수 있는 것은 아니다.

특히 `useRoute`, `useFetch`, `useRuntimeConfig` 같은 Nuxt composable은 컴포넌트, plugin, middleware, composable처럼 Nuxt 컨텍스트가 있는 곳에서 사용하는 것이 좋다.

## 정리

Auto Imports는 Nuxt가 Vue API, Nuxt composable, `components`, `composables`, `utils` 폴더의 항목을 자동으로 가져오는 기능이다. 편리하지만 함수가 어디에서 왔는지, 어떤 실행 컨텍스트에서 안전한지 이해하고 사용해야 한다.

## 다음 장으로

다음 장에서는 재사용 가능한 로직을 만드는 `composables` 폴더를 더 자세히 배운다.
