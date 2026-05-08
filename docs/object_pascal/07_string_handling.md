# 7장. 문자열 처리

프로그램에서 텍스트를 다루는 일은 매우 자주 발생합니다.  
사용자의 이름을 입력받거나, 이메일 주소를 검사하거나, 문장에서 특정 단어를 찾는 일 등이죠.  
오브젝트 파스칼에는 이런 작업을 쉽게 할 수 있는 **다양한 문자열 함수**가 내장되어 있습니다!

---

## 1. 문자열 기본 연산

### 문자열 합치기 (+)

```pascal
var
  FirstName, LastName, FullName: String;
begin
  FirstName := '길동';
  LastName := '홍';
  FullName := LastName + FirstName; // + 기호로 문자열을 붙입니다
  Writeln('이름: ', FullName); // 출력: 홍길동
end.
```

### 문자열 길이 구하기 (Length)

```pascal
var
  Msg: String;
begin
  Msg := 'Hello, Pascal!';
  Writeln('문자열 길이: ', Length(Msg)); // 출력: 14
end.
```

---

## 2. 자주 쓰는 문자열 함수 모음

### 🔪 잘라내기 (Copy)
문자열의 일부분만 잘라서 가져옵니다.

```pascal
var
  Email, Domain: String;
begin
  Email := 'user@example.com';
  // Copy(원본문자열, 시작위치, 가져올글자수)
  Domain := Copy(Email, 6, 11); // 6번째 글자부터 11글자 → 'example.com'
  Writeln('도메인: ', Domain);
end.
```

### 🔍 찾기 (Pos)
특정 문자나 단어가 몇 번째 위치에 있는지 찾습니다. 없으면 0을 반환합니다.

```pascal
var
  Sentence: String;
  Position: Integer;
begin
  Sentence := 'I love Object Pascal!';
  Position := Pos('Pascal', Sentence); // 'Pascal'이 몇 번째 위치에 있나?
  Writeln('"Pascal"의 위치: ', Position); // 출력: 15
  
  if Pos('@', 'test@email.com') > 0 then
    Writeln('올바른 이메일 형식입니다.')
  else
    Writeln('@가 없습니다!');
end.
```

### 🔄 대소문자 변환 (UpperCase / LowerCase)

```pascal
var
  Text: String;
begin
  Text := 'Hello World';
  Writeln(UpperCase(Text)); // 출력: HELLO WORLD (모두 대문자)
  Writeln(LowerCase(Text)); // 출력: hello world (모두 소문자)
end.
```

### ✂️ 공백 제거 (Trim / TrimLeft / TrimRight)
사용자가 입력할 때 실수로 넣은 앞뒤 공백을 제거합니다.

```pascal
var
  UserInput: String;
begin
  UserInput := '   홍길동   '; // 앞뒤에 공백이 잔뜩 있는 상태
  Writeln('원본: [', UserInput, ']');
  Writeln('Trim 후: [', Trim(UserInput), ']'); // 출력: [홍길동]
end.
```

### 🔁 바꾸기 (StringReplace)
문자열 안의 특정 텍스트를 다른 텍스트로 바꿉니다.

```pascal
uses SysUtils; // StringReplace를 쓰려면 이 줄이 필요합니다

var
  Text: String;
begin
  Text := '나는 자바를 좋아한다. 자바는 정말 좋은 언어이다.';
  // StringReplace(원본, 찾을것, 바꿀것, 옵션)
  // rfReplaceAll 은 모든 곳을 다 바꾸라는 옵션입니다
  Text := StringReplace(Text, '자바', '파스칼', [rfReplaceAll]);
  Writeln(Text);
  // 출력: 나는 파스칼을 좋아한다. 파스칼은 정말 좋은 언어이다.
end.
```

---

## 3. 숫자 ↔ 문자열 변환

실제 프로그램에서는 숫자를 문자로, 문자를 숫자로 변환하는 일이 매우 많습니다.

```pascal
uses SysUtils;

var
  Age: Integer;
  Pi: Double;
  AgeStr: String;
begin
  // 숫자 → 문자열
  Age := 25;
  AgeStr := IntToStr(Age);           // 정수를 문자열로
  Writeln('문자열로 변환: ' + AgeStr + '세'); // + 로 붙일 수 있습니다

  Pi := 3.14159;
  Writeln('실수를 문자열로: ', FloatToStr(Pi));

  // 문자열 → 숫자
  AgeStr := '30';
  Age := StrToInt(AgeStr);           // 문자열을 정수로
  Writeln('숫자로 변환: ', Age + 1);  // 출력: 31 (숫자 연산 가능!)

  // 변환 실패를 안전하게 처리 (StrToIntDef: 변환 실패시 기본값 반환)
  Age := StrToIntDef('abc', 0); // 'abc'는 숫자로 못 바꾸므로 기본값 0 반환
  Writeln('안전한 변환 결과: ', Age); // 출력: 0
end.
```

---

## 4. 자주 쓰는 문자열 함수 요약표

| 함수 | 용도 | 예시 |
|---|---|---|
| `Length(s)` | 길이 반환 | `Length('Hello')` → `5` |
| `Copy(s, 시작, 길이)` | 일부 잘라내기 | `Copy('Hello', 2, 3)` → `'ell'` |
| `Pos(찾을것, 원본)` | 위치 찾기 | `Pos('l', 'Hello')` → `3` |
| `UpperCase(s)` | 대문자로 변환 | `UpperCase('hi')` → `'HI'` |
| `LowerCase(s)` | 소문자로 변환 | `LowerCase('HI')` → `'hi'` |
| `Trim(s)` | 앞뒤 공백 제거 | `Trim('  hi  ')` → `'hi'` |
| `IntToStr(n)` | 정수 → 문자열 | `IntToStr(42)` → `'42'` |
| `StrToInt(s)` | 문자열 → 정수 | `StrToInt('42')` → `42` |
| `FloatToStr(f)` | 실수 → 문자열 | `FloatToStr(3.14)` → `'3.14'` |
| `StringReplace(...)` | 문자열 교체 | `'Hello' → 'World'` |

---

## 5. 실습: 간단한 이메일 유효성 검사

배운 함수들을 조합해서 이메일 형식이 올바른지 검사해 봅시다!

```pascal
uses SysUtils;

function IsValidEmail(Email: String): Boolean;
begin
  Email := Trim(Email); // 앞뒤 공백 제거
  // 조건: @가 있어야 하고, .이 있어야 하고, 길이가 5 이상이어야 한다
  Result := (Pos('@', Email) > 0) and 
            (Pos('.', Email) > 0) and 
            (Length(Email) >= 5);
end;

begin
  if IsValidEmail('user@email.com') then
    Writeln('올바른 이메일입니다.')
  else
    Writeln('잘못된 이메일 형식입니다.');

  if IsValidEmail('abcdef') then
    Writeln('올바른 이메일입니다.')
  else
    Writeln('잘못된 이메일 형식입니다.'); // 이쪽이 출력됩니다

  Readln;
end.
```
