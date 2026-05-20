# TypeScript 기초

## 이번 장에서 배울 것

- TypeScript가 무엇인지
- Nuxt가 TypeScript를 어떻게 지원하는지
- `.ts`와 `<script setup lang="ts">` 사용 방법
- `nuxi typecheck`로 타입 검사하는 방법

## TypeScript란

TypeScript는 JavaScript에 타입을 추가한 언어다.

JavaScript에서는 변수에 어떤 값이 들어갈지 실행 전에는 알기 어렵다.

```js
function greet(name) {
  return `안녕하세요, ${name}`
}
```

TypeScript에서는 타입을 적어 의도를 더 분명히 할 수 있다.

```ts
function greet(name: string) {
  return `안녕하세요, ${name}`
}
```

이제 `name`에는 문자열이 들어와야 한다는 것을 도구가 알 수 있다.

## Nuxt와 TypeScript

Nuxt 3는 TypeScript를 기본적으로 잘 지원한다.

프로젝트를 만들면 다음 파일들이 TypeScript로 생성되는 경우가 많다.

```txt
nuxt.config.ts
server/api/hello.get.ts
composables/useCounter.ts
```

Vue 파일에서도 TypeScript를 사용할 수 있다.

```vue
<script setup lang="ts">
const count = ref<number>(0)
</script>
```

`lang="ts"`를 붙이면 `<script setup>` 안에서 TypeScript 문법을 사용할 수 있다.

## 타입 추론

TypeScript는 많은 경우 타입을 자동으로 추론한다.

```ts
const count = ref(0)
```

이 코드는 `count`가 숫자 ref라는 것을 TypeScript가 추론할 수 있다.

항상 타입을 직접 적을 필요는 없다. 타입이 분명하면 추론을 활용하고, 애매하거나 중요한 경계에는 타입을 적는 것이 좋다.

## 명시적 타입

객체 구조가 중요하다면 타입을 직접 만들 수 있다.

```ts
type User = {
  id: number
  name: string
  email: string
}

const user = ref<User | null>(null)
```

`User | null`은 user 값이 사용자 객체이거나 null일 수 있다는 뜻이다.

## 함수 파라미터 타입

함수의 입력과 반환 타입을 지정할 수 있다.

```ts
const formatPrice = (price: number): string => {
  return `${price.toLocaleString()}원`
}
```

숫자가 아닌 값을 넣으면 타입 오류가 난다.

```ts
formatPrice('1000')
```

이런 오류를 실행 전에 발견할 수 있다는 점이 TypeScript의 장점이다.

## Nuxt 타입 생성

Nuxt는 자동 import, components, runtime config 같은 타입을 `.nuxt` 폴더에 생성한다.

개발 서버를 실행하면 Nuxt가 필요한 타입을 준비한다.

```bash
npm run dev
```

또는 prepare 명령을 사용할 수 있다.

```bash
npx nuxt prepare
```

타입 자동 완성이 이상하면 개발 서버를 재시작하거나 `nuxt prepare`를 실행해 볼 수 있다.

## 타입 검사하기

Nuxt는 타입 검사를 위한 명령을 제공한다.

```bash
npx nuxt typecheck
```

`package.json`에 script를 추가해도 좋다.

```json
{
  "scripts": {
    "typecheck": "nuxt typecheck"
  }
}
```

실행한다.

```bash
npm run typecheck
```

빌드 전에 타입 검사를 실행하면 많은 실수를 미리 잡을 수 있다.

## any를 조심하기

`any`는 어떤 타입이든 허용한다.

```ts
const user: any = {}
```

편해 보이지만 TypeScript의 장점을 잃게 된다.

정말 필요한 경우가 아니라면 `any`보다 구체적인 타입을 쓰는 것이 좋다.

## 자주 하는 실수

TypeScript를 처음 배우면 모든 값에 타입을 다 적으려고 하다가 지치기 쉽다.

처음에는 다음 기준으로 시작하자.

- 함수 파라미터에는 타입을 적는다.
- API 응답에는 타입을 만든다.
- 상태가 null일 수 있으면 `| null`을 적는다.
- 자동 추론이 잘 되는 값은 그대로 둔다.

## 정리

TypeScript는 JavaScript에 타입을 더해 실행 전 오류를 줄여 준다. Nuxt 3는 TypeScript를 기본적으로 지원하며, `<script setup lang="ts">`, `.ts` 파일, `nuxi typecheck`를 활용할 수 있다.

## 다음 장으로

다음 장에서는 Nuxt에서 API 응답, composable, runtime config에 타입을 적용하는 방법을 배운다.
