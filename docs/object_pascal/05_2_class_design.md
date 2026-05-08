# 5-2장. 클래스 설계 심화 (생성자, 소멸자, Self)

5-1장에서 클래스와 객체의 기본 개념을 배웠습니다.  
이번 장에서는 클래스를 **더 전문적으로 설계하는 방법**을 배웁니다.  
생성자(Constructor), 소멸자(Destructor), 그리고 `Self` 키워드가 핵심입니다.

---

## 1. 생성자 (Constructor) — 객체 탄생의 순간

`Create`를 호출할 때 자동으로 실행되는 특별한 메서드입니다.  
객체가 만들어지는 순간 초기 설정을 자동으로 해줍니다.

### 기본 생성자 vs 커스텀 생성자

```pascal
type
  TPerson = class
  public
    Name: String;
    Age: Integer;
    
    // 기본 생성자 재정의: override 사용
    constructor Create; overload;
    
    // 매개변수가 있는 커스텀 생성자: overload로 여러 버전 만들기
    constructor Create(const AName: String; AAge: Integer); overload;
  end;

constructor TPerson.Create;
begin
  inherited Create; // 부모(TObject)의 Create를 먼저 호출합니다. 항상 첫 줄에!
  Name := '이름 없음'; // 기본값 설정
  Age := 0;
  Writeln('TPerson 객체가 생성되었습니다.');
end;

constructor TPerson.Create(const AName: String; AAge: Integer);
begin
  inherited Create; // 부모 생성자 호출은 항상!
  Name := AName;
  Age := AAge;
  Writeln(AName, ' 객체가 생성되었습니다.');
end;
```

> 💡 **`inherited Create`를 왜 써야 할까요?**  
> 부모 클래스(TObject)도 내부적으로 초기화해야 할 것들이 있습니다.  
> 이것을 빠뜨리면 나중에 알 수 없는 에러가 발생할 수 있습니다!

### 커스텀 생성자 사용하기

```pascal
var
  Person1, Person2: TPerson;
begin
  // 기본 생성자 호출
  Person1 := TPerson.Create;
  
  // 매개변수 있는 생성자 호출 (훨씬 편리합니다!)
  Person2 := TPerson.Create('홍길동', 25);

  try
    Writeln(Person1.Name, ' / ', Person1.Age, '세');  // 이름 없음 / 0세
    Writeln(Person2.Name, ' / ', Person2.Age, '세');  // 홍길동 / 25세
  finally
    Person2.Free;
    Person1.Free;
  end;
  Readln;
end.
```

---

## 2. 소멸자 (Destructor) — 객체가 사라지는 순간

`Free`를 호출할 때 자동으로 실행되는 특별한 메서드입니다.  
객체가 사라지기 전에 **뒷정리 작업**을 합니다.

### 소멸자가 필요한 경우

클래스 안에 다른 객체를 생성해서 들고 있을 때, 소멸자에서 함께 해제해야 합니다.

```pascal
uses Classes;

type
  TSchool = class
  private
    FStudentList: TStringList; // TSchool 안에서 TStringList 객체를 가집니다
  public
    Name: String;
    
    constructor Create(const AName: String);
    destructor Destroy; override; // 소멸자는 항상 override 합니다
    
    procedure AddStudent(const StudentName: String);
    procedure PrintStudents;
  end;

constructor TSchool.Create(const AName: String);
begin
  inherited Create;
  Name := AName;
  FStudentList := TStringList.Create; // 내부 객체도 여기서 생성
end;

destructor TSchool.Destroy;
begin
  FStudentList.Free; // 내부 객체를 먼저 해제!
  inherited Destroy; // 부모 소멸자 호출은 마지막에!
  Writeln(Name, ' 학교 객체가 메모리에서 해제되었습니다.');
end;

procedure TSchool.AddStudent(const StudentName: String);
begin
  FStudentList.Add(StudentName);
end;

procedure TSchool.PrintStudents;
var
  i: Integer;
begin
  Writeln('=== ', Name, ' 학생 목록 ===');
  for i := 0 to FStudentList.Count - 1 do
    Writeln(' - ', FStudentList[i]);
end;
```

> ⚠️ **주의!** 생성자에서는 `inherited`를 **첫 줄**에,  
> 소멸자에서는 `inherited`를 **마지막 줄**에 호출합니다!

### 소멸자 사용 패턴

