# SSG와 프리렌더링

## 이번 장에서 배울 것

- SSG가 무엇인지
- 프리렌더링이 무엇인지
- `npm run generate`와 `nuxt build --prerender`
- `routeRules`와 `nitro.prerender`

## SSG란

SSG는 Static Site Generation의 줄임말이다.

페이지를 사용자가 요청할 때 서버에서 매번 만드는 것이 아니라, 빌드할 때 미리 HTML 파일로 만들어 두는 방식이다.

예를 들어 블로그 글, 문서 사이트, 회사 소개 페이지처럼 내용이 자주 바뀌지 않는 페이지에 잘 어울린다.

## 프리렌더링이란

프리렌더링은 Nuxt가 특정 route를 미리 HTML로 렌더링해 두는 과정이다.

브라우저가 페이지를 요청하면 이미 만들어진 HTML을 바로 받을 수 있다.

장점은 다음과 같다.

- 빠른 응답
- 단순한 정적 호스팅 가능
- 검색 엔진이 읽기 쉬운 HTML 제공
- 서버 실행 비용 감소

단점도 있다.

- 사용자마다 다른 데이터에는 부적합할 수 있다.
- 콘텐츠가 바뀌면 다시 빌드해야 한다.
- 동적 경로를 모두 알기 어려울 수 있다.

## generate 명령

정적 사이트를 생성하려면 다음 명령을 사용할 수 있다.

```bash
npm run generate
```

`package.json`에는 보통 다음 script가 있다.

```json
{
  "scripts": {
    "generate": "nuxt generate"
  }
}
```

Nuxt는 정적으로 생성 가능한 페이지를 HTML로 만든다.

## build --prerender

Nuxt에서는 다음 명령으로도 프리렌더링을 실행할 수 있다.

```bash
npx nuxt build --prerender
```

GitHub Pages 같은 정적 호스팅에 배포할 때 플랫폼 preset과 함께 사용될 수 있다.

## routeRules로 프리렌더링 지정하기

`nuxt.config.ts`에서 route별 규칙을 지정할 수 있다.

```ts
export default defineNuxtConfig({
  routeRules: {
    '/': { prerender: true },
    '/about': { prerender: true },
    '/admin/**': { ssr: false }
  }
})
```

이 예시는 홈과 소개 페이지를 미리 생성하고, 관리자 페이지는 클라이언트에서만 렌더링하도록 설정한다.

`routeRules`는 프리렌더링뿐 아니라 캐시, SSR 여부 등 route별 전략을 정할 때도 사용한다.

## nitro.prerender

프리렌더링할 route를 더 명시적으로 지정하려면 `nitro.prerender`를 사용할 수 있다.

```ts
export default defineNuxtConfig({
  nitro: {
    prerender: {
      routes: ['/sitemap.xml', '/robots.txt']
    }
  }
})
```

Nuxt 3에서는 프리렌더링 설정에 `nitro.prerender`를 사용하는 흐름이 권장된다.

## 동적 페이지 프리렌더링

동적 페이지는 빌드 시점에 어떤 경로를 만들지 알아야 한다.

예를 들어 다음 페이지가 있다고 하자.

```txt
pages/posts/[slug].vue
```

빌드 시점에 `/posts/hello`, `/posts/nuxt-data` 같은 경로를 알려 줘야 해당 HTML을 미리 만들 수 있다.

Nuxt Content나 API에서 글 목록을 가져와 경로를 생성하는 방식은 뒤의 실습 프로젝트에서 다시 다룬다.

## 프리렌더링에 적합한 페이지

다음 페이지는 프리렌더링에 적합하다.

- 홈
- 소개
- 블로그 글
- 문서
- 제품 소개
- 가격 정책
- 도움말

다음 페이지는 신중해야 한다.

- 로그인 사용자별 대시보드
- 마이페이지
- 관리자 페이지
- 실시간 데이터 페이지
- 권한별로 내용이 다른 페이지

## 자주 하는 실수

동적 페이지를 만들었는데 프리렌더링할 경로를 알려 주지 않아 배포 후 404가 나는 경우가 있다.

또 사용자별 데이터가 있는 페이지를 정적으로 생성하면 잘못된 HTML이 공유될 수 있다.

프리렌더링은 "모든 사용자에게 같은 내용을 보여 줘도 되는가?"를 먼저 생각해야 한다.

## 정리

SSG는 페이지를 빌드 시점에 미리 HTML로 만드는 방식이다. Nuxt에서는 `nuxt generate`, `nuxt build --prerender`, `routeRules`, `nitro.prerender`를 통해 정적 생성과 프리렌더링을 구성할 수 있다.

## 다음 장으로

다음 장에서는 Nitro preset과 배포 환경의 차이를 배운다.
