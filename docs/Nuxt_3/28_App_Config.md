# App Config

## 이번 장에서 배울 것

- `app.config.ts`가 무엇인지
- `useAppConfig` 사용 방법
- Runtime Config와 App Config의 차이
- 테마 설정 예시

## App Config란

App Config는 앱 전체에서 사용할 공개 설정을 관리하는 파일이다.

예를 들어 다음 값은 App Config에 어울린다.

- 사이트 이름
- 테마 색상 이름
- UI 컴포넌트 기본 옵션
- 공개적인 앱 설정

중요한 점은 App Config에 비밀 값을 넣으면 안 된다는 것이다. App Config는 클라이언트 번들에 노출될 수 있다.

## app.config.ts 만들기

프로젝트 루트에 `app.config.ts` 파일을 만든다.

```txt
app.config.ts
```

다음처럼 작성한다.

```ts
export default defineAppConfig({
  site: {
    name: 'Nuxt 학습 사이트'
  },
  theme: {
    primaryColor: '#2563eb'
  }
})
```

## useAppConfig로 읽기

페이지나 컴포넌트에서 `useAppConfig`를 사용한다.

```vue
<script setup>
const appConfig = useAppConfig()
</script>

<template>
  <section>
    <h1>{{ appConfig.site.name }}</h1>
    <p>기본 색상: {{ appConfig.theme.primaryColor }}</p>
  </section>
</template>
```

`useAppConfig`도 Nuxt에서 자동 import되므로 별도 import 없이 사용할 수 있다.

## Runtime Config와 App Config 비교

둘 다 설정을 다루지만 목적이 다르다.

| 구분 | Runtime Config | App Config |
| --- | --- | --- |
| 파일 | `nuxt.config.ts` | `app.config.ts` |
| 주요 목적 | 환경별 설정, 서버 비밀 값 | 공개 앱 설정 |
| 비밀 값 저장 | 가능하나 public 밖에만 | 불가능 |
| 클라이언트 노출 | public만 노출 | 노출될 수 있음 |
| 예시 | API secret, API base URL | 테마, 사이트 이름 |

비밀 값이 조금이라도 관련되어 있다면 App Config에 넣지 않는다.

## 테마 설정 예시

간단한 테마 이름을 App Config로 관리해 보자.

```ts
export default defineAppConfig({
  theme: {
    name: 'light',
    primaryClass: 'button-primary'
  }
})
```

컴포넌트에서 사용한다.

```vue
<script setup>
const appConfig = useAppConfig()
</script>

<template>
  <button :class="appConfig.theme.primaryClass">
    저장
  </button>
</template>
```

이런 방식은 앱 전체에서 공통 UI 설정을 맞출 때 사용할 수 있다.

## updateAppConfig

Nuxt는 실행 중에 App Config 값을 업데이트할 수 있는 `updateAppConfig`도 제공한다.

```vue
<script setup>
const appConfig = useAppConfig()

const changeTheme = () => {
  updateAppConfig({
    theme: {
      name: 'dark'
    }
  })
}
</script>
```

초보 단계에서는 "App Config는 읽을 수 있고, 필요하면 갱신할 수도 있다" 정도만 기억하면 충분하다.

## 자주 하는 실수

App Config에 비밀 키를 넣는 실수를 조심해야 한다.

```ts
export default defineAppConfig({
  apiSecret: '절대 넣으면 안 되는 값'
})
```

이런 값은 `runtimeConfig`의 public 밖에 둔다.

## 정리

App Config는 앱 전체에서 사용할 공개 설정을 관리한다. 테마, 사이트 이름, UI 옵션처럼 사용자에게 노출되어도 괜찮은 값에 적합하다. 비밀 값은 절대 넣지 않는다.

## 다음 장으로

다음 장에서는 Nuxt 앱을 이해하고 디버깅하는 데 도움이 되는 Nuxt DevTools를 살펴본다.