```pascal
var
  MySchool: TSchool;
begin
  MySchool := TSchool.Create('한빛고등학교');
  try
    MySchool.AddStudent('홍길동');
    MySchool.AddStudent('김철수');
    MySchool.AddStudent('이영희');
    MySchool.PrintStudents;
  finally
    MySchool.Free; // 여기서 Destroy가 자동으로 호출됩니다!
    // FStudentList.Free도 Destroy 안에서 자동으로 처리됩니다.
  end;
  Readln;
end.
```

---

## 3. Self — 나 자신을 가리키는 키워드

`Self`는 클래스의 메서드 안에서 **자기 자신(현재 객체)**을 가리키는 특별한 변수입니다.

### Self가 필요한 상황

매개변수 이름이 속성 이름과 겹칠 때 명확하게 구분해 줍니다.

```pascal
type
  TRectangle = class
  public
    Width: Integer;
    Height: Integer;
    
    constructor Create(Width, Height: Integer); // 매개변수 이름이 속성과 같습니다!
    function GetArea: Integer;
    procedure PrintInfo;
  end;

constructor TRectangle.Create(Width, Height: Integer);
begin
  inherited Create;
  // 'Self.Width'는 클래스의 속성, 그냥 'Width'는 매개변수
  Self.Width := Width;   // ← Self 없이는 어떤 Width인지 헷갈립니다
  Self.Height := Height;
end;

function TRectangle.GetArea: Integer;
begin
  // 메서드 안에서는 Self를 생략해도 됩니다 (속성 이름이 겹치지 않을 때)
  Result := Width * Height; // Self.Width * Self.Height 와 동일
end;

procedure TRectangle.PrintInfo;
begin
  // Self를 사용해서 자기 자신을 다른 함수에 넘길 수도 있습니다
  Writeln('이 도형의 클래스: ', Self.ClassName);
  Writeln('넓이: ', Self.GetArea);
end;
```

---

## 4. 클래스 메서드와 클래스 변수 (class var / class function)

일반 속성과 메서드는 객체마다 따로 존재하지만,  
`class` 키워드를 붙이면 **모든 객체가 공유**하는 속성/메서드가 됩니다.

```pascal
type
  TCounter = class
  private
    class var FCount: Integer; // 모든 TCounter 객체가 공유하는 변수!
  public
    Name: String;
    
    constructor Create(const AName: String);
    destructor Destroy; override;
    
    class function GetCount: Integer; // 객체 없이도 호출 가능!
  end;

constructor TCounter.Create(const AName: String);
begin
  inherited Create;
  Name := AName;
  Inc(FCount); // 객체가 생성될 때마다 카운트 증가
end;

destructor TCounter.Destroy;
begin
  Dec(FCount); // 객체가 해제될 때마다 카운트 감소
  inherited Destroy;
end;

class function TCounter.GetCount: Integer;
begin
  Result := FCount;
end;

var
  A, B, C: TCounter;
begin
  // 객체를 만들기 전에도 클래스 메서드 호출 가능!
  Writeln('현재 객체 수: ', TCounter.GetCount); // 0

  A := TCounter.Create('A');
  B := TCounter.Create('B');
  Writeln('현재 객체 수: ', TCounter.GetCount); // 2

  C := TCounter.Create('C');
  Writeln('현재 객체 수: ', TCounter.GetCount); // 3

  try
    A.Free; A := nil;
    Writeln('현재 객체 수: ', TCounter.GetCount); // 2
  finally
    if Assigned(B) then B.Free;
    if Assigned(C) then C.Free;
  end;
  Readln;
end.
```

> 💡 **`Assigned(변수)`**: 객체 변수가 nil(비어있음)인지 확인하는 함수입니다.  
> Free 후에 `변수 := nil`을 습관화하면 실수로 이중 해제하는 것을 방지할 수 있습니다.

---

## 5. 생성자와 소멸자 규칙 정리

| 규칙 | 생성자 (Constructor) | 소멸자 (Destructor) |
|---|---|---|
| **이름** | `Create` (관례) | `Destroy` (고정) |
| **`inherited` 위치** | **첫 번째 줄** | **마지막 줄** |
| **`override` 필요** | 선택적 | **항상 필요** |
| **호출 방법** | `클래스명.Create` | `객체.Free` |
| **자동 호출** | Create 시 | Free 시 |

---

> 📌 **다음 장 예고**: 5-3장에서는 **캡슐화와 접근 제어**를 배웁니다.  
> `private`, `protected`, `public`, 그리고 파스칼의 강력한 `property` 문법을 마스터해 봅시다!
