pipeline {
                agent any
 
                environment {
                  GITLAB_USERNAME = 'Sumair'
                  GITLAB_PASSWORD = 'JzWpS2533$R:password'
                }
 
                stages {
                  stage('Git Clone') {
                      steps {
                          script {
                              // Clean workspace before cloning
                              deleteDir()
 
                              // Clone GitLab repository
                              sh "git clone https://${GITLAB_USERNAME}:${GITLAB_PASSWORD}@osigitlab.osius.com/devops/devops_templates.git"
                          }
                      }
                  }
 
                  stage('Run Python Script') {
                      steps {
                          script {
                              // Navigate to the root directory of the cloned repository
                              dir('devops_templates') {
                                  // Assuming 'generate.py' is at the root of the repository
                                  sh 'python3 generate.py generate -m docker -c values.xlsx -o OUT'
                              }
                          }
                      }
                  }
 
                  stage('Terraform Commands') {
                      steps {
                          script {
                              // Navigate to the 'OUT' directory
                              dir('devops_templates/OUT') {
                                  // Run Terraform commands
                                  sh 'terraform init'
                                  sh 'terraform plan -out=tfplan'
                                  sh 'terraform apply tfplan'
                              }
                          }
                      }
                  }
 
                  // Add more stages if needed...
 
                }
 
                post {
                  always {
                      // Clean up workspace after the pipeline
                      cleanWs()
                    }
                  }
                }
