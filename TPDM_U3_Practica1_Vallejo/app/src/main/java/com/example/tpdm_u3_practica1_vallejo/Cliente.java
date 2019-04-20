package com.example.tpdm_u3_practica1_vallejo;

public class Cliente {
     String domicilio, nombre, sucursal;
     String matricula;

    public Cliente(){

    }
    public Cliente(String matricula, String nombre, String domicilio, String sucursal){
        this.matricula = matricula;

        this.nombre = nombre;
        this.domicilio = domicilio;
        this.sucursal = sucursal;

    }

}
