package rannygaming.developers.footballfan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultTeammatesModeActivity extends AppCompatActivity {

    Button btvResult, btvErrors, btnNext;
    int trueReplys = ModeTeammatesActivity15.counter;
    int errors;
    double result;
    int money;
    String stringResult;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";

    String login;
    String ServerURL = "https://rannygaming.ru/footballfanphp/insert_mode_teammates.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_teammates_mode);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        login = sharedPreferences.getString(KEY_LOGIN, null);

        btvResult = findViewById(R.id.btvResult);
        btvErrors = findViewById(R.id.btvErrorsCounter);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultTeammatesModeActivity.this, RatingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        result = (trueReplys / 15.0) * 100;
        result = Math.round(result);
        stringResult = Double.toString(result);
        btvResult.setText("Правильно: "+result+"%");

        errors = 15 - trueReplys;
        btvErrors.setText("Ошибок: "+errors);

        if (trueReplys == 15){
            money = 10;
        }
        if (trueReplys >= 10 && trueReplys < 15){
            money = 7;
        }
        if (trueReplys < 10 && trueReplys > 0){
            money = 3;
        }
        if (trueReplys == 0){
            money = 0;
        }

        String mMoney = Integer.toString(money);

        InsertData(login, stringResult, mMoney);
    }

    public void InsertData(final String login, final String result, final String money){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String LoginHolder = login;
                String ResultHolder = result;
                String MoneyHolder = money;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("login", LoginHolder));
                nameValuePairs.add(new BasicNameValuePair("result", ResultHolder));
                nameValuePairs.add(new BasicNameValuePair("money", MoneyHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();


                    HttpPost httpPost = new HttpPost(ServerURL);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Toast.makeText(ResultTeammatesModeActivity.this, "Спасибо\nВаш результат успешно сохранен", Toast.LENGTH_LONG).show();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login, result, money);
    }
}