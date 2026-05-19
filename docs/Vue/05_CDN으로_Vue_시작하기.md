# CDN으로 Vue 시작하기

## 이번 장에서 배울 것

이번 장에서는 프로젝트 생성 없이 HTML 파일 하나로 Vue를 실행한다. 이 방식은 Vue의 가장 기본적인 동작을 빠르게 확인하기 좋다.

CDN 방식은 설치가 간단하지만, `.vue` 단일 파일 컴포넌트나 복잡한 빌드 설정은 사용할 수 없다. 따라서 학습 초반에 감을 잡는 용도로 사용하고, 실제 프로젝트는 다음 장에서 Vite로 만든다.

---

## CDN이란?

CDN은 Content Delivery Network의 줄임말이다. 여기서는 인터넷 주소를 통해 Vue 파일을 가져와 사용하는 방식이라고 이해하면 충분하다.

HTML 파일에 다음 한 줄을 넣으면 Vue를 사용할 수 있다.

```html
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
```

이 방식은 별도 설치가 필요 없어서 빠르게 실험하기 좋다.

---

## 첫 번째 HTML 파일 만들기

`hello-vue.html` 파일을 만들고 아래 코드를 작성한다.

```html
<!doctype html>
<html lang="ko">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vue CDN 시작하기</title>
  </head>
  <body>
    <div id="app">
      <h1>{{ message }}</h1>
    </div>

    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <script>
      const { createApp } = Vue

      createApp({
        data() {
          return {
            message: '안녕하세요 Vue!'
          }
        }
      }).mount('#app')
    </script>
  </body>
</html>
```

브라우저로 파일을 열면 `안녕하세요 Vue!`가 보인다.

---

## 코드 이해하기

### `div id="app"`

```html
<div id="app">
  <h1>{{ message }}</h1>
</div>
```

이 영역은 Vue가 관리할 화면이다. Vue 앱은 이 영역 안에서 동작한다.

### `{{ message }}`

`{{ }}`는 Vue의 보간법이다. JavaScript 값을 화면에 표시할 때 사용한다.

```html
<h1>{{ message }}</h1>
```

`message` 값이 바뀌면 화면도 바뀐다.

### `createApp`

```js
const { createApp } = Vue
```

`createApp`은 Vue 앱을 만드는 함수다.

### `data`

```js
data() {
  return {
    message: '안녕하세요 Vue!'
  }
}
```

`data`는 화면에서 사용할 상태를 반환한다. 여기서는 `message`라는 값을 만들었다.

### `mount`

```js
}).mount('#app')
```

`mount`는 Vue 앱을 실제 HTML 요소에 연결한다. `#app`은 `id="app"`인 요소를 뜻한다.

---

## 버튼 클릭 예제

이번에는 버튼을 누를 때 숫자가 증가하는 예제를 만든다.

```html
<!doctype html>
<html lang="ko">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vue 카운터</title>
  </head>
  <body>
    <div id="app">
      <p>현재 숫자: {{ count }}</p>
      <button type="button" @click="count++">1 증가</button>
    </div>

    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <script>
      const { createApp } = Vue

      createApp({
        data() {
          return {
            count: 0
          }
        }
      }).mount('#app')
    </script>
  </body>
</html>
```

`@click`은 클릭 이벤트를 처리하는 Vue 문법이다.

```html
<button type="button" @click="count++">1 증가</button>
```

버튼을 누르면 `count` 값이 증가하고, Vue가 화면을 자동으로 다시 보여준다.

---

## 입력 값 연결하기

`v-model`을 사용하면 입력 값과 상태를 쉽게 연결할 수 있다.

```html
<!doctype html>
<html lang="ko">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vue 입력 예제</title>
  </head>
  <body>
    <div id="app">
      <input v-model="name" placeholder="이름을 입력하세요">
      <p>안녕하세요, {{ name }}님!</p>
    </div>

    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <script>
      const { createApp } = Vue

      createApp({
        data() {
          return {
            name: ''
          }
        }
      }).mount('#app')
    </script>
  </body>
</html>
```

입력 칸에 글자를 입력하면 아래 문장도 함께 바뀐다.

---

## CDN 방식의 장점과 한계

| 구분 | 내용 |
| --- | --- |
| 장점 | 설치 없이 HTML 파일 하나로 시작할 수 있다. |
| 장점 | 기존 서버 페이지에 Vue 기능을 일부만 붙이기 쉽다. |
| 한계 | `.vue` 단일 파일 컴포넌트를 사용할 수 없다. |
| 한계 | 큰 프로젝트 구조를 만들기 어렵다. |
| 한계 | 빌드 최적화와 개발 도구 활용이 제한된다. |

초보자는 CDN 방식으로 Vue의 감각을 잡은 뒤, 곧바로 Vite 프로젝트 방식으로 넘어가는 것이 좋다.

---

## 자주 하는 실수

### Vue script를 app 코드보다 아래에 두지 않은 경우

Vue를 사용하기 전에 Vue 파일이 먼저 로드되어야 한다.

```html
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script>
  const { createApp } = Vue
</script>
```

### mount 대상이 없는 경우

`mount('#app')`를 사용했다면 HTML에 `id="app"`인 요소가 있어야 한다.

```html
<div id="app"></div>
```

### 중괄호를 잘못 쓴 경우

Vue 보간법은 중괄호 두 개를 사용한다.

```html
{{ message }}
```

---

## 작은 실습

카운터 예제를 다음처럼 바꾸어보자.

- `1 증가` 버튼 추가
- `1 감소` 버튼 추가
- `0으로 초기화` 버튼 추가

힌트:

```html
<button type="button" @click="count--">1 감소</button>
<button type="button" @click="count = 0">0으로 초기화</button>
```

---

## 이번 장 요약

- CDN을 사용하면 HTML 파일 하나로 Vue를 실행할 수 있다.
- `createApp`은 Vue 앱을 만든다.
- `mount`는 Vue 앱을 HTML 요소에 연결한다.
- `{{ }}`는 값을 화면에 표시한다.
- `@click`은 클릭 이벤트를 처리한다.
- `v-model`은 입력 값과 상태를 연결한다.

---

## 다음 장으로

다음 장에서는 Vue 공식 권장 흐름에 따라 `npm create vue@latest`로 Vite 기반 프로젝트를 만든다.

