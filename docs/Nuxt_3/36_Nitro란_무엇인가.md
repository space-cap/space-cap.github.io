# Nitro란 무엇인가

## 이번 장에서 배울 것

- Nitro가 무엇인지
- Nuxt 3에서 Nitro가 하는 일
- 서버 API와 배포 방식이 Nitro와 어떤 관련이 있는지
- 초보자가 Nitro를 어느 정도까지 이해하면 좋은지

## Nitro를 한 문장으로 말하면

Nitro는 Nuxt 3의 서버 엔진이다.

Nuxt 앱은 단순히 브라우저에서만 실행되는 프론트엔드 코드가 아니다. 서버에서 HTML을 만들고, API 요청을 처리하고, 배포 환경에 맞는 서버 결과물을 만들 수도 있다.

이 서버 쪽 일을 담당하는 핵심 엔진이 Nitro다.

## 왜 서버 엔진이 필요할까

Nuxt는 다음과 같은 일을 할 수 있다.

- 서버에서 페이지를 렌더링한다.
- `server/api` 폴더의 API를 실행한다.
- 서버 미들웨어를 실행한다.
- 정적 파일과 서버 응답을 함께 다룬다.
- 배포 환경에 맞는 결과물을 만든다.

이런 기능은 브라우저만으로는 할 수 없다. 서버에서 요청을 받고 응답을 만들어 주는 엔진이 필요하다.

Nuxt 3에서는 이 역할을 Nitro가 맡는다.

## Nitro가 제공하는 기능

Nitro는 Nuxt에 다음 기능을 제공한다.

- API routes
- server routes
- server middleware
- 서버리스 배포 지원
- Node 서버 배포 지원
- 정적 사이트와 서버 기능을 섞는 하이브리드 구조
- 빌드 후 독립 실행 가능한 서버 결과물

초보자 입장에서는 "Nuxt에서 서버 관련 기능을 가능하게 해 주는 기반"이라고 이해하면 충분하다.

## server 폴더와 Nitro

Nuxt 프로젝트에서 서버 기능은 주로 `server` 폴더에 작성한다.

```txt
server/
  api/
    hello.get.ts
  routes/
    health.get.ts
  middleware/
    log.ts
  utils/
    response.ts
```

Nuxt는 이 폴더를 읽고 Nitro 서버에 등록한다.

예를 들어 `server/api/hello.get.ts` 파일을 만들면 `/api/hello` 주소로 접근할 수 있는 API가 생긴다.

## 직접 API 호출

Nuxt 서버에서 자신의 API를 호출할 때 `$fetch`를 사용하면 Nitro가 효율적으로 처리할 수 있다.

브라우저에서 `$fetch('/api/hello')`를 호출하면 실제 HTTP 요청이 발생한다.

하지만 서버에서 같은 API를 호출할 때는 Nitro가 내부 함수를 직접 호출하는 방식으로 처리할 수 있어 불필요한 네트워크 요청을 줄일 수 있다.

처음에는 이 정도만 기억하면 된다.

- 브라우저에서 호출하면 HTTP 요청
- 서버에서 내부 API를 호출하면 더 효율적인 직접 호출 가능

## 빌드 결과와 Nitro

Nuxt 앱을 빌드하면 `.output` 폴더가 만들어진다.

```bash
npm run build
```

```txt
.output/
  public/
  server/
```

이 결과물은 Nuxt 앱을 운영 환경에서 실행하는 데 사용된다. Nitro는 이 서버 결과물을 만들어 다양한 환경에 배포할 수 있게 돕는다.

## 초보자가 알아야 할 수준

처음부터 Nitro의 내부 구조를 깊게 알 필요는 없다.

지금은 다음만 기억하자.

- Nitro는 Nuxt 3의 서버 엔진이다.
- `server/api`에 API를 만들 수 있게 해 준다.
- 서버 미들웨어와 서버 유틸도 Nitro 영역이다.
- 배포할 때 서버 결과물을 만드는 데 관여한다.

## 자주 하는 실수

Nuxt의 라우트 미들웨어와 서버 미들웨어를 헷갈리는 경우가 많다.

라우트 미들웨어는 페이지 이동 전에 실행된다.

서버 미들웨어는 서버 요청이 들어올 때 실행된다.

둘은 이름은 비슷하지만 실행 위치와 목적이 다르다.

## 정리

Nitro는 Nuxt 3의 서버 엔진이다. Nuxt가 SSR, API routes, server middleware, 다양한 배포 방식을 지원할 수 있는 기반을 제공한다.

## 다음 장으로

다음 장에서는 `server/api` 폴더를 사용해 실제 서버 API를 만드는 방법을 배운다.
