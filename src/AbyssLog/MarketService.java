package AbyssLog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MarketService {
    private String apiKey = "G9KwKq3465588VPd6747t95Zh94q3W2E";

    public JSONObject appraise(String loot) throws IOException {
        String url = "https://janice.e-351.com/api/rest/v2/appraisal?market=2&designation=appraisal&pricing=split&pricingVariant=immediate&persist=false&compactize=true&pricePercentage=1";
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        OutputStream output;

        if (connection != null) {
            // set headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent","Mozilla/5.0");
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("X-ApiKey", apiKey);
            connection.setRequestProperty("Content-Type","text/plain");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            output = connection.getOutputStream();
            // set body
            output.write(loot.getBytes());
            output.flush();

            int responseCode = connection.getResponseCode();
            System.out.println("'POST' request is sent to URL : "+ url +"\nResponse Code: "+responseCode);

            if (responseCode == 200 || responseCode == 202) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return new JSONObject(response.toString());
            }
        }
        return null;
    }
}
