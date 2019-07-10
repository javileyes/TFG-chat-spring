#!/usr/bin/env bash
rm -rf ~/eureka/project
git clone ~/eureka/git ~/eureka/project
bash -x ~/eureka/project/eureka/gradle/bash/deploy.sh