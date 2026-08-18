plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.mypage.impl"
}

dependencies {
    implementation(projects.feature.mypage.api)

    // Oss Licenses
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.oss.licenses)
}