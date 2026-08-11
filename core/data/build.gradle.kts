plugins {
    alias(libs.plugins.anipick.android.library)
}

android {
    namespace = "com.jparkbro.core.data"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.datastore)
}