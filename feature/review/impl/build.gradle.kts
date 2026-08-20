plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.review.impl"
}

dependencies {
    implementation(projects.feature.review.api)
}