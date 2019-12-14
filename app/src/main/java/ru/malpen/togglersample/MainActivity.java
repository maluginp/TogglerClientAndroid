package ru.malpen.togglersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import ru.malpen.toggler.Toggler;
import ru.malpen.toggler.TogglerUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Toggler.init(this, "TEST-PRJ");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
//
//        Toggler.getInstance().setUser(new TogglerUser()
//                .setId("526bcd50-cbcd-4dad-b041-ccccd8542066")
//                .setUserName("pmalyugin")
//        );

        findViewById(R.id.tvTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean isEnabled = Toggler.getInstance().isFeatureEnabled("test.feature");
//                Toast.makeText(MainActivity.this, "Feature Enabled: " + ((isEnabled) ? "ENABLED" : "DISABLED"), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
