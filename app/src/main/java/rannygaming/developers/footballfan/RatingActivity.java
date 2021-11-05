package rannygaming.developers.footballfan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class RatingActivity extends AppCompatActivity {

    Button btnBack;
    static Button ratingLogo, ratingPlayer, ratingTF, ratingTeammates;
    static String login;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingLogo = findViewById(R.id.btnLogoRating);
        ratingPlayer = findViewById(R.id.btnPlayerRating);
        ratingTF = findViewById(R.id.btnTrueFalseRating);
        ratingTeammates = findViewById(R.id.btnTeammateRating);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        login = sharedPreferences.getString(KEY_LOGIN, null);

        new Task().execute();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RatingActivity.this, SelectModeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //SQL запрос
    static class Task extends AsyncTask<Void, Void, Void> {
        String error="";
        static String md_logo, md_player, md_teammate, md_true_false;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(ModeLogoActivity.url, ModeLogoActivity.user, ModeLogoActivity.password);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login = ?");
                preparedStatement.setString(1, login);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    md_logo = resultSet.getString(4);
                    md_player = resultSet.getString(5);
                    md_true_false = resultSet.getString(6);
                    md_teammate = resultSet.getString(7);
                }
            }
            catch (Exception e){
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ratingLogo.setText(md_logo+"%");
            ratingPlayer.setText(md_player+"%");
            ratingTF.setText(md_true_false+"%");
            ratingTeammates.setText(md_teammate+"%");
            super.onPostExecute(aVoid);
        }
    }

}