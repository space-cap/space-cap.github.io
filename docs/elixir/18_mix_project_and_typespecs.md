# 18. Mix와 품질 관리: 전문가의 도구 상자

이제 터미널에서 `iex`만 치는 단계를 넘어, 실제 프로젝트를 관리하고 품질을 높이는 법을 배웁니다. 전문가는 도구를 탓하지 않지만, 좋은 도구는 반드시 챙깁니다.

---

## 1. Mix: Elixir 프로젝트 매니저

`Mix`는 프로젝트 생성, 컴파일, 테스트, 의존성 관리를 모두 수행합니다.

```bash
# 새 프로젝트 생성
mix new my_app

# 의존성 가져오기
mix deps.get

# 프로젝트 실행 (IEx와 함께)
iex -S mix
```

---

## 2. Typespecs: 정적 분석의 힘

Elixir는 동적 타입 언어이지만, `@spec`을 통해 타입을 명시할 수 있습니다. 이는 문서화에도 좋고, **Dialyzer**라는 도구를 통해 잠재적인 버그를 잡아내는 데도 탁월합니다.

```elixir
defmodule Math do
  @typedoc "숫자 또는 소수점"
  @type number_type :: integer() | float()

  @doc "두 수를 더합니다."
  @spec add(number_type(), number_type()) :: number_type()
  def add(a, b), do: a + b
end
```

---

## 3. ExDoc: 아름다운 문서화

Elixir의 철학 중 하나는 **"문서화는 일등 시민이다"**입니다. 코드 안에 쓴 `@doc`과 `@moduledoc`을 바탕으로 HTML 문서를 자동으로 생성합니다.

```elixir
defmodule Calculator do
  @moduledoc """
  계산기 모듈입니다.
  매우 정밀한 계산을 보장합니다.
  """

  @doc "제곱을 구합니다."
  def square(n), do: n * n
end
```
`mix docs` 명령어를 실행하면 브라우저에서 볼 수 있는 멋진 문서가 탄생합니다.

---

## 4. 의존성 관리 (Hex)

Elixir의 패키지 매니저는 **Hex**입니다. `mix.exs` 파일의 `deps` 함수 안에 필요한 라이브러리를 추가합니다.

```elixir
defp deps do
  [
    {:jason, "~> 1.4"}, # JSON 라이브러리
    {:httpoison, "~> 2.0"} # HTTP 클라이언트
  ]
end
```

---

## 노련한 선배의 한마디 👴

> "훌륭한 코드는 읽기 쉬운 코드입니다. `@spec`으로 인자와 반환값의 타입을 명시하고, `@doc`으로 친절하게 설명하는 습관을 들이세요. 그것이 바로 '코딩하는 사람'과 '소프트웨어 엔지니어'를 구분 짓는 기준입니다. 이제 우리의 코드를 꼼꼼히 검증하고 배포할 준비를 해봅시다!"

---
**다음 장 안내**: [19. 디버깅 기술 (Debugging)](19_debugging.md)
