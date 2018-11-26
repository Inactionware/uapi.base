#! /bin/bash

echo "Host gitlab.com\n" >> ~/.ssh/config
echo "    HostName gitlab.com\n" >> ~/.ssh/config
echo "    User git\n" >> ~/.ssh/config
echo "    StrictHostKeyChecking no\n" >> ~/.ssh/config

# Check out build configuration repo from remote
rm -rf .config
mkdir .config
cd .config

git init
git remote add -f origin git@gitlab.com:Inactionware/configuration.git
git config core.sparsecheckout true
echo "uapi" >> .git/info/sparse-checkout
# git pull origin master
git checkout master

# Run gradle build script
# cd ..
# ./gradlew clean build
