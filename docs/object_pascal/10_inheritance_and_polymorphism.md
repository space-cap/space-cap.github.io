# 10장. 상속과 다형성 (OOP 심화)

5장에서 클래스와 객체의 기초를 배웠습니다.  
이번 장에서는 객체 지향 프로그래밍(OOP)의 진짜 강력한 기능인 **상속(Inheritance)**과 **다형성(Polymorphism)**을 배워봅니다.  
이 개념들을 이해하면 코드를 훨씬 효율적으로 만들 수 있습니다!

---

## 1. 상속이란? (부모 → 자식 클래스)

상속은 이미 만들어진 클래스를 바탕으로 새로운 클래스를 만드는 것입니다.  
마치 부모에게서 특성을 물려받는 것과 같습니다!

- **부모 클래스 (Parent / Base Class)**: 공통 기능을 가진 클래스
- **자식 클래스 (Child / Derived Class)**: 부모의 모든 기능을 물려받고, 추가 기능을 더한 클래스

```pascal
type
  // ① 부모 클래스: 동물
  TAnimal = class
  public
    Name: String;
    procedure Eat;
    procedure Sleep;
  end;

  // ② 자식 클래스: 개 (TAnimal의 모든 기능을 물려받음)
  TDog = class(TAnimal)  // ← 괄호 안에 부모 클래스 이름을 씁니다
  public
    Breed: String; // 개만 가진 추가 속성: 품종
    procedure Bark; // 개만 할 수 있는 추가 행동: 짖기
  end;

  // ③ 자식 클래스: 고양이
  TCat = class(TAnimal)
  public
    procedure Purr; // 고양이만 할 수 있는 행동: 그루밍
  end;

procedure TAnimal.Eat;
begin
  Writeln(Name, '가(이) 밥을 먹습니다.');
end;

procedure TAnimal.Sleep;
begin
  Writeln(Name, '가(이) 잠을 잡니다.');
end;

procedure TDog.Bark;
begin
  Writeln(Name, '가(이) 짖습니다: 멍멍!');
end;

procedure TCat.Purr;
begin
  Writeln(Name, '가(이) 가르릉거립니다.');
end;

var
  Dog: TDog;
  Cat: TCat;
begin
  Dog := TDog.Create;
  Cat := TCat.Create;
  try
    Dog.Name := '바둑이';
    Dog.Breed := '진돗개';
    Dog.Eat;   // 부모(TAnimal)에게서 물려받은 기능!
    Dog.Sleep; // 부모(TAnimal)에게서 물려받은 기능!
    Dog.Bark;  // 자기만의 기능

    Cat.Name := '나비';
    Cat.Eat;   // 부모에게서 물려받은 기능!
    Cat.Purr;  // 자기만의 기능
  finally
    Dog.Free;
    Cat.Free;
  end;
  Readln;
end.
```

---

## 2. 메서드 오버라이딩 (부모 기능을 자식이 바꾸기)

부모에게서 물려받은 기능이지만, 자식 클래스에서 다르게 동작하도록 바꾸고 싶을 때 사용합니다.  
부모 메서드에 `virtual`을, 자식 메서드에 `override`를 붙입니다.

```pascal
type
  TShape = class
  public
    // virtual: 자식 클래스에서 바꿀 수 있다고 표시
    function GetArea: Double; virtual;
  end;

  TCircle = class(TShape)
  public
    Radius: Double;
    // override: 부모의 GetArea를 나만의 방식으로 덮어씁니다
    function GetArea: Double; override;
  end;

  TRectangle = class(TShape)
  public
    Width, Height: Double;
    function GetArea: Double; override;
  end;

function TShape.GetArea: Double;
begin
  Result := 0; // 기본값 (도형이 뭔지 모르니까)
end;

function TCircle.GetArea: Double;
begin
  Result := 3.14159 * Radius * Radius; // 원 넓이
end;

function TRectangle.GetArea: Double;
begin
  Result := Width * Height; // 직사각형 넓이
end;

var
  Circle: TCircle;
  Rect: TRectangle;
begin
  Circle := TCircle.Create;
  Rect := TRectangle.Create;
  try
    Circle.Radius := 5;
    Writeln('원의 넓이: ', Circle.GetArea:0:2);

    Rect.Width := 4;
    Rect.Height := 6;
    Writeln('직사각형의 넓이: ', Rect.GetArea:0:2);
  finally
    Circle.Free;
    Rect.Free;
  end;
  Readln;
end.
```

---

## 3. 다형성이란? (하나의 변수로 여러 타입 다루기)

다형성(Polymorphism)은 "여러 가지 형태"라는 뜻입니다.  
부모 클래스 타입의 변수에 자식 클래스 객체를 담아, 같은 코드로 다르게 동작하게 만드는 마법 같은 기능입니다!

```pascal
type
  TAnimal = class
  public
    Name: String;
    procedure MakeSound; virtual;  // 가상 메서드
  end;

  TDog = class(TAnimal)
  public
    procedure MakeSound; override;
  end;

  TCat = class(TAnimal)
  public
    procedure MakeSound; override;
  end;

procedure TAnimal.MakeSound;
begin
  Writeln(Name, ': (소리 없음)');
end;

procedure TDog.MakeSound;
begin
  Writeln(Name, ': 멍멍!');
end;

procedure TCat.MakeSound;
begin
  Writeln(Name, ': 야옹~');
end;

var
  Animals: array[0..2] of TAnimal; // 부모 타입 배열
  i: Integer;
begin
  // 부모 타입 배열에 자식 객체를 담습니다!
  Animals[0] := TDog.Create;
  Animals[0].Name := '바둑이';
  Animals[1] := TCat.Create;
  Animals[1].Name := '나비';
  Animals[2] := TDog.Create;
  Animals[2].Name := '흰둥이';

  try
    // 같은 MakeSound를 불렀지만, 각자 자기에 맞는 소리를 냅니다!
    for i := 0 to 2 do
      Animals[i].MakeSound; // ← 이것이 다형성입니다!
  finally
    for i := 0 to 2 do
      Animals[i].Free;
  end;
  Readln;
end.
```

---

## 4. 상속 관련 주요 키워드 정리

| 키워드 | 설명 |
|---|---|
| `class(부모클래스)` | 부모 클래스를 상속받아 자식 클래스 선언 |
| `virtual` | 이 메서드는 자식이 바꿀 수 있다 (부모에 표시) |
| `override` | 부모의 virtual 메서드를 자식이 덮어씀 |
| `inherited` | 자식에서 부모의 원래 메서드를 호출 |
| `abstract` | 구현 없이 선언만 함 (자식이 반드시 구현해야 함) |

---

## 5. inherited — 부모 코드도 함께 실행하기

자식이 오버라이드할 때 부모의 코드도 함께 실행하고 싶다면 `inherited`를 씁니다.

```pascal
procedure TDog.MakeSound;
begin
  inherited MakeSound; // 부모의 MakeSound를 먼저 실행
  Writeln('그리고 꼬리를 흔듭니다!');
end;
```
