package rannygaming.developers.footballfan;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {

    String ServerURL = "https://rannygaming.ru/footballfanphp/registration.php";

    EditText edEmail, edLogin, edPass, edConfPass;
    Button register;
    TextView signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edEmail = findViewById(R.id.edEmail);
        edLogin = findViewById(R.id.edLogin);
        edPass = findViewById(R.id.edPass);
        edConfPass = findViewById(R.id.edPassConf);

        register = findViewById(R.id.btnSignUp);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = edPass.getText().toString().equals(edConfPass.getText().toString());
                if (result == true) {
                    boolean em, pass, pass2, log;
                    em = (edEmail.getText().toString()).equals("");
                    pass = (edPass.getText().toString()).equals("");
                    pass2 = (edConfPass.getText().toString()).equals("");
                    log = (edLogin.getText().toString()).equals("");
                    if (em == true || pass == true || pass2 == true || log == true){
                        Toast.makeText(RegisterActivity.this, "Заполните все поля", Toast.LENGTH_LONG).show();
                    } else {
                        InsertData(edLogin.getText().toString(), edEmail.getText().toString(), edPass.getText().toString());
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
                }
            }
        });

        signin = findViewById(R.id.tvSignIn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void InsertData(final String login, final String email, final String password){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String LoginHolder = login;
                String EmailHolder = email;
                String PassHolder = password;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("login", LoginHolder));
                nameValuePairs.add(new BasicNameValuePair("email", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("password", PassHolder));

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
                Toast.makeText(RegisterActivity.this, "Вы успешно зарегистрировались", Toast.LENGTH_LONG).show();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login, email, password);
    }
}