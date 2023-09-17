package com.example.myappfaceauthentication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

//Estos es para los permisos,abre la ventanita
public class Permisos {
    private AppCompatActivity activity;

    public Permisos(AppCompatActivity activity) {
        this.activity = activity;
    }

    public ArrayList<String> getPermisosNoAprobados(ArrayList<String>  listaPermisos) {
        ArrayList<String> list = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23)
            for(String permiso: listaPermisos) {
                if (activity.checkSelfPermission(permiso) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permiso);
                }
            }
        return list;
    }
}

