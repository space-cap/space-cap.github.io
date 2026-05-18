# Delphi IDE 단축키 치트시트

> 작성 기준일: 2026-05-19  
> 대상 독자: Delphi IDE에서 자주 쓰는 조작을 빠르게 익히려는 사람  
> 주의: 단축키는 IDE 버전, 키맵, 회사 설정에 따라 달라질 수 있다. 여기서는 일반적으로 자주 쓰이는 기본 단축키와 메뉴 경로를 함께 정리한다.

## 1. 처음 외울 단축키 10개

처음에는 이 정도만 익혀도 충분하다.

| 작업 | 단축키 | 의미 |
| --- | --- | --- |
| 실행 | `F9` | 현재 프로젝트 실행 |
| 중단점 설정/해제 | `F5` 또는 줄 왼쪽 클릭 | 브레이크포인트 토글 |
| Step Over | `F8` | 한 줄 실행, 메서드 안으로 들어가지 않음 |
| Step Into | `F7` | 호출한 메서드 안으로 들어감 |
| 자동 완성 | `Ctrl+Space` | Code Completion |
| 파라미터 힌트 | `Ctrl+Shift+Space` | Parameter Completion |
| 선언/구현 생성 | `Ctrl+Shift+C` | Class Completion |
| 저장 | `Ctrl+S` | 현재 파일 저장 |
| 모두 저장 | `Shift+Ctrl+S` | 프로젝트 관련 파일 전체 저장 |
| 찾기 | `Ctrl+F` | 현재 파일 검색 |

회사 IDE 키맵이 다르면 메뉴에서 같은 기능을 찾아 확인한다.

## 2. 실행과 디버깅

| 작업 | 단축키 | 메뉴/설명 |
| --- | --- | --- |
| Run | `F9` | 프로그램 실행 |
| Pause | IDE/툴바 확인 | 실행 중 일시 중지 |
| Stop/Reset | `Ctrl+F2`인 키맵이 많음 | 디버그 세션 종료 |
| Toggle Breakpoint | `F5` 또는 에디터 왼쪽 여백 클릭 | 중단점 설정/해제 |
| Step Into | `F7` | 메서드 내부로 들어감 |
| Step Over | `F8` | 메서드 내부로 들어가지 않고 다음 줄 |
| Step Out | `Shift+F8`인 키맵이 많음 | 현재 메서드를 빠져나감 |
| Run to Cursor | `F4`인 키맵이 많음 | 커서 위치까지 실행 |

디버깅 중 가장 많이 쓰는 조합:

```text
F5로 중단점 설정
-> F9로 실행
-> F8로 흐름 확인
-> 필요한 메서드에서 F7
-> 너무 깊이 들어가면 Step Out
```

## 3. Code Insight

| 작업 | 단축키 | 설명 |
| --- | --- | --- |
| Code Completion | `Ctrl+Space` | 속성/메서드/변수 자동 완성 |
| Parameter Completion | `Ctrl+Shift+Space` | 함수 파라미터 힌트 |
| Class Completion | `Ctrl+Shift+C` | 선언한 메서드 구현부 자동 생성 |
| Find Declaration | `Ctrl+Click` | 선언 위치로 이동 |
| Tooltip Insight | 마우스 올리기 | 심볼/타입 정보 확인 |

예:

```pascal
edtName.
```

여기서 `Ctrl+Space`를 누르면 `Text`, `Clear`, `SetFocus` 같은 후보를 볼 수 있다.

## 4. 검색과 이동

| 작업 | 단축키 | 설명 |
| --- | --- | --- |
| 현재 파일 찾기 | `Ctrl+F` | 현재 파일 문자열 검색 |
| 다음 찾기 | `F3`인 키맵이 많음 | 다음 검색 결과 |
| 파일 전체 검색 | IDE 메뉴 확인 | Find in Files |
| 줄 이동 | `Ctrl+G`인 키맵이 많음 | 특정 줄 번호로 이동 |
| 선언으로 이동 | `Ctrl+Click` | 메서드/클래스 선언 찾기 |

