# 13. 재귀와 Enum: 반복을 예술로 승화하기

Elixir에는 `for`나 `while` 루프가 없습니다. 데이터가 불변(Immutable)이기 때문에 인덱스 값을 바꿔가며 반복하는 것이 불가능하기 때문이죠. 대신 우리는 **재귀(Recursion)**와 **Enum 모듈**을 사용합니다.

---

## 1. 재귀 (Recursion)

재귀는 함수가 자기 자신을 다시 호출하는 기법입니다. Elixir 반복의 가장 근본적인 원리입니다.

```elixir
defmodule Math do
  # 종료 조건 (Base Case)
  def sum_list([], acc), do: acc
  # 재귀 단계 (Recursive Step)
  def sum_list([head | tail], acc) do
    sum_list(tail, acc + head)
  end
end

Math.sum_list([1, 2, 3], 0) # 결과: 6
```

> [!TIP]
> Elixir 컴파일러는 **꼬리 재귀 최적화(Tail Call Optimization)**를 지원합니다. 재귀 호출이 함수의 마지막 동작이라면, 아무리 많이 호출해도 스택 오버플로우가 발생하지 않고 루프만큼 빠릅니다.

---

## 2. Enum 모듈: 반복의 만능 도구

재귀를 매번 직접 짜는 것은 번거롭습니다. `Enum` 모듈은 컬렉션을 다루는 거의 모든 기능을 제공합니다.

```elixir
# 모든 요소에 2 곱하기
iex> Enum.map([1, 2, 3], fn x -> x * 2 end)
[2, 4, 6]

# 짝수만 골라내기
iex> Enum.filter([1, 2, 3, 4], fn x -> rem(x, 2) == 0 end)
[2, 4]

# 합계 구하기
iex> Enum.reduce([1, 2, 3], 0, fn x, acc -> x + acc end)
6
```

---

## 3. 리스트 컴프리헨션 (Comprehensions)

`for` 키워드를 사용하지만, 다른 언어의 루프와는 다릅니다. 이는 데이터를 걸러내고(filter) 새로운 형태를 생성하는(map) 더 직관적인 문법입니다.

```elixir
# 1부터 5까지 숫자 중 짝수의 제곱 구하기
iex> for n <- 1..5, rem(n, 2) == 0, do: n * n
[4, 16]

# 여러 리스트의 조합 만들기
iex> for x <- [1, 2], y <- [3, 4], do: {x, y}
[{1, 3}, {1, 4}, {2, 3}, {2, 4}]
```

---

## 4. Enum vs Stream 다시 보기

- **Enum**: 연산 결과를 즉시 리스트로 만듭니다. (메모리 사용)
- **Stream**: 연산 순서만 정해두고 나중에 한꺼번에 처리합니다. (지연 연산)

---

## 노련한 선배의 한마디 👴

> "처음에는 재귀가 어렵게 느껴질 수 있습니다. '어떻게 반복하지?'라는 생각 대신 **'데이터의 구조를 어떻게 쪼갤까?'**라고 생각해보세요. 리스트를 머리(Head)와 꼬리(Tail)로 나누어 처리하는 방식에 익숙해지면, 복잡한 데이터 구조도 아주 간결하게 다룰 수 있게 됩니다. 이제 Elixir의 진수인 '프로세스'로 넘어갈 준비가 되셨나요?"

---
**다음 장 안내**: [14. 프로세스 기초: Task와 Agent (Processes, Task, and Agent)](14_processes_task_agent.md)
