# IDE 첫 실행과 화면 구성

> 작성 기준일: 2026-05-19  
> 대상 독자: Delphi/RAD Studio IDE를 처음 실행하는 사람  
> 목표: IDE 화면에서 무엇을 봐야 하는지, 각 창이 어떤 역할을 하는지 익힌다.

## 1. 처음 실행했을 때 당황하지 않기

Delphi IDE를 처음 열면 Visual Studio Code나 IntelliJ와는 조금 다른 느낌을 받는다. 코드 편집기 하나가 중심인 도구라기보다, 화면 설계 도구와 코드 편집기와 프로젝트 관리 창이 함께 붙어 있는 작업대에 가깝다.

처음부터 모든 메뉴를 외울 필요는 없다. 먼저 다음 다섯 가지만 찾으면 된다.

1. `Project Manager`
2. `Form Designer`
3. `Object Inspector`
4. `Tool Palette`
5. `Code Editor`

이 다섯 개가 Delphi IDE의 기본 작업 흐름이다.

```text
Project Manager에서 파일을 고른다
-> Form Designer에서 화면을 본다
-> Tool Palette에서 컴포넌트를 올린다
-> Object Inspector에서 속성/이벤트를 바꾼다
-> Code Editor에서 이벤트 코드를 작성한다
```

이 흐름 하나를 잡으면 IDE가 덜 낯설어진다.

## 2. 첫 실행 화면

RAD Studio Athens 공식 문서에 따르면 처음 시작할 때 테마를 선택하고, 소스 제어와 공통 설정을 구성할 수 있다. 실제 화면은 버전과 에디션에 따라 조금씩 다를 수 있다.

처음에는 다음 정도만 기억한다.

| 항목 | 설명 | 처음 선택 기준 |
| --- | --- | --- |
| Theme | IDE 색상 테마 | 눈이 편한 것으로 선택 |
| Source Control | Git 등 소스 제어 설정 | 회사 정책 확인 전까지 기본값 유지 |
| Welcome Page | 최근 프로젝트, 문서, 예제 접근 화면 | 새 프로젝트 생성/최근 프로젝트 열기 용도 |
| Layout | 창 배치 | 기본 레이아웃으로 시작 |

처음 실행 후 IDE 창 배치가 마음에 들지 않더라도 너무 빨리 커스터마이징하지 않는 편이 좋다. 선배 개발자와 화면을 같이 보거나 문서를 따라갈 때 기본 위치를 알고 있으면 훨씬 편하다.

## 3. IDE 기본 지도

Delphi IDE의 화면은 보통 다음 역할로 나뉜다.

| 위치/창 | 역할 |
| --- | --- |
| 중앙 | Form Designer 또는 Code Editor |
| 오른쪽 | Object Inspector, Tool Palette |
| 왼쪽/오른쪽 | Project Manager, Structure View |
| 아래쪽 | Messages, Search Results, Debug 관련 창 |
| 상단 | 메뉴, 툴바, Run/Build 버튼 |

창 배치는 사용자가 바꿀 수 있으므로 절대적인 위치는 아니다. 그러나 창 이름과 역할은 거의 그대로다.

## 4. Welcome Page

`Welcome Page`는 IDE를 열었을 때 처음 만나는 시작 화면이다.

여기서 보통 할 수 있는 일은 다음과 같다.

- 최근 프로젝트 열기
- 새 프로젝트 만들기
- 예제나 문서 접근
- IDE 관련 안내 확인

실무에서는 Welcome Page보다 기존 프로젝트를 여는 일이 더 많다. 입사 후에는 보통 다음 중 하나로 시작한다.

- `File > Open Project`
- `File > Open Project Group`
- 최근 프로젝트 목록에서 열기
- `.dproj` 또는 `.groupproj` 파일을 직접 열기

처음에는 어떤 파일을 열어야 할지 모를 수 있다. 이때는 폴더에서 `.groupproj`, `.dproj`, `.dpr` 파일을 찾는다.

## 5. Project Manager

`Project Manager`는 현재 열려 있는 프로젝트와 파일을 보여주는 창이다.

공식 문서에서도 Project Manager에서 프로젝트의 Form과 관련 unit을 볼 수 있다고 설명한다. 실무에서는 이 창이 사실상 프로젝트의 지도다.

Project Manager에서 주로 확인할 것:

