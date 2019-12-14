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

        Toggler.init(this, "TEST-PRJ");
//
//        Toggler.getInstance().setUser(new TogglerUser()
//                .setId("526bcd50-cbcd-4dad-b041-ccccd8542066")
//                .setUserName("pmalyugin")
//        );

    }

    public void trackErrorEvent(View view) {
        Toggler.getInstance().trackEvent("TEST", "message", 100L);
    }
}
