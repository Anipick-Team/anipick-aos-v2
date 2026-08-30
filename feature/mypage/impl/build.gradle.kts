plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.mypage.impl"
}

dependencies {
    implementation(projects.feature.mypage.api)
    implementation(projects.feature.catalog.api)
    implementation(projects.feature.community.api)
    implementation(projects.feature.review.api)

    // Oss Licenses
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.oss.licenses)
}