package rannygaming.developers.footballfan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SelectModeActivity extends AppCompatActivity {

    Button btnBack;
    Button btnShop;
    Button btnRating;

    Button btnLogoMode;
    Button btnPlayerMode;
    Button btnTrueFalseMode;
    Button btnTeammateMode;

    static Button btvMoney;
    static Button btvLevel;

    static String modePlayer, modeTF, modeTeammates;

    static String login;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        btvMoney = findViewById(R.id.btvMoney);
        btvLevel = findViewById(R.id.btvLevel);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        login = sharedPreferences.getString(KEY_LOGIN, null);

        new Level_Money().execute();

        btnShop = findViewById(R.id.btnShop);
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectModeActivity.this, StoreActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogoMode = findViewById(R.id.btnModeLogo);
        btnLogoMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FancyGifDialog.Builder(SelectModeActivity.this)
                        .setTitle("Угадай логотип") // You can also send title like R.string.from_resources
                        .setMessage("В этой категории вам нужно правильно ответить на вопрос\n'Логотип какой команды изображен на рисунке?'\nНа размышления есть 20 секунд") // or pass like R.string.description_from_resources
                        .setNegativeBtnText("Отмена") // or pass it like android.R.string.cancel
                        .setPositiveBtnBackground(R.color.ligth_orange) // or pass it like R.color.positiveButton
                        .setPositiveBtnText("Играть") // or pass it like android.R.string.ok
                        .setNegativeBtnBackground(R.color.colorSecondaryText) // or pass it like R.color.negativeButton
                        .setGifResource(R.drawable.ic_football_small_orange)
                        .isCancellable(true)
                        .OnPositiveClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                Intent intent = new Intent(SelectModeActivity.this, ModeLogoActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .OnNegativeClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                            }
                        })
                        .build();
            }
        });

        btnPlayerMode = findViewById(R.id.btnModePlayer);
        btnPlayerMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modePlayer.equals("0")){
                    new FancyGifDialog.Builder(SelectModeActivity.this)
                            .setTitle("Режим Угадай футболиста")
                            .setMessage("Данный режим игры для вас пока недоступен. Вы можете приобрести его в магазине")
                            .setNegativeBtnText("Отмена")
                            .setPositiveBtnBackground(R.color.ligth_yellow)
                            .setPositiveBtnText("Купить")
                            .setNegativeBtnBackground(R.color.colorSecondaryText)
                            .setGifResource(R.drawable.ic_football_player_small_yellow)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Intent intent = new Intent(SelectModeActivity.this, StoreActivity.class);
                                    startActivity(intent);
                                    finish();
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
                    new FancyGifDialog.Builder(SelectModeActivity.this)
                            .setTitle("Угадай футболиста")
                            .setMessage("В этом режиме вам необходимо угадать, какой футболист изображен на рисунке.\nНа размышления у вас есть 20 секунд")
                            .setNegativeBtnText("Отмена")
                            .setPositiveBtnBackground(R.color.ligth_yellow)
                            .setPositiveBtnText("Играть")
                            .setNegativeBtnBackground(R.color.colorSecondaryText)
                            .setGifResource(R.drawable.ic_football_player_small_yellow)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Intent intent = new Intent(SelectModeActivity.this, ModePlayerActivity.class);
                                    startActivity(intent);
                                    finish();
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

        btnTrueFalseMode = findViewById(R.id.btnTrueFalseMode);
        btnTrueFalseMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modeTF.equals("0")){
                    new FancyGifDialog.Builder(SelectModeActivity.this)
                            .setTitle("Режим Правда или Ложь")
                            .setMessage("Данный режим игры для вас пока недоступен. Вы можете приобрести его в магазине")
                            .setNegativeBtnText("Отмена")
                            .setPositiveBtnBackground(R.color.light_red)
                            .setPositiveBtnText("Купить")
                            .setNegativeBtnBackground(R.color.colorSecondaryText)
                            .setGifResource(R.drawable.ic_true_small_red)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Intent intent = new Intent(SelectModeActivity.this, StoreActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                }
                            })
                            .build();
                }
                if(modeTF.equals("1")){
                    new FancyGifDialog.Builder(SelectModeActivity.this)
                            .setTitle("Правда или Ложь")
                            .setMessage("В этом режиме вы должны решить, правдой или ложью является предложенное выражение")
                            .setNegativeBtnText("Отмена")
                            .setPositiveBtnBackground(R.color.light_red)
                            .setPositiveBtnText("Играть")
                            .setNegativeBtnBackground(R.color.colorSecondaryText)
                            .setGifResource(R.drawable.ic_true_small_red)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Intent intent = new Intent(SelectModeActivity.this, ModeTFActivity.class);
                                    startActivity(intent);
                                    finish();
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
        btnTeammateMode = findViewById(R.id.btnTeammateMode);
        btnTeammateMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modeTeammates.equals("0")){
                    new FancyGifDialog.Builder(SelectModeActivity.this)
                            .setTitle("Режим Одноклубники")
                            .setMessage("Данный режим игры для вас пока недоступен. Вы можете приобрести его в магазине")
                            .setNegativeBtnText("Отмена")
                            .setPositiveBtnBackground(R.color.ligth_blue)
                            .setPositiveBtnText("Купить")
                            .setNegativeBtnBackground(R.color.colorSecondaryText)
                            .setGifResource(R.drawable.ic_team_small_blue)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Intent intent = new Intent(SelectModeActivity.this, StoreActivity.class);
                                    startActivity(intent);
                                    finish();
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
                    new FancyGifDialog.Builder(SelectModeActivity.this)
                            .setTitle("Одноклубники")
                            .setMessage("В этом режиме вы должный выбрать футболиста, который был в одной команде с игроком представленном на рисунке")
                            .setNegativeBtnText("Отмена")
                            .setPositiveBtnBackground(R.color.ligth_blue)
                            .setPositiveBtnText("Играть")
                            .setNegativeBtnBackground(R.color.colorSecondaryText)
                            .setGifResource(R.drawable.ic_team_small_blue)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Intent intent = new Intent(SelectModeActivity.this, ModeTeammatesActivity.class);
                                    startActivity(intent);
                                    finish();
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
                Intent intent = new Intent(SelectModeActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRating = findViewById(R.id.btnRating);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectModeActivity.this, RatingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //SQL запрос
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
            modeTF = accessModeTF;
            modeTeammates = accessModeTeammates;
            super.onPostExecute(aVoid);
        }
    }
}