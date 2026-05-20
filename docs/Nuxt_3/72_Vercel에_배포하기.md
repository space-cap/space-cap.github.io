# Vercel에 배포하기

## 이번 장에서 배울 것

- Vercel 배포 흐름
- Git 저장소를 연결하는 방법
- 환경 변수 설정
- Preview Deployment와 Production Deployment

## Vercel이란

Vercel은 프론트엔드와 풀스택 웹 앱을 쉽게 배포할 수 있는 플랫폼이다.

Nuxt 앱은 Vercel에 비교적 간단히 배포할 수 있다. Vercel은 Nuxt/Nitro 앱을 감지하고 알맞은 설정을 적용할 수 있다.

## 기본 배포 흐름

Vercel 배포의 일반적인 흐름은 다음과 같다.

1. Nuxt 프로젝트를 GitHub, GitLab, Bitbucket 같은 Git 저장소에 올린다.
2. Vercel에서 새 프로젝트를 만든다.
3. 저장소를 선택한다.
4. Vercel이 Nuxt 앱을 감지한다.
5. 필요한 환경 변수를 등록한다.
6. Deploy를 실행한다.

이후 main 브랜치에 push하면 production 배포가 실행되고, 다른 브랜치나 Pull Request는 preview 배포로 생성될 수 있다.

## 빌드 명령

대부분의 경우 Vercel이 Nuxt 프로젝트를 자동 감지한다.

기본 빌드 명령은 보통 다음과 같다.

```bash
npm run build
```

`package.json`에 build script가 있어야 한다.

```json
{
  "scripts": {
    "build": "nuxt build"
  }
}
```

## 환경 변수 등록

API 주소나 비밀 키가 있다면 Vercel Project Settings에서 Environment Variables에 등록한다.

예를 들어 Nuxt runtime config와 연결하려면 다음처럼 이름을 맞춘다.

```txt
NUXT_PUBLIC_API_BASE=https://api.example.com
NUXT_API_SECRET=secret-value
```

`nuxt.config.ts`

```ts
export default defineNuxtConfig({
  runtimeConfig: {
    apiSecret: '',
    public: {
      apiBase: ''
    }
  }
})
```

`NUXT_PUBLIC_API_BASE`는 클라이언트에도 노출될 수 있고, `NUXT_API_SECRET`은 서버에서만 사용해야 한다.

## Preview Deployment

Vercel은 브랜치나 Pull Request마다 Preview Deployment를 만들 수 있다.

Preview Deployment는 배포 전에 변경 사항을 실제 URL로 확인할 수 있는 기능이다.

다음 상황에 유용하다.

- 팀원에게 UI 확인 요청
- QA 테스트
- 배포 전 기능 검증
- API 환경 변수 차이 확인

Preview와 Production의 환경 변수를 다르게 설정할 수 있으므로, API 주소나 인증 설정을 잘 구분해야 한다.

## Production Deployment

Production Deployment는 실제 사용자가 보는 운영 배포다.

보통 main 또는 production 브랜치에 push하면 실행된다.

운영 배포 전에는 최소한 다음을 확인한다.

```bash
npm run lint
npm run typecheck
npm run build
```

프로젝트에 테스트가 있다면 테스트도 실행한다.

```bash
npm run test:run
npm run test:e2e
```

## Vercel Edge Functions

Vercel은 Edge Functions 배포도 지원한다.

엣지 실행은 빠른 응답에 도움이 될 수 있지만, Node.js 전체 API를 사용할 수 없는 등 제약이 있다.

처음 배포할 때는 기본 배포 흐름을 사용하고, 성능이나 지역별 응답 시간이 중요해질 때 엣지 배포를 검토하는 것이 좋다.

## 자주 하는 실수

가장 흔한 실수는 환경 변수를 로컬 `.env`에만 넣고 Vercel에 등록하지 않는 것이다.

로컬 `.env`는 Vercel 운영 환경에 자동으로 올라가지 않는다. 운영에서 필요한 값은 Vercel Settings에 등록해야 한다.

또 비밀 값을 `NUXT_PUBLIC_` 접두사로 등록하면 클라이언트에 노출될 수 있다.

## 정리

Vercel은 Nuxt 앱을 Git 저장소와 연결해 쉽게 배포할 수 있다. Nuxt/Nitro를 자동 감지하는 흐름이 강력하지만, 환경 변수와 Preview/Production 구분은 직접 신중하게 관리해야 한다.

## 다음 장으로

다음 장에서는 Netlify에 Nuxt 앱을 배포하는 방법을 배운다.
