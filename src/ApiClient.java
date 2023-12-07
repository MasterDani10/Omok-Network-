import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    private static final String BASE_URL = "http://omok.atwebpages.com/play/";

    public void sendMove(String pid, int x, int y) throws IOException {
        String urlString = String.format("%s?pid=%s&x=%d&y=%d", BASE_URL, pid, x, y);
        String response = sendGet(urlString);
        parseResponse(response);
    }

    private String sendGet(String urlString) throws IOException {
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }


    private void parseResponse(String response) {

    }
}