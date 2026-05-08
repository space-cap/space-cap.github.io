# 8장. 예외 처리 (try / except / finally)

프로그램을 만들다 보면 예상치 못한 상황이 발생하기 마련입니다.  
사용자가 숫자를 입력해야 할 곳에 글자를 입력하거나, 없는 파일을 열려고 하면 프로그램이 갑자기 죽어버릴 수 있습니다! 😱

**예외 처리**는 이런 에러 상황에서도 프로그램이 안전하게 계속 실행될 수 있도록 해줍니다.

---

## 1. 예외 처리가 없으면?

```pascal
uses SysUtils;
var
  Num: Integer;
begin
  Num := StrToInt('abc'); // 'abc'를 숫자로 변환 → 에러!
  Writeln('변환 결과: ', Num); // 이 줄은 절대 실행되지 않습니다
end.
```
위 코드는 실행 즉시 에러 창이 뜨며 프로그램이 종료됩니다.

---

## 2. try ... except (예외 잡기)

`try` 블록 안에서 에러가 발생하면, 프로그램이 죽는 대신 `except` 블록으로 건너뜁니다.

```pascal
uses SysUtils;
var
  Num: Integer;
begin
  try
    Num := StrToInt('abc'); // 위험한 코드
    Writeln('변환 성공! 결과: ', Num);
  except
    on E: EConvertError do
      Writeln('에러! 숫자를 입력해주세요. 원인: ', E.Message);
  end;

  Writeln('프로그램이 계속 실행됩니다. :)');
  Readln;
end.
```

---

## 3. try ... finally (반드시 실행되어야 하는 코드)

에러가 나든 안 나든 **무조건 실행되어야 하는 코드**를 `finally`에 넣습니다.  
주로 객체의 `Free`를 여기에 씁니다.

```pascal
uses Classes;
var
  MyList: TStringList;
begin
  MyList := TStringList.Create;
  try
    MyList.Add('항목 1');
    // 이곳에서 에러가 나도...
  finally
    MyList.Free; // 반드시 실행됩니다!
    Writeln('메모리 해제 완료!');
  end;
end.
```

> 💡 **황금률**: `Create` 한 객체는 반드시 `try...finally...Free` 패턴으로 감싸세요!

---

## 4. 자주 만나는 예외 종류

| 예외 클래스 | 언제 발생하나? |
|---|---|
| `Exception` | 모든 예외의 최상위 (어떤 에러든 잡을 수 있음) |
| `EConvertError` | 타입 변환 실패 (`StrToInt('abc')` 등) |
| `EDivByZero` | 0으로 나누기 |
| `EAccessViolation` | 잘못된 메모리 접근 |
| `EFileNotFoundException` | 파일을 찾을 수 없음 |

---

## 5. 나만의 예외 발생시키기 (raise)

잘못된 입력을 직접 에러로 처리하고 싶을 때 `raise`를 씁니다.

```pascal
uses SysUtils;

procedure CheckAge(Age: Integer);
begin
  if Age < 0 then
    raise Exception.Create('나이는 0 이상이어야 합니다!');
  Writeln('유효한 나이: ', Age);
end;

begin
  try
    CheckAge(25);  // 정상
    CheckAge(-5);  // 예외 발생!
  except
    on E: Exception do
      Writeln('[오류] ', E.Message);
  end;
  Readln;
end.
```
