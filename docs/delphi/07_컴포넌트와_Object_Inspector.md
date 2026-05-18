# 컴포넌트와 Object Inspector

> 작성 기준일: 2026-05-19  
> 대상 독자: Delphi Form Designer에서 컴포넌트를 처음 다루는 사람  
> 목표: 컴포넌트, 속성, 이벤트, Object Inspector의 관계를 실무 관점에서 이해한다.

## 1. Delphi 화면 개발의 기본 단위는 컴포넌트

Delphi VCL 화면은 컴포넌트를 조립해서 만든다.

버튼도 컴포넌트고, 입력창도 컴포넌트고, DB 조회용 Query도 컴포넌트다. 화면에 보이는 것만 컴포넌트가 아니다.

공식 문서 기준으로 Delphi 컴포넌트는 `TComponent`에서 내려온다. IDE의 Tool Palette에는 많은 컴포넌트가 제공되고, 개발자는 이를 Form에 올려 속성과 이벤트를 설정한다.

처음에는 이렇게 이해하면 된다.

```text
컴포넌트 = IDE에서 배치하고 속성을 설정할 수 있는 객체
```

## 2. Visual Component와 Non-Visual Component

컴포넌트는 크게 두 종류로 나눌 수 있다.

| 구분 | 설명 | 예 |
| --- | --- | --- |
| Visual Component | 실행 시 화면에 보인다. | `TButton`, `TEdit`, `TLabel`, `TDBGrid` |
| Non-Visual Component | 실행 시 화면에는 안 보이지만 기능을 제공한다. | `TTimer`, `TOpenDialog`, `TFDQuery`, `TDataSource` |

Form Designer에서 Non-Visual Component는 보통 Form 위나 아래에 작은 아이콘처럼 보인다. 실행하면 사용자는 그 아이콘을 보지 못한다.

업무 시스템에서는 Non-Visual Component가 매우 중요하다. DB 연결, 쿼리, 타이머, 파일 대화상자, 프린터 설정 같은 기능이 여기에 숨어 있다.

## 3. Object Inspector의 역할

`Object Inspector`는 선택한 객체의 속성과 이벤트를 보여주는 창이다.

공식 문서에서는 Object Inspector가 선택된 객체의 properties와 events의 이름과 현재 값을 표시하며, 시각적 모양과 실행 코드를 연결하는 역할을 한다고 설명한다.

Object Inspector에서 할 수 있는 일:

- Form과 컴포넌트의 design-time property 설정
- 이벤트 핸들러 생성
- 기존 이벤트 핸들러로 이동
- 속성과 이벤트 필터링
- 선택 객체의 빠른 작업 수행

처음에는 이 문장 하나를 기억한다.

> Object Inspector는 화면과 코드를 연결하는 조종석이다.

## 4. Properties 탭

`Properties` 탭은 컴포넌트의 상태와 모양을 설정한다.

자주 보는 속성:

| 속성 | 의미 | 예 |
| --- | --- | --- |
| `Name` | 코드에서 부르는 이름 | `btnSave` |
| `Caption` | 사용자에게 보이는 글자 | `저장` |
| `Text` | 입력/표시 텍스트 | `홍길동` |
| `Enabled` | 사용 가능 여부 | `True`, `False` |
| `Visible` | 화면 표시 여부 | `True`, `False` |
| `Left`, `Top` | 위치 | 숫자 |
| `Width`, `Height` | 크기 | 숫자 |
| `Align` | 부모 영역 기준 자동 배치 | `alTop`, `alClient` |
| `Anchors` | 창 크기 변경 시 고정 기준 | `[akLeft, akTop]` |
| `TabOrder` | Tab 키 이동 순서 | `0`, `1`, `2` |
| `Font` | 글꼴 | 폰트 설정 |
| `Color` | 배경색 | 색상 |

실무에서 가장 먼저 익힐 속성은 `Name`, `Caption`, `Text`, `Enabled`, `Visible`, `Align`, `Anchors`, `TabOrder`다.

## 5. `Name`, `Caption`, `Text` 구분

초보자가 가장 자주 헷갈리는 세 속성이다.

