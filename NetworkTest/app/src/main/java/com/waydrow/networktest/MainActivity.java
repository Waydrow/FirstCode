package com.waydrow.networktest;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.version;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    public static final String XML_REQUEST_URL = "https://qcloud.waydrow.com/Android/get_data.xml";

    public static final String JSON_REQUEST_URL = "https://qcloud.waydrow.com/Android/get_data.json";

    private TextView responseText;

    // Handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendRequeset = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.response_text);
        sendRequeset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {
            //sendRequestWithHttpURLConnection();
//            sendRequestWithOkHttp();
             //封装HTTP URL Connection
            String url = null;

            url = "https://qcloud.waydrow.com/LoveInn/index.php/Home/App/getActivityInfoById";
            String params = "id=18";

            HttpUtil.sendHttpRequestWithHttpURLConnection(url, params, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Log.d(TAG, "onFinish: " + response);
                    showResponse(response);
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            /*
            // 封装okhttp
            HttpUtil.sendOkHttpRequest(JSON_REQUEST_URL, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    showResponse(responseData);
                }
            });
            */
        }
    }

    // okhttp 发送网络请求
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(JSON_REQUEST_URL)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
//                    showResponse(responseData);
//                    parseXMLWithPull(responseData);
//                    parseJSONWithJSONObject(responseData);
                    parseJSONWithGSON(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        }).start();
    }

    // 使用 GSON 解析 json
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<People> peopleList = gson.fromJson(jsonData, new TypeToken<List<People>>(){}.getType());
        for (People people : peopleList) {
            Log.d(TAG, "id is " + people.getId());
            Log.d(TAG, "name is " + people.getName());
            Log.d(TAG, "gender is " + people.getGender());
        }
    }

    // 使用json object解析json数据
    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                String gender = jsonObject.getString("gender");
                Log.d(TAG, "id is " + id);
                Log.d(TAG, "name is " + name);
                Log.d(TAG, "gender is " + gender);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 解析 xml 数据
    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    // 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG: {
                        if ("app".equals(nodeName)) {
                            Log.d(TAG, "id is " + id);
                            Log.d(TAG, "name is " + name);
                            Log.d(TAG, "version is " + version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // http url connection 发送网络请求
    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(XML_REQUEST_URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    // 取获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
}
