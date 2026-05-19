# Vue의 탄생과 역사

## 이번 장에서 배울 것

이번 장에서는 Vue가 왜 만들어졌는지, 어떤 흐름으로 발전했는지 살펴본다. 역사를 아주 깊게 외우기보다, 현재 Vue 3를 기준으로 학습해야 하는 이유를 이해하는 것이 목표다.

---

## Vue를 만든 사람

Vue는 Evan You가 만들었다. Evan You는 Google에서 AngularJS를 사용한 경험이 있었고, AngularJS의 장점 중 일부를 더 가볍고 유연하게 사용할 수 있는 도구를 구상했다.

그 결과 2014년에 Vue가 공개되었다. 이름인 Vue는 영어 단어 `view`처럼 발음한다. 화면을 만드는 도구라는 성격이 이름에 담겨 있다.

---

## Vue가 주목받은 이유

Vue가 인기를 얻은 이유는 다음과 같다.

- HTML과 비슷한 템플릿 문법으로 시작하기 쉽다.
- 작은 페이지 일부에만 붙여 사용할 수 있다.
- 컴포넌트 기반으로 큰 애플리케이션도 만들 수 있다.
- 공식 문서가 친절하고 예제가 비교적 이해하기 쉽다.
- React나 Angular에 비해 진입 장벽이 낮다고 느끼는 개발자가 많다.

Vue는 "처음에는 쉽고, 필요하면 크게 확장할 수 있다"는 방향을 꾸준히 유지해왔다.

---

## 간단한 연표

| 시기 | 주요 내용 |
| --- | --- |
| 2014년 | Vue.js 공개 |
| 2016년 | Vue 2 공개, 가상 DOM과 서버 사이드 렌더링 지원 강화 |
| 2020년 | Vue 3 공개, Composition API와 더 나은 TypeScript 지원 |
| 2023년 12월 31일 | Vue 2 공식 지원 종료 |
| 현재 | 새 프로젝트는 Vue 3와 Vite 기반 도구 사용 권장 |

---

## Vue 2와 Vue 3의 큰 차이

새로 배우는 사람은 Vue 3부터 시작하면 된다. 그래도 Vue 2와 Vue 3의 차이를 대략 알아두면 기존 프로젝트를 만났을 때 덜 당황한다.

| 구분 | Vue 2 | Vue 3 |
| --- | --- | --- |
| 기본 학습 대상 | 기존 프로젝트 유지보수 | 새 프로젝트 권장 |
| 주요 API | Options API 중심 | Composition API와 Options API 모두 지원 |
| TypeScript | 제한적 | 더 나은 지원 |
| 성능 | 안정적 | 더 개선됨 |
| 상태 | 지원 종료 | 현재 표준 |

Vue 3에서도 Options API를 사용할 수 있다. 따라서 Vue 2 방식의 코드가 완전히 사라진 것은 아니다. 다만 새 프로젝트와 공식 예제는 Composition API와 `<script setup>`을 많이 사용한다.

---

## Options API와 Composition API

Vue에는 컴포넌트 로직을 작성하는 대표적인 방식이 두 가지 있다.

### Options API

객체 안에 `data`, `methods`, `computed` 같은 옵션을 나누어 작성한다.

```vue
<script>
export default {
  data() {
    return {
      count: 0
    }
  },
  methods: {
    increment() {
      this.count++
    }
  }
}
</script>
```

처음 보기에는 구조가 명확해서 이해하기 쉽다.

### Composition API

필요한 기능을 함수처럼 가져와서 조합한다.

```vue
<script setup>
import { ref } from 'vue'

const count = ref(0)

function increment() {
  count.value++
}
</script>
```

큰 프로젝트에서 로직을 재사용하고 정리하기 좋다. 이 문서에서는 Vue 3의 일반적인 흐름에 맞추어 Composition API와 `<script setup>`을 기본으로 설명하되, 필요한 곳에서는 Options API도 비교한다.

---

## Vue CLI와 create-vue

예전에는 Vue 프로젝트를 만들 때 Vue CLI를 많이 사용했다.

```bash
vue create my-project
```

하지만 현재 Vue CLI는 유지보수 모드다. 새 프로젝트를 만들 때는 공식 Quick Start에서 안내하는 `create-vue`와 Vite 기반 프로젝트 생성을 사용하는 것이 좋다.

```bash
npm create vue@latest
```

이 문서에서도 새 프로젝트는 `npm create vue@latest`를 기준으로 만든다. Vue CLI는 기존 프로젝트를 이해해야 할 때만 짧게 다룬다.

---

## 초보자가 역사에서 꼭 기억할 것

외워야 할 것은 많지 않다.

- Vue는 2014년에 공개되었다.
- Vue 3가 현재 학습 기준이다.
- Vue 2는 공식 지원이 종료되었다.
- 새 프로젝트는 Vue 3, create-vue, Vite 흐름으로 시작한다.
- 공식 문서의 예제는 Composition API와 `<script setup>` 흐름이 많다.

---

## 작은 실습

아래 질문에 답해보자.

1. 새 Vue 프로젝트를 만들 때 `vue create`보다 먼저 고려할 명령은 무엇인가?
2. Vue 2를 지금 처음부터 배울 필요가 없는 이유는 무엇인가?
3. Options API와 Composition API는 서로 완전히 다른 프레임워크인가?

정답:

1. `npm create vue@latest`
2. Vue 2는 공식 지원이 종료되었고 새 프로젝트는 Vue 3가 기준이기 때문이다.
3. 아니다. 같은 Vue 컴포넌트를 작성하는 두 가지 스타일이다.

---

## 이번 장 요약

- Vue는 Evan You가 만든 JavaScript 프레임워크다.
- 2014년에 공개되었고, 현재는 Vue 3가 표준 학습 기준이다.
- Vue 2는 공식 지원이 종료되었다.
- 새 프로젝트는 `create-vue`와 Vite 기반으로 시작하는 것이 좋다.

---

## 다음 장으로

다음 장에서는 Vue를 배우기 전에 알고 있으면 좋은 HTML, CSS, JavaScript 기초를 정리한다.

