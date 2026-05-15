# 16. Supervisor: 자가 치유 시스템의 설계자

Elixir가 "죽지 않는 시스템"으로 불리는 이유는 코드가 완벽해서가 아닙니다. 바로 **Supervisor(감시자)**라는 강력한 관리 체계가 있기 때문입니다. 

---

## 1. Supervisor의 철학: "Watchdog"

우리는 앞에서 **"Let it crash"** 철학을 배웠습니다. Supervisor는 이 철학을 실현하는 도구입니다. 자식 프로세스(GenServer 등)를 실행하고 감시하다가, 자식이 죽으면 설정된 전략에 따라 즉시 다시 살려냅니다.

---

## 2. Supervisor 정의하기

```elixir
defmodule MyApp.Supervisor do
  use Supervisor

  def start_link(init_arg) do
    Supervisor.start_link(__MODULE__, init_arg, name: __MODULE__)
  end

  @impl true
  def init(_init_arg) do
    children = [
      # 감시할 자식들 목록
      {Stack, ["Elixir", "Rocks"]}
    ]

    # 전략: :one_for_one (죽은 놈만 살린다)
    Supervisor.init(children, strategy: :one_for_one)
  end
end
```

---

## 3. 복구 전략 (Restart Strategies)

상황에 따라 자식을 어떻게 살릴지 결정할 수 있습니다.

1. **`:one_for_one`**: 죽은 프로세스 하나만 다시 시작합니다. (가장 많이 사용됨)
2. **`:one_for_all`**: 하나가 죽으면 모든 자식을 다 죽이고 다 같이 다시 시작합니다. (서로 의존성이 클 때)
3. **`:rest_for_one`**: 죽은 프로세스와 그 이후에 시작된 프로세스들만 다시 시작합니다.

---

## 4. 감시 트리 (Supervision Tree)

진정한 전문가는 Supervisor 밑에 또 다른 Supervisor를 두어 거대한 **계층형 트리**를 만듭니다.
- 말단 프로세스는 실제 일을 하고,
- 상위 Supervisor는 하위 시스템의 생사를 책임집니다.
이렇게 하면 시스템의 한 부분이 고장 나도 그 부분만 재시작될 뿐, 전체 시스템은 끄떡없습니다.

---

## 5. 자식 규약 (Child Spec)

어떤 모듈을 Supervisor 아래에 두려면, 그 모듈은 "어떻게 시작되고 어떻게 재시작되어야 하는지"에 대한 정보(`child_spec`)를 가지고 있어야 합니다. 보통 `use GenServer`가 이 작업을 자동으로 처리해 줍니다.

---

## 노련한 선배의 한마디 👴

> "초보자는 에러 로그가 안 올라오게 막으려 애쓰지만, 전문가는 **'에러가 났을 때 얼마나 빨리, 깨끗하게 복구되는가'**에 집중합니다. Supervisor는 여러분이 잠든 사이에도 시스템을 지켜주는 든든한 파수꾼입니다. 이제 여러분의 서버는 24시간 365일 멈추지 않을 준비가 되었습니다!"

---
**다음 장 안내**: [17. 네트워크와 분산 환경 (Networking and Distribution)](17_networking_and_distribution.md)
