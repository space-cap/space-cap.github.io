# HTTP 메서드와 API 설계

## 이번 장에서 배울 것

- GET, POST, PUT, DELETE의 기본 의미
- Nuxt 서버 API에서 메서드별 파일을 만드는 방법
- REST 스타일 API의 기본 흐름
- 초보자가 지켜야 할 API 설계 습관

## HTTP 메서드란

HTTP 메서드는 클라이언트가 서버에 어떤 일을 요청하는지 나타내는 방식이다.

대표적인 메서드는 다음과 같다.

| 메서드 | 의미 | 예시 |
| --- | --- | --- |
| GET | 조회 | 게시글 목록 가져오기 |
| POST | 생성 | 새 게시글 작성 |
| PUT | 전체 수정 | 게시글 전체 수정 |
| PATCH | 일부 수정 | 게시글 제목만 수정 |
| DELETE | 삭제 | 게시글 삭제 |

API를 만들 때 메서드를 적절히 사용하면 주소만 보고도 의도를 이해하기 쉬워진다.

## Nuxt의 메서드 파일 이름

Nuxt 서버 API는 파일 이름에 HTTP 메서드를 붙일 수 있다.

```txt
server/api/posts.get.ts
server/api/posts.post.ts
```

위 구조는 같은 `/api/posts` 주소를 사용하지만 메서드에 따라 다른 파일이 실행된다.

```txt
GET  /api/posts -> posts.get.ts
POST /api/posts -> posts.post.ts
```

다른 메서드로 요청하면 405 Method Not Allowed 오류가 날 수 있다.

## 게시글 목록 조회

`server/api/posts.get.ts`

```ts
export default defineEventHandler(() => {
  return [
    { id: 1, title: '첫 번째 글' },
    { id: 2, title: '두 번째 글' }
  ]
})
```

페이지에서 호출한다.

```vue
<script setup>
const { data: posts } = await useFetch('/api/posts', {
  default: () => []
})
</script>
```

## 게시글 생성

`server/api/posts.post.ts`

```ts
export default defineEventHandler(async (event) => {
  const body = await readBody(event)

  return {
    id: Date.now(),
    title: body.title
  }
})
```

클라이언트에서 호출한다.

```vue
<script setup>
const createPost = async () => {
  const post = await $fetch('/api/posts', {
    method: 'POST',
    body: {
      title: '새 게시글'
    }
  })

  console.log(post)
}
</script>
```

`readBody`는 요청 body를 읽는 함수다. 보통 POST, PUT, PATCH 같은 요청에서 사용한다.

## 동적 API 경로

게시글 상세 조회는 ID가 필요하다.

```txt
server/api/posts/[id].get.ts
```

```ts
export default defineEventHandler((event) => {
  const id = getRouterParam(event, 'id')

  return {
    id,
    title: `${id}번 게시글`
  }
})
```

이 파일은 다음 주소를 처리한다.

```txt
GET /api/posts/1
GET /api/posts/2
```

## REST 스타일 주소 예시

게시글 API를 REST 스타일로 설계하면 다음과 같다.

| 기능 | 메서드와 주소 | 파일 |
| --- | --- | --- |
| 목록 조회 | `GET /api/posts` | `posts.get.ts` |
| 생성 | `POST /api/posts` | `posts.post.ts` |
| 상세 조회 | `GET /api/posts/:id` | `posts/[id].get.ts` |
| 수정 | `PUT /api/posts/:id` | `posts/[id].put.ts` |
| 삭제 | `DELETE /api/posts/:id` | `posts/[id].delete.ts` |

이 구조를 사용하면 API가 많아져도 규칙을 유지하기 쉽다.

## 초보자를 위한 설계 습관

처음에는 다음 규칙을 지키는 것만으로도 충분하다.

- 조회는 GET
- 생성은 POST
- 수정은 PUT 또는 PATCH
- 삭제는 DELETE
- 같은 자원은 같은 주소를 사용하고 메서드로 구분
- 응답은 가능한 한 일관된 구조로 반환

## 자주 하는 실수

GET 요청에서 body를 읽으려고 하는 실수를 조심해야 한다.

GET 요청은 보통 query string으로 조건을 전달한다.

```txt
/api/posts?page=1&q=nuxt
```

body가 필요한 요청은 POST, PUT, PATCH를 사용한다.

## 정리

HTTP 메서드는 API 요청의 의도를 나타낸다. Nuxt에서는 `posts.get.ts`, `posts.post.ts`처럼 파일 이름에 메서드를 붙여 같은 주소의 API를 메서드별로 나눌 수 있다.

## 다음 장으로

다음 장에서는 query, body, params, status code처럼 요청과 응답을 더 자세히 다루는 방법을 배운다.
