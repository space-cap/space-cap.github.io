# 오브젝트 파스칼 (Object Pascal) 초보자 가이드

환영합니다! 🎉  
이 가이드는 프로그래밍을 처음 접하거나 오브젝트 파스칼을 새롭게 배우고자 하는 초보자분들을 위해 아주 쉽게 작성되었습니다.

차근차근 아래 목차를 따라 학습하며 여러분만의 멋진 프로그램을 만들어 보세요!

---

## 📖 학습 목차

### 🌱 기초 편

1. [**1장. 오브젝트 파스칼 시작하기**](01_introduction_and_setup.md)
   - 언어 소개 및 특징
   - IDE 선택 가이드 (Lazarus, Delphi, VS Code 비교)
   - 설치 방법 및 첫 번째 Hello World 실행

2. [**2장. 변수와 데이터 타입**](02_variables_and_types.md)
   - 정보를 담는 상자: 변수와 상수
   - 자주 쓰는 기본 타입 (Integer, Double, String, Boolean)

3. [**3장. 제어문 (조건문과 반복문)**](03_control_structures.md)
   - 조건문: If-Then-Else, Case
   - 반복문: For, While

4. [**4장. 함수와 프로시저**](04_procedures_and_functions.md)
   - 프로시저(Procedure)와 함수(Function)의 차이
   - 매개변수 넘기기 (값 전달 vs `var` 참조 전달)

5. [**5장. 객체 지향 프로그래밍 기초**](05_object_oriented_programming.md)
   - 클래스와 객체의 개념
   - 메모리 관리 (Create, Free, try...finally)

---

### 🌿 중급 편

6. [**6장. 배열과 컬렉션**](06_arrays_and_collections.md)
   - 정적/동적 배열
   - 2차원 배열
   - TStringList 활용

7. [**7장. 문자열 처리**](07_string_handling.md)
   - 문자열 함수 총정리 (Copy, Pos, Trim, UpperCase 등)
   - 숫자 ↔ 문자열 변환 (IntToStr, StrToInt 등)
   - 실습: 이메일 유효성 검사

8. [**8장. 예외 처리**](08_exception_handling.md)
   - try ... except (에러 잡기)
   - try ... finally (반드시 실행되는 코드)
   - 자주 만나는 에러 종류

9. [**9장. 유닛(Unit)과 프로그램 구조**](09_units_and_structure.md)
   - 유닛이란? (코드 파일 나누기)
   - `uses`로 유닛 가져오기
   - 나만의 유닛 만들기 (interface / implementation)

10. [**10장. 상속과 다형성 (OOP 심화)**](10_inheritance_and_polymorphism.md)
    - 부모 클래스에서 자식 클래스로 상속
    - 메서드 오버라이딩 (virtual / override)
    - 다형성: 하나의 코드로 여러 타입 처리

---

### 🌳 실전 편

11. [**11장. 파일 입출력**](11_file_io.md)
    - 텍스트 파일 읽기/쓰기
    - TStringList로 간편하게 파일 다루기
    - 유용한 파일 관련 함수들

12. [**12장. GUI 프로그래밍 기초**](12_gui_programming.md)
    - Lazarus 폼 디자이너 사용법
    - 버튼, 입력창, 레이블 등 주요 컴포넌트
    - 이벤트 처리 (OnClick 등)
    - 실습: 간단한 계산기 만들기

13. [**13장. 디버깅 방법**](13_debugging.md)
    - 에러의 세 가지 종류 (컴파일/런타임/논리)
    - IDE 디버거 활용법 (중단점, F8 단계 실행)
    - 에러를 줄이는 좋은 습관

14. [**14장. 실전 미니 프로젝트: 학생 성적 관리 프로그램**](14_mini_project.md)
    - 클래스 설계부터 전체 프로그램 완성까지
    - 1~13장에서 배운 모든 개념 종합 활용

---

## 💡 학습 팁

- 📌 **단순히 눈으로만 읽지 마세요!** 예제 코드를 직접 타이핑하고 `F9`를 눌러 실행해보는 것이 가장 빠른 학습법입니다.
- 📌 **에러가 나도 괜찮습니다!** 에러를 해결하는 과정에서 가장 많이 배웁니다.
- 📌 **1장부터 순서대로** 학습하는 것을 권장합니다. 각 장은 앞 장의 내용을 기반으로 합니다.
- 📌 **14장 미니 프로젝트**는 이 가이드의 최종 목표입니다. 혼자 힘으로 완성해보세요! 💪
