package rannygaming.developers.footballfan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

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
import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    Button btnFootcoin;
    Button btnModePlayer;
    Button btnModeTrueFalse;
    Button btnModeTeammates;

    static Button btvMoney;
    static Button btvLevel;

    static String modePlayer;
    static String modeTrueFalse;
    static String modeTeammates;

    Button btnBack;
    Button btnFootcoinFree;
    private RewardedAd rewardedAd;

    static String login;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";

    static String URL_Mode_Player = "https://rannygaming.ru/footballfanphp/buymodeplayer.php" ;
    static String URL_Mode_TF = "https://rannygaming.ru/footballfanphp/buymodetruefalse.php" ;
    static String URL_Mode_Teammates = "https://rannygaming.ru/footballfanphp/buymodeteammates.php" ;
    static String URL_AddMoney = "https://rannygaming.ru/footballfanphp/addmoney.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        login = sharedPreferences.getString(KEY_LOGIN, null);

        //admob
        rewardedAd = new RewardedAd(this,
                "ca-app-pub-5367063362152379/7280114634");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        btnFootcoinFree = findViewById(R.id.btnFootcoinFree);
        btnFootcoinFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd.isLoaded()) {
                    Activity activityContext = StoreActivity.this;
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
                            String addMoney = "10";
                            AddMoney(login, URL_AddMoney, addMoney);
                            new Level_Money().execute();
                        }

                        @Override
                        public void onRewardedAdFailedToShow(AdError adError) {
                            // Ad failed to display.
                        }
                    };
                    rewardedAd.show(activityContext, adCallback);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
            }
        });


        btvMoney = findViewById(R.id.btvMoney);
        btvLevel = findViewById(R.id.btvLevel);

        new Level_Money().execute();

        btnFootcoin = findViewById(R.id.btnFootcoin);
        btnModePlayer = findViewById(R.id.btnPlayerMode);
        btnModePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modePlayer.equals("0")){
                    new FancyGifDialog.Builder(StoreActivity.this)
                            .setTitle("Режим Угадай футболиста") // You can also send title like R.string.from_resources
                            .setMessage("Данный режим можно приобрести на 500 Footcoin`s") // or pass like R.string.description_from_resources
                            .setNegativeBtnText("Отмена") // or pass it like android.R.string.cancel
                            .setPositiveBtnBackground(R.color.ligth_orange) // or pass it like R.color.positiveButton
                            .setPositiveBtnText("Купить") // or pass it like android.R.string.ok
                            .setNegativeBtnBackground(R.color.colorSecondaryText) // or pass it like R.color.negativeButton
                            .setGifResource(R.drawable.ic_football_player_small)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    String money = "500";
                                    String accessMode = "1";
                                    String currentMoney = btvMoney.getText().toString();
                                    Integer mMoney = Integer.valueOf(currentMoney);
                                    if (mMoney >= 500){
                                        BuyMode(login, accessMode, URL_Mode_Player, money);
                                        recreate();
                                    }
                                    if (mMoney < 500){
                                        Toast.makeText(StoreActivity.this, "У вас недостаточно Footcoin`s", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .build();
                }
                if(modePlayer.equals("1")){
                    new FancyGifDialog.Builder(StoreActivity.this)
                            .setTitle("Режим Угадай футболиста") // You can also send title like R.string.from_resources
                            .setMessage("Вы уже приобрели этот режим") // or pass like R.string.description_from_resources
                            .setNegativeBtnText("Отмена") // or pass it like android.R.string.cancel
                            .setPositiveBtnBackground(R.color.ligth_orange) // or pass it like R.color.positiveButton
                            .setPositiveBtnText("Продолжить") // or pass it like android.R.string.ok
                            .setNegativeBtnBackground(R.color.colorSecondaryText) // or pass it like R.color.negativeButton
                            .setGifResource(R.drawable.ic_football_player_small)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .build();
                }
            }
        });
        btnModeTrueFalse = findViewById(R.id.btnTrueFalseMode);
        btnModeTrueFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modeTrueFalse.equals("0")){
                    new FancyGifDialog.Builder(StoreActivity.this)
                            .setTitle("Режим Правда или Ложь") // You can also send title like R.string.from_resources
                            .setMessage("Данный режим можно приобрести на 1000 Footcoin`s") // or pass like R.string.description_from_resources
                            .setNegativeBtnText("Отмена") // or pass it like android.R.string.cancel
                            .setPositiveBtnBackground(R.color.ligth_orange) // or pass it like R.color.positiveButton
                            .setPositiveBtnText("Купить") // or pass it like android.R.string.ok
                            .setNegativeBtnBackground(R.color.colorSecondaryText) // or pass it like R.color.negativeButton
                            .setGifResource(R.drawable.ic_true_small)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    String money = "1000";
                                    String accessMode = "1";
                                    String currentMoney = btvMoney.getText().toString();
                                    Integer mMoney = Integer.valueOf(currentMoney);
                                    if (mMoney >= 1000){
                                        BuyMode(login, accessMode, URL_Mode_TF, money);
                                        recreate();
                                    }
                                    if (mMoney < 1000){
                                        Toast.makeText(StoreActivity.this, "У вас недостаточно Footcoin`s", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .build();
                }
                if(modeTrueFalse.equals("1")){
                    new FancyGifDialog.Builder(StoreActivity.this)
                            .setTitle("Режим Правда или Ложь") // You can also send title like R.string.from_resources
                            .setMessage("Вы уже приобрели этот режим") // or pass like R.string.description_from_resources
                            .setNegativeBtnText("Отмена") // or pass it like android.R.string.cancel
                            .setPositiveBtnBackground(R.color.ligth_orange) // or pass it like R.color.positiveButton
                            .setPositiveBtnText("Продолжить") // or pass it like android.R.string.ok
                            .setNegativeBtnBackground(R.color.colorSecondaryText) // or pass it like R.color.negativeButton
                            .setGifResource(R.drawable.ic_true_small)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .build();
                }
            }
        });
        btnModeTeammates = findViewById(R.id.btnTeammateMode);
        btnModeTeammates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modeTeammates.equals("0")){
                    new FancyGifDialog.Builder(StoreActivity.this)
                            .setTitle("Режим Одноклубники") // You can also send title like R.string.from_resources
                            .setMessage("Данный режим можно приобрести на 1500 Footcoin`s") // or pass like R.string.description_from_resources
                            .setNegativeBtnText("Отмена") // or pass it like android.R.string.cancel
                            .setPositiveBtnBackground(R.color.ligth_orange) // or pass it like R.color.positiveButton
                            .setPositiveBtnText("Купить") // or pass it like android.R.string.ok
                            .setNegativeBtnBackground(R.color.colorSecondaryText) // or pass it like R.color.negativeButton
                            .setGifResource(R.drawable.ic_team_small)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    String money = "1500";
                                    String accessMode = "1";
                                    String currentMoney = btvMoney.getText().toString();
                                    Integer mMoney = Integer.valueOf(currentMoney);
                                    if (mMoney >= 1500){
                                        BuyMode(login, accessMode, URL_Mode_Teammates, money);
                                        recreate();
                                    }
                                    if (mMoney < 1500){
                                        Toast.makeText(StoreActivity.this, "У вас недостаточно Footcoin`s", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .build();
                }
                if(modeTeammates.equals("1")){
                    new FancyGifDialog.Builder(StoreActivity.this)
                            .setTitle("Режим Одноклубники") // You can also send title like R.string.from_resources
                            .setMessage("Вы уже приобрели этот режим") // or pass like R.string.description_from_resources
                            .setNegativeBtnText("Отмена") // or pass it like android.R.string.cancel
                            .setPositiveBtnBackground(R.color.ligth_orange) // or pass it like R.color.positiveButton
                            .setPositiveBtnText("Продолжить") // or pass it like android.R.string.ok
                            .setNegativeBtnBackground(R.color.colorSecondaryText) // or pass it like R.color.negativeButton
                            .setGifResource(R.drawable.ic_team_small)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .build();
                }
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreActivity.this, SelectModeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //SQL запрос Level & Money
    static class Level_Money extends AsyncTask<Void, Void, Void> {
        String error="";
        static String mMoney;
        static String mLevel;
        static String accessModePlayers;
        static String accessModeTF;
        static String accessModeTeammates;

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
                    mLevel = resultSet.getString(9);
                    accessModePlayers = resultSet.getString(10);
                    accessModeTF = resultSet.getString(11);
                    accessModeTeammates = resultSet.getString(12);
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
            btvLevel.setText(mLevel+" Уровень");
            modePlayer = accessModePlayers;
            modeTrueFalse = accessModeTF;
            modeTeammates = accessModeTeammates;
            super.onPostExecute(aVoid);
        }
    }

    public void BuyMode(final String login, final String access, final String URL, final String money){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String LoginHolder = login;
                String AccessHolder = access;
                String MoneyHolder = money;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("login", LoginHolder));
                nameValuePairs.add(new BasicNameValuePair("access", AccessHolder));
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
                Toast.makeText(StoreActivity.this, "Вы купили новый режим игры", Toast.LENGTH_LONG).show();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(login, access, money);
    }

    public void AddMoney(final String login, final String URL, final String money){

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