# 15. GenServer: 엘릭서 서버의 표준 모델

우리는 지난 장에서 `Agent`를 통해 상태를 관리하는 법을 배웠습니다. 하지만 실무에서 더 복잡한 로직(비동기 처리, 타임아웃, 시스템 연동 등)을 다루려면 **GenServer (Generic Server)**를 반드시 알아야 합니다.

---

## 1. GenServer란?

GenServer는 **"상태를 가진 서버"**를 만들기 위한 표준 템플릿입니다. OTP(Open Telecom Platform)의 핵심 부품으로, 우리가 겪을 수 있는 대부분의 동시성 문제를 이미 해결해 둔 검증된 구조입니다.

---

## 2. GenServer의 구조

GenServer를 구현할 때는 크게 두 부분으로 나눕니다.
1. **Client API**: 외부에서 호출하는 함수들 (공개 인터페이스).
2. **Server Callbacks**: 서버 내부에서 실제로직을 처리하는 함수들.

### 예시: 간단한 스택(Stack) 서버
```elixir
defmodule Stack do
  use GenServer

  # --- Client API ---

  def start_link(initial_stack) do
    GenServer.start_link(__MODULE__, initial_stack, name: __MODULE__)
  end

  def push(element) do
    GenServer.cast(__MODULE__, {:push, element})
  end

  def pop() do
    GenServer.call(__MODULE__, :pop)
  end

  # --- Server Callbacks ---

  @impl true
  def init(stack) do
    {:ok, stack}
  end

  @impl true
  def handle_call(:pop, _from, [head | tail]) do
    {:reply, head, tail}
  end

  @impl true
  def handle_cast({:push, element}, state) do
    {:noreply, [element | state]}
  end
end
```

---

## 3. Call vs Cast: 언제 무엇을 쓰는가?

- **`call` (동기)**: 서버로부터 **응답을 기다립니다.** 결과가 필요할 때 사용합니다.
- **`cast` (비동기)**: 서버에게 메시지만 던지고 **기다리지 않습니다.** "알아서 해줘"라고 말할 때 사용합니다.

---

## 4. 왜 직접 프로세스를 만들지 않고 GenServer를 쓰는가?

30년 베테랑의 시선으로 본 GenServer의 장점:
1. **표준화**: 모든 Elixir 개발자가 같은 구조를 쓰므로 협업이 쉽습니다.
2. **강력한 기능**: 타임아웃 처리, 시스템 메시지 대응, 로깅 등이 내장되어 있습니다.
3. **Supervisor 연동**: 감시 시스템과 완벽하게 호환됩니다.

---

## 노련한 선배의 한마디 👴

> "처음에는 `handle_call`과 `handle_cast`가 복잡해 보일 수 있습니다. 하지만 기억하세요. 서버는 오직 **한 번에 하나의 메시지만 처리**합니다. 덕분에 우리는 '락(Lock)'이나 '뮤텍스(Mutex)' 같은 복잡한 동기화 기법 없이도 안전하게 상태를 관리할 수 있습니다. 이것이 Elixir 서버가 강력한 이유입니다."

---
**다음 장 안내**: [16. OTP Supervisor (Supervisor)](16_otp_supervisor.md)
