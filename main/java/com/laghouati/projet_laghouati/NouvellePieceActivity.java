package com.laghouati.projet_laghouati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class NouvellePieceActivity extends AppCompatActivity {

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_piece);

        Intent i = getIntent();
        token = i.getStringExtra("token");

        loadPiecesImages();
    }


    public void creerPièce(View view){
        EditText txtnom = findViewById(R.id.nomPiece);
        String nom = txtnom.getText().toString();

        Spinner spinner = findViewById(R.id.spinnerPiece);
        String position = String.valueOf(spinner.getSelectedItemPosition()+1);

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/room-create")
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("name",nom)
                .addBodyParameter("idPicture", position)
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
                                Toast toast = Toast.makeText(NouvellePieceActivity.this,"Erreur lors de la création",Toast.LENGTH_SHORT);

                                toast.show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(NouvellePieceActivity.this,"Une erreur est survenue ",Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });

    }

    public void loadPiecesImages(){
        Context that = this;

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/pictures")
                .addQueryParameter("type","room")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray images = response.getJSONArray("pictures");

                            ArrayList<String> urlList = new ArrayList<>();
                            for(int i=0; i < images.length(); ++i){
                                final JSONObject url = images.getJSONObject(i);

                                urlList.add(url.getString("url"));

                            }
                            ImageAdapter adapter = new ImageAdapter(
                                    that,
                                    R.layout.image_item,
                                    urlList
                            );

                            Spinner spinner = findViewById(R.id.spinnerPiece);
                            spinner.setAdapter(adapter);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(NouvellePieceActivity.this,"impossible de récupérer les images",Toast.LENGTH_SHORT);

                        toast.show();
                    }

                });

    }
}