# Elixir 초보자 가이드 제작 계획서

Elixir를 처음 접하는 사용자가 기초부터 실무 수준의 동시성 프로그래밍까지 체계적으로 배울 수 있는 문서 세트를 제작합니다. 각 문서는 독립적인 주제를 다루며, 실습 위주로 구성됩니다.

## 사용자 검토 필요 사항

> [!IMPORTANT]
> 제안된 목차 중 특정 분야(예: 웹 프레임워크인 Phoenix, 데이터베이스 라이브러리인 Ecto)를 포함할지, 아니면 Elixir 언어 자체에 집중할지에 대한 의견을 부탁드립니다. 현재 계획은 Elixir 언어의 핵심 기능과 OTP 기초에 집중하고 있습니다.

## 제안된 문서 구조 (목차)

각 문서는 `docs/elixir/` 폴더 내에 번호 순서대로 생성될 예정입니다.

### 1단계: 입문 및 환경 구축
- **01_introduction.md**: Elixir의 탄생 배경(Jose Valim, Erlang/BEAM), 철학, 특징(불변성, 동시성, 확장성).
- **02_installation.md**: Windows, macOS, Linux 환경별 설치 방법 및 `iex` 사용법.
- **03_basic_types.md**: 변수, 수치형, 아톰, 불리언, 날짜와 시간(`Date`, `DateTime`) 등 기초 타입.

### 2단계: 데이터와 제어의 정석
- **04_strings_and_binaries.md**: 문자열 조작(`String` 모듈), 시질(Sigils, `~r`, `~w`), 바이너리 처리.
- **05_collections.md**: List, Tuple, Map, Keyword List의 차이점과 활용법.
- **06_pattern_matching.md**: Elixir의 핵심인 패턴 매칭(`=`)과 함수 시그니처 매칭.
- **07_control_flow.md**: `case`, `cond`, `if`, `unless`를 활용한 조건문 처리.
- **08_error_handling.md**: `raise`, `try/rescue/after`, 그리고 "Let it crash" 철학의 이해.

### 3단계: 구조화 및 함수형 프로그래밍
- **09_modules_and_code_organization.md**: 모듈 정의, 함수, 파이프 연산자, 코드 조직화(`alias`, `import`, `use`).
- **10_structs.md**: 데이터 구조화 및 사용자 정의 타입(`defstruct`).
- **11_protocols_and_behaviours.md**: 다형성(Polymorphism) 구현과 모듈 규약 설정.
- **12_file_and_io.md**: 파일 읽기/쓰기, `IO` 모듈, 경로 처리 및 스트림(Stream)을 이용한 지연 연산.
- **13_recursion_and_enumerable.md**: 재귀 함수의 원리, `Enum` 모듈 활용 및 컴프리헨션(Comprehensions).

### 4단계: 동시성과 OTP (Elixir의 진수)
- **14_processes_task_agent.md**: 프로세스 기초(`spawn`, `send`, `receive`)와 편리한 추상화(`Task`, `Agent`).
- **15_otp_genserver.md**: 상태를 관리하고 서버-클라이언트 모델을 구현하는 `GenServer`.
- **16_otp_supervisor.md**: 자가 치유 시스템을 위한 `Supervisor`와 프로세스 트리 구성.
- **17_networking_and_distribution.md**: 노드(Node) 연결, 분산 메시징, `Registry`를 통한 프로세스 관리.

### 5단계: 도구, 품질 및 배포
- **18_mix_project_and_typespecs.md**: `Mix` 프로젝트 관리, 종속성, Typespecs(`@spec`)와 문서화(`ExDoc`).
- **19_debugging.md**: `IO.inspect`, `dbg`, `IEx.pry` 및 GUI 도구인 `Observer` 활용.
- **20_testing_exunit.md**: `ExUnit`을 활용한 단위 테스트 및 속성 기반 테스트 기초.
- **21_deployment_and_releases.md**: `mix release`를 활용한 운영 환경 배포 전략.
- **22_metaprogramming_intro.md**: 매크로, `quote`, `unquote`를 활용한 코드 생성 기술.

### 6단계: 실전 프로젝트
- **23_mini_project_chatbot.md**: AI 챗봇을 만들며 함수형 사고방식 적용 (수신, 처리, 응답 로직 설계).
- **24_mini_project_dashboard.md**: 분산 시스템 모니터링 대시보드 만들기 (프로세스, GenServer, PubSub 활용).

## 구현 일정 및 절차

1. **계획 승인**: 사용자의 목차 및 방향성 검토 및 승인.
2. **문서 순차 생성**: `01_introduction.md`부터 순차적으로 상세 내용을 작성하여 파일 생성.
3. **검토 및 보완**: 각 단계 완료 후 사용자의 피드백을 반영하여 내용 보강.

## 검증 계획

### 수동 검증
- 각 문서에 포함된 코드 예제가 최신 Elixir 버전에서 정상 동작하는지 확인.
- 초보자의 시각에서 용어 설명이 충분한지, 가독성이 좋은지 검토.
