#!/bin/bash

# Move to the Kotlin project root
cd ..
cd ..

# Run gradle task
./gradlew :shared:packForXCode

# Copy framework to Xcode project root
mkdir ios-app/Kotlin-MPP/kotlin-outputs
cp -R shared/build/xcode-frameworks/main.framework ios-app/Kotlin-MPP/kotlin-outputs

echo "Built for: "$SDK_NAME