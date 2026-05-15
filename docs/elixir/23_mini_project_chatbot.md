# 23. 실전 프로젝트 1: 기초 챗봇 로직 설계

이 프로젝트에서는 지금까지 배운 함수형 프로그래밍의 핵심(패턴 매칭, 재귀, 모듈 구조화)을 사용하여 간단하지만 확장 가능한 챗봇의 핵심 로직을 만듭니다.

---

## 1. 프로젝트 목표
사용자의 입력을 분석하여 적절한 응답을 내놓는 엔진을 만듭니다. 우리는 '동작'과 '데이터'를 철저히 분리할 것입니다.

---

## 2. 모듈 구조 잡기

### 응답 로직 (BotEngine)
패턴 매칭을 사용하여 질문에 따른 응답을 정의합니다.

```elixir
defmodule ChatBot.Engine do
  def response(input) do
    input
    |> String.downcase()
    |> String.trim()
    |> do_response()
  end

  # 비공개 함수를 통한 패턴 매칭 처리
  defp do_response("안녕"), do: "안녕하세요! 저는 Elixir 봇입니다."
  defp do_response("날씨"), do: "코딩하기 딱 좋은 날씨네요!"
  defp do_response("종료"), do: :quit
  defp do_response(_), do: "죄송해요, 무슨 말씀인지 잘 모르겠어요. '안녕' 혹은 '날씨'라고 말해보세요."
end
```

### 상호작용 로직 (BotCLI)
사용자로부터 입력을 받고 결과를 출력하는 루프(재귀)를 담당합니다.

```elixir
defmodule ChatBot.CLI do
  alias ChatBot.Engine

  def start do
    IO.puts("--- Elixir 챗봇에 오신 것을 환영합니다! ---")
    loop()
  end

  defp loop do
    input = IO.gets("> ")
    
    case Engine.response(input) do
      :quit ->
        IO.puts("안녕히 가세요!")
      msg ->
        IO.puts(msg)
        loop() # 다시 입력을 기다리는 재귀 호출
    end
  end
end
```

---

## 3. 실행해보기

`iex -S mix`로 실행한 뒤 다음 명령어로 챗봇을 깨워보세요.
```elixir
iex> ChatBot.CLI.start()
> 안녕
안녕하세요! 저는 Elixir 봇입니다.
> 날씨
코딩하기 딱 좋은 날씨네요!
> 종료
안녕히 가세요!
```

---

## 노련한 선배의 한마디 👴

> "이 간단한 챗봇에는 Elixir의 정수가 담겨 있습니다. `Engine` 모듈은 상태가 없는 **'순수 함수'**들로만 이루어져 있어 테스트하기 매우 쉽습니다. 반면 `CLI` 모듈은 사용자 입출력이라는 **'부작용(Side Effect)'**과 **'재귀'**를 담당하죠. 이렇게 로직과 부작용을 분리하는 습관이 좋은 함수형 코드를 만듭니다."

---
**다음 장 안내**: [24. 실전 프로젝트 2: 분산 모니터링 대시보드](24_mini_project_dashboard.md)
