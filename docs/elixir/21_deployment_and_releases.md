# 21. 배포 전략: 코드를 세상 밖으로

개발 환경에서의 작업을 마쳤다면, 이제 실제 운영 서버(Production)에 코드를 올릴 차례입니다. Elixir는 **Releases**라는 매우 효율적이고 독립적인 배포 방식을 제공합니다.

---

## 1. Mix Release: 자립형 패키지

Elixir 1.9부터 표준으로 채택된 배포 방식입니다. 애플리케이션 코드, 종속성, Erlang 가상 머신(BEAM)까지 모두 하나의 폴더에 패키징합니다.

**장점:**
- **독립성**: 서버에 Elixir나 Erlang을 미리 설치할 필요가 없습니다.
- **보안**: 소스 코드가 아닌 컴파일된 바이너리만 배포됩니다.
- **안정성**: 런타임에 필요한 모든 설정이 미리 결정되어 예측 가능합니다.

---

## 2. 릴리즈 만들기

1. **설정**: `mix.exs`에서 프로젝트 설정을 확인합니다.
2. **빌드**:
```bash
# 운영 환경용 의존성 설치 및 컴파일
MIX_ENV=prod mix deps.get
MIX_ENV=prod mix compile

# 릴리즈 생성
MIX_ENV=prod mix release
```
3. **실행**: 생성된 `_build/prod/rel/my_app/bin/my_app start` 명령어로 서버를 켭니다.

---

## 3. 런타임 설정 (Runtime Config)

운영 환경에서는 데이터베이스 주소나 비밀번호 같은 정보가 바뀔 수 있습니다. `config/runtime.exs`를 사용하여 실행 시점에 설정을 읽어오도록 합니다.

```elixir
# config/runtime.exs 예시
import Config

if config_env() == :prod do
  secret_key_base = System.fetch_env!("SECRET_KEY_BASE")
  config :my_app, MyAppWeb.Endpoint,
    secret_key_base: secret_key_base
end
```

---

## 4. 도커(Docker)와 Elixir

현대적인 배포를 위해 도커를 많이 사용합니다. Elixir는 `mix release` 덕분에 매우 가벼운 도커 이미지를 만들 수 있습니다. (알파인 리눅스 등을 사용하면 이미지가 수십 MB 수준으로 줄어듭니다.)

---

## 노련한 선배의 한마디 👴

> "배포는 단순히 파일을 옮기는 과정이 아닙니다. **'불변의 아티팩트'**를 만드는 과정이죠. 한 번 빌드된 릴리즈는 절대 변하지 않아야 하며, 환경 설정만 바꾸어 어디서든 똑같이 돌아가야 합니다. `mix release`는 이 원칙을 가장 잘 실현하는 도구입니다. 이제 마지막 이론인 메타프로그래밍으로 떠나볼까요?"

---
**다음 장 안내**: [22. 메타프로그래밍 맛보기 (Metaprogramming Intro)](22_metaprogramming_intro.md)
