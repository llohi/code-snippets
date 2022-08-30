
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class used to connect to an API with a web address
 * and extract an object of any type.
 * @author J.L.
 */
public class ServerRequest {

    /**
     * Use Gson to return an object of type T from a web address.
     * @param url The web address to the raw data
     * @param token A TypeToken for the returned type
     * @param <T> The type to be returned
     * @return An object of type T with the data from the web address
     */
    public static <T> T getObject(String url, TypeToken<T> token) throws IOException {
        return new Gson().fromJson(
                getRawData(url),
                token.getType());
    }

    /**
     * Connect to a URL and return its raw data as a String.
     * @param url The web address to the raw data
     * @return Raw data as a string
     */
    public static String getRawData(String url) throws IOException {

        StringBuilder result = new StringBuilder();

        // Connect to url and get input stream
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        InputStreamReader stream = new InputStreamReader(conn.getInputStream());

        // Append input stream to result
        try (BufferedReader reader = new BufferedReader(stream)) {
            for (String line; (line = reader.readLine()) != null; ) result.append(line);
        }

        return result.toString();
    }
}
