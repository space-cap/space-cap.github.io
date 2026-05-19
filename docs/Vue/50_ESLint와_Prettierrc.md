# ESLint와 Prettier

## 이번 장에서 배울 것

ESLint와 Prettier는 코드 품질과 코드 스타일을 관리하는 도구다.

- ESLint: 버그 가능성이 있는 코드와 규칙 위반을 찾아준다.
- Prettier: 코드 모양을 자동으로 정리한다.

둘을 함께 사용하면 팀 코드 스타일을 일정하게 유지하고, 실수를 빨리 발견할 수 있다.

---

## ESLint란?

ESLint는 JavaScript 코드에서 문제 패턴을 찾아주는 도구다.

예:

- 사용하지 않는 변수
- 잘못된 import
- 위험한 문법
- 팀에서 금지한 코드 스타일

Vue 프로젝트에서는 `.vue` 파일도 검사할 수 있도록 Vue 관련 설정을 함께 사용한다.

---

## Prettier란?

Prettier는 코드 포맷터다. 줄바꿈, 들여쓰기, 따옴표, 세미콜론 같은 형식을 자동으로 맞춘다.

ESLint가 "이 코드가 문제인가?"에 가깝다면, Prettier는 "이 코드를 어떤 모양으로 정리할까?"에 가깝다.

---

## create-vue에서 선택하기

새 프로젝트를 만들 때 `npm create vue@latest`에서 ESLint와 Prettier 옵션을 선택할 수 있다.

처음 배우는 단계에서는 옵션을 켜고 프로젝트가 만들어주는 설정을 그대로 사용하는 것이 가장 쉽다.

```bash
npm create vue@latest
```

---

## 직접 설치하기

프로젝트에 ESLint가 없다면 공식 설정 도구로 시작할 수 있다.

```bash
npm init @eslint/config@latest
```

Prettier는 다음처럼 설치한다.

```bash
npm install --save-dev --save-exact prettier
```

Vue 프로젝트에서는 이미 설정이 있는 경우가 많으므로, 기존 설정을 먼저 확인한다.

---

## package.json 스크립트

예시:

```json
{
  "scripts": {
    "lint": "eslint . --fix",
    "format": "prettier --write src/"
  }
}
```

실행:

```bash
npm run lint
npm run format
```

`lint`는 코드 문제를 찾고 일부는 자동 수정한다. `format`은 코드 모양을 정리한다.

---

## Prettier 설정 파일

프로젝트 루트에 `.prettierrc` 파일을 둘 수 있다.

```json
{
  "singleQuote": true,
  "semi": false,
  "printWidth": 100
}
```

팀마다 선호가 다르다. 중요한 것은 개인 취향보다 프로젝트에서 하나로 맞추는 것이다.

---

## ESLint 설정 파일

최신 ESLint는 `eslint.config.js` 또는 `eslint.config.mjs`를 사용하는 flat config 흐름을 사용한다.

예시는 프로젝트 생성 도구가 만들어주는 설정을 우선 따른다. 직접 처음부터 설정하기보다 `create-vue`가 생성한 파일을 읽으며 이해하는 것이 좋다.

---

## VS Code와 자동 저장

VS Code에서 저장할 때 자동 포맷을 설정할 수 있다.

예시 설정:

```json
{
  "editor.formatOnSave": true,
  "editor.defaultFormatter": "esbenp.prettier-vscode",
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": "explicit"
  }
}
```

팀 프로젝트에서는 `.vscode/settings.json`으로 공유할 수도 있다.

---

## ESLint와 Prettier 충돌 줄이기

예전에는 ESLint와 Prettier가 포맷 규칙에서 충돌하는 일이 많았다. 현재도 프로젝트 설정에 따라 충돌이 날 수 있다.

기준을 단순하게 잡자.

- 코드 문제는 ESLint
- 코드 모양은 Prettier
- 생성 도구가 만든 설정을 우선 사용
- 팀에서 합의한 규칙만 추가

---

## 자주 하는 실수

### 포맷 논쟁에 시간을 너무 많이 씀

도구가 자동으로 정하게 하고, 팀 규칙을 따른다.

### 저장 시 자동 수정이 안 되는데 설정만 탓함

VS Code 확장이 설치되어 있는지, 프로젝트에 Prettier/ESLint가 설치되어 있는지, 현재 파일이 검사 대상인지 확인한다.

### lint 오류를 무조건 무시함

ESLint 경고는 실제 버그로 이어질 수 있다. 왜 경고가 나오는지 먼저 이해한다.

---

## 작은 실습

현재 Vue 프로젝트에 다음을 설정해보자.

- `npm run lint` 실행
- `npm run format` 실행
- 일부러 사용하지 않는 변수를 만들고 ESLint 경고 확인
- 들여쓰기를 망가뜨린 뒤 Prettier로 정리

---

## 이번 장 요약

- ESLint는 코드 문제를 찾는 도구다.
- Prettier는 코드 모양을 정리하는 도구다.
- 새 Vue 프로젝트에서는 create-vue 옵션으로 쉽게 설정할 수 있다.
- 저장 시 자동 수정 설정을 하면 일상 개발이 편해진다.

---

## 다음 장으로

다음 장에서는 Vue 앱 성능 최적화의 기본 원칙을 배운다.

