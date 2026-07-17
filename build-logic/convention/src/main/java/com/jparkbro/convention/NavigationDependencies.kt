package com.jparkbro.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.addNavigationDependencies(project: Project) {
    add("implementation", project.libs.findBundle("navigation3").get())
}