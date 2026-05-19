# Provide와 Inject

## 이번 장에서 배울 것

Provide와 Inject는 부모 계층의 컴포넌트가 깊은 자식 컴포넌트에게 데이터를 전달하는 방법이다.

일반적으로 가까운 부모와 자식 사이에는 Props와 Emit을 사용한다. 하지만 컴포넌트가 여러 단계로 깊어지면 props를 중간 컴포넌트에 계속 전달해야 하는 불편함이 생긴다.

---

## Props Drilling 문제

아래 구조를 생각해보자.

```text
App.vue
└─ Layout.vue
   └─ Sidebar.vue
      └─ UserMenu.vue
```

`App.vue`의 `currentUser`가 `UserMenu.vue`에 필요하다면 중간 컴포넌트들이 모두 props를 받아 다시 내려줘야 한다.

```text
App -> Layout -> Sidebar -> UserMenu
```

이렇게 중간 컴포넌트가 실제로 쓰지 않는 값을 계속 전달하는 문제를 props drilling이라고 부른다.

---

## provide 기본

상위 컴포넌트에서 값을 제공한다.

```vue
<script setup>
import { provide, ref } from 'vue'

const currentUser = ref({
  name: 'Kim',
  role: 'admin'
})

provide('currentUser', currentUser)
</script>
```

첫 번째 인자는 key, 두 번째 인자는 제공할 값이다.

---

## inject 기본

깊은 자식 컴포넌트에서 값을 주입받는다.

```vue
<script setup>
import { inject } from 'vue'

const currentUser = inject('currentUser')
</script>

<template>
  <p>{{ currentUser.name }}</p>
</template>
```

`currentUser`가 ref라면 템플릿에서는 자동으로 풀려 보인다.

---

## 기본값 설정

상위에서 provide하지 않은 경우를 대비해 기본값을 줄 수 있다.

```js
const theme = inject('theme', 'light')
```

객체 기본값도 가능하다.

```js
const settings = inject('settings', {
  language: 'ko'
})
```

---

## 반응형 값 제공하기

`ref`나 `reactive`를 provide하면 자식에서도 반응형으로 사용할 수 있다.

```vue
<script setup>
import { provide, ref } from 'vue'

const theme = ref('light')

function toggleTheme() {
  theme.value = theme.value === 'light' ? 'dark' : 'light'
}

provide('theme', {
  theme,
  toggleTheme
})
</script>
```

자식:

```vue
<script setup>
import { inject } from 'vue'

const themeContext = inject('theme')
</script>

<template>
  <p>현재 테마: {{ themeContext.theme }}</p>
  <button @click="themeContext.toggleTheme">테마 변경</button>
</template>
```

값과 변경 함수를 함께 제공하면 변경 위치를 명확하게 관리할 수 있다.

---

## Symbol key 사용하기

문자열 key는 이름이 겹칠 수 있다. 큰 프로젝트에서는 Symbol을 사용하기도 한다.

`keys.js`

```js
export const themeKey = Symbol('theme')
```

제공:

```js
import { themeKey } from './keys'

provide(themeKey, themeContext)
```

주입:

```js
import { themeKey } from './keys'

const themeContext = inject(themeKey)
```

초보 단계에서는 문자열 key로 시작해도 충분하다.

---

## 언제 사용할까?

Provide/Inject는 다음 상황에 적합하다.

- 테마 정보
- 현재 로그인 사용자
- 폼 그룹과 내부 입력 컴포넌트의 공유 상태
- 탭, 메뉴, 아코디언 같은 복합 컴포넌트 내부 상태
- 깊은 컴포넌트 트리에서 여러 자식이 공통으로 쓰는 값

앱 전체의 복잡한 상태 관리는 나중에 배울 Pinia가 더 적합할 수 있다.

---

## Props와 Provide/Inject 비교

| 구분 | Props | Provide/Inject |
| --- | --- | --- |
| 관계 | 가까운 부모-자식 | 깊은 자식까지 |
| 명시성 | 템플릿에 드러남 | 코드 내부에서 주입 |
| 추천 용도 | 일반 데이터 전달 | 깊은 공통 데이터 |
| 남용 위험 | 낮음 | 의존 관계가 숨겨질 수 있음 |

기본은 Props다. 중간 전달이 너무 많아질 때 Provide/Inject를 고려한다.

---

## 자주 하는 실수

### 모든 상태를 provide로 처리함

Provide/Inject는 편하지만 의존성이 숨겨질 수 있다. 가까운 컴포넌트 관계는 Props와 Emit이 더 명확하다.

### inject 결과가 없을 수 있음을 고려하지 않음

상위에서 provide하지 않으면 `undefined`가 될 수 있다. 기본값을 주거나 존재 여부를 확인한다.

### 자식에서 제공받은 상태를 아무 곳에서나 직접 수정함

가능하면 변경 함수도 함께 provide해서 수정 경로를 명확히 한다.

---

## 작은 실습

테마 예제를 만들어보자.

- `App.vue`에서 `theme` ref를 만든다.
- `provide('theme', { theme, toggleTheme })`로 제공한다.
- 깊은 자식 `ThemeButton.vue`에서 inject한다.
- 버튼을 누르면 light/dark가 전환된다.

---

## 이번 장 요약

- Provide/Inject는 깊은 자식에게 데이터를 전달하는 방법이다.
- 상위 컴포넌트는 `provide`, 하위 컴포넌트는 `inject`를 사용한다.
- 반응형 값을 제공하면 자식도 변경을 반영받는다.
- 기본 데이터 전달은 Props와 Emit을 먼저 고려한다.

---

## 다음 장으로

다음 장에서는 필요한 시점에 컴포넌트를 불러오는 비동기 컴포넌트를 배운다.

