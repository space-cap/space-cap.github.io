# Object Pascal 최소 문법

> 작성 기준일: 2026-05-19  
> 대상 독자: 다른 언어 경험은 있지만 Delphi/Object Pascal은 처음인 개발자  
> 목표: 기존 Delphi 코드를 읽고 작은 수정을 할 수 있을 만큼의 최소 문법을 익힌다.

## 1. 처음부터 문법 전체를 외우지 않는다

Object Pascal은 Delphi의 주 언어다. 문법이 C#, Java, JavaScript와 다르게 보이지만, 실무 유지보수에 필요한 첫 문법은 많지 않다.

처음 한 달의 목표는 다음 정도면 충분하다.

1. unit 구조를 읽는다.
2. Form class와 이벤트 핸들러를 구분한다.
3. 변수와 타입 선언을 읽는다.
4. `if`, `case`, `for`, `while` 흐름을 읽는다.
5. `try..except`, `try..finally`를 구분한다.
6. 문자열, 숫자, 날짜, Boolean 값을 다룬다.
7. `uses`를 보고 어떤 unit에 의존하는지 안다.

Delphi 코드는 문법보다 구조가 먼저다.

```text
unit
-> uses
-> type
-> class
-> component field
-> event handler
-> 업무 로직
```

이 순서로 보면 낯선 코드도 훨씬 읽기 쉬워진다.

## 2. unit 기본 구조

Delphi의 `.pas` 파일은 보통 unit이다.

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
    procedure ValidateInput;
  public
    procedure LoadData;
  end;

var
  MainForm: TMainForm;

implementation

{$R *.dfm}

procedure TMainForm.btnSaveClick(Sender: TObject);
begin
  ValidateInput;
  ShowMessage('저장합니다.');
end;

procedure TMainForm.ValidateInput;
begin
  if Trim(btnSave.Caption) = '' then
    raise Exception.Create('Caption이 비어 있습니다.');
end;

procedure TMainForm.LoadData;
begin
end;

end.
```

중요한 구역은 다음과 같다.

| 구역 | 의미 |
| --- | --- |
| `unit` | unit 이름. 보통 파일명과 맞춘다. |
| `interface` | 외부에서 볼 수 있는 선언 영역 |
| `uses` | 참조하는 unit 목록 |
| `type` | 클래스, 레코드, 타입 선언 |
| `private` | 클래스 내부에서만 쓰는 멤버 |
| `public` | 외부에서도 호출 가능한 멤버 |
| `implementation` | 실제 구현 코드 |
| `{$R *.dfm}` | Form 디자인 파일 연결 |
| `end.` | unit 끝. 마지막은 점이다. |

공식 문서 기준으로 Embarcadero 도구에서 unit 식별자는 unit 파일 이름과 일치해야 한다. 파일명만 바꾸고 unit 이름을 안 바꾸면 문제가 생길 수 있다.

## 3. `interface`와 `implementation`

`interface`는 다른 unit에서 볼 수 있는 계약서에 가깝다. `implementation`은 실제 구현이다.

```pascal
interface

type
  TCustomerService = class
  public
    procedure SaveCustomer;
  end;

implementation

procedure TCustomerService.SaveCustomer;
begin
  // 실제 저장 로직
end;
```

처음 코드를 읽을 때는 `interface`에서 이 unit이 무엇을 제공하는지 보고, `implementation`에서 실제 동작을 따라간다.

## 4. `uses` 절

`uses`는 다른 unit을 참조하는 부분이다. 공식 문서에 따르면 `uses` 절은 project file, unit의 `interface`, unit의 `implementation`에 올 수 있다.

```pascal
uses
  System.SysUtils,
  Vcl.Forms,
  CustomerRepository;
```

다른 언어와 비교하면 다음과 비슷하다.

| 언어 | 비슷한 개념 |
| --- | --- |
| Java | `import` |
| C# | `using` |
| JavaScript | `import` |

다만 Delphi에서는 위치가 중요하다.

| 위치 | 의미 |
| --- | --- |
| `interface uses` | 공개 선언에 필요한 의존성 |
| `implementation uses` | 내부 구현에만 필요한 의존성 |

순환 참조가 생기면 unit을 서로 참조하다가 컴파일 오류가 날 수 있다. 이때는 `implementation uses`로 옮기거나 구조를 분리한다.

## 5. 변수와 타입 선언

Delphi는 변수명 뒤에 타입을 적는다.

```pascal
var
  CustomerName: string;
  Quantity: Integer;
  Price: Currency;
  IsActive: Boolean;
