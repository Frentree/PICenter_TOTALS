import java.sql.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DbConnectionTest {

    private static final String AES_KEY = "PICenterTree0119";

    public static void main(String[] args) {
        try {
            // 복호화된 비밀번호 확인
            String encryptedPassword = "H/FK3rBcQc3Jg1slgquphw==";
            String decryptedPassword = decrypt(encryptedPassword);
            System.out.println("Decrypted password: " + decryptedPassword);

            // MariaDB 연결 정보
            String url = "jdbc:mariadb://192.168.0.66:3306/rmpicenter?useCursorFetch=true&useCompression=true";
            String username = "root";

            // MariaDB JDBC 드라이버 로드
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("MariaDB JDBC Driver loaded successfully.");

            // 연결 테스트
            Connection conn = DriverManager.getConnection(url, username, decryptedPassword);
            System.out.println("Successfully connected to MariaDB!");

            // 간단한 쿼리 테스트
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("Database Product: " + meta.getDatabaseProductName());
            System.out.println("Database Version: " + meta.getDatabaseProductVersion());

            // 기존 테이블 확인
            ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"});
            System.out.println("\nTables in database:");
            int count = 0;
            while (tables.next() && count < 10) {
                System.out.println("  - " + tables.getString("TABLE_NAME"));
                count++;
            }
            if (count >= 10) {
                System.out.println("  ... and more");
            }

            conn.close();
            System.out.println("\nConnection test completed successfully!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String decrypt(String encryptedBase64) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decoded = Base64.getDecoder().decode(encryptedBase64);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }
}
