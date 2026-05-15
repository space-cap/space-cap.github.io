# 14. 프로세스 기초: Elixir의 살아있는 세포

Elixir 시스템은 수만, 수백만 개의 **프로세스(Processes)**들로 구성됩니다. 이들은 OS의 무거운 프로세스가 아니라, Erlang VM 위에서 돌아가는 아주 가벼운 '경량 프로세스'입니다.

---

## 1. 프로세스란 무엇인가?

Elixir의 모든 코드는 프로세스 안에서 실행됩니다.
- 프로세스는 서로 **완전히 격리**되어 있습니다. (메모리 공유 안 함)
- 프로세스 간의 유일한 대화 수단은 **메시지 전달**입니다.
- 하나가 죽어도 다른 프로세스에는 영향을 주지 않습니다.

---

## 2. 기본 도구: spawn, send, receive

### spawn: 프로세스 만들기
```elixir
pid = spawn(fn -> IO.puts("새로운 프로세스에서 실행 중!") end)
# pid는 Process ID의 약자입니다.
```

### send와 receive: 메시지 주고받기
```elixir
parent = self() # 현재 프로세스의 ID

spawn(fn ->
  send(parent, {:hello, "저는 자식 프로세스예요"})
end)

receive do
  {:hello, msg} -> IO.puts("메시지 도착: #{msg}")
after
  1000 -> IO.puts("기다리다 지쳤어요")
end
```

---

## 3. 편리한 추상화: Task와 Agent

`spawn`과 `receive`를 직접 쓰는 것은 조금 번거로울 수 있습니다. Elixir는 이를 더 쉽게 쓰도록 두 가지 도구를 제공합니다.

### Task: 비동기 작업 실행하기
단순히 병렬로 일을 시키고 결과를 받을 때 최적입니다.
```elixir
task = Task.async(fn -> 
  # 무거운 작업 수행
  :timer.sleep(2000)
  "작업 완료!"
end)

# 다른 작업 수행...

result = Task.await(task)
IO.puts(result)
```

### Agent: 상태 관리하기
여러 프로세스 사이에서 공유해야 할 '데이터(상태)'를 보관할 때 씁니다.
```elixir
# 초기값 0으로 에이전트 시작
{:ok, agent} = Agent.start_link(fn -> 0 end)

# 상태 업데이트
Agent.update(agent, fn state -> state + 1 end)

# 상태 가져오기
Agent.get(agent, fn state -> state end) # 결과: 1
```

---

## 4. 왜 프로세스를 사용하는가?

30년 베테랑의 시선으로 볼 때, Elixir 프로세스는 **'진정한 캡슐화'**를 실현합니다.
1. **동시성**: CPU 코어를 모두 활용해 여러 일을 동시에 합니다.
2. **가용성**: 에러가 난 부분만 격리해서 죽일 수 있습니다.
3. **분산**: 메시지 전달 방식이므로, 다른 서버에 있는 프로세스에게도 똑같이 메시지를 보낼 수 있습니다.

---

## 노련한 선배의 한마디 👴

> "객체지향 프로그래밍(OOP)에서 '객체'가 해야 할 일을 Elixir에서는 '프로세스'가 담당합니다. 하지만 프로세스는 객체보다 훨씬 강력하죠. 메모리를 공유하지 않기 때문에 '데드락(Deadlock)' 걱정 없이 수백만 개의 작업을 동시에 돌릴 수 있습니다. 이제 이 프로세스들을 체계적으로 관리하는 **GenServer**를 만나러 가볼까요?"

---
**다음 장 안내**: [15. OTP GenServer (GenServer)](15_otp_genserver.md)
