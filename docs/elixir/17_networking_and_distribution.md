# 17. 네트워크와 분산 환경: 수평적 확장의 마법

Elixir(Erlang VM)의 가장 경이로운 특징은 **'분산(Distribution)'**이 언어 차원에서 내장되어 있다는 점입니다. 옆에 있는 서버에 메시지를 보내는 것이 내 컴퓨터 안에서 보내는 것만큼이나 쉽습니다.

---

## 1. 노드(Node): Elixir 시스템의 단위

실행 중인 Elixir 시스템 하나를 **노드(Node)**라고 부릅니다. 각 노드는 이름을 가질 수 있고, 서로 연결될 수 있습니다.

```bash
# 터미널 1 (이름을 'a'로 지정)
iex --sname a

# 터미널 2 (이름을 'b'로 지정)
iex --sname b
```

이제 노드 'b'에서 'a'를 연결해 봅시다:
```elixir
iex(b@hostname)> Node.connect(:a@hostname)
true
iex(b@hostname)> Node.list()
[:a@hostname]
```

---

## 2. 원격 메시지 전달

노드가 연결되면, 상대방 노드의 PID만 알면 어디서든 메시지를 보낼 수 있습니다.

```elixir
# 노드 'a'에서 현재 프로세스 등록
Process.register(self(), :receiver)

# 노드 'b'에서 노드 'a'의 등록된 프로세스에게 메시지 전송
send({:receiver, :a@hostname}, "안녕, 다른 세계의 친구!")
```

---

## 3. Registry: 프로세스 위치 추적하기

분산 환경에서 수많은 프로세스가 떴다 죽었다 할 때, 특정 이름으로 프로세스를 찾아야 할 일이 생깁니다. 이때 **Registry** 모듈을 사용합니다.

```elixir
# 레지스트리 시작
{:ok, _} = Registry.start_link(keys: :unique, name: MyApp.Registry)

# 프로세스 등록
{:ok, _} = Registry.register(MyApp.Registry, "user_123", nil)

# 프로세스 찾기
[{pid, _}] = Registry.lookup(MyApp.Registry, "user_123")
```

---

## 4. 분산 시스템 설계 시 고려할 점

30년 베테랑의 충고:
1. **네트워크 단절(Netsplit)**: 서버 간 연결이 끊겼을 때 시스템이 어떻게 행동할지 설계해야 합니다.
2. **메시지 크기**: 너무 큰 데이터를 네트워크로 주고받으면 성능이 저하됩니다. 필요한 정보만 보내세요.
3. **보안**: 분산 노드 간에는 'Cookie'라는 비밀번호를 공유해야 합니다. 외부 노드가 함부로 연결하지 못하게 보호하세요.

---

## 노련한 선배의 한마디 👴

> "다른 언어에서 분산 서버를 구축하려면 RabbitMQ나 Redis 같은 별도의 도구가 필요하지만, Elixir는 그저 **'Node'**와 **'Send'**만으로 충분합니다. 이 단순함이 바로 Elixir가 수억 명의 동시 접속자를 처리하는 대규모 서비스(Discord, WhatsApp)에서 사랑받는 이유입니다."

---
**다음 장 안내**: [18. Mix와 품질 관리 (Mix and Quality)](18_mix_project_and_typespecs.md)
