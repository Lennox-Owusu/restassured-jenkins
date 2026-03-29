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
        unstable {
            script {
                def testResults = currentBuild.testResultAction
                def total = testResults ? testResults.totalCount : 0
                def failed = testResults ? testResults.failCount : 0
                def skipped = testResults ? testResults.skipCount : 0
                def passed = total - failed - skipped
                def duration = currentBuild.durationString.replace(' and counting', '')

                def failedTests = ''
                if (testResults && testResults.failCount > 0) {
                    failedTests = testResults.failedTests.take(10).collect { test ->
                        "• ${test.fullName}"
                    }.join('\n')
                }

                slackSend(
                    tokenCredentialId: 'slack-token',
                    channel: '#jenkins-messages',
                    color: 'warning',
                    message: """⚠️ *UNSTABLE* — ${env.JOB_NAME} #${env.BUILD_NUMBER}
    *Branch:* ${env.GIT_BRANCH}
    *Duration:* ${duration}
    *Tests:* ✅ ${passed} passed | ❌ ${failed} failed | ⏭️ ${skipped} skipped
    *Failed Tests:*
    ${failedTests}
    *Build URL:* ${env.BUILD_URL}"""
                )
            }
        }
        success {
            script {
                def testResults = currentBuild.testResultAction
                def total = testResults ? testResults.totalCount : 0
                def failed = testResults ? testResults.failCount : 0
                def skipped = testResults ? testResults.skipCount : 0
                def passed = total - failed - skipped
                def duration = currentBuild.durationString.replace(' and counting', '')

                slackSend(
                    tokenCredentialId: 'slack-token',
                    channel: '#jenkins-messages',
                    color: 'good',
                    message: """✅ *PASSED* — ${env.JOB_NAME} #${env.BUILD_NUMBER}
    *Branch:* ${env.GIT_BRANCH}
    *Duration:* ${duration}
    *Tests:* ✅ ${passed} passed | ⏭️ ${skipped} skipped
    *Build URL:* ${env.BUILD_URL}"""
                )
            }
        }
        failure {
            script {
                def duration = currentBuild.durationString.replace(' and counting', '')

                slackSend(
                    tokenCredentialId: 'slack-token',
                    channel: '#jenkins-messages',
                    color: 'danger',
                    message: """❌ *FAILED* — ${env.JOB_NAME} #${env.BUILD_NUMBER}
    *Branch:* ${env.GIT_BRANCH}
    *Duration:* ${duration}
    *Build URL:* ${env.BUILD_URL}"""
                )
            }
        }
    }