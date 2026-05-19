# create-vue와 Vite로 프로젝트 만들기

## 이번 장에서 배울 것

이번 장에서는 Vue 공식 권장 흐름에 따라 새 Vue 프로젝트를 만든다. CDN 방식은 간단하지만 큰 프로젝트에는 부족하다. 실제 개발에서는 Vite 기반 프로젝트를 사용하는 것이 일반적이다.

---

## Vite란?

Vite는 현대적인 프론트엔드 개발 도구다. Vue 프로젝트에서 Vite는 다음 일을 도와준다.

- 개발 서버 실행
- 빠른 새로고침
- JavaScript와 CSS 번들링
- 배포용 파일 생성
- `.vue` 파일 처리

초보자는 Vite 내부 동작을 모두 알 필요는 없다. 지금은 Vue 프로젝트를 실행하고 빌드해주는 도구라고 이해하면 된다.

---

## create-vue란?

`create-vue`는 Vue 공식 프로젝트 생성 도구다. 터미널에서 몇 가지 질문에 답하면 Vue 프로젝트 기본 구조를 만들어준다.

새 프로젝트 생성 명령은 다음과 같다.

```bash
npm create vue@latest
```

`@latest`는 최신 버전의 생성 도구를 사용하겠다는 뜻이다.

---

## 프로젝트 만들기

터미널에서 실습 폴더로 이동한 뒤 아래 명령을 실행한다.

```bash
npm create vue@latest
```

프로젝트 이름을 물어보면 원하는 이름을 입력한다.

예:

```text
vue-basic-app
```

이후 여러 옵션을 물어본다. 처음 배우는 단계에서는 대부분 `No`를 선택해도 된다.

추천 선택:

| 질문 | 초보자 추천 |
| --- | --- |
| TypeScript 추가 | No |
| JSX 지원 | No |
| Vue Router 추가 | No |
| Pinia 추가 | No |
| Vitest 추가 | No |
| E2E 테스트 추가 | No |
| ESLint 추가 | Yes 또는 No |
| Prettier 추가 | Yes 또는 No |

처음에는 핵심 문법에 집중하기 위해 Router와 Pinia를 넣지 않는다. 나중에 해당 장에서 직접 추가하면서 배운다.

---

## 프로젝트 실행하기

프로젝트가 만들어지면 터미널에 안내 명령이 나온다. 보통 다음 순서다.

```bash
cd vue-basic-app
npm install
npm run dev
```

`npm install`은 필요한 패키지를 설치한다.

`npm run dev`는 개발 서버를 실행한다.

성공하면 터미널에 다음과 비슷한 주소가 나온다.

```text
http://localhost:5173/
```

브라우저에서 이 주소를 열면 Vue 기본 화면이 보인다.

---

## 개발 서버 중지하기

개발 서버를 멈추려면 터미널에서 다음 키를 누른다.

```text
Ctrl + C
```

다시 실행하려면 프로젝트 폴더에서 아래 명령을 실행한다.

```bash
npm run dev
```

---

## package.json 이해하기

프로젝트 폴더 안에는 `package.json` 파일이 있다.

예:

```json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "vue": "^3.5.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "latest",
    "vite": "latest"
  }
}
```

중요한 부분은 `scripts`다.

| 스크립트 | 실행 명령 | 의미 |
| --- | --- | --- |
| `dev` | `npm run dev` | 개발 서버 실행 |
| `build` | `npm run build` | 배포용 파일 생성 |
| `preview` | `npm run preview` | 빌드 결과 미리보기 |

---

## 빌드해보기

개발이 끝난 앱은 배포용 파일로 만들어야 한다.

```bash
npm run build
```

성공하면 `dist` 폴더가 생성된다. 이 폴더 안의 파일들이 실제 배포에 사용된다.

빌드 결과를 로컬에서 확인하려면 다음 명령을 사용한다.

```bash
npm run preview
```

---

## Vue CLI와 헷갈리지 않기

예전 문서나 강의에서는 다음 명령을 볼 수 있다.

```bash
vue create my-project
```

이 명령은 Vue CLI 방식이다. 현재 새 프로젝트에서는 `npm create vue@latest`를 우선 사용한다.

Vue CLI가 완전히 쓸모없는 것은 아니다. 오래된 프로젝트를 유지보수할 때는 여전히 만날 수 있다. 하지만 새로 배우는 지금은 create-vue와 Vite 흐름을 기준으로 익히면 된다.

---

## 자주 하는 실수

### 프로젝트 폴더로 이동하지 않고 npm run dev를 실행함

`npm run dev`는 `package.json`이 있는 프로젝트 폴더에서 실행해야 한다.

```bash
cd vue-basic-app
npm run dev
```

### npm install을 건너뜀

패키지를 설치하지 않으면 개발 서버가 실행되지 않는다.

```bash
npm install
```

### 터미널 주소를 브라우저에 열지 않음

개발 서버는 실행만으로 끝나지 않는다. 터미널에 나온 `http://localhost:5173/` 같은 주소를 브라우저에서 열어야 화면을 볼 수 있다.

---

## 작은 실습

새 프로젝트를 만들고 실행해보자.

```bash
npm create vue@latest
cd vue-basic-app
npm install
npm run dev
```

브라우저에서 기본 Vue 화면이 보이면 성공이다.

---

## 이번 장 요약

- 새 Vue 프로젝트는 `npm create vue@latest`로 만든다.
- Vite는 Vue 프로젝트의 개발 서버와 빌드를 도와준다.
- `npm install`로 패키지를 설치한다.
- `npm run dev`로 개발 서버를 실행한다.
- `npm run build`로 배포용 파일을 만든다.

---

## 다음 장으로

다음 장에서는 생성된 Vue 프로젝트의 폴더와 파일 구조를 하나씩 살펴본다.

