# 디버깅과 Vue DevTools

## 이번 장에서 배울 것

디버깅은 문제가 어디서 생겼는지 찾아 고치는 과정이다. Vue 앱에서는 브라우저 개발자 도구와 Vue DevTools를 함께 사용하면 컴포넌트 상태, props, 이벤트, Pinia store를 더 쉽게 확인할 수 있다.

---

## 먼저 확인할 것

문제가 생기면 다음 순서로 확인해보자.

1. 브라우저 Console에 에러가 있는가
2. Network 탭에서 API 요청이 성공했는가
3. Vue DevTools에서 컴포넌트 상태가 기대와 같은가
4. Props와 emit 이벤트가 올바르게 오가는가
5. Pinia store 값이 바뀌는가
6. 라우터 경로와 params/query가 맞는가

디버깅은 추측보다 확인이 중요하다.

---

## Vue DevTools 설치

Vue DevTools는 브라우저 확장 또는 독립 실행 도구 형태로 사용할 수 있다.

보통은 Chrome 또는 Edge 확장으로 설치해 시작하면 된다.

Vue 앱을 열고 개발자 도구를 보면 Vue 관련 패널이 표시된다.

---

## Components 패널

Components 패널에서는 컴포넌트 트리를 볼 수 있다.

확인할 수 있는 것:

- 현재 렌더링된 컴포넌트 구조
- 선택한 컴포넌트의 props
- 선택한 컴포넌트의 상태
- computed 값
- inject된 값

컴포넌트가 화면에 보이지 않는다면 먼저 트리에 존재하는지 확인한다.

---

## Props 확인

부모에서 자식으로 값이 제대로 전달되는지 확인할 때 유용하다.

예:

- `TodoItem`에 `todo` prop이 들어왔는가
- `UserCard`의 `name` 값이 기대와 같은가
- Boolean prop이 문자열로 전달되지 않았는가

템플릿에서 `active="false"`라고 쓰면 문자열 `"false"`가 될 수 있다. 동적 값은 `:active="false"`처럼 작성해야 한다.

---

## Pinia Store 확인

Pinia를 사용하면 Vue DevTools에서 store 상태를 확인할 수 있다.

확인할 것:

- 로그인 상태가 바뀌었는가
- 장바구니 items가 추가되었는가
- getter 값이 기대와 같은가
- action이 호출되었는가

Store 상태가 바뀌지 않는다면 action이 호출되는지부터 확인한다.

---

## Timeline 활용

Vue DevTools의 Timeline에서는 컴포넌트 업데이트, 이벤트, Pinia 변경 등을 시간 순서로 볼 수 있다.

사용자 동작 후 어떤 상태가 바뀌었는지 추적할 때 도움이 된다.

예:

- 버튼 클릭
- emit 발생
- Pinia action 실행
- 컴포넌트 업데이트

---

## 브라우저 Console

Console은 JavaScript 에러와 로그를 확인하는 곳이다.

```js
console.log('현재 값:', value)
console.error(error)
```

임시 확인에는 `console.log`가 유용하지만, 작업이 끝난 뒤 불필요한 로그는 정리하는 것이 좋다.

---

## Network 탭

API 문제가 의심되면 Network 탭을 본다.

확인할 것:

- 요청 URL이 맞는가
- HTTP 상태 코드가 200대인가
- 요청 payload가 맞는가
- 응답 데이터 구조가 기대와 같은가
- CORS 에러가 있는가
- Authorization 헤더가 들어갔는가

화면 문제가 아니라 API 응답이 잘못된 경우도 많다.

---

## Sources와 breakpoint

복잡한 로직은 `console.log`보다 breakpoint가 더 좋을 수 있다.

브라우저 개발자 도구의 Sources 탭에서 코드 줄에 breakpoint를 걸고 실행을 멈출 수 있다.

확인할 수 있는 것:

- 현재 변수 값
- 함수 호출 순서
- 조건문 분기
- 비동기 함수 흐름

---

## Vue에서 자주 보는 문제

### ref의 .value를 빼먹음

```js
count++
```

```js
count.value++
```

### props를 직접 수정함

자식에서 props를 직접 바꾸기보다 emit으로 부모에게 알린다.

### v-for key가 불안정함

index 대신 고유 id를 사용한다.

### API 응답 구조를 잘못 예상함

Network 탭에서 실제 응답을 확인한다.

### 라우터 params가 문자열임을 잊음

`route.params.id`는 보통 문자열이다. 숫자가 필요하면 변환한다.

---

## 디버깅 습관

좋은 디버깅은 문제를 작게 나누는 것이다.

- 데이터가 들어왔는가
- 상태가 바뀌었는가
- computed가 계산되는가
- 템플릿 조건이 참인가
- 컴포넌트가 렌더링되는가
- CSS로 숨겨진 것은 아닌가

한 번에 모든 것을 의심하지 말고 흐름을 따라 확인한다.

---

## 작은 실습

Todo 앱에서 일부러 버그를 만들어보자.

- 삭제 버튼이 동작하지 않게 만든다.
- Vue DevTools에서 emit이 발생하는지 확인한다.
- 부모 함수가 호출되는지 확인한다.
- todos 배열이 바뀌는지 확인한다.
- 화면 조건과 key를 확인한다.

---

## 이번 장 요약

- 디버깅은 추측보다 확인이 중요하다.
- Vue DevTools로 컴포넌트 트리, props, 상태, Pinia store를 볼 수 있다.
- Console과 Network 탭은 에러와 API 문제를 확인하는 기본 도구다.
- breakpoint를 사용하면 복잡한 로직 흐름을 단계별로 볼 수 있다.
- Vue 특유의 문제는 ref, props, key, route params에서 자주 발생한다.

---

## 다음 장으로

다음 장부터는 TypeScript 구간이다. 먼저 Vue에서 TypeScript를 왜 쓰는지와 시작 방법을 배운다.

