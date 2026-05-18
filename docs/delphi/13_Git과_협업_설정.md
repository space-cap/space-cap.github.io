# Git과 협업 설정

> 작성 기준일: 2026-05-19  
> 대상 독자: Delphi 프로젝트를 Git으로 관리하거나 회사 저장소에서 받아 작업하는 사람  
> 목표: Delphi 특유의 `.dfm`, `.dproj`, 패키지/산출물 파일을 안전하게 다루는 Git 협업 기본기를 익힌다.

## 1. Delphi에서도 Git은 필수다

Delphi 프로젝트가 오래된 Windows 업무 프로그램이라고 해서 버전 관리가 덜 중요한 것은 아니다. 오히려 더 중요하다.

Delphi 프로젝트에는 코드뿐 아니라 화면 디자인, 프로젝트 설정, 패키지 설정이 함께 들어간다.

```text
.pas  = 코드
.dfm  = 화면 디자인/이벤트 연결
.dproj = 프로젝트 설정
.dpr  = 프로그램 시작점
.dpk  = 패키지 프로젝트
```

이 파일들이 서로 맞지 않으면 빌드가 안 되거나, 화면은 열리지만 버튼 이벤트가 안 동작하는 일이 생긴다.

## 2. RAD Studio의 Git 통합

공식 문서에 따르면 RAD Studio IDE의 Version Insight 기능은 Git 버전 관리 시스템을 IDE 안에 통합한다.

Git을 IDE에서 쓰려면 `Tools > Options > Version Control > Git`에서 Git 실행 파일 경로와 사용자 이름/이메일을 설정할 수 있다.

공식 문서 기준 Git 옵션에서 설정하는 항목:

| 항목 | 의미 |
| --- | --- |
| Git Executable | 설치된 Git 실행 파일 경로 |
| User name | 커밋 작성자 이름 |
| E-mail address | 커밋 작성자 이메일 |
| Remote authentication data | IDE에 저장된 원격 인증 정보 |

하지만 회사에서는 IDE 내 Git보다 외부 Git 도구를 더 많이 쓸 수도 있다.

예:

- Git CLI
- Sourcetree
- GitKraken
- TortoiseGit
- VS Code
- 사내 Git 웹 UI

중요한 것은 도구가 아니라 팀 규칙을 따르는 것이다.

## 3. 처음 클론할 때

회사 저장소를 받을 때 확인할 것:

1. 저장소 URL
2. 브랜치 이름
3. 서브모듈 사용 여부
4. LFS 사용 여부
5. 외부 컴포넌트/패키지 별도 저장소 여부
6. 개발환경 설치 문서 위치
7. 빌드 순서 문서 위치

단순히 소스를 클론했다고 개발환경이 끝나는 것은 아니다. Delphi는 IDE에 설치된 컴포넌트와 Library Path도 중요하다.

## 4. Git에 넣어야 하는 파일

일반적으로 넣어야 하는 파일:

| 파일 | 이유 |
| --- | --- |
| `.pas` | 소스 코드 |
| `.dfm` | Form 디자인과 이벤트 연결 |
| `.dpr` | 프로그램 시작점 |
| `.dproj` | 프로젝트 설정 |
| `.groupproj` | Project Group |
| `.dpk` | 패키지 프로젝트 |
| `.res` | 필요한 리소스 |
| `.sql` | DB 스크립트 |
| `.ini.template` | 설정 파일 템플릿 |
| 문서 | 설치/빌드/배포 절차 |

팀 정책에 따라 일부 자동 생성 파일도 넣을 수 있다. 기존 저장소 규칙을 먼저 따른다.

## 5. Git에서 제외할 가능성이 높은 파일

일반적으로 제외 대상이 될 수 있는 파일:

```gitignore
*.dcu
*.exe
*.dll
*.bpl
*.dcp
*.map
*.tds
*.identcache
*.local
*.stat
__history/
Win32/
Win64/
Debug/
Release/
```

