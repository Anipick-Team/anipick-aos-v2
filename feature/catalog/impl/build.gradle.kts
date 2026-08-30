plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.catalog.impl"
}

dependencies {
    implementation(projects.feature.catalog.api)
    implementation(projects.feature.community.api)
    implementation(projects.feature.review.api)
}