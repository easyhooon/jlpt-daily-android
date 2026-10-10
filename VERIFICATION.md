# 검증 범위

- JUnit: QuestionBankTest 6개, StudyStoreTest 7개. 채점·잘못된 인덱스·동시 중복 제출·날짜별 기록·오답 재시도와 정답 후 해소·Room 파일 재개방·복사 텍스트·미풀이 우선 출제·소진·오늘 답 복원을 확인한다.
- 명령: `./gradlew :app:testDebugUnitTest :app:assembleDebug :app:lintDebug --offline`.
- 콘텐츠 언어 검토와 프로그램 테스트는 서로 다른 검증이다. 출제와 독립 AI 검토 및 실제 자료 대조 결과는 CONTENT_REVIEW.md에 기록한다.
- 기기 화면 검증 결과는 UI_VERIFICATION.md에 기록한다. 삭제나 기기 설정 변경 없이 실행한다.
- 초기 묶음은 13문항이고 장기 학습 분량과 N3 전체 난이도·합격 예측은 검증하지 않는다.
