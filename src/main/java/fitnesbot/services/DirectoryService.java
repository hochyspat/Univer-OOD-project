package fitnesbot.services;

import java.io.File;

public class DirectoryService {
    public static String createFile(String fileName, String direcoryPath) {
        String direcory = System.getProperty("user.dir");
        File charts = new File(direcory, direcoryPath);
        if (!charts.exists()) {
            charts.mkdirs();
        }
        return new File(charts, fileName).getAbsolutePath();
    }
}
