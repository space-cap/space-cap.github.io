# Delphi 용어사전

> 작성 기준일: 2026-05-19  
> 대상 독자: Delphi IDE와 기존 프로젝트에서 낯선 용어를 빠르게 확인하려는 사람  
> 목표: 입사 초기 자주 만나는 Delphi, IDE, VCL, DB, 빌드, 배포 용어를 짧게 정리한다.

## A

### ADO

Windows 기반 DB 접근 기술. 오래된 Delphi 프로젝트에서 볼 수 있다. FireDAC 이전 또는 병행 사용되는 경우가 있다.

### Align

컴포넌트를 부모 영역의 위, 아래, 왼쪽, 오른쪽, 전체 영역에 붙이는 VCL 속성. `alTop`, `alClient` 등을 사용한다.

### Anchors

창 크기가 바뀔 때 컴포넌트가 어느 가장자리에 고정될지 정하는 속성.

## B

### BDE

Borland Database Engine. 매우 오래된 Delphi DB 기술. 레거시 프로젝트에서 만날 수 있으나 신규 개발에서는 보통 피한다.

### BPL

Borland Package Library. Delphi runtime package 결과물. 실행 시 필요한 경우 운영 PC에도 배포해야 한다.

### Build

프로젝트 전체를 다시 컴파일해 산출물을 만드는 작업. 배포 전에는 보통 Build를 수행한다.

### Build Configuration

Debug, Release 같은 빌드 설정 묶음. 같은 코드라도 구성에 따라 옵션과 결과가 달라질 수 있다.

## C

### Caption

Form, Button, Label 등에 사용자에게 보이는 글자. 코드에서 부르는 이름인 `Name`과 다르다.

### Class Completion

클래스 선언부에 작성한 메서드의 구현부를 IDE가 자동 생성하는 기능. 보통 `Ctrl+Shift+C`.

### Code Completion

자동 완성 기능. 보통 `Ctrl+Space`. property, method, 변수 후보를 보여준다.

### Code Editor

`.pas`, `.dpr`, `.dpk` 같은 소스 코드를 편집하는 IDE 영역.

### Code Insight

Code Completion, Parameter Completion, Tooltip, Find Declaration 등 코드 편집 보조 기능의 묶음.

### Compile

변경된 파일 중심으로 컴파일하는 작업. Build보다 빠를 수 있다.

### Component

Delphi Form에 올리거나 코드에서 사용하는 구성 요소. Button, Edit, Query, Timer 등이 모두 컴포넌트다.

## D

### DataModule

화면은 없지만 DB 연결, Query, 공통 로직 등을 담는 컨테이너.

### DataSource

Dataset과 DB-aware 화면 컴포넌트를 연결하는 컴포넌트. `TDataSource`.

### DCP

Delphi Compiled Package. 패키지 컴파일 정보 파일.

### DCU

Delphi Compiled Unit. `.pas`가 컴파일된 결과물. 오래된 DCU가 남아 빌드 문제를 만들 수 있다.

### Debug

디버깅용 빌드 구성. 디버그 정보를 포함하고 최적화가 낮은 경우가 많다.

### Delphi

Object Pascal 기반의 네이티브 애플리케이션 개발 도구이자 언어/제품 이름.

### DFM

Delphi Form 디자인 파일. 컴포넌트 위치, 속성, 이벤트 연결 정보가 들어간다.

### DPK

Delphi Package 소스 파일. 패키지 프로젝트를 정의한다.

### DPR

Delphi Project source. 프로그램 시작점. `Application.CreateForm`, `Application.Run` 등을 볼 수 있다.

### DPROJ

Delphi 프로젝트 설정 파일. XML 기반이며 IDE가 관리한다. Search Path, Build Configuration, Target Platform 등이 들어간다.

## E

### Event

클릭, 값 변경, Form 생성 같은 사건. `OnClick`, `OnCreate`, `OnChange` 등이 있다.

### Event Handler

이벤트가 발생했을 때 실행되는 메서드. 예: `btnSaveClick`.

### ExecSQL

결과 집합을 반환하지 않는 SQL 명령을 실행할 때 쓰는 메서드. INSERT, UPDATE, DELETE 등에 사용.

## F

### FieldByName

Dataset에서 필드 이름으로 값을 읽는 메서드.

### FireDAC

Delphi/RAD Studio의 데이터 액세스 프레임워크. 여러 DBMS 연결과 SQL 실행을 지원한다.

### FMX

FireMonkey의 약칭. 멀티 플랫폼 UI 프레임워크.

### Form

Windows 애플리케이션의 화면/창 단위. VCL에서는 `TForm` 기반이다.

