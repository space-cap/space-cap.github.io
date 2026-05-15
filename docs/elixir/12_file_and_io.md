# 12. 파일 및 IO: 데이터와 세상 연결하기

컴퓨터 내부의 계산을 넘어 파일을 읽고 쓰고 터미널과 대화하는 법을 배웁니다. Elixir의 IO 시스템은 모두 '메시지 전달' 기반으로 되어 있어 매우 강력합니다.

---

## 1. IO 모듈: 터미널 입출력

가장 기본적인 입출력 도구입니다.

```elixir
# 출력
IO.puts("안녕하세요!")

# 입력 받기
name = IO.gets("이름이 무엇인가요? ") |> String.trim()
IO.puts("반가워요, #{name}님!")

# 디버깅용 출력 (데이터 구조를 그대로 보여줌)
IO.inspect([1, 2, 3], label: "리스트 확인")
```

---

## 2. File 모듈: 파일 다루기

파일을 읽고 쓰는 작업은 `File` 모듈을 사용합니다. Elixir의 관습대로 '성공/실패 튜플'을 반환하는 버전과 '에러를 던지는(!)' 버전이 있습니다.

```elixir
# 파일 쓰기
File.write("hello.txt", "Elixir는 즐거워!")

# 파일 읽기 (권장되는 방식)
case File.read("hello.txt") do
  {:ok, body} -> IO.puts(body)
  {:error, reason} -> IO.puts("실패: #{reason}")
end

# 파일 읽기 (에러를 던지는 방식)
body = File.read!("hello.txt")
```

---

## 3. Path 모듈: 경로 처리

운영체제마다 다른 경로 구분자(`/` 또는 `\`)를 신경 쓰지 않게 도와줍니다.

```elixir
iex> Path.join("users", "jose")
"users/jose"

iex> Path.expand("~/docs")
"/Users/jose/docs"
```

---

## 4. 스트림 (Streams): 지연 연산의 위력 (전문가 코너 🧠)

만약 10GB짜리 로그 파일을 읽어야 한다면 어떻게 할까요? `File.read`로 한 번에 메모리에 올리면 서버가 터질 것입니다. 이때 **스트림**을 사용합니다.

```elixir
# 파일을 한 줄씩 읽어서 처리 (메모리를 거의 쓰지 않음)
File.stream!("large_log.txt")
|> Stream.map(&String.upcase/1)
|> Stream.filter(&String.contains?(&1, "ERROR"))
|> Enum.to_list() # 여기서 비로소 실행됨
```

스트림은 **"데이터를 어떻게 처리할지 계획"**만 세워두고, 실제로 결과가 필요할 때까지 실행을 미룹니다.

---

## 노련한 작가의 비교 👴

| 도구 | 특징 | 비유 |
| :--- | :--- | :--- |
| **Enum** | 즉시 연산 (Eager) | 식당에서 요리가 다 나올 때까지 기다렸다가 한 번에 먹음 |
| **Stream** | 지연 연산 (Lazy) | 요리가 나오는 대로 한 점씩 천천히 음미하며 먹음 (대용량 처리에 필수) |

---

## 노련한 선배의 한마디 👴

> "IO 작업은 CPU 계산보다 훨씬 느립니다. 그래서 Elixir는 모든 IO 작업을 별도의 프로세스에서 처리하도록 설계했습니다. 여러분이 `IO.puts`를 호출할 때, 내부적으로는 'IO 서버'라는 프로세스에 메시지를 보내는 과정이 일어납니다. 이 놀라운 구조 덕분에 수많은 입출력이 동시에 일어나도 Elixir 시스템은 멈추지 않습니다."

---
**다음 장 안내**: [13. 재귀와 Enum (Recursion and Enumerable)](13_recursion_and_enumerable.md)
