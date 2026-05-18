# 기존 Delphi 프로젝트 처음 열기

> 작성 기준일: 2026-05-19  
> 대상 독자: 회사에서 기존 Delphi 소스 폴더를 처음 받은 입사 초기 개발자  
> 목표: 프로젝트를 어디서부터 열고, 어떤 설정을 확인하고, 첫 빌드 전 무엇을 점검해야 하는지 익힌다.

## 1. 처음 열 때의 목표

기존 프로젝트를 처음 받았을 때 목표는 "바로 고치기"가 아니다.

첫 목표는 이것이다.

```text
프로젝트를 안전하게 열고
-> 구조를 파악하고
-> 빌드 조건을 확인하고
-> 실행 흐름을 찾고
-> 작은 수정이 가능한 상태를 만든다.
```

Delphi 프로젝트는 소스 코드뿐 아니라 IDE 설정, 패키지, 외부 컴포넌트, DB 연결, 빌드 경로가 함께 맞아야 한다.

## 2. 폴더에서 먼저 볼 파일

소스 폴더를 받으면 먼저 확장자를 본다.

| 파일 | 의미 |
| --- | --- |
| `.groupproj` | Project Group. 여러 프로젝트를 묶음 |
| `.dproj` | Delphi 프로젝트 설정 |
| `.dpr` | 프로그램 시작점 |
| `.pas` | Pascal unit |
| `.dfm` | Form 디자인 |
| `.dpk` | 패키지 프로젝트 |
| `.res` | 리소스 |
| `.ini`, `.json`, `.xml` | 설정 파일 가능성 |

공식 문서에 따르면 project group은 `.groupproj` 확장자를 가지며, project file은 `.dproj` 확장자를 가진다.

여러 파일이 있다면 보통 `.groupproj`부터 연다. 없다면 메인 `.dproj`를 연다.

## 3. 먼저 묻고 열기

입사 초기에는 다음을 묻고 시작한다.

1. 이 폴더에서 어떤 파일을 열어야 하나요?
2. `.groupproj`로 여나요, 특정 `.dproj`로 여나요?
3. 메인 실행 프로젝트는 무엇인가요?
4. 먼저 빌드해야 하는 패키지가 있나요?
5. 필요한 외부 컴포넌트가 설치되어 있나요?
6. 기본 Target Platform은 Win32인가요, Win64인가요?
7. 개발 DB 접속 설정은 어디에 있나요?

이 질문은 초보 티가 아니라 실무 감각이다. 잘못된 프로젝트를 열고 임의로 빌드하면 더 오래 걸린다.

## 4. Project Group 확인

Project Group에는 여러 프로젝트가 들어 있을 수 있다.

예:

```text
ERPSystem.groupproj
  - CommonCore.dproj
  - CommonUI.dproj
  - ERPClient.dproj
  - ReportPackage.dproj
  - MigrationTool.dproj
```

공식 문서에서도 Project Manager에서 project group node에 새 프로젝트나 기존 프로젝트를 추가할 수 있다고 설명한다.

확인할 것:

| 항목 | 질문 |
| --- | --- |
| 프로젝트 개수 | 몇 개의 프로젝트가 묶여 있는가? |
| 메인 프로젝트 | 실제 실행 EXE는 무엇인가? |
| 패키지 프로젝트 | 먼저 빌드해야 하는 `.dpk`/패키지가 있는가? |
| 도구 프로젝트 | 마이그레이션/관리 도구가 섞여 있는가? |
| 빌드 순서 | 어떤 순서로 빌드해야 하는가? |

## 5. 메인 Form 찾기

메인 Form은 프로그램 흐름의 출발점이다.

찾는 방법:

1. `.dpr` 파일을 연다.
2. `Application.CreateForm` 줄을 찾는다.
3. 첫 번째로 생성되는 주요 Form을 확인한다.

예:

```pascal
Application.Initialize;
Application.MainFormOnTaskbar := True;
Application.CreateForm(TMainForm, MainForm);
Application.Run;
```

여기서 `TMainForm`과 `MainForm`을 따라가면 메인 화면을 찾을 수 있다.

주의: 로그인 Form, Splash Form, DataModule이 먼저 생성될 수도 있다. 그 경우 실제 업무 메인 화면은 조금 뒤에 열릴 수 있다.

