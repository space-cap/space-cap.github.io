

# TradingAgents KR: 바이브 코딩으로 만드는 AI 멀티에이전트 주식 분석

> **💡 30초 데모 안내** 브라우저에서 `demo.html` 파일을 열어 '삼성전자', '비츠로셀', 'SK하이닉스'를 입력하면 UI 목업을 즉시 확인할 수 있습니다. (※ 실제 데이터 연동 및 LLM 호출이 포함된 버전은 인프런 강의 및 디스코드 커뮤니티를 통해 다룹니다.)
> 

---

[demo.html](attachment:5d6b99ae-6477-4570-95b4-43320869e835:demo.html)

## 📌 시스템 개요

**종목 코드 1개 입력으로 28~80초 내에 BUY/SELL/HOLD 판단과 다각적 분석 근거를 도출하는 AI 멀티에이전트 시스템입니다.**

### 🆚 왜 멀티에이전트인가?

| 구분 | 단일 LLM ("삼성전자 어때?") | 멀티에이전트 (TradingAgents KR) |
| --- | --- | --- |
| **분석 관점** | "균형 잡힌" 뻔한 NEUTRAL 답변 | 6~10명의 AI 전문가가 각기 다른 관점으로 심층 분석 |
| **정확도** | 업종 환각 발생 (예: 비츠로셀을 AI로 착각) | 실제 업종 데이터 기반, 에이전트별 명확한 역할 강제 |
| **의사결정** | 단일 시점의 얕은 스냅샷 | Bull vs Bear 토론 후 트레이더 에이전트의 최종 판단 도출 |
| **분석 범위** | 개별 종목 데이터에 국한 | 매크로 내러티브 분석 및 26년 과거 위기 유사성 매칭 포함 |

Sheets로 내보내기

---

## 🏗️ 아키텍처 및 파이프라인

### 1️⃣ 기술 스택

- **Backend (Python):** Flask + SSE (Server-Sent Events) 스트리밍
- **LLM & Search:** Google Gemini 3.1 Pro (Grounding Search), OpenAI GPT-5-mini, Perplexity Sonar
- **Data API:** yfinance, KRX OpenAPI, DART API, 네이버 금융 크롤링
- **Frontend (TypeScript):** Next.js 16 (App Router), React 19, Tailwind v4
- **Visualization:** D3.js 7 (Force graph), framer-motion

### 2️⃣ 5-Phase 파이프라인

| 단계 | 역할 | 소요 시간 |
| --- | --- | --- |
| **Phase 1. 과거 학습** | 가격, 기술지표, 수급, 공시, 재무, 매크로 내러티브 수집 | ~8초 |
| **Phase 2. 인과 연결** | Event Knowledge Graph BFS + 위기 유사 매칭 | ~3초 |
| **Phase 3. 전문가 배치** | EKG 클러스터 기반 테마 전문가 동적 생성 | ~1초 |
| **Phase 4. 미래 예측** | 병렬 분석 + Bull/Bear 토론 + 미래 노드 | ~20초 |
| **Phase 5. 최종 결론** | 가중 집계 + FinCast 분포 + 트레이더 판단 | ~8초 |

Sheets로 내보내기

---

## 📊 실제 분석 예시 (삼성전자 005930)

Plaintext

`▶ 매크로 내러티브 (자동 수집):
   총평: BULLISH 7/10
   주도 테마: AI 반도체 랠리 +85, 우주항공 +80, 중동 완화 +75

▶ 6명 에이전트 분석 요약:
   🔬 김칩 (반도체): BULLISH 78% — HBM3E 수주 + 2nm 양산
   💹 이에이 (AI 인프라): BULLISH 72% — AI capex 폭증
   🌏 박관세 (지정학): NEUTRAL 58% — 미중 기술 패권 리스크
   ⚡ 최환율 (매크로): BULLISH 65% — 환율 안정 + 외국인 유입
   🟢 Bull: BULLISH 81% — 10일 +69만주 기관 매집
   🔴 Bear: NEUTRAL 55% — PER 18.4 상단 + RSI 72 과열

▶ FinCast 분포: UP 68%, p50 +0.8%, σ 2.1%

▶ 최종 결론: 🟢 BUY 73% (반도체 슈퍼사이클 + 수급 모멘텀 + 레짐 risk_on)
▶ 리스크 요인: 미중 기술 패권, DRAM 단기 차익실현`

---

## 🎨 주요 UI/UX 기능

- **Knowledge Graph:** 5층 force-directed 그래프 (stock/past/agent/future/conclusion), 호버 연결 하이라이트
- **시네마틱 배경:** 28초 주기 breathing mesh gradient, 노이즈 필터, 커서 스포트라이트
- **데이터 애니메이션:** 카운트업, 텍스트 스크램블, 3D letter-rise, 1.5초 Confidence ring 드로우인

---

## 🎓 수강 정보 및 실전 구현

단순한 프롬프트 엔지니어링을 넘어, 바이브 코딩(Vibe Coding)을 통해 실제 작동하는 풀스택 시스템을 구축합니다.

### 🛠️ 핵심 학습 내용

- **LLM Fallback Chain 설계:** 장애 격리를 위한 Circuit Breaker 및 Gemini → DeepSeek → GPT-5-mini 체인
- **역할 강제 프롬프팅:** 일반 LLM의 NEUTRAL 편향 회피 기법
- **데이터 파이프라인 구축:** 네이버 금융 10일 수급 크롤링(Regex 패턴), DART corpCode.xml 매핑 캐시 전략
- **고급 분석 로직:** Event Knowledge Graph BFS 확장, 매크로 내러티브 통합, 적중률 기반 에이전트 가중치 EMA 진화
- **풀스택 연동:** Flask SSE 제너레이터 패턴과 Next.js EventSource 연동

### 📦 제공 자료

- 복사/붙여넣기로 즉시 활용 가능한 22개 Claude Code 프롬프트
- 전체 소스 코드 및 단계별 VOD 영상 (~3시간)
- 실시간 버그 해결 스트림 녹화본
- 수강생 전용 Discord 커뮤니티 액세스 (기술 지원 및 자동화 블루프린트 공유)