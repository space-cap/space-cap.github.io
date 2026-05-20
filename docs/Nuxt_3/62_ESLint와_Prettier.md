# ESLint와 Prettier

## 이번 장에서 배울 것

- ESLint와 Prettier의 역할 차이
- Nuxt에서 ESLint를 설정하는 방법
- Prettier를 설치하고 사용하는 방법
- 저장 시 자동 정리와 CI 확인의 기본

## 왜 코드 품질 도구가 필요할까

프로젝트가 커지면 코드 스타일과 실수를 사람이 매번 눈으로 확인하기 어렵다.

예를 들어 다음 문제가 자주 생긴다.

- 사용하지 않는 변수
- 잘못된 import
- 들여쓰기나 따옴표 스타일 차이
- 팀원마다 다른 포맷
- 단순 실수로 인한 오류

ESLint와 Prettier는 이런 문제를 줄여 준다.

## ESLint와 Prettier의 차이

둘은 비슷해 보이지만 역할이 다르다.

| 도구 | 역할 |
| --- | --- |
| ESLint | 코드 문제와 규칙 위반을 찾는다 |
| Prettier | 코드 모양을 일정하게 정리한다 |

ESLint는 "이 코드는 위험하거나 규칙에 어긋난다"를 찾는 도구다.

Prettier는 "이 코드를 어떤 모양으로 정렬할까"를 처리하는 도구다.

## Nuxt ESLint 설치

Nuxt는 공식 ESLint 모듈을 제공한다.

```bash
npx nuxt module add eslint
```

설치 후 `nuxt.config.ts`의 `modules`에 `@nuxt/eslint`가 등록된다.

```ts
export default defineNuxtConfig({
  modules: ['@nuxt/eslint']
})
```

Nuxt ESLint 모듈은 프로젝트에 맞는 ESLint 설정을 생성하고 Nuxt DevTools와도 통합될 수 있다.

## lint 명령 추가하기

`package.json`에 lint 명령을 추가한다.

```json
{
  "scripts": {
    "lint": "eslint ."
  }
}
```

실행한다.

```bash
npm run lint
```

자동으로 고칠 수 있는 문제는 `--fix` 옵션을 사용할 수 있다.

```json
{
  "scripts": {
    "lint:fix": "eslint . --fix"
  }
}
```

## Prettier 설치

Prettier는 코드 포맷터다.

```bash
npm install --save-dev --save-exact prettier
```

설정 파일을 만든다.

`.prettierrc`

```json
{}
```

처음에는 빈 설정으로 시작해도 된다. Prettier의 기본 규칙을 그대로 사용한다는 뜻이다.

## Prettier ignore

포맷하지 않을 파일이나 폴더는 `.prettierignore`에 적는다.

```txt
.nuxt
.output
node_modules
coverage
dist
```

빌드 결과물과 의존성 폴더는 포맷 대상에서 제외한다.

## format 명령 추가하기

`package.json`에 format 명령을 추가한다.

```json
{
  "scripts": {
    "format": "prettier . --write",
    "format:check": "prettier . --check"
  }
}
```

코드를 정리한다.

```bash
npm run format
```

정리 여부만 확인한다.

```bash
npm run format:check
```

## VS Code 설정

VS Code를 사용한다면 저장할 때 자동 정리되도록 설정할 수 있다.

`.vscode/settings.json`

```json
{
  "editor.formatOnSave": true,
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": "explicit"
  }
}
```

팀 프로젝트에서는 이런 설정을 문서화하거나 저장소에 포함해 팀원들이 같은 환경을 쓰게 할 수 있다.

## ESLint와 Prettier 충돌

ESLint와 Prettier가 같은 포맷 규칙을 서로 다르게 주장하면 불편하다.

Nuxt ESLint의 최신 설정 흐름과 Prettier 연동 방식은 버전에 따라 달라질 수 있다. 실제 프로젝트에서는 Nuxt ESLint 문서의 Prettier 관련 안내를 확인하는 것이 좋다.

초보 단계에서는 먼저 역할을 나누어 이해하자.

- ESLint: 문제 찾기
- Prettier: 모양 정리

## CI에서 확인하기

배포 전에 자동으로 검사하면 실수를 줄일 수 있다.

```bash
npm run lint
npm run format:check
```

GitHub Actions 같은 CI에서 이 명령을 실행하면 포맷되지 않은 코드나 규칙 위반이 배포 전에 발견된다.

## 자주 하는 실수

전역으로 설치한 ESLint나 Prettier에 의존하는 실수를 조심해야 한다.

프로젝트마다 버전이 다를 수 있으므로, 가능한 한 프로젝트 로컬 devDependencies에 설치하고 `npm run ...` 명령으로 실행하는 것이 좋다.

## 정리

ESLint는 코드 문제를 찾고, Prettier는 코드 형식을 정리한다. Nuxt에서는 `@nuxt/eslint` 모듈을 사용하고, Prettier는 프로젝트에 로컬로 설치해 `format` 명령을 구성하면 좋다.

## 다음 장으로

다음 장에서는 Nuxt에서 TypeScript를 사용하는 기본 흐름을 배운다.
