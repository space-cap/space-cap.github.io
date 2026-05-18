# 첫 VCL 프로그램 만들기

> 작성 기준일: 2026-05-19  
> 대상 독자: Delphi IDE에서 처음으로 화면 프로그램을 만들어보는 사람  
> 목표: VCL Forms Application을 만들고, 버튼 이벤트를 작성하고, 실행 파일이 만들어지는 흐름을 경험한다.

## 1. 이번 문서에서 만들 프로그램

아주 작은 프로그램을 만든다.

화면 구성:

- 이름 입력 칸
- 인사말 표시 라벨
- 확인 버튼
- 초기화 버튼

동작:

1. 이름을 입력한다.
2. `확인` 버튼을 누른다.
3. 라벨에 `안녕하세요, 홍길동님.` 같은 문장을 표시한다.
4. `초기화` 버튼을 누르면 입력값과 라벨을 비운다.

작지만 Delphi의 기본 흐름이 모두 들어 있다.

```text
새 프로젝트 생성
-> Form에 컴포넌트 배치
-> Object Inspector에서 속성 변경
-> 이벤트 핸들러 생성
-> Pascal 코드 작성
-> 실행/확인
```

## 2. VCL Forms Application이란?

`VCL Forms Application`은 Windows 네이티브 GUI 프로그램을 만드는 기본 프로젝트 유형이다.

공식 문서에서는 VCL Forms Application이 VCL, 즉 native Windows application의 틀을 만들고, 기본 Form을 Form Designer에 표시한다고 설명한다.

업무용 Delphi 프로그램을 처음 배운다면 VCL Forms Application으로 시작하는 것이 좋다. ERP, MES, 회계, 물류, 생산관리 같은 Windows 업무 프로그램에서 VCL을 만날 가능성이 높기 때문이다.

## 3. 새 프로젝트 만들기

IDE에서 다음 메뉴를 선택한다.

```text
File > New > VCL Forms Application - Delphi
```

버전이나 IDE 레이아웃에 따라 `File > New > Other` 안에서 찾아야 할 수도 있다.

프로젝트가 생성되면 보통 다음이 생긴다.

- 빈 Form
- 기본 unit
- 기본 프로젝트 파일
- Form Designer
- Code Editor에서 볼 수 있는 Pascal 코드

처음 만들어지는 이름은 `Project1`, `Unit1`, `Form1`처럼 의미 없는 이름일 수 있다. 실무에서는 이름을 바꾸는 것이 좋지만, 첫 실습에서는 구조를 먼저 익히기 위해 기본 이름으로 시작해도 된다.

## 4. 프로젝트 저장하기

새 프로젝트를 만든 뒤 바로 저장한다.

추천 폴더 예:

```text
C:\work\delphi-practice\hello-vcl\
```

저장할 때 이름을 이렇게 정한다.

| 항목 | 예 |
| --- | --- |
| Project | `HelloVcl` |
| Unit | `MainFormUnit` |
| Form Class | `TMainForm` |
| Form Variable | `MainForm` |

처음에는 IDE가 파일 이름과 unit 이름을 어떻게 묻는지 관찰한다. 회사 프로젝트에서는 이름 규칙이 있을 수 있으니 그 규칙을 따른다.

## 5. Form 속성 바꾸기

Form Designer에서 빈 Form을 클릭한 뒤 Object Inspector에서 속성을 바꾼다.

| 속성 | 값 |
| --- | --- |
| `Name` | `MainForm` |
| `Caption` | `첫 VCL 프로그램` |
| `Width` | `420` |
| `Height` | `220` |
| `Position` | `poScreenCenter` |

중요한 구분:

```text
Name    = 코드에서 쓰는 이름
Caption = 화면에 보이는 제목
```

`Caption`은 자유롭게 바꿔도 되지만, `Name`은 코드와 연결된다. 처음에는 Object Inspector가 자동으로 코드를 바꾸는 것을 관찰하면서 천천히 바꾼다.

## 6. 컴포넌트 배치하기

Tool Palette에서 다음 컴포넌트를 찾아 Form 위에 올린다.

| 컴포넌트 | Name | 주요 속성 |
| --- | --- | --- |
| `TLabel` | `lblName` | `Caption = 이름` |
| `TEdit` | `edtName` | `Text = 빈 값` |
| `TButton` | `btnHello` | `Caption = 확인` |
| `TButton` | `btnClear` | `Caption = 초기화` |
| `TLabel` | `lblMessage` | `Caption = 빈 값` |

배치는 대략 이렇게 잡는다.

```text
이름 [              ] [확인] [초기화]

안녕하세요 메시지가 표시될 영역
```

