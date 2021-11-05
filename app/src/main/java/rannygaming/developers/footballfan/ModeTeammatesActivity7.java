package rannygaming.developers.footballfan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModeTeammatesActivity7 extends AppCompatActivity {

    static ImageView imageView;
    static Button btnVar1;
    static Button btnVar2;
    static Button btnVar3;
    static Button btnVar4;

    public ProgressBar pb;
    CountDownTimer mCountDownTimer;
    static int counter = ModeTeammatesActivity6.counter;

    static String image_path = "";

    //Randomize
    static int min = 1;
    static int max = 4;
    static Random r = new Random();
    static int index = r.nextInt(max - min + 1) + min;
    //Randomize end

    //Валюта и подсказка
    static String login;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    Button btnHelp;
    static String help;
    static Button btvMoney;
    static String URL_Buy_Help = "https://rannygaming.ru/footballfanphp/buyhelp.php" ;
    Integer statusHelp = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_teammates);

        btvMoney = findViewById(R.id.btvMoney);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        login = sharedPreferences.getString(KEY_LOGIN, null);
        new Level_Money().execute();

        new Task().execute();

        btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setText("50 Footcoin");
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentMoney = btvMoney.getText().toString();
                Integer mMoney = Integer.valueOf(currentMoney);
                String money = "50";
                if (statusHelp == 0){
                    if (mMoney >= 50){
                        BuyHelp(login, URL_Buy_Help, money);
                        new ModePlayerActivity.Level_Money().execute();
                        statusHelp = 1;
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ModeTeammatesActivity7.this, R.style.BottomSheetDialogTheme);
                        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet_help,
                                (LinearLayout)findViewById(R.id.bottomSheetContainer));
                        TextView tvHelp = bottomSheetView.findViewById(R.id.tvHelp);
                        tvHelp.setText(help);
                        bottomSheetView.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.dismiss();
                            }
                        });
                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();
                    }
                } else if (mMoney < 50 && statusHelp == 0){
                    Toast.makeText(ModeTeammatesActivity7.this, "У вас недостаточно Footcoin`s", Toast.LENGTH_LONG).show();
                } else if (statusHelp == 1){
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ModeTeammatesActivity7.this, R.style.BottomSheetDialogTheme);
                    View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet_help,
                            (LinearLayout)findViewById(R.id.bottomSheetContainer));
                    TextView tvHelp = bottomSheetView.findViewById(R.id.tvHelp);
                    tvHelp.setText(help);
                    bottomSheetView.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomSheetDialog.dismiss();
                        }
                    });
                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();
                }
            }
        });


        btnVar1 = findViewById(R.id.btnVar1);
        btnVar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnVar1.getText().toString() == Task.replys){
                    counter++;
                }
                btnClick(ModeTeammatesActivity8.class);
            }
        });
        btnVar2 = findViewById(R.id.btnVar2);
        btnVar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnVar2.getText().toString() == Task.replys){
                    counter++;
                }
                btnClick(ModeTeammatesActivity8.class);
            }
        });
        btnVar3 = findViewById(R.id.btnVar3);
        btnVar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnVar3.getText().toString() == Task.replys){
                    counter++;
                }
                btnClick(ModeTeammatesActivity8.class);
            }
        });
        btnVar4 = findViewById(R.id.btnVar4);
        btnVar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnVar4.getText().toString() == Task.replys){
                    counter++;
                }
                btnClick(ModeTeammatesActivity8.class);
            }
        });

        pb = findViewById(R.id.pbTimer);
        mCountDownTimer = new CountDownTimer(21000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ millisUntilFinished);
                pb.setProgress((int) (millisUntilFinished/1000));
            }
            @Override
            public void onFinish() {
                Intent intent = new Intent(ModeTeammatesActivity7.this, ModeTeammatesActivity8.class);
                startActivity(intent);finish();
            }
        };
        mCountDownTimer.start();

        imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this).load(image_path).into(imageView);
    }

    //SQL запрос
    static class Task extends AsyncTask<Void, Void, Void> {
        String error = "";
        static String replys = "";
        String vars2 = "";
        String vars3 = "";
        String vars4 = "";

        static String path_to_image;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(ModeLogoActivity.url, ModeLogoActivity.user, ModeLogoActivity.password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM questions_mode_teammates WHERE id=?");
                preparedStatement.setString(1, String.valueOf(ModeTeammatesActivity.numAsk7));
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    path_to_image = resultSet.getString(2);
                    replys = resultSet.getString(3);
                    vars2 = resultSet.getString(4);
                    vars3 = resultSet.getString(5);
                    vars4 = resultSet.getString(6);
                }
                connection.close();
            } catch (Exception e) {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Glide.with(imageView.getContext()).load(path_to_image).into(imageView);
            if (index == 1){
                btnVar1.setText(replys);
                btnVar2.setText(vars2);
                btnVar3.setText(vars3);
                btnVar4.setText(vars4);
            } else if (index == 2){
                btnVar1.setText(vars2);
                btnVar2.setText(replys);
                btnVar3.setText(vars3);
                btnVar4.setText(vars4);
            } else if (index == 3){
                btnVar1.setText(vars3);
                btnVar2.setText(vars2);
                btnVar3.setText(replys);
                btnVar4.setText(vars4);
            } else {
                btnVar1.setText(vars4);
                btnVar2.setText(vars2);
                btnVar3.setText(vars3);
                btnVar4.setText(replys);
            }
            super.onPostExecute(aVoid);
        }
    }
    //SQL end

    public void btnClick(Class mClass) {
        mCountDownTimer.cancel();
        Intent intent = new Intent(this, mClass);
        startActivity(intent);
        finish();
    }

    //SQL запрос Level & Money
    static class Level_Money extends AsyncTask<Void, Void, Void> {
        String error="";
        static String mMoney;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(ModeLogoActivity.url, ModeLogoActivity.user, ModeLogoActivity.password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login = ?");
                preparedStatement.setString(1, login);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    mMoney = resultSet.getString(8);
                }
            }
            catch (Exception e){
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            btvMoney.setText(mMoney);
            super.onPostExecute(aVoid);
        }
    }

    public void BuyHelp(final String login, final String URL, final String money){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String LoginHolder = login;
                String MoneyHolder = money;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("login", LoginHolder));
                nameValuePairs.add(new BasicNameValuePair("money", MoneyHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();


                    HttpPost httpPost = new HttpPost(URL);
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
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login, money);
    }
}