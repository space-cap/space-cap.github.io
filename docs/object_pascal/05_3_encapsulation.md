# 5-3장. 캡슐화와 접근 제어 (private, property)

캡슐화는 OOP의 4대 특징 중 하나로, **데이터를 보호**하고 **안전하게 접근**하도록 만드는 기술입니다.  
이 장에서는 파스칼의 접근 제어자와 `property`(프로퍼티) 문법을 마스터해 봅시다.

---

## 1. 왜 캡슐화가 필요할까?

먼저 캡슐화가 **없을 때**의 문제점을 봅시다.

```pascal
type
  TBankAccount = class
  public
    Balance: Integer; // 잔액이 외부에서 직접 변경 가능! 위험합니다 💣
  end;

var
  Account: TBankAccount;
begin
  Account := TBankAccount.Create;
  try
    Account.Balance := 1000000; // 정상
    Account.Balance := -99999999; // 잔액이 음수가 되어도 아무도 막지 않음! 😱
  finally
    Account.Free;
  end;
end.
```

잔액이 마음대로 음수가 되어버렸습니다!  
캡슐화를 통해 이런 **잘못된 접근을 원천 차단**할 수 있습니다.

---

## 2. 접근 제어자 (Access Modifiers)

파스칼은 4가지 접근 제어자를 제공합니다.

| 접근 제어자 | 범위 | 설명 |
|---|---|---|
| `public` | 어디서든 접근 가능 | 외부에 공개된 인터페이스 |
| `private` | 같은 유닛 내에서만 접근 가능 | 외부에 완전히 숨겨진 내부 구현 |
| `protected` | 같은 유닛 + 자식 클래스에서 접근 가능 | 상속을 위해 자식에게만 공개 |
| `published` | public과 같지만 RTTI(런타임 타입 정보) 노출 | Lazarus/Delphi IDE의 컴포넌트에서 사용 |

### 실무에서 자주 쓰는 패턴

```pascal
type
  TPerson = class
  private
    // private: 외부에서 직접 접근 불가!
    // 관례적으로 F(Field의 F)로 시작합니다.
    FName: String;
    FAge: Integer;
    
  protected
    // protected: 자식 클래스는 접근 가능
    FId: Integer;
    
  public
    // public: 누구나 접근 가능 (외부 인터페이스)
    constructor Create(const AName: String; AAge: Integer);
    
    // 데이터에 접근하는 안전한 통로 역할의 메서드
    function GetName: String;
    procedure SetAge(AAge: Integer);
    function GetAge: Integer;
  end;

constructor TPerson.Create(const AName: String; AAge: Integer);
begin
  inherited Create;
  FName := AName;
  SetAge(AAge); // 메서드를 통해 유효성 검사와 함께 설정
end;

function TPerson.GetName: String;
begin
  Result := FName;
end;

procedure TPerson.SetAge(AAge: Integer);
begin
  // private 필드에 값을 넣기 전에 유효성 검사!
  if AAge < 0 then
    raise Exception.Create('나이는 0 이상이어야 합니다.')
  else if AAge > 150 then
    raise Exception.Create('나이가 너무 큽니다.')
  else
    FAge := AAge;
end;

function TPerson.GetAge: Integer;
begin
  Result := FAge;
end;
```

```pascal
var
  P: TPerson;
begin
  P := TPerson.Create('홍길동', 25);
  try
    Writeln(P.GetName); // 'GetName'을 통해서만 접근 가능

    P.SetAge(30);       // 메서드를 통해 안전하게 변경
    // P.FAge := -5;    // ← 컴파일 에러! private이라 접근 불가

    try
      P.SetAge(-1);     // 유효성 검사에 걸려서 예외 발생
    except
      on E: Exception do Writeln('에러: ', E.Message);
    end;
  finally
    P.Free;
  end;
  Readln;
end.
```

---

## 3. Property (프로퍼티) — 파스칼의 꽃! 🌸

`GetAge`, `SetAge` 같은 메서드를 매번 호출하는 것은 조금 불편합니다.  
파스칼의 **`property`** 는 이 두 가지를 하나로 합쳐서 **마치 일반 변수처럼 사용**할 수 있게 해줍니다!

```pascal
type
  TPerson = class
  private
    FName: String;
    FAge: Integer;
    
    // Getter: 값을 읽을 때 호출되는 함수
    function GetName: String;
    
    // Setter: 값을 쓸 때 호출되는 프로시저
    procedure SetAge(AAge: Integer);
    
  public
    constructor Create(const AName: String; AAge: Integer);
    
    // property 선언: 외부에서는 변수처럼 사용합니다
    property Name: String read GetName;         // 읽기 전용 (Setter 없음)
    property Age: Integer read FAge write SetAge; // 읽기/쓰기 모두 가능
    // 'read FAge'는 getter 함수 없이 필드를 직접 읽는 단축 문법입니다
  end;

constructor TPerson.Create(const AName: String; AAge: Integer);
begin
  inherited Create;
  FName := AName;
  FAge := AAge;
end;

function TPerson.GetName: String;
begin
  Result := FName; // 필요하면 여기서 가공해서 반환할 수 있습니다
end;

procedure TPerson.SetAge(AAge: Integer);
begin
  if (AAge >= 0) and (AAge <= 150) then
    FAge := AAge
  else
    raise Exception.CreateFmt('유효하지 않은 나이입니다: %d', [AAge]);
end;
```

### 프로퍼티 사용하기 (일반 변수처럼!)