정확한 픽셀 위치는 중요하지 않다. 지금은 컴포넌트를 올리고, 선택하고, 속성을 바꾸는 감각을 익히는 것이 목표다.

## 7. 입력 칸 초기화

`TEdit`를 선택하고 Object Inspector에서 `Text`를 비운다.

처음 생성된 Edit는 `Edit1` 같은 텍스트를 가지고 있을 수 있다. 그대로 두면 실행했을 때 입력 칸에 `Edit1`이 보인다.

`lblMessage`도 `Caption`을 비운다. 빈 값 설정이 어렵다면 공백 한 칸으로 두어도 실습에는 문제 없다.

## 8. 확인 버튼 이벤트 만들기

`btnHello` 버튼을 선택한다.

1. Object Inspector에서 `Events` 탭을 연다.
2. `OnClick` 항목을 찾는다.
3. 빈 칸을 더블클릭한다.
4. Code Editor로 이동한다.

IDE가 다음과 비슷한 코드를 만든다.

```pascal
procedure TMainForm.btnHelloClick(Sender: TObject);
begin

end;
```

이제 `begin`과 `end` 사이에 코드를 작성한다.

```pascal
procedure TMainForm.btnHelloClick(Sender: TObject);
begin
  if Trim(edtName.Text) = '' then
  begin
    ShowMessage('이름을 입력하세요.');
    edtName.SetFocus;
    Exit;
  end;

  lblMessage.Caption := '안녕하세요, ' + Trim(edtName.Text) + '님.';
end;
```

처음 보는 코드가 있어도 괜찮다.

| 코드 | 의미 |
| --- | --- |
| `Trim` | 앞뒤 공백 제거 |
| `edtName.Text` | 입력 칸의 텍스트 |
| `ShowMessage` | 메시지 창 표시 |
| `SetFocus` | 입력 포커스 이동 |
| `Exit` | 현재 메서드 종료 |
| `lblMessage.Caption := ...` | 라벨에 글자 표시 |

Delphi에서 대입은 `:=`를 쓴다. 비교는 `=`를 쓴다. 이 차이는 초반에 꼭 익혀둔다.

## 9. 초기화 버튼 이벤트 만들기

`btnClear` 버튼을 선택하고 `OnClick` 이벤트를 만든다.

```pascal
procedure TMainForm.btnClearClick(Sender: TObject);
begin
  edtName.Clear;
  lblMessage.Caption := '';
  edtName.SetFocus;
end;
```

이 코드는 입력 칸을 비우고, 메시지 라벨을 비우고, 다시 이름 입력 칸으로 포커스를 옮긴다.

작은 코드지만 업무 프로그램에서 아주 자주 나오는 흐름이다.

```text
입력값 확인
-> 메시지 표시
-> 화면 값 변경
-> 포커스 이동
```

## 10. 실행하기

상단의 Run 버튼을 누르거나 단축키를 사용해 실행한다. 기본 단축키는 보통 `F9`다.

실행 후 확인할 것:

1. 프로그램 창 제목이 `첫 VCL 프로그램`으로 보이는가?
2. 이름을 비우고 확인을 누르면 메시지가 뜨는가?
3. 이름을 입력하고 확인을 누르면 라벨이 바뀌는가?
4. 초기화를 누르면 입력 칸과 라벨이 비워지는가?

실행 중인 프로그램을 닫고 다시 IDE로 돌아온다.

## 11. 빌드 오류가 날 때

처음 실습에서 가장 흔한 오류는 오타다.

예:

```pascal
lblMessage.Caption = '안녕하세요';
```

이 코드는 오류다. Delphi에서 값을 넣을 때는 `:=`를 사용한다.

올바른 코드:

```pascal
lblMessage.Caption := '안녕하세요';
```

또 다른 흔한 오류:

```pascal
ShowMessage('이름을 입력하세요.);
```

문자열 닫는 따옴표가 없다.

올바른 코드:

```pascal
ShowMessage('이름을 입력하세요.');
```

오류가 나면 아래 `Messages` 창을 본다. 오류 줄을 더블클릭하면 대개 해당 코드로 이동한다.

## 12. 실행 파일 위치 확인

프로젝트를 빌드하면 실행 파일이 생성된다. 위치는 프로젝트 설정과 Build Configuration에 따라 다르다.

흔히 다음과 비슷한 폴더에 생긴다.

```text
Win32\Debug\
Win32\Release\
Win64\Debug\
Win64\Release\
```

정확한 위치는 `Project > Options`의 output 설정에서 확인한다.

