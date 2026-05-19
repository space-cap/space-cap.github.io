# Slot으로 내용 끼워넣기

## 이번 장에서 배울 것

Slot은 부모 컴포넌트가 자식 컴포넌트 안쪽에 템플릿 내용을 전달하는 기능이다.

Props가 JavaScript 값을 전달한다면, Slot은 HTML 구조나 다른 컴포넌트 같은 템플릿 조각을 전달한다.

---

## 기본 Slot

`BaseButton.vue`

```vue
<template>
  <button class="button">
    <slot></slot>
  </button>
</template>
```

부모:

```vue
<BaseButton>저장</BaseButton>
<BaseButton>삭제</BaseButton>
```

`<slot></slot>` 위치에 부모가 넣은 내용이 들어간다.

---

## Slot은 텍스트만 가능한가?

아니다. Slot에는 여러 요소를 넣을 수 있다.

```vue
<BaseButton>
  <strong>저장</strong>
  <span>Ctrl + S</span>
</BaseButton>
```

다른 컴포넌트도 넣을 수 있다.

```vue
<BaseButton>
  <SaveIcon />
  저장
</BaseButton>
```

---

## 기본 내용

부모가 slot 내용을 넘기지 않았을 때 보여줄 기본 내용을 지정할 수 있다.

`SubmitButton.vue`

```vue
<template>
  <button type="submit">
    <slot>제출</slot>
  </button>
</template>
```

부모:

```vue
<SubmitButton />
<SubmitButton>저장하기</SubmitButton>
```

첫 번째 버튼은 `제출`, 두 번째 버튼은 `저장하기`가 보인다.

---

## Named Slot

여러 영역을 부모가 채우게 하려면 이름 있는 slot을 사용한다.

`BaseCard.vue`

```vue
<template>
  <article class="card">
    <header>
      <slot name="header"></slot>
    </header>

    <section>
      <slot></slot>
    </section>

    <footer>
      <slot name="footer"></slot>
    </footer>
  </article>
</template>
```

부모:

```vue
<BaseCard>
  <template #header>
    <h2>카드 제목</h2>
  </template>

  <p>카드 본문입니다.</p>

  <template #footer>
    <button>확인</button>
  </template>
</BaseCard>
```

`#header`는 `v-slot:header`의 짧은 문법이다.

---

## 기본 Slot과 이름 있는 Slot

이름이 없는 slot은 default slot이다.

```vue
<slot></slot>
```

부모에서 별도 이름 없이 작성한 내용이 이 위치로 들어간다.

```vue
<BaseCard>
  <p>기본 slot 내용</p>
</BaseCard>
```

---

## Slot의 스코프

부모가 slot 안에 작성한 코드는 부모의 데이터를 사용할 수 있다.

```vue
<script setup>
const message = '부모의 메시지'
</script>

<template>
  <BaseCard>
    {{ message }}
  </BaseCard>
</template>
```

slot 내용은 자식 컴포넌트 안에 렌더링되지만, 작성된 위치는 부모 템플릿이기 때문에 부모의 변수에 접근한다.

---

## Scoped Slot

자식이 slot에 데이터를 넘겨줄 수도 있다. 이를 scoped slot이라고 한다.

`TodoList.vue`

```vue
<script setup>
defineProps({
  todos: {
    type: Array,
    required: true
  }
})
</script>

<template>
  <ul>
    <li v-for="todo in todos" :key="todo.id">
      <slot name="item" :todo="todo">
        {{ todo.title }}
      </slot>
    </li>
  </ul>
</template>
```

부모:

```vue
<TodoList :todos="todos">
  <template #item="{ todo }">
    <strong>{{ todo.title }}</strong>
    <span v-if="todo.done">완료</span>
  </template>
</TodoList>
```

초보 단계에서는 기본 slot과 named slot을 먼저 익히고, scoped slot은 재사용 목록 컴포넌트가 필요할 때 사용하면 된다.

---

## Props와 Slot 선택 기준

| 상황 | 추천 |
| --- | --- |
| 문자열, 숫자, 객체 같은 데이터 전달 | Props |
| 버튼 안의 내용처럼 템플릿 전달 | Slot |
| 카드의 header/body/footer를 부모가 구성 | Named Slot |
| 자식 데이터로 부모가 렌더링 모양 결정 | Scoped Slot |

---

## 자주 하는 실수

### Props로 HTML 구조를 억지로 전달함

HTML 조각이나 여러 요소를 전달해야 한다면 Slot이 더 자연스럽다.

### slot 내용에서 자식 변수에 바로 접근하려 함

slot 내용은 부모 스코프에서 작성된다. 자식 데이터를 쓰려면 scoped slot으로 명시적으로 전달해야 한다.

### named slot 문법을 헷갈림

```vue
<template #header>
  <h2>제목</h2>
</template>
```

---

## 작은 실습

`BaseCard.vue`를 만들어보자.

- `header` slot
- 기본 slot
- `footer` slot
- slot 내용이 없을 때 기본 텍스트 제공

그리고 부모에서 카드 2개를 서로 다른 내용으로 렌더링해보자.

---

## 이번 장 요약

- Slot은 부모가 자식 컴포넌트 내부에 템플릿 내용을 전달하는 기능이다.
- 기본 slot은 `<slot></slot>`로 만든다.
- 여러 영역이 필요하면 named slot을 사용한다.
- 자식 데이터를 slot으로 넘길 때는 scoped slot을 사용한다.

---

## 다음 장으로

다음 장에서는 깊은 컴포넌트 트리에서 데이터를 공유하는 Provide와 Inject를 배운다.

