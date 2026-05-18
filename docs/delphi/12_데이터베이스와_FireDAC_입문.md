# 데이터베이스와 FireDAC 입문

> 작성 기준일: 2026-05-19  
> 대상 독자: Delphi 업무 프로그램에서 DB 연결과 조회/저장 코드를 처음 읽는 사람  
> 목표: FireDAC의 기본 구성 요소와 DB 코드 읽는 법, 운영 DB 주의사항을 익힌다.

## 1. Delphi 업무 프로그램은 DB를 많이 만난다

Delphi로 만든 ERP, MES, 회계, 물류, 생산관리 프로그램은 대부분 DB 중심이다.

화면 버튼 하나를 눌러도 실제 흐름은 보통 이렇다.

```text
버튼 클릭
-> 입력값 검증
-> SQL 파라미터 설정
-> 조회/저장 실행
-> 트랜잭션 Commit/Rollback
-> 화면 Grid 새로고침
```

따라서 Delphi를 익힐 때 DB 코드를 읽는 능력은 매우 중요하다. Pascal 문법을 조금 몰라도 DB 흐름을 읽으면 업무를 이해할 수 있고, 반대로 문법을 알아도 DB 흐름을 모르면 실무 수정이 어렵다.

## 2. FireDAC이란?

FireDAC은 Delphi/RAD Studio에서 제공하는 데이터 액세스 프레임워크다.

공식 문서에서는 FireDAC을 여러 DBMS를 지원하는 데이터 액세스 엔진으로 설명한다. `TFDConnection`, `TFDQuery`, `TFDTransaction` 같은 컴포넌트를 통해 DB 연결, SQL 실행, 트랜잭션 처리를 한다.

다만 회사 프로젝트가 항상 FireDAC을 쓰는 것은 아니다.

| 기술 | 설명 |
| --- | --- |
| FireDAC | 현대 Delphi에서 많이 쓰는 DB 프레임워크 |
| ADO | 오래된 Windows/DB 프로젝트에서 볼 수 있음 |
| dbExpress | 기존 프로젝트에서 남아 있을 수 있음 |
| BDE | 매우 오래된 레거시에서 볼 수 있음 |
| 자체 컴포넌트 | 회사가 만든 DB 래퍼 |

입사 후에는 "우리 프로젝트는 FireDAC을 쓰나요?"를 먼저 확인한다.

## 3. FireDAC 기본 컴포넌트

자주 보는 컴포넌트:

| 컴포넌트 | 역할 |
| --- | --- |
| `TFDConnection` | DB 연결 |
| `TFDQuery` | SQL 조회/저장 실행 |
| `TFDTransaction` | 트랜잭션 제어 |
| `TDataSource` | Dataset과 화면 컴포넌트 연결 |
| `TDBGrid` | DB 데이터를 표로 표시 |
| `TFDStoredProc` | 저장 프로시저 실행 |
| `TFDPhys...DriverLink` | 특정 DB 드라이버 연결 보조 |

가장 기본 관계는 이렇다.

```text
TFDConnection
-> TFDQuery
-> TDataSource
-> TDBGrid
```

`TFDConnection`이 DB에 연결하고, `TFDQuery`가 SQL을 실행하고, `TDataSource`가 Query 결과와 화면 컴포넌트를 연결한다.

## 4. Connection

`TFDConnection`은 DB 접속 정보를 가진다.

공식 FireDAC 연결 문서는 connection definition이 DBMS 연결 파라미터를 지정하는 용도라고 설명한다. 디자인 타임 편집기에서 설정하면 `TFDConnection.Params`와 `DriverName`에 연결 정보가 들어간다.

확인할 속성:

| 속성 | 의미 |
| --- | --- |
| `DriverName` | 어떤 DB 드라이버를 쓰는지 |
| `Params` | 서버, DB명, 사용자, 기타 연결 정보 |
| `Connected` | 연결 여부 |
| `LoginPrompt` | 로그인 창 표시 여부 |
| `Transaction` | 기본 트랜잭션 연결 |

예:

```pascal
FDConnection1.DriverName := 'MSSQL';
FDConnection1.Params.Values['Server'] := 'localhost';
FDConnection1.Params.Values['Database'] := 'TestDB';
FDConnection1.Connected := True;
```

실무에서는 접속 정보가 코드에 직접 있지 않고 설정 파일, 레지스트리, 암호화 파일, 공통 로그인 모듈에 있을 수 있다.

## 5. Query

`TFDQuery`는 SQL을 실행하는 컴포넌트다.

조회 예:

```pascal
qryCustomer.Close;
qryCustomer.SQL.Text := 'select * from customer where customer_id = :customer_id';
qryCustomer.ParamByName('customer_id').AsString := CustomerId;
qryCustomer.Open;
```

저장/수정 예:

