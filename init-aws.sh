#!/bin/bash
echo "----------- Inicializando recursos AWS locais -----------"

awslocal sqs create-queue --queue-name minha-fila-dlq

awslocal sqs create-queue --queue-name minha-fila --attributes '{
  "RedrivePolicy": "{\"deadLetterTargetArn\":\"arn:aws:sqs:us-east-1:000000000000:minha-fila-dlq\",\"maxReceiveCount\":\"3\"}"
}'

awslocal s3 mb s3://meu-bucket-de-arquivos

echo "----------- Recursos AWS locais criados com sucesso! -----------"