package com.theark.alert;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.theark.alert.Map.currentLocation;

/**
 * Created by jeffrywicaksana on 11/13/16.
 */

public class ReportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        final EditText textDesc = (EditText) findViewById(R.id.etDesc);
        Button reportButton = (Button) findViewById(R.id.bReport);
        Button cancelButton = (Button) findViewById(R.id.bCancel);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = textDesc.getText().toString();
                ViolenceLocation toReport = new ViolenceLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), desc);
                ArrayList<ViolenceLocation> reportLocations = new ArrayList<>();
                reportLocations.add(toReport);
                Call<List<ViolenceLocation>> reportCall = Map.client.reportLocation(reportLocations);
                reportCall.enqueue(new Callback<List<ViolenceLocation>>() {
                    @Override
                    public void onResponse(Call<List<ViolenceLocation>> call, Response<List<ViolenceLocation>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Report successful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ViolenceLocation>> call, Throwable t) {
                        Toast.makeText(getBaseContext(), "Report failed", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
