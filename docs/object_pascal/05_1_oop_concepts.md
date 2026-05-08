# 5-1장. 객체 지향 프로그래밍이란?

오브젝트 파스칼의 '오브젝트(Object)'는 '객체'라는 뜻입니다.  
객체 지향 프로그래밍(OOP)은 현대 소프트웨어 개발의 핵심 패러다임입니다.  
이 장에서는 **OOP가 왜 필요한지**, **핵심 개념이 무엇인지** 먼저 이해해 봅시다.

---

## 1. 왜 객체 지향이 필요할까?

### 절차적 프로그래밍의 한계

초반에 배운 방식(위에서 아래로 순서대로 실행)을 **절차적 프로그래밍**이라고 합니다.  
간단한 프로그램에는 충분하지만, 규모가 커지면 문제가 생깁니다.

```pascal
// 절차적 방식: 학생 3명의 정보를 관리하려면?
var
  Student1Name: String;
  Student1Score: Integer;
  Student2Name: String;
  Student2Score: Integer;
  Student3Name: String;
  Student3Score: Integer;
begin
  Student1Name := '홍길동';
  Student1Score := 90;
  // 학생이 100명이면? 변수가 200개... 😱
end.
```

학생이 100명이면 변수를 200개 만들어야 합니다. 코드가 스파게티처럼 엉켜버립니다!

### 객체 지향 방식으로 해결

```pascal
type
  TStudent = class  // '학생'이라는 설계도(클래스)를 하나만 만들면!
  public
    Name: String;
    Score: Integer;
  end;

var
  Students: array[1..100] of TStudent; // 100명도 이렇게 간단하게!
```

이처럼 OOP는 **현실 세계의 개념을 코드로 그대로 표현**할 수 있게 해줍니다.

---

## 2. 클래스(Class)와 객체(Object) — 붕어빵의 비유

| 개념 | 비유 | 설명 |
|---|---|---|
| **클래스 (Class)** | 붕어빵 틀 | 객체를 만들기 위한 **설계도** |
| **객체 (Object)** | 구워진 붕어빵 | 설계도를 바탕으로 **실제로 만들어진 것** |
| **속성 (Field)** | 팥소의 양, 크기 | 객체가 **가지고 있는 데이터** |
| **메서드 (Method)** | 먹다, 포장하다 | 객체가 **할 수 있는 행동** |
| **인스턴스 (Instance)** | 붕어빵 한 개 한 개 | 클래스로 만든 **개별 객체** |

```
      [붕어빵 틀 = 클래스]
           ↓ Create (생산)
  [붕어빵1]  [붕어빵2]  [붕어빵3]
   (팥소)    (크림)     (피자)
  = 객체(인스턴스)들
```

**하나의 클래스(틀)로 여러 개의 객체(붕어빵)를 만들 수 있습니다!**  
각 객체는 같은 틀로 만들었지만 내용물(속성 값)은 다를 수 있습니다.

---

## 3. OOP의 4대 핵심 특징

파스칼 코드를 보기 전에, OOP의 핵심 개념 4가지를 먼저 이해해 봅시다.

### ① 캡슐화 (Encapsulation)
데이터와 그것을 다루는 기능을 하나의 단위(클래스)로 묶고,  
외부에서 함부로 접근하지 못하게 보호하는 것입니다.

> 🏦 **비유**: ATM 기계. 내부 구조는 몰라도 버튼만 누르면 됩니다. 내부 회로를 직접 건드릴 수는 없죠.

### ② 상속 (Inheritance)
기존 클래스를 바탕으로 새로운 클래스를 만들어 코드를 재사용하는 것입니다.

> 👨‍👦 **비유**: 부모에게서 특성을 물려받는 것처럼, 클래스도 부모 클래스의 기능을 물려받습니다.

### ③ 다형성 (Polymorphism)
같은 이름의 메서드를 호출해도, 객체의 종류에 따라 다르게 동작하는 것입니다.

> 🎮 **비유**: "공격" 버튼을 눌렀을 때 전사는 칼을 쓰고, 마법사는 마법을 씁니다. 같은 버튼인데 다른 동작!

### ④ 추상화 (Abstraction)
복잡한 내부 구현은 감추고, 핵심적인 기능만 외부에 노출하는 것입니다.

> 🚗 **비유**: 자동차 운전자는 엔진 구조를 몰라도 됩니다. 핸들과 페달만 알면 됩니다.

---

## 4. 파스칼에서 클래스 선언하기

파스칼에서 클래스는 `type` 섹션에서 선언합니다.

```pascal
type
  // 클래스 이름은 관례적으로 대문자 T로 시작합니다 (Type의 T)
  TCar = class
  public
    // 속성 (Field): 데이터를 저장합니다
    Brand: String;   // 제조사
    Color: String;   // 색상
    Speed: Integer;  // 현재 속도

    // 메서드 (Method): 행동을 정의합니다
    procedure Accelerate(Amount: Integer); // 가속
    procedure Brake;                       // 브레이크
    function GetInfo: String;              // 차량 정보 반환
  end;
```

### 메서드의 실제 내용 작성 (Implementation)

```pascal
// 클래스 이름.메서드 이름 형태로 작성합니다
procedure TCar.Accelerate(Amount: Integer);
begin
  Speed := Speed + Amount;
  Writeln(Brand, '가 가속합니다. 현재 속도: ', Speed, 'km/h');
end;

procedure TCar.Brake;
begin
  if Speed > 0 then
    Speed := 0;
  Writeln(Brand, '가 정지했습니다.');
end;

function TCar.GetInfo: String;
begin
  Result := Brand + ' (' + Color + ') - ' + IntToStr(Speed) + 'km/h';
end;
```

---

## 5. 객체 만들고 사용하기

```pascal
var
  MyCar, YourCar: TCar;
begin
  // 1. 객체 생성 (Create = 붕어빵 굽기)
  MyCar := TCar.Create;
  YourCar := TCar.Create;

  try
    // 2. 각 객체의 속성 설정 (같은 클래스이지만 내용은 다릅니다!)
    MyCar.Brand := '현대';
    MyCar.Color := '빨간색';
    MyCar.Speed := 0;

    YourCar.Brand := '기아';
    YourCar.Color := '파란색';
    YourCar.Speed := 0;

    // 3. 메서드 호출
    MyCar.Accelerate(60);
    MyCar.Accelerate(40);
    Writeln(MyCar.GetInfo);    // 출력: 현대 (빨간색) - 100km/h

    YourCar.Accelerate(80);
    Writeln(YourCar.GetInfo);  // 출력: 기아 (파란색) - 80km/h

    MyCar.Brake;

  finally
    // 4. 반드시 메모리 해제! (순서는 생성의 역순)
    YourCar.Free;
    MyCar.Free;
  end;

  Readln;
end.
```

---

## 6. 파스칼 클래스의 최상위 조상: TObject

파스칼의 모든 클래스는 **TObject**를 자동으로 상속받습니다.  
`TCar = class` 는 사실 `TCar = class(TObject)` 와 동일합니다.

TObject는 `Create`, `Free`, `ClassName` 등의 기본 기능을 제공합니다.

```pascal
var
  MyCar: TCar;
begin
  MyCar := TCar.Create;
  try
    Writeln(MyCar.ClassName); // 출력: TCar (클래스 이름 확인)
  finally
    MyCar.Free;
  end;
end.
```

---

> 📌 **다음 장 예고**: 5-2장에서는 생성자/소멸자를 직접 만들고,  
> 클래스를 더 정교하게 설계하는 방법을 배웁니다!
