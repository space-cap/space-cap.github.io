# 🚀 space-cap.github.io

> **프론트엔드 UI/UX 포트폴리오 & 목업 모음집**  
> 다양한 산업 도메인의 실제 서비스형 웹 페이지 및 ERP 목업을 담은 정적 사이트입니다.

---

## 📋 목차

- [프로젝트 소개](#-프로젝트-소개)
- [포함된 프로젝트](#-포함된-프로젝트)
- [기술 스택](#-기술-스택)
- [디렉토리 구조](#-디렉토리-구조)
- [로컬 실행](#-로컬-실행)
- [라이선스](#-라이선스)

---

## 📌 프로젝트 소개

`space-cap.github.io`는 실제 운영 가능한 수준의 **반응형 웹 UI 목업**과 **포트폴리오 페이지**를 GitHub Pages로 서비스하는 정적 웹사이트입니다.

병원 랜딩 페이지, ERP 시스템 Ribbon UI 등 다양한 도메인의 프론트엔드 결과물을 포함하며, 각 프로젝트는 **독립적인 디렉토리**로 분리되어 관리됩니다.

---

## 🗂 포함된 프로젝트

### 🦷 청포도 치과 (`/cheongpodo`)

**치과 의원 공식 홈페이지 랜딩 페이지 목업**

| 항목 | 내용 |
|------|------|
| 특징 | AI 정밀 진단, 무통 마취, 사후 관리 등 병원 특징 강조 |
| 진료과목 | 네비게이션 임플란트, 투명교정, 심미보철, 보존치료 |
| 디자인 | Teal 컬러 기반 반응형 디자인, 부드러운 스크롤 애니메이션 |
| 라이브 링크 | [바로가기 →](https://space-cap.github.io/cheongpodo/) |

---

### 🦶 두발로병원 (`/duballo`)

**족부·관절·척추 전문 병원 홈페이지 랜딩 페이지 목업**

| 항목 | 내용 |
|------|------|
| 특징 | 족부 센터, 관절 센터, 척추(신경외과) 센터 3개 전문센터 구성 |
| 부가기능 | 플로팅 퀵메뉴(PC), 상단 스크롤 시 유틸리티 바 숨김 |
| 디자인 | Sky Blue 브랜드 컬러, 전문 의료기관 느낌의 신뢰감 있는 UI |
| 라이브 링크 | [바로가기 →](https://space-cap.github.io/duballo/) |

---

### 🖥️ 델파이 ERP 목업 (`/temp/Delphi_ERP_Mockup_Ribbon_UI.html`)

**NEXUS ERP 2026 — MS Office 스타일 Ribbon UI 기반 ERP 시스템 목업**

| 항목 | 내용 |
|------|------|
| 특징 | 실제 ERP 업무 화면 흐름을 시뮬레이션한 인터랙티브 목업 |
| 주요 메뉴 | 기초정보관리, 영업/매출, 구매/재고/AS, 회계/금융, 시스템 설정 |
| UI 패턴 | Ribbon Tab, Document Tab(MDI), Master-Detail Grid, 상세 팝업(Modal) |
| 데이터 | Mock DB 기반 목업 데이터 (사용자/거래처/품목/재고 등) |
| 라이브 링크 | [바로가기 →](https://space-cap.github.io/temp/Delphi_ERP_Mockup_Ribbon_UI.html) |

---

## 🛠 기술 스택

| 기술 | 용도 |
|------|------|
| ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=html5&logoColor=white) | 페이지 구조 및 시멘틱 마크업 |
| ![TailwindCSS](https://img.shields.io/badge/TailwindCSS-06B6D4?style=flat&logo=tailwindcss&logoColor=white) | 유틸리티 기반 반응형 스타일링 |
| ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=black) | 인터랙션 및 동적 UI 로직 |
| ![Font Awesome](https://img.shields.io/badge/Font_Awesome-528DD7?style=flat&logo=fontawesome&logoColor=white) | 아이콘 라이브러리 |
| ![Google Fonts](https://img.shields.io/badge/Google_Fonts-4285F4?style=flat&logo=google&logoColor=white) | Noto Sans KR 등 웹폰트 |
| ![GitHub Pages](https://img.shields.io/badge/GitHub_Pages-222222?style=flat&logo=githubpages&logoColor=white) | 정적 사이트 호스팅 |

---

## 📁 디렉토리 구조

```
space-cap.github.io/
│
├── cheongpodo/             # 🦷 청포도 치과 랜딩 페이지
│   └── index.html
│
├── duballo/                # 🦶 두발로병원 랜딩 페이지
│   └── index.html
│
├── temp/                   # 🖥️ 임시/실험용 목업
│   └── Delphi_ERP_Mockup_Ribbon_UI.html
│
├── docs/                   # 📄 문서 디렉토리
├── final/                  # ✅ 완성 결과물
├── portfolio/              # 💼 포트폴리오
├── img/                    # 🖼️ 이미지 리소스
├── download/               # 📦 다운로드 파일
├── toy4_rag/               # 🤖 RAG 실험
├── 정보처리기사/             # 📚 정보처리기사 학습 자료
│   ├── 1장_개발_데이터 입출력 구현.md
│   └── 2장_통합_구현.md
│
├── dental_core.html        # 치과 핵심 페이지
├── index.html              # 메인 진입점
└── README.md               # 이 파일
```

---

## 💻 로컬 실행

별도의 빌드 과정 없이 정적 HTML 파일로 구성되어 있습니다.

```bash
# 저장소 클론
git clone https://github.com/space-cap/space-cap.github.io.git
cd space-cap.github.io

# VS Code Live Server 또는 Python 내장 서버 사용
python -m http.server 8080

# 브라우저에서 접속
# http://localhost:8080
```

또는 **GitHub Pages**를 통해 바로 접속할 수 있습니다:  
🌐 **https://space-cap.github.io**

---

## 📄 라이선스

이 저장소의 코드 및 디자인은 학습 및 포트폴리오 목적으로 제작되었습니다.  
상업적 이용 시 별도 문의 바랍니다.

---

<div align="center">
  <sub>Made with ❤️ by <a href="https://github.com/space-cap">space-cap</a></sub>
</div>
