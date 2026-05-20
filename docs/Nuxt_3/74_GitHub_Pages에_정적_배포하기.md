# GitHub Pages에 정적 배포하기

## 이번 장에서 배울 것

- GitHub Pages의 제약
- Nuxt 앱을 정적 사이트로 배포하는 방법
- `github_pages` preset
- repository site에서 `NUXT_APP_BASE_URL`이 필요한 이유

## GitHub Pages의 특징

GitHub Pages는 정적 사이트 호스팅이다.

즉, HTML, CSS, JavaScript, 이미지 같은 정적 파일을 제공한다.

서버에서 코드를 실행하는 기능은 없다.

따라서 다음 기능은 GitHub Pages에서 직접 동작하지 않는다.

- Nuxt server API
- SSR 서버 실행
- 서버 미들웨어
- 런타임 서버 로직
- DB 직접 연결

GitHub Pages에 배포하려면 Nuxt 앱을 정적 사이트로 만들어야 한다.

## 적합한 프로젝트

GitHub Pages에 어울리는 Nuxt 프로젝트는 다음과 같다.

- 블로그
- 문서 사이트
- 포트폴리오
- 회사 소개 페이지
- 정적 랜딩 페이지

로그인, 서버 API, 사용자별 대시보드가 필요한 앱은 Vercel, Netlify, Node 서버 같은 배포 환경을 고려해야 한다.

## github_pages preset

Nuxt는 GitHub Pages 배포를 위한 preset을 제공한다.

```bash
npx nuxt build --preset github_pages
```

이 명령은 GitHub Pages에 맞는 정적 결과물을 생성한다.

출력 결과는 보통 다음 경로에 있다.

```txt
.output/public
```

GitHub Actions에서 이 폴더를 Pages artifact로 업로드한다.

## base URL 문제

GitHub Pages에는 두 가지 형태가 있다.

사용자 또는 조직 사이트:

```txt
https://username.github.io/
```

저장소 사이트:

```txt
https://username.github.io/repository/
```

저장소 사이트는 루트가 `/`가 아니라 `/repository/`다.

이 경우 빌드할 때 `NUXT_APP_BASE_URL`을 설정해야 한다.

```bash
NUXT_APP_BASE_URL=/repository/ npx nuxt build --preset github_pages
```

Windows PowerShell에서는 다음처럼 실행한다.

```powershell
$env:NUXT_APP_BASE_URL="/repository/"
npx nuxt build --preset github_pages
```

base URL을 맞추지 않으면 배포 후 CSS와 JavaScript 파일 경로가 깨질 수 있다.

## GitHub Actions 예시

`.github/workflows/deploy.yml`

```yaml
name: Deploy to GitHub Pages

on:
  workflow_dispatch:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: 20
      - run: npm install
      - run: npx nuxt build --preset github_pages
      - uses: actions/upload-pages-artifact@v3
        with:
          path: ./.output/public

  deploy:
    needs: build
    permissions:
      pages: write
      id-token: write
    environment:
      name: github-pages
    runs-on: ubuntu-latest
    steps:
      - uses: actions/deploy-pages@v4
```

저장소 사이트라면 build 단계에 환경 변수를 추가한다.

```yaml
- run: npx nuxt build --preset github_pages
  env:
    NUXT_APP_BASE_URL: /repository/
```

## GitHub Pages 설정

GitHub 저장소의 Settings에서 Pages를 설정한다.

일반적인 흐름은 다음과 같다.

1. Settings로 이동한다.
2. Pages 메뉴를 연다.
3. Source를 GitHub Actions로 설정한다.
4. workflow를 실행한다.

배포가 완료되면 Pages URL이 표시된다.

## 자주 하는 실수

서버 API가 있는 Nuxt 앱을 GitHub Pages에 배포하려는 실수가 많다.

GitHub Pages는 서버 API를 실행할 수 없다.

또 저장소 사이트인데 `NUXT_APP_BASE_URL`을 설정하지 않으면 배포 후 흰 화면이나 asset 404가 발생할 수 있다.

## 정리

GitHub Pages는 정적 사이트만 지원한다. Nuxt 앱을 GitHub Pages에 배포하려면 `github_pages` preset으로 정적 결과물을 만들고, 저장소 사이트에서는 `NUXT_APP_BASE_URL`을 저장소 경로에 맞게 설정해야 한다.

## 다음 장으로

다음 장에서는 development, staging, production 환경별 설정 관리 방법을 배운다.
