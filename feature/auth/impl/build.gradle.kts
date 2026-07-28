plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.auth.impl"
}

dependencies {
    // Kakao
    implementation(libs.kakao.user)

    // Google Login
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    implementation(projects.feature.auth.api)
    implementation(projects.feature.home.api)
}