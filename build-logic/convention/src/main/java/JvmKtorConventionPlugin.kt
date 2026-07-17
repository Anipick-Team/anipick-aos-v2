import com.jparkbro.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JvmKtorConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                add("implementation", project.libs.findBundle("ktor").get())
                add("implementation", project.libs.findLibrary("kotlinx.serialization.json").get())
            }
        }
    }
}