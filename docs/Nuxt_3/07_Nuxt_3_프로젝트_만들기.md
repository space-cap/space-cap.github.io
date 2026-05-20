# Nuxt 3 프로젝트 만들기

## 이번 장에서 배울 것

- Nuxt 3 프로젝트 생성 방법
- 의존성 설치와 개발 서버 실행
- 처음 생성된 프로젝트 확인 방법

## 프로젝트 생성 명령

Nuxt 공식 문서에서 안내하는 기본 생성 명령은 다음과 같다.

```bash
npm create nuxt@latest my-nuxt-app
```

여기서 `my-nuxt-app`은 만들 프로젝트 폴더 이름이다. 원하는 이름으로 바꿀 수 있다.

예를 들어 `hello-nuxt`라는 프로젝트를 만들고 싶다면 다음처럼 실행한다.

```bash
npm create nuxt@latest hello-nuxt
```

명령을 실행하면 Nuxt 프로젝트가 생성된다.

## 프로젝트 폴더로 이동하기

프로젝트가 만들어졌다면 해당 폴더로 이동한다.

```bash
cd hello-nuxt
```

앞으로 Nuxt 관련 명령은 대부분 프로젝트 폴더 안에서 실행한다.

## 의존성 설치

프로젝트 생성 과정에서 의존성이 자동으로 설치될 수 있다. 만약 설치가 되어 있지 않거나, `node_modules` 폴더가 없다면 다음 명령을 실행한다.

```bash
npm install
```

의존성은 프로젝트가 사용하는 외부 패키지다. Nuxt, Vue, Vite 같은 패키지가 여기에 포함된다.

## 개발 서버 실행

개발 서버를 실행한다.

```bash
npm run dev
```

터미널에 로컬 주소가 출력된다.

```txt
http://localhost:3000
```

브라우저에서 이 주소를 열면 Nuxt 시작 화면을 볼 수 있다.

## 개발 서버란

개발 서버는 코드를 작성하는 동안 앱을 바로 확인할 수 있게 해 주는 서버다.

Nuxt 개발 서버는 파일 변경을 감지한다. 코드를 수정하면 브라우저 화면이 자동으로 갱신된다. 이 기능 덕분에 저장하고 새로고침하는 반복 작업이 줄어든다.

## 프로젝트 생성 후 확인할 파일

처음 생성한 프로젝트에서 다음 파일을 먼저 확인한다.

```txt
hello-nuxt/
  app.vue
  nuxt.config.ts
  package.json
```

`app.vue`는 Nuxt 앱의 가장 바깥 화면이다.

`nuxt.config.ts`는 Nuxt 설정 파일이다.

`package.json`은 프로젝트 정보, 의존성, 실행 명령어가 들어 있는 파일이다.

## package.json의 scripts

`package.json` 안에는 보통 다음과 같은 명령이 있다.

```json
{
  "scripts": {
    "dev": "nuxt dev",
    "build": "nuxt build",
    "generate": "nuxt generate",
    "preview": "nuxt preview"
  }
}
```

우리가 실행한 `npm run dev`는 여기 있는 `dev` 명령을 실행한 것이다.

## 자주 하는 실수

가장 흔한 실수는 프로젝트 폴더 밖에서 `npm run dev`를 실행하는 것이다.

`package.json`이 있는 폴더에서 실행해야 한다. 오류가 나면 현재 폴더에 `package.json`이 있는지 먼저 확인한다.

또 다른 실수는 이미 3000번 포트를 사용 중인 경우다. 이때 Nuxt가 다른 포트를 제안하거나, 포트 충돌 오류가 날 수 있다. 터미널 메시지를 잘 읽으면 해결 방향을 알 수 있다.

## 정리

Nuxt 3 프로젝트는 `npm create nuxt@latest 프로젝트명`으로 만든다. 프로젝트 폴더로 이동한 뒤 `npm install`, `npm run dev`를 실행하면 브라우저에서 앱을 확인할 수 있다.

## 다음 장으로

다음 장에서는 첫 번째 Nuxt 페이지를 만들어 본다.