대형 프로젝트에서는 IDE 검색과 외부 검색 도구를 같이 쓰는 것이 좋다. 특히 `.dfm`의 `Caption`, `OnClick`도 검색 대상에 넣는다.

## 5. 파일과 프로젝트

| 작업 | 단축키 | 설명 |
| --- | --- | --- |
| 현재 파일 저장 | `Ctrl+S` | 현재 탭 저장 |
| 모두 저장 | `Shift+Ctrl+S` | 프로젝트/폼/유닛 전체 저장 |
| 새 파일/프로젝트 | `Ctrl+N` 계열 또는 메뉴 | IDE 키맵 확인 |
| 파일 열기 | `Ctrl+O` 계열 또는 메뉴 | 파일 열기 |
| 프로젝트 열기 | `File > Open Project` | `.dproj` 열기 |
| 프로젝트 그룹 열기 | `File > Open Project Group` | `.groupproj` 열기 |

Delphi에서는 `Save All` 습관이 중요하다. Form을 바꾸면 `.dfm`, 코드를 바꾸면 `.pas`, 설정을 바꾸면 `.dproj`가 함께 바뀔 수 있다.

## 6. Form Designer와 Object Inspector

| 작업 | 방법 | 설명 |
| --- | --- | --- |
| 컴포넌트 선택 | Form Designer에서 클릭 | Object Inspector 대상 변경 |
| 이벤트 생성 | Events 탭에서 더블클릭 | 이벤트 핸들러 생성 |
| 속성 필터 | Object Inspector 필터 입력 | `Caption`, `OnClick` 빠르게 찾기 |
| Form/Code 전환 | 탭 또는 메뉴 사용 | IDE 버전에 따라 다름 |
| 컴포넌트 복사 | `Ctrl+C`, `Ctrl+V` | 이벤트 연결도 복사될 수 있음 |
| 컴포넌트 삭제 | `Delete` | `.dfm`과 `.pas` 변경 확인 |

컴포넌트를 복사한 뒤에는 반드시 `Name`, `Caption`, `OnClick`, `TabOrder`를 확인한다.

## 7. 빌드 관련

| 작업 | 메뉴/단축키 | 설명 |
| --- | --- | --- |
| Compile | `Project > Compile` | 변경 파일 중심 컴파일 |
| Build | `Project > Build` | 전체 빌드 |
| Clean | `Project > Clean` | 빌드 산출물 정리 |
| Run | `F9` | 빌드 후 실행 |
| Project Options | `Project > Options` | 빌드/경로/패키지 설정 |

단축키보다 중요한 것은 현재 Active Project, Build Configuration, Target Platform을 확인하는 습관이다.

## 8. 디버깅 창

| 창 | 여는 위치 | 용도 |
| --- | --- | --- |
| Watches | Debug 관련 View 메뉴 | 지정한 변수 감시 |
| Local Variables | Debug 관련 View 메뉴 | 현재 메서드 지역 변수 |
| Call Stack | Debug 관련 View 메뉴 | 호출 흐름 |
| Breakpoints | Debug 관련 View 메뉴 | 중단점 목록 |
| Evaluate/Modify | Debug 메뉴/컨텍스트 메뉴 | 표현식 확인/수정 |

디버깅 중에는 `Local Variables`, `Watches`, `Call Stack` 세 창만 잘 써도 충분하다.

## 9. 외워야 할 것보다 중요한 것

단축키는 생산성을 올려주지만, 처음부터 전부 외울 필요는 없다.

먼저 익힐 순서:

1. `F9`, `F5`, `F8`, `F7`
2. `Ctrl+Space`, `Ctrl+Shift+Space`, `Ctrl+Shift+C`
3. `Ctrl+F`, `Ctrl+Click`
4. `Ctrl+S`, `Shift+Ctrl+S`
5. Find in Files와 Project Options 메뉴 위치

## 입사 후 확인할 질문

1. 회사 IDE 키맵은 기본값인가요?
2. 선배들이 자주 쓰는 단축키가 있나요?
3. Form/Code 전환 단축키를 따로 쓰나요?
4. Find in Files보다 외부 검색 도구를 쓰나요?
5. 디버깅 창 레이아웃 표준이 있나요?

