# JLPT 매일 N3

문법·독해를 매일 약 5분 학습하는 개인용 Android 앱. 한국어 UI, 일본어 문제, 오프라인 Room 저장입니다.

오늘의 미풀이 문제 → 답 선택·채점·해설 → 오답 복습 → 날짜별 기록 → 문제 ID·문제·보기·내 답·정답·해설 복사 → 기존 ChatGPT 대화에 직접 붙여넣기.

최초 콘텐츠는 자체 제작 문법 10개·독해 3개인 동작 검증용 묶음입니다. 공식 기출·전문가 검수·JLPT 인증 자료가 아니며 기본 문법 복습도 포함합니다. 새 문제를 소진하면 복습 안내를 표시합니다. 자세한 범위와 추가 형식은 [MVP.md](MVP.md), 검토 근거는 [CONTENT_REVIEW.md](CONTENT_REVIEW.md)를 확인하세요.

## 개발

JDK 21, Android SDK 37, Gradle wrapper. Android Studio에서 열거나 SDK 경로를 로컬 `local.properties`에 설정합니다.

```sh
./gradlew :app:testDebugUnitTest :app:assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.

Compose · retain Presenter · Navigation3 · Soil(Room subscription) · Metro · Room을 단일 app 모듈에 사용합니다. 네트워크 권한, 로그인, LLM API, 결제, 앱 알림이 없습니다. 공식 참고 링크를 열 때만 외부 브라우저를 사용합니다. 학습 기록은 기기 내에 보관되며 앱 삭제 시 사라집니다.
