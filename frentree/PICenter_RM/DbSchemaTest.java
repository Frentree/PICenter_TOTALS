import java.sql.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DbSchemaTest {

    private static final String AES_KEY = "PICenterTree0119";

    public static void main(String[] args) {
        try {
            String encryptedPassword = "H/FK3rBcQc3Jg1slgquphw==";
            String decryptedPassword = decrypt(encryptedPassword);

            String url = "jdbc:mariadb://192.168.0.66:3306/rmpicenter";
            String username = "root";

            Class.forName("org.mariadb.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, decryptedPassword);
            System.out.println("Connected to MariaDB!");

            // Check if sequence exists
            DatabaseMetaData meta = conn.getMetaData();

            // MariaDB 10.3+ uses sequences stored in tables
            ResultSet sequences = null;
            try {
                // Try to query sequence table
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'pi_er2_connect_hist_seq'");
                if (rs.next()) {
                    System.out.println("Found sequence table: pi_er2_connect_hist_seq");
                } else {
                    System.out.println("Sequence table not found. Checking for sequence object...");

                    // Check for SEQUENCE object (MariaDB 10.3+)
                    rs = stmt.executeQuery("SELECT * FROM information_schema.sequences WHERE sequence_name = 'pi_er2_connect_hist_seq'");
                    if (rs.next()) {
                        System.out.println("Found SEQUENCE object: pi_er2_connect_hist_seq");
                    } else {
                        System.out.println("WARNING: Sequence 'pi_er2_connect_hist_seq' not found!");
                        System.out.println("You may need to create it with: CREATE SEQUENCE pi_er2_connect_hist_seq;");
                    }
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                System.out.println("Error checking sequence: " + e.getMessage());
            }

            // Check for main tables
            ResultSet tables = meta.getTables(null, null, "pi_%", new String[]{"TABLE"});
            System.out.println("\nApplication tables found:");
            int count = 0;
            while (tables.next() && count < 20) {
                System.out.println("  - " + tables.getString("TABLE_NAME"));
                count++;
            }

            conn.close();
            System.out.println("\nSchema check completed!");

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
