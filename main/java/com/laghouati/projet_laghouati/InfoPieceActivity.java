package com.laghouati.projet_laghouati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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

public class InfoPieceActivity extends AppCompatActivity {

    private int id;
    private String token;

    ListView listCapteur;
    ListView listPeripherique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_piece);
        Intent i = getIntent();
        String nom = i.getStringExtra("nom");
        id = i.getIntExtra("id",0);
        token = i.getStringExtra("token");
        TextView textView = findViewById(R.id.textNomPiece);
        textView.setText(nom);

        listCapteur = findViewById(R.id.listCapteurs);
        listPeripherique = findViewById(R.id.listPeriphs);


    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    @Override
    protected void onResume(){
        super.onResume();

        loadCapteurs();
        loadPeriphs();
    }

    public void raffraichir(){
        loadCapteurs();
        loadPeriphs();
    }

    public void ajouterCapteur(View view){
        Intent nextActivity = new Intent(this,NouveauCapteurActivity.class);
        nextActivity.putExtra("token",token);
        nextActivity.putExtra("id",id);
        startActivity(nextActivity);
    }

    public void ajouterPeripherique(View view){
        Intent nextActivity = new Intent(this,NouveauPeripheriqueActivity.class);
        nextActivity.putExtra("token",token);
        nextActivity.putExtra("id",id);
        startActivity(nextActivity);
    }


    public void supprimerPiece(View view){
        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/room-delete")
                .addHeaders("Authorization", "Bearer "+token)
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
                                Toast toast = Toast.makeText(InfoPieceActivity.this,"Erreur lors de la suppression",Toast.LENGTH_SHORT);

                                toast.show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(InfoPieceActivity.this,"Une erreur est survenue ",Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });


    }

    public void loadCapteurs(){
        Context that = this;
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensors")
                .addHeaders("Authorization", "Bearer "+token)
                .addQueryParameter("idRoom", String.valueOf(id))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray capteurs = response.getJSONArray("sensors");

                            ArrayList<Capteur> capteurList = new ArrayList<>();
                            for(int i=0; i < capteurs.length(); ++i){
                                final JSONObject capteur = capteurs.getJSONObject(i);

                                capteurList.add(new Capteur(capteur.getString("name"),capteur.getInt("id"),capteur.getString("type"),capteur.getString("picture")));

                            }
                            CapteurAdapter adapter = new CapteurAdapter(that, R.layout.capteur_item,capteurList, token);



                            listCapteur.setAdapter(adapter);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(InfoPieceActivity.this,"impossible de récupérer les capteurs",Toast.LENGTH_SHORT);

                        toast.show();
                    }

                });

    }

    public void loadPeriphs(){
        Context that = this;
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/devices")
                .addHeaders("Authorization", "Bearer "+token)
                .addQueryParameter("idRoom", String.valueOf(id))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray periphs = response.getJSONArray("devices");

                            ArrayList<Peripherique> periphList = new ArrayList<>();
                            for(int i=0; i < periphs.length(); ++i){
                                final JSONObject periph = periphs.getJSONObject(i);

                                periphList.add(new Peripherique(periph.getString("name"),periph.getInt("id"),periph.getString("type"),periph.getString("picture"),periph.getInt("status")));
                            }
                            PeripheriqueAdapter adapter = new PeripheriqueAdapter(that, R.layout.peripherique_item,periphList, token);



                            listPeripherique.setAdapter(adapter);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(InfoPieceActivity.this,"impossible de récupérer les périphériques",Toast.LENGTH_SHORT);

                        toast.show();
                    }

                });


    }

    public void retour(View view){
        finish();
    }
}