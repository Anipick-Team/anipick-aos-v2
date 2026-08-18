plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.explore.impl"
}

dependencies {
    implementation(projects.feature.explore.api)
    implementation(projects.feature.search.api)
}