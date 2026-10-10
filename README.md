# 하루 N3

짧은 설명을 먼저 읽고 문법·독해 문제를 푸는 개인용 Android 앱. 한국어 UI, 일본어 문제, 오프라인 Room 저장입니다.

오늘 배울 내용 선택 → 설명·접속 형태·일본어 예문과 뜻 → 명시적으로 연결된 문제 → 답 선택·채점·해설 → 오답 복습 → 날짜별 기록 → 문제 ID·문제·보기·내 답·정답·해설 복사 → 기존 ChatGPT 대화에 직접 붙여넣기.

최초 콘텐츠는 자체 제작 문법 학습 10개·독해 학습 3개와 연결된 확인 문제 13개입니다. 문제 중에도 학습 설명을 다시 볼 수 있습니다. 공식 기출·전문가 검수·JLPT 인증 자료가 아니며 기본 문법 복습도 포함합니다. 새 문제를 소진하면 복습 안내를 표시합니다. 자세한 범위와 추가 형식은 [MVP.md](MVP.md), 검토 근거는 [CONTENT_REVIEW.md](CONTENT_REVIEW.md)를 확인하세요.

## 개발

JDK 21, Android SDK 37, Gradle wrapper. Android Studio에서 열거나 SDK 경로를 로컬 `local.properties`에 설정합니다.

```sh
./gradlew :app:testDebugUnitTest :app:assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.

Compose · retain Presenter · Navigation3 · Soil(Room subscription) · Metro · Room을 단일 app 모듈에 사용합니다. 네트워크 권한, 로그인, LLM API, 결제, 앱 알림이 없습니다. 공식 참고 링크를 열 때만 외부 브라우저를 사용합니다. 학습 기록은 기기 내에 보관되며 앱 삭제 시 사라집니다.

학습 추가 버전은 기존 Room schema v1과 문제 ID·정답을 유지합니다. 이전 기록은 보존되며 학습 ID·목표가 복사 텍스트에 포함됩니다. 뒤로가기 설정의 범위와 한계는 [BACK_NAVIGATION.md](BACK_NAVIGATION.md)에 기록했습니다.
