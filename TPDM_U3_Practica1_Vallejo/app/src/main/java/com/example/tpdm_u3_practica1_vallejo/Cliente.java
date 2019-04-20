package com.example.tpdm_u3_practica1_vallejo;

public class Cliente {
    String nombre, domicilio, sucursal;
    int antiguedad;
    public void Cliente(){

    }
    public void Cliente(String nombre, String domicilio, String sucursal, int antiguedad){
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.sucursal = sucursal;
        this.antiguedad = antiguedad;
    }
}
