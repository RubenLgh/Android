package com.laghouati.projet_laghouati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

public class NouveauPeripheriqueActivity extends AppCompatActivity {

    private int id;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_peripherique);

        Intent i = getIntent();
        id = i.getIntExtra("id",0);
        token = i.getStringExtra("token");

        loadTypePeripherique();

    }

    public void creerPeripherique(View view){
        EditText txtnom = findViewById(R.id.nomPeripherique);
        String nom = txtnom.getText().toString();

        Spinner spinner = findViewById(R.id.spinnerPeripherique);
        String idtype = String.valueOf(spinner.getAdapter().getCount()-spinner.getSelectedItemPosition());

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/device-create")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("name",nom)
                .addBodyParameter("idDeviceType", idtype)
                .addBodyParameter("idRoom", String.valueOf(id))
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        switch (response.code())
                        {
                            case 200 :
                                finish();
                                break;

                            default:
                                Toast toast = Toast.makeText(NouveauPeripheriqueActivity.this,"Erreur lors de la création",Toast.LENGTH_SHORT);

                                toast.show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(NouveauPeripheriqueActivity.this,"Une erreur est survenue ",Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });
    }

    public void loadTypePeripherique(){
        Context that = this;

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/device-types")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray types = response.getJSONArray("device-types");

                            ArrayList<String> typeList = new ArrayList<>();
                            for(int i=0; i < types.length(); ++i){
                                final JSONObject type = types.getJSONObject(i);

                                typeList.add(type.getString("name"));

                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    that,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    typeList
                            );

                            Spinner spinner = findViewById(R.id.spinnerPeripherique);
                            spinner.setAdapter(adapter);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(NouveauPeripheriqueActivity.this,"impossible de récupérer les types de capteurs",Toast.LENGTH_SHORT);

                        toast.show();
                    }

                });
    }
}