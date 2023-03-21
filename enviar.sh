#!/bin/bash

push.sh

./gradlew clean build -Pvaadin.productionMode

sshpass -p '3st02@2o&*' \
  rsync -av --progress \
  build/libs/devFornecedor-1.0.war \
  root@172.20.47.2:/root/containers/devFornecedor/