```pascal
qrySave.SQL.Text :=
  'update customer set customer_name = :customer_name where customer_id = :customer_id';
qrySave.ParamByName('customer_name').AsString := CustomerName;
qrySave.ParamByName('customer_id').AsString := CustomerId;
qrySave.ExecSQL;
```

차이:

| 메서드 | 용도 |
| --- | --- |
| `Open` | 결과 집합을 반환하는 SELECT |
| `ExecSQL` | INSERT, UPDATE, DELETE 등 결과 집합 없는 명령 |
| `Close` | 열린 Dataset 닫기 |
| `ParamByName` | SQL 파라미터 값 설정 |
| `FieldByName` | 조회 결과 필드 값 읽기 |

## 6. 파라미터를 써야 하는 이유

SQL 문자열에 값을 직접 붙이면 위험하다.

나쁜 예:

```pascal
qry.SQL.Text := 'select * from customer where name = ''' + edtName.Text + '''';
```

좋은 예:

```pascal
qry.SQL.Text := 'select * from customer where name = :name';
qry.ParamByName('name').AsString := edtName.Text;
```

파라미터를 쓰면 SQL Injection 위험을 줄이고, 따옴표/날짜/숫자 변환 문제를 줄일 수 있다.

## 7. FieldByName

조회 결과에서 값을 읽을 때 `FieldByName`을 자주 본다.

```pascal
CustomerName := qryCustomer.FieldByName('customer_name').AsString;
Quantity := qryCustomer.FieldByName('quantity').AsInteger;
OrderDate := qryCustomer.FieldByName('order_date').AsDateTime;
```

자주 보는 변환:

| 속성 | 의미 |
| --- | --- |
| `AsString` | 문자열 |
| `AsInteger` | 정수 |
| `AsFloat` | 실수 |
| `AsCurrency` | 금액 |
| `AsDateTime` | 날짜/시간 |
| `AsBoolean` | Boolean |

주의할 점:

- 필드명이 틀리면 런타임 오류가 난다.
- NULL 값 처리에 주의한다.
- DB 컬럼 타입과 Delphi 변환 타입이 맞아야 한다.

NULL 확인:

```pascal
if qryCustomer.FieldByName('phone').IsNull then
  Phone := ''
else
  Phone := qryCustomer.FieldByName('phone').AsString;
```

## 8. Dataset 반복

조회 결과 여러 건을 처리할 때 자주 보는 패턴:

```pascal
qryCustomer.First;
while not qryCustomer.Eof do
begin
  ProcessCustomer(qryCustomer.FieldByName('customer_id').AsString);
  qryCustomer.Next;
end;
```

읽는 법:

| 코드 | 의미 |
| --- | --- |
| `First` | 첫 번째 행으로 이동 |
| `Eof` | 마지막 다음 위치인지 확인 |
| `Next` | 다음 행으로 이동 |

`Next`를 빠뜨리면 무한 루프가 된다.

## 9. Transaction

트랜잭션은 여러 DB 작업을 하나의 작업 단위로 묶는다.

예:

```pascal
FDConnection1.StartTransaction;
try
  InsertOrder;
  InsertOrderItems;
  UpdateStock;
  FDConnection1.Commit;
except
  FDConnection1.Rollback;
  raise;
end;
```

의미:

| 명령 | 의미 |
| --- | --- |
| `StartTransaction` | 트랜잭션 시작 |
| `Commit` | 변경 확정 |
| `Rollback` | 변경 취소 |

업무 프로그램에서 저장 버튼은 트랜잭션과 강하게 연결된다. 주문 저장 중 품목 저장이 실패했다면 주문 헤더도 같이 취소되어야 할 수 있다.

## 10. DataSource와 DBGrid

화면에 DB 데이터를 보여줄 때 자주 보는 구조:

```text
TFDQuery -> TDataSource -> TDBGrid
```

설정 예:

| 컴포넌트 | 속성 | 값 |
| --- | --- | --- |
| `dsCustomer` | `DataSet` | `qryCustomer` |
| `grdCustomer` | `DataSource` | `dsCustomer` |

이렇게 연결하면 `qryCustomer.Open` 후 Grid에 데이터가 표시된다.

기존 Form을 볼 때 Grid가 어떤 Query와 연결되는지 확인하면 화면의 데이터 흐름을 빠르게 파악할 수 있다.

## 11. DataModule

DataModule은 화면 없는 컨테이너다. DB 연결, Query, 공통 로직을 모아두는 데 자주 쓰인다.

예:

```text
MainForm
-> CustomerDataModule
   -> FDConnection
   -> qryCustomer
   -> qrySave
```

DataModule이 있으면 Form에 Query가 직접 없을 수 있다. 이때는 Form 코드에서 `dmCustomer.qryCustomer` 같은 참조를 찾는다.

## 12. 연결 정보는 어디에 있을까?

