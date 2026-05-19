# Render Function과 JSX 입문

## 이번 장에서 배울 것

Vue에서는 대부분 템플릿을 사용해 화면을 만든다. 하지만 아주 동적인 UI나 렌더링 로직을 JavaScript로 직접 작성해야 할 때 Render Function을 사용할 수 있다.

JSX는 JavaScript 안에서 HTML과 비슷한 문법으로 UI를 작성하는 방식이다.

---

## 대부분은 템플릿으로 충분하다

Vue의 기본 권장 흐름은 템플릿이다.

```vue
<template>
  <button @click="count++">{{ count }}</button>
</template>
```

템플릿은 읽기 쉽고, Vue 컴파일러가 최적화하기 좋으며, 초보자에게 가장 자연스럽다.

Render Function과 JSX는 필요한 상황에서만 사용한다.

---

## Render Function이란?

Render Function은 화면 구조를 반환하는 JavaScript 함수다.

Vue에서는 `h` 함수를 사용해 vnode를 만든다.

```js
import { h } from 'vue'

export default {
  render() {
    return h('div', { class: 'box' }, '안녕하세요')
  }
}
```

이 코드는 대략 다음 템플릿과 비슷하다.

```html
<div class="box">안녕하세요</div>
```

---

## h 함수 기본

`h`는 보통 세 가지 인자를 받는다.

```js
h(type, props, children)
```

예:

```js
h('button', { onClick: handleClick }, '저장')
```

| 인자 | 의미 |
| --- | --- |
| `type` | 태그 이름 또는 컴포넌트 |
| `props` | 속성, class, 이벤트 |
| `children` | 자식 내용 |

---

## setup에서 render 함수 반환하기

```js
import { h, ref } from 'vue'

export default {
  setup() {
    const count = ref(0)

    return () =>
      h('button', {
        onClick: () => count.value++
      }, count.value)
  }
}
```

이런 방식은 템플릿보다 낯설지만, 렌더링 구조를 함수로 조합해야 할 때 유용하다.

---

## 컴포넌트 렌더링

`h`의 첫 번째 인자로 컴포넌트를 넣을 수 있다.

```js
import { h } from 'vue'
import BaseButton from './BaseButton.vue'

export default {
  setup() {
    return () =>
      h(BaseButton, {
        type: 'button'
      }, {
        default: () => '저장'
      })
  }
}
```

Slot은 함수 형태로 전달한다.

---

## JSX란?

JSX는 JavaScript 안에서 XML/HTML과 비슷한 문법을 사용하는 방식이다.

```jsx
export default {
  setup() {
    const count = ref(0)

    return () => (
      <button onClick={() => count.value++}>
        {count.value}
      </button>
    )
  }
}
```

Vue에서 JSX를 사용하려면 별도 설정이 필요할 수 있다. 새로 배우는 단계에서는 "이런 방식도 있다" 정도로 이해하면 된다.

---

## 언제 사용할까?

Render Function/JSX는 다음 상황에서 고려한다.

- 매우 동적인 컴포넌트 생성
- renderless 컴포넌트나 고급 UI 라이브러리 작성
- slot 조합이 복잡한 컴포넌트
- 템플릿보다 JavaScript 로직으로 표현하는 것이 명확한 경우

일반 화면, 폼, 목록, 페이지는 템플릿이 더 적합하다.

---

## 템플릿과 비교

| 구분 | 템플릿 | Render Function/JSX |
| --- | --- | --- |
| 읽기 쉬움 | 높음 | 익숙해져야 함 |
| 초보자 추천 | 기본 선택 | 필요할 때만 |
| 동적 조합 | 충분히 가능 | 매우 복잡한 경우 유리 |
| Vue 컴파일러 최적화 | 좋음 | 작성자 책임 증가 |

---

## 자주 하는 실수

### 모든 것을 JSX로 바꾸려 함

Vue는 템플릿이 강력하다. 특별한 이유가 없다면 템플릿을 사용한다.

### h 함수 이벤트 이름을 헷갈림

Render Function에서는 이벤트를 `onClick`처럼 props로 전달한다.

```js
h('button', { onClick: handleClick }, '확인')
```

### Slot 전달 방식을 헷갈림

컴포넌트 자식 slot은 함수로 전달하는 경우가 많다.

---

## 작은 실습

간단한 `RenderButton.js`를 만들어보자.

- `h` 함수 사용
- 버튼 렌더링
- 클릭하면 count 증가
- 템플릿으로 작성한 버튼과 비교

---

## 이번 장 요약

- Vue에서는 대부분 템플릿을 사용하면 된다.
- Render Function은 JavaScript 함수로 화면 구조를 만드는 방식이다.
- `h(type, props, children)`로 vnode를 만든다.
- JSX는 JavaScript 안에서 HTML과 비슷한 문법으로 UI를 작성하는 방식이다.
- 일반 앱 개발자는 먼저 템플릿을 충분히 익히는 것이 좋다.

---

## 다음 장으로

다음 장에서는 Vue와 Web Components의 관계를 배운다.

