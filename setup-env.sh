#! /bin/bash

os=`uname -s`
host=`hostname`
javac=`command -v javac`

if [ -z $javac ]
then
    # Setup variable
    echo -e "Initialize JAVA environment......\c"
    if [ $USER == "min" ] && [ $host == "min-vm-elementary" ]       # At Linux VM host development
    then
        export JAVA_HOME="/home/min/Dev/zuluJdk-8.0.192/"
    elif [ $USER == "xxx" ]                                         # At MacOS host development
    then
        export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home"
    else
        echo "Unsupported user - $USER@$host, please reconfig the user"
        return 1
    fi
    export PATH=$JAVA_HOME/bin:$PATH
    echo "Done"
fi

cfgBranch=master # The configuration repo branch/tag name will be checked out

# Check out build configuration repo from remote
rm -rf .config
mkdir .config
cd .config

git init
git remote add -f origin https://gitlab.com/Inactionware/configuration.git
git config core.sparsecheckout true
echo "uapi" >> .git/info/sparse-checkout
git checkout ${cfgBranch}

# Run gradle build script
cd ..
# ./gradlew clean build
