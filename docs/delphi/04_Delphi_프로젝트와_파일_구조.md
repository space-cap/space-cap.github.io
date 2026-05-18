# Delphi 프로젝트와 파일 구조

> 작성 기준일: 2026-05-19  
> 대상 독자: 기존 Delphi 프로젝트 폴더를 처음 열어보는 사람  
> 목표: `.dpr`, `.dproj`, `.pas`, `.dfm` 파일의 역할을 구분하고, 프로젝트를 어디서부터 읽어야 하는지 익힌다.

## 1. 먼저 큰 그림

Delphi 프로젝트는 코드 파일 하나로만 이루어지지 않는다. 실행 파일의 시작점, 프로젝트 설정, 화면 디자인, Pascal 코드, 리소스, 패키지 파일이 함께 움직인다.

처음 폴더를 열었을 때 이런 파일들이 보이면 정상이다.

```text
MyApp.dpr
MyApp.dproj
MainForm.pas
MainForm.dfm
DataModuleUnit.pas
DataModuleUnit.dfm
MyApp.res
CommonPackage.dpk
```

가장 먼저 외울 관계는 이것이다.

```text
.dpr   = 프로그램 시작점
.dproj = 프로젝트 설정
.pas   = Pascal 코드
.dfm   = Form 디자인 정보
.res   = 리소스
.dpk   = 패키지 프로젝트
```

## 2. Project와 Project Group

Delphi IDE에서는 하나의 프로젝트만 열 수도 있고, 여러 프로젝트를 묶은 Project Group을 열 수도 있다.

| 용어 | 의미 |
| --- | --- |
| Project | 하나의 실행 파일, DLL, 패키지 등을 만드는 단위 |
| Project Group | 여러 Project를 한 번에 묶어 관리하는 단위 |

실무 프로젝트에서는 Project Group이 흔하다.

예:

```text
ERPSystem.groupproj
  - ERPClient.dproj
  - CommonLib.dproj
  - ReportPackage.dproj
  - MigrationTool.dproj
```

이 경우 아무 프로젝트나 실행하면 안 된다. 실제 사용자가 실행하는 프로그램이 무엇인지 먼저 확인해야 한다.

입사 후 기존 폴더를 받았다면 다음 순서로 찾는다.

1. `.groupproj` 파일이 있는지 확인한다.
2. 없다면 `.dproj` 파일을 찾는다.
3. 프로젝트가 여러 개면 메인 실행 프로젝트가 무엇인지 묻는다.
4. 패키지 프로젝트와 실행 프로젝트를 구분한다.

## 3. `.dpr`: 프로그램 시작점

`.dpr` 파일은 Delphi 프로그램의 시작점이다. 공식 문서에서는 실행 가능한 Delphi 애플리케이션이 여러 unit으로 구성되며, 이들을 묶는 단일 project file이 있다고 설명한다. Embarcadero 도구는 이 주 프로그램 소스 모듈에 `.dpr` 확장자를 사용한다.

VCL 프로그램의 `.dpr`은 보통 이런 모양이다.

```pascal
program MyApp;

uses
  Vcl.Forms,
  MainFormUnit in 'MainFormUnit.pas' {MainForm};

{$R *.res}

begin
  Application.Initialize;
  Application.MainFormOnTaskbar := True;
  Application.CreateForm(TMainForm, MainForm);
  Application.Run;
end.
```

여기서 중요한 줄은 이 부분이다.

```pascal
Application.CreateForm(TMainForm, MainForm);
Application.Run;
```

`Application.CreateForm`은 프로그램 시작 시 Form을 만든다. 여러 Form이 여기에 있으면 자동 생성 Form이 여러 개라는 뜻이다. `Application.Run`은 Windows 메시지 루프를 시작해 프로그램이 계속 실행되게 한다.

공식 문서도 Delphi project file은 보통 IDE가 자동으로 만들고 관리하며, 대부분의 로직은 unit 파일에 있다고 설명한다. 즉 `.dpr`은 자주 수정하는 파일이 아니다.

## 4. `.dproj`: 프로젝트 설정

