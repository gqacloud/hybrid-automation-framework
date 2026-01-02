#!/bin/bash

echo "======================================"
echo " Running Hybrid Automation Tests (CI)"
echo " Environment : QA"
echo " Browser     : Chrome"
echo " Run Mode    : CI"
echo " Headless    : TRUE"
echo "======================================"

# Fail immediately if any command fails
set -e

# Print Java & Maven versions (useful for CI logs/debugging)
echo "Java Version:"
java -version

echo "Maven Version:"
mvn -version

# Clean & execute tests using TestNG XML
echo "Cleaning and starting test execution..."
mvn clean test \
  -Denv=qa \
  -Dbrowser=chrome \
  -DrunMode=CI \
  -Dheadless=true \
  -Dsurefire.suiteXmlFiles=testng/master.xml

echo "======================================"
echo " CI Execution Completed Successfully"
echo "======================================"

echo " Reports available at:"
echo " $(pwd)/reports"
echo "======================================"