주의: DLL/BPL이 항상 제외인 것은 아니다. 회사가 외부 라이브러리를 바이너리로 관리하는 정책이라면 저장소에 포함될 수 있다. 무조건 삭제하지 않는다.

## 6. `.dfm` 변경은 꼭 확인한다

`.dfm`은 화면 디자인 파일이다. 버튼 위치, Caption, 이벤트 연결, DB 컴포넌트 속성이 들어간다.

예:

```text
object btnSave: TButton
  Caption = '저장'
  OnClick = btnSaveClick
end
```

`.dfm` 변경을 가볍게 보면 안 된다.

확인할 것:

1. 의도한 컴포넌트만 바뀌었는가?
2. 이벤트 연결이 바뀌었는가?
3. 컴포넌트가 삭제되었는가?
4. TabOrder가 대량으로 바뀌었는가?
5. DB 연결 문자열 같은 민감 정보가 들어갔는가?
6. Form을 열기만 했는데 IDE가 자동 변경한 것은 아닌가?

## 7. `.dproj` 충돌

`.dproj`는 XML 기반 프로젝트 설정 파일이다. IDE에서 옵션을 바꾸면 변경된다.

자주 바뀌는 항목:

- Search Path
- Output Directory
- Build Configuration
- Target Platform
- Runtime Packages
- Conditional Defines
- Version Info

`.dproj` 충돌은 조심해서 해결해야 한다. 단순히 내 것을 선택하거나 상대 것을 선택하면 빌드 환경이 깨질 수 있다.

충돌 시 확인:

1. 누가 어떤 설정을 바꿨는가?
2. 전체 팀에 필요한 설정인가, 개인 PC 경로인가?
3. 절대 경로가 들어갔는가?
4. Debug/Release/Win32/Win64 중 어느 설정인가?
5. 빌드가 실제로 되는가?

## 8. 개인 PC 경로를 커밋하지 않기

Delphi 프로젝트 옵션에 개인 PC 경로가 들어가기 쉽다.

나쁜 예:

```text
C:\Users\rich\Downloads\ComponentX\Source
D:\MyTest\DBDriver
```

이런 경로가 `.dproj`나 설정 파일에 들어가면 다른 사람 PC에서 빌드가 깨진다.

가능하면 상대 경로, 환경 변수, 사내 표준 경로를 쓴다.

## 9. 커밋 전 체크리스트

커밋 전 확인:

1. 변경 파일 목록을 본다.
2. `.pas` 변경이 의도한 코드인지 확인한다.
3. `.dfm` 변경이 의도한 화면 변경인지 확인한다.
4. `.dproj` 변경이 필요한 설정 변경인지 확인한다.
5. 빌드 산출물이 들어가지 않았는지 확인한다.
6. DB 접속 정보/비밀번호가 들어가지 않았는지 확인한다.
7. Debug 로그나 임시 주석이 남지 않았는지 확인한다.
8. 최소 빌드 또는 실행 확인을 한다.

## 10. 브랜치 전략

회사마다 브랜치 전략이 다르다.

예:

| 브랜치 | 용도 |
| --- | --- |
| `main` 또는 `master` | 운영/릴리스 기준 |
| `develop` | 개발 통합 |
| `feature/...` | 기능 개발 |
| `hotfix/...` | 긴급 수정 |
| `release/...` | 배포 준비 |

Delphi 업무 시스템에서는 운영 배포와 직접 연결되는 경우가 많으므로 브랜치 실수가 위험하다. 작업 전 현재 브랜치를 확인한다.

## 11. 충돌이 자주 나는 파일

| 파일 | 이유 |
| --- | --- |
| `.dfm` | 여러 사람이 같은 화면 수정 |
| `.dproj` | IDE 옵션 변경 |
| `.groupproj` | 프로젝트 추가/삭제 |
| 공통 unit | 여러 화면이 공유 |
| 설정 파일 | 환경별 값 충돌 |

`.dfm` 충돌은 특히 조심한다. 충돌 해결 후 Form Designer로 열리는지, 이벤트 연결이 유지되는지 확인한다.

