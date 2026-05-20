# Nuxt Content 입문

## 이번 장에서 배울 것

- Nuxt Content가 무엇인지
- Markdown 파일을 콘텐츠로 관리하는 방법
- collection을 정의하고 조회하는 방법
- `<ContentRenderer>`로 Markdown을 렌더링하는 방법

## Nuxt Content란

Nuxt Content는 Markdown, YAML, JSON 같은 파일 기반 콘텐츠를 Nuxt 앱에서 쉽게 다룰 수 있게 해 주는 모듈이다.

블로그, 문서 사이트, 가이드, 포트폴리오 글처럼 파일로 관리하기 좋은 콘텐츠에 잘 어울린다.

Nuxt Content를 사용하면 다음 흐름을 만들 수 있다.

- `content` 폴더에 Markdown 파일 작성
- frontmatter로 제목, 설명, 날짜 같은 메타 정보 작성
- `queryCollection`으로 콘텐츠 조회
- `<ContentRenderer>`로 Markdown 렌더링

## 설치하기

Nuxt Content를 설치한다.

```bash
npx nuxi@latest module add content
```

설치 후 `nuxt.config.ts`에 모듈이 등록된다.

```ts
export default defineNuxtConfig({
  modules: ['@nuxt/content']
})
```

## Markdown 파일 만들기

`content/blog/hello-nuxt.md` 파일을 만든다.

```md
---
title: 'Nuxt Content 시작하기'
description: 'Markdown으로 블로그 글을 작성합니다.'
date: '2026-05-20'
---

# Nuxt Content 시작하기

Nuxt Content를 사용하면 Markdown 파일을 콘텐츠로 관리할 수 있습니다.
```

`---` 사이의 영역을 frontmatter라고 부른다. 제목, 설명, 날짜 같은 메타 정보를 넣는다.

## collection 정의하기

Nuxt Content v3에서는 `content.config.ts`에서 collection을 정의할 수 있다.

```ts
import { defineCollection, defineContentConfig } from '@nuxt/content'
import { z } from 'zod'

export default defineContentConfig({
  collections: {
    blog: defineCollection({
      type: 'page',
      source: 'blog/*.md',
      schema: z.object({
        title: z.string(),
        description: z.string(),
        date: z.string()
      })
    })
  }
})
```

이 설정은 `content/blog/*.md` 파일을 `blog` collection으로 다루겠다는 뜻이다.

## 콘텐츠 목록 조회하기

`pages/blog/index.vue`

```vue
<script setup>
const { data: posts } = await useAsyncData('blog-posts', () => {
  return queryCollection('blog')
    .order('date', 'DESC')
    .all()
})
</script>

<template>
  <main>
    <h1>블로그</h1>

    <ul>
      <li v-for="post in posts" :key="post.path">
        <NuxtLink :to="post.path">
          {{ post.title }}
        </NuxtLink>
      </li>
    </ul>
  </main>
</template>
```

`queryCollection('blog')`으로 blog collection의 문서를 조회한다.

## 콘텐츠 상세 렌더링

`pages/blog/[slug].vue`

```vue
<script setup>
const route = useRoute()
const slug = route.params.slug

const { data: post } = await useAsyncData(`blog-${slug}`, () => {
  return queryCollection('blog')
    .path(`/blog/${slug}`)
    .first()
})

if (!post.value) {
  throw createError({
    statusCode: 404,
    statusMessage: '글을 찾을 수 없습니다.'
  })
}
</script>

<template>
  <main>
    <ContentRenderer :value="post" />
  </main>
</template>
```

`<ContentRenderer>`는 조회한 Markdown 문서를 HTML로 렌더링한다.

## frontmatter와 SEO

Markdown의 frontmatter는 SEO 정보로도 활용할 수 있다.

```vue
<script setup>
useSeoMeta({
  title: () => post.value?.title || '블로그',
  description: () => post.value?.description || '블로그 글입니다.'
})
</script>
```

글마다 title과 description을 다르게 설정할 수 있다.

## Nuxt Content v3 주의점

Nuxt Content v3에서는 Markdown 파일이 자동으로 페이지가 되는 방식에만 의존하지 않는다.

일반적으로 `pages` 안에 목록 페이지와 상세 페이지를 만들고, 그 페이지에서 `queryCollection`으로 콘텐츠를 조회해 렌더링한다.

즉, 콘텐츠 파일과 페이지 라우팅을 분리해서 생각하는 것이 좋다.

## 자주 하는 실수

Markdown 파일을 `content` 폴더에 넣기만 하면 자동으로 모든 페이지가 생긴다고 생각하는 실수가 있다.

Nuxt Content v3에서는 collection 정의와 페이지 렌더링 구조를 함께 만들어야 한다.

또 frontmatter의 필드 이름이 collection schema와 맞지 않으면 조회나 타입 처리에서 문제가 생길 수 있다.

## 정리

Nuxt Content는 Markdown 기반 콘텐츠를 Nuxt 앱에서 조회하고 렌더링할 수 있게 해 주는 모듈이다. `content.config.ts`에서 collection을 정의하고, `queryCollection`으로 조회하며, `<ContentRenderer>`로 화면에 표시한다.

## 다음 장으로

다음 장에서는 Nuxt Content를 사용해 실제 블로그 구조를 만드는 방법을 배운다.
