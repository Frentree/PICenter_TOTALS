#!/bin/bash

# PICenter_RM 자동 배포 스크립트
# 사용자가 Tomcat 설치 후 실행

set -e

PROJECT_DIR="/apps/frentree/PICenter_RM"
WAR_FILE="$PROJECT_DIR/build/libs/PICenter_RM.war"
TOMCAT_WEBAPP="/var/lib/tomcat10/webapps"
TOMCAT_LOG="/var/lib/tomcat10/logs/catalina.out"

echo "========================================"
echo "  PICenter_RM 자동 배포 스크립트"
echo "========================================"
echo ""

# sudo 권한 확인
if [ "$EUID" -ne 0 ]; then
    echo "sudo 권한으로 실행해주세요: sudo $0"
    exit 1
fi

# 1. Tomcat 상태 확인
echo "[1/5] Tomcat 상태 확인..."
if systemctl is-active --quiet tomcat10; then
    echo "  ✓ Tomcat10 실행 중"
    systemctl stop tomcat10
    echo "  → Tomcat 중지 완료"
else
    echo "  ○ Tomcat10 중지됨"
fi

# 2. WAR 파일 존재 확인
echo ""
echo "[2/5] WAR 파일 확인..."
if [ -f "$WAR_FILE" ]; then
    echo "  ✓ WAR 파일 존재: $WAR_FILE"
    ls -lh "$WAR_FILE"
else
    echo "  ✗ WAR 파일 없음: $WAR_FILE"
    echo "  → 먼저 빌드를 실행하세요: gradle clean build"
    exit 1
fi

# 3. 기존 배포 삭제
echo ""
echo "[3/5] 기존 배포 정리..."
rm -rf "$TOMCAT_WEBAPP/PICenter_RM"
rm -f "$TOMCAT_WEBAPP/PICenter_RM.war"
echo "  ✓ 기존 파일 삭제 완료"

# 4. WAR 파일 배포
echo ""
echo "[4/5] WAR 파일 배포..."
cp "$WAR_FILE" "$TOMCAT_WEBAPP/"
echo "  ✓ 배포 완료: $TOMCAT_WEBAPP/PICenter_RM.war"

# 5. Tomcat 시작
echo ""
echo "[5/5] Tomcat 시작..."
systemctl start tomcat10
sleep 5

if systemctl is-active --quiet tomcat10; then
    echo "  ✓ Tomcat 시작 완료"
else
    echo "  ✗ Tomcat 시작 실패"
    echo ""
    echo "=== 로그 확인 ==="
    tail -50 "$TOMCAT_LOG"
    exit 1
fi

# 배포 완료 확인
echo ""
echo "========================================"
echo "  배포 완료!"
echo "========================================"
echo ""
echo "접속 URL: http://localhost:8080/PICenter_RM/"
echo ""
echo "=== 최신 로그 ==="
tail -20 "$TOMCAT_LOG"
echo ""
echo "========================================"
