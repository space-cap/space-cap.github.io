
# MiroFish x ASCII Brain — 시스템 아키텍처 및 구현 가이드

> **개요**: 뉴스 입력부터 지식 그래프(GraphRAG) 추출, 멀티에이전트 토론, 가상 소셜 시뮬레이션, ReACT 기반 판정 및 최종 예측 그래프 생성까지 이어지는 자동화 파이프라인의 전체 구조와 세부 구현 방식입니다.
> 

---

Git 주소:  https://github.com/666ghj/MiroFish

## 1. 전체 아키텍처 파이프라인

데이터 흐름은 다음 8단계 순서로 진행됩니다.

1. **뉴스 입력**
2. **Brain 13차원 데이터 로드** (사전 분석된 상태 데이터)
3. **GraphRAG**: 뉴스에서 엔티티 및 인과 관계 자동 추출
4. **에이전트 자동 생성**: 뉴스 도메인에 특화된 전문가 에이전트 동적 추가
5. **멀티라운드 토론**: 5인 고정 에이전트 + 자동 생성 에이전트 간 토론
6. **가상 Twitter 시뮬레이션**: 자율적 소셜 상호작용
7. **ReACT CIO 판정**: 7개 도구를 활용한 최종 검증 및 결론 도출
8. **3-Layer 예측 그래프** 및 **리포트 자동 생성**

---

## 2. 모듈별 핵심 기능

### 2.1. Brain 13차원 데이터 (Analysis Engine)

시장을 13개 차원으로 분해하여 다각도로 분석하는 핵심 엔진입니다. `brain_loader.py`를 통해 6종의 JSON 데이터를 로드하여 에이전트에게 컨텍스트를 제공합니다.

| 차원 (Dimension) | 개념 요약 | 직관적 비유 |
| --- | --- | --- |
| **sector_momentum** | 업종별 상승/하락 강도 | 반 평균 성적 |
| **macro_regime** | 거시 경제의 큰 흐름 | 계절의 변화 |
| **options_flow** | 옵션 시장의 방향성 | 보험 가입자 추이 |
| **earnings_catalyst** | 기업 실적 및 모멘텀 | 성적표 |
| **event_risk** | 돌발 이벤트 위험도 | 날씨(재난) 예보 |
| **ml_prediction** | 머신러닝 AI 모델 예측값 | 시험 예상 점수 |

Sheets로 내보내기

- **로드 대상 파일**: `daily_briefing.json`, `crisis_alert.json`, `active_inference_state.json`, `cross_market_graph.json`, `bayesian_posteriors.json`

### 2.2. 멀티에이전트 토론 시스템 (`agents.py`)

각 에이전트는 부여된 페르소나와 Brain 데이터에 기반하여 토론을 진행합니다.

**기본 5인 에이전트 구성**

| 이름 | 아이콘 | 역할 및 성향 (Bias) | 중점 분석 차원 (Focus) |
| --- | --- | --- | --- |
| **김리스크** | 🐻 | 글로벌 매크로 전략가 (Bearish) | `macro_regime`, `event_risk` |
| **박모멘텀** | 🐂 | 시장 모멘텀 트레이더 (Bullish) | `sector_momentum`, `earnings_catalyst` |
| **이퀀트** | 🤖 | 시스템 트레이딩 퀀트 (Quant) | `ml_prediction`, `options_flow` |
| **최역발상** | 🔄 | 역발상 전략가 (Contrarian) | `reversal_signal`, `crypto_sentiment` |
| **정헤지** | 🛡️ | 리스크 관리자 (Neutral) | `correlation_stability`, `event_risk` |

Sheets로 내보내기

> **동작 규칙**: 모든 발언에는 반드시 **Brain 데이터의 구체적 숫자 1개 이상이 인용**되어야 합니다. (예: "단순히 오를 것이다"가 아닌 "earnings_catalyst가 88이므로 상승 압력이 높다"로 발언)
> 

### 2.3. GraphRAG: 지식 그래프 자동 구축 (`graph_rag.py`)

비정형 뉴스 텍스트를 구조화된 엔티티 및 인과 관계로 변환합니다.

- **추출 파이프라인**: 뉴스 입력 → LLM (Structured JSON 출력) → 중복 체크 후 기존 지식 그래프(EKG) 병합
- **온톨로지 동적 확장**: 정의되지 않은 새로운 타입이 발견되면 스키마를 자동 업데이트합니다.
- **탐색 알고리즘**: BFS(너비 우선 탐색)를 통해 인과 체인을 역추적합니다. (예: `금리 인상` → `국채 금리 상승` → `환율 변동`)

JSON

