# 실습 프로젝트 6: 풀스택 Nuxt 앱

## 이번 프로젝트에서 만들 것

Nuxt의 프론트엔드와 서버 API를 함께 사용해 작은 풀스택 앱을 만든다.

이 프로젝트에서 연습할 내용은 다음과 같다.

- 기능별 폴더 구조
- 서버 API 설계
- DB 연동 구조 설계
- API 레이어
- 인증과 권한
- 배포 전 체크리스트

## 목표 앱

작은 메모 앱을 만든다고 가정한다.

```txt
/notes
  내 메모 목록

/notes/new
  메모 작성

/notes/1
  메모 상세
```

로그인한 사용자만 자신의 메모를 볼 수 있다.

## 전체 구조

```txt
components/
  notes/
    NoteCard.vue

composables/
  useAuth.ts
  useNoteApi.ts

pages/
  notes/
    index.vue
    new.vue
    [id].vue

server/
  api/
    notes/
      index.get.ts
      index.post.ts
      [id].get.ts
      [id].put.ts
      [id].delete.ts
  utils/
    requireUser.ts
    db.ts

types/
  note.ts
```

처음에는 이 구조를 기준으로 파일을 채워 간다.

## 타입 정의

`types/note.ts`

```ts
export type Note = {
  id: number
  title: string
  content: string
  userId: number
  createdAt: string
}
```

프론트엔드와 API 레이어에서 함께 사용할 수 있다.

## DB 연결 위치

DB 연결은 서버 전용 코드다.

따라서 `server/utils/db.ts`에 둔다.

```ts
export const db = {
  notes: []
}
```

처음에는 학습용 메모리 DB처럼 시작할 수 있다.

실제 프로젝트에서는 SQLite, PostgreSQL, MySQL 같은 DB와 Prisma, Drizzle, Kysely 같은 도구를 선택할 수 있다.

중요한 원칙은 DB 연결 코드가 클라이언트 번들에 들어가면 안 된다는 것이다. 반드시 server 영역에 둔다.

## 사용자 확인 유틸

`server/utils/requireUser.ts`

```ts
export const requireUser = (event) => {
  const session = getCookie(event, 'session')

  if (session !== 'demo-session') {
    throw createError({
      statusCode: 401,
      statusMessage: '로그인이 필요합니다.'
    })
  }

  return {
    id: 1,
    name: '홍길동'
  }
}
```

실제 서비스에서는 세션 ID를 바탕으로 DB에서 사용자를 조회한다.

## 메모 목록 API

`server/api/notes/index.get.ts`

```ts
export default defineEventHandler((event) => {
  const user = requireUser(event)

  return db.notes.filter((note) => note.userId === user.id)
})
```

사용자별 데이터는 반드시 서버에서 필터링해야 한다.

## 메모 작성 API

`server/api/notes/index.post.ts`

```ts
export default defineEventHandler(async (event) => {
  const user = requireUser(event)
  const body = await readBody(event)

  if (!body.title || !body.content) {
    throw createError({
      statusCode: 400,
      statusMessage: '제목과 내용은 필수입니다.'
    })
  }

  const note = {
    id: Date.now(),
    title: body.title,
    content: body.content,
    userId: user.id,
    createdAt: new Date().toISOString()
  }

  db.notes.unshift(note)

  setResponseStatus(event, 201)

  return note
})
```

## API 레이어

`composables/useNoteApi.ts`

```ts
import type { Note } from '~/types/note'

export const useNoteApi = () => {
  const getNotes = () => {
    return $fetch<Note[]>('/api/notes')
  }

  const createNote = (body: { title: string; content: string }) => {
    return $fetch<Note>('/api/notes', {
      method: 'POST',
      body
    })
  }

  return {
    getNotes,
    createNote
  }
}
```

페이지에서는 API 경로를 직접 반복하지 않는다.

## 메모 목록 페이지

`pages/notes/index.vue`

```vue
<script setup lang="ts">
definePageMeta({
  middleware: 'auth'
})

const noteApi = useNoteApi()

const { data: notes, status, error } = await useAsyncData('notes', () => {
  return noteApi.getNotes()
}, {
  default: () => []
})
</script>

<template>
  <main>
    <h1>내 메모</h1>
    <NuxtLink to="/notes/new">새 메모</NuxtLink>

    <p v-if="status === 'pending'">불러오는 중...</p>
    <p v-else-if="error">메모를 불러오지 못했습니다.</p>
    <p v-else-if="notes.length === 0">아직 메모가 없습니다.</p>

    <NoteCard
      v-for="note in notes"
      v-else
      :key="note.id"
      :note="note"
    />
  </main>
</template>
```

## 메모 작성 페이지

`pages/notes/new.vue`

```vue
<script setup lang="ts">
definePageMeta({
  middleware: 'auth'
})

const noteApi = useNoteApi()
const title = ref('')
const content = ref('')
const errorMessage = ref('')

const submit = async () => {
  errorMessage.value = ''

  try {
    const note = await noteApi.createNote({
      title: title.value,
      content: content.value
    })

    await navigateTo(`/notes/${note.id}`)
  } catch {
    errorMessage.value = '메모 저장에 실패했습니다.'
  }
}
</script>
```

## DB 도입 시 고려할 점

실제 DB를 도입할 때는 다음을 확인한다.

- DB 연결 정보는 서버 전용 runtime config에 둔다.
- DB 비밀번호를 `public`에 넣지 않는다.
- 서버 API에서 입력값을 검증한다.
- 사용자별 데이터는 서버에서 권한을 확인한다.
- 마이그레이션 전략을 정한다.
- 배포 플랫폼에서 DB 연결 방식을 확인한다.

## 배포 전 체크리스트

- `npm run lint`
- `npm run typecheck`
- `npm run test:run`
- `npm run build`
- 환경 변수 등록 확인
- 서버 API 권한 검증 확인
- 비밀 값 public 노출 여부 확인
- 배포 플랫폼이 서버 기능을 지원하는지 확인

## 정리

풀스택 Nuxt 앱은 페이지, 컴포넌트, composable, server API, DB 연결이 함께 움직인다. 중요한 것은 클라이언트와 서버 경계를 지키고, 인증과 권한 검증을 서버에서 수행하는 것이다.

## 다음 장으로

다음 장에서는 전체 Nuxt 학습을 마무리하고 앞으로의 학습 로드맵을 정리한다.
