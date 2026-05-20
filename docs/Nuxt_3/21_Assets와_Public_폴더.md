# Assets와 Public 폴더

## 이번 장에서 배울 것

- `assets` 폴더와 `public` 폴더의 차이
- 이미지와 CSS 파일을 어디에 두면 좋은지
- 정적 파일을 URL로 접근하는 방법

## 두 폴더의 차이

Nuxt에서 이미지, CSS, 폰트 같은 파일을 다룰 때 자주 만나는 폴더가 두 개 있다.

- `assets`
- `public`

둘 다 파일을 넣는 폴더지만 역할이 다르다.

간단히 말하면 다음과 같다.

- `public`: 그대로 공개할 파일
- `assets`: 빌드 도구가 처리할 파일

## public 폴더

`public` 폴더의 파일은 서버 루트에서 그대로 제공된다.

```txt
public/
  favicon.ico
  robots.txt
  img/
    logo.png
```

`public/img/logo.png` 파일은 브라우저에서 다음 주소로 접근할 수 있다.

```txt
/img/logo.png
```

컴포넌트에서는 이렇게 사용할 수 있다.

```vue
<template>
  <img src="/img/logo.png" alt="사이트 로고" />
</template>
```

파일 이름을 그대로 유지해야 하거나, 특정 URL로 직접 접근해야 하는 파일은 `public`에 둔다.

예를 들어 다음 파일들이 `public`에 잘 어울린다.

- `favicon.ico`
- `robots.txt`
- `sitemap.xml`
- 공개용 OG 이미지
- 다운로드용 정적 파일

## assets 폴더

`assets` 폴더는 빌드 도구가 처리할 파일을 넣는 곳이다.

```txt
assets/
  css/
    main.css
  images/
    hero.png
```

`assets` 안의 파일은 `/assets/hero.png` 같은 고정 URL로 바로 접근하는 용도가 아니다. Nuxt와 빌드 도구가 파일을 처리하고, 필요하면 최적화하거나 이름을 바꿔 결과물에 포함한다.

컴포넌트에서 이미지 파일을 참조할 수 있다.

```vue
<template>
  <img src="~/assets/images/hero.png" alt="메인 이미지" />
</template>
```

전역 CSS 파일도 보통 `assets`에 둔다.

```txt
assets/
  css/
    main.css
```

## 전역 CSS 등록하기

`assets/css/main.css` 파일을 만들고 `nuxt.config.ts`에 등록한다.

```ts
export default defineNuxtConfig({
  css: ['~/assets/css/main.css']
})
```

이렇게 등록한 CSS는 앱 전체에 적용된다.

## public과 assets 선택 기준

처음에는 다음 기준으로 선택하면 된다.

| 상황 | 추천 폴더 |
| --- | --- |
| `/robots.txt`처럼 정해진 URL이 필요하다 | `public` |
| `/img/logo.png`처럼 직접 URL로 접근하고 싶다 | `public` |
| CSS 파일을 Nuxt 빌드에 포함하고 싶다 | `assets` |
| 컴포넌트에서 import하듯 이미지나 스타일을 쓰고 싶다 | `assets` |
| 파일 이름을 빌드 도구가 처리해도 괜찮다 | `assets` |

## 폰트 파일

폰트를 직접 제공해야 한다면 `public/fonts`에 두고 CSS에서 사용할 수 있다.

```txt
public/
  fonts/
    Pretendard.woff2
```

```css
@font-face {
  font-family: 'Pretendard';
  src: url('/fonts/Pretendard.woff2') format('woff2');
  font-weight: 400;
  font-style: normal;
  font-display: swap;
}
```

## 자주 하는 실수

`assets` 폴더에 넣은 파일을 `/assets/...` 주소로 직접 접근하려는 실수를 자주 한다.

`assets`는 빌드 도구가 처리하는 폴더다. 브라우저에서 정해진 주소로 직접 접근해야 하는 파일은 `public`에 넣는 것이 맞다.

## 정리

`public` 폴더의 파일은 루트 URL에서 그대로 제공된다. `assets` 폴더의 파일은 Nuxt의 빌드 도구가 처리한다. 정해진 URL이 필요한 파일은 `public`, 빌드에 포함해 관리할 파일은 `assets`에 둔다.

## 다음 장으로

다음 장에서는 Nuxt에서 CSS를 작성하고 관리하는 여러 스타일링 전략을 배운다.