## 12. 코드 리뷰에서 볼 것

Delphi 코드 리뷰 포인트:

1. 이벤트 핸들러에 너무 많은 로직이 들어갔는가?
2. DB 파라미터를 제대로 쓰는가?
3. 트랜잭션 Commit/Rollback이 안전한가?
4. 객체 `Create` 후 `Free`가 있는가?
5. 운영 DB 접속 정보가 들어가지 않았는가?
6. `.dfm` 이벤트 연결이 의도와 맞는가?
7. `.dproj` 변경이 불필요하지 않은가?
8. Win32/Win64 빌드에 영향이 없는가?

## 13. IDE Git과 외부 Git을 섞어 쓸 때

IDE Git 기능과 외부 Git 도구를 같이 써도 되지만, 상태를 잘 확인해야 한다.

주의:

- IDE에서 파일이 열려 있으면 외부 변경 반영이 늦게 보일 수 있다.
- Form Designer가 자동 저장을 일으킬 수 있다.
- 외부 Git에서 브랜치를 바꾼 뒤 IDE가 열린 파일을 덮어쓸 수 있다.
- 충돌 해결 후 IDE에서 다시 저장하면서 충돌 흔적이 남을 수 있다.

브랜치를 바꾸거나 pull하기 전에는 IDE에서 Save All 후 프로젝트를 닫는 습관도 좋다.

## 14. History Manager

RAD Studio에는 History Manager가 있고, 공식 Git 통합 문서는 Git으로 관리되는 파일의 Git history와 로컬 history 정보를 볼 수 있다고 설명한다.

History Manager는 다음 상황에서 도움이 된다.

- 방금 바꾼 코드를 되돌려 비교하고 싶다.
- IDE 자동 저장 전후를 보고 싶다.
- Git 커밋 전 변경을 확인하고 싶다.

하지만 History Manager가 Git 백업을 대체하지는 않는다.

## 바로 해볼 실습

1. Delphi 예제 프로젝트를 Git 저장소로 초기화한다.
2. `.gitignore`에 빌드 산출물을 제외한다.
3. Form에 Button을 하나 추가하고 `.pas`, `.dfm` 변경을 비교한다.
4. Project Options를 바꿔 `.dproj` diff를 확인한다.
5. 불필요한 `.dproj` 변경을 되돌리는 연습을 한다.
6. 커밋 메시지에 변경 의도를 짧게 적는다.

## 입사 후 확인할 질문

1. Git은 IDE에서 쓰나요, 외부 도구를 쓰나요?
2. 브랜치 전략은 어떻게 되나요?
3. `.dfm` 충돌 해결 기준이 있나요?
4. `.dproj` 변경은 커밋해도 되나요?
5. 빌드 산출물은 저장소에 포함하나요?
6. 외부 컴포넌트 바이너리는 어디서 관리하나요?
7. 코드 리뷰 기준이 있나요?

## 자주 막히는 지점

- `.dfm` 변경을 확인하지 않고 커밋한다.
- 개인 PC 절대 경로를 `.dproj`에 넣어 커밋한다.
- 빌드 산출물을 대량으로 커밋한다.
- Form Designer 자동 변경을 의도한 변경으로 착각한다.
- IDE가 열린 상태에서 브랜치를 바꿔 파일 상태가 꼬인다.
- `.dproj` 충돌을 무심코 한쪽으로 덮어쓴다.

## 참고 자료

- [Git Integration in the IDE](https://docwiki.embarcadero.com/RADStudio/Athens/en/Git_Integration_in_the_IDE)
- [Version Control Options: Git](https://docwiki.embarcadero.com/RADStudio/Athens/en/Version_Control_Options%3A_Git)
- [Using Source Control](https://docwiki.embarcadero.com/RADStudio/Athens/en/Using_Source_Control)
- [Version Control System](https://docwiki.embarcadero.com/RADStudio/Athens/en/Version_Control_System)

