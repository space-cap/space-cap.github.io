# 02. 설치 및 환경 구성: 전장에 나갈 무기 준비하기

가이드를 집필하는 노련한 작가로서, 저는 환경 설정에서 지쳐 포기하는 초보자를 너무나 많이 봐왔습니다. 그래서 이번 장에서는 가장 간단하고 확실하게 Elixir를 여러분의 컴퓨터에 모시는 방법을 알려드리겠습니다.

---

## 1. 운영체제별 설치 방법

Elixir는 Erlang 가상 머신(BEAM) 위에서 동작하므로, Elixir를 설치하면 Erlang도 함께 설치됩니다.

### Windows (가장 쉬운 방법)
1. [Elixir 공식 웹사이트의 설치 페이지](https://elixir-lang.org/install.html#windows)로 이동합니다.
2. **"Precompiled setup"** (exe 파일)을 다운로드하여 실행합니다.
3. 설치 마법사를 따라 'Next'를 누르면 끝입니다! (자동으로 환경 변수까지 설정해 줍니다.)

### macOS (Homebrew 사용)
터미널을 열고 다음 명령어를 입력하세요:
```bash
brew install elixir
```

### Linux (Ubuntu/Debian 기준)
```bash
wget https://packages.erlang-solutions.com/erlang-solutions_2.0_all.deb && sudo dpkg -i erlang-solutions_2.0_all.deb
sudo apt-get update
sudo apt-get install esl-erlang
sudo apt-get install elixir
```

---

## 2. 설치 확인하기: 살아있는지 물어보기

설치가 완료되었다면, 터미널(또는 명령 프롬프트)을 열고 다음 명령어를 입력해 보세요:

```bash
elixir -v
```

다음과 유사한 메시지가 출력된다면 성공입니다!
```text
Erlang/OTP 26 [erts-14.x.x] ...
Elixir 1.16.x (compiled with Erlang/OTP 26)
```

---

## 3. IEx: Elixir와 대화하는 창구

Elixir의 가장 큰 매력 중 하나는 **IEx (Interactive Elixir)**입니다. 코드를 파일로 저장하지 않고도 즉석에서 실행해 볼 수 있는 놀이터죠. 터미널에 `iex`를 입력해 보세요.

```bash
iex
```

그러면 `iex(1)>` 이라는 프롬프트가 뜹니다. 여기서 간단한 계산을 해볼까요?
```elixir
iex(1)> 1 + 2
3
iex(2)> "Hello" <> " Elixir!"
"Hello Elixir!"
```

> [!TIP]
> IEx를 종료하려면 `Ctrl + C`를 두 번 누르거나, `Ctrl + G`를 누른 뒤 `q`를 입력하세요.

---

## 4. 추천 개발 환경: VS Code + ElixirLS

코드를 더 즐겁게 짜기 위해 에디터 설정을 추천합니다.
1. **Visual Studio Code**를 설치합니다.
2. 확장(Extensions) 메뉴에서 **"ElixirLS"**를 검색하여 설치하세요.
   - 문법 강조(Syntax Highlighting), 자동 완성, 실시간 오류 체크 기능을 제공합니다.

---

## 5. Mix: Elixir의 만능 도구 상자

나중에 자세히 배우겠지만, Elixir 설치와 함께 `mix`라는 도구도 생겼을 것입니다. 프로젝트를 만들고, 실행하고, 테스트하는 모든 일을 담당하는 비서 같은 존재입니다. 설치가 잘 되었는지 확인해 보세요:

```bash
mix -v
```

---

## 노련한 작가의 팁 💡

> "설치 과정에서 문제가 생기면 당황하지 마세요. 대부분은 환경 변수(PATH) 문제입니다. 설치 후 터미널을 껐다 켜는 것만으로 해결되는 경우가 많습니다. 이제 무기를 모두 챙겼으니, 다음 장에서 Elixir의 '말씨'인 기본 타입들을 배워보겠습니다."

---
**다음 장 안내**: [03. 기초 타입 (Basic Types)](03_basic_types.md)
