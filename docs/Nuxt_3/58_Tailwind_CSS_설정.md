# Tailwind CSS 설정

## 이번 장에서 배울 것

- Tailwind CSS가 무엇인지
- Nuxt에서 Tailwind CSS를 설치하는 방법
- 전역 CSS 파일에 Tailwind를 연결하는 방법
- 유틸리티 클래스 사용 시 주의할 점

## Tailwind CSS란

Tailwind CSS는 유틸리티 클래스 기반 CSS 프레임워크다.

일반 CSS에서는 클래스 이름을 만들고 CSS 파일에 스타일을 작성한다.

```css
.button {
  padding: 8px 16px;
  background: black;
  color: white;
}
```

Tailwind CSS는 미리 준비된 클래스를 HTML에 조합한다.

```vue
<template>
  <button class="bg-black px-4 py-2 text-white">
    저장
  </button>
</template>
```

빠르게 화면을 만들 수 있지만, 클래스가 길어질 수 있으므로 컴포넌트 분리와 규칙이 중요하다.

## 설치하기

Tailwind CSS 공식 Nuxt 가이드는 Vite 플러그인 방식 설치를 안내한다.

```bash
npm install tailwindcss @tailwindcss/vite
```

`nuxt.config.ts`에 Vite 플러그인을 추가한다.

```ts
import tailwindcss from '@tailwindcss/vite'

export default defineNuxtConfig({
  vite: {
    plugins: [
      tailwindcss()
    ]
  }
})
```

## CSS 파일 만들기

전역 CSS 파일을 만든다.

```txt
assets/
  css/
    main.css
```

`assets/css/main.css`

```css
@import "tailwindcss";
```

## Nuxt에 CSS 등록하기

`nuxt.config.ts`에 CSS 파일을 등록한다.

```ts
import tailwindcss from '@tailwindcss/vite'

export default defineNuxtConfig({
  css: ['~/assets/css/main.css'],
  vite: {
    plugins: [
      tailwindcss()
    ]
  }
})
```

이제 Tailwind 클래스를 사용할 수 있다.

## 사용 예시

```vue
<template>
  <section class="mx-auto max-w-3xl px-4 py-10">
    <h1 class="text-3xl font-bold">
      Nuxt와 Tailwind CSS
    </h1>
    <p class="mt-4 text-gray-600">
      유틸리티 클래스로 빠르게 화면을 구성합니다.
    </p>
  </section>
</template>
```

처음에는 간단한 여백, 글자 크기, 색상부터 사용해 보자.

## 반복되는 스타일은 컴포넌트로 분리하기

Tailwind를 쓰다 보면 버튼 클래스가 여러 곳에서 반복될 수 있다.

```vue
<button class="rounded bg-black px-4 py-2 text-white hover:bg-gray-800">
  저장
</button>
```

이런 경우 `BaseButton` 컴포넌트로 분리하면 좋다.

```vue
<template>
  <button class="rounded bg-black px-4 py-2 text-white hover:bg-gray-800">
    <slot />
  </button>
</template>
```

페이지에서는 간단히 사용한다.

```vue
<BaseButton>저장</BaseButton>
```

## Tailwind와 scoped CSS

Tailwind를 사용한다고 CSS를 전혀 쓰지 않는 것은 아니다.

복잡한 애니메이션, 특수한 레이아웃, 서드파티 라이브러리 스타일 조정은 CSS가 더 적합할 수 있다.

Tailwind와 scoped CSS를 함께 사용할 수 있다.

```vue
<template>
  <div class="card">
    <slot />
  </div>
</template>

<style scoped>
.card {
  box-shadow: 0 1px 3px rgb(0 0 0 / 0.12);
}
</style>
```

## 자주 하는 실수

모든 UI를 긴 Tailwind 클래스 한 줄에 몰아넣는 실수를 조심하자.

템플릿이 너무 길어지면 구조를 읽기 어렵다. 반복되는 UI는 컴포넌트로 분리하고, 공통 규칙은 팀 안에서 정리하는 것이 좋다.

또 Tailwind 버전에 따라 설치 방법이 달라질 수 있다. 실제 프로젝트에서는 Tailwind 공식 Nuxt 가이드를 확인해야 한다.

## 정리

Tailwind CSS는 유틸리티 클래스로 빠르게 스타일을 작성하는 도구다. Nuxt에서는 Tailwind Vite 플러그인을 등록하고 전역 CSS에서 `@import "tailwindcss";`를 추가해 사용할 수 있다.

## 다음 장으로

다음 장에서는 여러 언어를 지원하는 i18n 다국어 처리를 배운다.