### Form Designer

Form을 시각적으로 배치하고 수정하는 IDE 영역.

## G

### GetIt

RAD Studio/Delphi에서 패키지나 기능을 설치하는 패키지 관리 도구.

### Git

버전 관리 시스템. Delphi 프로젝트에서는 `.pas`, `.dfm`, `.dproj` 변경 확인이 특히 중요하다.

### Group Project / Project Group

여러 프로젝트를 묶은 단위. 파일 확장자는 `.groupproj`.

## I

### IDE

Integrated Development Environment. 코드 편집, 화면 설계, 빌드, 디버깅을 하는 통합 개발 환경.

### Implementation

unit에서 실제 구현 코드가 들어가는 영역.

### Interface

unit에서 외부에 공개되는 선언 영역.

## L

### Library Path

IDE/컴파일러가 공통 라이브러리와 unit을 찾는 경로.

### Local Variables

디버깅 중 현재 메서드의 지역 변수를 보여주는 창.

## N

### Name

컴포넌트의 코드상 이름. `Caption`이나 `Text`와 다르다.

### Non-Visual Component

실행 화면에는 보이지 않지만 기능을 제공하는 컴포넌트. `TTimer`, `TFDQuery`, `TOpenDialog` 등.

## O

### Object Inspector

선택한 Form/컴포넌트의 Properties와 Events를 수정하는 IDE 창.

### Open

SELECT처럼 결과 집합을 반환하는 Query를 실행할 때 사용하는 메서드.

## P

### Package

unit과 컴포넌트를 묶어 재사용/배포하는 단위. `.dpk`, `.bpl`, `.dcp`와 연결된다.

### ParamByName

SQL 파라미터에 값을 넣을 때 사용하는 메서드.

### Pascal / Object Pascal

Delphi에서 사용하는 언어. `begin/end`, `procedure`, `function`, `class` 등을 사용한다.

### Project Manager

프로젝트, unit, Form, Build Configuration, Target Platform을 확인하는 IDE 창.

### Property

객체나 컴포넌트의 속성. `Caption`, `Text`, `Enabled`, `Visible` 등이 있다.

## Q

### Query

SQL 실행 단위. FireDAC에서는 `TFDQuery`를 자주 사용한다.

## R

### RAD

Rapid Application Development. 시각적 화면 설계와 빠른 애플리케이션 개발 방식을 뜻한다.

### RAD Studio

Delphi, C++Builder 등을 포함하는 Embarcadero의 통합 개발 환경 제품군.

### Release

배포용 빌드 구성. 최적화와 디버그 정보 설정이 Debug와 다를 수 있다.

### Runtime Package

프로그램 실행 시 필요한 패키지. BPL 배포와 연결된다.

## S

### Search Path

현재 프로젝트가 unit을 찾는 경로. `Unit not found` 오류와 자주 연결된다.

### ShowMessage

간단한 메시지 창을 표시하는 함수. 학습용 예제에서 자주 사용한다.

### Step Into

디버깅 중 호출한 메서드 안으로 들어가는 실행 명령.

### Step Over

디버깅 중 현재 줄을 실행하되 호출 메서드 내부로 들어가지 않는 명령.

## T

### TabOrder

Tab 키로 입력 포커스가 이동하는 순서.

### Target Platform

빌드 대상 플랫폼. VCL에서는 주로 Win32, Win64를 확인한다.

### TDataSource

Dataset과 화면 컴포넌트를 연결하는 컴포넌트.

### TFDConnection

FireDAC DB 연결 컴포넌트.

### TFDQuery

FireDAC SQL 실행 컴포넌트.

### Tool Palette

Form에 올릴 컴포넌트를 찾는 IDE 창.

### Transaction

DB 작업을 하나의 단위로 묶어 Commit 또는 Rollback하는 처리.

## U

### Unit

Delphi의 코드 모듈. `.pas` 파일 하나가 보통 하나의 unit이다.

### Uses

다른 unit을 참조하는 절. Java/C#의 import/using과 비슷하다.

## V

### VCL

Visual Component Library. Windows 데스크톱 애플리케이션 개발에 많이 쓰이는 Delphi UI 프레임워크.

### Visual Component

실행 화면에 보이는 컴포넌트. `TButton`, `TEdit`, `TLabel` 등.

## W

### Watch

디버깅 중 지정한 변수나 표현식 값을 계속 확인하는 기능.

### Win32 / Win64

Windows 32비트/64비트 Target Platform. 외부 DLL, DB 드라이버, BPL 비트 수와 맞아야 한다.

