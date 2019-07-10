#!/usr/bin/env bash
rm -rf ~/gateway/project
git clone ~/gateway/git ~/gateway/project
bash -x ~/gateway/project/gateway/gradle/bash/deploy.sh