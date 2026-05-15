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
- **03_basic_syntax.md**: 변수, 수치형, 아톰, 불리언, 문자열 등 기초 타입과 연산자.

### 2단계: 데이터 다루기
- **04_collections.md**: List, Tuple, Map, Keyword List의 차이점과 활용법.
- **05_pattern_matching.md**: Elixir의 핵심인 패턴 매칭(`=`)과 매칭의 힘.
- **06_control_flow.md**: `case`, `cond`, `if`, `unless`를 활용한 조건문 처리.

### 3단계: 구조화 및 함수형 프로그래밍
- **07_modules_and_functions.md**: 모듈 정의, 이름 있는 함수와 익명 함수, 파이프 연산자(`|>`).
- **08_file_and_io.md**: 파일 읽기/쓰기, `IO` 모듈을 통한 입출력, 경로 처리 및 스트림(Stream) 기초.
- **09_recursion_and_enumerable.md**: 반복문 대신 사용하는 재귀 함수와 `Enum` 모듈 활용.

### 4단계: 동시성과 OTP (Elixir의 진수)
- **10_processes.md**: 경량 프로세스(`spawn`, `send`, `receive`)와 프로세스 모델 이해.
- **11_otp_genserver.md**: 상태를 관리하고 서버-클라이언트 모델을 구현하는 `GenServer`.
- **12_otp_supervisor.md**: 자가 치유(Self-healing) 시스템을 위한 `Supervisor`와 감시 전략.
- **13_networking_and_distribution.md**: 노드(Node) 간 연결, 분산 메시징, TCP/UDP 기초 등 네트워크 활용법.

### 5단계: 도구 및 심화
- **14_mix_and_project.md**: 프로젝트 관리 도구인 `Mix`와 종속성 관리.
- **15_testing_exunit.md**: `ExUnit`을 활용한 테스트 코드 작성법.
- **16_metaprogramming_intro.md**: 매크로, `quote`, `unquote`를 활용한 메타프로그래밍 기초.

## 구현 일정 및 절차

1. **계획 승인**: 사용자의 목차 및 방향성 검토 및 승인.
2. **문서 순차 생성**: `01_introduction.md`부터 순차적으로 상세 내용을 작성하여 파일 생성.
3. **검토 및 보완**: 각 단계 완료 후 사용자의 피드백을 반영하여 내용 보강.

## 검증 계획

### 수동 검증
- 각 문서에 포함된 코드 예제가 최신 Elixir 버전에서 정상 동작하는지 확인.
- 초보자의 시각에서 용어 설명이 충분한지, 가독성이 좋은지 검토.
