# useSeoMeta

## 이번 장에서 배울 것

- `useSeoMeta`가 무엇인지
- SEO 기본 태그와 Open Graph 태그를 작성하는 방법
- `useHead`와 `useSeoMeta`의 차이
- 공유 카드에 필요한 메타 정보를 설정하는 방법

## useSeoMeta란

`useSeoMeta`는 SEO와 소셜 공유에 필요한 meta 태그를 객체 형태로 작성하게 해 주는 Nuxt composable이다.

`useHead`로도 meta 태그를 작성할 수 있다.

```vue
<script setup>
useHead({
  meta: [
    { name: 'description', content: '페이지 설명' },
    { property: 'og:title', content: '공유 제목' }
  ]
})
</script>
```

하지만 meta 태그는 `name`과 `property`를 구분해야 하고, 오타도 자주 난다.

`useSeoMeta`를 사용하면 더 읽기 쉬운 객체 형태로 작성할 수 있다.

```vue
<script setup>
useSeoMeta({
  title: 'Nuxt 학습 사이트',
  description: 'Nuxt 3를 초보자도 쉽게 배우는 문서입니다.',
  ogTitle: 'Nuxt 학습 사이트',
  ogDescription: 'Nuxt 3를 초보자도 쉽게 배우는 문서입니다.',
  ogImage: 'https://example.com/og-image.png',
  twitterCard: 'summary_large_image'
})
</script>
```

## 기본 SEO 태그

가장 먼저 챙길 태그는 제목과 설명이다.

```vue
<script setup>
useSeoMeta({
  title: '게시글 목록',
  description: 'Nuxt 학습 사이트의 게시글 목록입니다.'
})
</script>
```

`title`은 검색 결과와 브라우저 탭에 영향을 준다.

`description`은 검색 결과의 설명 문구로 사용될 수 있다. 검색 엔진이 항상 그대로 사용하는 것은 아니지만, 페이지 내용을 요약하는 중요한 정보다.

## Open Graph 태그

Open Graph는 링크를 소셜 서비스나 메신저에 공유했을 때 표시되는 정보에 영향을 준다.

```vue
<script setup>
useSeoMeta({
  title: 'Nuxt 3 시작하기',
  description: 'Nuxt 3 프로젝트를 만들고 첫 페이지를 작성합니다.',
  ogTitle: 'Nuxt 3 시작하기',
  ogDescription: 'Nuxt 3 프로젝트를 만들고 첫 페이지를 작성합니다.',
  ogImage: 'https://example.com/images/nuxt-start.png',
  ogUrl: 'https://example.com/posts/nuxt-start',
  ogType: 'article'
})
</script>
```

페이지가 공유될 때 제목, 설명, 이미지가 잘 보이려면 Open Graph 정보를 챙기는 것이 좋다.

## Twitter 카드

Twitter 또는 X 같은 서비스에서 큰 이미지 카드로 보이게 하려면 `twitterCard`를 설정한다.

```vue
<script setup>
useSeoMeta({
  twitterCard: 'summary_large_image',
  twitterTitle: 'Nuxt 3 시작하기',
  twitterDescription: 'Nuxt 3 프로젝트를 만들고 첫 페이지를 작성합니다.',
  twitterImage: 'https://example.com/images/nuxt-start.png'
})
</script>
```

이미지는 절대 URL을 사용하는 것이 안전하다.

## useHead와 useSeoMeta의 차이

둘은 경쟁 관계가 아니다.

| 구분 | useHead | useSeoMeta |
| --- | --- | --- |
| 목적 | head 전체 관리 | SEO meta 태그 관리 |
| 예시 | title, link, script, bodyAttrs | description, ogTitle, twitterCard |
| 장점 | 자유도가 높음 | SEO 태그 작성이 간결함 |

외부 CSS, favicon, body class 같은 것은 `useHead`가 어울린다.

SEO meta 태그는 `useSeoMeta`가 더 읽기 쉽다.

## 동적 데이터와 함께 쓰기

게시글 상세 페이지처럼 데이터에 따라 meta 정보가 달라질 수 있다.

```vue
<script setup>
const route = useRoute()

const { data: post } = await useFetch(`/api/posts/${route.params.id}`)

useSeoMeta({
  title: () => post.value?.title || '게시글',
  description: () => post.value?.description || '게시글 상세 페이지',
  ogTitle: () => post.value?.title || '게시글',
  ogDescription: () => post.value?.description || '게시글 상세 페이지',
  ogImage: () => post.value?.image || 'https://example.com/default-og.png'
})
</script>
```

반응형 함수 형태로 작성하면 데이터가 준비된 뒤 meta 정보도 갱신될 수 있다.

## 자주 하는 실수

`ogImage`에 상대 경로를 넣는 실수가 많다.

```ts
ogImage: '/images/og.png'
```

서비스에 따라 상대 경로를 제대로 해석하지 못할 수 있다. 공유 이미지에는 가능한 절대 URL을 사용한다.

또 모든 페이지에 같은 description을 넣으면 검색 결과에서 페이지별 차이가 드러나지 않는다.

## 정리

`useSeoMeta`는 SEO와 소셜 공유 meta 태그를 객체 형태로 안전하게 작성하는 composable이다. `useHead`는 head 전체 관리에, `useSeoMeta`는 SEO 관련 태그에 사용하면 역할이 깔끔해진다.

## 다음 장으로

다음 장에서는 게시글 상세 페이지처럼 동적 데이터가 있는 페이지에서 SEO를 적용하는 방법을 배운다.
