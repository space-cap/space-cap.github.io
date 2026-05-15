# 08. 예외 처리와 철학: 죽어야 사는 시스템

Elixir의 예외 처리는 다른 언어와 문법은 비슷해 보일지 몰라도, 그 밑에 깔린 철학은 완전히 다릅니다. 이 장을 이해하면 여러분은 진정한 BEAM 개발자로 거듭나게 될 것입니다.

---

## 1. 에러 발생시키기 (raise)

프로그램 실행 중 강제로 에러를 발생시킵니다.

```elixir
iex> raise "심각한 오류 발생!"
** (RuntimeError) 심각한 오류 발생!

# 특정 에러 타입 지정
iex> raise ArgumentError, message: "잘못된 인자입니다"
```

---

## 2. try, rescue, after

전통적인 예외 처리 방식입니다. 하지만 Elixir에서는 **꼭 필요한 경우가 아니면 사용을 권장하지 않습니다.**

```elixir
try do
  raise "에러!"
rescue
  e in RuntimeError -> IO.puts("에러 잡았다: #{e.message}")
after
  IO.puts("에러가 나든 안 나든 실행됩니다.")
end
```

---

## 3. Elixir의 독특한 예외 처리 방식

### ① "Let it crash" (그냥 죽게 내버려 둬라)
BEAM 생태계의 대원칙입니다. 우리는 모든 예외 상황을 `try/rescue`로 꼼꼼히 막으려 애쓰지 않습니다. 대신 **예상치 못한 상황이 오면 프로세스를 기분 좋게 죽게 둡니다.** 

왜냐고요? Elixir 시스템에는 죽은 프로세스를 즉시 살려내는 **Supervisor(감시자)**가 있기 때문입니다. 에러가 난 오염된 상태를 억지로 붙들고 있는 것보다, 깨끗한 상태로 다시 시작하는 것이 훨씬 안전하다는 철학입니다.

### ② 실패할 수도 있는 함수 관례
Elixir에서는 예외를 던지는 대신 **결과를 튜플로 반환**하는 방식을 선호합니다.

- `File.read("test.txt")` -> `{:ok, contents}` 또는 `{:error, reason}`
- 만약 에러를 던지는 버전을 쓰고 싶다면 함수 이름 뒤에 `!`를 붙입니다.
- `File.read!("test.txt")` -> 파일이 없으면 에러(`raise`) 발생.

---

## 4. Throw와 Catch

`try/rescue`가 예외 상황을 위한 것이라면, `throw/catch`는 코드의 흐름을 갑자기 끊고 특정 값을 반환하며 빠져나올 때 사용합니다. (매우 드물게 사용됩니다.)

```elixir
try do
  Enum.each(-5..5, fn x ->
    if x == 0, do: throw(:found_zero)
  end)
catch
  :found_zero -> "0을 찾았습니다!"
end
```

---

## 노련한 작가의 요약표 👴

| 도구 | 용도 | 권장도 |
| :--- | :--- | :--- |
| **Pattern Matching** | 정상/비정상 흐름 분리 | **매우 권장 (최우선)** |
| **{:ok, val} 튜플** | 함수의 결과 처리 | **매우 권장 (관례)** |
| **try / rescue** | 통제 불가능한 외부 라이브러리 사용 시 | 낮음 (최소화) |
| **throw / catch** | 루프 중도 탈출 등 특수 상황 | 매우 낮음 |

---

## 노련한 선배의 한마디 👴

> "방어적인 프로그래밍(Defensive Programming)에 익숙한 분들은 에러가 나는 것을 두려워합니다. 하지만 Elixir에서는 **'예쁘게 죽는 법'**을 먼저 배우세요. 에러가 발생했을 때 시스템 전체가 멈추지 않고 해당 부분만 재시작되는 마법을 경험하게 되면, 여러분의 퇴근 시간이 훨씬 빨라질 것입니다."

---
**다음 장 안내**: [09. 모듈과 코드 조직화 (Modules and Code Organization)](09_modules_and_code_organization.md)
