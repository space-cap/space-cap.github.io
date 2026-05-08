# 11장. 파일 입출력 (File I/O)

프로그램이 종료되면 메모리의 변수 값은 모두 사라집니다.  
데이터를 영구적으로 보존하려면 **파일**에 저장해야 합니다!  
이 장에서는 텍스트 파일을 읽고 쓰는 방법을 배웁니다.

---

## 1. 텍스트 파일에 쓰기

```pascal
uses SysUtils;

var
  MyFile: TextFile; // 텍스트 파일을 다루는 변수
begin
  // 1. 파일 이름 지정
  AssignFile(MyFile, 'hello.txt');

  try
    // 2. 파일을 쓰기 모드로 열기 (파일이 없으면 새로 만들고, 있으면 덮어씁니다)
    Rewrite(MyFile);

    // 3. 파일에 내용 쓰기
    Writeln(MyFile, '안녕하세요!');
    Writeln(MyFile, '오브젝트 파스칼로 파일을 작성했습니다.');
    Writeln(MyFile, '오늘 날짜: ' + DateToStr(Now)); // SysUtils 필요

    Writeln('파일 저장 완료!');
  finally
    // 4. 반드시 파일을 닫아줍니다!
    CloseFile(MyFile);
  end;

  Readln;
end.
```

> 💡 `Rewrite`는 기존 파일이 있으면 내용을 지우고 새로 씁니다.  
> 기존 내용 뒤에 **이어서 쓰고 싶다면** `Append`를 사용하세요!

---

## 2. 텍스트 파일 읽기

```pascal
uses SysUtils;

var
  MyFile: TextFile;
  Line: String;      // 한 줄씩 읽어서 저장할 변수
  LineNum: Integer;
begin
  AssignFile(MyFile, 'hello.txt');

  // 파일이 존재하는지 먼저 확인합니다
  if not FileExists('hello.txt') then
  begin
    Writeln('파일을 찾을 수 없습니다!');
    Readln;
    Exit; // 함수/프로그램을 즉시 종료하는 명령어
  end;

  try
    // 읽기 모드로 파일 열기
    Reset(MyFile);

    Writeln('--- 파일 내용 ---');
    LineNum := 1;

    // EOF = End Of File (파일의 끝에 도달했는지 확인)
    while not EOF(MyFile) do
    begin
      Readln(MyFile, Line); // 한 줄씩 읽어서 Line 변수에 저장
      Writeln(LineNum, ': ', Line);
      Inc(LineNum); // LineNum := LineNum + 1 과 같습니다
    end;
  finally
    CloseFile(MyFile);
  end;

  Readln;
end.
```

---

## 3. TStringList로 파일 읽고 쓰기 (더 편리한 방법!)

`TStringList`를 사용하면 파일 읽기/쓰기가 훨씬 간단합니다.  
초보자에게 가장 추천하는 방법입니다!

```pascal
uses Classes;

var
  Lines: TStringList;
begin
  Lines := TStringList.Create;
  try
    // ① 파일 쓰기 (딱 한 줄!)
    Lines.Add('첫 번째 줄');
    Lines.Add('두 번째 줄');
    Lines.Add('세 번째 줄');
    Lines.SaveToFile('mydata.txt'); // 파일에 저장
    Writeln('파일 저장 완료!');

    // ② 파일 읽기 (딱 한 줄!)
    Lines.Clear; // 기존 내용 지우기
    Lines.LoadFromFile('mydata.txt'); // 파일 전체를 읽어옴

    Writeln('파일에 ', Lines.Count, '줄이 있습니다.');
    Writeln(Lines.Text); // 전체 내용 한꺼번에 출력

  finally
    Lines.Free;
  end;
  Readln;
end.
```

---

## 4. 파일 관련 유용한 함수들

| 함수 | 설명 | 예시 |
|---|---|---|
| `FileExists(경로)` | 파일이 존재하는지 확인 | `FileExists('data.txt')` |
| `DirectoryExists(경로)` | 폴더가 존재하는지 확인 | `DirectoryExists('C:\Data')` |
| `DeleteFile(경로)` | 파일 삭제 | `DeleteFile('old.txt')` |
| `RenameFile(원본, 새이름)` | 파일 이름 바꾸기 | `RenameFile('a.txt', 'b.txt')` |
| `ForceDirectories(경로)` | 폴더가 없으면 자동으로 만들기 | `ForceDirectories('C:\Data\Logs')` |
| `ExtractFilePath(경로)` | 경로에서 폴더 부분만 추출 | `'C:\Data\'` |
| `ExtractFileName(경로)` | 경로에서 파일명만 추출 | `'data.txt'` |

> 위 함수들은 모두 `SysUtils` 유닛에 포함되어 있습니다.

---

## 5. 실습: 간단한 메모장 프로그램

```pascal
uses SysUtils, Classes;

procedure SaveMemo(const FileName, Content: String);
var
  Lines: TStringList;
begin
  Lines := TStringList.Create;
  try
    Lines.Text := Content;
    Lines.SaveToFile(FileName);
    Writeln('저장 완료: ', FileName);
  finally
    Lines.Free;
  end;
end;

function LoadMemo(const FileName: String): String;
var
  Lines: TStringList;
begin
  Result := '';
  if not FileExists(FileName) then
  begin
    Writeln('파일이 없습니다!');
    Exit;
  end;
  Lines := TStringList.Create;
  try
    Lines.LoadFromFile(FileName);
    Result := Lines.Text;
  finally
    Lines.Free;
  end;
end;

begin
  SaveMemo('memo.txt', '파스칼 공부하기' + #13#10 + '오늘도 열심히!');
  Writeln('--- 저장된 메모 ---');
  Writeln(LoadMemo('memo.txt'));
  Readln;
end.
```
