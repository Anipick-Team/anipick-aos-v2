plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.home.impl"
}

dependencies {
    implementation(projects.feature.home.api)
}