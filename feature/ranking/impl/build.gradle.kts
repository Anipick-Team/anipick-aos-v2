plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.ranking.impl"
}

dependencies {
    implementation(projects.feature.ranking.api)
    implementation(projects.feature.search.api)
    implementation(projects.feature.catalog.api)
}