```

자주 보는 기본 타입:

| 타입 | 의미 |
| --- | --- |
| `string` | 문자열 |
| `Integer` | 정수 |
| `Int64` | 큰 정수 |
| `Double` | 실수 |
| `Currency` | 금액 계산에 자주 사용 |
| `Boolean` | 참/거짓 |
| `TDateTime` | 날짜와 시간 |
| `Variant` | 여러 타입을 담을 수 있는 값. 남용 주의 |

상수는 `const`로 선언한다.

```pascal
const
  MaxRetryCount = 3;
  DefaultMessage = '처리되었습니다.';
```

## 6. 대입과 비교

Delphi에서 대입은 `:=`이고, 비교는 `=`다.

```pascal
CustomerName := '홍길동';

if CustomerName = '홍길동' then
  ShowMessage('같습니다.');
```

초보자가 가장 많이 하는 실수:

```pascal
CustomerName = '홍길동'; // 잘못된 대입
```

올바른 코드:

```pascal
CustomerName := '홍길동';
```

이 차이 하나만 빨리 몸에 넣어도 컴파일 오류가 많이 줄어든다.

## 7. 문자열

문자열은 작은따옴표를 사용한다.

```pascal
ShowMessage('안녕하세요.');
```

문자열 연결은 `+`를 쓴다.

```pascal
Message := '안녕하세요, ' + CustomerName + '님.';
```

작은따옴표 자체를 문자열에 넣고 싶으면 두 번 쓴다.

```pascal
Message := 'It''s Delphi.';
```

자주 쓰는 문자열 함수:

| 함수 | 의미 |
| --- | --- |
| `Trim` | 앞뒤 공백 제거 |
| `UpperCase` | 대문자 변환 |
| `LowerCase` | 소문자 변환 |
| `Copy` | 일부 문자열 추출 |
| `Pos` | 문자열 위치 찾기 |
| `StringReplace` | 문자열 치환 |
| `Format` | 형식 문자열 생성 |

예:

```pascal
if Trim(edtName.Text) = '' then
  ShowMessage('이름을 입력하세요.');
```

## 8. procedure와 function

`procedure`는 값을 반환하지 않는다. `function`은 값을 반환한다.

```pascal
procedure SaveCustomer;
begin
  // 저장만 한다.
end;

function GetCustomerName: string;
begin
  Result := '홍길동';
end;
```

Delphi function의 반환값은 보통 `Result`에 넣는다.

```pascal
function Add(A, B: Integer): Integer;
begin
  Result := A + B;
end;
```

파라미터 예:

```pascal
procedure SetCustomerName(const AName: string);
begin
  CustomerName := AName;
end;
```

`const`는 파라미터 값을 바꾸지 않겠다는 의미로 자주 쓰인다. 문자열이나 객체 참조를 불필요하게 복사하지 않는 효과도 있다.

## 9. class와 object

Delphi 클래스는 이런 식으로 선언한다.

```pascal
type
  TCustomer = class
  private
    FName: string;
  public
    procedure SetName(const AName: string);
    function GetName: string;
  end;
```

구현은 `implementation` 영역에 쓴다.

```pascal
procedure TCustomer.SetName(const AName: string);
begin
  FName := AName;
end;

function TCustomer.GetName: string;
begin
  Result := FName;
end;
```

Delphi 관례:

| 관례 | 의미 |
| --- | --- |
| `TCustomer` | 클래스 타입 이름. 보통 `T`로 시작 |
| `FName` | 필드. 보통 `F`로 시작 |
| `AName` | 인자. 보통 `A`로 시작하는 팀도 있음 |

회사의 네이밍 규칙이 있으면 그 규칙을 따른다.

## 10. property

property는 필드에 접근하는 공개 인터페이스다.

```pascal
type
  TCustomer = class
  private
    FName: string;
  public
    property Name: string read FName write FName;
  end;
```

사용:

```pascal
Customer.Name := '홍길동';
ShowMessage(Customer.Name);
```

VCL 컴포넌트의 `Caption`, `Text`, `Enabled`, `Visible`도 property다.

```pascal
btnSave.Caption := '저장';
edtName.Text := '';
btnSave.Enabled := True;
```

## 11. 이벤트 핸들러

Delphi VCL 프로그램에서 가장 자주 보는 코드는 이벤트 핸들러다.

```pascal
procedure TMainForm.btnSaveClick(Sender: TObject);
begin
  ShowMessage('저장 버튼 클릭');
