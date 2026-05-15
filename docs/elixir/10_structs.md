# 10. 구조체: 타입이 있는 데이터 그릇

우리는 이미 '맵(Map)'을 배웠습니다. 하지만 맵은 너무 자유로워서 어떤 데이터가 들어있는지 보장하기 힘듭니다. 이때 필요한 것이 바로 **구조체(Structs)**입니다.

---

## 1. 구조체 정의하기

구조체는 반드시 모듈 내부에서 `defstruct` 키워드로 정의해야 합니다.

```elixir
defmodule User do
  defstruct [:name, :age, :email]
  # 또는 기본값을 지정할 수 있습니다.
  # defstruct name: "Guest", age: 0, email: nil
end
```

---

## 2. 구조체 사용하기

구조체는 `%{User{...}}` 문법을 사용합니다.

```elixir
# 생성
iex> user = %User{name: "호세", age: 30}
%User{age: 30, email: nil, name: "호세"}

# 값 읽기 (맵과 동일하지만 점(.) 문법을 권장)
iex> user.name
"호세"

# 업데이트
iex> updated_user = %{user | age: 31}
%User{age: 31, email: nil, name: "호세"}
```

---

## 3. 구조체와 맵의 차이점 (중요!)

구조체는 본질적으로 **'이름표가 붙은 맵'**이지만, 맵과는 다른 중요한 제약 사항이 있습니다.

1. **키 제한**: 구조체 정의 시 명시한 키만 사용할 수 있습니다. 새로운 키를 마음대로 추가할 수 없습니다.
2. **함수 미지원**: `Map` 모듈의 많은 함수는 구조체에서 동작하지만, 구조체는 맵이 아니므로 `is_map/1`은 `true`를 반환하되 `is_struct/1`로 구분해야 합니다.
3. **점(.) 문법**: 구조체에서 존재하지 않는 키에 점으로 접근하면 컴파일 시점에 에러를 잡아낼 수 있어 훨씬 안전합니다.

---

## 4. 필수 키 지정하기

특정 필드 없이는 구조체를 만들 수 없게 강제하고 싶다면 `@enforce_keys`를 사용합니다.

```elixir
defmodule User do
  @enforce_keys [:name]
  defstruct [:name, :age]
end

# 에러 발생!
# iex> %User{age: 30}
# ** (ArgumentError) the following keys must also be given when building struct User: [:name]
```

---

## 5. 실전 활용: 데이터 유효성 검사

구조체는 데이터의 형태를 보장하므로, 함수의 인자에서 패턴 매칭을 할 때 매우 유용합니다.

```elixir
defmodule Greeter do
  # User 구조체인 경우에만 실행됨
  def welcome(%User{name: name}) do
    "환영합니다, #{name}님!"
  end
end
```

---

## 노련한 작가의 팁 💡

| 그릇 | 특징 | 언제 사용하는가? |
| :--- | :--- | :--- |
| **Map** | 유연함, 키 자유로움 | 데이터 형태가 동적이거나 간단할 때 |
| **Struct** | 엄격함, 명확함 | 도메인 모델, 고정된 데이터 스키마 정의 시 |

---

## 노련한 선배의 한마디 👴

> "맵은 '아무거나 담을 수 있는 비닐봉지'라면, 구조체는 '칸이 정해진 약 상자'와 같습니다. 실무에서는 가능한 한 데이터를 구조체로 정의하세요. 코드를 읽는 동료(혹은 미래의 나)가 '여기에 어떤 데이터가 들어있지?'라고 고민하는 시간을 획기적으로 줄여줄 것입니다."

---
**다음 장 안내**: [11. 프로토콜과 행동 (Protocols and Behaviours)](11_protocols_and_behaviours.md)
