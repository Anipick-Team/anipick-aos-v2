plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.home.impl"
}

dependencies {
    implementation(projects.feature.home.api)
    implementation(projects.feature.search.api)
    implementation(projects.feature.review.api)
    implementation(projects.feature.catalog.api)
    implementation(projects.feature.ranking.api)
    implementation(projects.feature.explore.api)
}