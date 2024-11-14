#!/bin/bash

./gradlew -p privacy clean artifactoryPublish -Dusername=admin -Dpassword=Hqg994821/ -Dsnapshot=false || { exit 1 ; }
