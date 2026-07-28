plugins {
    alias(libs.plugins.anipick.android.library.compose)
}

android {
    namespace = "com.jparkbro.core.ui"
}

dependencies {
    implementation(projects.core.model)
}