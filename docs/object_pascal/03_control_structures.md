# 3장. 제어문 (조건문과 반복문)

프로그램이 항상 똑같은 순서로만 실행된다면 단순한 계산기에 불과하겠죠? 상황에 따라 다른 행동을 하거나, 똑같은 행동을 여러 번 반복하게 만드는 것이 바로 **제어문**입니다.

## 1. 조건문: 만약에 ~라면 (If-Then-Else)
특정 조건이 참(`True`)일 때만 코드를 실행하고 싶을 때 사용합니다.

```pascal
var
  Score: Integer;
begin
  Score := 85;

  if Score >= 80 then // 만약 점수가 80점 이상이라면
  begin
    Writeln('합격입니다!');
    Writeln('축하합니다.');
  end
  else // 그렇지 않다면 (80점 미만이라면)
  begin
    Writeln('불합격입니다.');
    Writeln('다음 기회에 다시 도전하세요.');
  end;
end.
```
- 여러 줄의 코드를 묶을 때는 항상 `begin`과 `end`를 사용하여 블록을 만듭니다.
- **주의할 점**: `else` 바로 앞에 있는 `end` 뒤에는 세미콜론(`;`)을 붙이지 않습니다! 이것은 파스칼만의 독특한 문법입니다.

## 2. 조건문: 여러 경우 중 하나 선택 (Case)
조건이 여러 개일 때 `if`를 계속 쓰면 코드가 매우 복잡해집니다. 이때 `case`문을 쓰면 깔끔하게 정리할 수 있습니다.

```pascal
var
  Grade: Char; // Char는 글자 딱 한 개를 저장하는 타입입니다 ('A', 'B' 등)
begin
  Grade := 'B';

  case Grade of
    'A': Writeln('최우수 학생입니다.');
    'B': Writeln('우수 학생입니다.');
    'C': Writeln('조금 더 노력하세요.');
    else Writeln('올바른 학점이 아닙니다.'); // 그 외의 모든 경우
  end;
end.
```

## 3. 반복문: 정해진 횟수만큼 반복 (For)
똑같은 글자를 화면에 5번 출력하고 싶다면 코드를 5줄 쓰는 대신 `for`문을 사용합니다.

```pascal
var
  i: Integer;
begin
  // i라는 변수가 1부터 5가 될 때까지 do 이하를 실행합니다.
  for i := 1 to 5 do 
  begin
    Writeln(i, '번째 인사: 안녕하세요!');
  end;
end.
```

## 4. 반복문: 조건이 맞을 때까지 반복 (While)
정확히 몇 번 반복할지는 모르겠지만, '어떤 조건이 만족하는 동안'에는 계속 반복하고 싶을 때 씁니다.

```pascal
var
  Count: Integer;
begin
  Count := 1;
  while Count <= 3 do // Count가 3 이하인 '동안에'
  begin
    Writeln('카운트: ', Count);
    Count := Count + 1; // Count 값을 1씩 증가시킵니다.
  end;
end.
```