end;
```

읽는 법:

| 부분 | 의미 |
| --- | --- |
| `procedure` | 반환값 없는 메서드 |
| `TMainForm` | 이 메서드가 속한 클래스 |
| `btnSaveClick` | 메서드 이름 |
| `Sender: TObject` | 이벤트를 발생시킨 객체 |
| `begin ... end` | 실행 코드 |

`Sender`를 활용하면 하나의 이벤트 핸들러를 여러 버튼이 공유할 수도 있다.

```pascal
procedure TMainForm.ButtonClick(Sender: TObject);
begin
  if Sender = btnSave then
    ShowMessage('저장')
  else if Sender = btnDelete then
    ShowMessage('삭제');
end;
```

하지만 처음에는 버튼마다 별도 이벤트를 쓰는 편이 읽기 쉽다.

## 12. if 문

기본 형태:

```pascal
if Quantity > 0 then
  ShowMessage('수량이 있습니다.');
```

여러 줄이면 `begin/end`를 쓴다.

```pascal
if Quantity <= 0 then
begin
  ShowMessage('수량을 입력하세요.');
  Exit;
end;
```

`else`:

```pascal
if IsActive then
  StatusText := '사용'
else
  StatusText := '미사용';
```

주의: `else` 바로 앞의 `end`에는 세미콜론을 붙이지 않는다.

```pascal
if IsActive then
begin
  StatusText := '사용';
end
else
begin
  StatusText := '미사용';
end;
```

## 13. case 문

여러 값을 나눌 때 `case`를 쓴다.

```pascal
case OrderStatus of
  0: StatusText := '대기';
  1: StatusText := '처리중';
  2: StatusText := '완료';
else
  StatusText := '알 수 없음';
end;
```

업무 코드에서는 상태 코드, 구분 코드, 유형 코드 처리에 자주 나온다.

## 14. 반복문

`for`:

```pascal
for I := 0 to List.Count - 1 do
begin
  ProcessItem(List[I]);
end;
```

`while`:

```pascal
while not Query.Eof do
begin
  ProcessRow;
  Query.Next;
end;
```

DB 코드에서는 `while not Query.Eof do` 패턴을 자주 만난다.

## 15. 배열과 리스트

동적 배열:

```pascal
var
  Names: array of string;
begin
  SetLength(Names, 2);
  Names[0] := '홍길동';
  Names[1] := '김철수';
end;
```

현대 Delphi 코드에서는 제네릭 리스트도 자주 쓴다.

```pascal
uses
  System.Generics.Collections;

var
  Names: TList<string>;
begin
  Names := TList<string>.Create;
  try
    Names.Add('홍길동');
  finally
    Names.Free;
  end;
end;
```

객체를 `Create`했다면 대부분 `Free`가 필요하다. 소유권이 누구에게 있는지 확인하는 습관이 중요하다.

## 16. try..except와 try..finally

`try..except`는 예외를 처리한다.

```pascal
try
  SaveCustomer;
except
  on E: Exception do
    ShowMessage(E.Message);
end;
```

`try..finally`는 예외가 나든 안 나든 정리 코드를 실행한다.

```pascal
Query := TFDQuery.Create(nil);
try
  Query.Connection := Connection;
  Query.Open('select * from customer');
finally
  Query.Free;
end;
```

차이:

| 구문 | 목적 |
| --- | --- |
| `try..except` | 오류를 잡고 처리 |
| `try..finally` | 자원 정리 보장 |

실무에서는 DB 트랜잭션, 파일, 객체 생성/해제에서 `try..finally`가 매우 중요하다.

## 17. nil과 Assigned

Delphi에서 객체 참조가 없다는 값은 `nil`이다.

```pascal
if Customer = nil then
  Exit;
```

`Assigned`도 자주 쓰인다.

```pascal
if Assigned(Customer) then
  Customer.Save;
```

둘은 비슷하게 쓰이지만 팀 스타일을 따른다.

객체를 `Free`한 뒤에는 필요하면 `nil`을 대입한다.

```pascal
Customer.Free;
Customer := nil;
```

또는 `FreeAndNil`을 쓴다.

```pascal
FreeAndNil(Customer);
```

## 18. 날짜와 시간

Delphi에서 날짜와 시간은 보통 `TDateTime`을 쓴다.

```pascal
var
  Today: TDateTime;
