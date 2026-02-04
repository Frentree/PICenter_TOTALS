#!/bin/bash

echo "=== PICenter_RM 배포 가이드 ==="
echo ""
echo "1. Tomcat 설치:"
echo "   sudo apt-get update && sudo apt-get install -y tomcat10"
echo ""
echo "2. WAR 파일 배포:"
echo "   sudo cp build/libs/PICenter_RM.war /var/lib/tomcat10/webapps/"
echo ""
echo "3. Tomcat 재시작:"
echo "   sudo systemctl restart tomcat10"
echo ""
echo "4. 로그 확인:"
echo "   sudo tail -f /var/lib/tomcat10/logs/catalina.out"
echo ""
echo "5. 접속:"
echo "   http://localhost:8080/PICenter_RM/"
echo ""

# 현재 상태 확인
echo "=== 현재 상태 ==="
if [ -f "build/libs/PICenter_RM.war" ]; then
    echo "✓ WAR 파일 존재함: build/libs/PICenter_RM.war"
    ls -lh build/libs/PICenter_RM.war
else
    echo "✗ WAR 파일 없음"
fi

if systemctl is-active --quiet tomcat10 2>/dev/null; then
    echo "✓ Tomcat10 실행 중"
else
    echo "○ Tomcat10 미설치 또는 중지됨"
fi

echo ""
echo "DB 연결 테스트:"
java -cp ".:src/main/java:lib/mariadb-java-client-3.5.1.jar" DbConnectionTest 2>&1 | grep -E "Connected|Database|MariaDB" | head -3

