package online.yudream.intl.utils;

import java.io.IOException;

public class FlutterCommandExecutor {
    public void executeFlutterCommand() {
        // 定义 Flutter 命令
        String command = "flutter pub global run intl_utils:generate";

        // 使用 ProcessBuilder 来执行命令
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);  // 对于 Windows 使用 "cmd", "/c"

        try {
            // 启动进程并等待命令执行完成
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("Command executed with exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
