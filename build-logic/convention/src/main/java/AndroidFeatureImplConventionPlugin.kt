import com.jparkbro.convention.addNavigationDependencies
import com.jparkbro.convention.addUiLayerDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureImplConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("anipick.android.library.compose")

            dependencies {
//                add("implementation", project(":core:data"))
//                add("implementation", project(":core:model"))
//                add("implementation", project(":core:ui"))
//                add("implementation", project(":core:designsystem"))

                addUiLayerDependencies(target)
                addNavigationDependencies(target)
            }
        }
    }
}