| 속성 | 의미 | 예 |
| --- | --- | --- |
| `Name` | 코드에서 사용하는 식별자 | `btnSave` |
| `Caption` | 버튼/라벨/Form 제목처럼 화면에 보이는 문구 | `저장` |
| `Text` | Edit 같은 입력 컴포넌트의 내용 | `홍길동` |

예:

```pascal
btnSave.Caption := '저장';
edtName.Text := '홍길동';
```

`Name`은 코드와 연결된다.

```pascal
btnSave.Enabled := False;
```

따라서 `Name`을 바꾸면 코드 참조도 함께 영향을 받을 수 있다. Object Inspector에서 `Name`을 바꾸면 IDE가 관련 코드를 바꿔주기도 하지만, 기존 대형 프로젝트에서는 수동 확인이 필요하다.

## 6. Events 탭

`Events` 탭은 컴포넌트에서 발생하는 사건과 코드를 연결한다.

대표 이벤트:

| 이벤트 | 발생 시점 |
| --- | --- |
| `OnClick` | 클릭했을 때 |
| `OnDblClick` | 더블클릭했을 때 |
| `OnChange` | 값이 바뀌었을 때 |
| `OnEnter` | 포커스가 들어왔을 때 |
| `OnExit` | 포커스가 빠져나갈 때 |
| `OnKeyDown` | 키를 눌렀을 때 |
| `OnCreate` | Form이 생성될 때 |
| `OnShow` | Form이 표시될 때 |
| `OnClose` | Form이 닫힐 때 |

이벤트 핸들러 생성 흐름:

1. 컴포넌트를 선택한다.
2. Object Inspector에서 `Events` 탭을 연다.
3. 이벤트 이름을 찾는다.
4. 빈 칸을 더블클릭한다.
5. IDE가 `.pas`에 메서드를 만든다.

예:

```pascal
procedure TMainForm.btnSaveClick(Sender: TObject);
begin
  SaveData;
end;
```

## 7. 이벤트 연결 정보는 `.dfm`에도 들어간다

버튼의 `OnClick`에 `btnSaveClick`을 연결하면 `.dfm`에도 연결 정보가 저장된다.

```text
object btnSave: TButton
  Caption = '저장'
  OnClick = btnSaveClick
end
```

그리고 `.pas`에는 실제 코드가 있다.

```pascal
procedure TMainForm.btnSaveClick(Sender: TObject);
begin
  SaveData;
end;
```

따라서 이벤트 관련 변경은 `.dfm`과 `.pas`를 함께 봐야 한다.

자주 생기는 문제:

- `.dfm`에는 이벤트가 연결되어 있는데 `.pas`에 메서드가 없다.
- `.pas`에는 메서드가 있는데 `.dfm` 연결이 끊겼다.
- 컴포넌트를 복사하면서 이벤트가 원래 메서드에 연결된 채 남았다.
- 버튼 이름은 바꿨는데 이벤트 메서드 이름은 예전 이름이다.

## 8. Align과 Anchors

업무 프로그램은 창 크기가 바뀌어도 화면이 망가지지 않아야 한다. 이때 중요한 속성이 `Align`과 `Anchors`다.

`Align`은 부모 영역 안에서 컴포넌트를 어느 쪽에 붙일지 정한다.

| 값 | 의미 |
| --- | --- |
| `alNone` | 자동 배치 없음 |
| `alTop` | 위쪽에 붙음 |
| `alBottom` | 아래쪽에 붙음 |
| `alLeft` | 왼쪽에 붙음 |
| `alRight` | 오른쪽에 붙음 |
| `alClient` | 남은 영역을 채움 |

`Anchors`는 창 크기가 바뀔 때 어느 가장자리를 기준으로 고정할지 정한다.

예:

| Anchors | 동작 |
| --- | --- |
| `akLeft`, `akTop` | 왼쪽 위 기준 고정 |
| `akLeft`, `akTop`, `akRight` | 창이 넓어지면 가로로 늘어남 |
| `akLeft`, `akTop`, `akRight`, `akBottom` | 가로/세로 모두 늘어남 |

