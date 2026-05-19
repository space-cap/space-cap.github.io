# 학습 전에 알아야 할 HTML, CSS, JavaScript

## 이번 장에서 배울 것

Vue는 HTML, CSS, JavaScript 위에서 동작한다. Vue를 배우기 전에 모든 웹 기술을 완벽히 알아야 하는 것은 아니지만, 최소한의 기본기는 필요하다.

이번 장에서는 Vue 학습에 바로 필요한 웹 기초를 빠르게 정리한다.

---

## HTML에서 알아야 할 것

HTML은 화면의 구조를 만든다. Vue 템플릿도 HTML과 비슷하게 작성하므로 HTML 기본 태그를 알고 있어야 한다.

### 자주 쓰는 태그

| 태그 | 의미 |
| --- | --- |
| `div` | 의미가 특별히 없는 영역 |
| `header` | 상단 영역 |
| `main` | 주요 내용 영역 |
| `section` | 문서의 한 구역 |
| `h1`~`h6` | 제목 |
| `p` | 문단 |
| `ul`, `li` | 목록 |
| `button` | 버튼 |
| `input` | 입력 칸 |
| `form` | 입력 양식 |

### 속성

HTML 태그에는 속성을 붙일 수 있다.

```html
<button type="button" disabled>저장</button>
<input type="text" placeholder="이름을 입력하세요">
```

Vue에서는 속성을 동적으로 바꾸는 일이 많다.

```html
<button :disabled="isSaving">저장</button>
```

여기서 `:disabled`는 Vue 문법이다. `isSaving` 값이 참이면 버튼이 비활성화된다.

---

## CSS에서 알아야 할 것

CSS는 화면의 모양을 꾸민다.

```css
.button {
  padding: 8px 12px;
  border: 1px solid #ddd;
  background: white;
}
```

Vue에서는 일반 CSS도 사용할 수 있고, 특정 컴포넌트에만 적용되는 `scoped` 스타일도 사용할 수 있다.

```vue
<style scoped>
.button {
  color: #2563eb;
}
</style>
```

`scoped`를 붙이면 해당 컴포넌트 안의 요소에만 스타일이 적용된다. 큰 프로젝트에서 스타일 충돌을 줄이는 데 도움이 된다.

---

## JavaScript에서 꼭 알아야 할 것

Vue를 배우는 데 가장 중요한 기초는 JavaScript다. 아래 항목은 반드시 익숙해져야 한다.

### 변수

```js
const name = 'Vue'
let count = 0

count = count + 1
```

`const`는 다시 할당하지 않는 값, `let`은 다시 할당할 수 있는 값에 사용한다.

### 객체

```js
const user = {
  name: 'Kim',
  age: 20
}

console.log(user.name)
```

Vue에서는 화면 상태를 객체로 다루는 일이 많다.

### 배열

```js
const todos = ['Vue 공부', '예제 실행', '정리하기']

console.log(todos[0])
```

목록 화면을 만들 때 배열은 거의 항상 등장한다.

### 함수

```js
function add(a, b) {
  return a + b
}

const result = add(1, 2)
```

Vue에서는 버튼 클릭, 폼 제출, API 호출 같은 동작을 함수로 작성한다.

### 화살표 함수

```js
const add = (a, b) => {
  return a + b
}
```

짧게 쓰면 다음과 같다.

```js
const add = (a, b) => a + b
```

### 배열 메서드

Vue에서 목록을 다룰 때 자주 쓰는 배열 메서드다.

```js
const numbers = [1, 2, 3]

const doubled = numbers.map((number) => number * 2)
const evenNumbers = numbers.filter((number) => number % 2 === 0)
const found = numbers.find((number) => number === 2)
```

| 메서드 | 역할 |
| --- | --- |
| `map` | 배열의 각 값을 바꾸어 새 배열을 만든다. |
| `filter` | 조건에 맞는 값만 모아 새 배열을 만든다. |
| `find` | 조건에 맞는 첫 번째 값을 찾는다. |
| `some` | 조건을 만족하는 값이 하나라도 있는지 확인한다. |
| `every` | 모든 값이 조건을 만족하는지 확인한다. |

---

## DOM과 이벤트

DOM은 JavaScript가 HTML 문서를 다룰 수 있게 만든 구조다.

기존 JavaScript에서는 버튼 클릭을 다음처럼 처리한다.

```html
<button id="saveButton">저장</button>
```

```js
const button = document.querySelector('#saveButton')

button.addEventListener('click', () => {
  console.log('저장 버튼 클릭')
})
```

Vue에서는 더 짧게 작성할 수 있다.

```html
<button @click="save">저장</button>
```

```js
function save() {
  console.log('저장 버튼 클릭')
}
```

Vue가 DOM 연결을 도와주기 때문에 개발자는 이벤트가 발생했을 때 실행할 동작에 집중하면 된다.

---

## 비동기 처리

실무에서는 서버에서 데이터를 가져오는 일이 많다. 이때 비동기 처리를 사용한다.

```js
async function loadPosts() {
  const response = await fetch('/api/posts')
  const posts = await response.json()
  return posts
}
```

지금은 `async`, `await`, `fetch`가 서버 데이터를 가져올 때 자주 쓰인다는 정도만 기억해도 된다. 자세한 내용은 API 통신 장에서 다시 다룬다.

---

## 모듈

Vue 프로젝트에서는 파일을 나누고 필요한 값을 가져오는 일이 많다.

```js
import { ref } from 'vue'
```

```js
export function formatPrice(value) {
  return value.toLocaleString() + '원'
}
```

`import`는 다른 파일이나 패키지에서 기능을 가져올 때 사용하고, `export`는 현재 파일의 값을 밖으로 내보낼 때 사용한다.

---

## Vue를 배우기 전 체크리스트

아래 항목이 너무 낯설다면 Vue 문서를 읽으면서 동시에 JavaScript 기초를 보강하면 된다.

- HTML 태그를 읽을 수 있다.
- CSS class를 사용할 수 있다.
- `const`, `let` 차이를 안다.
- 객체와 배열을 만들 수 있다.
- 함수를 만들고 호출할 수 있다.
- 클릭 이벤트가 무엇인지 안다.
- `map`, `filter` 같은 배열 메서드를 본 적이 있다.
- `async`, `await`가 비동기 처리와 관련 있다는 정도는 안다.

---

## 작은 실습

아래 JavaScript 코드를 읽고 결과를 예상해보자.

```js
const todos = [
  { id: 1, title: 'Vue 설치하기', done: true },
  { id: 2, title: '컴포넌트 배우기', done: false },
  { id: 3, title: 'Todo 앱 만들기', done: false }
]

const unfinishedTodos = todos.filter((todo) => !todo.done)

console.log(unfinishedTodos)
```

`done`이 `false`인 항목만 남는다. Vue에서 Todo 목록을 만들 때 이런 코드가 자주 등장한다.

---

## 이번 장 요약

- Vue는 HTML, CSS, JavaScript 위에서 동작한다.
- HTML 구조, CSS class, JavaScript 변수와 함수는 꼭 알아야 한다.
- 배열과 객체는 Vue 화면 상태를 다룰 때 자주 사용한다.
- 이벤트와 비동기 처리는 Vue 실습에서 계속 등장한다.

---

## 다음 장으로

다음 장에서는 Vue 개발을 위한 Node.js, VS Code, 브라우저 개발자 도구 환경을 준비한다.

