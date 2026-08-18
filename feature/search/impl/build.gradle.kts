plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.search.impl"
}

dependencies {
    implementation(projects.feature.search.api)
    implementation(projects.feature.catalog.api)
}
