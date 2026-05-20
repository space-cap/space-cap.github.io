# Netlify에 배포하기

## 이번 장에서 배울 것

- Netlify 배포 흐름
- Nuxt 앱의 자동 감지
- 환경 변수 설정
- Edge Functions와 On-demand Builders 개념

## Netlify란

Netlify는 정적 사이트와 서버리스 웹 앱 배포에 많이 사용되는 플랫폼이다.

Nuxt 3 앱은 Netlify에 배포할 수 있으며, Netlify는 Nuxt/Nitro 환경을 자동 감지해 최적화된 서버 결과물을 만들 수 있다.

## 기본 배포 흐름

Netlify 배포의 일반적인 흐름은 다음과 같다.

1. Nuxt 프로젝트를 Git 저장소에 올린다.
2. Netlify에서 새 사이트를 만든다.
3. Git 저장소를 연결한다.
4. 빌드 명령과 publish directory를 확인한다.
5. 환경 변수를 등록한다.
6. Deploy를 실행한다.

새 프로젝트라면 Netlify가 Nuxt 3를 감지하고 적절한 기본 설정을 제안할 수 있다.

## 빌드 명령

일반적인 빌드 명령은 다음과 같다.

```bash
npm run build
```

`package.json`

```json
{
  "scripts": {
    "build": "nuxt build"
  }
}
```

Netlify가 Nuxt 3를 감지하면 필요한 빌드 설정을 자동으로 구성할 수 있다.

## publish directory

Netlify에서는 publish directory를 확인해야 한다.

Nuxt 3 프로젝트에서는 플랫폼 감지와 preset에 따라 필요한 출력 경로가 달라질 수 있다.

Netlify 공식 Nuxt 배포 문서는 새 사이트에서 Nuxt 3를 감지하면 publish directory와 build command를 자동 설정할 수 있다고 안내한다.

직접 설정해야 하는 경우에는 현재 Nuxt/Netlify 문서를 확인해 `.output/public` 또는 플랫폼이 요구하는 경로를 맞춘다.

## 환경 변수 설정

Netlify Site settings에서 Environment variables를 등록한다.

예시는 다음과 같다.

```txt
NUXT_PUBLIC_API_BASE=https://api.example.com
NUXT_API_SECRET=secret-value
```

Nuxt의 runtime config에 정의된 키와 환경 변수 이름이 맞아야 런타임에서 올바르게 적용된다.

## Redirects

Netlify에서 redirect가 필요하면 두 가지 방향이 있다.

Nuxt의 `routeRules`를 사용할 수 있다.

```ts
export default defineNuxtConfig({
  routeRules: {
    '/old-page': {
      redirect: '/new-page'
    }
  }
})
```

또는 `public/_redirects` 파일을 둘 수도 있다.

```txt
/old-page /new-page 301
```

단순 정적 redirect는 `_redirects`가 편할 수 있고, Nuxt 앱 내부 전략과 연결된 규칙은 `routeRules`가 어울린다.

## Netlify Edge Functions

Netlify Edge Functions는 사용자와 가까운 위치에서 코드를 실행하는 기능이다.

Nuxt를 Edge Functions로 실행하려면 환경 변수로 preset을 지정할 수 있다.

```txt
SERVER_PRESET=netlify_edge
```

엣지 환경은 빠른 응답에 장점이 있지만 런타임 제약이 있을 수 있다.

## On-demand Builders

Netlify On-demand Builders는 처음 요청된 페이지를 생성하고 캐시하는 방식이다.

Nuxt에서는 다음 환경 변수로 활성화할 수 있다.

```txt
SERVER_PRESET=netlify_builder
```

콘텐츠가 많아 모든 페이지를 빌드 때 만들기 부담스럽다면 검토할 수 있다.

초보 단계에서는 기본 배포부터 성공시키고, 필요할 때 Edge Functions나 On-demand Builders를 살펴보면 된다.

## 자주 하는 실수

Netlify에 환경 변수를 등록하지 않아 운영에서 API 호출이 실패하는 경우가 많다.

또 Nuxt 2 자료를 보고 `target: 'static'` 같은 오래된 설정을 Nuxt 3 프로젝트에 적용하려는 실수도 있다. Nuxt 3 기준 문서를 확인해야 한다.

## 정리

Netlify는 Nuxt 3 앱을 자동 감지해 배포할 수 있는 플랫폼이다. 기본 배포는 Git 연결, build command, 환경 변수 설정이 핵심이다. Edge Functions와 On-demand Builders는 필요할 때 추가로 검토한다.

## 다음 장으로

다음 장에서는 GitHub Pages에 정적 Nuxt 사이트를 배포하는 방법을 배운다.
