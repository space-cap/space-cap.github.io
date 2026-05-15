# 11. 프로토콜과 행동: 상속 없는 다형성

Elixir는 클래스 상속이 없습니다. 대신 서로 다른 데이터 타입이나 모듈이 동일한 방식으로 동작하게 만드는 두 가지 강력한 도구, **프로토콜(Protocols)**과 **행동(Behaviours)**을 사용합니다.

---

## 1. 프로토콜 (Protocols)

프로토콜은 **데이터 타입에 따른 다형성**을 지원합니다. OOP의 인터페이스나 추상 클래스와 비슷하지만, 기존 코드를 수정하지 않고도 새로운 데이터 타입을 추가할 수 있다는 점이 다릅니다.

### 프로토콜 정의
```elixir
defprotocol Blank do
  @doc "데이터가 비어 있는지 확인합니다."
  def empty?(data)
end
```

### 프로토콜 구현
```elixir
# 리스트에 대한 구현
defimpl Blank, for: List do
  def empty?([]), do: true
  def empty?(_), do: false
end

# 맵에 대한 구현
defimpl Blank, for: Map do
  def empty?(map), do: map == %{}
end

# 정수에 대한 구현 (항상 false)
defimpl Blank, for: Integer do
  def empty?(_), do: false
end
```

이제 `Blank.empty?([1, 2])`나 `Blank.empty?(%{})`처럼 데이터 타입에 상관없이 동일한 함수를 호출할 수 있습니다.

---

## 2. 행동 (Behaviours)

행동은 **모듈 간의 규약**을 정의합니다. 특정 모듈이 반드시 구현해야 할 함수의 목록을 지정하며, OOP의 인터페이스와 더 가깝습니다.

### 행동 정의
```elixir
defmodule Parser do
  @callback parse(String.t()) :: {:ok, term()} | {:error, String.t()}
end
```

### 행동 구현
`@behaviour`를 사용합니다. 규약을 지키지 않으면 컴파일러가 경고를 줍니다.
```elixir
defmodule JSONParser do
  @behaviour Parser

  def parse(str) do
    # JSON 파싱 로직
    {:ok, "parsed json"}
  end
end
```

---

## 3. 언제 무엇을 쓰는가?

| 도구 | 대상 | 목적 | 비유 |
| :--- | :--- | :--- | :--- |
| **Protocol** | **데이터 타입** (List, Map, Struct 등) | 데이터에 따른 다른 동작 구현 | "여러 종류의 악기가 '연주하다'라는 기능을 가짐" |
| **Behaviour** | **모듈** | 공통된 인터페이스 규격화 | "여러 회사의 충전기가 '충전 단자' 규격을 따름" |

---

## 노련한 선배의 한마디 👴

> "상속은 코드를 복잡하게 꼬이게 만드는 주범이 될 때가 많습니다. Elixir의 프로토콜은 '데이터가 무엇인가(Is-a)'보다 **'데이터가 무엇을 할 수 있는가(Can-do)'**에 집중하게 합니다. 이것이 바로 유연하고 확장 가능한 시스템을 만드는 비결입니다."

---
**다음 장 안내**: [12. 파일 및 IO (File and I/O)](12_file_and_io.md)