| 항목 | 의미 |
| --- | --- |
| Project Group | 여러 프로젝트를 묶은 단위 |
| Project | 하나의 실행 파일, DLL, 패키지 등 |
| Build Configuration | Debug, Release 같은 빌드 구성 |
| Target Platform | Win32, Win64 같은 대상 플랫폼 |
| Forms | 화면 파일 |
| Units | 일반 코드 파일 |
| References/Requires | 참조 패키지나 라이브러리 |

처음 회사 프로젝트를 열었다면 Project Manager에서 먼저 다음을 본다.

1. 프로젝트가 하나인지 여러 개인지
2. 실행 프로젝트가 무엇인지
3. 메인 Form이 무엇인지
4. Target Platform이 Win32인지 Win64인지
5. Debug/Release 중 무엇이 선택되어 있는지

Delphi 프로젝트는 오래 운영된 경우가 많아서 프로젝트 그룹 안에 실행 파일, 패키지, 공통 라이브러리, 테스트 도구가 함께 들어 있을 수 있다. 아무거나 빌드하지 말고, 먼저 어떤 프로젝트가 최종 프로그램인지 확인한다.

## 6. Form Designer

`Form Designer`는 화면을 눈으로 배치하는 곳이다.

공식 문서에서는 Form Designer가 WYSIWYG, 즉 "보이는 대로 얻는다"는 방식으로 UI를 빠르게 만들고 수정할 수 있게 해준다고 설명한다. 보통 하나의 Form은 실행 시 하나의 창이나 화면 일부가 된다.

Form Designer에서 하는 일:

- 버튼, 입력창, 그리드, 라벨 배치
- 컴포넌트 크기와 위치 조정
- Align, Anchors 같은 배치 속성 조정
- 메뉴, 패널, 탭 구성 확인
- 화면에 올라간 컴포넌트 선택

처음에는 Form을 "HTML 페이지"처럼 생각하기보다 "Windows 창을 설계하는 캔버스"로 생각하면 쉽다.

주의할 점:

- Form Designer에서 바꾼 내용은 보통 `.dfm` 파일에 저장된다.
- `.dfm`은 텍스트처럼 보여도 IDE가 관리하는 화면 정의 파일이다.
- 화면 수정 후 `.pas`와 `.dfm`이 함께 바뀌는 경우가 흔하다.
- 컴포넌트를 삭제하면 연결된 이벤트 코드가 남을 수 있으므로 확인이 필요하다.

## 7. Tool Palette

`Tool Palette`는 Form에 올릴 컴포넌트를 찾는 창이다.

예를 들어 VCL 프로젝트에서는 다음과 같은 컴포넌트를 자주 만난다.

| 컴포넌트 | 역할 |
| --- | --- |
| `TButton` | 버튼 |
| `TLabel` | 화면에 글자 표시 |
| `TEdit` | 한 줄 입력 |
| `TMemo` | 여러 줄 입력 |
| `TComboBox` | 선택 목록 |
| `TCheckBox` | 체크박스 |
| `TPanel` | 영역 묶기 |
| `TPageControl` | 탭 화면 |
| `TStringGrid` | 간단한 표 |
| `TDBGrid` | DB 데이터 표 |
| `TMainMenu` | 상단 메뉴 |
| `TOpenDialog` | 파일 열기 대화상자 |

컴포넌트에는 두 종류가 있다.

| 종류 | 설명 | 예 |
| --- | --- | --- |
| Visual Component | 실행 시 화면에 보인다. | Button, Edit, Grid |
| Non-Visual Component | 실행 시 화면에는 안 보이지만 기능을 제공한다. | Timer, Dialog, Query |

Form Designer 아래쪽이나 별도 영역에 아이콘처럼 놓인 컴포넌트가 있다면 Non-Visual Component일 수 있다. DB 연결, 타이머, 대화상자 같은 기능이 여기에 속한다.

## 8. Object Inspector

`Object Inspector`는 선택한 Form이나 컴포넌트의 속성과 이벤트를 수정하는 창이다.

공식 문서는 Object Inspector가 선택된 컴포넌트의 published properties를 보여주고, Events 탭에서 이벤트 핸들러를 연결할 수 있다고 설명한다. Delphi IDE에서 가장 자주 쓰는 창 중 하나다.

Object Inspector에는 보통 두 탭이 있다.

| 탭 | 역할 |
| --- | --- |
| Properties | 속성 변경 |
| Events | 이벤트 핸들러 연결 |

예를 들어 Button을 선택했을 때:

