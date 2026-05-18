# Delphi와 RAD Studio 큰 그림

> 작성 기준일: 2026-05-19  
> 대상 독자: Delphi IDE를 처음 여는 입사 예정자 또는 입사 초기 개발자  
> 목표: Delphi가 어떤 도구이고, IDE 안에서 무엇을 보게 될지 큰 지도를 먼저 잡는다.

## 1. Delphi는 무엇인가?

Delphi는 Embarcadero가 제공하는 네이티브 애플리케이션 개발 도구이자, Object Pascal 계열 언어를 사용하는 개발 환경이다.

처음 들으면 조금 낯설 수 있지만, 실무에서 Delphi는 주로 이런 위치에 있다.

- Windows 데스크톱 업무 프로그램
- ERP, MES, POS, 물류, 생산, 회계, 영업관리 같은 사내 업무 시스템
- 오래 운영된 레거시 시스템의 유지보수
- 장비, 바코드, 프린터, 시리얼 통신, 로컬 DB와 가까운 프로그램
- 빠르게 화면을 만들고 이벤트 코드를 붙이는 업무용 프로그램

웹 프론트엔드처럼 브라우저 안에서 돌아가는 프로그램을 만드는 도구라기보다, Windows 실행 파일을 만들어 현장 PC나 사무실 PC에서 직접 실행하는 프로그램을 만드는 도구라고 보는 편이 쉽다.

Delphi의 강점은 "화면을 눈으로 배치하고, 이벤트에 코드를 붙이고, 컴파일해서 바로 실행 파일을 만든다"는 흐름이다. 이 감각은 Visual Basic, WinForms, C# WPF, Java Swing을 써본 사람에게는 꽤 익숙하게 느껴질 수 있다.

## 2. RAD Studio와 Delphi IDE의 관계

`RAD Studio`는 Embarcadero의 통합 개발 환경 제품군 이름이다. RAD Studio 안에는 Delphi와 C++Builder가 들어갈 수 있다.

공식 문서에서는 RAD Studio IDE가 여러 "personality"를 가질 수 있다고 설명한다. Delphi는 Object Pascal을 쓰고, C++Builder는 C++를 쓴다. 즉 같은 IDE 껍데기 안에서 어떤 언어/제품을 쓰느냐에 따라 Delphi 개발 환경이 되기도 하고 C++Builder 개발 환경이 되기도 한다.

입사 후 선배가 "RAD Studio 열어보세요"라고 말하면 대개 Delphi IDE를 열라는 뜻일 가능성이 높다. "Delphi 열어보세요"라고 해도 실제 실행 파일이나 바로가기 이름은 RAD Studio로 보일 수 있다.

| 용어 | 의미 | 처음 볼 때의 감각 |
| --- | --- | --- |
| Delphi | Object Pascal 기반 개발 도구/언어/제품 이름 | 내가 주로 코딩할 대상 |
| RAD Studio | Delphi, C++Builder 등을 포함하는 IDE 제품군 | IDE 전체 이름 |
| C++Builder | C++ 기반 RAD 개발 도구 | Delphi와 같은 IDE 계열의 다른 언어 |
| Object Pascal | Delphi에서 쓰는 주 언어 | `.pas` 파일 안에서 만나는 문법 |
| IDE | 코딩, 화면 설계, 빌드, 디버깅을 하는 통합 개발 환경 | 매일 열어놓을 작업장 |

## 3. RAD라는 말의 의미

RAD는 Rapid Application Development, 즉 빠른 애플리케이션 개발을 뜻한다.

Delphi를 이해할 때 이 단어는 중요하다. Delphi는 텍스트 코드만으로 모든 것을 쌓아 올리는 방식이 아니라, 화면을 시각적으로 만들고 속성을 바꾸고 이벤트를 연결하는 방식에 강하다.

예를 들어 버튼 하나를 만드는 과정은 대략 이렇게 진행된다.

1. `Tool Palette`에서 `TButton`을 고른다.
2. Form 위에 버튼을 올린다.
3. `Object Inspector`에서 `Caption`을 바꾼다.
4. `OnClick` 이벤트를 더블클릭한다.
5. 자동 생성된 메서드 안에 코드를 쓴다.
6. 실행해서 버튼을 눌러본다.

이 흐름이 Delphi의 기본 리듬이다. 처음에는 낯설어도, 이 리듬만 잡으면 IDE의 많은 기능이 한 줄로 연결된다.

## 4. Object Pascal은 어떤 언어인가?

Delphi의 주 언어는 Object Pascal이다. Pascal 문법에 객체지향 기능과 현대적인 라이브러리, 컴포넌트 모델이 얹힌 언어라고 보면 된다.

간단한 이벤트 코드는 이런 모양을 가진다.

```pascal
procedure TMainForm.ButtonSaveClick(Sender: TObject);
begin
  ShowMessage('저장 버튼을 눌렀습니다.');
end;
```