## 6. 프로그램 시작 흐름 보기

`.dpr`에서 시작해 다음을 본다.

```text
.dpr
-> Application.Initialize
-> DataModule 생성
-> Login Form 생성/표시
-> Main Form 생성
-> Application.Run
```

확인할 것:

- 로그인 로직은 어디인가?
- DB 연결은 언제 열리는가?
- 환경 설정 파일은 언제 읽는가?
- 권한/메뉴는 언제 로딩되는가?
- 메인 Form은 어떤 unit인가?

첫날에는 전체 업무를 다 이해하려 하지 말고 시작 흐름만 잡아도 큰 성과다.

## 7. 외부 컴포넌트 오류 대응

Form을 열 때 이런 오류가 날 수 있다.

```text
Class TcxGrid not found
Package xxx.bpl not found
Error reading MainForm.dfm
```

이 경우 `.dfm`을 고치려 하지 않는다. 필요한 컴포넌트가 설치되지 않았거나 버전이 맞지 않는 것이다.

대응 순서:

1. 오류 메시지 원문을 복사한다.
2. 어떤 class/package가 없는지 확인한다.
3. 사내 개발환경 문서에서 컴포넌트 목록을 찾는다.
4. 선배에게 설치 파일/버전을 확인한다.
5. 설치 후 IDE를 재시작한다.
6. 다시 Form을 연다.

## 8. Search Path 문제

빌드할 때 unit을 못 찾는 오류가 날 수 있다.

```text
File not found: 'CommonUtils.dcu'
Unit CommonDb not found
```

원인 후보:

- Search Path 누락
- Library Path 누락
- 공통 소스 폴더 미클론
- 서브모듈 미초기화
- 패키지 빌드 누락
- 다른 브랜치 사용

대응:

1. 해당 unit 파일이 실제로 있는지 검색한다.
2. 있으면 Search Path에 포함되어 있는지 확인한다.
3. 없으면 별도 저장소/공통 라이브러리인지 확인한다.
4. 패키지 빌드 순서를 확인한다.

## 9. DB 연결 정보 찾기

업무 프로그램은 DB 연결 없이는 제대로 실행되지 않을 수 있다.

찾을 위치:

| 위치 | 예 |
| --- | --- |
| 설정 파일 | `.ini`, `.json`, `.xml` |
| DataModule | `TFDConnection`, ADOConnection |
| 로그인 모듈 | 사용자/회사/DB 선택 |
| 공통 unit | `DbConfig`, `ConnectionManager` |
| 레지스트리 | 오래된 프로그램 |
| 환경 변수 | 서버별 설정 |

DB 연결 정보를 찾았다고 바로 수정하지 않는다. 개발 DB/테스트 DB/운영 DB 구분을 먼저 확인한다.

## 10. 처음 빌드하기 전 체크리스트

첫 빌드 전 확인:

1. 올바른 브랜치인가?
2. 필요한 서브모듈/외부 소스가 있는가?
3. Delphi 버전이 맞는가?
4. 에디션이 맞는가?
5. 외부 컴포넌트가 설치되어 있는가?
6. 패키지를 먼저 빌드해야 하는가?
7. Target Platform이 맞는가?
8. Build Configuration이 Debug인가?
9. Search Path/Library Path가 맞는가?
10. 개발 DB 설정이 준비되었는가?

## 11. 첫 빌드

처음에는 바로 Release 전체 빌드보다 Debug 빌드로 시작한다.

순서:

1. 패키지가 있으면 지정된 순서로 빌드한다.
2. 메인 프로젝트를 Active Project로 설정한다.
3. Target Platform을 회사 기준으로 맞춘다.
4. Debug 구성으로 Build한다.
5. Messages 창의 첫 Error를 확인한다.
6. 오류를 하나씩 해결한다.

오류가 여러 개 나오면 전부 고치려고 하지 말고 첫 번째 실제 원인부터 본다.

## 12. 첫 실행

첫 실행은 신중하게 한다.

확인:

1. 개발 DB에 연결되는가?
2. 운영 DB가 아닌가?
3. 로그인 계정은 테스트 계정인가?
4. 저장/삭제 버튼을 누르지 않아도 되는 화면부터 보는가?
5. 로그 파일 위치를 아는가?
6. 설정 파일 백업이 있는가?

