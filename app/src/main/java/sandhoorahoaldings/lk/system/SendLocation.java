package sandhoorahoaldings.lk.system;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendLocation extends AsyncTask<String, String, String> {

    protected String doInBackground(String... params) {

        String TAG = "Location Sender";
        String JsonResponse = null;
        String JsonDATA = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("https://locationtracker-d681c.firebaseio.com/users/test1.json");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            // is output buffer writter
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(JsonDATA);
// json data
            writer.close();
            InputStream inputStream = urlConnection.getInputStream();
//input stream
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();
//response data
            Log.i(TAG,JsonResponse);
//send to post execute
                return JsonResponse;
            } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

//        HttpURLConnection client = null;
//        try {
//            URL url = new URL("https://locationtracker-d681c.firebaseio.com/users/test1.json");
//
//
//            client = (HttpURLConnection) url.openConnection();
//            client.setRequestMethod("POST");
//            client.setRequestProperty("Key","Value");
//            client.setDoOutput(true);
//
//            OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
//
//            outputPost.flush();
//            outputPost.close();
//
//            //client.setFixedLengthStreamingMode(outputPost.getBytes().length);
////            client.setChunkedStreamingMode(0);
//
//
//
//        } catch (java.io.IOException e){}
//        finally {
//            if(client != null) // Make sure the connection is not null.
//                client.disconnect();
//        }
        return "a";
    }

//    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
//    }

//    protected void onPostExecute(Long result) {
//        showDialog("Downloaded " + result + " bytes");
//    }
}