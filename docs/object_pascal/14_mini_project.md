# 14장. 실전 미니 프로젝트: 학생 성적 관리 프로그램

드디어 마지막 장입니다! 🎉  
지금까지 배운 **모든 개념**을 하나로 합쳐서 실제로 동작하는 프로그램을 만들어 봅시다.  
콘솔 기반의 **학생 성적 관리 프로그램**을 처음부터 끝까지 직접 만들어봅니다!

---

## 프로그램 기능 목록

1. ✅ 학생 정보 추가 (이름, 점수)
2. ✅ 모든 학생 목록 출력
3. ✅ 평균, 최고점, 최저점 계산
4. ✅ 특정 학생 검색
5. ✅ 데이터를 파일에 저장하고 불러오기

---

## 1단계: 학생 데이터 구조 설계

```pascal
type
  // 학생 한 명의 정보를 담는 클래스
  TStudent = class
  public
    Name: String;
    Score: Integer;

    constructor Create(const AName: String; AScore: Integer);
    function GetGrade: String; // 점수에 따른 등급 반환
  end;

constructor TStudent.Create(const AName: String; AScore: Integer);
begin
  inherited Create; // 부모 클래스(TObject) 생성자 호출
  Name := AName;
  Score := AScore;
end;

function TStudent.GetGrade: String;
begin
  if Score >= 90 then Result := 'A'
  else if Score >= 80 then Result := 'B'
  else if Score >= 70 then Result := 'C'
  else if Score >= 60 then Result := 'D'
  else Result := 'F';
end;
```

---

## 2단계: 성적부 클래스 설계

```pascal
uses Classes, SysUtils;

type
  TGradeBook = class
  private
    FStudents: TList; // 학생 객체들을 담을 동적 목록
  public
    constructor Create;
    destructor Destroy; override; // 소멸자 (객체가 Free될 때 자동 호출)

    procedure AddStudent(const Name: String; Score: Integer);
    procedure PrintAll;
    procedure PrintStats;
    function FindStudent(const Name: String): TStudent;
    procedure SaveToFile(const FileName: String);
    procedure LoadFromFile(const FileName: String);
    function GetCount: Integer;
  end;

constructor TGradeBook.Create;
begin
  inherited Create;
  FStudents := TList.Create;
end;

destructor TGradeBook.Destroy;
var
  i: Integer;
begin
  // 목록 안의 모든 학생 객체를 먼저 해제합니다
  for i := 0 to FStudents.Count - 1 do
    TStudent(FStudents[i]).Free;
  FStudents.Free;
  inherited Destroy;
end;

function TGradeBook.GetCount: Integer;
begin
  Result := FStudents.Count;
end;
```

---

## 3단계: 핵심 기능 구현

```pascal
procedure TGradeBook.AddStudent(const Name: String; Score: Integer);
var
  Student: TStudent;
begin
  Student := TStudent.Create(Name, Score);
  FStudents.Add(Student);
  Writeln('"', Name, '" 학생이 추가되었습니다.');
end;

procedure TGradeBook.PrintAll;
var
  i: Integer;
  Student: TStudent;
begin
  if FStudents.Count = 0 then
  begin
    Writeln('등록된 학생이 없습니다.');
    Exit;
  end;

  Writeln('');
  Writeln('┌────────────────────────────────┐');
  Writeln('│        학생 성적 목록          │');
  Writeln('├──────────┬──────────┬──────────┤');
  Writeln('│   이름   │   점수   │   등급   │');
  Writeln('├──────────┼──────────┼──────────┤');
  for i := 0 to FStudents.Count - 1 do
  begin
    Student := TStudent(FStudents[i]);
    Writeln('│ ', Student.Name:-8, ' │ ', Student.Score:6, '점  │   ', Student.GetGrade, '    │');
  end;
  Writeln('└──────────┴──────────┴──────────┘');
  Writeln('총 ', FStudents.Count, '명');
end;

procedure TGradeBook.PrintStats;
var
  i: Integer;
  Student: TStudent;
  Total, MaxScore, MinScore: Integer;
  Average: Double;
begin
  if FStudents.Count = 0 then
  begin
    Writeln('등록된 학생이 없습니다.');
    Exit;
  end;

  Total := 0;
  MaxScore := TStudent(FStudents[0]).Score;
  MinScore := TStudent(FStudents[0]).Score;

  for i := 0 to FStudents.Count - 1 do
  begin
    Student := TStudent(FStudents[i]);
    Total := Total + Student.Score;
    if Student.Score > MaxScore then MaxScore := Student.Score;
    if Student.Score < MinScore then MinScore := Student.Score;
  end;

  Average := Total / FStudents.Count;

  Writeln('');
  Writeln('=== 통계 ===');
  Writeln('평균 점수: ', Average:0:1, '점');
  Writeln('최고 점수: ', MaxScore, '점');
  Writeln('최저 점수: ', MinScore, '점');
end;

function TGradeBook.FindStudent(const Name: String): TStudent;
var
  i: Integer;
  Student: TStudent;
begin
  Result := nil; // 못 찾으면 nil(없음)을 반환
  for i := 0 to FStudents.Count - 1 do
  begin
    Student := TStudent(FStudents[i]);
    if LowerCase(Student.Name) = LowerCase(Name) then // 대소문자 무시하고 비교
    begin
      Result := Student;
      Exit; // 찾으면 바로 반환
    end;
  end;
end;
```