처음 볼 때 중요한 것은 세 가지다.

- `procedure`는 값을 반환하지 않는 메서드다.
- `begin ... end`가 코드 블록이다.
- `TMainForm.ButtonSaveClick`은 `TMainForm` 클래스에 속한 이벤트 처리 메서드다.

C#, Java, JavaScript에 익숙하다면 중괄호 대신 `begin/end`를 쓰고, 변수 선언과 타입 표기 위치가 다르다는 점이 먼저 눈에 들어온다. 그러나 실무 유지보수에서는 문법 전체보다 "폼, 컴포넌트, 이벤트, DB 쿼리, 공통 함수"를 읽는 능력이 먼저 필요하다.

## 5. VCL과 FireMonkey

Delphi에서 화면을 만드는 대표 프레임워크는 `VCL`과 `FireMonkey`다.

| 구분 | VCL | FireMonkey |
| --- | --- | --- |
| 주 용도 | Windows 데스크톱 애플리케이션 | 멀티 플랫폼 애플리케이션 |
| 성격 | 오래되고 안정적인 Windows 중심 프레임워크 | Windows, macOS, iOS, Android 등을 고려한 프레임워크 |
| 실무 빈도 | 국내 업무용/레거시 시스템에서 흔함 | 모바일/멀티 플랫폼 요구가 있을 때 사용 |
| 처음 학습 우선순위 | 높음 | 회사가 쓰는 경우에 맞춰 학습 |

입사해서 기존 ERP, MES, 회계, 생산관리 프로그램을 만진다면 VCL일 가능성이 높다. 따라서 이 문서 시리즈는 VCL을 기본 예제로 삼는다.

FireMonkey가 중요하지 않다는 뜻은 아니다. 다만 첫 달의 목표가 "기존 회사 프로젝트를 열고 작은 수정을 할 수 있다"라면 VCL 화면과 이벤트 흐름이 먼저다.

## 6. Delphi 프로젝트에서 보게 될 파일

처음 프로젝트 폴더를 열면 낯선 확장자가 많다.

| 파일 | 의미 | 처음 확인할 것 |
| --- | --- | --- |
| `.dpr` | 프로그램 시작점 | 어떤 Form을 먼저 만드는지 |
| `.dproj` | 프로젝트 설정 파일 | Target Platform, Search Path, 빌드 설정 |
| `.pas` | Pascal 코드 파일 | 클래스, 이벤트, 업무 로직 |
| `.dfm` | VCL Form 디자인 파일 | 화면에 어떤 컴포넌트가 올라갔는지 |
| `.res` | 리소스 파일 | 아이콘, 버전 정보 등 |
| `.dpk` | 패키지 프로젝트 | 공통 컴포넌트/라이브러리 패키지 |

Delphi는 `.pas` 코드와 `.dfm` 화면 파일이 한 쌍으로 움직이는 경우가 많다. 버튼 위치를 바꾸거나 컴포넌트 속성을 바꾸면 `.dfm`이 바뀐다. 이벤트 코드를 수정하면 `.pas`가 바뀐다.

처음에는 `.dfm`을 손으로 직접 고치려고 하지 않는 편이 좋다. IDE의 Form Designer에서 바꾼 뒤 변경 내용을 확인하는 습관을 들이는 것이 안전하다.

## 7. IDE에서 자주 만나는 창

Delphi IDE는 처음 열면 복잡해 보이지만, 매일 보는 창은 정해져 있다.

| 창 | 역할 |
| --- | --- |
| Project Manager | 프로젝트와 unit 목록을 관리한다. |
| Tool Palette | Form에 올릴 컴포넌트를 찾는다. |
| Object Inspector | 선택한 Form/컴포넌트의 속성과 이벤트를 수정한다. |
| Form Designer | 화면을 시각적으로 배치한다. |
| Code Editor | Pascal 코드를 작성한다. |
| Structure View | Form 안의 컴포넌트 구조를 본다. |
| Messages | 빌드 오류와 경고를 확인한다. |
| Call Stack, Watches, Local Variables | 디버깅 중 흐름과 값을 추적한다. |

처음에는 모든 메뉴를 외우려 하지 않아도 된다. `Project Manager`, `Object Inspector`, `Form Designer`, `Code Editor`, `Messages`만 편하게 오가도 첫 관문은 넘은 것이다.

## 8. Delphi가 업무 시스템에 오래 남아 있는 이유

Delphi는 한때 Windows 업무 프로그램 개발에서 매우 강력한 선택지였다. 지금도 많은 회사에는 Delphi로 만든 프로그램이 남아 있다.

그 이유는 단순하다.

