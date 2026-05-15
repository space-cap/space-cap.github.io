# 💧 Elixir Master Guide: 초보자부터 전문가까지

> **"BEAM의 지혜를 빌려, 죽지 않는 시스템을 설계하다."**

이 프로젝트는 **Elixir** 프로그래밍 언어를 기초부터 실전 프로젝트 배포까지 체계적으로 학습할 수 있도록 구성된 종합 가이드북입니다. 30년 BEAM 생태계 베테랑의 통찰력을 담아, 단순한 문법 습득을 넘어선 **함수형 사고방식**과 **결함 허용 시스템** 설계 능력을 배양합니다.

---

## 🗺️ 학습 로드맵 (Table of Contents)

### 🌿 1단계: 입문 및 환경 구축
*   [01. 탄생과 철학](01_introduction.md) - 왜 Elixir인가? (Jose Valim과 Erlang의 만남)
*   [02. 설치 및 환경 구성](02_installation.md) - 전장에 나갈 무기 준비하기
*   [03. 기초 타입](03_basic_types.md) - Elixir의 말씨 익히기 (수치, 아톰, 날짜/시간)

### 💎 2단계: 데이터와 제어의 정석
*   [04. 문자열과 시질](04_strings_and_binaries.md) - 텍스트 그 이상의 본질 (바이너리와 시질)
*   [05. 컬렉션](05_collections.md) - 데이터를 담는 네 가지 그릇 (List, Tuple, Map, Keyword)
*   [06. 패턴 매칭](06_pattern_matching.md) - Elixir의 마법 지팡이
*   [07. 제어 구문](07_control_flow.md) - 논리의 흐름 다스리기 (case, cond, with)
*   [08. 예외 처리와 철학](08_error_handling.md) - 죽어야 사는 시스템 (Let it crash!)

### 🏗️ 3단계: 구조화 및 함수형 프로그래밍
*   [09. 모듈과 코드 조직화](09_modules_and_code_organization.md) - 파이프 연산자(`|>`)와 코드 관리
*   [10. 구조체](10_structs.md) - 타입이 있는 데이터 그릇 (`defstruct`)
*   [11. 프로토콜과 행동](11_protocols_and_behaviours.md) - 상속 없는 다형성
*   [12. 파일 및 IO](12_file_and_io.md) - 데이터와 세상 연결하기 (Stream 지연 연산)
*   [13. 재귀와 Enum](13_recursion_and_enumerable.md) - 반복을 예술로 승화하기

### ⚡ 4단계: 동시성과 OTP (Elixir의 진수)
*   [14. 프로세스 기초](14_processes_task_agent.md) - Elixir의 살아있는 세포 (Task/Agent)
*   [15. OTP GenServer](15_otp_genserver.md) - 엘릭서 서버의 표준 모델
*   [16. OTP Supervisor](16_otp_supervisor.md) - 자가 치유 시스템의 설계자
*   [17. 네트워크와 분산 환경](17_networking_and_distribution.md) - 수평적 확장의 마법

### 🛠️ 5단계: 도구, 품질 및 배포
*   [18. Mix와 품질 관리](18_mix_project_and_typespecs.md) - Typespecs와 ExDoc 문서화
*   [19. 디버깅 기술](19_debugging.md) - IO.inspect, dbg, 그리고 Observer
*   [20. 테스트](20_testing_exunit.md) - ExUnit과 잠들기 전의 안식
*   [21. 배포 전략](21_deployment_and_releases.md) - `mix release`와 운영 환경
*   [22. 메타프로그래밍 맛보기](22_metaprogramming_intro.md) - 코드를 짜는 코드 (Macros)

### 🚀 6단계: 실전 프로젝트
*   [23. 프로젝트: AI 챗봇](23_mini_project_chatbot.md) - 패턴 매칭과 재귀를 이용한 대화 엔진
*   [24. 프로젝트: 분산 대시보드](24_mini_project_dashboard.md) - 실시간 노드 모니터링 시스템

---

## 🎯 이 가이드의 특징
- **전문가의 시선**: 30년 BEAM 전문가의 '선배 한마디'를 통해 실무 노하우를 전수합니다.
- **실전 위주**: 단순히 이론을 배우는 데 그치지 않고, 직접 코드를 타이핑하며 결과를 확인하는 실습 중심입니다.
- **최신 버전 대응**: Elixir 1.14 이상의 최신 기능(`dbg` 등)을 포함하고 있습니다.

---

## 👴 노련한 선배의 한마디
> "Elixir를 배우는 것은 단순히 새로운 언어를 배우는 것이 아닙니다. 여러분의 시스템이 어떻게 동시에 수백만 개의 일을 처리하고, 장애에도 끄떡없이 살아남을 수 있는지 그 비결을 배우는 과정입니다. 이 여정이 끝날 때쯤, 여러분은 세상을 바라보는 눈이 달라져 있을 것입니다."

---
*본 가이드는 [space-cap.github.io](https://space-cap.github.io) 프로젝트의 일환으로 제작되었습니다.*
