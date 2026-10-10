plugins { id("com.android.application"); id("org.jetbrains.kotlin.plugin.compose"); id("com.google.devtools.ksp"); id("dev.zacsweers.metro"); id("org.jetbrains.kotlin.plugin.serialization") }
android {
 namespace = "dev.jlpt.daily"
 compileSdk = 37
 defaultConfig { applicationId = "dev.jlpt.daily"; minSdk = 26; targetSdk = 36; versionCode = 2; versionName = "0.2.0"; testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
 buildFeatures { compose = true }
 compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
 testOptions { unitTests.isIncludeAndroidResources = true }
}
dependencies {
 implementation("androidx.activity:activity-compose:1.13.0")
 implementation(platform("androidx.compose:compose-bom:2026.09.00"))
 implementation("androidx.compose.material3:material3")
 implementation("androidx.compose.ui:ui")
 implementation("androidx.compose.runtime:runtime-retain")
 implementation("androidx.navigation3:navigation3-runtime:1.2.0")
 implementation("androidx.navigation3:navigation3-ui:1.2.0")
 implementation("com.soil-kt.soil:query-core:1.0.0-alpha15")
 implementation("com.soil-kt.soil:query-compose:1.0.0-alpha15")
 implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
 implementation("androidx.room:room-runtime:2.8.5")
 ksp("androidx.room:room-compiler:2.8.5")
 testImplementation("androidx.compose.ui:ui-test-junit4")
 debugImplementation("androidx.compose.ui:ui-test-manifest")
 testImplementation("junit:junit:4.13.2")
 testImplementation("org.robolectric:robolectric:4.17")
 testImplementation("androidx.test:core:1.7.0")
 testImplementation("androidx.test.ext:junit:1.3.0")
 testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.11.0")
}