`// 추출 JSON 스키마 예시
{
  "entities": [{"id": "fed_rate", "type": "policy", "name": "연준 금리"}],
  "relations": [
    {
      "source_id": "fed_rate", 
      "target_id": "us_bond_10y", 
      "relation_type": "causes", 
      "strength": 0.9,
      "evidence": "금리 인상 시 국채 금리 동반 상승"
    }
  ]
}`

### 2.4. 가상 소셜 플랫폼 (`social_platform.py`)

에이전트들이 자율적으로 소통하는 폐쇄형 마이크로블로깅(Twitter-like) 환경입니다. SQLite(`simulation.db`)를 기반으로 구동됩니다.

- **상호작용 로그**: `posts`, `comments`, `likes`, `follows`, `mutes`, `bookmarks` 등 상태 저장.
- **자율 행동 결정**: 에이전트가 타임라인과 Brain 컨텍스트를 분석하여 행동(Post, Like, Comment, Repost 등)을 **스스로 결정**하고 실행합니다.

### 2.5. ReACT CIO 판정 엔진 (`react_engine.py`)

최고투자책임자(CIO) 역할을 수행하는 논리 추론 엔진입니다. `Thought → Action → Observation` 루프를 최소 3회 이상 거쳐 최종 판정을 내립니다.

**지원되는 7가지 Action Tools**

1. `query_brain`: 차원별 스코어/신뢰도 조회
2. `search_graph`: EKG 인과 체인 BFS 탐색
3. `check_history`: 과거 예측 적중률 검증
4. `interview_agent`: 특정 에이전트에게 추가 논거 요구
5. `insight_forge`: 복합 질문 분할 및 통합 검색
6. `panorama_search`: 특정 시간 필터 및 깊은 BFS (Depth 4) 탐색
7. `final_answer`: 확신도 및 자산 배분 비율을 포함한 최종 결론 도출

### 2.6. 3-Layer 예측 그래프 및 리포트 (`agents.py`, `report_agent.py`)

토론 내용을 기반으로 LLM이 새로운 인과 관계를 추론하여 그래프 레이어를 형성합니다.

- **Layer 1 (Blue)**: 기존 EKG 사전 지식 네트워크
- **Layer 2 (Red)**: LLM이 이번 토론에서 새롭게 추론한 인과 체인
- **Layer 3 (Gold)**: 수렴된 최종 예측 결과 노드 (Bullish / Neutral / Bearish)

도출된 결과는 `report_agent.py`를 통해 3~5 챕터 구조의 마크다운 리포트로 자동 포맷팅되며, Brain 숫자 인용이 포함됩니다.

---

## 3. 부가 기능 및 실행 환경

### 3.1. 문서 업로드 파이프라인 (`doc_pipeline.py`)

- **지원 포맷**: PDF, MD, TXT, CSV (최대 50MB)
- **청킹 전략**: 슬라이딩 윈도우 (1,500자 기준, 200자 오버랩)
- 청크별 엔티티/관계 추출 후 EKG에 실시간 병합.

### 3.2. 디렉토리 구조

Plaintext

`lectures/mirofish/
├── app.py                  # Flask 메인 서버 (20개 이상의 API 엔드포인트)
├── agents.py               # 5인 에이전트 프롬프트 및 시뮬레이션 엔진
├── brain_loader.py         # 데이터 로더 (13차원)
├── memory.py               # 에이전트 장기 기억 저장소
├── graph_rag.py            # 지식 그래프 추출/병합/탐색
├── react_engine.py         # ReACT CIO 추론 루프 (7 Tool)
├── social_platform.py      # 가상 소셜 시뮬레이션 환경 (SQLite)
├── graph_updater.py        # 실시간 EKG 업데이트 모듈
├── agent_factory.py        # 동적 에이전트 생성기
├── simulation_manager.py   # 시뮬레이션 생명주기 제어
├── report_agent.py         # 최종 리서치 리포트 작성
├── batch_interview.py      # 배치 인터뷰 실행
├── doc_pipeline.py         # 대용량 문서 업로드 및 파싱
├── execution_log.py        # 실행 로그 기록 (JSONL)
├── data/                   # SQLite DB, JSON 상태 파일, 로그 폴더
├── templates/index.html    # 프론트엔드 UI 템플릿
└── static/                 # CSS 및 에이전트 아바타 에셋`

### 3.3. 시스템 실행 방법

Bash

`# 1. 의존성 패키지 설치
pip install flask openai python-dotenv

# 2. 환경 변수 설정 (.env 파일 생성)
echo "OPENAI_API_KEY=sk-..." > .env

# 3. 메인 서버 실행
python lectures/mirofish/app.py

# 4. 웹 브라우저 접속
http://localhost:5050`