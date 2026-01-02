#!/bin/bash

echo "======================================"
echo " Running Hybrid Automation Tests on Selenium Grid"
echo " Environment : QA"
echo " Browser     : Chrome"
echo " Run Mode    : GRID"
echo " Headless    : TRUE"
echo "======================================"

# Fail fast in CI / Grid environments
set -e

# Selenium Grid URL
GRID_URL="http://localhost:4444/wd/hub"
echo "Selenium Grid URL: $GRID_URL"

# Print tool versions (helps Grid debugging)
java -version
mvn -version

# Clean & execute tests using TestNG XML
echo "Cleaning and starting Grid execution..."
mvn clean test \
  -Denv=qa \
  -Dbrowser=chrome \
  -DrunMode=GRID \
  -DgridUrl=$GRID_URL \
  -Dheadless=true \
  -Dsurefire.suiteXmlFiles=testng/master.xml

EXECUTION_STATUS=$?

echo "======================================"
if [ $EXECUTION_STATUS -eq 0 ]; then
  echo " Grid Execution SUCCESSFUL"
else
  echo " Grid Execution FAILED"
fi
echo "======================================"

echo " Reports available at:"
echo " $(pwd)/reports"
echo "======================================"

exit $EXECUTION_STATUS
