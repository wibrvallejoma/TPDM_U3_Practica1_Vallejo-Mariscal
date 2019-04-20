package com.example.tpdm_u3_practica1_vallejo;

public class Empleado {
    String nombre, puesto, sucursal;
    int horasTrabajo;

    public void Empleado(){

    }
    public void Empleado(String nombre, String puesto, String sucursal, int horasTrabajo){
        this.nombre = nombre;
        this.puesto = puesto;
        this.sucursal = sucursal;
        this.horasTrabajo = horasTrabajo;
    }
}
