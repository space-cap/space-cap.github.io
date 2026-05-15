# 09. 모듈과 코드 조직화: 아름답고 질서 있는 코드

이제 단편적인 코드를 넘어, 실제 애플리케이션을 구축하기 위해 코드를 어떻게 묶고 관리하는지 배울 시간입니다. Elixir의 코드는 **모듈(Module)** 안에 정의되며, **함수(Function)**를 통해 동작합니다.

---

## 1. 모듈과 함수 정의

`defmodule`과 `def` 키워드를 사용합니다.

```elixir
defmodule Math do
  @doc "두 수를 더합니다."
  def add(a, b) do
    a + b
  end

  # 한 줄로 정의하기
  def subtract(a, b), do: a - b

  # 비공개 함수 (모듈 내부에서만 사용 가능)
  defp private_logic(x) do
    x * 2
  end
end
```

---

## 2. 파이프 연산자 (`|>`): 가독성의 혁명

Elixir 개발자가 가장 사랑하는 연산자입니다. **왼쪽의 결과를 오른쪽 함수의 첫 번째 인자로 전달**합니다.

### 전 (기존 방식 - 읽기 힘들고 안에서부터 읽어야 함)
```elixir
String.capitalize(String.trim(String.downcase("  ELIXIR  ")))
```

### 후 (파이프 방식 - 흐름이 명확함)
```elixir
"  ELIXIR  "
|> String.downcase()
|> String.trim()
|> String.capitalize()
# 결과: "Elixir"
```

---

## 3. 코드 조직화의 도구들

프로젝트가 커지면 다른 모듈을 가져와 써야 합니다. 이때 다음 네 가지를 구분해서 사용합니다.

### ① alias
모듈 이름이 너무 길 때 별칭을 줍니다. 가장 많이 사용됩니다.
```elixir
alias MyProject.Users.Profiles, as: Profiles
# 이제 Profiles.get_name()으로 쓸 수 있습니다.
```

### ② import
다른 모듈의 함수를 내 모듈의 함수처럼 직접 쓰게 해줍니다.
```elixir
import Integer, only: [is_even: 1]
is_even(2) # true (Integer.is_even 대신 직접 호출)
```

### ③ require
매크로가 포함된 모듈을 사용할 때 컴파일러에게 알리는 용도입니다. (나중에 심화에서 다룹니다.)

### ④ use
다른 모듈의 코드를 현재 모듈로 **'주입'**합니다. 주로 프레임워크나 라이브러리(Phoenix 등)를 설정할 때 사용합니다.

---

## 4. 함수의 다중 정의와 가드 (Guards)

같은 이름의 함수를 인자의 패턴에 따라 여러 번 정의할 수 있습니다. `when`을 사용해 더 정밀하게 제어할 수 있습니다.

```elixir
defmodule Checker do
  def check(n) when n > 0, do: "양수"
  def check(n) when n < 0, do: "음수"
  def check(0), do: "영"
end
```

---

## 노련한 작가의 요약표 👴

| 도구 | 주 용도 | 특징 |
| :--- | :--- | :--- |
| **alias** | 긴 모듈명 줄이기 | **가장 안전하고 권장됨** |
| **import** | 특정 함수 직접 호출하기 | 너무 많이 쓰면 함수 출처가 불명확해짐 |
| **use** | 모듈 설정 주입하기 | 해당 모듈의 `__using__` 매크로를 실행함 |
| **|>** | 데이터 흐름 연결 | 함수형 프로그래밍의 정수 |

---

## 노련한 선배의 한마디 👴

> "파이프 연산자는 단순히 코드를 예쁘게 만드는 것이 아닙니다. 데이터를 단계별로 어떻게 가공하는지 그 **'의도'**를 명확하게 드러내는 설계 도구입니다. 복잡한 로직을 짤 때 종이 위에 파이프라인을 먼저 그려보세요. 코딩이 훨씬 즐거워질 것입니다."

---
**다음 장 안내**: [10. 구조체 (Structs)](10_structs.md)
