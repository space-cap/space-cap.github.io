# Nuxt DevTools

## 이번 장에서 배울 것

- Nuxt DevTools가 무엇인지
- 어떤 정보를 확인할 수 있는지
- 학습 중 DevTools를 어떻게 활용하면 좋은지
- 브라우저 개발자 도구와의 차이

## Nuxt DevTools란

Nuxt DevTools는 Nuxt 앱을 개발할 때 앱의 구조와 상태를 브라우저에서 확인할 수 있게 해 주는 개발 도구다.

라우트, 컴포넌트, 자동 import, 모듈, 플러그인, 서버 라우트 같은 정보를 시각적으로 확인할 수 있다.

처음 Nuxt를 배울 때는 "내가 만든 파일이 Nuxt에 어떻게 인식되고 있는지" 확인하는 도구로 사용하면 좋다.

## DevTools 활성화

Nuxt 프로젝트에서는 `nuxt.config.ts`에서 DevTools 설정을 확인할 수 있다.

```ts
export default defineNuxtConfig({
  devtools: { enabled: true }
})
```

Nuxt 버전에 따라 기본 활성화 여부가 다를 수 있으므로, 학습 중에는 명시적으로 켜 두면 이해하기 쉽다.

## 확인할 수 있는 정보

Nuxt DevTools에서는 다음과 같은 정보를 확인할 수 있다.

- 현재 Nuxt와 Vue 버전
- pages 기반 라우트 목록
- 컴포넌트 목록
- 자동 import 목록
- 설치된 Nuxt 모듈
- 등록된 plugins
- public assets
- runtime config
- payload와 상태
- server routes
- 빌드 분석 정보

모든 기능을 처음부터 다 알 필요는 없다. 학습 단계에 맞춰 하나씩 보면 된다.

## 라우팅 학습에 활용하기

`pages` 폴더에 파일을 추가한 뒤 DevTools에서 라우트 목록을 확인해 보자.

예를 들어 다음 파일을 만들었다면

```txt
pages/
  index.vue
  about.vue
  posts/
    [id].vue
```

DevTools에서 `/`, `/about`, `/posts/:id` 같은 라우트가 인식되는지 확인할 수 있다.

라우팅을 배울 때 특히 유용하다.

## 컴포넌트 학습에 활용하기

`components` 폴더에 컴포넌트를 만들면 DevTools에서 컴포넌트 목록을 확인할 수 있다.

```txt
components/
  AppHeader.vue
  PostCard.vue
```

자동 등록이 제대로 되었는지 확인할 수 있고, 어떤 컴포넌트가 어디에서 사용되는지도 파악하는 데 도움이 된다.

## Auto Imports 확인하기

Nuxt는 많은 함수를 자동으로 import한다.

처음에는 `ref`, `useRoute`, `useFetch`, `useRuntimeConfig`가 어디에서 오는지 헷갈릴 수 있다.

DevTools의 imports 관련 화면을 보면 어떤 항목이 자동 import로 등록되어 있는지 확인할 수 있다.

## Server Routes 확인하기

나중에 `server/api` 폴더에 API를 만들면 DevTools에서 서버 라우트를 확인할 수 있다.

```txt
server/
  api/
    hello.get.ts
```

이런 파일이 `/api/hello`로 연결되는지 확인하는 데 유용하다.

## 브라우저 개발자 도구와의 차이

브라우저 개발자 도구는 HTML, CSS, 네트워크 요청, 콘솔 오류를 확인하는 데 강하다.

Nuxt DevTools는 Nuxt 앱의 구조를 이해하는 데 강하다.

둘은 경쟁 관계가 아니라 함께 쓰는 도구다.

- 화면 스타일 문제: 브라우저 Elements 탭
- API 요청 문제: 브라우저 Network 탭
- Nuxt 라우트 확인: Nuxt DevTools
- 자동 import 확인: Nuxt DevTools
- 서버 API 목록 확인: Nuxt DevTools

## 자주 하는 실수

DevTools가 보여 주는 정보만 보고 실제 브라우저 콘솔 오류를 놓치는 경우가 있다.

문제가 생기면 다음을 함께 확인한다.

- 터미널 로그
- 브라우저 콘솔
- 브라우저 Network 탭
- Nuxt DevTools

여러 도구를 함께 보면 오류의 위치를 더 빨리 찾을 수 있다.

## 정리

Nuxt DevTools는 Nuxt 앱의 라우트, 컴포넌트, imports, plugins, server routes 같은 내부 구조를 확인하는 개발 도구다. 학습 중에는 내가 만든 파일이 Nuxt에 어떻게 연결되는지 확인하는 용도로 적극 활용하면 좋다.

## 다음 장으로

다음 장부터는 `useFetch`, `useAsyncData`, `$fetch`를 이용해 데이터를 가져오는 방법을 배운다.
