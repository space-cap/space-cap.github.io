# 04. 문자열과 시질: 텍스트 그 이상의 본질

초보자들은 문자열을 그저 '글자 묶음'으로 보지만, 전문가는 그 아래의 **'바이너리(Binary)'**를 봅니다. Elixir의 문자열은 왜 그렇게 강력하고 효율적인지 파헤쳐 보겠습니다.

---

## 1. 문자열 (Strings)

Elixir에서 문자열은 항상 **큰따옴표(`"`)**로 감쌉니다. 내부적으로는 UTF-8로 인코딩된 바이너리입니다.

```elixir
iex> "안녕, Elixir!"
"안녕, Elixir!"
```

### 문자열 보간법 (Interpolation)
`#{}`을 사용해 문자열 안에 변수나 코드를 넣을 수 있습니다.
```elixir
iex> name = "호세"
iex> "반가워요, #{name}님!"
"반가워요, 호세님!"
```

### 문자열 합치기 (Concatenation)
`<>` 연산자를 사용합니다.
```elixir
iex> "Elixir" <> "는 " <> "즐거워"
"Elixir는 즐거워"
```

---

## 2. 문자 리스트 (Charlists)

**홑따옴표(`'`)**로 감싼 것은 문자열이 아니라 **정수들의 리스트**입니다. 주로 오래된 Erlang 라이브러리와 통신할 때 사용합니다. 초보자들은 흔히 실수하니 주의하세요!

```elixir
iex> 'hello' == "hello"
false
iex> is_list('hello')
true
```

---

## 3. 시질 (Sigils): 개발자의 생산성 치트키

시질은 문법적 설탕(Syntactic Sugar)으로, 텍스트를 특정 타입으로 쉽게 변환해 줍니다. `~` 기호로 시작합니다.

- **`~r` (정규표현식)**:
  ```elixir
  iex> "elixir" =~ ~r/eli/
  true
  ```
- **`~w` (단어 리스트)**: 공백을 기준으로 단어를 쪼개 리스트로 만듭니다.
  ```elixir
  iex> ~w(apple banana orange)
  ["apple", "banana", "orange"]
  ```
- **`~s` (문자열)**: 따옴표를 포함한 문자열을 쓸 때 유용합니다.
  ```elixir
  iex> ~s(He said "Hello")
  "He said \"Hello\""
  ```

---

## 4. 바이너리의 본질 (전문가 코너 🧠)

Elixir 문자열은 사실 **바이너리**입니다. 즉, 메모리의 연속된 바이트 덩어리죠. `<< >>` 문법을 사용해 직접 바이너리를 다룰 수 있습니다.

```elixir
iex> <<104, 101, 108, 108, 111>>
"hello"
```
이러한 구조 덕분에 Elixir는 대용량 파일이나 네트워크 패킷을 처리할 때 다른 언어보다 월등히 빠르고 효율적입니다.

---

## 5. String 모듈: 텍스트 조작의 백과사전

거의 모든 문자열 작업은 `String` 모듈에 있습니다.
```elixir
iex> String.length("안녕하세요")
5
iex> String.upcase("elixir")
"ELIXIR"
iex> String.split("one,two,three", ",")
["one", "two", "three"]
```

---

## 노련한 작가의 팁 💡

> "Elixir에서 문자열은 불변(Immutable)입니다. 한 번 생성된 문자열은 절대 변하지 않죠. 문자열을 수정하면 새로운 문자열이 만들어집니다. 이게 느릴 것 같다고요? BEAM의 효율적인 메모리 관리 덕분에 오히려 동시성 환경에서는 데이터 복사 비용이 적어 훨씬 유리합니다. 이제 데이터를 담는 그릇인 '컬렉션'으로 넘어가 볼까요?"

---
**다음 장 안내**: [05. 컬렉션 (Collections)](05_collections.md)
