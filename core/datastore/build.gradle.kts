plugins {
    alias(libs.plugins.anipick.android.library)
}

android {
    namespace = "com.jparkbro.core.datastore"

}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}