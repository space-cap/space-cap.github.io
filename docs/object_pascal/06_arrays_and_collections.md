# 6장. 배열과 컬렉션

지금까지는 변수 하나에 값 하나만 저장했습니다.  
하지만 학생 30명의 점수, 과일 100가지 목록처럼 **같은 종류의 데이터를 여러 개** 한꺼번에 다뤄야 할 때가 많습니다.  
이럴 때 사용하는 것이 바로 **배열(Array)**과 **컬렉션(Collection)**입니다!

---

## 1. 정적 배열 (크기가 정해진 배열)

가장 기본적인 배열입니다. 만들 때 크기를 미리 정해야 합니다.  
마치 칸 수가 정해진 달력 같은 것입니다!

```pascal
var
  Scores: array[1..5] of Integer; // 1번부터 5번 칸까지, 정수를 담는 배열
  i: Integer;
begin
  // 배열에 값 넣기
  Scores[1] := 90;
  Scores[2] := 85;
  Scores[3] := 78;
  Scores[4] := 92;
  Scores[5] := 88;

  // 배열에서 값 꺼내기 (for 반복문과 함께 사용하면 편리합니다!)
  Writeln('--- 성적 목록 ---');
  for i := 1 to 5 do
  begin
    Writeln(i, '번 학생 점수: ', Scores[i]);
  end;
end.
```

### 배열의 인덱스
배열에서 각 칸의 번호를 **인덱스(Index)**라고 합니다.  
파스칼은 `array[시작..끝]` 형태로 시작 번호를 자유롭게 정할 수 있습니다. `array[0..4]`도 되고, `array[1..5]`도 됩니다!

---

## 2. 동적 배열 (크기가 변하는 배열)

크기를 미리 모를 때는 **동적 배열**을 사용합니다.  
필요한 만큼 크기를 늘리거나 줄일 수 있습니다.

```pascal
var
  Fruits: array of String; // 크기를 정하지 않고 선언
  i: Integer;
begin
  // SetLength로 배열 크기를 나중에 정합니다
  SetLength(Fruits, 3);

  // 동적 배열의 인덱스는 항상 0부터 시작합니다!
  Fruits[0] := '사과';
  Fruits[1] := '바나나';
  Fruits[2] := '포도';

  // 나중에 크기를 4로 늘릴 수도 있습니다
  SetLength(Fruits, 4);
  Fruits[3] := '딸기';

  Writeln('과일 개수: ', Length(Fruits)); // Length()로 배열 길이를 알 수 있습니다

  for i := 0 to High(Fruits) do  // High()는 배열의 마지막 인덱스 번호를 알려줍니다
  begin
    Writeln(Fruits[i]);
  end;
end.
```

---

## 3. 2차원 배열 (표처럼 생긴 배열)

엑셀 표처럼 **행(가로)과 열(세로)**이 있는 2차원 배열도 만들 수 있습니다.

```pascal
var
  Matrix: array[1..3, 1..3] of Integer; // 3행 3열짜리 배열
  Row, Col: Integer;
begin
  // 구구단 3단을 2차원 배열에 저장해 봅시다
  for Row := 1 to 3 do
    for Col := 1 to 3 do
      Matrix[Row, Col] := Row * Col;

  // 출력하기
  for Row := 1 to 3 do
  begin
    for Col := 1 to 3 do
      Write(Matrix[Row, Col]:4); // :4는 4칸 너비로 출력하라는 의미입니다
    Writeln; // 줄바꿈
  end;
end.
```

---

## 4. TStringList (문자열을 편리하게 관리하는 컬렉션)

배열보다 훨씬 편리하게 **문자열 목록**을 관리할 수 있는 클래스입니다.  
추가, 삭제, 정렬이 아주 쉽습니다!

```pascal
uses
  Classes; // TStringList를 사용하려면 이 줄이 필요합니다

var
  ShoppingList: TStringList;
  i: Integer;
begin
  ShoppingList := TStringList.Create; // 객체 생성
  try
    // 항목 추가
    ShoppingList.Add('우유');
    ShoppingList.Add('계란');
    ShoppingList.Add('빵');
    ShoppingList.Add('사과');

    Writeln('쇼핑 목록 (', ShoppingList.Count, '개):'); // Count로 개수 확인
    for i := 0 to ShoppingList.Count - 1 do
      Writeln('- ', ShoppingList[i]);

    // 정렬하기
    ShoppingList.Sort;
    Writeln('--- 가나다 순으로 정렬 후 ---');
    for i := 0 to ShoppingList.Count - 1 do
      Writeln('- ', ShoppingList[i]);

    // 특정 항목 삭제
    ShoppingList.Delete(0); // 0번째 항목 삭제
    Writeln('--- 첫 번째 항목 삭제 후 ---');
    for i := 0 to ShoppingList.Count - 1 do
      Writeln('- ', ShoppingList[i]);
  finally
    ShoppingList.Free; // 반드시 메모리 해제!
  end;
end.
```

| 메서드 | 설명 |
|---|---|
| `Add('값')` | 목록 끝에 항목 추가 |
| `Delete(인덱스)` | 특정 위치 항목 삭제 |
| `Count` | 항목 개수 반환 |
| `Sort` | 알파벳/가나다 순 정렬 |
| `IndexOf('값')` | 항목의 위치(인덱스) 찾기, 없으면 -1 |
| `Clear` | 모든 항목 지우기 |

---

## 5. 언제 배열 vs TStringList를 쓸까?

| 상황 | 추천 |
|---|---|
| 크기가 고정, 숫자 계산 위주 | 정적 배열 |
| 크기가 가변적, 데이터 추가/삭제 많음 | 동적 배열 또는 TStringList |
| 문자열 목록을 파일로 저장/불러오기 | TStringList (파일 기능 내장!) |