- 실행 파일이 빠르고 배포가 비교적 단순하다.
- Windows UI를 빠르게 만들 수 있다.
- DB 중심 업무 프로그램과 궁합이 좋다.
- 오래된 코드가 아직 안정적으로 돈을 벌고 있다.
- 현장 장비, 프린터, 바코드, 파일, 로컬 네트워크와 붙은 프로그램이 많다.

실무에서는 "왜 아직 Delphi를 쓰지?"보다 "이 시스템이 회사 업무의 어느 부분을 책임지고 있나?"가 더 중요하다. 오래된 도구라도 업무의 혈관을 잡고 있으면 아주 중요한 시스템이다.

## 9. 다른 언어 경험과 비교하기

| 경험 | Delphi에서 연결되는 개념 |
| --- | --- |
| C# WinForms | Form, Control, Event, Property 개념이 비슷하다. |
| Java Swing | 화면 객체와 이벤트 리스너 흐름이 비슷하다. |
| Spring Boot | 구조는 다르지만, DB/업무 로직/레이어 분리는 실무적으로 연결된다. |
| JavaScript DOM 이벤트 | 버튼 클릭 이벤트에 핸들러를 붙이는 감각이 비슷하다. |
| SQL 개발 | Delphi 업무 프로그램에서는 SQL 읽기 능력이 매우 중요하다. |

Delphi를 잘 배우려면 "문법 암기"보다 "화면 이벤트가 어떤 DB 작업으로 이어지는지"를 추적하는 힘이 더 중요하다.

예를 들어 저장 버튼을 눌렀을 때 다음 흐름을 따라갈 수 있어야 한다.

```text
ButtonSaveClick
-> ValidateInput
-> BuildSql or Query.ParamByName(...)
-> Transaction Commit
-> Grid Refresh
-> Message 표시
```

이 흐름을 읽기 시작하면 Delphi 코드는 갑자기 훨씬 덜 낯설어진다.

## 10. 처음 한 달의 목표

처음 한 달에 Delphi를 "마스터"하려고 하면 지친다. 목표를 이렇게 잡는 것이 현실적이다.

1. 회사 프로젝트를 열 수 있다.
2. 메인 Form과 주요 메뉴 Form을 찾을 수 있다.
3. 버튼/그리드/입력창의 이벤트 코드를 찾을 수 있다.
4. 브레이크포인트를 걸고 값 변화를 볼 수 있다.
5. 작은 문구, 조건문, SQL 파라미터, 화면 속성 수정 정도를 할 수 있다.
6. 빌드 오류가 나면 어느 설정을 봐야 하는지 감을 잡는다.
7. 내가 모르는 것이 IDE 문제인지, 코드 문제인지, DB/환경 문제인지 구분하기 시작한다.

Delphi는 첫인상이 조금 오래된 도구처럼 느껴질 수 있다. 하지만 업무 시스템의 입장에서 보면, 오래된 것은 곧 많이 검증되었다는 뜻이기도 하다. 차분히 화면, 이벤트, DB, 빌드 흐름을 연결하면 생각보다 빨리 손에 들어온다.

## 바로 해볼 실습

1. Delphi IDE 또는 RAD Studio를 실행한다.
2. 새 VCL Forms Application을 만든다.
3. Form에 Button과 Label을 하나씩 올린다.
4. Button의 `Caption`을 `확인`으로 바꾼다.
5. `OnClick` 이벤트에 `Label1.Caption := '버튼 클릭';`을 작성한다.
6. 실행해서 버튼을 눌러본다.

## 입사 후 확인할 질문

1. 회사 프로젝트는 VCL인가, FireMonkey인가?
2. 회사에서 쓰는 Delphi 정확한 버전과 에디션은 무엇인가?
3. Target Platform은 Win32인가, Win64인가?
4. 공통 컴포넌트나 외부 패키지는 어떤 것을 쓰는가?
5. 빌드는 개인 PC에서 하는가, 별도 빌드 PC/서버에서 하는가?
6. 운영 배포는 누가, 어떤 절차로 하는가?

## 자주 막히는 지점

- IDE 이름은 RAD Studio인데 문서는 Delphi라고 해서 헷갈린다.
- `.pas`만 보면 화면이 어디 있는지 모르겠다.
- `.dfm`이 코드처럼 보여서 직접 수정하고 싶어진다.
- Object Inspector에서 속성과 이벤트 탭을 구분하지 못한다.
- 빌드 오류가 코드 오류인지 경로/패키지 오류인지 구분이 안 된다.
- Win32/Win64 설정 차이를 놓친다.

## 참고 자료

- [RAD Studio First Look and Welcome Page](https://docwiki.embarcadero.com/RADStudio/Athens/en/First_Look_and_Welcome_Page)
- [RAD Studio 12 Athens DocWiki](https://docwiki.embarcadero.com/RADStudio/Athens/en/Main_Page)
- [Delphi FAQ For Application Developers](https://www.embarcadero.com/delphi-frequently-asked-questions)

