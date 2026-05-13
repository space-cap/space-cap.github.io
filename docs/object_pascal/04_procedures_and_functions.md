# 4장. 함수와 프로시저

코드가 길어지면 똑같은 코드를 여러 번 써야 할 때가 자주 생깁니다. 이럴 때 코드를 하나로 묶어서 이름을 붙여두고, 필요할 때마다 그 이름만 부르는 방법을 사용합니다. 이것이 바로 **함수(Function)** 와 **프로시저(Procedure)** 입니다.

## 1. 프로시저 (Procedure)
프로시저는 명령들을 모아놓은 블록입니다. 지정된 일만 묵묵히 수행하고, **나에게 결과값을 돌려주지 않습니다.** (마치 심부름을 시키면 심부름만 하고 끝나는 것과 같습니다.)
a
```pascal
// 'SayHello' 라는 이름의 프로시저를 만듭니다.
procedure SayHello(Name: String);
begin
  Writeln('안녕하세요, ', Name, '님! 만나서 반갑습니다.');
end;

begin
  // 필요할 때마다 이름과 넘겨줄 값만 불러서 사용합니다!
  SayHello('홍길동');
  SayHello('김철수');
end.
```

## 2. 함수 (Function)
함수는 프로시저와 비슷하지만, 계산이나 작업을 한 후에 **반드시 하나의 결과값을 돌려줍니다.** (마치 자판기에 돈을 넣으면 음료수를 내어주는 것과 같습니다.)

```pascal
// 두 숫자를 더해서 '정수(Integer)' 결과를 돌려주는 함수를 만듭니다.
function AddNumbers(A: Integer; B: Integer): Integer;
begin
  // 파스칼에서는 특별한 키워드인 'Result'라는 변수에 값을 넣어서 결과를 돌려줍니다.
  Result := A + B; 
end;

var
  Sum: Integer;
begin
  Sum := AddNumbers(10, 20); // 10과 20을 더해서 결과인 30이 Sum에 저장됩니다.
  Writeln('두 수의 합은: ', Sum);
end.
```

## 3. 매개변수 전달 방식 (값 전달 vs 참조 전달)
함수나 프로시저에 값을 넘겨줄 때(매개변수) 중요한 규칙이 있습니다.
일반적으로 값을 넘기면 복사본을 넘기기 때문에 원본은 안전합니다. 하지만 `var` 키워드를 앞에 붙여서 넘기면 원본 상자 자체가 통째로 넘어가서 원본 값이 바뀔 수 있습니다!

```pascal
// 매개변수 앞에 var가 붙어있으면, 넘겨받은 원본 변수의 값이 직접 바뀝니다! (참조 전달)
procedure MakeDouble(var Num: Integer);
begin
  Num := Num * 2;
end;

var
  MyNumber: Integer;
begin
  MyNumber := 5;
  MakeDouble(MyNumber); 
  Writeln('결과: ', MyNumber); // 5가 복제된게 아니라 원본이 변해서 10으로 출력됩니다!
end.
```

프로시저와 함수를 잘게 나누어 작성하면, 코드가 훨씬 깔끔해지고 나중에 에러를 찾거나 수정하기가 매우 쉬워집니다.
