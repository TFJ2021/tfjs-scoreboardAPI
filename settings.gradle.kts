rootProject.name = "tfjs-scoreboardAPI"

// Jitpack integration
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven("https://repo.papermc.io/repository/maven-public/") {
            name = "papermc-repo"
        }
    }
}