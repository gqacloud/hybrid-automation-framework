#!/bin/bash

echo "======================================"
echo " Running Hybrid Automation Framework"
echo " Environment : QA"
echo " Browser     : Chrome"
echo " Run Mode    : LOCAL"
echo "======================================"

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
cd "$PROJECT_DIR" || exit 1

echo "Project Directory: $PROJECT_DIR"

echo "Cleaning previous builds..."
mvn clean || exit 1

echo "Starting test execution..."
mvn test \
  -Denv=qa \
  -Dbrowser=chrome \
  -DrunMode=LOCAL \
  -Dsurefire.suiteXmlFiles=src/test/resources/testng/master.xml

EXECUTION_STATUS=$?

echo "======================================"
if [ $EXECUTION_STATUS -eq 0 ]; then
  echo " Test Execution SUCCESSFUL"
else
  echo " Test Execution FAILED"
fi
echo "======================================"

exit $EXECUTION_STATUS
