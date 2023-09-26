package com.example.veterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Listar extends AppCompatActivity {
    ListView lvMascota;
    private List<String> listData = new ArrayList<>();
    private List<Integer> idData = new ArrayList<>();
    int idcliente;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        loadUI();

        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null){
            idcliente = parametros.getInt("idcliente");
            listarMascotas(idcliente);
        }
    }
    private void listarMascotas(int idcliente){
        listData.clear();
        idData.clear();
        adapter = new Adapter(this,listData);
        lvMascota.setAdapter(adapter);

        Uri.Builder URLUPdate = Uri.parse(Utilidades.URL+"mascota.php").buildUpon();
        URLUPdate.appendQueryParameter("operacion", "getPet");
        URLUPdate.appendQueryParameter("idCliente", String.valueOf(idcliente));
        String URLFinal = URLUPdate.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URLFinal,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0 ; i < response.length(); i++){
                                JSONObject jsonObject = new JSONObject(response.getString(i));
                                listData.add(jsonObject.getString("nombre"));
                                idData.add(jsonObject.getInt("idmascota"));
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error catch", String.valueOf(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                    }
                });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
    private void loadUI(){
        lvMascota = findViewById(R.id.lvMascota);
    }
}