# 검증 범위

- JUnit/Robolectric: QuestionBankTest 6개, StudyStoreTest 8개, LessonBankTest 4개, LessonUiTest 2개, NavigationCompatibilityTest 2개 (총 22개). 채점·잘못된 인덱스·동시 중복 제출·날짜별 기록·오답 재시도와 정답 후 해소·Room 파일 재개방·복사 텍스트·미풀이 우선 출제·소진·오늘 답 복원을 확인한다.
- 명령: `./gradlew :app:testDebugUnitTest :app:assembleDebug :app:lintDebug --offline`.
- 콘텐츠 언어 검토와 프로그램 테스트는 서로 다른 검증이다. 출제와 독립 AI 검토 및 실제 자료 대조 결과는 CONTENT_REVIEW.md에 기록한다.
- 기기 화면 검증 결과는 UI_VERIFICATION.md에 기록한다. 삭제나 기기 설정 변경 없이 실행한다.
- 초기 묶음은 13문항이고 장기 학습 분량과 N3 전체 난이도·합격 예측은 검증하지 않는다.

학습 설명 추가 후 최종 실행 결과: 22개 테스트, 실패·오류 0; debug APK 생성 성공. lint 오류 0, 경고 4(최신 target API 권고, API 33 이상에서 사용하는 뒤로가기 속성, minSdk와 같은 리소스 폴더 한정자, Uri.parse 대신 KTX 권고). targetSdk 36과 요청한 opt-out을 유지했다. 최종 APK 화면 검증 결과는 UI_VERIFICATION.md에 기록한다.

새 검증에는 13개 학습 설명과 문제의 일대일 연결, 학습→확인 문제→해설→학습 재방문, 소진 안내, 기존 Navigation3 경로 역직렬화, schema version 1 실제 SQL/identity hash로 만든 이전 데이터 복원, 학습 ID·목표가 포함된 복사 텍스트가 포함된다. 기존 문제 ID·정답·보기와 Room schema version 1을 유지해 새로 출제하거나 기록을 초기화하지 않는다.

## 런처 이름·아이콘 변경

앱 표시 이름을 ‘하루 N3’로 바꾸고 책 모양 vector/adaptive 아이콘과 테마용 monochrome 아이콘을 추가했다. `assembleDebug`·`lintDebug` 성공 및 AAPT로 APK의 label·icon·adaptive foreground/background/monochrome 리소스를 확인했다. 연결된 단일 실기기에 `adb install -r`로 설치했으며 패키지 installed=true, stopped=true, notLaunched=true를 확인했다. 앱 실행·화면 조작·기기 설정 변경·데이터 삭제는 하지 않았다.