`.dproj`는 프로젝트 설정 파일이다. XML 형식이며 IDE가 관리한다.

여기에는 다음과 같은 정보가 들어간다.

- Target Platform
- Build Configuration
- Search Path
- Output Directory
- Compiler Options
- Version Info
- Application Icon
- Runtime Packages
- 조건부 define

실무에서 `.dproj`는 중요하지만 직접 손으로 자주 고치는 파일은 아니다. IDE의 `Project > Options`에서 설정을 바꾸면 `.dproj`가 변경된다.

Git에서 `.dproj` 충돌이 나면 조심해야 한다. 단순 텍스트 충돌처럼 보이지만 빌드 설정이 깨질 수 있다. 특히 다음 항목은 함부로 덮어쓰지 않는다.

| 항목 | 깨졌을 때 생기는 문제 |
| --- | --- |
| Search Path | unit이나 패키지를 못 찾는다. |
| Output Directory | 실행 파일/빌드 산출물이 엉뚱한 곳에 생긴다. |
| Runtime Packages | 실행 시 BPL/DLL 오류가 난다. |
| Target Platform | Win32/Win64가 바뀌어 빌드가 실패한다. |
| Conditional Defines | 특정 코드가 포함/제외되어 동작이 달라진다. |

## 5. `.pas`: unit과 코드

`.pas` 파일은 Delphi 코드가 들어 있는 파일이다. Delphi에서는 코드 파일을 보통 unit이라고 부른다.

Form unit의 기본 구조는 다음과 같다.

```pascal
unit MainFormUnit;

interface

uses
  System.SysUtils, System.Classes, Vcl.Forms, Vcl.StdCtrls;

type
  TMainForm = class(TForm)
    btnSave: TButton;
    procedure btnSaveClick(Sender: TObject);
  private
  public
  end;

var
  MainForm: TMainForm;

implementation

{$R *.dfm}

procedure TMainForm.btnSaveClick(Sender: TObject);
begin
  ShowMessage('저장합니다.');
end;

end.
```

처음에는 세 구역만 보면 된다.

| 구역 | 역할 |
| --- | --- |
| `interface` | 다른 unit에서 볼 수 있는 선언 |
| `implementation` | 실제 구현 코드 |
| `{$R *.dfm}` | 이 unit과 연결된 Form 디자인 파일 포함 |

`interface`에는 클래스 선언, 컴포넌트 필드, 메서드 선언이 있고, `implementation`에는 실제 이벤트 코드와 업무 로직이 있다.

## 6. `.dfm`: Form 디자인 정보

`.dfm`은 VCL Form의 디자인 정보가 저장되는 파일이다.

예를 들어 Form에 Button 하나를 올리면 `.dfm`에는 대략 이런 정보가 들어간다.

```text
object MainForm: TMainForm
  Caption = 'MainForm'
  ClientHeight = 300
  ClientWidth = 500
  object btnSave: TButton
    Left = 24
    Top = 24
    Width = 75
    Height = 25
    Caption = '저장'
    OnClick = btnSaveClick
  end
end
```

여기서 볼 수 있듯 `.dfm`에는 컴포넌트 이름, 위치, 크기, Caption, 이벤트 연결 정보가 들어간다.

중요한 원칙:

1. `.dfm`은 Form Designer로 수정하는 것이 기본이다.
2. Git diff에서 `.dfm` 변경을 꼭 확인한다.
3. 컴포넌트 이름 변경, 삭제, 이벤트 연결 변경은 `.pas`와 함께 봐야 한다.
4. 직접 수정해야 할 때는 백업 또는 리뷰가 필요하다.

`.dfm` 충돌은 꽤 까다롭다. 버튼 위치 충돌처럼 보여도 실제로는 이벤트 연결이 날아갔을 수 있다.

## 7. `.res`: 리소스 파일

`.res`는 리소스 파일이다. 아이콘, 버전 정보, 기타 리소스가 들어갈 수 있다.

`.dpr`에서 보통 다음 줄로 연결된다.

```pascal
{$R *.res}
```