begin
  Today := Now;
  ShowMessage(DateToStr(Today));
end;
```

자주 쓰는 함수:

| 함수 | 의미 |
| --- | --- |
| `Now` | 현재 날짜와 시간 |
| `Date` | 현재 날짜 |
| `Time` | 현재 시간 |
| `DateToStr` | 날짜를 문자열로 |
| `FormatDateTime` | 지정한 형식으로 변환 |
| `StrToDate` | 문자열을 날짜로 |

예:

```pascal
FormatDateTime('yyyy-mm-dd', Now);
```

## 19. DB 코드에서 자주 보는 패턴

Delphi 업무 프로그램에서는 DB 코드가 자주 나온다.

```pascal
Query.Close;
Query.SQL.Text := 'select * from customer where customer_id = :customer_id';
Query.ParamByName('customer_id').AsString := CustomerId;
Query.Open;
```

읽는 법:

| 코드 | 의미 |
| --- | --- |
| `Close` | 기존 결과 닫기 |
| `SQL.Text` | 실행할 SQL 지정 |
| `:customer_id` | SQL 파라미터 |
| `ParamByName` | 파라미터 값 대입 |
| `Open` | 조회 실행 |

수정/삭제/저장은 `ExecSQL`을 쓸 수 있다.

```pascal
Query.SQL.Text := 'update customer set name = :name where customer_id = :customer_id';
Query.ParamByName('name').AsString := CustomerName;
Query.ParamByName('customer_id').AsString := CustomerId;
Query.ExecSQL;
```

운영 DB에 연결된 코드는 절대 가볍게 실행하지 않는다.

## 20. C#/Java/JavaScript 개발자가 특히 조심할 차이

| 익숙한 습관 | Delphi에서 주의할 점 |
| --- | --- |
| `{}` 블록 | `begin/end` 사용 |
| `=` 대입 | `:=` 대입 |
| `==` 비교 | `=` 비교 |
| `null` | `nil` |
| `try/finally` 자원 정리 | Delphi 객체는 `Free` 필요 |
| `import` | `uses` |
| 자동 메모리 관리 기대 | 일반 객체는 소유권 확인 필요 |
| 파일명 자유 변경 | unit 이름과 파일명 일치 중요 |

## 바로 해볼 실습

1. 새 VCL 프로젝트에서 Button을 하나 만든다.
2. `OnClick` 이벤트에 `ShowMessage`를 작성한다.
3. `if Trim(Edit1.Text) = '' then` 조건을 추가한다.
4. `function BuildMessage(const AName: string): string;`을 만들어 메시지 생성을 분리한다.
5. `try..except`로 오류 메시지를 표시해본다.

## 입사 후 확인할 질문

1. 회사 Delphi 코딩 컨벤션이 있나요?
2. Form 이벤트 안에 업무 로직을 직접 쓰나요, 서비스/모듈로 분리하나요?
3. 예외 처리는 공통 함수가 있나요?
4. 객체 생성/해제 규칙이나 메모리 누수 확인 도구를 쓰나요?
5. DB Query 컴포넌트는 FireDAC, ADO, dbExpress 중 무엇을 쓰나요?

## 자주 막히는 지점

- `:=`와 `=`를 헷갈린다.
- `else` 앞의 `end;` 때문에 문법 오류가 난다.
- 객체를 `Create`하고 `Free`하지 않는다.
- `.pas` 파일명과 `unit` 이름을 다르게 바꾼다.
- `interface uses`에 너무 많은 unit을 넣어 순환 참조가 생긴다.
- `try..except`와 `try..finally` 목적을 섞어 쓴다.

## 참고 자료

- [Programs and Units - Delphi](https://docwiki.embarcadero.com/RADStudio/Athens/en/Programs_and_Units_%28Delphi%29)
- [Classes and Objects - Delphi](https://docwiki.embarcadero.com/RADStudio/Athens/en/Classes_and_Objects_Index)
- [Methods - Delphi](https://docwiki.embarcadero.com/RADStudio/Athens/en/Methods_%28Delphi%29)
- [Properties, Methods, and Events](https://docwiki.embarcadero.com/RADStudio/Athens/en/Properties%2C_Methods%2C_and_Events)