| 항목 | 의미 |
| --- | --- |
| `Name` | 코드에서 사용할 컴포넌트 이름 |
| `Caption` | 버튼에 표시되는 글자 |
| `Enabled` | 사용 가능 여부 |
| `Visible` | 표시 여부 |
| `Align` | 부모 영역 안에서의 배치 |
| `Anchors` | 창 크기 변경 시 고정 기준 |
| `OnClick` | 클릭했을 때 실행될 이벤트 |

처음에는 `Name`과 `Caption`을 구분해야 한다.

```text
Name    = 코드에서 부르는 이름
Caption = 사용자 화면에 보이는 글자
```

예를 들어 저장 버튼이라면:

```text
Name    = btnSave
Caption = 저장
```

`Caption`만 바꿔야 하는데 `Name`을 바꾸면 코드 참조가 함께 바뀌거나 오류가 생길 수 있다. 반대로 `Name`을 의미 있게 바꾸면 코드가 읽기 좋아진다.

## 9. Events 탭과 이벤트 핸들러

Delphi는 이벤트 기반 개발을 많이 한다. 버튼을 누르면 `OnClick`, 폼이 열리면 `OnCreate`, 입력값이 바뀌면 `OnChange` 같은 이벤트가 발생한다.

이벤트 핸들러를 만드는 기본 흐름:

1. Form Designer에서 컴포넌트를 선택한다.
2. Object Inspector에서 `Events` 탭을 연다.
3. 원하는 이벤트를 찾는다.
4. 빈 칸을 더블클릭한다.
5. Code Editor로 이동하면서 메서드가 자동 생성된다.
6. `begin ... end` 사이에 코드를 작성한다.

예:

```pascal
procedure TMainForm.btnSaveClick(Sender: TObject);
begin
  ShowMessage('저장합니다.');
end;
```

공식 문서에 따르면 Object Inspector에서 이벤트 핸들러 이름 같은 코드 관련 속성을 바꾸면 소스 코드에도 반영될 수 있다. 따라서 이벤트 이름을 바꿀 때는 신중해야 한다.

## 10. Code Editor

`Code Editor`는 Pascal 코드를 작성하는 곳이다.

처음에는 다음 동작을 익히면 된다.

| 작업 | 설명 |
| --- | --- |
| Form과 Code 전환 | 화면 디자인과 코드 파일을 오간다. |
| 선언으로 이동 | 메서드나 클래스 정의를 찾는다. |
| 검색 | 현재 파일 또는 전체 파일에서 문자열을 찾는다. |
| 자동 완성 | 클래스, 메서드, 변수 후보를 본다. |
| 오류 위치 이동 | Messages 창의 오류를 더블클릭해 이동한다. |

Code Editor에서 보게 되는 기본 구조는 대략 이렇다.

```pascal
unit Unit1;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Classes,
  Vcl.Graphics, Vcl.Controls, Vcl.Forms, Vcl.Dialogs;

type
  TForm1 = class(TForm)
    Button1: TButton;
    procedure Button1Click(Sender: TObject);
  private
  public
  end;

var
  Form1: TForm1;

implementation

{$R *.dfm}

procedure TForm1.Button1Click(Sender: TObject);
begin
  ShowMessage('Hello Delphi');
end;

end.
```

처음에는 전부 이해하지 않아도 된다. 우선 `procedure ... begin ... end;` 안에 이벤트 코드가 들어간다는 것만 잡아도 충분하다.

## 11. Structure View

`Structure View`는 현재 Form이나 코드의 구조를 보여준다.

Form을 보고 있을 때는 Form 안의 컴포넌트 계층을 보여줄 수 있고, 코드를 보고 있을 때는 unit 안의 클래스, 메서드, 필드 구조를 보여줄 수 있다.

복잡한 화면에서는 버튼이 패널 안에 있고, 패널이 탭 안에 있고, 탭이 또 다른 컨테이너 안에 있는 경우가 많다. 이때 Structure View를 보면 원하는 컴포넌트를 찾기 쉽다.

## 12. Messages 창

`Messages` 창은 빌드 결과, 오류, 경고를 보여준다.

Delphi 초보자는 화면에 빨간 줄이 없으면 오류가 없다고 생각하기 쉽다. 하지만 실제 오류는 빌드할 때 Messages 창에 나온다.

자주 보는 메시지 유형:

| 메시지 | 의미 |
| --- | --- |
| Error | 빌드 실패. 반드시 고쳐야 한다. |
| Warning | 빌드는 될 수 있지만 위험한 부분이 있다. |
| Hint | 컴파일러가 알려주는 참고 정보 |
| Fatal | 컴파일을 계속할 수 없는 큰 문제 |