처음에는 `.res`를 깊이 볼 일은 많지 않다. 다만 아이콘이나 버전 정보가 바뀌었는데 빌드 결과에 반영되지 않는다면 리소스 설정을 확인해야 한다.

## 8. `.dpk`: 패키지 프로젝트

`.dpk`는 Delphi package 파일이다. 패키지는 공통 컴포넌트나 라이브러리를 묶는 단위다.

공식 문서에 따르면 패키지는 IDE에서 `.dpk` 파일로 컴파일할 수 있고, 성공적으로 컴파일하면 `.dcp`, `.bpl` 같은 파일이 생성된다.

| 파일 | 의미 |
| --- | --- |
| `.dpk` | 패키지 소스 프로젝트 |
| `.dcp` | 컴파일된 패키지 심볼/정보 |
| `.bpl` | 런타임 패키지. DLL과 비슷한 역할 |

기존 회사 프로젝트를 열었을 때 "패키지가 없다", "컴포넌트를 찾을 수 없다", "BPL을 로드할 수 없다" 같은 오류가 나면 `.dpk`와 패키지 설치 순서를 확인해야 한다.

패키지는 `11_패키지_라이브러리_외부_컴포넌트.md`에서 더 자세히 다룬다.

## 9. `uses` 절

Delphi의 `uses` 절은 다른 unit을 참조하는 부분이다.

예:

```pascal
uses
  System.SysUtils,
  System.Classes,
  Vcl.Forms,
  CustomerService,
  CustomerRepository;
```

처음에는 `uses`를 Java의 `import`, C#의 `using`, JavaScript의 `import`와 비슷하게 생각해도 된다.

다만 Delphi에는 `interface uses`와 `implementation uses`가 있다.

| 위치 | 의미 |
| --- | --- |
| interface의 uses | 이 unit의 공개 선언에 필요한 참조 |
| implementation의 uses | 구현 내부에서만 필요한 참조 |

가능하면 구현 내부에서만 쓰는 unit은 `implementation uses`에 두는 편이 의존성을 줄이는 데 좋다. 하지만 기존 코드에서는 팀 스타일을 먼저 따른다.

## 10. Form class와 전역 Form 변수

Form unit에는 보통 클래스와 전역 변수가 함께 있다.

```pascal
type
  TMainForm = class(TForm)
  end;

var
  MainForm: TMainForm;
```

`TMainForm`은 Form의 클래스 타입이고, `MainForm`은 실제 Form 인스턴스를 담는 변수다.

초보자가 자주 헷갈리는 부분:

| 이름 | 의미 |
| --- | --- |
| `TMainForm` | 클래스 |
| `MainForm` | 변수/인스턴스 |
| `MainFormUnit` | unit 이름 |
| `MainForm.dfm` | 화면 디자인 파일 |

기존 프로젝트에서는 Form 변수명을 없애거나 직접 생성하는 패턴을 쓸 수도 있다. 그러나 기본 IDE 생성 코드는 위 구조를 따른다.

## 11. 자동 생성 코드와 손대면 위험한 곳

Delphi IDE는 많은 코드를 자동 생성한다.

초기에는 다음을 함부로 지우지 않는다.

```pascal
{$R *.dfm}
```

이 줄이 사라지면 Form 디자인 파일이 연결되지 않는다.

또한 Form 클래스 안의 컴포넌트 필드도 조심한다.

```pascal
btnSave: TButton;
```

이 필드는 Form Designer와 연결되어 있다. IDE에서 컴포넌트를 삭제하면 같이 사라질 수 있지만, 코드에서 직접 지우면 `.dfm`과 불일치가 생길 수 있다.

## 12. 기존 프로젝트를 처음 읽는 순서

기존 Delphi 프로젝트를 처음 받았다면 이렇게 읽는다.

1. `.groupproj` 또는 `.dproj`를 찾는다.
2. Project Manager에서 메인 프로젝트를 확인한다.
3. `.dpr`을 열어 `Application.CreateForm` 목록을 본다.
4. 메인 Form unit을 연다.
5. Form Designer에서 메뉴와 주요 버튼을 훑는다.
6. Object Inspector에서 주요 이벤트를 확인한다.
7. 이벤트 코드에서 업무 흐름을 따라간다.
8. DB 연결 unit 또는 DataModule을 찾는다.
9. 공통 함수/공통 상수 unit을 찾는다.
10. 빌드 설정과 Search Path를 확인한다.

