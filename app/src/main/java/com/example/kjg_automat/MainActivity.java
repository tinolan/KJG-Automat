package com.example.kjg_automat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sumup.merchant.reader.api.SumUpAPI;
import com.sumup.merchant.reader.api.SumUpLogin;
import com.sumup.merchant.reader.api.SumUpPayment;
import com.sumup.merchant.reader.models.TransactionInfo;

import com.google.android.things.userdriver.location.GnssDriver;
import com.google.android.things.userdriver.UserDriverManager;

import android.bluetooth.BluetoothClass;
import com.google.android.things.bluetooth.BluetoothClassFactory;
import com.google.android.things.bluetooth.BluetoothConfigManager;

import java.math.BigDecimal;
import java.util.UUID;


public class MainActivity extends Activity {

    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_PAYMENT = 2;
    private static final int REQUEST_CODE_PAYMENT_SETTINGS = 3;

    private TextView mResultCode;
    private TextView mResultMessage;
    private TextView mTxCode;
    private TextView mReceiptSent;
    private TextView mTxInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        Button btnLogin = (Button) findViewById(R.id.button_login);
        btnLogin.setText("Login");
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SumUpLogin automat_login = SumUpLogin.builder("155d4d1d-ca6c-4032-9020-2dec4ba1c8eb").build();
                SumUpAPI.openLoginActivity(MainActivity.this, automat_login, REQUEST_CODE_LOGIN);
                mResultCode.setText("Eingeloggt");
            }
            });

        Button btnCharge = (Button) findViewById(R.id.button_charge);
        btnCharge.setText("Zahlung");
        btnCharge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SumUpPayment payment = SumUpPayment.builder()
                        // mandatory parameters
                        .total(new BigDecimal("1.20")) // minimum 1.00
                        .currency(SumUpPayment.Currency.EUR)
                        // optional: add details
                        .title("Einkauf XX")
                        .receiptEmail("sumup@miniwecker.de")
                        // optional: foreign transaction ID, must be unique!
                        .foreignTransactionId(UUID.randomUUID().toString()) // can not exceed 128 chars
                        .build();

                SumUpAPI.checkout(MainActivity.this, payment, REQUEST_CODE_PAYMENT);
            }
        });

        Button btnLogout = (Button) findViewById(R.id.button_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SumUpAPI.logout();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        resetViews();

        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (data != null) {
                    Bundle extra = data.getExtras();
                    mResultCode.setText("Result code: " + extra.getInt(SumUpAPI.Response.RESULT_CODE));
                    mResultMessage.setText("Message: " + extra.getString(SumUpAPI.Response.MESSAGE));
                }
                break;

            case REQUEST_CODE_PAYMENT:
                if (data != null) {
                    Bundle extra = data.getExtras();

                    mResultCode.setText("Result code: " + extra.getInt(SumUpAPI.Response.RESULT_CODE));
                    mResultMessage.setText("Message: " + extra.getString(SumUpAPI.Response.MESSAGE));

                    String txCode = extra.getString(SumUpAPI.Response.TX_CODE);
                    mTxCode.setText(txCode == null ? "" : "Transaction Code: " + txCode);

                    boolean receiptSent = extra.getBoolean(SumUpAPI.Response.RECEIPT_SENT);
                    mReceiptSent.setText("Receipt sent: " + receiptSent);

                    TransactionInfo transactionInfo = extra.getParcelable(SumUpAPI.Response.TX_INFO);
                    mTxInfo.setText(transactionInfo == null ? "" : "Transaction Info : " + transactionInfo);
                }
                break;

            case REQUEST_CODE_PAYMENT_SETTINGS:
                if (data != null) {
                    Bundle extra = data.getExtras();
                    mResultCode.setText("Result code: " + extra.getInt(SumUpAPI.Response.RESULT_CODE));
                    mResultMessage.setText("Message: " + extra.getString(SumUpAPI.Response.MESSAGE));
                }
                break;

            default:
                break;
        }
    }

    private void resetViews() {
        mResultCode.setText("");
        mResultMessage.setText("");
        mTxCode.setText("");
        mReceiptSent.setText("");
        mTxInfo.setText("");
    }

    private void findViews() {
        mResultCode = (TextView) findViewById(R.id.result);
        mResultMessage = (TextView) findViewById(R.id.result_msg);
        mTxCode = (TextView) findViewById(R.id.tx_code);
        mReceiptSent = (TextView) findViewById(R.id.receipt_sent);
        mTxInfo = (TextView) findViewById(R.id.tx_info);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SumUpAPI.logout();

    }

}