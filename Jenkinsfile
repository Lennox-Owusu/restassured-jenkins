pipeline {
    agent any

    environment {
        SLACK_CHANNEL = '#jenkins-messages'
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
                sh 'mvn test -B || true'
            }
        }

        stage('Publish Reports') {
            steps {
                echo 'Publishing test reports...'

                junit allowEmptyResults: true,
                      testResults: '**/target/surefire-reports/*.xml'

                sh 'mvn allure:report -B || true'

                publishHTML([
                    allowMissing: true,
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
                        <p><a href="${env.BUILD_URL}">View Build</a></p>
                    </body>
                    </html>
                """,
                to: '${DEFAULT_RECIPIENTS}',
                mimeType: 'text/html'
            )
            slackSend(
                tokenCredentialId: 'slack-token',
                channel: '#jenkins-messages',
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
                        <p><a href="${env.BUILD_URL}">View Build</a></p>
                    </body>
                    </html>
                """,
                to: '${DEFAULT_RECIPIENTS}',
                mimeType: 'text/html'
            )
            slackSend(
                tokenCredentialId: 'slack-token',
                channel: '#jenkins-messages',
                color: 'danger',
                message: "❌ FAILED — ${env.JOB_NAME} #${env.BUILD_NUMBER} | <${env.BUILD_URL}|View Build>"
            )
        }
        unstable {
            slackSend(
                tokenCredentialId: 'slack-token',
                channel: '#jenkins-messages',
                color: 'warning',
                message: "⚠️ UNSTABLE — ${env.JOB_NAME} #${env.BUILD_NUMBER} (some tests failed) | <${env.BUILD_URL}|View Build>"
            )
        }
    }
}