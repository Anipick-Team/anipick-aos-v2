import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureApiConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("anipick.android.library")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
//                add("api", project(":core:navigation"))
            }
        }
    }
}