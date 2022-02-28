package com.laghouati.projet_laghouati;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.Response;

public class PeripheriqueAdapter extends ArrayAdapter<Peripherique> {
    ArrayList<Peripherique> periphs;

    Context context;
    String token;

    int resource;

    public PeripheriqueAdapter(Context context, int resource, ArrayList<Peripherique> periphs, String token){
        super(context, resource, periphs);
        this.context = context;
        this.resource = resource;
        this.periphs = periphs;
        this.token = token;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        ImageView imageView = view.findViewById(R.id.imagePeripherique);
        TextView textName = view.findViewById(R.id.textPeripherique);

        Peripherique peripherique = periphs.get(position);

        Picasso.get().load("https://myhouse.lesmoulinsdudev.com/"+peripherique.getPicture()).into(imageView);
        textName.setText(peripherique.getNom());

        Switch status = view.findViewById(R.id.statusPeripherique);

        if(peripherique.getStatus() == 0)
            status.setChecked(false);
        else status.setChecked(true);

        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/device-status")
                        .addHeaders("Authorization", "Bearer "+token)
                        .addBodyParameter("idDevice", String.valueOf(peripherique.getId()))
                        .addBodyParameter("status", String.valueOf(1-peripherique.getStatus()))
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

                                        Toast toast = Toast.makeText(context,"Erreur lors du changement de status",Toast.LENGTH_SHORT);

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

        Button btn = view.findViewById(R.id.supprButton2);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AndroidNetworking.post("https://myhouse.lesmoulinsdudev.com/device-delete")
                        .addHeaders("Authorization", "Bearer "+token)
                        .addBodyParameter("idDevice", String.valueOf(peripherique.getId()))
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
