package com.example.lazcarate.birthdayhelper;

import android.graphics.Bitmap;

/**
 * Created by lazcarate on 31/1/16.
 */
public class Agenda {

    private String nombre;
    private String telefono;
    private Bitmap foto;
    private String id;
    private String aviso;
    private String fechaN;

    public Agenda(String id, String nombre, String telefono, Bitmap foto, String aviso, String fechaN) {

        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.foto= foto;
        this.aviso = aviso;
        this.telefono = telefono;
        this.fechaN = fechaN;

    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAviso() {
        return aviso;
    }

    public void setAviso(String aviso) {
        this.aviso = aviso;
    }

    public String getFechaN() {
        return fechaN;
    }

    public void setFechaN(String fechaN) {
        this.fechaN = fechaN;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}


