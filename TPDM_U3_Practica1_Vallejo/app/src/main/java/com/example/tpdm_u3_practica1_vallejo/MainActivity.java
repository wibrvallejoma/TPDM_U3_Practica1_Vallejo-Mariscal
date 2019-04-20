package com.example.tpdm_u3_practica1_vallejo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    Button clientes, empleados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clientes = findViewById(R.id.btnClientes);
        empleados = findViewById(R.id.btnEmpleados);

        clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vClientes = new Intent(MainActivity.this, ClientesActivity.class);
                startActivity(vClientes);
            }
        });

        empleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vEmpleados = new Intent(MainActivity.this, EmpleadosActivity.class);
                startActivity(vEmpleados);
            }
        });
    }
}
