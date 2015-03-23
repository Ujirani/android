package br.liveo.ndrawer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public static String mUrl = null;
    public static String mPostUrl = null;
    public static List<NameValuePair> mPostParams = null;
    public static List<NameValuePair> mParams = null;
    public static JSONObject mResult = null;
    public static Context mContext;

    // constructor
    public JSONParser(Context context) {
        JSONParser.mContext = context;
    }

    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) throws InterruptedException, ExecutionException {
        JSONParser.mPostUrl = url;
        JSONParser.mPostParams = params;

        JSONObject jsonResult = new AsyncTask<String, String, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... params) {
                InputStream mis = null;


                // Making HTTP request
                try {

                    // defaultHttpClient
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(JSONParser.mPostUrl);
                    httpPost.setEntity(new UrlEncodedFormEntity(JSONParser.mPostParams));

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    mis = httpEntity.getContent();


                    StatusLine statusLine = httpResponse.getStatusLine();
                    Log.e("HTTP STATUS ", statusLine.getReasonPhrase());
                    Log.e("HTTP STATUS ", statusLine.toString());

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            mis, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "n");
                    }
                    mis.close();
                    json = sb.toString();
                    Log.e("JSON", json);
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
                try {
                    jObj = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

                return jObj;
            }
        }.execute().get();

        return jsonResult;


    }

    public String getGetRequestJsonStringFromUrl(String url) throws JSONException, InterruptedException, ExecutionException {
        return new AsyncTask<String, String, String>() {
            public String result = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String url = params[0];
                InputStream mInputStream = null;

                // Making HTTP request
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet(url);
                    HttpResponse response = client.execute(httpget);
                    // get response
                    HttpEntity entity = response.getEntity();
                    if (entity == null) {
                        return null;
                    }
                    // get response content and convert it to json string
                    mInputStream = entity.getContent();

                    result = streamToJsonString(mInputStream);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
            }
        }.execute(url).get();
    }

    public String getGetSyncRequestJsonStringFromUrl(String url) throws JSONException, InterruptedException, ExecutionException {

        String result = null;

        InputStream mInputStream = null;

        // Making HTTP request
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);
            // get response
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            // get response content and convert it to json string
            mInputStream = entity.getContent();

            result = streamToJsonString(mInputStream);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;

    }

    public String streamToJsonString(final InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw e;
            }
        }
        return sb.toString();
    }

    public JSONObject streamToString(final InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw e;
            }
        }
        try {
            jObj = new JSONObject(sb.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("Json Object", jObj.toString());
        return jObj;
    }
}