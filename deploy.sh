#!/bin/bash

./gradlew -p utils clean artifactoryPublish -Dusername=sr_deployer -Dpassword=SRdeploy@2018 -Dsnapshot=false || { exit 1 ; }
