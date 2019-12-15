package ru.malpen.togglersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import ru.malpen.toggler.HttpPacket;
import ru.malpen.toggler.PerformanceType;
import ru.malpen.toggler.Toggler;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toggler.init(this, "TEST-PRJ");
    }

    public void trackErrorEvent(View view) {
        Toggler.getInstance().trackEvent("TEST", "Error message", 100L);
    }

    public void trackWarningEvent(View view) {
        Toggler.getInstance().trackWarning("TEST", "Warning message", 100L);
    }

    public void trackHttpEvent(View view) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        HttpPacket requestPacket = new HttpPacket();
        requestPacket.setBody("{test: test}");
        requestPacket.setHeaders(headers);

        HttpPacket responsePacket = new HttpPacket();
        responsePacket.setBody("{test2: test2}");
        responsePacket.setHeaders(headers);


        Toggler.getInstance().trackHttp(
                "http://yandex.ru",
                "POST",
                200,
                requestPacket,
                responsePacket,
                100L
        );
    }

    public void trackPerformanceScreenEvent(View view) {
        Toggler.getInstance().trackPerformance(
                PerformanceType.Screen,
                100,
                "ScreenEvent",
                "Done",
                100L
        );
    }

    public void trackPerformanceCaseEvent(View view) {
        Toggler.getInstance().trackPerformance(
                PerformanceType.Case,
                1000,
                "CaseEvent",
                "Done",
                100L
        );
    }


}
