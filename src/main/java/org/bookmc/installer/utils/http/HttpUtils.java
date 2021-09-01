package org.bookmc.installer.utils.http;

import org.bookmc.installer.utils.constants.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public static String get(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setConnectTimeout(3 * 1000);
            connection.addRequestProperty("User-Agent", "Book/Installer " + Constants.INSTALLER_VERSION);
            try (InputStream stream = connection.getInputStream()) {
                byte[] bytes = stream.readAllBytes();
                return new String(bytes, 0, bytes.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException();
    }
}
