# 동적 페이지 SEO

## 이번 장에서 배울 것

- 동적 페이지에서 SEO가 어려운 이유
- 게시글 상세 페이지의 title과 description 만들기
- 없는 데이터는 404로 처리하는 방법
- canonical URL과 공유 이미지 기본값

## 동적 페이지란

동적 페이지는 주소의 일부나 데이터에 따라 내용이 바뀌는 페이지다.

예를 들어 게시글 상세 페이지는 다음처럼 주소마다 다른 내용을 보여 준다.

```txt
/posts/1
/posts/2
/posts/hello-nuxt
```

이런 페이지는 각 게시글마다 제목, 설명, 공유 이미지가 달라야 한다.

## 기본 구조

`pages/posts/[slug].vue` 파일을 생각해 보자.

```vue
<script setup>
const route = useRoute()
const slug = route.params.slug

const { data: post } = await useFetch(`/api/posts/${slug}`)
</script>
```

이제 `post` 데이터에 따라 SEO 정보를 설정해야 한다.

## 게시글 SEO 설정

```vue
<script setup>
const route = useRoute()
const slug = route.params.slug

const { data: post, error } = await useFetch(`/api/posts/${slug}`)

if (error.value || !post.value) {
  throw createError({
    statusCode: 404,
    statusMessage: '게시글을 찾을 수 없습니다.'
  })
}

useSeoMeta({
  title: () => post.value.title,
  description: () => post.value.description,
  ogTitle: () => post.value.title,
  ogDescription: () => post.value.description,
  ogImage: () => post.value.ogImage || 'https://example.com/default-og.png',
  ogType: 'article'
})
</script>
```

각 게시글마다 다른 meta 정보가 설정된다.

## 404 처리가 중요한 이유

없는 게시글이 정상 페이지처럼 200 상태 코드로 응답되면 검색 엔진이 잘못된 페이지를 수집할 수 있다.

없는 데이터는 명확히 404로 처리해야 한다.

```ts
throw createError({
  statusCode: 404,
  statusMessage: '게시글을 찾을 수 없습니다.'
})
```

사용자에게도 "글을 찾을 수 없다"는 명확한 화면을 보여 줄 수 있다.

## canonical URL

같은 콘텐츠가 여러 주소로 접근될 수 있다면 canonical URL을 설정할 수 있다.

```vue
<script setup>
const route = useRoute()
const url = `https://example.com${route.path}`

useHead({
  link: [
    {
      rel: 'canonical',
      href: url
    }
  ]
})
</script>
```

canonical URL은 검색 엔진에 "이 페이지의 대표 주소는 이것"이라고 알려 주는 역할을 한다.

## 기본 공유 이미지

모든 게시글에 개별 공유 이미지가 없을 수 있다.

이럴 때는 기본 이미지를 준비한다.

```ts
useSeoMeta({
  ogImage: () => post.value?.ogImage || 'https://example.com/default-og.png'
})
```

기본 이미지에는 사이트 이름이나 콘텐츠 카테고리를 담아 두면 공유했을 때 어색하지 않다.

## 제목 길이

SEO 제목은 너무 길면 검색 결과에서 잘릴 수 있다.

초보 단계에서는 다음 정도를 기준으로 생각하면 좋다.

- 페이지 핵심 키워드를 앞쪽에 둔다.
- 불필요한 반복 문구를 줄인다.
- 사이트 이름은 `titleTemplate`로 붙인다.

예를 들어 다음처럼 구성할 수 있다.

```txt
Nuxt useFetch 쉽게 이해하기 - Nuxt 학습 사이트
```

## 설명 문구 작성

description은 페이지 내용을 한두 문장으로 요약한다.

좋은 설명은 다음 조건을 가진다.

- 페이지 내용을 정확히 설명한다.
- 너무 짧거나 길지 않다.
- 클릭을 유도하되 과장하지 않는다.
- 모든 페이지에서 중복되지 않는다.

## 자주 하는 실수

동적 페이지에서 데이터가 없을 때도 기본 title만 보여 주고 200 응답을 보내는 실수가 많다.

검색 엔진 입장에서는 빈 페이지도 정상 페이지로 보일 수 있다. 데이터가 없는 상세 페이지는 404로 처리하는 습관이 중요하다.

## 정리

동적 페이지는 데이터에 따라 SEO 정보를 다르게 설정해야 한다. 게시글 상세 페이지에서는 title, description, Open Graph 이미지, canonical URL, 404 처리를 함께 고려해야 한다.

## 다음 장으로

다음 장에서는 Nuxt Image를 이용해 이미지 최적화를 적용하는 방법을 배운다.
