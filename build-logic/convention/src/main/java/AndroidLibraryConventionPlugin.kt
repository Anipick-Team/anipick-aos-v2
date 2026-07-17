import com.android.build.api.dsl.LibraryExtension
import com.jparkbro.convention.configureBuildTypes
import com.jparkbro.convention.configureKotlinAndroid
import com.jparkbro.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                configureBuildTypes(this)

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            dependencies {
                add("implementation", project(":core:model"))
                add("implementation", project(":core:common"))

                add("implementation", project.libs.findBundle("koin").get())
                add("implementation", project.libs.findLibrary("timber").get())
                add("testImplementation", kotlin("test"))
            }
        }
    }
}