처음부터 모든 파일을 읽으려 하지 않는다. 프로그램 시작점과 메인 화면에서부터 가지를 타고 내려가는 것이 훨씬 빠르다.

## 13. Git에서 특히 조심할 파일

Delphi 프로젝트에서는 다음 파일 변경을 유심히 봐야 한다.

| 파일 | 주의 이유 |
| --- | --- |
| `.dproj` | 빌드 설정이 바뀐다. |
| `.dfm` | 화면/이벤트 연결이 바뀐다. |
| `.pas` | 코드 로직이 바뀐다. |
| `.res` | 아이콘/버전 리소스가 바뀔 수 있다. |
| `.dpk` | 패키지 구성이 바뀐다. |

반대로 빌드 산출물은 보통 Git에 넣지 않는다. 팀 정책에 따라 다르지만 일반적으로 다음 파일은 제외 대상이 될 수 있다.

```text
*.dcu
*.exe
*.dll
*.bpl
*.dcp
*.map
*.identcache
__history/
Win32/
Win64/
```

이미 회사 저장소에 규칙이 있다면 그 규칙을 따른다.

## 14. 파일 이름 변경 시 주의

Delphi에서 Form 이름, unit 이름, 파일 이름을 바꾸는 일은 생각보다 민감하다.

예를 들어 `Unit1.pas`를 `MainFormUnit.pas`로 바꾸고 싶다면 다음이 함께 맞아야 한다.

- 파일명
- unit 선언 이름
- `.dpr`의 참조
- `.dfm` 연결
- Project Manager 항목
- 다른 unit의 uses

IDE의 리팩터링/저장 기능을 사용하거나, 팀에서 쓰는 방식에 맞춰 바꾼다. 파일 탐색기에서 이름만 바꾸면 프로젝트가 깨질 수 있다.

## 바로 해볼 실습

1. 새 VCL Forms Application을 만든다.
2. 저장한 뒤 프로젝트 폴더를 파일 탐색기에서 연다.
3. `.dpr`, `.dproj`, `.pas`, `.dfm`, `.res` 파일을 확인한다.
4. `.dpr`에서 `Application.CreateForm` 줄을 찾는다.
5. `.pas`에서 `{$R *.dfm}` 줄을 찾는다.
6. Form에 Button을 하나 올리고 저장한 뒤 `.dfm` 변경 내용을 확인한다.

## 입사 후 확인할 질문

1. 프로젝트는 `.groupproj`로 열어야 하나요, `.dproj`로 열어야 하나요?
2. 메인 실행 프로젝트는 무엇인가요?
3. 자동 생성 Form과 수동 생성 Form을 구분하는 기준이 있나요?
4. `.dproj` 충돌은 누가/어떤 기준으로 해결하나요?
5. 빌드 산출물 제외 규칙이 정리된 `.gitignore`가 있나요?
6. 공통 패키지와 외부 컴포넌트 설치 순서가 있나요?

## 자주 막히는 지점

- `.dpr`과 `.dproj`를 구분하지 못한다.
- `.pas`만 수정하고 `.dfm` 변경을 확인하지 않는다.
- Form 컴포넌트 필드를 코드에서 직접 지워서 `.dfm`과 불일치가 난다.
- Project Group 안에서 잘못된 프로젝트를 실행한다.
- Search Path 문제를 코드 오류로 착각한다.
- 파일 이름만 바꾸고 unit 이름과 `.dpr` 참조를 맞추지 않는다.

## 참고 자료

- [Programs and Units - Delphi](https://docwiki.embarcadero.com/RADStudio/Athens/en/Programs_and_Units_%28Delphi%29)
- [Adding Forms - RAD Studio](https://docwiki.embarcadero.com/RADStudio/Athens/en/Adding_Forms)
- [Packages - Delphi](https://docwiki.embarcadero.com/RADStudio/Athens/en/Packages_%28Delphi%29)

