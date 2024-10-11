pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ToDoListYd"
include(":app")
include(":todo-uikit")
include(":database")
include(":todo-utils")
include(":domain")
include(":features:todo-list")
include(":features:todo-add-edit")
include(":activity:main-activity-impl")
include(":activity:main-activity-api")
include(":navigation")
