# Options API와 Composition API

## 이번 장에서 배울 것

Vue 컴포넌트 로직을 작성하는 대표적인 방식은 Options API와 Composition API다.

두 방식은 서로 다른 Vue가 아니다. 같은 Vue 컴포넌트를 작성하는 두 가지 스타일이다.

---

## Options API란?

Options API는 `data`, `methods`, `computed`, `watch` 같은 옵션별로 코드를 나누는 방식이다.

```vue
<script>
export default {
  data() {
    return {
      count: 0
    }
  },
  computed: {
    doubleCount() {
      return this.count * 2
    }
  },
  methods: {
    increase() {
      this.count++
    }
  }
}
</script>

<template>
  <p>{{ count }}</p>
  <p>{{ doubleCount }}</p>
  <button @click="increase">증가</button>
</template>
```

처음 보면 각 코드의 자리가 정해져 있어 이해하기 쉽다.

---

## Composition API란?

Composition API는 `ref`, `computed`, `watch`, `onMounted` 같은 함수를 import해서 사용하는 방식이다.

```vue
<script setup>
import { computed, ref } from 'vue'

const count = ref(0)

const doubleCount = computed(() => count.value * 2)

function increase() {
  count.value++
}
</script>

<template>
  <p>{{ count }}</p>
  <p>{{ doubleCount }}</p>
  <button @click="increase">증가</button>
</template>
```

Vue 3의 공식 예제와 새 프로젝트에서는 Composition API와 `<script setup>`을 많이 사용한다.

---

## 두 방식 비교

| 구분 | Options API | Composition API |
| --- | --- | --- |
| 코드 구조 | 옵션별로 분리 | 기능별로 묶기 쉬움 |
| 초반 이해 | 비교적 쉽다 | JavaScript 함수 흐름에 익숙해야 함 |
| 로직 재사용 | mixin 등 필요 | Composable로 자연스럽게 분리 |
| TypeScript | 상대적으로 제한 | 더 좋은 추론 |
| Vue 3 새 예제 | 사용 가능 | 많이 사용됨 |

---

## 같은 기능을 두 방식으로 비교하기

### Options API

```vue
<script>
export default {
  data() {
    return {
      keyword: '',
      items: ['Vue', 'React', 'Svelte']
    }
  },
  computed: {
    filteredItems() {
      return this.items.filter((item) =>
        item.includes(this.keyword)
      )
    }
  }
}
</script>
```

### Composition API

```vue
<script setup>
import { computed, ref } from 'vue'

const keyword = ref('')
const items = ref(['Vue', 'React', 'Svelte'])

const filteredItems = computed(() => {
  return items.value.filter((item) =>
    item.includes(keyword.value)
  )
})
</script>
```

두 코드 모두 같은 일을 한다. 차이는 코드를 어떤 기준으로 배치하느냐다.

---

## 초보자는 무엇을 먼저 배워야 할까?

새로 Vue를 배우는 사람은 Composition API와 `<script setup>`을 기본으로 배우는 것을 추천한다.

이유:

- Vue 3 공식 문서와 예제에서 널리 사용된다.
- Composable로 로직 재사용을 배우기 좋다.
- TypeScript로 확장하기 쉽다.
- 새 프로젝트에서 자주 만난다.

다만 Options API를 몰라도 된다는 뜻은 아니다. 기존 Vue 2 프로젝트나 오래된 강의, 레거시 코드에서는 Options API를 자주 만난다. 읽을 수 있을 정도로는 알아두면 좋다.

---

## 두 방식을 섞어도 될까?

기술적으로는 한 컴포넌트 안에서 함께 사용할 수 있다. 하지만 새로 작성하는 코드에서는 한 컴포넌트 안에 두 스타일을 섞지 않는 것이 읽기 쉽다.

기존 Options API 프로젝트에 Composition API 기반 라이브러리나 기능을 일부 도입해야 할 때는 예외적으로 섞을 수 있다.

---

## Options API는 사라질까?

Vue 공식 입장상 Options API를 제거할 계획은 없다. Options API는 여전히 Vue의 일부이며, 작은 컴포넌트나 중간 규모 프로젝트에서 충분히 사용할 수 있다.

따라서 "Composition API가 새롭기 때문에 Options API는 나쁘다"라고 생각할 필요는 없다. 다만 이 문서에서는 새 Vue 3 학습 흐름에 맞춰 Composition API를 기본으로 사용한다.

---

## 선택 기준

| 상황 | 추천 |
| --- | --- |
| 새 Vue 3 프로젝트 | Composition API + `<script setup>` |
| 공식 문서 흐름 따라가기 | Composition API |
| 기존 Vue 2 코드 유지보수 | Options API 이해 필요 |
| 작은 학습 예제 | 둘 다 가능 |
| 로직 재사용이 많은 프로젝트 | Composition API |

---

## 자주 하는 실수

### 두 방식을 서로 다른 프레임워크처럼 생각함

둘 다 Vue 컴포넌트를 작성하는 방식이다.

### Composition API에서 this를 사용하려 함

`<script setup>`에서는 `this`를 사용하지 않는다. 변수와 함수를 직접 선언해 사용한다.

```js
const count = ref(0)
```

### Options API 코드를 무조건 Composition API로 바꾸려 함

동작 중인 기존 코드를 이유 없이 바꿀 필요는 없다. 새 기능이나 리팩터링이 필요할 때 점진적으로 고려한다.

---

## 작은 실습

아래 Options API 코드를 Composition API로 바꿔보자.

```vue
<script>
export default {
  data() {
    return {
      count: 0
    }
  },
  methods: {
    increase() {
      this.count++
    }
  }
}
</script>
```

힌트:

```vue
<script setup>
import { ref } from 'vue'

const count = ref(0)

function increase() {
  count.value++
}
</script>
```

---

## 이번 장 요약

- Options API와 Composition API는 Vue 컴포넌트 작성 스타일이다.
- Options API는 옵션별로 코드를 나눈다.
- Composition API는 함수와 반응형 API로 로직을 구성한다.
- 새 Vue 3 학습은 Composition API와 `<script setup>` 중심을 추천한다.
- Options API도 여전히 Vue의 일부이며 기존 코드 이해에 필요하다.

---

## 다음 장으로

다음 장에서는 Vue가 화면을 어떻게 갱신하는지 렌더링 원리를 가볍게 살펴본다.

