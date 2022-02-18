package apitests.helper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileHelper {

    public static String getContent(String nameFileResponse) {
        return loadStringFromFile(nameFileResponse);
    }

    public static String loadStringFromFile(String path) {
        String data = "";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        try {
            assert is != null;
            data = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
        return data;
    }

}
