# KeepAlive

## 이번 장에서 배울 것

`KeepAlive`는 동적 컴포넌트를 전환할 때 컴포넌트 인스턴스를 제거하지 않고 캐시해두는 Vue 내장 컴포넌트다.

탭 화면에서 입력 값이나 스크롤 상태를 유지하고 싶을 때 유용하다.

---

## 문제 상황

동적 컴포넌트를 사용해 탭을 만들 수 있다.

```vue
<component :is="activeComponent" />
```

하지만 기본적으로 다른 컴포넌트로 전환하면 이전 컴포넌트는 언마운트된다. 다시 돌아오면 새로 만들어지므로 내부 상태가 초기화될 수 있다.

예를 들어 탭 안의 입력 값이 사라질 수 있다.

---

## KeepAlive 기본 사용법

```vue
<template>
  <KeepAlive>
    <component :is="activeComponent" />
  </KeepAlive>
</template>
```

이렇게 감싸면 전환된 컴포넌트가 완전히 사라지지 않고 캐시된다.

---

## 탭 예제

```vue
<script setup>
import { computed, ref } from 'vue'
import ProfileTab from './components/ProfileTab.vue'
import SettingsTab from './components/SettingsTab.vue'

const activeTab = ref('profile')

const tabs = {
  profile: ProfileTab,
  settings: SettingsTab
}

const activeComponent = computed(() => tabs[activeTab.value])
</script>

<template>
  <button @click="activeTab = 'profile'">프로필</button>
  <button @click="activeTab = 'settings'">설정</button>

  <KeepAlive>
    <component :is="activeComponent" />
  </KeepAlive>
</template>
```

`ProfileTab` 안에 입력한 값은 `SettingsTab`으로 갔다가 돌아와도 유지된다.

---

## include와 exclude

특정 컴포넌트만 캐시하거나 제외할 수 있다.

```vue
<KeepAlive include="ProfileTab">
  <component :is="activeComponent" />
</KeepAlive>
```

여러 개는 쉼표로 구분할 수 있다.

```vue
<KeepAlive include="ProfileTab,SettingsTab">
  <component :is="activeComponent" />
</KeepAlive>
```

제외하려면 `exclude`를 사용한다.

```vue
<KeepAlive exclude="LogsTab">
  <component :is="activeComponent" />
</KeepAlive>
```

이때 컴포넌트 이름이 중요하다. SFC 파일명이나 컴포넌트 이름을 명확히 관리하는 것이 좋다.

---

## max

캐시할 컴포넌트 개수를 제한할 수 있다.

```vue
<KeepAlive :max="10">
  <component :is="activeComponent" />
</KeepAlive>
```

캐시가 너무 많아지는 것을 막을 때 사용한다.

---

## onActivated와 onDeactivated

`KeepAlive`로 캐시된 컴포넌트는 일반적인 mount/unmount와 조금 다르게 동작한다.

캐시에서 다시 활성화될 때는 `onActivated`, 비활성화될 때는 `onDeactivated`를 사용할 수 있다.

```vue
<script setup>
import { onActivated, onDeactivated } from 'vue'

onActivated(() => {
  console.log('다시 활성화됨')
})

onDeactivated(() => {
  console.log('비활성화됨')
})
</script>
```

---

## 언제 사용할까?

`KeepAlive`는 다음 상황에 적합하다.

- 탭 전환 시 입력 값 유지
- 동적 컴포넌트 전환 시 상태 보존
- 페이지 일부를 캐시해 다시 불러오는 비용 줄이기
- 스크롤 위치나 임시 편집 상태 유지

모든 컴포넌트를 무조건 캐시하면 메모리를 더 사용할 수 있다. 상태 보존이 필요한 곳에만 사용하자.

---

## 자주 하는 실수

### 일반 v-if 상태 보존 용도로 오해함

`KeepAlive`는 주로 동적 컴포넌트 캐싱에 사용한다. 단순 요소에는 맞지 않는다.

### 캐시가 많아지는 것을 고려하지 않음

동적 컴포넌트가 많다면 `max`를 고려한다.

### 생명주기 훅을 헷갈림

캐시된 컴포넌트는 다시 나타날 때 `onMounted`가 매번 실행되지 않는다. 필요하면 `onActivated`를 사용한다.

---

## 작은 실습

두 개의 탭 컴포넌트를 만들어보자.

- `ProfileTab.vue`: 이름 입력 input
- `MemoTab.vue`: 메모 입력 textarea
- `KeepAlive` 없이 탭 전환해보기
- `KeepAlive`를 적용해 입력 값이 유지되는지 확인하기

---

## 이번 장 요약

- `KeepAlive`는 동적 컴포넌트를 캐시해 상태를 보존한다.
- 탭 UI에서 입력 값이나 스크롤 상태를 유지할 때 유용하다.
- `include`, `exclude`, `max`로 캐시 대상을 제어할 수 있다.
- 캐시 컴포넌트에는 `onActivated`, `onDeactivated` 훅을 사용할 수 있다.

---

## 다음 장으로

다음 장에서는 모달이나 팝업을 DOM의 다른 위치로 렌더링하는 `Teleport`를 배운다.