그리드나 메모처럼 넓어져야 하는 컴포넌트는 Anchors 또는 Align을 제대로 설정해야 한다.

## 9. TabOrder

`TabOrder`는 사용자가 Tab 키를 눌렀을 때 포커스가 이동하는 순서다.

입력 화면에서 TabOrder가 엉망이면 사용자가 불편하다.

예:

```text
거래처명 -> 전화번호 -> 주소 -> 저장 버튼
```

이런 순서로 이동해야 하는데, 화면 배치를 나중에 바꾸면 TabOrder가 이상해질 수 있다.

실무 화면 수정 후에는 Tab 키 이동을 직접 테스트한다.

## 10. 컴포넌트 이름 짓기

회사마다 규칙이 다르지만, VCL에서는 접두어를 쓰는 팀이 많다.

| 컴포넌트 | 예 |
| --- | --- |
| Button | `btnSave`, `btnClose` |
| Label | `lblName`, `lblStatus` |
| Edit | `edtName`, `edtPhone` |
| ComboBox | `cboCustomerType` |
| CheckBox | `chkUseYn` |
| Panel | `pnlSearch`, `pnlBody` |
| Grid | `grdCustomer` |
| Query | `qryCustomer` |
| DataSource | `dsCustomer` |

중요한 것은 접두어 자체가 아니라 일관성이다. 기존 프로젝트가 `ButtonSave` 스타일이면 그 스타일을 따른다.

## 11. 자주 쓰는 VCL 컴포넌트

처음 업무 프로그램에서 자주 보는 컴포넌트:

| 컴포넌트 | 역할 |
| --- | --- |
| `TForm` | 창/화면 |
| `TPanel` | 화면 영역 묶기 |
| `TLabel` | 텍스트 표시 |
| `TEdit` | 한 줄 입력 |
| `TMemo` | 여러 줄 입력 |
| `TButton` | 버튼 |
| `TComboBox` | 선택 목록 |
| `TCheckBox` | 체크박스 |
| `TRadioButton` | 라디오 버튼 |
| `TDateTimePicker` | 날짜/시간 선택 |
| `TPageControl` | 탭 화면 |
| `TTabSheet` | 탭 페이지 |
| `TGroupBox` | 관련 입력 묶기 |
| `TStringGrid` | 문자열 표 |
| `TDBGrid` | DB 데이터 표 |
| `TMainMenu` | 메뉴 |
| `TPopupMenu` | 우클릭 메뉴 |
| `TTimer` | 주기적 실행 |

## 12. DB 관련 컴포넌트

DB 프로그램에서는 다음 컴포넌트를 자주 만난다.

| 컴포넌트 | 역할 |
| --- | --- |
| `TFDConnection` | FireDAC DB 연결 |
| `TFDQuery` | SQL 실행 |
| `TDataSource` | Dataset과 UI 컴포넌트 연결 |
| `TDBGrid` | DB 데이터를 표로 표시 |
| `TDBEdit` | DB 필드와 연결된 입력창 |
| `TClientDataSet` | 메모리 기반 데이터셋 |

회사에 따라 ADO, dbExpress, BDE, 자체 컴포넌트를 쓸 수도 있다. 오래된 프로젝트에서는 지금은 권장되지 않는 컴포넌트도 남아 있을 수 있다. 함부로 교체하지 않는다.

## 13. 컴포넌트를 삭제할 때

컴포넌트를 삭제하면 `.dfm`에서는 사라진다. 그러나 연결된 이벤트 메서드가 `.pas`에 남을 수 있다.

삭제 후 확인할 것:

1. `.dfm`에서 컴포넌트가 사라졌는가?
2. `.pas`의 컴포넌트 필드가 사라졌는가?
3. 이벤트 핸들러 메서드가 남아 있지는 않은가?
4. 다른 코드에서 해당 컴포넌트를 참조하지 않는가?
5. 실행 중 오류가 나지 않는가?

컴포넌트 하나 삭제가 생각보다 큰 변경일 수 있다. 특히 DB 연결 컴포넌트, 메뉴, 공통 패널은 영향 범위를 확인해야 한다.

