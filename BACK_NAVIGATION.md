# 뒤로가기 설정 확인

2026-10-10에 참고 앱의 로컬 매니페스트와 원격 기본 브랜치의 매니페스트를 읽기 전용으로 확인했습니다. 로컬 체크아웃에는 설정이 없었지만 원격 최신 파일의 `<application>`에는 다음 값이 실제로 선언되어 있었습니다. Activity별 재정의는 없었습니다.

```xml
android:enableOnBackInvokedCallback="false"
```

사용자가 요청한 동일 설정을 이 앱의 `<application>`에 반영했습니다. 화면 이동과 스택 제거는 기존 Navigation3 `NavDisplay`와 `onBack`이 담당합니다. 별도의 `onBackPressed` 오버라이드나 `KEYCODE_BACK` 가로채기는 추가하지 않습니다.

[Android 공식 predictive back 문서](https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture#opt-out)는 `false`가 시스템의 predictive back 애니메이션을 끄고 플랫폼 `OnBackInvokedCallback`을 무시하지만 AndroidX `OnBackPressedCallback`은 계속 호출된다고 설명합니다. Application에 선언한 값은 Activity의 기본값이며 Activity에서 재정의할 수 있습니다.

이 앱은 targetSdk 36, AndroidX Activity 1.13.0, Navigation3 1.2.0을 사용합니다. [Android 16 동작 변경 문서](https://developer.android.com/about/versions/16/behavior-changes-16#predictive-back)에 따르면 API 36을 대상으로 Android 16 이상에서 실행되는 앱은 predictive back이 기본 적용되며, 매니페스트의 `false`는 지원되는 opt-out입니다. 이번 설정은 사용자 요청에 따른 opt-out이며 predictive back 기능을 새로 구현한 것은 아닙니다.

[Navigation3 애니메이션 문서](https://developer.android.com/guide/navigation/navigation-3/animate-destinations)는 앱 내부 화면 전환을 `transitionSpec`, `popTransitionSpec`, `predictivePopTransitionSpec`으로 별도 정의한다고 설명합니다. 따라서 이 매니페스트 값만으로 모든 내부 전환 문제의 원인과 해결이 입증됐다고 단정할 수 없습니다. 기본 뒤로가기와 화면 내부 스택 복귀가 유지되는지 최종 APK에서 검증해야 합니다. 제스처 애니메이션 개선 여부는 제스처 탐색을 이미 사용하는 에뮬레이터에서 완료·취소 동작을 각각 확인해야 하며, 기기 설정 변경 없이 확인할 수 없는 경우 검증하지 않은 것으로 기록합니다.

참고 앱의 파일·브랜치·기기 설정은 변경하지 않았습니다. 매니페스트의 unrelated 속성이나 서비스 설정은 복제하지 않았습니다.

## 최종 APK 뒤로가기 확인

2026-10-10, opt-out 설정과 Navigation3 saveable decorator를 포함한 debug APK를 기존 에뮬레이터에 덮어 설치해 확인했습니다. 미제출 문제에서 설명 다시 보기 → 시스템 BACK → 같은 답 선택 유지, 문제 → 시스템 BACK → 오늘 목록 복귀가 정상 동작했습니다. 학습 결과는 강제 종료·재실행 뒤에도 유지됐습니다.

에뮬레이터의 현재 탐색 방식은 3버튼으로 확인됐습니다. 설정을 변경하지 않았기 때문에 제스처 완료·취소 애니메이션은 검증하지 않았습니다. 이번 검증은 뒤로가기 스택 처리와 답 선택 보존을 확인한 것이며 애니메이션 문제의 원인이 이 속성이라고 입증한 결과는 아닙니다.
