package rannygaming.developers.footballfan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    TextView password;
    static TextView login;
    Button btnLogout, btnBack;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        login = findViewById(R.id.tvLogin);
        password = findViewById(R.id.tvPassword);

        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String mLogin = sharedPreferences.getString(KEY_LOGIN, null);
        String mPassword = sharedPreferences.getString(KEY_PASSWORD, null);

        if (mLogin != null || mPassword != null){
            login.setText(mLogin);
            password.setText(mPassword);
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Toast.makeText(ProfileActivity.this,"Вы успешно вышли из учетной записи",Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}