package com.example.myappfaceauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_CAMERA = 111;
    public static int REQUEST_GALLERY = 222;

    Bitmap mSelectedImage;
    ImageView mImageView;
    Button btnCamara, btnGaleria;

    TextView txtResults;
    Permisos permisos;
    ArrayList<String> permisosNoAprobados;

    int tamañoImagen = 32;

    Bitmap imagenprocesada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResults = findViewById(R.id.txtresults);
        mImageView = findViewById(R.id.image_view);
        btnCamara = findViewById(R.id.btCamera);
        btnGaleria = findViewById(R.id.btGallery);
        ArrayList<String> permisos_requeridos = new ArrayList<String>();
        permisos_requeridos.add(android.Manifest.permission.CAMERA);
        permisos_requeridos.add(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        permisos_requeridos.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        permisos = new Permisos(this);

        permisosNoAprobados = permisos.getPermisosNoAprobados(permisos_requeridos);

        requestPermissions(permisosNoAprobados.toArray(new String[permisosNoAprobados.size()]),
                100);

    }

    //Para abrir la galeria
    public void abrirGaleria(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_GALLERY);
    }

    //Para abrir la camara
    public void abrirCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //Mostrar la imagene seleccionada ya sea foto o galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            try {

                if (requestCode == REQUEST_CAMERA) {
                    mSelectedImage = (Bitmap) data.getExtras().get("data");
                    int dimension = Math.min(mSelectedImage.getWidth(), mSelectedImage.getHeight());
                    mSelectedImage = ThumbnailUtils.extractThumbnail(mSelectedImage, dimension, dimension);

                    imagenprocesada = Bitmap.createScaledBitmap(mSelectedImage, tamañoImagen, tamañoImagen, false);
                }
                else{
                    mSelectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    imagenprocesada = Bitmap.createScaledBitmap(mSelectedImage, tamañoImagen, tamañoImagen, false);

                }
                txtResults.setText("");
                mImageView.setImageBitmap(mSelectedImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void OnClikProcesar (View view){
        if (mSelectedImage != null)
            txtResults.setText(ClasificarImg.clasificarImagen(this, imagenprocesada));
        else
            txtResults.setText("Debe seleccionar una imagen");
    }

}