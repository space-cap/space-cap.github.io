# Nuxt 프로젝트 폴더 구조

## 이번 장에서 배울 것

- Nuxt 프로젝트의 주요 폴더
- 각 폴더에 어떤 파일을 넣는지
- 초보자가 먼저 알아야 할 구조

## 폴더 구조를 알아야 하는 이유

Nuxt는 약속된 폴더 이름을 많이 사용한다. 특정 폴더에 파일을 넣으면 Nuxt가 자동으로 기능을 연결한다.

예를 들어 `pages` 폴더에 파일을 만들면 라우트가 생기고, `components` 폴더에 컴포넌트를 만들면 자동으로 사용할 수 있다.

처음에는 이 자동 동작이 신기하면서도 헷갈릴 수 있다. 그래서 폴더의 역할을 먼저 정리해 두는 것이 좋다.

## 기본 구조 예시

Nuxt 프로젝트는 보통 다음과 같은 구조로 커진다.

```txt
my-nuxt-app/
  assets/
  components/
  composables/
  layouts/
  middleware/
  pages/
  plugins/
  public/
  server/
  app.vue
  nuxt.config.ts
  package.json
```

프로젝트를 처음 만들었을 때 모든 폴더가 있는 것은 아니다. 필요한 시점에 직접 만들면 된다.

## app.vue

`app.vue`는 Nuxt 앱의 가장 바깥 컴포넌트다.

페이지 기능을 사용한다면 보통 다음처럼 작성한다.

```vue
<template>
  <NuxtPage />
</template>
```

공통 레이아웃을 사용할 때도 `app.vue`가 중요한 출발점이 된다.

## pages

`pages` 폴더는 페이지를 만드는 곳이다.

```txt
pages/
  index.vue
  about.vue
```

이 구조는 다음 주소와 연결된다.

```txt
/        -> pages/index.vue
/about   -> pages/about.vue
```

Nuxt 라우팅의 핵심이므로 가장 먼저 익숙해져야 하는 폴더다.

## components

`components` 폴더는 재사용 가능한 화면 조각을 넣는 곳이다.

```txt
components/
  AppHeader.vue
  AppFooter.vue
  PostCard.vue
```

Nuxt는 이 폴더의 컴포넌트를 자동으로 등록한다. 그래서 많은 경우 별도 import 없이 템플릿에서 바로 사용할 수 있다.

## layouts

`layouts` 폴더는 여러 페이지에서 공통으로 사용하는 화면 틀을 넣는 곳이다.

예를 들어 모든 페이지에 헤더와 푸터가 필요하다면 레이아웃으로 분리할 수 있다.

```txt
layouts/
  default.vue
  admin.vue
```

레이아웃은 블로그, 관리자, 로그인 화면처럼 화면 구조가 다른 영역을 나눌 때 유용하다.

## composables

`composables` 폴더는 재사용 가능한 로직을 넣는 곳이다.

```txt
composables/
  useCounter.ts
  useAuth.ts
```

예를 들어 로그인 상태 확인, API 호출 로직, 공통 상태 처리 등을 composable로 만들 수 있다.

## plugins

`plugins` 폴더는 앱이 시작될 때 등록해야 하는 기능을 넣는 곳이다.

예를 들어 외부 라이브러리를 Nuxt 앱에 연결하거나, 전역으로 사용할 값을 주입할 때 사용한다.

처음에는 자주 쓰지 않아도 되지만, UI 라이브러리나 인증 라이브러리를 붙일 때 만나게 된다.

## server

`server` 폴더는 서버 쪽 코드를 넣는 곳이다.

```txt
server/
  api/
    hello.get.ts
```

`server/api/hello.get.ts` 파일을 만들면 `/api/hello` 주소로 접근할 수 있는 API가 생긴다.

Nuxt 3의 중요한 특징 중 하나이므로 뒤에서 자세히 다룬다.

## public과 assets

`public` 폴더는 그대로 제공할 정적 파일을 넣는 곳이다.

```txt
public/
  favicon.ico
  robots.txt
```

`assets` 폴더는 빌드 과정에서 처리할 이미지, CSS, 폰트 등을 넣는 곳이다.

간단히 말하면 주소로 그대로 접근해야 하는 파일은 `public`, 빌드 도구가 처리해 주면 좋은 파일은 `assets`에 둔다.

## 정리

Nuxt는 폴더 이름에 의미가 있다. `pages`, `components`, `layouts`, `composables`, `plugins`, `server` 같은 폴더는 Nuxt가 자동으로 인식한다.

처음에는 `app.vue`, `pages`, `components`, `layouts`, `server`만 잘 알아도 충분하다.

## 다음 장으로

다음 장에서는 Nuxt의 설정 파일인 `nuxt.config`를 살펴본다.
