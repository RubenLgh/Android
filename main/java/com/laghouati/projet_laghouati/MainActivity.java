package com.laghouati.projet_laghouati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {    //Démarrage de l'application
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void inscription(View view){ //Ouverture de l'activité inscription
        Intent intent = new Intent(this,InscriptionActivity.class);

        startActivity(intent);
    }

    public void connexion(View view){ //Requete post vers l'api pour se connecter
        EditText txtmail = findViewById(R.id.Email);
        String mail = txtmail.getText().toString();

        EditText txtmdp = findViewById(R.id.Pwd);
        String mdp = txtmdp.getText().toString();

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/auth")
                .addBodyParameter("login",mail)
                .addBodyParameter("password", mdp)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("token");     //On recupere le token

                            Intent nextActivity = new Intent(MainActivity.this,PiecesActivity.class);
                            nextActivity.putExtra("token",token);
                            startActivity(nextActivity);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(MainActivity.this,"Mauvais identifiant ou mot de passe",Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });
    }
}