---

## 4단계: 파일 저장/불러오기

```pascal
procedure TGradeBook.SaveToFile(const FileName: String);
var
  Lines: TStringList;
  i: Integer;
  Student: TStudent;
begin
  Lines := TStringList.Create;
  try
    for i := 0 to FStudents.Count - 1 do
    begin
      Student := TStudent(FStudents[i]);
      // 이름과 점수를 쉼표로 구분해서 저장
      Lines.Add(Student.Name + ',' + IntToStr(Student.Score));
    end;
    Lines.SaveToFile(FileName);
    Writeln('파일 저장 완료: ', FileName);
  finally
    Lines.Free;
  end;
end;

procedure TGradeBook.LoadFromFile(const FileName: String);
var
  Lines: TStringList;
  i, CommaPos: Integer;
  Name: String;
  Score: Integer;
begin
  if not FileExists(FileName) then
  begin
    Writeln('저장 파일이 없습니다: ', FileName);
    Exit;
  end;

  Lines := TStringList.Create;
  try
    Lines.LoadFromFile(FileName);
    for i := 0 to Lines.Count - 1 do
    begin
      CommaPos := Pos(',', Lines[i]);
      if CommaPos > 0 then
      begin
        Name := Copy(Lines[i], 1, CommaPos - 1);
        Score := StrToIntDef(Copy(Lines[i], CommaPos + 1, Length(Lines[i])), 0);
        AddStudent(Name, Score);
      end;
    end;
    Writeln('파일 불러오기 완료! (', Lines.Count, '명)');
  finally
    Lines.Free;
  end;
end;
```

---

## 5단계: 메인 메뉴와 전체 실행

```pascal
const
  SAVE_FILE = 'students.csv';

procedure ShowMenu;
begin
  Writeln('');
  Writeln('╔════════════════════════╗');
  Writeln('║   학생 성적 관리 시스템  ║');
  Writeln('╠════════════════════════╣');
  Writeln('║  1. 학생 추가           ║');
  Writeln('║  2. 전체 목록 보기      ║');
  Writeln('║  3. 통계 보기           ║');
  Writeln('║  4. 학생 검색           ║');
  Writeln('║  5. 저장               ║');
  Writeln('║  6. 불러오기           ║');
  Writeln('║  0. 종료               ║');
  Writeln('╚════════════════════════╝');
  Write('선택: ');
end;

var
  GradeBook: TGradeBook;
  Choice: Integer;
  Name: String;
  Score: Integer;
  Found: TStudent;
begin
  GradeBook := TGradeBook.Create;
  try
    Writeln('학생 성적 관리 프로그램에 오신 것을 환영합니다!');

    repeat
      ShowMenu;
      try
        Readln(Choice);
      except
        Choice := -1; // 숫자가 아닌 값 입력 시 처리
      end;

      case Choice of
        1: begin
             Write('학생 이름: ');
             Readln(Name);
             Write('점수 (0-100): ');
             try
               Readln(Score);
               if (Score < 0) or (Score > 100) then
                 Writeln('점수는 0~100 사이여야 합니다!')
               else
                 GradeBook.AddStudent(Name, Score);
             except
               Writeln('올바른 점수를 입력해주세요!');
             end;
           end;
        2: GradeBook.PrintAll;
        3: GradeBook.PrintStats;
        4: begin
             Write('검색할 학생 이름: ');
             Readln(Name);
             Found := GradeBook.FindStudent(Name);
             if Found <> nil then
               Writeln('찾았습니다! → ', Found.Name, ' / ', Found.Score, '점 / ', Found.GetGrade, '등급')
             else
               Writeln('"', Name, '" 학생을 찾을 수 없습니다.');
           end;
        5: GradeBook.SaveToFile(SAVE_FILE);
        6: GradeBook.LoadFromFile(SAVE_FILE);
        0: Writeln('프로그램을 종료합니다. 수고하셨습니다! 👋');
        else
           Writeln('올바른 메뉴 번호를 선택해주세요.');
      end;
    until Choice = 0;

  finally
    GradeBook.Free;
  end;
end.
```

---

## 🎓 완성! 이 프로젝트에서 사용한 개념들

| 사용된 개념 | 배운 장 |
|---|---|
| 변수, 데이터 타입 | 2장 |
| 조건문 (if, case), 반복문 (repeat...until) | 3장 |
| 함수와 프로시저 | 4장 |
| 클래스와 객체 (TStudent, TGradeBook) | 5장 |
| TList (컬렉션) | 6장 |
| 문자열 처리 (Pos, Copy, LowerCase) | 7장 |
| 예외 처리 (try...except) | 8장 |
| 파일 저장/불러오기 (TStringList) | 11장 |

---

## 🚀 더 발전시켜보기

이 프로그램을 스스로 더 개선해보는 것을 도전해보세요!

- [ ] 학생 삭제 기능 추가
- [ ] 점수 기준으로 정렬하는 기능 추가
- [ ] 12장의 내용을 활용해서 GUI 버전으로 만들기
- [ ] 데이터베이스에 저장하도록 업그레이드

여기까지 오신 여러분은 이미 오브젝트 파스칼 초보를 졸업했습니다! 🏆  
앞으로도 계속 코딩을 즐겨주세요!
