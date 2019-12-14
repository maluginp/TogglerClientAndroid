package ru.malpen.togglersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import ru.malpen.toggler.Toggler;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toggler.init(this, "TEST-PRJ");
    }

    public void trackErrorEvent(View view) {
        Toggler.getInstance().trackEvent("TEST", "message", 100L);
    }
}
