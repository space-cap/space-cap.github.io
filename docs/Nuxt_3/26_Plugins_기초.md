# Plugins 기초

## 이번 장에서 배울 것

- plugin이 무엇인지
- `plugins` 폴더의 역할
- 클라이언트 전용 plugin과 서버 전용 plugin
- plugin에서 값을 제공하는 방법

## plugin이란

plugin은 Nuxt 앱이 시작될 때 실행되는 코드다.

보통 다음과 같은 일을 할 때 사용한다.

- Vue plugin 등록
- 외부 라이브러리 초기화
- 앱 전체에서 사용할 값을 주입
- 앱 실행 시 한 번 필요한 설정 수행

초보 단계에서는 "앱 시작 시 등록해야 하는 기능을 넣는 곳"이라고 이해하면 된다.

## plugins 폴더

Nuxt는 `plugins` 폴더의 파일을 자동으로 읽어 등록한다.

```txt
plugins/
  hello.ts
```

`plugins/hello.ts`를 작성한다.

```ts
export default defineNuxtPlugin((nuxtApp) => {
  console.log('Nuxt plugin 실행')
})
```

개발 서버를 실행하면 앱이 시작될 때 plugin 코드가 실행된다.

## provide로 값 제공하기

plugin에서 앱 전체에 사용할 함수를 제공할 수 있다.

```ts
export default defineNuxtPlugin(() => {
  return {
    provide: {
      hello: (name: string) => `안녕하세요, ${name}`
    }
  }
})
```

컴포넌트에서 `useNuxtApp`으로 사용할 수 있다.

```vue
<script setup>
const { $hello } = useNuxtApp()
</script>

<template>
  <p>{{ $hello('Nuxt') }}</p>
</template>
```

Nuxt는 제공된 값 앞에 `$`를 붙여 사용할 수 있게 한다.

## composable과 plugin의 차이

plugin과 composable은 모두 공통 코드를 다룰 수 있지만 목적이 다르다.

composable은 필요할 때 호출해 상태나 로직을 재사용하는 함수다.

plugin은 앱이 시작될 때 한 번 등록하거나 초기화해야 하는 기능에 적합하다.

공식 문서에서도 단순 helper를 전역으로 많이 주입하기보다 composable을 사용하는 방식을 권장한다. 전역 주입이 많아지면 어디에서 온 값인지 추적하기 어려워질 수 있다.

## 클라이언트 전용 plugin

브라우저에서만 동작해야 하는 plugin은 `.client`를 붙인다.

```txt
plugins/
  browser-only.client.ts
```

```ts
export default defineNuxtPlugin(() => {
  console.log(window.location.href)
})
```

`window`, `document`, `localStorage`처럼 브라우저에만 있는 기능을 사용할 때 필요하다.

## 서버 전용 plugin

서버에서만 실행해야 하는 plugin은 `.server`를 붙인다.

```txt
plugins/
  server-only.server.ts
```

```ts
export default defineNuxtPlugin(() => {
  console.log('서버에서만 실행되는 plugin')
})
```

서버 전용 로직과 클라이언트 전용 로직을 파일 이름으로 구분하면 SSR 오류를 줄일 수 있다.

## plugin 등록 순서

plugin 사이에 의존 관계가 있다면 파일 이름 앞에 숫자를 붙여 순서를 조정할 수 있다.

```txt
plugins/
  01.api.ts
  02.auth.ts
```

파일 이름은 문자열 기준으로 정렬된다. 그래서 `1.plugin.ts`, `10.plugin.ts`, `2.plugin.ts`처럼 만들면 예상과 다를 수 있다. 한 자리 숫자에는 `01`, `02`처럼 0을 붙이는 것이 좋다.

## 자주 하는 실수

plugin에 너무 많은 로직을 넣는 실수를 조심해야 한다.

앱 시작 시 plugin이 무겁게 동작하면 초기 로딩이 느려질 수 있다. 단순히 여러 컴포넌트에서 재사용하는 함수라면 plugin보다 composable이 더 적합한 경우가 많다.

## 정리

plugin은 Nuxt 앱이 시작될 때 실행되는 등록 코드다. `plugins` 폴더의 파일은 자동으로 등록되며, `.client`와 `.server` 접미사로 실행 환경을 구분할 수 있다.

## 다음 장으로

다음 장에서는 환경 변수와 실행 설정을 다루는 Runtime Config를 배운다.
