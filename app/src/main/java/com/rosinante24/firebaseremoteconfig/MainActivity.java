package com.rosinante24.firebaseremoteconfig;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String LOADING_PHRASE_CONFIG_KEY = "loading_phrase";
    private static final String WELCOME_MESSAGE_KEY = "welcome_message";
    private static final String WELCOME_MESSAGE_CAPS_KEY = "welcome_message_caps";
    // belum dipakai
//    private static final String TEXT_COLOR = "color_text";
//    private static final String APA_BAE = "ntar_tong";

    @BindView(R.id.welcomeTextView)
    TextView welcomeTextView;
    @BindView(R.id.fetchButton)
    Button fetchButton;

    private FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        fetchWelcome();
    }

    @OnClick(R.id.fetchButton)
    public void onClick() {
        fetchWelcome();
    }

    private void fetchWelcome() {
        welcomeTextView.setText(firebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_KEY));

        long cacheExpiration = 3600;

        if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        firebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Berhasil brayy", Toast.LENGTH_SHORT).show();
                            firebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(MainActivity.this, "Gagal brayy", Toast.LENGTH_SHORT).show();
                        }

                        displayWelcomeMessage();
                    }
                });
    }

    private void displayWelcomeMessage() {

        String welcomemessage = firebaseRemoteConfig.getString(WELCOME_MESSAGE_KEY);

        //belum dipakai
//
//        String warna = firebaseRemoteConfig.getString(TEXT_COLOR);
//
//        boolean apa = firebaseRemoteConfig.getBoolean(APA_BAE);
//
//        int a =  apa ? Color.parseColor(warna) : ContextCompat.getColor(this, R.color.colorAccent);


        if (firebaseRemoteConfig.getBoolean(WELCOME_MESSAGE_CAPS_KEY)) {
            welcomeTextView.setAllCaps(true);
        } else {
            welcomeTextView.setAllCaps(false);
        }

        welcomeTextView.setText(welcomemessage);

        //belum dipakai
//        welcomeTextView.setTextColor(a);

    }
}
