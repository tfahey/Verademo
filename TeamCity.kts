package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.MavenBuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

object Build : BuildType({
    name = "Build"

    vcs {
        root(HttpsGithubComTfaheyVerademoRefsHeadsMaster)
    }

    steps {
        maven {
            goals = "clean test package"
            pomLocation = "app/pom.xml"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            localRepoScope = MavenBuildStep.RepositoryScope.MAVEN_DEFAULT
        }
        step {
            type = "teamcity-veracode-plugin"
            param("uploadIncludePattern", "**/**.war")
            param("appName", "%env.TEAMCITY_PROJECT_NAME%")
            param("createProfile", "true")
            param("criticality", "VeryHigh")
            param("waitForScan", "false")
            param("sandboxName", "TeamCity")
            param("useGlobalCredentials", "true")
            param("createSandbox", "true")
            param("version", "%env.BUILD_NUMBER%")
        }
    }

    triggers {
        vcs {
        }
    }
})