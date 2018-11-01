package cs340.client.communication;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is the interface that fires off a Http Request to the Family
 * Map Server.
 */
public class HttpClient {

    //variables to hold the needed data
    private final String TAG = "HttpClient";

    /**
     * This method fires off the request.
     * @param url The url for the request.
     * @param requestBody The request body for the request.
     * @param authToken The authToken for the request.
     * @return The result of the request.
     * @throws Exception If anything went wrong.
     */
    public String getUrl(URL url, String requestBody, String authToken) throws Exception {

//        Log.i(TAG, "Trying to connect to Server");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", authToken);
        if(requestBody == null || requestBody.isEmpty()) {
            connection.setDoOutput(false);
        }
        else {
            connection.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(requestBody);
            outputStreamWriter.close();
        }
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get response body input stream
//            Log.i(TAG, "Connected to Server");
            InputStream responseBody = connection.getInputStream();

            // Read response body bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = responseBody.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }

            return baos.toString();
        }

        Log.d(TAG, "Connection failed");
        return null;
    }
}
