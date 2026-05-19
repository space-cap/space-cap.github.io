# Transition

## 이번 장에서 배울 것

`Transition`은 요소나 컴포넌트가 화면에 나타나거나 사라질 때 애니메이션을 적용하는 Vue 내장 컴포넌트다.

예를 들어 알림 메시지, 모달, 드롭다운, 토스트 메시지가 부드럽게 나타나고 사라지게 만들 수 있다.

---

## 기본 사용법

```vue
<script setup>
import { ref } from 'vue'

const show = ref(false)
</script>

<template>
  <button @click="show = !show">전환</button>

  <Transition>
    <p v-if="show">안녕하세요 Vue</p>
  </Transition>
</template>

<style scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 0.3s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>
```

`show`가 `true`가 되면 문장이 나타나고, `false`가 되면 사라진다. `Transition`은 이 과정에 CSS class를 자동으로 붙여준다.

---

## Transition이 동작하는 조건

`Transition`은 다음 상황에서 전환 효과를 적용할 수 있다.

- `v-if`로 요소가 생성되거나 제거될 때
- `v-show`로 표시 여부가 바뀔 때
- 동적 컴포넌트가 바뀔 때
- `key`가 바뀌어 다른 요소로 교체될 때

가장 많이 쓰는 형태는 `v-if`와 함께 사용하는 방식이다.

---

## 자동으로 붙는 CSS class

기본 이름을 사용하면 Vue는 다음 class를 붙인다.

| class | 의미 |
| --- | --- |
| `v-enter-from` | 나타나기 시작 상태 |
| `v-enter-active` | 나타나는 동안 적용 |
| `v-enter-to` | 나타나기 끝 상태 |
| `v-leave-from` | 사라지기 시작 상태 |
| `v-leave-active` | 사라지는 동안 적용 |
| `v-leave-to` | 사라지기 끝 상태 |

초보자는 먼저 네 가지를 자주 사용한다.

```css
.v-enter-active,
.v-leave-active {
  transition: opacity 0.3s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
```

---

## 이름 있는 Transition

여러 전환 효과를 구분하려면 `name`을 사용한다.

```vue
<Transition name="fade">
  <p v-if="show">페이드 효과</p>
</Transition>
```

CSS class 이름도 `fade-`로 시작한다.

```css
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
```

---

## 슬라이드와 페이드 함께 적용하기

```vue
<Transition name="slide-fade">
  <div v-if="show" class="panel">
    설정 패널
  </div>
</Transition>
```

```css
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: opacity 0.25s ease, transform 0.25s ease;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
```

실무에서는 이 정도 전환만으로도 UI가 훨씬 자연스러워진다.

---

## appear

처음 렌더링될 때도 전환 효과를 주고 싶다면 `appear`를 사용한다.

```vue
<Transition name="fade" appear>
  <p>처음부터 페이드 인</p>
</Transition>
```

---

## mode

두 요소가 교체될 때 들어오고 나가는 순서를 조절할 수 있다.

```vue
<Transition name="fade" mode="out-in">
  <p v-if="isEditing" key="edit">편집 중</p>
  <p v-else key="view">보기 모드</p>
</Transition>
```

`out-in`은 기존 요소가 먼저 사라지고, 그다음 새 요소가 나타난다.

---

## 주의: 하나의 요소만 감싸기

`Transition`의 기본 slot에는 하나의 요소나 하나의 컴포넌트만 들어갈 수 있다.

```vue
<!-- 좋은 예 -->
<Transition>
  <p v-if="show">하나의 요소</p>
</Transition>
```

여러 요소에 목록 전환을 적용하려면 다음 장의 `TransitionGroup`을 사용한다.

---

## 자주 하는 실수

### CSS class 이름을 잘못 씀

`<Transition name="fade">`를 사용했다면 CSS도 `fade-enter-active`처럼 작성해야 한다.

### transition 시간이 없음

CSS에 실제 전환 시간이 없으면 효과가 보이지 않는다.

```css
transition: opacity 0.3s ease;
```

### 여러 요소를 Transition 안에 넣음

여러 목록 항목에는 `TransitionGroup`을 사용한다.

---

## 작은 실습

알림 메시지를 만들어보자.

- 버튼을 누르면 알림이 보인다.
- 다시 누르면 알림이 사라진다.
- `fade` 이름의 Transition을 사용한다.
- opacity와 translateY를 함께 적용한다.

---

## 이번 장 요약

- `Transition`은 요소가 나타나고 사라질 때 전환 효과를 준다.
- `v-if`, `v-show`, 동적 컴포넌트와 함께 사용할 수 있다.
- `name`을 지정하면 CSS class 이름이 해당 이름으로 시작한다.
- 하나의 요소나 컴포넌트만 감싸는 것이 기본이다.

---

## 다음 장으로

다음 장에서는 여러 목록 항목의 추가, 삭제, 이동에 애니메이션을 적용하는 `TransitionGroup`을 배운다.

