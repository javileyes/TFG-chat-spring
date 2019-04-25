#!/usr/bin/env bash
rm -rf ~/chat/project
git clone ~/chat/git ~/chat/project
bash -x ~/chat/project/gradle/bash/deploy.sh