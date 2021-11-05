package rannygaming.developers.footballfan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Button btnAuth;
    TextView register;
    static EditText login, password;
    static String sqlPassword, mdPassword;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Определение элементов
        login = findViewById(R.id.edLogin);
        password = findViewById(R.id.edPass);
        btnAuth = findViewById(R.id.btnSignUp);
        register = findViewById(R.id.tvRegistration);

        //Кнопка "Зарегистрироваться"
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Запоминаем данные о пользователе
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String mLogin = sharedPreferences.getString(KEY_LOGIN, null);

        //Если логин существует, значит авторизация была ранее -> переходим на Стартовый экран
        if (mLogin != null){
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }

        //Кнопка "Войти"
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Task().execute();
                mdPassword = Hash.md5(password.getText().toString());
                boolean result = mdPassword.equals(sqlPassword);
                if (result == true){
                    //Записываем данные пользователя в профиль
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_LOGIN, login.getText().toString());
                    editor.putString(KEY_PASSWORD, password.getText().toString());
                    editor.apply();

                    //Переходим на Стартовый экран
                    Intent intent = new Intent(MainActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();

                    //Сообщение о успешной авторизации
                    Toast.makeText(MainActivity.this,"Авторизация прошла успешно",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //SQL запрос
    static class Task extends AsyncTask<Void, Void, Void> {
        String error="";
        static String pass="";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(ModeLogoActivity.url, ModeLogoActivity.user, ModeLogoActivity.password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login = ?");
                preparedStatement.setString(1, login.getText().toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    pass = resultSet.getString("password");
                }
            }
            catch (Exception e){
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sqlPassword = pass;
            super.onPostExecute(aVoid);
        }
    }

    public static class Hash {
        public static String getHash(String txt, String hashType) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
                byte[] array = md.digest(txt.getBytes());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < array.length; ++i) {
                    sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
                }
                return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
            }
            return null;
        }

        public static String md5(String txt) {
            return Hash.getHash(txt, "MD5");
        }
    }
}