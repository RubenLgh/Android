package com.laghouati.projet_laghouati;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import okhttp3.Response;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        AndroidNetworking.initialize(this);
    }

    public void inscription(View view){
        EditText txtnom = findViewById(R.id.TextName);
        String nom = txtnom.getText().toString();

        EditText txtmdp = findViewById(R.id.TextMdp);
        String mdp = txtmdp.getText().toString();

        EditText txtemail = findViewById(R.id.TextEmail);
        String email = txtemail.getText().toString();

        AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/register")
                .addBodyParameter("name",nom)
                .addBodyParameter("login",email)
                .addBodyParameter("password",mdp)
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
                                Toast toast = Toast.makeText(InscriptionActivity.this,"Erreur lors de l'enregistrement",Toast.LENGTH_SHORT);

                                toast.show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(InscriptionActivity.this,"Une erreur est survenue ",Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });
    }
}