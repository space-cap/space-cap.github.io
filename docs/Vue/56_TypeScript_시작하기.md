# TypeScript 시작하기

## 이번 장에서 배울 것

TypeScript는 JavaScript에 타입 시스템을 더한 언어다. Vue는 TypeScript로 작성되어 있고, 공식 패키지도 타입 선언을 함께 제공한다.

이번 장에서는 Vue 프로젝트에서 TypeScript를 왜 쓰는지, 어떻게 시작하는지, 초보자가 무엇부터 익히면 좋은지 배운다.

---

## TypeScript를 쓰는 이유

TypeScript는 코드를 실행하기 전에 많은 실수를 찾아준다.

예:

- 문자열이어야 하는 값에 숫자를 넣음
- 존재하지 않는 속성에 접근함
- 함수 인자를 잘못 전달함
- 리팩터링 중 이름 변경을 빠뜨림

Vue 프로젝트가 커질수록 타입은 자동 완성, 문서화, 리팩터링 안정성에 큰 도움을 준다.

---

## 새 프로젝트에서 TypeScript 선택하기

Vue 공식 프로젝트 생성 도구인 `create-vue`에서 TypeScript 옵션을 선택할 수 있다.

```bash
npm create vue@latest
```

질문 중 TypeScript를 사용할지 묻는 항목에서 `Yes`를 선택한다.

이미 JavaScript로 학습 중이라면 처음부터 TypeScript를 억지로 넣을 필요는 없다. 기본 Vue 문법에 익숙해진 뒤 TypeScript를 추가로 배우면 된다.

---

## SFC에서 TypeScript 사용하기

`.vue` 파일에서 TypeScript를 사용하려면 `<script setup>`에 `lang="ts"`를 붙인다.

```vue
<script setup lang="ts">
const message: string = '안녕하세요 TypeScript'
</script>

<template>
  <p>{{ message }}</p>
</template>
```

`lang="ts"`를 붙이면 script 영역과 template 표현식에서 더 나은 타입 확인과 자동 완성을 받을 수 있다.

---

## 기본 타입

자주 쓰는 타입은 다음과 같다.

```ts
const name: string = 'Kim'
const age: number = 20
const active: boolean = true
const tags: string[] = ['vue', 'typescript']
```

객체 타입은 `type` 또는 `interface`로 만들 수 있다.

```ts
type User = {
  id: number
  name: string
  email: string
}

const user: User = {
  id: 1,
  name: 'Kim',
  email: 'kim@example.com'
}
```

---

## ref 타입

대부분은 TypeScript가 타입을 추론한다.

```ts
import { ref } from 'vue'

const count = ref(0)
```

`count.value`는 number로 추론된다.

초기값이 `null`인 경우에는 명시적으로 타입을 적는 것이 좋다.

```ts
type User = {
  id: number
  name: string
}

const user = ref<User | null>(null)
```

`User | null`은 User이거나 null일 수 있다는 뜻이다.

---

## reactive 타입

```ts
import { reactive } from 'vue'

type Form = {
  email: string
  password: string
}

const form = reactive<Form>({
  email: '',
  password: ''
})
```

객체 상태에는 타입을 붙이면 속성 누락이나 오타를 빨리 찾을 수 있다.

---

## vue-tsc

Vite 개발 서버는 빠른 개발을 위해 TypeScript 타입 검사를 별도 단계로 처리하는 흐름을 권장한다. Vue SFC 타입 검사는 `vue-tsc`를 사용할 수 있다.

```bash
npm install -D vue-tsc typescript
```

`package.json`:

```json
{
  "scripts": {
    "type-check": "vue-tsc --build"
  }
}
```

실행:

```bash
npm run type-check
```

`create-vue`로 TypeScript 프로젝트를 만들면 관련 설정이 이미 들어 있을 수 있다.

---

## IDE 설정

VS Code에서는 `Vue - Official` 확장을 사용한다. 예전 Vue 2 시절에 많이 쓰던 Vetur는 Vue 3 프로젝트에서는 비활성화하는 것이 좋다.

확인할 것:

- VS Code 설치
- `Vue - Official` 확장 설치
- TypeScript 프로젝트라면 `npm run type-check` 실행 가능
- `.vue` 파일에서 자동 완성과 타입 오류 표시 확인

---

## 언제 TypeScript를 시작하면 좋을까?

처음 Vue를 배우는 단계에서는 JavaScript로 흐름을 익히는 것도 괜찮다.

TypeScript는 다음 시점에 특히 좋다.

- 컴포넌트 Props가 많아진다.
- API 응답 구조를 명확히 관리해야 한다.
- 여러 사람이 함께 개발한다.
- Pinia store가 커진다.
- 리팩터링이 잦다.

---

## 자주 하는 실수

### 모든 곳에 타입을 과하게 적음

TypeScript는 추론을 잘한다. 명확한 초기값이 있으면 굳이 타입을 반복해서 적지 않아도 된다.

```ts
const count = ref(0)
```

### any로 모든 문제를 덮음

`any`는 타입 검사를 포기하는 것에 가깝다. 정말 필요한 경우가 아니라면 구체적인 타입을 작성한다.

### 타입 오류를 실행 오류와 혼동함

타입 오류는 실행 전의 경고다. 빨간 줄이 보이면 "귀찮은 오류"가 아니라 잠재 버그를 미리 알려주는 신호로 보자.

---

## 작은 실습

`UserProfile.vue`를 TypeScript로 작성해보자.

- `<script setup lang="ts">` 사용
- `User` 타입 선언
- `const user = ref<User | null>(null)` 작성
- user가 있을 때만 이름과 이메일 출력

---

## 이번 장 요약

- TypeScript는 JavaScript에 타입 시스템을 더한 언어다.
- Vue는 TypeScript를 공식적으로 잘 지원한다.
- SFC에서는 `<script setup lang="ts">`를 사용한다.
- Vue SFC 타입 검사는 `vue-tsc`를 사용할 수 있다.
- 초보자는 기본 Vue 흐름을 익힌 뒤 TypeScript를 단계적으로 도입해도 된다.

---

## 다음 장으로

다음 장에서는 Props와 Emit에 타입을 적용하는 방법을 배운다.

