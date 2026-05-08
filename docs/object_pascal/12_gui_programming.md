# 12장. GUI 프로그래밍 기초 (Lazarus 폼 디자이너)

지금까지는 검은 콘솔 창에서만 텍스트를 출력했습니다.  
이제 버튼, 입력창, 텍스트 박스가 있는 **진짜 윈도우 프로그램**을 만들어 봅시다! 🖥️  
이것이 바로 오브젝트 파스칼(델파이/라자루스)의 가장 강력한 장점입니다.

---

## 1. GUI란?

**GUI (Graphical User Interface)**란 마우스로 클릭하고 텍스트를 입력하는 시각적인 프로그램 화면입니다.  
윈도우의 메모장, 계산기, 브라우저 같은 것들이 모두 GUI 프로그램입니다.

---

## 2. Lazarus에서 GUI 프로젝트 시작하기

### Step 1: 새 GUI 프로젝트 만들기
1. Lazarus 메뉴 → **[File] → [New...] → [Application]** 선택
2. 화면에 빈 폼(Form)이 나타납니다. 이것이 윈도우 창입니다!

### Step 2: 폼 디자이너 사용법
- **왼쪽 팔레트(Component Palette)**: 추가할 수 있는 컴포넌트 목록
- **가운데 폼**: 마우스로 컴포넌트를 올려놓을 수 있는 디자인 화면
- **오른쪽 Object Inspector**: 선택한 컴포넌트의 속성(Properties) 변경

---

## 3. 주요 컴포넌트 소개

| 컴포넌트 | 클래스 이름 | 용도 |
|---|---|---|
| 버튼 | `TButton` | 클릭 이벤트 처리 |
| 텍스트 입력 (한 줄) | `TEdit` | 사용자 텍스트 입력 |
| 텍스트 입력 (여러 줄) | `TMemo` | 여러 줄 텍스트 입력/표시 |
| 레이블 (텍스트 표시) | `TLabel` | 고정된 텍스트 표시 |
| 체크박스 | `TCheckBox` | 켜기/끄기 선택 |
| 라디오 버튼 | `TRadioButton` | 여러 개 중 하나 선택 |
| 리스트박스 | `TListBox` | 목록 표시 및 선택 |
| 콤보박스 | `TComboBox` | 드롭다운 선택 목록 |

---

## 4. 실습: 나만의 계산기 만들기 (단계별)

### Step 1: 폼에 컴포넌트 배치하기

Lazarus에서 다음 컴포넌트를 폼에 끌어다 놓습니다:

```
[ TEdit: edtNum1 ]  +  [ TEdit: edtNum2 ]  =  [ TLabel: lblResult ]
                   [ TButton: btnAdd (더하기) ]
```

1. `TEdit` 두 개를 올려놓고, 이름(Name)을 각각 `edtNum1`, `edtNum2`로 변경
2. `TButton` 하나를 올려놓고, 이름을 `btnAdd`, 캡션(Caption)을 `더하기` 로 변경
3. `TLabel` 하나를 올려놓고, 이름을 `lblResult`로 변경

> 이름 변경 방법: 컴포넌트를 클릭 → Object Inspector의 `Name` 속성 수정

### Step 2: 버튼 클릭 이벤트 작성

버튼을 **더블클릭**하면 코드 에디터에 클릭 이벤트 함수가 자동으로 생성됩니다!

```pascal
// 이 코드는 자동으로 생성됩니다. 내용만 채워주면 됩니다.
procedure TForm1.btnAddClick(Sender: TObject);
var
  Num1, Num2, Sum: Double;
begin
  try
    // edtNum1.Text, edtNum2.Text로 입력된 텍스트를 가져옵니다
    Num1 := StrToFloat(edtNum1.Text);
    Num2 := StrToFloat(edtNum2.Text);
    Sum := Num1 + Num2;

    // lblResult.Caption으로 라벨에 텍스트를 표시합니다
    lblResult.Caption := '결과: ' + FloatToStr(Sum);
  except
    on E: EConvertError do
      ShowMessage('숫자를 올바르게 입력해주세요!'); // 팝업 창 표시
  end;
end;
```

### Step 3: 실행!
`F9`를 누르면 버튼과 입력창이 있는 윈도우 프로그램이 실행됩니다! 🎉

---

## 5. 자주 쓰는 컴포넌트 속성과 메서드

### TEdit (텍스트 입력창)
```pascal
edtName.Text := '홍길동';      // 텍스트 설정
Writeln(edtName.Text);         // 텍스트 읽기
edtName.Clear;                  // 내용 지우기
edtName.SetFocus;              // 커서를 이 입력창으로 이동
```

### TLabel (텍스트 표시)
```pascal
lblResult.Caption := '안녕하세요!'; // 텍스트 설정
lblResult.Font.Size := 16;           // 폰트 크기 변경
lblResult.Font.Color := clRed;       // 텍스트 색상 변경 (빨간색)
lblResult.Visible := False;          // 숨기기
```

### TMemo (여러 줄 텍스트)
```pascal
memOutput.Lines.Add('새 줄 추가');  // 한 줄 추가
memOutput.Lines.Clear;               // 모든 내용 지우기
Writeln(memOutput.Text);             // 전체 내용 읽기
```

### TButton (버튼)
```pascal
btnOK.Enabled := False;  // 버튼 비활성화 (회색)
btnOK.Caption := '확인'; // 버튼 글자 변경
```

---

## 6. ShowMessage와 MessageDlg (팝업 창)

```pascal
uses Dialogs; // 메시지 창을 사용하려면 필요합니다

// 단순 알림 창
ShowMessage('저장이 완료되었습니다!');

// 예/아니오 선택 창
if MessageDlg('정말 삭제하시겠습니까?', mtConfirmation, [mbYes, mbNo], 0) = mrYes then
begin
  Writeln('삭제를 선택했습니다.');
end
else
begin
  Writeln('취소했습니다.');
end;
```

---

## 7. 폼의 이벤트 종류

컴포넌트에는 클릭 외에도 다양한 이벤트가 있습니다:

| 이벤트 | 언제 발생하나? |
|---|---|
| `OnClick` | 마우스 클릭 시 |
| `OnChange` | 텍스트 입력이 바뀔 때 |
| `OnKeyPress` | 키보드 키를 누를 때 |
| `OnCreate` | 폼이 처음 생성될 때 |
| `OnClose` | 폼이 닫힐 때 |
| `OnMouseMove` | 마우스가 움직일 때 |

> Object Inspector의 **Events(이벤트)** 탭에서 원하는 이벤트를 더블클릭하면 자동으로 코드가 생성됩니다!