## 14. 컴포넌트를 복사할 때

컴포넌트를 복사하면 속성뿐 아니라 이벤트 연결도 함께 복사될 수 있다.

예를 들어 `btnSave`를 복사해서 `btnDelete`를 만들었는데 `OnClick`이 여전히 `btnSaveClick`에 연결되어 있으면 삭제 버튼을 눌렀을 때 저장 로직이 실행될 수 있다.

복사 후 확인할 것:

| 확인 항목 | 이유 |
| --- | --- |
| `Name` | 중복/의미 없는 이름 방지 |
| `Caption` | 화면 문구 확인 |
| `OnClick` | 잘못된 이벤트 연결 방지 |
| `TabOrder` | 입력 흐름 확인 |
| `Anchors/Align` | 화면 크기 변경 대응 |

## 15. Object Inspector 필터 사용

Object Inspector에는 속성/이벤트를 필터링하는 기능이 있다. 큰 컴포넌트는 속성이 너무 많기 때문에 필터를 쓰면 빠르다.

예:

- `caption` 입력
- `onclick` 입력
- `align` 입력
- `visible` 입력

필터 사용에 익숙해지면 메뉴를 찾아 헤매는 시간이 줄어든다.

## 16. 실무 화면을 읽는 순서

기존 Form을 처음 열었다면 이렇게 본다.

1. Form의 `Name`, `Caption`을 본다.
2. 화면을 큰 영역으로 나눈다. 상단 검색, 중앙 그리드, 하단 버튼 등.
3. 주요 버튼의 `Name`, `Caption`, `OnClick`을 확인한다.
4. 검색 입력창의 `Name`, `OnKeyDown`, `OnChange`를 확인한다.
5. Grid와 DataSource 연결을 확인한다.
6. Query 또는 DataModule 연결을 찾는다.
7. 이벤트 코드에서 실제 업무 흐름을 따라간다.

화면을 볼 때는 "어떤 컴포넌트가 어떤 이벤트로 어떤 코드를 실행하는가"를 계속 묻는다.

## 바로 해볼 실습

1. 새 VCL Form에 `TEdit`, `TButton`, `TLabel`을 올린다.
2. 각 컴포넌트의 `Name`, `Caption`, `Text`를 의미 있게 바꾼다.
3. Button의 `OnClick` 이벤트를 만든다.
4. Button을 복사해 다른 버튼을 만들고 이벤트 연결을 확인한다.
5. Form 크기를 바꿔보고 Anchors 설정 차이를 확인한다.
6. Tab 키를 눌러 TabOrder를 확인한다.

## 입사 후 확인할 질문

1. 회사 컴포넌트 이름 규칙이 있나요?
2. 화면 레이아웃은 Align 중심인가요, Anchors 중심인가요?
3. 공통 버튼 패널이나 공통 검색 패널이 있나요?
4. DB 연결 컴포넌트는 Form에 두나요, DataModule에 두나요?
5. 외부 UI 컴포넌트 라이브러리를 사용하나요?

## 자주 막히는 지점

- `Name`, `Caption`, `Text`를 헷갈린다.
- 복사한 버튼의 이벤트 연결을 확인하지 않는다.
- `TabOrder`를 수정하지 않아 입력 흐름이 불편해진다.
- `Align`과 `Anchors`를 동시에 어색하게 설정해 화면이 깨진다.
- Non-Visual Component를 실행 화면에도 보인다고 착각한다.
- `.dfm` 변경을 코드 변경보다 가볍게 본다.

## 참고 자료

- [Working with Components - RAD Studio](https://docwiki.embarcadero.com/RADStudio/Athens/en/Working_with_Components_Index)
- [About the Object Inspector](https://docwiki.embarcadero.com/RADStudio/Athens/en/About_the_Object_Inspector)
- [Setting Properties and Events](https://docwiki.embarcadero.com/RADStudio/Athens/en/Setting_Properties_and_Events)
- [Properties, Methods, and Events](https://docwiki.embarcadero.com/RADStudio/Athens/en/Properties%2C_Methods%2C_and_Events)

