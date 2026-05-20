# 사이트맵과 robots

## 이번 장에서 배울 것

- sitemap이 무엇인지
- robots.txt가 무엇인지
- Nuxt에서 sitemap과 robots를 준비하는 방법
- robots.txt가 보안 장치가 아니라는 점

## sitemap이란

sitemap은 검색 엔진에게 사이트의 주요 URL 목록을 알려 주는 파일이다.

보통 XML 형식으로 제공된다.

```txt
/sitemap.xml
```

검색 엔진은 sitemap을 참고해 어떤 페이지가 있는지 더 쉽게 파악할 수 있다.

특히 다음 사이트에 유용하다.

- 페이지가 많은 블로그
- 상품 상세 페이지가 많은 쇼핑몰
- 동적 URL이 많은 서비스
- 새 콘텐츠가 자주 생기는 사이트

## robots.txt란

`robots.txt`는 검색 엔진 크롤러에게 어떤 경로를 크롤링해도 되는지 안내하는 파일이다.

보통 사이트 루트에 둔다.

```txt
/robots.txt
```

간단한 예시는 다음과 같다.

```txt
User-agent: *
Allow: /
Sitemap: https://example.com/sitemap.xml
```

모든 크롤러에게 전체 사이트 접근을 허용하고 sitemap 위치를 알려 준다.

## robots.txt는 보안이 아니다

중요한 점이 있다.

`robots.txt`는 보안 장치가 아니다.

성실한 크롤러에게 "여기는 크롤링하지 말아 주세요"라고 안내하는 파일일 뿐이다. 악의적인 사용자는 무시할 수 있다.

따라서 관리자 페이지나 비공개 API를 robots.txt로 막는다고 안전해지지 않는다.

보안이 필요한 페이지는 인증과 권한 검증으로 보호해야 한다.

## public 폴더에 직접 만들기

가장 단순한 방법은 `public` 폴더에 `robots.txt`를 만드는 것이다.

```txt
public/
  robots.txt
```

```txt
User-agent: *
Allow: /
Sitemap: https://example.com/sitemap.xml
```

이 파일은 브라우저에서 `/robots.txt`로 접근할 수 있다.

정적이고 단순한 사이트라면 이 방식도 충분하다.

## sitemap 모듈 사용하기

페이지가 많거나 동적 URL이 많다면 sitemap 모듈을 사용할 수 있다.

```bash
npx nuxi@latest module add @nuxtjs/sitemap
```

설치 후 `nuxt.config.ts`에서 사이트 URL을 설정한다.

```ts
export default defineNuxtConfig({
  modules: ['@nuxtjs/sitemap'],
  site: {
    url: 'https://example.com',
    name: 'Nuxt 학습 사이트'
  }
})
```

모듈 설정은 버전에 따라 달라질 수 있으므로 실제 프로젝트에서는 공식 문서를 확인한다.

## robots 모듈 사용하기

환경별로 robots 정책을 다르게 가져가야 한다면 robots 모듈을 사용할 수 있다.

예를 들어 운영 사이트는 허용하고, staging 사이트는 검색 노출을 막고 싶을 수 있다.

```bash
npx nuxi@latest module add robots
```

또는 Nuxt SEO 계열 모듈을 함께 사용할 수도 있다.

모듈을 선택할 때는 현재 Nuxt 버전과 모듈 문서를 확인하는 것이 좋다.

## 어떤 페이지를 sitemap에 넣을까

sitemap에는 검색 결과에 노출되길 원하는 대표 페이지를 넣는다.

예를 들어 다음 페이지는 sitemap에 어울린다.

- 홈
- 블로그 글
- 문서 페이지
- 상품 상세
- 카테고리 페이지

반대로 다음 페이지는 보통 sitemap에 넣지 않는다.

- 로그인 페이지
- 관리자 페이지
- 마이페이지
- 검색 결과 페이지
- 임시 페이지
- 중복 콘텐츠 페이지

## noindex와 robots.txt

특정 페이지를 검색 결과에 노출하고 싶지 않다면 meta robots의 `noindex`를 사용할 수 있다.

```vue
<script setup>
useHead({
  meta: [
    { name: 'robots', content: 'noindex, nofollow' }
  ]
})
</script>
```

robots.txt는 크롤링 경로를 안내하고, `noindex`는 검색 결과 색인을 막는 데 사용된다.

상황에 따라 둘을 구분해서 사용해야 한다.

## 자주 하는 실수

개발 서버나 staging 사이트가 검색 엔진에 노출되는 경우가 있다.

운영이 아닌 환경에서는 robots 정책과 인증 설정을 확인해야 한다.

또 robots.txt로 비밀 URL을 숨기려는 실수도 많다. robots.txt는 누구나 볼 수 있는 공개 파일이다.

## 정리

sitemap은 검색 엔진에게 주요 URL 목록을 알려 주는 파일이고, robots.txt는 크롤러에게 접근 정책을 안내하는 파일이다. 둘 다 SEO에 도움이 되지만 보안 장치는 아니다.

## 다음 장으로

다음 장에서는 Markdown 기반 콘텐츠를 다룰 수 있는 Nuxt Content를 배운다.
