
# Smart Money Tracker v2.0 — Vibe Coding Blueprint

> 💡 **가이드 안내** 이 문서는 AI 코딩 도구(Claude Code, Cursor, Copilot 등)에 입력하여 **Smart Money Tracker** 웹앱을 처음부터 자동으로 생성하기 위한 마스터 블루프린트입니다.
> 

## 1. 프로젝트 개요

**Smart Money Tracker**는 SEC EDGAR 13F 보고서를 Gemini AI로 자동 분석하는 웹 애플리케이션입니다. 워런 버핏, 조지 소로스, 레이 달리오 등 전설적인 투자자 15명의 실제 포트폴리오를 추적하고 비교 분석합니다.

- **핵심 기능**
    - SEC EDGAR에서 13F XML 자동 수집 및 파일 캐싱
    - Gemini AI Structured Output으로 포트폴리오 분석 (JSON 포맷)
    - Grounding Search를 통한 최신 뉴스 교차 검증
    - SSE(Server-Sent Events) 기반 실시간 분석 진행 상태 스트리밍
    - 다중 투자자 비교 분석 (레이더 차트, 히트맵 지원)
    - 워치리스트 및 분석 히스토리 자동 저장
    - AI 어시스턴트 채팅 지원 및 다양한 포맷(JSON/CSV/HTML) 내보내기 기능
    - 글래스모피즘 기반 다크/라이트 모드 반응형 UI
    - Gemini Image API를 활용한 투자자 아바타 자동 생성
- **기술 스택**
    - **Backend**: Python 3.9+ / Flask
    - **AI**: Google Gemini API (`google-generativeai` 또는 `google-genai`)
    - **Data**: SEC EDGAR (무료, 별도 인증 불필요)
    - **Frontend**: Vanilla JS + Chart.js + CSS (Glassmorphism)
    - **Streaming & Config**: SSE, `python-dotenv`

---

## 2. 🔑 필수 API 발급 및 설정 가이드

프로젝트를 실행하기 위해 가장 먼저 환경을 세팅해야 합니다.

### Google Gemini API 키 발급 방법

