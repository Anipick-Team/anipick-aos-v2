package com.jparkbro.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.addUiLayerDependencies(project: Project) {
    add("implementation", project.libs.findBundle("compose").get())
    add("implementation", project.libs.findBundle("koin-compose").get())
    add("implementation", project.libs.findBundle("coil").get())
    add("debugImplementation", project.libs.findBundle("compose-debug").get())
    add("androidTestImplementation", project.libs.findLibrary("androidx-compose-ui-test-junit4").get())
}