```pascal
var
  P: TPerson;
begin
  P := TPerson.Create('김영희', 20);
  try
    // 일반 변수처럼 읽고 씁니다!
    Writeln('이름: ', P.Name);  // 실제로는 GetName()이 호출됩니다
    Writeln('나이: ', P.Age);   // 실제로는 FAge 필드를 직접 읽습니다

    P.Age := 25;                // 실제로는 SetAge(25)가 호출됩니다 (유효성 검사 포함!)
    Writeln('변경된 나이: ', P.Age);

    // P.Name := '박민수'; // 컴파일 에러! Name은 읽기 전용 property 입니다.
    
    try
      P.Age := -5; // SetAge(-5) 호출 → 예외 발생!
    except
      on E: Exception do
        Writeln('에러: ', E.Message);
    end;
  finally
    P.Free;
  end;
  Readln;
end.
```

---

## 4. 프로퍼티 문법 다양한 패턴

```pascal
type
  TExample = class
  private
    FValue: Integer;
    FReadOnly: String;
    FItems: TStringList;
    
    function GetValue: Integer;
    procedure SetValue(AValue: Integer);
    function GetItemCount: Integer;
    
  public
    // ① 읽기/쓰기 (가장 일반적)
    property Value: Integer read GetValue write SetValue;
    
    // ② 읽기 전용 (외부에서 값 변경 불가)
    property ReadOnly: String read FReadOnly;
    
    // ③ getter/setter 없이 필드 직접 접근 (간단한 경우)
    property ItemCount: Integer read GetItemCount; // 계산이 필요할 때는 함수 사용
    
    // ④ 인덱스 프로퍼티 (배열처럼 사용!)
    // property Items[Index: Integer]: String read GetItem write SetItem;
  end;
```

---

## 5. 완전한 예제: 안전한 은행 계좌 클래스

앞서 문제가 있었던 은행 계좌를 캡슐화로 완벽하게 만들어 봅시다!

```pascal
uses SysUtils;

type
  TBankAccount = class
  private
    FOwner: String;
    FBalance: Int64;      // 잔액 (private으로 보호!)
    FTransactionLog: TStringList; // 거래 내역
    
    function GetBalance: Int64;
    
  public
    constructor Create(const AOwner: String; InitialDeposit: Int64);
    destructor Destroy; override;
    
    procedure Deposit(Amount: Int64);    // 입금
    procedure Withdraw(Amount: Int64);   // 출금
    procedure PrintLog;                  // 거래 내역 출력
    
    property Owner: String read FOwner;  // 읽기 전용
    property Balance: Int64 read GetBalance; // 직접 쓰기 불가!
  end;

constructor TBankAccount.Create(const AOwner: String; InitialDeposit: Int64);
begin
  inherited Create;
  FOwner := AOwner;
  FTransactionLog := TStringList.Create;
  Deposit(InitialDeposit); // 초기 입금
end;

destructor TBankAccount.Destroy;
begin
  FTransactionLog.Free;
  inherited Destroy;
end;

function TBankAccount.GetBalance: Int64;
begin
  Result := FBalance;
end;

procedure TBankAccount.Deposit(Amount: Int64);
begin
  if Amount <= 0 then
    raise Exception.Create('입금액은 0보다 커야 합니다.');
  FBalance := FBalance + Amount;
  FTransactionLog.Add(Format('입금: +%d원 (잔액: %d원)', [Amount, FBalance]));
end;

procedure TBankAccount.Withdraw(Amount: Int64);
begin
  if Amount <= 0 then
    raise Exception.Create('출금액은 0보다 커야 합니다.');
  if Amount > FBalance then
    raise Exception.Create('잔액이 부족합니다.');
  FBalance := FBalance - Amount;
  FTransactionLog.Add(Format('출금: -%d원 (잔액: %d원)', [Amount, FBalance]));
end;

procedure TBankAccount.PrintLog;
var
  i: Integer;
begin
  Writeln('=== ', FOwner, '님의 거래 내역 ===');
  for i := 0 to FTransactionLog.Count - 1 do
    Writeln(FTransactionLog[i]);
  Writeln('현재 잔액: ', FBalance, '원');
end;

// --- 메인 프로그램 ---
var
  Account: TBankAccount;
begin
  Account := TBankAccount.Create('홍길동', 100000);
  try
    Account.Deposit(50000);
    Account.Withdraw(30000);
    
    // Account.FBalance := -999999; // 컴파일 에러! private 접근 불가
    // Account.Balance := 999999;   // 컴파일 에러! 읽기 전용 property

    try
      Account.Withdraw(999999); // 잔액 부족 예외 발생!
    except
      on E: Exception do
        Writeln('[거절] ', E.Message);
    end;

    Account.PrintLog;
  finally
    Account.Free;
  end;
  Readln;
end.
```

---

## 6. OOP 5장 전체 핵심 요약

| 개념 | 키워드 | 역할 |
|---|---|---|
| 클래스 선언 | `type ... = class` | 설계도 만들기 |
| 생성자 | `constructor Create` | 객체 초기화 |
| 소멸자 | `destructor Destroy; override` | 객체 뒷정리 |
| 자기 참조 | `Self` | 자기 자신을 가리킴 |
| 외부 공개 | `public` | 누구나 접근 가능 |
| 내부 숨김 | `private` | 클래스 내부만 접근 |
| 상속용 공개 | `protected` | 자식 클래스만 접근 |
| 스마트 접근 | `property` | 안전한 읽기/쓰기 통로 |

---

> 📌 **다음 단계**: 6장에서는 배열과 컬렉션을,  
> 10장에서는 OOP의 나머지 핵심인 **상속과 다형성**을 깊이 배웁니다!
