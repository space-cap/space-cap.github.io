# Nitro Preset과 배포 환경

## 이번 장에서 배울 것

- Nitro preset이 무엇인지
- Node 서버, 서버리스, 엣지, 정적 배포의 차이
- 플랫폼이 preset을 자동 감지하는 흐름
- 배포 환경을 고를 때의 기준

## Nitro preset이란

Nitro preset은 Nuxt 앱을 어떤 환경에서 실행할지에 맞춰 빌드 결과를 조정하는 설정이다.

Nuxt는 Nitro를 통해 다양한 환경에 배포할 수 있다.

- Node.js 서버
- Vercel
- Netlify
- Cloudflare Workers
- AWS Lambda
- 정적 호스팅
- GitHub Pages

각 환경은 서버 실행 방식이 다르다. Nitro preset은 이런 차이에 맞는 결과물을 만든다.

## 기본 Node 서버

일반적인 서버 환경에서는 Node.js 서버로 실행할 수 있다.

빌드한다.

```bash
npm run build
```

실행한다.

```bash
node .output/server/index.mjs
```

Node 서버 방식은 다음 상황에 어울린다.

- 직접 서버를 운영한다.
- VPS나 VM에 배포한다.
- Docker 컨테이너로 실행한다.
- Node 런타임을 직접 제어하고 싶다.

## 서버리스 배포

Vercel, Netlify 같은 플랫폼은 서버리스 함수 기반으로 Nuxt 서버 기능을 실행할 수 있다.

개발자는 보통 별도 서버를 직접 관리하지 않는다.

장점은 다음과 같다.

- 배포가 쉽다.
- 트래픽에 따라 확장된다.
- Git push 기반 자동 배포가 쉽다.

주의할 점도 있다.

- cold start가 있을 수 있다.
- 파일 시스템 쓰기 제한이 있을 수 있다.
- 실행 시간 제한이 있을 수 있다.
- 플랫폼별 제약을 알아야 한다.

## 엣지 배포

엣지 환경은 사용자와 가까운 위치에서 코드를 실행하는 방식이다.

응답 지연을 줄일 수 있지만, Node.js 전체 API를 사용할 수 없는 경우가 있다.

엣지 배포를 선택할 때는 사용하는 라이브러리가 엣지 런타임에서 동작하는지 확인해야 한다.

## 정적 배포

정적 배포는 HTML, CSS, JS 파일만 배포하는 방식이다.

GitHub Pages 같은 환경은 정적 사이트만 지원한다.

정적 배포는 단순하고 빠르지만 서버 API나 SSR이 필요한 기능은 사용할 수 없다.

정적 배포에 어울리는 사이트는 다음과 같다.

- 문서 사이트
- 블로그
- 회사 소개 페이지
- 포트폴리오

## preset 자동 감지

Vercel과 Netlify 같은 플랫폼은 Nuxt/Nitro 앱을 자동 감지하고 알맞은 빌드 방식을 적용할 수 있다.

그래서 많은 경우 별도 preset 설정 없이 Git 저장소를 연결하고 배포할 수 있다.

하지만 GitHub Pages처럼 정적 호스팅 제약이 명확한 경우에는 `github_pages` preset과 base URL 설정이 필요할 수 있다.

## preset 명시하기

필요하다면 빌드 명령에서 preset을 명시할 수 있다.

```bash
npx nuxt build --preset node_server
```

GitHub Pages 예시는 다음과 같다.

```bash
npx nuxt build --preset github_pages
```

플랫폼마다 권장 preset과 설정이 다를 수 있으므로 배포 전 공식 문서를 확인한다.

## 배포 환경 선택 기준

처음에는 다음 기준으로 선택하자.

| 상황 | 추천 방향 |
| --- | --- |
| 블로그, 문서 사이트 | 정적 배포 |
| SSR과 서버 API가 필요함 | Vercel, Netlify, Node 서버 |
| 직접 서버 제어가 필요함 | Node 서버 또는 Docker |
| 빠른 글로벌 응답이 중요함 | 엣지 배포 검토 |
| GitHub 저장소 기반 무료 정적 배포 | GitHub Pages |

## 자주 하는 실수

서버 API를 사용하는 앱을 GitHub Pages에 그대로 배포하려는 실수가 많다.

GitHub Pages는 정적 사이트만 지원한다. `server/api` 기능이 필요한 앱은 서버 실행을 지원하는 플랫폼을 선택해야 한다.

또 엣지 배포에서 Node 전용 라이브러리를 사용해 오류가 나는 경우도 있다. 런타임 제약을 확인해야 한다.

## 정리

Nitro preset은 Nuxt 앱을 실행할 배포 환경에 맞게 빌드 결과를 조정한다. Vercel과 Netlify는 많은 경우 자동 감지되지만, 정적 호스팅이나 특수 환경에서는 preset과 설정을 명확히 지정해야 한다.

## 다음 장으로

다음 장에서는 Vercel에 Nuxt 앱을 배포하는 기본 흐름을 배운다.
