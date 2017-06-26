// If you want, you can define your seed job in the DSL and create it via the REST API.
// See https://github.com/sheehan/job-dsl-gradle-example#rest-api-runner

def project = GIT_REPO + "-" + GIT_PROJECT
def job_name = project +'/' + 'seed'

folder(project){
    displayName(project)
}

listView(project) {
    description('OpenStack-Helm CI')
    jobs {
        name(project)
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

job(job_name) {
    scm {
        github('att-comdev/jenkins-ci', 'master')
    }
    triggers {
        githubPush()
    }
    steps {
        gradle 'clean test'
        dsl {
            external 'jobs/**/*-job.groovy'
            additionalClasspath 'src/main/groovy'
        }
    }
    publishers {
        archiveJunit 'build/test-results/**/*.xml'
    }
}