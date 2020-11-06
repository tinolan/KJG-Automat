package com.example.kjg_automat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sumup.merchant.reader.models.TransactionInfo;
import com.sumup.merchant.reader.api.SumUpAPI;
import com.sumup.merchant.reader.api.SumUpLogin;
import com.sumup.merchant.reader.api.SumUpPayment;


public class MainActivity extends Activity {

    private static final String TAG = "Automat-Aktivität";
    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_PAYMENT = 2;
    private static final int REQUEST_CODE_PAYMENT_SETTINGS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        final Button login = (Button) findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login.setText("Button Login gedrückt, Login wird gestartet");
                SumUpLogin automat_login = SumUpLogin.builder("155d4d1d-ca6c-4032-9020-2dec4ba1c8eb").build();
                SumUpAPI.openLoginActivity(MainActivity.this, automat_login, REQUEST_CODE_LOGIN);

            }

            });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}