회사 프로젝트의 DB 연결 정보는 여러 곳에 있을 수 있다.

| 위치 | 설명 |
| --- | --- |
| `.ini` 파일 | 서버, DB명, 사용자 정보 |
| XML/JSON 설정 | 환경별 설정 |
| 레지스트리 | 오래된 Windows 프로그램에서 사용 |
| 로그인 서버/API | 중앙 인증 |
| 암호화 파일 | DB 비밀번호 보호 |
| 소스 코드 | 오래된 프로젝트에서 발견 가능 |
| FireDAC connection definition | FDExplorer/설정 파일 기반 |

입사 초기에는 운영 DB 접속 정보를 절대 임의로 수정하지 않는다.

## 13. 개발 DB와 운영 DB

DB 개발에서 가장 중요한 안전 원칙:

> 내가 지금 연결한 DB가 개발 DB인지 운영 DB인지 먼저 확인한다.

확인 방법:

1. 서버 주소
2. DB명
3. 접속 계정
4. 화면 제목/상태바의 환경 표시
5. 설정 파일 경로
6. 테스트 데이터 존재 여부
7. 선배 개발자의 확인

운영 DB에 연결한 상태에서 `UPDATE`, `DELETE`, `ExecSQL`을 실행하면 실제 업무 데이터가 바뀔 수 있다.

## 14. CE와 DB 기능 제한

Community Edition은 DB 기능 범위가 유료 에디션과 다를 수 있다. 공식 FAQ에서는 CE가 로컬/임베디드 DB 연결 중심이며, 폭넓은 원격 DB 접속은 Enterprise/Architect 구매 대상으로 안내된다.

따라서 회사가 MS SQL Server, Oracle, MySQL, PostgreSQL 같은 원격 DB를 쓴다면 다음을 확인한다.

1. 회사 에디션이 무엇인가?
2. FireDAC 드라이버 사용 권한이 있는가?
3. 개발 PC에 DB 클라이언트/ODBC 드라이버가 설치되어 있는가?
4. 32비트/64비트 드라이버가 Target Platform과 맞는가?

## 15. DB 오류 읽기

자주 보는 오류 유형:

| 오류 | 원인 후보 |
| --- | --- |
| connection failed | 서버, 계정, 방화벽, 드라이버 문제 |
| invalid column name | 컬럼명 오타, DB 버전 차이 |
| table or view does not exist | 테이블명, 권한, 스키마 문제 |
| primary key violation | 중복 키 |
| foreign key violation | 참조 무결성 |
| transaction deadlock | 동시성 문제 |
| login failed | 계정/비밀번호/권한 문제 |

오류가 나면 SQL과 파라미터 값, 연결 DB, 실행 시점을 함께 확인한다.

## 바로 해볼 실습

1. 예제 프로젝트에 `TFDConnection`, `TFDQuery`, `TDataSource`, `TDBGrid`를 올려본다.
2. 로컬 테스트 DB 또는 샘플 DB 연결을 시도한다.
3. SELECT SQL을 `Open`으로 실행해본다.
4. 파라미터가 있는 SQL을 작성해본다.
5. `FieldByName`으로 값을 읽어 Label에 표시해본다.
6. 개발 DB와 운영 DB 구분 체크리스트를 개인 노트에 적는다.

## 입사 후 확인할 질문

1. 회사 프로젝트는 FireDAC을 쓰나요?
2. DBMS는 무엇인가요? MS SQL Server, Oracle, MySQL 등
3. 개발 DB/테스트 DB/운영 DB는 어떻게 구분하나요?
4. DB 접속 정보는 어디에 저장되나요?
5. 저장 로직은 Form에 있나요, DataModule/Service에 있나요?
6. 운영 DB 직접 접속 권한이 있나요?
7. 트랜잭션 처리 공통 규칙이 있나요?

## 자주 막히는 지점

- `Open`과 `ExecSQL`을 구분하지 못한다.
- SQL 문자열에 값을 직접 붙인다.
- `ParamByName` 이름과 SQL 파라미터 이름이 다르다.
- NULL 값을 바로 `AsString`, `AsInteger`로 읽는다.
- 개발 DB와 운영 DB를 혼동한다.
- 32비트/64비트 DB 드라이버를 Target Platform과 맞추지 않는다.

## 참고 자료

- [Setting up Connections - FireDAC](https://docwiki.embarcadero.com/RADStudio/en/Setting_up_Connections_%28FireDAC%29)
- [Executing Commands - FireDAC](https://docwiki.embarcadero.com/RADStudio/Athens/en/Executing_Commands_%28FireDAC%29)
- [Databases - FireDAC](https://docwiki.embarcadero.com/RADStudio/en/Databases_%28FireDAC%29)
- [TFDConnection API Documentation](https://docwiki.embarcadero.com/Libraries/Athens/en/FireDAC.Comp.Client.TFDConnection)