처음 실행에서는 메뉴 탐색과 조회 화면 확인 정도로 시작한다. 저장/삭제/전송/마감/확정 같은 버튼은 의미를 확인하기 전까지 누르지 않는다.

## 13. 화면 찾기

특정 업무 화면을 찾는 방법:

1. 메뉴 문구를 검색한다.
2. `.dfm`의 `Caption`을 검색한다.
3. 메뉴 `OnClick` 이벤트를 찾는다.
4. 어떤 Form을 생성하는지 본다.
5. Form unit을 연다.
6. 주요 버튼과 Query를 확인한다.

예:

```pascal
procedure TMainForm.mnuCustomerClick(Sender: TObject);
begin
  TCustomerForm.Execute;
end;
```

## 14. 첫 수정 작업 전

처음 수정은 작아야 한다.

좋은 첫 작업:

- 화면 문구 수정
- 버튼 Enabled 조건 수정
- 조회 조건 하나 추가
- 메시지 문구 정리
- 로그 보강
- 단순 null 체크 추가

피해야 할 첫 작업:

- DB 스키마 변경
- 공통 모듈 대규모 수정
- 패키지 구조 변경
- 배포 설정 변경
- 운영 DB 직접 데이터 수정
- Form 대량 리팩터링

## 15. 변경 후 확인

작은 수정도 다음을 확인한다.

1. 빌드 성공
2. 수정 화면 실행
3. 관련 이벤트 동작
4. `.dfm` 변경 의도 확인
5. `.dproj` 불필요 변경 여부 확인
6. DB 연결 환경 확인
7. Git diff 확인
8. 선배 리뷰 요청

## 16. 온보딩 노트 만들기

처음 프로젝트를 열며 다음을 기록한다.

```text
프로젝트명:
Delphi 버전:
에디션:
Target Platform:
메인 .groupproj/.dproj:
메인 Form:
DBMS:
개발 DB 설정 위치:
외부 컴포넌트:
패키지 빌드 순서:
EXE output 경로:
로그 위치:
배포 방식:
```

이 노트는 나중에 다음 신입에게도 큰 도움이 된다.

## 바로 해볼 실습

1. 예제 프로젝트 폴더에서 `.dpr`, `.dproj`, `.pas`, `.dfm`을 찾는다.
2. `.dpr`에서 `Application.CreateForm`을 확인한다.
3. Project Manager에서 Active Project와 Target Platform을 확인한다.
4. Form Designer에서 버튼 하나의 `OnClick`을 찾아 코드로 이동한다.
5. Debug로 빌드한다.
6. Git diff에서 `.pas`, `.dfm`, `.dproj` 변경 여부를 본다.

## 입사 후 확인할 질문

1. 어떤 파일로 프로젝트를 열어야 하나요?
2. 메인 실행 프로젝트와 메인 Form은 무엇인가요?
3. 패키지 빌드 순서가 있나요?
4. 외부 컴포넌트 설치 문서가 있나요?
5. 개발 DB 접속 설정은 어디에 있나요?
6. 처음 실행해도 안전한 테스트 계정이 있나요?
7. 첫 수정 작업은 어떤 화면부터 하면 좋을까요?

## 자주 막히는 지점

- `.dproj`를 아무거나 열고 메인 프로젝트라고 생각한다.
- 패키지 설치 전 Form Designer 오류를 코드 문제로 본다.
- Search Path 문제를 unit 누락으로만 생각한다.
- 운영 DB 연결 여부를 확인하지 않고 실행한다.
- 첫 작업부터 너무 큰 리팩터링을 하려 한다.
- Git diff에서 `.dfm`/`.dproj` 변경을 확인하지 않는다.

## 참고 자료

- [Starting a Project - Overview](https://docwiki.embarcadero.com/RADStudio/Athens/en/Starting_a_Project_-_Overview)
- [Adding a Project to the Project Group](https://docwiki.embarcadero.com/RADStudio/Athens/en/Adding_a_Project_to_the_Project_Group)
- [Programs and Units - Delphi](https://docwiki.embarcadero.com/RADStudio/Athens/en/Programs_and_Units_%28Delphi%29)
- [Project Manager](https://docwiki.embarcadero.com/RADStudio/Athens/en/Project_Manager)

