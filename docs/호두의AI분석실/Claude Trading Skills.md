
# Claude Trading Skills — tradermonty 완전 가이드

> **출처:** [github.com/tradermonty/claude-trading-skills](https://github.com/tradermonty/claude-trading-skills) **정리일:** 2026-02-22
> 

---

저를 사칭하는 사람이 있습니다. 조심해주세요

저는 절대 개인 트레이드 사이트를 공유하지 않습니다.

![image.png](attachment:97dc69d1-5080-4d63-bcd4-50b514d40482:image.png)

## 1. 🎯 프로젝트 개요

**claude-trading-skills**는 Claude AI를 전문 트레이딩/투자 어시스턴트로 변환하는 **33개 스킬 패키지** 모음입니다. 각 스킬은 독립된 `SKILL.md`와 레퍼런스 파일로 구성되며, Claude 웹앱이나 Claude Code에 로드하여 해당 분야의 전문가 역할을 수행하도록 설계되었습니다.

### 💡 핵심 철학

- **정량적 스코어링:** 감이 아닌 숫자로 판단 (대부분의 스킬이 0~100점 산출)
- **스킬 체이닝:** 단독 실행이 아닌 여러 스킬을 워크플로우로 조합
- **자동 자기 개선:** 매일 스킬 품질을 스스로 점검하고 개선하는 루프 내장
- **검증된 방법론 인코딩:** William O'Neil, Minervini, Druckenmiller 등 대가들의 방식 적용

### 📁 디렉토리 구조

Plaintext

`claude-trading-skills/
├── skills/           # 33개 스킬 폴더 (SKILL.md + references/)
├── scripts/          # 자동화 스크립트
├── agents/           # Dual-Agent 설정 (scenario-analyst, strategy-reviewer)
├── zip-packages/     # Claude 웹앱용 ZIP
├── commands/         # 커맨드 정의
├── launchd/          # macOS 데몬 (매일 05:00 자동 실행)
└── reports/          # 일일 개선 리포트`

---

## 2. 🗂️ 33개 스킬 상세 가이드

### 📈 2-1. Market Analysis & Research (11개)

**#1. Sector Analyst**

- **요약:** 섹터 퍼포먼스 차트 이미지를 분석하여 현재 마켓 사이클 위치 판단.
- **입/출력:** 1주/1개월 차트 이미지 ➔ Executive Summary, 확률 시나리오, 포지셔닝.
- **특징:** 상대 수익률 중심 분석, Kindleberger/Faber 4단계 마켓 사이클 적용. (API 불필요)

**#2. Breadth Chart Analyst**

- **요약:** S&P 500 시장 폭(Breadth) 차트 이미지를 분석해 전략/전술적 포지셔닝 제시.
- **임계값:** 전략 (73% 이상 과열, 23% 이하 과매도) / 전술 (40% 이상 과매수, 10% 이하 과매도)
- **특징:** 최근 3~5개 데이터 포인트 집중, 'Failed Reversal' 감지. (API 불필요)

**#3. Technical Analyst**

- **요약:** 주봉 차트 이미지를 순수 기술적으로 분석하여 2~4개 확률 시나리오 생성.
- **분석 항목:** 추세, 지지/저항, 이동평균(20/50/200), 거래량, 패턴.
- **특징:** 뉴스/펀더멘털 배제. 목표가 및 무효화(Invalidation) 레벨 수치화 필수. (API 불필요)

**#4. Market News Analyst**

- **요약:** 최근 10일간 시장 뉴스를 수집하여 '3차원 임팩트 스코어'로 랭킹.
- **스코어링:** (가격 영향 × 범위 배수) × 미래 전망 보정 = 임팩트 5 이상만 상세 분석.
- **특징:** 노이즈 배제, 수치적 근거 필수. WebSearch 사용.

**#5. US Stock Analysis**

- **요약:** 개별 종목 종합 리서치 (펀더멘털 + 테크니컬 + 피어 비교).
- **출력:** 재무 테이블, 기술적 분석, 상대 강도 메모.
- **특징:** 출처 날짜 표기 필수, Bull/Bear 양면 균형 분석. WebSearch 사용.

**#6. Market Environment Analysis**

- **요약:** 글로벌 시장 브리핑 (지수, FX, 원자재, 채권, 변동성 종합).
- **특징:** 사용자 투자 스타일에 맞춘 전략 커스터마이징 제공. WebSearch 사용.

**#7. Market Breadth Analyzer**

- **요약:** 시장 건강도를 6요소 기반 0~100점으로 정량화 (무료 CSV 사용).
- **스코어 비중:** 레벨&트렌드(25%), MA크로스오버(20%), 사이클(20%), 약세플래그(15%), 히스토리백분위(10%), 다이버전스(10%).

**#8. Uptrend Analyzer**

- **요약:** ~2,800개 미국 주식의 상승추세 비율로 시장 건강도 0~100점 산출.
- **스코어 비중:** 시장폭(30%), 섹터참여(25%), 모멘텀(20%), 로테이션(15%), 과거맥락(10%).
- **경고 시스템:** Late-cycle, 과도한 섹터 스프레드 등에 패널티 부여.

**#9. Macro Regime Detector**

- **요약:** 1~2년 호라이즌의 구조적 매크로 레짐 전환을 6요소 크로스에셋 분석으로 감지.
- **5대 레짐:** Concentration, Broadening, Contraction, Inflationary, Transitional.
- **API:** FMP API (ETF 9개 + 국채금리 조회).

**#10. Institutional Flow Tracker**

- **요약:** SEC 13F 공시 분석으로 '스마트 머니'의 축적/분배 패턴 탐지.
- **특징:** 기관 품질 티어(슈퍼투자자 3x, 인덱스 0x)에 따라 가중치 부여. FMP API 사용.

**#11. Theme Detector**

- **요약:** FINVIZ 기반 트렌딩 테마 자동 탐지 및 3차원 스코어 랭킹.
- **분석:** Theme Heat, Lifecycle Maturity, Confidence 평가. Python 스크립트 실행.

### 📅 2-2. Economic & Earnings Calendars (2개)

- **#12. Economic Calendar Fetcher:** FMP API로 7~90일 주요 경제 이벤트(FOMC, CPI 등) 조회. 임팩트(High/Med/Low) 분류.
- **#13. Earnings Calendar:** 7일간 미국 주식 실적 발표 일정. 시가총액($2B 이상) 및 시간(BMO/AMC) 기준 정렬. FMP API 사용.

### 🛡️ 2-3. Strategy & Risk Management (6개)

**#14. Scenario Analyzer (Dual-Agent)**

- **작동:** 뉴스 헤드라인 입력 ➔ 18개월 투자 시나리오(Base/Bull/Bear) 생성.
- **Dual-Agent:** `scenario-analyst`(초안) ➔ `strategy-reviewer`(확률 검증, 편향 감지).

**#15. Backtest Expert**

- **요약:** "가장 많이 벌리는" 전략보다 "가장 덜 깨지는" 전략 검증 프레임워크.
- **원칙:** 스트레스 테스트에 전체 시간 80% 할당, 파라미터 고원(plateau) 탐색, Walk-Forward 검증.

**#16. Stanley Druckenmiller Investment Advisor**

- **요약:** 8개 업스트림 스킬 종합 ➔ 확신 점수(0~100) 기반 자산 배분 추천.
- **확신 존:** 80-100(Maximum, Fat pitch), 0-19(자본 보전).

**#17. US Market Bubble Detector v2.1**

- **요약:** Minsky/Kindleberger 프레임워크로 버블 위험도를 정량(12점) + 정성(3점) 채점.
- **지표:** Put/Call, VIX, 마진부채, IPO 과열, 가격 가속도 등. (WebSearch 기반)

**#18. Options Strategy Advisor**

- **지원:** Black-Scholes 가격 결정, 17개 이상 옵션 전략, 그릭스 산출, ASCII P/L 다이어그램. FMP API.

**#19. Portfolio Manager**

- **지원:** Alpaca MCP 연동. 실시간 포트폴리오 분석, 리스크 메트릭, 리밸런싱 및 절세 추천.

### ⏱️ 2-4. Market Timing & Bottom Detection (2개)

- **#20. Market Top Detector:** O'Neil 분배일 + Minervini 선도주 악화 분석으로 천장 확률(0~100) 산출. 공격적 방어 타이밍. (FMP API)
- **#21. FTD Detector:** Follow-Through Day 기반 바닥 확인 시그널 탐지. 듀얼 인덱스 상태머신 적용. (FMP API)

### 🚀 2-5. Earnings Momentum (2개)

- **#22. Earnings Trade Analyzer:** 실적 발표 후 갭 사이즈, 추세, 거래량 등 5팩터로 모멘텀 기회 탐지(A~D 등급).
- **#23. PEAD Screener:** Post-Earnings Announcement Drift 패턴을 주봉 캔들로 탐지. (상태: MONITORING ➔ SIGNAL_READY ➔ BREAKOUT).

### 🔍 2-6. Stock Screening & Selection (8개)

- **#24. VCP Screener:** S&P 500 대상 Minervini VCP + Stage 2 스크리닝. 8개 튜닝 파라미터 적용.
- **#25. CANSLIM Screener:** O'Neil의 7요소(0~100점). **(규칙: M요소=0 이면 절대 매수 금지)**
- **#26. Value Dividend Screener:** 가치 + 인컴 + 성장 3-Phase 고품질 배당주 선별.
- **#27. Dividend Growth Pullback:** 배당 성장률 12%+ 종목이 RSI≤40 풀백 시 진입 기회 포착.
- **#28~30. Kanchi Dividend Series (SOP/Review/Tax):** 일본 Kanchi의 5-step 안전제일 배당 워크플로우. (절대 자동매도 금지 규칙 적용).
- **#31. Pair Trade Screener:** 공적분(Cointegration) 기반 통계적 차익거래. Z-Score 기반 진입/청산.

### ⚖️ 2-7. Quality Assurance & Orchestration (2개)

- **#32. Dual-Axis Skill Reviewer:** 스킬 코드를 결정론적 자동 검사(50%) + LLM 정성 리뷰(50%)로 채점. 90점 미만 시 개선 루프 트리거.
- **#33. Weekly Trade Strategy:** 5개 Agent가 9개 업스트림 스킬을 체이닝하여 주간 트레이딩 블로그 자동 생성.

---

## 3. 🧠 핵심 투자 방법론

| 방법론 | 적용 원칙 | 연관 스킬 |
| --- | --- | --- |
| **William O'Neil** | CANSLIM 팩터, Follow-Through Day(바닥 확인), 분배일 누적(천장 감지). 시장 방향(M) 역행 금지. | CANSLIM Screener, FTD Detector, Market Top Detector |
| **Mark Minervini** | Stage 2 상승추세 진입 종목만 매수. VCP(변동성 수축 패턴) 돌파 타점 공략. | VCP Screener, Market Top Detector |
| **S. Druckenmiller** | 매크로 레짐 우선. 고확신 시나리오에 대규모 베팅(Fat pitch), 틀리면 즉각 손절. | Druckenmiller Advisor |
| **Minsky / Kindleberger** | 5단계 버블 사이클(Normal ➔ Caution ➔ Elevated ➔ Euphoria ➔ Critical) 기반 리스크 예산 조절. | US Market Bubble Detector |
| **Kanchi (배당)** | 과도한 고수익률(8%+) 경계, 수익/성장/밸류에이션/일회성 필터 검증 후 약세 분할 매수. | Kanchi 3종 파이프라인 |

Sheets로 내보내기

---

## 4. 🔄 Self-Improvement Loop (자동 개선 루프)

매일 05:00에 macOS `launchd`가 자동 실행하여 스킬 품질을 유지/보수하는 파이프라인입니다.

1. **Round-Robin 선택:** 매일 1개의 스킬을 타겟으로 지정.
2. **Dual-Axis 채점:** 메타데이터, 워크플로우, 안전규칙, 산출물, 테스트 커버리지 기반 0~100점 평가.
3. **개선 트리거:** 점수가 90점 미만일 경우 `claude -p` 실행하여 코드 및 프롬프트 수정.
4. **Quality Gate:** 개선 후 재채점하여 점수가 상승했을 때만 PR 생성 (하락 시 자동 롤백).
5. **리포트:** 수정 델타와 함께 사람 리뷰 대기열로 전달.

---

## 5. 🤖 Dual-Agent & 추천 워크플로우

### Dual-Agent 아키텍처 (Scenario Analyzer)

Plaintext

`[scenario-analyst] (초안 작성) ───▶ [strategy-reviewer] (6차원 검증)
- Base/Bull/Bear 시나리오           - 누락 탐지, 편향 감지
- 18개월 타임라인                     - 대안 시나리오 제시
- 수혜/피해 섹터 분류                 - 확률 및 로직 검증`

### 추천 워크플로우 체인

- **일일 시장 모니터링:** Economic Cal ➔ Earnings Cal ➔ Market News ➔ Breadth Analyst
- **매크로 포지셔닝:** Regime Detector ➔ Market Top/FTD ➔ Bubble Detector ➔ Scenario Analyzer ➔ Druckenmiller Advisor
- **종목 리서치:** US Stock Analysis ➔ Earnings Cal ➔ Market News ➔ Backtest Expert

---

## 6. 🔌 API 의존성 총정리

| API 소스 | 비용/한도 | 주요 사용 스킬 |
| --- | --- | --- |
| **무료 데이터 / 자체 분석** | $0 | 기술/섹터/차트 분석, Kanchi 배당 3종, Backtest, Dual-Axis Reviewer |
| **WebSearch (Claude)** | 내장 | Market News, US Stock, Bubble Detector, Scenario Analyzer |
| **FMP API** | 무료 (250회/일) | 달력(경제/실적), Market Top, FTD, Macro Regime, VCP, CANSLIM, 옵션/페어 트레이딩 |
| **FINVIZ** | 무료 스크래핑 | Theme Detector, CANSLIM(대체 데이터) |
| **Alpaca MCP** | 증권 계좌 연동 | Portfolio Manager |

---

# 🔑 FMP API 무료 키 발급 가이드

> **Financial Modeling Prep (FMP)** — 미국 주식 데이터 무료 API 실적, 재무제표, 주가, 경제 캘린더 등 150개+ 엔드포인트 제공
> 

---

## 📌 한눈에 보기

| 항목 | 내용 |
| --- | --- |
| **비용** | **무료 ($0)** |
| **일일 호출** | 250회 / 일 |
| **데이터 범위** | 약 5년 히스토리 |
| **엔드포인트** | 150개 이상 |
| **대역폭** | 30일 기준 500MB |
| **소요 시간** | **약 2분** |

Sheets로 내보내기

---

## 🛠 Step 1. 회원가입

1. 아래 링크에 접속합니다.
    - [FMP 회원가입 페이지 바로가기](https://site.financialmodelingprep.com/register)
2. 다음 항목을 입력합니다.
    - **Email**: 본인 이메일 (Gmail 권장)
    - **Password**: 사용할 비밀번호 설정
3. **[Sign Up]** 버튼을 클릭합니다.

---

## 📧 Step 2. 이메일 인증

1. 가입한 이메일의 받은편지함을 확인합니다.
2. FMP에서 발송한 **인증 메일**을 엽니다.
3. 메일 내 **[Verify Email]** 링크를 클릭합니다.
    
    > 💡 **Tip:** 메일이 오지 않는다면 스팸 메일함을 확인해 보세요!
    > 

---

## 🆔 Step 3. API Key 확인

1. 인증 완료 후 로그인을 진행합니다.
    - [FMP 로그인 페이지](https://site.financialmodelingprep.com/login)
2. 로그인 즉시 **Dashboard** 화면으로 이동됩니다.
3. Dashboard 중앙의 **API Key** 항목에서 자동 생성된 키를 확인합니다. `yKgehOoUpx9vDMX09ue...` (형태의 긴 문자열)
4. 해당 키를 **복사(Copy)**하여 안전한 곳에 저장합니다.

---

## 🧪 Step 4. API Key 테스트

브라우저 주소창에 아래 URL을 입력하여 정상 작동 여부를 확인합니다. *(YOUR_KEY 부분을 본인의 API 키로 교체하세요)*

Bash

`https://financialmodelingprep.com/api/v3/quote/AAPL?apikey=YOUR_KEY`

> **성공 시 출력 예시 (JSON):**
> 

JSON

`[{
  "symbol": "AAPL",
  "name": "Apple Inc.",
  "price": 245.12,
  "change": 2.34,
  ...
}]`

---

## 💻 Step 5. Claude Trading Skills 연결

### **방법 A: 터미널 환경변수 설정 (Mac OS)**

터미널에서 아래 명령어를 입력하여 환경변수에 등록합니다.

Bash

`# ~/.zshrc 파일 편집
echo 'export FMP_API_KEY="본인의_API_키"' >> ~/.zshrc

# 설정 적용
source ~/.zshrc`

### **방법 B: 스크립트 실행 시 직접 전달**

파이썬 스크립트 실행 시 인자로 키를 넘겨줍니다.

Bash

`python3 scripts/get_economic_calendar.py --api-key YOUR_API_KEY`

---

## 📊 플랜별 비교 (무료 vs 유료)

| 구분 | Free | Starter ($30/월) | Professional ($80/월) |
| --- | --- | --- | --- |
| **일일 호출 수** | 250회 | 750회 | 2,000회 |
| **대역폭 (30일)** | 500MB | 20GB | 50GB |
| **데이터 히스토리** | ~5년 | 전체 데이터 | 전체 데이터 |
| **실시간 데이터** | 지원 안 함 | 지원 안 함 | **지원함** |

Sheets로 내보내기

> 💡 **알림:** VCP, FTD, Earnings 등 대부분의 분석 스킬은 **무료 플랜으로도 충분히 운영 가능**합니다.
> 

---

## ⚠️ 주의사항

- **보안 유지:** API Key는 본인만 사용해야 하며 절대 외부에 노출하지 마세요.
- **용도 제한:** 무료 플랜은 개인용/학습용이며 상업적 사용은 제한됩니다.
- **한도 초과:** 하루 호출 한도(250회) 초과 시 `429 Error`가 발생하며, 다음 날 자정에 리셋됩니다.
- **업데이트:** 2025년 8월 이후 일부 엔드포인트가 v4로 마이그레이션 중이니 문서를 수시로 확인하세요.

---

## 🔗 관련 링크

- [FMP API 문서 (Docs)](https://site.financialmodelingprep.com/developer/docs)
- [FMP 가격 정책 안내](https://site.financialmodelingprep.com/pricing-plans)
- [Quickstart 가이드](https://site.financialmodelingprep.com/developer/docs/quickstart)

---

**작성일:** 2026-02-26

**작성자:** 호두의 AI 연구소 (Hodu's AI Analysis Lab)