입사 후에는 실행 파일 위치를 꼭 확인해야 한다. 개발자가 빌드한 파일과 실제 고객/운영 환경에 배포되는 파일이 다를 수 있기 때문이다.

## 13. Form과 Code 오가기

실습 중 자주 해야 하는 일은 Form Designer와 Code Editor를 오가는 것이다.

방법은 IDE 버전에 따라 조금씩 다를 수 있지만 보통 다음 방식이 있다.

- Form 탭 클릭
- Unit 코드 탭 클릭
- Project Manager에서 Form/unit 더블클릭
- View 메뉴에서 Form/Code 관련 항목 선택

중요한 것은 `.pas`와 `.dfm`이 연결되어 있다는 감각이다. 화면을 바꾸면 `.dfm`, 이벤트 코드를 바꾸면 `.pas`가 바뀐다.

## 14. 저장하기

작업 후 `File > Save All`을 실행한다.

Delphi에서는 프로젝트, unit, Form 디자인 파일이 함께 바뀌므로 `Save`보다 `Save All`을 쓰는 습관이 좋다.

Git을 쓴다면 변경 파일을 확인한다.

예상 변경:

```text
HelloVcl.dpr
HelloVcl.dproj
MainFormUnit.pas
MainFormUnit.dfm
```

처음 실습에서는 어떤 행동이 어떤 파일을 바꾸는지 관찰하는 것이 매우 좋은 공부다.

## 15. 완성 코드 예시

unit 전체는 환경에 따라 uses 목록이 조금 다를 수 있다. 핵심 이벤트 코드는 다음과 같다.

```pascal
procedure TMainForm.btnHelloClick(Sender: TObject);
begin
  if Trim(edtName.Text) = '' then
  begin
    ShowMessage('이름을 입력하세요.');
    edtName.SetFocus;
    Exit;
  end;

  lblMessage.Caption := '안녕하세요, ' + Trim(edtName.Text) + '님.';
end;

procedure TMainForm.btnClearClick(Sender: TObject);
begin
  edtName.Clear;
  lblMessage.Caption := '';
  edtName.SetFocus;
end;
```

## 16. 이 작은 예제가 실무와 연결되는 지점

실무의 저장 버튼도 구조는 비슷하다.

```text
버튼 클릭
-> 입력값 검증
-> 사용자 메시지
-> DB 저장
-> 화면 갱신
-> 포커스 이동
```

이번 실습은 DB 저장만 빠진 아주 작은 업무 화면이다. 여기서 DB 쿼리와 트랜잭션이 붙으면 실제 업무 프로그램의 기본 모양이 된다.

## 바로 해볼 실습

1. `HelloVcl` 프로젝트를 만든다.
2. Form의 `Caption`을 바꾼다.
3. `TEdit`, `TButton`, `TLabel`을 배치한다.
4. 확인 버튼 이벤트를 작성한다.
5. 초기화 버튼 이벤트를 작성한다.
6. 이름을 비웠을 때와 입력했을 때를 모두 테스트한다.
7. 실행 파일이 생성된 폴더를 확인한다.
8. 변경된 `.pas`와 `.dfm` 파일을 비교해본다.

## 입사 후 확인할 질문

1. 회사에서는 컴포넌트 이름 규칙이 있나요? 예: `btnSave`, `edtName`
2. 화면 문구는 코드에 직접 쓰나요, 리소스/상수로 관리하나요?
3. 입력값 검증은 Form에서 하나요, 공통 함수나 서비스에서 하나요?
4. 메시지 창은 `ShowMessage`를 쓰나요, 회사 공통 메시지 함수를 쓰나요?
5. 저장 버튼 이벤트에 DB 코드를 직접 쓰나요, 별도 모듈로 분리하나요?

## 자주 막히는 지점

- `Caption`과 `Text`를 헷갈린다.
- `Name`을 바꾼 뒤 이벤트 코드 이름이 바뀌어 당황한다.
- `:=` 대신 `=`를 써서 컴파일 오류가 난다.
- 문자열 따옴표를 닫지 않는다.
- 버튼을 더블클릭해 원하지 않는 이벤트를 만든다.
- 실행 중인 프로그램을 닫지 않고 다시 빌드하려 한다.

## 참고 자료

- [VCL Forms Application](https://docwiki.embarcadero.com/RADStudio/Sydney/en/VCL_Forms_Application)
- [Form Designer - RAD Studio](https://docwiki.embarcadero.com/RADStudio/Athens/en/Form_Designer_%28IDE_Tutorial%29)
- [Setting Properties and Events](https://docwiki.embarcadero.com/RADStudio/Athens/en/Setting_Properties_and_Events)

