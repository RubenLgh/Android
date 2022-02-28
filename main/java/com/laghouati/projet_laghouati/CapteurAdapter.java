package com.laghouati.projet_laghouati;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

public class CapteurAdapter extends ArrayAdapter<Capteur> {
    ArrayList<Capteur> capteurs;

    Context context;
    String token;

    int resource;

    public CapteurAdapter(Context context, int resource, ArrayList<Capteur> capteurs, String token){
        super(context, resource, capteurs);
        this.context = context;
        this.resource = resource;
        this.capteurs = capteurs;
        this.token = token;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        ImageView imageView = view.findViewById(R.id.imageCapteur);
        TextView textName = view.findViewById(R.id.textCapteur);
        TextView valeur = view.findViewById(R.id.valeurCapteur);

        Capteur capteur = capteurs.get(position);

        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/sensor-value")
                .addHeaders("Authorization", "Bearer "+token)
                .addQueryParameter("idSensor", String.valueOf(capteur.getId()))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            valeur.setText(response.getString("value")+response.getString("unit"));

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(context,"impossible de récupérer les valeurs ",Toast.LENGTH_SHORT);

                        toast.show();
                    }

                });



        Picasso.get().load("https://myhouse.lesmoulinsdudev.com/"+capteur.getPicture()).into(imageView);
        textName.setText(capteur.getNom());

        Button btn = view.findViewById(R.id.supprButton);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/sensor-delete")
                        .addHeaders("Authorization", "Bearer "+token)
                        .addBodyParameter("idSensor", String.valueOf(capteur.getId()))
                        .build()
                        .getAsOkHttpResponse(new OkHttpResponseListener() {
                            @Override
                            public void onResponse(Response response) {
                                switch (response.code())
                                {
                                    case 200 :
                                        ((InfoPieceActivity)context).raffraichir();
                                        break;

                                    default:

                                        Toast toast = Toast.makeText(context,"Erreur lors de la suppression",Toast.LENGTH_SHORT);

                                        toast.show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast toast = Toast.makeText(context,"Une erreur est survenue ",Toast.LENGTH_SHORT);

                                toast.show();
                            }
                        });

            }
        });



        return view;
    }



}
