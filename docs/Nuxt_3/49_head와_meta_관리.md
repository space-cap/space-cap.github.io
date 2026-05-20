# Head와 Meta 관리

## 이번 장에서 배울 것

- HTML head가 무엇인지
- Nuxt에서 head 정보를 설정하는 방법
- `nuxt.config.ts`와 `useHead`의 차이
- 페이지 제목과 설명을 관리하는 기본 흐름

## head란

HTML 문서에는 `head` 영역이 있다.

`head`는 화면에 직접 보이는 본문은 아니지만, 브라우저와 검색 엔진이 페이지를 이해하는 데 필요한 정보를 담는다.

예를 들어 다음 정보가 들어간다.

- 페이지 제목
- 페이지 설명
- 문자 인코딩
- viewport 설정
- favicon
- Open Graph 이미지
- 외부 CSS
- 검색 엔진 관련 meta 태그

Nuxt에서는 이런 head 정보를 편하게 관리할 수 있다.

## nuxt.config.ts에서 기본 head 설정하기

사이트 전체에서 거의 바뀌지 않는 값은 `nuxt.config.ts`에 설정할 수 있다.

```ts
export default defineNuxtConfig({
  app: {
    head: {
      title: 'Nuxt 학습 사이트',
      htmlAttrs: {
        lang: 'ko'
      },
      link: [
        { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
      ]
    }
  }
})
```

이 방식은 정적인 기본값에 적합하다.

예를 들어 사이트 기본 제목, HTML 언어, favicon처럼 모든 페이지에 공통으로 적용할 값에 사용한다.

## useHead 사용하기

페이지나 컴포넌트에서 동적으로 head를 설정하려면 `useHead`를 사용한다.

```vue
<script setup>
useHead({
  title: '소개',
  meta: [
    {
      name: 'description',
      content: 'Nuxt 학습 사이트 소개 페이지입니다.'
    }
  ]
})
</script>
```

`useHead`는 반응형 값을 사용할 수 있다.

```vue
<script setup>
const title = ref('게시글 목록')

useHead({
  title
})
</script>
```

## titleTemplate

모든 페이지 제목 뒤에 사이트 이름을 붙이고 싶다면 `titleTemplate`을 사용할 수 있다.

`app.vue`

```vue
<script setup>
useHead({
  titleTemplate: (title) => {
    return title ? `${title} - Nuxt 학습 사이트` : 'Nuxt 학습 사이트'
  }
})
</script>

<template>
  <NuxtLayout>
    <NuxtPage />
  </NuxtLayout>
</template>
```

이제 각 페이지에서 `title`만 지정하면 전체 제목 형식이 유지된다.

```vue
<script setup>
useHead({
  title: '게시글'
})
</script>
```

브라우저 제목은 `게시글 - Nuxt 학습 사이트`처럼 표시된다.

## definePageMeta와 함께 쓰기

페이지의 간단한 메타 정보는 `definePageMeta`에 넣을 수도 있다.

```vue
<script setup>
definePageMeta({
  title: '게시글 목록'
})
</script>
```

레이아웃에서 route meta를 읽어 head에 반영할 수 있다.

```vue
<script setup>
const route = useRoute()

useHead({
  title: computed(() => route.meta.title || 'Nuxt 학습 사이트')
})
</script>
```

다만 SEO 태그가 많아지면 페이지에서 `useSeoMeta`를 직접 쓰는 방식이 더 명확하다.

## useHeadSafe

사용자 입력처럼 신뢰할 수 없는 값으로 head를 구성해야 한다면 `useHeadSafe`를 고려한다.

`useHeadSafe`는 위험할 수 있는 head 입력을 제한해 더 안전하게 처리한다.

```vue
<script setup>
const userTitle = ref('사용자 제목')

useHeadSafe({
  title: userTitle
})
</script>
```

초보 단계에서는 "신뢰할 수 없는 입력을 head에 넣을 때는 안전한 API를 고려한다" 정도로 기억하면 된다.

## 자주 하는 실수

모든 페이지에 같은 title과 description을 넣는 실수가 많다.

검색 엔진과 사용자는 페이지마다 다른 정보를 기대한다.

블로그 글, 상품 상세, 게시글 상세처럼 내용이 다른 페이지는 title과 description도 다르게 작성해야 한다.

## 정리

Nuxt에서는 `nuxt.config.ts`의 `app.head`로 정적인 기본 head를 설정하고, `useHead`로 페이지별 동적 head를 관리할 수 있다. `titleTemplate`을 사용하면 사이트 전체 제목 형식을 통일할 수 있다.

## 다음 장으로

다음 장에서는 SEO 태그를 더 안전하고 타입 친화적으로 작성하는 `useSeoMeta`를 배운다.
