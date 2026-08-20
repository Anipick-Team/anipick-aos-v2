plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.community.impl"
}

dependencies {
    implementation(projects.feature.community.api)

}