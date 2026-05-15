# 24. 실전 프로젝트 2: 분산 대시보드 시스템 기초

마지막 프로젝트는 Elixir의 진정한 힘인 **'프로세스'**와 **'분산 환경'**을 활용합니다. 여러 서버(Node)의 상태를 수집하여 하나의 대시보드에서 보여주는 시스템의 뼈대를 만듭니다.

---

## 1. 프로젝트 목표
1. **Collector**: 각 서버에서 주기적으로 CPU/메모리 정보를 수집하는 프로세스.
2. **Dashboard**: 수집된 데이터를 통합하여 보관하고 보여주는 프로세스.
3. **PubSub**: 수집된 데이터를 실시간으로 대시보드에 전달하는 방식.

---

## 2. 데이터 수집 프로세스 (Collector)

`GenServer`를 사용하여 5초마다 데이터를 수집하고 대시보드에 보고합니다.

```elixir
defmodule Monitor.Collector do
  use GenServer

  def start_link(node_name) do
    GenServer.start_link(__MODULE__, node_name)
  end

  @impl true
  def init(node_name) do
    schedule_work() # 주기적 작업 예약
    {:ok, node_name}
  end

  @impl true
  def handle_info(:collect, node_name) do
    # 실제로는 시스템 정보를 가져오지만 여기서는 가짜 데이터를 만듭니다.
    stats = %{cpu: :rand.uniform(100), memory: :rand.uniform(1024)}
    
    # 대시보드 노드에게 데이터 전송 (분산 전송)
    send({Monitor.Dashboard, :dashboard_node@hostname}, {:stats, node_name, stats})
    
    schedule_work()
    {:noreply, node_name}
  end

  defp schedule_work() do
    Process.send_after(self(), :collect, 5000)
  end
end
```

---

## 3. 대시보드 관리 (Dashboard)

수집된 모든 노드의 최신 상태를 보관합니다.

```elixir
defmodule Monitor.Dashboard do
  use GenServer

  def start_link(_) do
    GenServer.start_link(__MODULE__, %{}, name: __MODULE__)
  end

  @impl true
  def init(state), do: {:ok, state}

  @impl true
  def handle_info({:stats, node_name, stats}, state) do
    new_state = Map.put(state, node_name, stats)
    IO.puts("--- 실시간 대시보드 업데이트 ---")
    IO.inspect(new_state)
    {:noreply, new_state}
  end
end
```

---

## 4. 마지막 관문: 결함 허용 트리 구축

이 모든 프로세스가 죽지 않도록 **Supervisor** 아래에 둡니다.

```elixir
defmodule Monitor.Application do
  use Application

  def start(_type, _args) do
    children = [
      Monitor.Dashboard,
      # 여러 노드의 컬렉터를 동시에 띄울 수 있습니다.
      {Monitor.Collector, "Server_A"}
    ]

    opts = [strategy: :one_for_one, name: Monitor.Supervisor]
    Supervisor.start_link(children, opts)
  end
end
```

---

## 노련한 선배의 맺음말 👴

> "축하합니다! 여러분은 이제 Elixir의 탄생 철학부터 분산 시스템의 뼈대까지 모두 섭렵하셨습니다. Elixir의 세계는 여기서 끝이 아닙니다. 웹 개발을 위한 **Phoenix**, 데이터베이스를 위한 **Ecto**, 그리고 임베디드 장치를 위한 **Nerves**까지 무궁무진한 가능성이 여러분을 기다리고 있습니다. 30년 BEAM 여행의 동료가 되신 것을 진심으로 환영합니다!"

---
**가이드 완료**: 이제 여러분만의 Elixir 여정을 시작해 보세요! 🚀✨
