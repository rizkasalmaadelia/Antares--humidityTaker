package com.rizkasalma.antares;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class MainActivity extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener{

    private Button btnRefresh;
    private Button btnOn;
    private Button btnOff;

    private TextView txtData;
    private String TAG = "ANTARES-API";
    private AntaresHTTPAPI antaresAPIHTTP;
    private String dataDevice;
    private String datatanah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Inisialisasi UI yang digunakan di aplikasi --- //
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnOff = (Button) findViewById(R.id.btnOff);
        btnOn = (Button) findViewById(R.id.btnOn);

        txtData = (TextView) findViewById(R.id.txtData);

        // --- Inisialisasi API Antares --- //
        //antaresAPIHTTP = AntaresHTTPAPI.getInstance();
        antaresAPIHTTP = new AntaresHTTPAPI();
        antaresAPIHTTP.addListener(this);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.getLatestDataofDevice("XXXXXXXXX","Sarjanatani","lembab");

            }
        });

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.storeDataofDevice(1,"XXXXXXXXX", "sarjanatani", "SensorKelembaban", "{\\\"status\\\":\\\"1\\\"}");

            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.storeDataofDevice(1,"XXXXXXXXX", "sarjanatani", "SensorKelembaban", "{\\\"status\\\":\\\"0\\\"}");
            }
        });
    }

    @Override
    public void onResponse(AntaresResponse antaresResponse) {
        // --- Cetak hasil yang didapat dari ANTARES ke System Log --- //
        //Log.d(TAG,antaresResponse.toString());
        Log.d(TAG,Integer.toString(antaresResponse.getRequestCode()));
        if(antaresResponse.getRequestCode()==0){
            try {
                JSONObject body = new JSONObject(antaresResponse.getBody());
                dataDevice = body.getJSONObject("m2m:cin").getString("con");
                datatanah = body.getString("con");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtData.setText(datatanah);
                        Toast.makeText(getApplicationContext(), dataDevice, Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d(TAG,dataDevice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