Messages 창의 오류를 더블클릭하면 보통 해당 코드 위치로 이동한다. 이동하지 않는다면 경로 문제, 패키지 문제, 리소스 문제일 수 있다.

## 13. 디버깅 창

디버깅을 시작하면 아래 창들을 자주 쓰게 된다.

| 창 | 역할 |
| --- | --- |
| Watches | 내가 지정한 변수 값을 계속 본다. |
| Local Variables | 현재 메서드 안의 지역 변수를 본다. |
| Call Stack | 지금 코드가 어떤 호출 흐름으로 들어왔는지 본다. |
| Breakpoints | 설정된 브레이크포인트 목록을 본다. |
| Evaluate/Modify | 실행 중 표현식 값을 확인하거나 바꾼다. |

초기에는 `Breakpoints`, `Local Variables`, `Call Stack` 세 개만 익혀도 충분하다.

## 14. 창이 사라졌을 때

IDE를 쓰다 보면 창을 실수로 닫거나 위치가 이상해질 수 있다.

대부분의 창은 `View` 메뉴에서 다시 열 수 있다.

예:

| 창 | 보통 찾는 위치 |
| --- | --- |
| Project Manager | `View > Project Manager` |
| Object Inspector | `View > Object Inspector` |
| Tool Palette | `View > Tool Palette` |
| Structure | `View > Structure` |
| Messages | 빌드 후 자동 표시 또는 View 메뉴 |

레이아웃이 완전히 꼬였다면 IDE의 데스크톱 레이아웃 저장/복원 기능을 찾아 기본 레이아웃으로 되돌릴 수 있다. 회사에서는 선배가 쓰는 표준 레이아웃이 있을 수도 있으니 확인한다.

## 15. 처음 외울 필요 없는 것

처음부터 다음을 다 외우려 하지 않아도 된다.

- 모든 단축키
- 모든 메뉴 위치
- 모든 컴포넌트 이름
- 모든 프로젝트 옵션
- 모든 디버거 기능
- 모든 패키지 설정

대신 다음은 꼭 익힌다.

1. Form과 Code를 오가는 방법
2. Object Inspector에서 Properties와 Events를 구분하는 방법
3. Button 클릭 이벤트를 만드는 방법
4. Messages 창에서 오류를 읽는 방법
5. Project Manager에서 현재 프로젝트와 Target Platform을 확인하는 방법

## 바로 해볼 실습

1. Delphi IDE를 실행한다.
2. `File > New > VCL Forms Application - Delphi`를 선택한다.
3. `Project Manager`를 열어 프로젝트 이름을 확인한다.
4. `Tool Palette`에서 `TButton`을 찾아 Form에 올린다.
5. Button을 선택하고 `Object Inspector`에서 `Caption`을 `확인`으로 바꾼다.
6. `Events` 탭에서 `OnClick`을 더블클릭한다.
7. 생성된 코드 위치를 확인한다.
8. `Run` 버튼을 눌러 실행한다.

## 입사 후 확인할 질문

1. 회사에서 권장하는 IDE 레이아웃이 있나요?
2. Project Group을 열어야 하나요, 개별 `.dproj`를 열어야 하나요?
3. 기본 Target Platform은 Win32인가요, Win64인가요?
4. 빌드 오류는 어떤 기준으로 Warning까지 처리하나요?
5. 외부 컴포넌트가 Tool Palette에 안 보일 때 설치 문서가 있나요?

## 자주 막히는 지점

- Object Inspector의 Properties와 Events를 헷갈린다.
- `Name`을 바꿔야 할 때와 `Caption`을 바꿔야 할 때를 구분하지 못한다.
- Form Designer에서 삭제한 컴포넌트의 이벤트 코드가 남아 있는지 확인하지 않는다.
- Project Manager에서 잘못된 프로젝트를 빌드한다.
- Messages 창을 읽지 않고 "IDE가 안 된다"고 판단한다.

## 참고 자료

- [RAD Studio First Look and Welcome Page](https://docwiki.embarcadero.com/RADStudio/Athens/en/First_Look_and_Welcome_Page)
- [Form Designer - RAD Studio](https://docwiki.embarcadero.com/RADStudio/Athens/en/Form_Designer_%28IDE_Tutorial%29)
- [About the Object Inspector](https://docwiki.embarcadero.com/RADStudio/Athens/en/About_the_Object_Inspector_Index)
- [Setting Properties and Events](https://docwiki.embarcadero.com/RADStudio/Athens/en/Setting_Properties_and_Events)

