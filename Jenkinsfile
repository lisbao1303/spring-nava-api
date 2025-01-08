pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-2c'           // Região AWS
        AWS_ECR_REPO = 'my-app-repo'       // Nome do repositório ECR
        AWS_ACCOUNT_ID = '205930605654'    // ID da conta AWS
        IMAGE_TAG = 'latest'               // Tag da imagem Docker
        ECS_CLUSTER = 'my-ecs-cluster'     // Nome do cluster ECS
        ECS_SERVICE = 'my-ecs-service'     // Nome do serviço ECS
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/lisbao1303/spring-nava-api.git' // Repositório
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh './mvnw clean test' // Executa os testes com Maven Wrapper
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    sh './mvnw clean package -P release -DskipTests' // Realiza o build da aplicação
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Criando a imagem Docker com Maven...'
                    sh 'docker build -t $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$AWS_ECR_REPO:$IMAGE_TAG .'  // Construa a imagem Docker
                }
            }
        }

        stage('Login to ECR') {
            steps {
                script {
                    echo 'Fazendo login no ECR...'
                    sh 'aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com'
                }
            }
        }

        stage('Push to ECR') {
            steps {
                script {
                    echo 'Subindo a imagem para o ECR...'
                    sh 'docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$AWS_ECR_REPO:$IMAGE_TAG'  // Envia a imagem para o ECR
                }
            }
        }

        stage('Deploy to ECS') {
            steps {
                script {
                    echo 'Fazendo o deploy na AWS ECS...'
                    sh '''
                    aws ecs update-service --cluster $ECS_CLUSTER --service $ECS_SERVICE --force-new-deployment --region $AWS_REGION
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline concluído com sucesso!'
        }
        failure {
            echo 'Pipeline falhou!'
        }
    }
}
