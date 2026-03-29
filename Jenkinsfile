pipeline {
    agent any

    environment {
        SLACK_CHANNEL = '#jenkins-notifications'
    }

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Pulling code from GitHub...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building project...'
                sh 'mvn clean compile -B'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running API tests...'
                sh 'mvn test -B'
            }
        }

        stage('Publish Reports') {
            steps {
                echo 'Publishing test reports...'
                junit '**/target/surefire-reports/*.xml'
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site/allure-maven-plugin',
                    reportFiles: 'index.html',
                    reportName: 'Allure Report'
                ])
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            emailext(
                subject: "[PASSED] API Tests — Build #${env.BUILD_NUMBER}",
                body: """
                    <html>
                    <body>
                        <h2 style="color: green;">✅ Build PASSED</h2>
                        <p><b>Job:</b> ${env.JOB_NAME}</p>
                        <p><b>Build:</b> #${env.BUILD_NUMBER}</p>
                        <p><b>Branch:</b> ${env.GIT_BRANCH}</p>
                        <p><a href="${env.BUILD_URL}">View Build</a></p>
                    </body>
                    </html>
                """,
                to: '${DEFAULT_RECIPIENTS}',
                mimeType: 'text/html'
            )
            slackSend(
                channel: "${SLACK_CHANNEL}",
                color: 'good',
                message: "✅ PASSED — ${env.JOB_NAME} #${env.BUILD_NUMBER} | <${env.BUILD_URL}|View Build>"
            )
        }
        failure {
            emailext(
                subject: "[FAILED] API Tests — Build #${env.BUILD_NUMBER}",
                body: """
                    <html>
                    <body>
                        <h2 style="color: red;">❌ Build FAILED</h2>
                        <p><b>Job:</b> ${env.JOB_NAME}</p>
                        <p><b>Build:</b> #${env.BUILD_NUMBER}</p>
                        <p><b>Branch:</b> ${env.GIT_BRANCH}</p>
                        <p><a href="${env.BUILD_URL}">View Build</a></p>
                    </body>
                    </html>
                """,
                to: '${DEFAULT_RECIPIENTS}',
                mimeType: 'text/html'
            )
            slackSend(
                channel: "${SLACK_CHANNEL}",
                color: 'danger',
                message: "❌ FAILED — ${env.JOB_NAME} #${env.BUILD_NUMBER} | <${env.BUILD_URL}|View Build>"
            )
        }
    }
}