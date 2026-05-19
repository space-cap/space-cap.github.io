# Teleport

## 이번 장에서 배울 것

`Teleport`는 컴포넌트의 템플릿 일부를 현재 위치가 아니라 DOM의 다른 위치에 렌더링하는 Vue 내장 컴포넌트다.

모달, 팝오버, 토스트처럼 화면 위에 떠야 하는 UI에서 자주 사용한다.

---

## 왜 필요할까?

모달을 컴포넌트 안에 작성하면 HTML 구조상 깊은 곳에 위치할 수 있다.

```text
App
└─ Page
   └─ Card
      └─ Modal
```

이때 부모 요소의 `overflow`, `z-index`, `position` 때문에 모달이 잘리거나 뒤에 깔릴 수 있다.

`Teleport`를 사용하면 모달 코드는 컴포넌트 안에 두면서 실제 렌더링 위치는 `body` 아래로 보낼 수 있다.

---

## 기본 사용법

```vue
<Teleport to="body">
  <div class="modal">
    모달 내용
  </div>
</Teleport>
```

`to="body"`는 이 내용을 `document.body` 아래에 렌더링하겠다는 뜻이다.

---

## 모달 예제

```vue
<script setup>
import { ref } from 'vue'

const open = ref(false)
</script>

<template>
  <button @click="open = true">모달 열기</button>

  <Teleport to="body">
    <div v-if="open" class="modal-backdrop">
      <section class="modal">
        <h2>모달 제목</h2>
        <p>이 모달은 body 아래에 렌더링됩니다.</p>
        <button @click="open = false">닫기</button>
      </section>
    </div>
  </Teleport>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  display: grid;
  place-items: center;
  background: rgba(0, 0, 0, 0.45);
}

.modal {
  width: min(420px, calc(100vw - 32px));
  padding: 20px;
  background: white;
  border-radius: 8px;
}
</style>
```

코드는 현재 컴포넌트에 있지만, 실제 DOM 위치는 `body` 아래가 된다.

---

## to 대상

`to`에는 CSS 선택자를 사용할 수 있다.

```vue
<Teleport to="#modals">
  <AppModal />
</Teleport>
```

이 경우 `index.html`에 대상 요소가 있어야 한다.

```html
<div id="app"></div>
<div id="modals"></div>
```

대상이 없으면 렌더링 문제가 생길 수 있다.

---

## disabled

상황에 따라 Teleport를 끌 수 있다.

```vue
<Teleport to="body" :disabled="isMobile">
  <div class="panel">패널</div>
</Teleport>
```

`disabled`가 참이면 원래 위치에 렌더링된다.

---

## 여러 Teleport

여러 `Teleport`가 같은 대상으로 이동할 수 있다.

```vue
<Teleport to="#modals">
  <LoginModal />
</Teleport>

<Teleport to="#modals">
  <HelpModal />
</Teleport>
```

같은 대상 안에 순서대로 렌더링된다.

---

## 컴포넌트 관계는 유지된다

Teleport는 DOM 위치만 바꾼다. Vue 컴포넌트의 부모-자식 관계는 그대로 유지된다.

따라서 props, emit, inject 같은 Vue 데이터 흐름은 일반 컴포넌트처럼 동작한다.

---

## 접근성 주의

모달을 만들 때는 단순히 화면에 띄우는 것만으로 충분하지 않다.

고려할 점:

- 모달이 열리면 포커스를 모달 안으로 이동
- ESC 키로 닫기
- 배경 스크롤 잠금
- 적절한 `role="dialog"`와 `aria-modal="true"`
- 닫기 버튼 제공

이 문서에서는 Teleport의 역할에 집중하고, 접근성은 별도 장에서 더 다룬다.

---

## 자주 하는 실수

### to 대상이 없음

`to="#modals"`를 사용했다면 실제 DOM에 `id="modals"` 요소가 있어야 한다.

### Teleport가 상태를 공유하지 못한다고 오해함

Teleport는 DOM 위치만 바꾼다. Vue 컴포넌트 데이터 흐름은 유지된다.

### 모달 접근성을 놓침

모달은 시각적으로 뜨는 것 외에도 키보드와 스크린 리더 사용성을 고려해야 한다.

---

## 작은 실습

`AppModal.vue`를 만들어보자.

- 부모에서 `open` 상태로 표시 제어
- `Teleport to="body"` 사용
- 닫기 버튼 클릭 시 `close` 이벤트 emit
- 배경 클릭 시 닫기

---

## 이번 장 요약

- `Teleport`는 템플릿 일부를 DOM의 다른 위치에 렌더링한다.
- 모달, 팝오버, 토스트 UI에 유용하다.
- `to`에는 `body`나 CSS 선택자를 사용할 수 있다.
- DOM 위치만 바뀌고 Vue 컴포넌트 관계는 유지된다.

---

## 다음 장으로

다음 장에서는 비동기 의존성을 가진 컴포넌트의 로딩 상태를 처리하는 `Suspense`를 배운다.

