# 20. 테스트: 잠들기 전의 안식

Elixir 개발자가 밤에 발을 뻗고 잘 수 있는 이유는 바로 강력한 테스트 코드가 시스템을 지탱해주고 있기 때문입니다. Elixir는 **ExUnit**이라는 훌륭한 테스트 프레임워크를 기본으로 내장하고 있습니다.

---

## 1. ExUnit 기초

프로젝트를 만들면 `test` 폴더에 이미 기본 테스트 파일이 있습니다.

```elixir
defmodule MathTest do
  use ExUnit.Case

  test "1 더하기 1은 2여야 합니다" do
    assert 1 + 1 == 2
  end

  test "실패하는 테스트 예시" do
    assert 2 + 2 == 5, "2 더하기 2가 5가 아니라니!"
  end
end
```

터미널에서 실행: `mix test`

---

## 2. Doctests: 문서가 곧 테스트다

Elixir의 가장 환상적인 특징 중 하나입니다. 모듈 문서(`@doc`)에 쓴 예제 코드가 실제로 맞는지 테스트합니다. 문서와 실제 코드가 불일치하는 일을 원천 차단합니다.

```elixir
defmodule Math do
  @doc """
  두 수를 더합니다.

  ## 예제
      iex> Math.add(1, 2)
      3
  """
  def add(a, b), do: a + b
end
```
테스트 파일에서 `doctest Math` 한 줄만 추가하면 됩니다.

---

## 3. 테스트 설정과 구조화 (Setup)

모든 테스트 전에 공통으로 필요한 데이터가 있다면 `setup` 블록을 사용합니다.

```elixir
setup do
  # 테스트용 데이터 준비
  {:ok, user: %{name: "호세", age: 30}}
end

test "사용자 이름 확인", %{user: user} do
  assert user.name == "호세"
end
```

---

## 4. 동시 테스트: 속도의 비밀

Elixir 프로세스는 격리되어 있으므로, 수천 개의 테스트를 **동시에 실행**해도 서로 간섭하지 않습니다. `async: true` 옵션 하나로 테스트 시간을 획기적으로 줄일 수 있습니다.

```elixir
use ExUnit.Case, async: true
```

---

## 노련한 선배의 한마디 👴

> "테스트 코드는 미래의 여러분에게 보내는 선물입니다. 기능을 수정할 때마다 '다른 게 고장 나지 않았을까?' 걱정하지 마세요. 잘 작성된 테스트 슈트가 있다면 버튼 하나로 확신을 얻을 수 있습니다. 특히 **Doctest**는 다른 개발자(혹은 사용자)에게 가장 신뢰받는 문서가 될 것입니다."

---
**다음 장 안내**: [21. 배포 전략 (Deployment and Releases)](21_deployment_and_releases.md)
