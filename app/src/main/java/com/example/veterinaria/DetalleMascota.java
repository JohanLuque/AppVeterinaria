package com.example.veterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class DetalleMascota extends AppCompatActivity {
    EditText etNombre, etColor, etGenero, etAnimal, etRaza;
    ImageView ivFotografia;
    int idmascota;
    final String URLImage = "http://192.168.18.12:81/veterinaria/image/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mascota);
        loadUI();
        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null){
            idmascota = parametros.getInt("idmascota");
        }
        mostrarDatos(idmascota);
    }
    private void loadUI(){
        etNombre = findViewById(R.id.etDetNombre);
        etColor = findViewById(R.id.etDetColor);
        etGenero = findViewById(R.id.etDetGenero);
        etAnimal = findViewById(R.id.etDetAnimal);
        etRaza = findViewById(R.id.etDetRaza);
        ivFotografia = findViewById(R.id.ivFotografia);
    }
    private void mostrarDatos(int idmascota){
        Uri.Builder URLUpate = Uri.parse(Utilidades.URL+"mascota.php").buildUpon();
        URLUpate.appendQueryParameter("operacion", "getDetails");
        URLUpate.appendQueryParameter("idmascota", String.valueOf(idmascota));
        String URLFinal = URLUpate.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLFinal, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Log.e("Error", response.toString());
                    //Log.d("datos", response.getString("fotografia"));
                    etNombre.setText(response.getString("nombre"));
                    etAnimal.setText(response.getString("nombreAnimal"));
                    etRaza.setText(response.getString("nombreRaza"));
                    etColor.setText(response.getString("color"));
                    etGenero.setText(response.getString("genero"));
                    //fotografia = response.getString("fotografia");
                    leerImagen(response.getString("fotografia"));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Volley", error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public void leerImagen(String image){
        String URLLeerImage= URLImage+image;
        ImageRequest imageRequest = new ImageRequest(
                URLLeerImage,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ivFotografia.setImageBitmap(response);
                    }
                }, 0,
                0,
                null,
                null,
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
        Volley.newRequestQueue(this).add(imageRequest);
    }
}