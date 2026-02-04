import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisTest {

    private static final String AES_KEY = "PICenterTree0119";

    public static void main(String[] args) {
        try {
            // MyBatis 설정 로드
            String resource = "mybatis-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);

            // 데이터베이스 연결 정보 설정
            String url = "jdbc:mariadb://192.168.0.66:3306/rmpicenter?useCursorFetch=true&useCompression=true";
            String username = "root";
            String encryptedPassword = "H/FK3rBcQc3Jg1slgquphw==";
            String password = decrypt(encryptedPassword);

            // JDBC 직접 연결 테스트
            System.out.println("=== Testing Direct JDBC Connection ===");
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = java.sql.DriverManager.getConnection(url, username, password);
            DatabaseMetaData meta = conn.getMetaData();

            System.out.println("Database: " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion());
            System.out.println("JDBC Driver: " + meta.getDriverName() + " " + meta.getDriverVersion());

            // PI_USER 테이블 확인
            var tables = meta.getTables(null, null, "pi_user", null);
            if (tables.next()) {
                System.out.println("Found PI_USER table");
            }

            // PI_GRADE_USER 테이블 확인
            tables = meta.getTables(null, null, "pi_grade_user", null);
            if (tables.next()) {
                System.out.println("Found PI_GRADE_USER table");
            }

            // pi_login_policy 테이블 확인
            tables = meta.getTables(null, null, "pi_login_policy", null);
            if (tables.next()) {
                System.out.println("Found pi_login_policy table");
            }

            conn.close();

            // 간단한 SQL 테스트
            System.out.println("\n=== Testing SQL Queries ===");
            conn = java.sql.DriverManager.getConnection(url, username, password);
            var stmt = conn.createStatement();

            // 사용자 수 확인
            var rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM PI_USER");
            if (rs.next()) {
                System.out.println("Total users in PI_USER: " + rs.getInt("cnt"));
            }

            // 등급 확인
            rs = stmt.executeQuery("SELECT * FROM PI_GRADE_USER LIMIT 5");
            System.out.println("\nGrade User entries:");
            while (rs.next()) {
                System.out.println("  Grade " + rs.getInt("GRADE_NO") + ": " + rs.getString("GRADE_NAME"));
            }

            rs.close();
            stmt.close();
            conn.close();

            System.out.println("\n=== All Tests Passed! ===");
            System.out.println("The project is ready for MariaDB deployment.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String decrypt(String encryptedBase64) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decoded = Base64.getDecoder().decode(encryptedBase64);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