1. **Google AI Studio 접속**: [https://aistudio.google.com/](https://aistudio.google.com/?authuser=1) 로 이동하여 Google 계정으로 로그인합니다.
2. **API 키 생성**: 좌측 메뉴에서 **'Get API key'**를 클릭한 후, **'Create API key'** 버튼을 누릅니다.
3. **키 복사**: 생성된 `AIzaSy...` 로 시작하는 문자열을 복사하여 프로젝트의 `.env` 파일에 붙여넣습니다. (무료 티어 사용 가능)

### SEC EDGAR API 접근 권한 (무료)

- SEC EDGAR 데이터는 **별도의 API 키가 필요 없습니다.**
- 단, 무단 크롤링을 방지하기 위해 요청 헤더(Header)에 신원을 밝히는 **User-Agent(앱 이름 + 이메일)**를 반드시 포함해야 합니다. (예: `SmartMoneyTracker/2.0 (your-email@example.com)`)
- **초당 요청 제한(Rate Limit)**: 1초에 10회 이상 요청 시 IP가 차단되므로 로직 내에 반드시 `time.sleep(0.15)` 이상의 딜레이를 포함해야 합니다.

---

## 3. 🏗️ 시스템 아키텍처 (System Architecture)

*(아래 다이어그램은 노션에 붙여넣으시면 자동으로 예쁜 그래프로 변환됩니다.)*

코드 스니펫

`graph TD
    A[Frontend UI <br> Vanilla JS, Chart.js] <-->|REST API & SSE Stream| B(Flask Backend <br> app.py)
    
    subgraph Data Sources
        C{SEC EDGAR API}
        D[Google Gemini API]
    end
    
    B -->|Fetch 13F XML <br> with User-Agent| C
    C -->|Raw XML Data| B
    
    B <-->|Prompt + Schema <br> Structured Output| D
    
    subgraph Local Storage
        E[(File Cache <br> cache/)]
        F[(JSON/HTML/CSV <br> output/history/)]
    end
    
    B <--> E
    B --> F`

---

## 4. 📁 디렉토리 구조 (Directory Structure)

반드시 아래 제시된 구조대로 프로젝트를 생성하세요.

Plaintext

`smart-money-tracker/
├── .env.example          # 환경변수 템플릿
├── .gitignore
├── requirements.txt      # flask, requests, google-generativeai, python-dotenv
├── config.py             # 중앙 설정 + 투자자 15명 데이터
├── app.py                # Flask 웹앱 메인 (SSE + 모든 API 라우트)
├── run_demo.py           # CLI 데모 스크립트
├── generate_avatars.py   # Gemini Image API로 아바타 생성 스크립트
├── models/
│   ├── __init__.py
│   └── schemas.py        # Gemini Structured Output JSON 스키마
├── services/
│   ├── __init__.py
│   ├── gemini_client.py  # 자체 Gemini API 래퍼 (외부 의존성 최소화)
│   ├── sec_edgar.py      # SEC EDGAR API + 파일 캐싱 로직
│   └── report.py         # HTML/CSV 리포트 생성기
├── static/
│   ├── css/style.css     # 글래스모피즘 + 밝은 톤 UI 스타일링
│   ├── js/app.js         # 전체 프론트엔드 로직
│   └── img/              # Gemini 생성 아바타 저장소
├── templates/
│   └── index.html        # 메인 대시보드 (Jinja2)
└── output/               
    └── history/          # 분석 히스토리 자동 저장`

---

## 5. ⚙️ 환경 변수 및 설정 데이터

### 1) `.env.example`

코드 스니펫

`# Google Gemini API
GOOGLE_API_KEY=your_google_api_key_here
GEMINI_MODEL=gemini-2.5-flash

# SEC EDGAR User-Agent (이메일 필수 포함)
SEC_USER_AGENT=SmartMoneyTracker/2.0 (your-email@example.com)

# Flask Server Config
FLASK_SECRET_KEY=change-this-to-random-string
FLASK_DEBUG=false
FLASK_PORT=5050

# Cache Setting
CACHE_ENABLED=true
CACHE_TTL_HOURS=6

# Logging
LOG_LEVEL=INFO`

### 2) `config.py` (투자자 15명 리스트)

Python

`INVESTORS = {
    'berkshire':    {'cik': '0001067983', 'name': 'Berkshire Hathaway (Warren Buffett)', 'style': 'Value', 'initials': 'WB', 'color': '#4361ee', 'photo': 'img/berkshire.png'},
    'citadel':      {'cik': '0001350694', 'name': 'Citadel Advisors (Ken Griffin)', 'style': 'Multi-Strategy', 'initials': 'KG', 'color': '#7c3aed', 'photo': 'img/citadel.png'},
    'renaissance':  {'cik': '0001423053', 'name': 'Renaissance Technologies (Jim Simons)', 'style': 'Quant', 'initials': 'JS', 'color': '#0ea5e9', 'photo': 'img/renaissance.png'},
    'bridgewater':  {'cik': '0001037389', 'name': 'Bridgewater Associates (Ray Dalio)', 'style': 'Macro', 'initials': 'RD', 'color': '#10b981', 'photo': 'img/bridgewater.png'},
    'soros':        {'cik': '0000895421', 'name': 'Soros Fund Management (George Soros)', 'style': 'Macro', 'initials': 'GS', 'color': '#f59e0b', 'photo': 'img/soros.png'},
    'point72':      {'cik': '0001649339', 'name': 'Point72 (Steve Cohen)', 'style': 'Multi-Strategy', 'initials': 'SC', 'color': '#ec4899', 'photo': 'img/point72.png'},
    'millennium':   {'cik': '0001336528', 'name': 'Millennium Management', 'style': 'Multi-Strategy', 'initials': 'ML', 'color': '#8b5cf6', 'photo': 'img/millennium.png'},
    'twosigma':     {'cik': '0001364742', 'name': 'Two Sigma Investments', 'style': 'Quant', 'initials': '2S', 'color': '#06b6d4', 'photo': 'img/twosigma.png'},
    'tiger':        {'cik': '0001061165', 'name': 'Tiger Global Management', 'style': 'Tech/Growth', 'initials': 'TG', 'color': '#f97316', 'photo': 'img/tiger.png'},
    'blackrock':    {'cik': '0001697748', 'name': 'BlackRock Inc.', 'style': 'Index/Passive', 'initials': 'BR', 'color': '#1e293b', 'photo': 'img/blackrock.png'},
    'vanguard':     {'cik': '0001040280', 'name': 'Vanguard Group', 'style': 'Index/Passive', 'initials': 'VG', 'color': '#991b1b', 'photo': 'img/vanguard.png'},
    'fidelity':     {'cik': '0001166559', 'name': 'Fidelity Management', 'style': 'Active', 'initials': 'FM', 'color': '#16a34a', 'photo': 'img/fidelity.png'},
    'statestreet':  {'cik': '0001095620', 'name': 'State Street Corporation', 'style': 'Index/Passive', 'initials': 'SS', 'color': '#0369a1', 'photo': 'img/statestreet.png'},
    'elliott':      {'cik': '0001167483', 'name': 'Elliott Investment Management', 'style': 'Activist', 'initials': 'EM', 'color': '#dc2626', 'photo': 'img/elliott.png'},
    'appaloosa':    {'cik': '0001273087', 'name': 'Appaloosa Management (David Tepper)', 'style': 'Distressed', 'initials': 'DT', 'color': '#a855f7', 'photo': 'img/appaloosa.png'},
}`

---

## 6. 💻 핵심 서비스 상세 스펙

### 1) 백엔드 파이프라인 (`app.py` & `services/`)

- **Gemini 래퍼 (`gemini_client.py`)**: `response_mime_type="application/json"`을 활용하여 정확한 형태의 데이터(Structured Output)를 추출하고, Grounding Search 도구를 활용해 최신 뉴스로 분석 내용을 교차 검증합니다.
- **SEC 크롤러 (`sec_edgar.py`)**: `data.sec.gov`가 불안정할 수 있으므로, 최우선적으로 `www.sec.gov`의 HTML 디렉토리를 직접 파싱하여 13F XML을 추출하도록 강제합니다.
- **API 설계 (`app.py`)**: 비동기 처리를 통해 무거운 분석 작업을 백그라운드로 넘기고, 프론트엔드와는 `SSE(Server-Sent Events)`를 통해 통신하여 0.8초 간격으로 진행 상황 퍼센테이지와 상태 메시지를 스트리밍합니다.

### 2) 프론트엔드 로직 (`static/js/app.js` & `style.css`)

- **디자인 시스템**: 밝은 톤의 메시 그라데이션 바탕과 글래스모피즘 카드를 조합하여 세련된 분위기를 연출합니다. 다크 모드는 `data-theme="dark"` 속성으로 제어합니다.
- **인터랙션**: 투자자 카드를 토글 방식으로 복수 선택하며, 1명이면 단일 포트폴리오 분석을, 2명 이상이면 레이더 차트와 겹침 히트맵(Overlap Heatmap)이 포함된 비교 분석 대시보드를 렌더링합니다.

---

## 7. 🚀 실행 방법 및 Vibe Coding 프롬프트

### 개발 환경 실행 방법

터미널에서 다음 명령어들을 순서대로 입력하여 프로젝트를 실행합니다.

Bash

`# 1. 패키지 설치
pip install -r requirements.txt

# 2. 환경 변수 파일 생성 후 .env에 키 입력
cp .env.example .env

# 3. 아바타 자동 생성 스크립트 실행 (선택 사항)
python generate_avatars.py

# 4. 웹서버 구동 (http://localhost:5050 접속)
python app.py`

### 🤖 AI 코딩 툴 전용 프롬프트 (그대로 복사하여 사용하세요)

> **"이 VIBE_CODING_BLUEPRINT 문서를 전체적으로 읽어줘.
> 
> 
> 이 문서를 바탕으로 처음부터 'Smart Money Tracker v2.0' 프로젝트를 생성할 거야. 제시된 디렉토리 구조에 맞춰서 폴더와 파일을 하나씩 빠짐없이 만들어줘. 특히 백엔드의 SEC EDGAR 파싱 로직, SSE 스트리밍 라우트, Gemini Structured Output 연동 부분을 명세에 적힌 제약사항을 지켜서 완벽하게 구현해 줘. 프론트엔드는 설명된 글래스모피즘 UI와 Chart.js 연동을 반영해서 작성해.
> 
> 첫 번째 단계로 .env.example 파일과 requirements.txt 파일을 생성하고, 기본 뼈대가 되는 config.py 파일을 작성하는 것부터 시작해 줘."**
>