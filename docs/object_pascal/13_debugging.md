# 13장. 디버깅 방법 (오류 찾기와 고치기)

코딩하다가 에러가 나면 당황스럽죠? 하지만 걱정하지 마세요!  
에러를 찾고 고치는 능력인 **디버깅(Debugging)**은 모든 개발자가 꼭 익혀야 할 중요한 기술입니다.  
오히려 에러를 잘 다루는 개발자가 실력 있는 개발자입니다! 💪

---

## 1. 에러의 종류 세 가지

### ① 컴파일 에러 (Compile Error)
코드를 실행하기 전에 **문법이 잘못된 경우** 발생합니다.  
가장 쉽게 발견할 수 있고, IDE가 정확한 위치를 알려줍니다.

```pascal
// 예: 세미콜론(;)을 빠뜨린 경우
var
  Name: String  // ← 여기서 ; 누락 → 컴파일 에러!
begin
  Name := '홍길동';
end.
```
> Lazarus/Delphi에서 `F9`를 누르면 빨간 줄로 에러 위치를 알려줍니다.

### ② 런타임 에러 (Runtime Error)
프로그램이 **실행 중에 발생**하는 에러입니다. 8장에서 배운 예외 처리로 대응합니다.

```pascal
var
  Arr: array[1..5] of Integer;
begin
  Arr[10] := 100; // 범위를 벗어남 → 실행 중 에러!
end.
```

### ③ 논리 에러 (Logic Error)
프로그램이 실행은 되지만 **결과가 이상한** 경우입니다. 가장 찾기 어렵습니다.

```pascal
// 평균을 구하려 했는데 잘못된 공식을 사용한 경우
var
  Total, Count, Average: Integer;
begin
  Total := 300;
  Count := 5;
  Average := Total + Count; // 실수! + 가 아니라 div 여야 함
  Writeln('평균: ', Average); // 305 출력 (잘못된 결과)
end.
```

---

## 2. IDE의 디버거 사용하기 (Lazarus / Delphi)

IDE에는 강력한 **디버거(Debugger)**가 내장되어 있습니다.  
코드를 한 줄씩 실행하며 변수의 값이 어떻게 변하는지 눈으로 직접 확인할 수 있습니다!

### 주요 단축키

| 단축키 | 기능 | 설명 |
|---|---|---|
| `F9` | 실행 (Run) | 프로그램을 시작합니다 |
| `F8` | 다음 줄 실행 (Step Over) | 코드를 한 줄씩 실행합니다 |
| `F7` | 함수 안으로 (Step Into) | 함수 내부로 들어가서 한 줄씩 실행 |
| `Shift+F8` | 함수 밖으로 (Step Out) | 현재 함수를 빠져나옵니다 |
| `F4` | 커서 위치까지 실행 | 커서가 있는 줄까지 단번에 실행 |
| `Ctrl+F2` | 프로그램 정지 | 실행 중인 프로그램을 멈춥니다 |

### 중단점(Breakpoint) 설정하기

**중단점**은 프로그램 실행을 특정 줄에서 잠시 멈추게 하는 마커입니다.  
에러가 의심되는 줄 왼쪽 여백을 **클릭**하면 빨간 동그라미 🔴 가 생깁니다.

```pascal
var
  i: Integer;
  Sum: Integer;
begin
  Sum := 0;
  for i := 1 to 10 do
  begin
    Sum := Sum + i; // ← 이 줄에 중단점을 설정하면 매번 반복할 때마다 멈춥니다
  end;
  Writeln('합계: ', Sum);
end.
```

> 중단점에서 멈추면 **마우스를 변수 위에 올리면** 현재 값을 팝업으로 보여줍니다!

---

## 3. Writeln으로 디버깅하기 (가장 원시적이지만 확실한 방법)

IDE 디버거가 어렵게 느껴진다면, 그냥 `Writeln`으로 중간중간 값을 출력해서 확인하는 방법도 효과적입니다.

```pascal
function CalculateBonus(Salary: Double; Rating: Integer): Double;
begin
  Writeln('[DEBUG] Salary 값: ', Salary); // 값 확인
  Writeln('[DEBUG] Rating 값: ', Rating);
  
  Result := Salary * (Rating / 10);

  Writeln('[DEBUG] 계산된 보너스: ', Result);
end;
```

> 디버깅이 끝나면 `[DEBUG]` 줄들을 지우거나 주석 처리하는 것을 잊지 마세요!

---

## 4. 자주 만나는 에러 메시지와 해결법

| 에러 메시지 | 원인 | 해결책 |
|---|---|---|
| `Identifier not found: xxx` | 변수/함수 이름이 잘못됨 | 오탈자 확인, `var` 선언 확인 |
| `Undeclared identifier` | 선언되지 않은 변수 사용 | `var` 블록에 변수 추가 |
| `Missing SEMICOLON` | 세미콜론(`;`) 누락 | 윗 줄 끝에 `;` 추가 |
| `Type mismatch` | 타입이 맞지 않음 | 변수 타입과 값의 타입 확인 |
| `Access violation` | 없는 메모리에 접근 | `Free`된 객체 재사용 여부 확인 |
| `Stack overflow` | 함수가 무한히 자기 자신을 호출 | 재귀 함수의 종료 조건 확인 |

---

## 5. 에러를 줄이는 좋은 습관

> 에러가 났을 때 고치는 것도 중요하지만, 처음부터 에러가 적게 나는 코드를 쓰는 습관이 더 중요합니다!

- ✅ **변수 이름을 의미 있게 짓기**: `x` 대신 `StudentAge`, `a` 대신 `TotalPrice`
- ✅ **함수를 짧게 유지하기**: 한 함수가 한 가지 일만 하도록 (20줄 이내 권장)
- ✅ **주석 달기**: 나중에 내가 봐도 이해할 수 있도록
- ✅ **예외 처리 습관화**: 사용자 입력은 항상 `try...except`로 감싸기
- ✅ **작은 단위로 자주 테스트**: 코드를 많이 짜고 한 번에 실행하지 말고, 조금 짜고 바로 실행해보기
- ✅ **Create-Free 짝 맞추기**: 객체 생성하면 바로 Free 위치도 작성하기
