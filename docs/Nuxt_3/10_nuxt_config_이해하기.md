# nuxt.config 이해하기

## 이번 장에서 배울 것

- `nuxt.config.ts`의 역할
- 기본 설정을 작성하는 방법
- modules, css, runtimeConfig의 기본 개념

## nuxt.config.ts란

`nuxt.config.ts`는 Nuxt 프로젝트의 설정 파일이다.

프로젝트 루트에 있으며, Nuxt가 앱을 실행하고 빌드할 때 이 파일을 읽는다.

가장 기본적인 형태는 다음과 같다.

```ts
export default defineNuxtConfig({
  devtools: { enabled: true }
})
```

`defineNuxtConfig`는 Nuxt 설정을 작성할 때 사용하는 함수다. TypeScript 자동 완성과 타입 확인을 도와준다.

## 무엇을 설정할 수 있을까

`nuxt.config.ts`에서는 다양한 설정을 할 수 있다.

- 개발 도구 사용 여부
- 전역 CSS
- 모듈 등록
- 런타임 설정
- 렌더링 방식
- 라우트 규칙
- 앱 메타 정보
- 빌드 관련 설정

처음부터 모든 설정을 알 필요는 없다. 프로젝트를 만들며 필요한 설정을 하나씩 추가하면 된다.

## devtools

Nuxt DevTools는 개발 중 Nuxt 앱의 상태를 확인할 수 있게 해 주는 도구다.

```ts
export default defineNuxtConfig({
  devtools: { enabled: true }
})
```

라우트, 컴포넌트, imports, modules 같은 정보를 확인할 수 있어 학습할 때도 도움이 된다.

## css

전역 CSS 파일을 등록할 수 있다.

```ts
export default defineNuxtConfig({
  css: ['~/assets/css/main.css']
})
```

이렇게 설정하면 `assets/css/main.css` 파일의 스타일이 앱 전체에 적용된다.

## modules

Nuxt 모듈은 Nuxt에 기능을 추가하는 확장 도구다.

예를 들어 이미지 최적화, 콘텐츠 관리, Tailwind CSS, 다국어 처리 같은 기능을 모듈로 추가할 수 있다.

```ts
export default defineNuxtConfig({
  modules: [
    '@nuxt/image'
  ]
})
```

모듈마다 설치 방법과 설정 방법이 다르므로, 실제 사용할 때는 해당 모듈 문서를 확인해야 한다.

## runtimeConfig

`runtimeConfig`는 실행 환경에서 사용할 설정값을 관리한다.

```ts
export default defineNuxtConfig({
  runtimeConfig: {
    apiSecret: '',
    public: {
      apiBase: '/api'
    }
  }
})
```

`runtimeConfig`에서 `public` 안에 있는 값은 브라우저에서도 접근할 수 있다. 반대로 `public` 밖의 값은 서버에서만 사용하는 비공개 설정으로 생각해야 한다.

API 비밀 키처럼 노출되면 안 되는 값은 `public` 안에 넣으면 안 된다.

## ssr 설정

Nuxt는 기본적으로 서버에서 먼저 HTML을 렌더링한다.

클라이언트 사이드 렌더링만 사용하고 싶다면 다음처럼 설정할 수 있다.

```ts
export default defineNuxtConfig({
  ssr: false
})
```

하지만 처음 Nuxt를 배우는 동안에는 기본값을 유지하는 것이 좋다. Nuxt의 장점인 서버 렌더링 흐름을 익힐 수 있기 때문이다.

## routeRules

`routeRules`는 주소별로 렌더링이나 캐시 전략을 다르게 설정할 때 사용한다.

```ts
export default defineNuxtConfig({
  routeRules: {
    '/': { prerender: true },
    '/admin/**': { ssr: false }
  }
})
```

이 예시는 메인 페이지는 미리 생성하고, 관리자 페이지는 브라우저에서만 렌더링하도록 설정한다.

처음에는 어렵게 느껴질 수 있으므로 "페이지별 전략을 정할 수 있다" 정도만 기억하면 된다.

## 자주 하는 실수

가장 중요한 실수는 비밀 값을 `public` 설정에 넣는 것이다.

```ts
export default defineNuxtConfig({
  runtimeConfig: {
    public: {
      apiSecret: 'secret-value'
    }
  }
})
```

`public` 안의 값은 브라우저로 전달될 수 있다. API 키, DB 비밀번호, 서버 토큰 같은 값은 절대 넣으면 안 된다.

## 정리

`nuxt.config.ts`는 Nuxt 프로젝트의 설정 중심이다. 개발 도구, 전역 CSS, 모듈, 환경 설정, 렌더링 전략 등을 이 파일에서 관리한다.

처음에는 `devtools`, `css`, `modules`, `runtimeConfig` 정도부터 익히면 충분하다.

## 다음 장으로

다음 장에서는 Nuxt 개발 서버와 빌드 명령어의 차이를 배운다.
