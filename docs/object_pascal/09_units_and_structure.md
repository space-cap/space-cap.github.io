# 9장. 유닛(Unit)과 프로그램 구조

코드가 길어지면 하나의 파일에 모든 것을 넣는 것은 매우 불편합니다.  
파스칼은 **유닛(Unit)**이라는 개념을 통해 코드를 여러 파일로 나누어 관리할 수 있습니다.  
이것이 오브젝트 파스칼만의 아주 중요한 특징입니다!

---

## 1. 유닛이란?

유닛은 관련된 코드(변수, 함수, 클래스 등)를 하나의 파일로 묶어놓은 것입니다.  
`.pas` 확장자를 가지며, 마치 레고 블록처럼 필요한 유닛만 골라서 조립해 사용합니다.

델파이/파스칼에는 이미 수십 개의 유용한 **내장 유닛**이 제공됩니다:

| 유닛 이름 | 포함된 기능 |
|---|---|
| `SysUtils` | 날짜, 문자열 변환, 파일 관련 함수 |
| `Classes` | TStringList, TStream 등 기본 클래스 |
| `Math` | 수학 함수 (Sin, Cos, Sqrt 등) |
| `StrUtils` | 추가적인 문자열 처리 함수 |
| `DateUtils` | 날짜/시간 계산 함수 |

---

## 2. uses 절 — 유닛 가져오기

다른 유닛의 기능을 쓰려면 `uses` 키워드로 가져와야 합니다.

```pascal
program MyProgram;

// 사용할 유닛들을 여기에 나열합니다
uses
  SysUtils,  // StrToInt, FloatToStr 등을 사용하기 위해
  Classes,   // TStringList를 사용하기 위해
  Math;      // Sqrt, Power 등을 사용하기 위해

begin
  Writeln(Sqrt(16));          // 4 (Math 유닛의 함수)
  Writeln(IntToStr(42));      // '42' (SysUtils 유닛의 함수)
  Readln;
end.
```

---

## 3. 나만의 유닛 만들기

이제 나만의 유닛을 직접 만들어 봅시다!  
예를 들어, 수학 계산 관련 함수들을 `MathHelper.pas` 파일로 따로 만들어 두면 여러 프로젝트에서 재사용할 수 있습니다.

### Step 1: 유닛 파일 만들기 (MathHelper.pas)

```pascal
unit MathHelper;  // 파일 이름과 unit 이름을 일치시키는 것이 관례입니다

interface
// ===== interface 섹션 =====
// 외부에서 사용할 수 있는 함수/타입을 여기에 선언(목차)합니다.
// 이 함수들이 "공개된" 것들입니다.

function Add(A, B: Integer): Integer;
function Subtract(A, B: Integer): Integer;
function IsEven(N: Integer): Boolean;

implementation
// ===== implementation 섹션 =====
// 위에서 선언한 함수들의 실제 내용(구현)을 여기에 작성합니다.

function Add(A, B: Integer): Integer;
begin
  Result := A + B;
end;

function Subtract(A, B: Integer): Integer;
begin
  Result := A - B;
end;

function IsEven(N: Integer): Boolean;
begin
  Result := (N mod 2) = 0; // mod는 나머지 연산입니다
end;

end.  // 유닛의 끝 (마침표 필수!)
```

### Step 2: 유닛 사용하기 (Main.pas)

```pascal
program Main;

uses
  MathHelper; // 내가 만든 유닛을 가져옵니다

begin
  Writeln('10 + 5 = ', Add(10, 5));
  Writeln('10 - 5 = ', Subtract(10, 5));

  if IsEven(42) then
    Writeln('42는 짝수입니다.')
  else
    Writeln('42는 홀수입니다.');

  Readln;
end.
```

---

## 4. 유닛의 구조 한눈에 보기

```
unit 유닛이름;

interface        ← "메뉴판" (무엇이 있는지 목록)
  uses ...       ← 이 유닛에서 사용하는 다른 유닛들
  type ...       ← 공개할 타입(클래스 등) 선언
  var ...        ← 공개할 전역 변수 선언
  function ...   ← 공개할 함수/프로시저 선언 (내용 없이 이름만)

implementation   ← "주방" (실제로 어떻게 만드는지)
  function ...   ← 위에서 선언한 함수의 실제 내용

initialization   ← (선택) 유닛이 처음 로드될 때 실행할 코드
finalization     ← (선택) 프로그램 종료 시 실행할 코드

end.             ← 유닛의 끝
```

---

## 5. 파스칼 프로그램의 전체 파일 구조

실제 델파이/라자루스 프로젝트는 여러 파일로 구성됩니다:

```
MyProject/
├── MyProject.lpr (또는 .dpr)   ← 프로그램 진입점 (main 파일)
├── MainForm.pas                 ← 메인 화면 유닛
├── MainForm.lfm (또는 .dfm)    ← 화면 디자인 파일 (자동 생성)
├── MathHelper.pas               ← 내가 만든 유틸 유닛
└── MyProject.lpi (또는 .dproj) ← 프로젝트 설정 파일 (자동 생성)
```

> 💡 **핵심 포인트: `.lpr`과 `.pas`의 역할 차이**
> - **`.lpr` (Lazarus Program)**: 프로그램이 처음 시작되는 **단 하나의 진입점(Entry Point)**입니다. 오케스트라의 지휘자처럼 프로그램을 시작하고 메인 화면을 띄우는 역할을 합니다. 아무리 규모가 큰 프로젝트라도 `.lpr` 파일은 무조건 **1개**만 존재합니다.
> - **`.pas` (Pascal Source)**: 프로그램의 각 기능을 담당하는 여러 개의 유닛(Unit)입니다. 오케스트라의 악기 연주자들처럼 각자의 역할을 수행합니다.
> 
> (대부분의 코딩은 `.pas` 파일에서 직접 하며, `.lpr`, `.lfm`, `.lpi` 파일 등은 주로 IDE가 자동으로 관리해 줍니다.)
