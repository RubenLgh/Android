package com.laghouati.projet_laghouati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PiecesActivity extends AppCompatActivity {

    private String token;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieces);
        Intent i = getIntent();
        token = i.getStringExtra("token");




         list = findViewById(R.id.pieceList);
         list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Piece piece = (Piece)adapterView.getItemAtPosition(i);
                 Intent intent = new Intent(PiecesActivity.this,InfoPieceActivity.class);
                 //based on item add info to intent
                 intent.putExtra("nom",piece.getName());
                 intent.putExtra("id",piece.getId());
                 intent.putExtra("token",token);
                 startActivity(intent);
             }
         });
    }
    @Override
    protected void onResume(){
        super.onResume();
        loadPieces();
    }

    public void creerPiece(View view){
        Intent nextActivity = new Intent(this,NouvellePieceActivity.class);
        nextActivity.putExtra("token",token);
        startActivity(nextActivity);
    }

    public void loadPieces(){
        Context that = this;
        AndroidNetworking.get("https://myhouse.lesmoulinsdudev.com/rooms")
                .addHeaders("Authorization", "Bearer "+token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray pieces = response.getJSONArray("rooms");

                            ArrayList<Piece> pieceList = new ArrayList<>();
                            for(int i=0; i < pieces.length(); ++i){
                                final JSONObject piece = pieces.getJSONObject(i);

                                pieceList.add(new Piece(piece.getString("name"),piece.getString("picture"),piece.getInt("id")));

                            }
                            PieceAdapter adapter = new PieceAdapter(that, R.layout.piece_item,pieceList);



                            list.setAdapter(adapter);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(PiecesActivity.this,"impossible de récupérer les pièces",Toast.LENGTH_SHORT);

                        toast.show();
                    }

                });
    }



}