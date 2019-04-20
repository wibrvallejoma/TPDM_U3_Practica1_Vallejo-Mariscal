package com.example.tpdm_u3_practica1_vallejo;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmpleadosActivity extends AppCompatActivity {
    ListView empleados;
    private DatabaseReference realtimeDatabase;
    Button insertar, eliminar, consultar;
    EditText puesto, matricula, nombre, sucursal;

    List<Empleado> datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);

        insertar = findViewById(R.id.empleadoInsertar);
        eliminar = findViewById(R.id.empleadoEliminar);
        consultar = findViewById(R.id.empleadoConsultar);

        matricula = findViewById(R.id.empleadoMatricula);
        nombre = findViewById(R.id.empleadoNombre);
        puesto = findViewById(R.id.empleadoPuesto);
        sucursal = findViewById(R.id.empleadoSucursal);

        empleados = findViewById(R.id.lista_empleados);

        realtimeDatabase = FirebaseDatabase.getInstance().getReference();

        //Llenar datos
        realtimeDatabase.child("Empleado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datos = new ArrayList<>();

                if(dataSnapshot.getChildrenCount()<=0){
                    Toast.makeText(EmpleadosActivity.this, "ERROR: No hay datos",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                for(final DataSnapshot snap : dataSnapshot.getChildren()){
                    realtimeDatabase.child("Empleado").child(snap.getKey()).addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Empleado e = dataSnapshot.getValue(Empleado.class);

                                    if (e != null) {
                                        datos.add(e);
                                    }

                                    cargarSelect();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            }
                    );
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Insertar actualizar
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Empleado empleadoNuevo = new Empleado(matricula.getText().toString(),
                        nombre.getText().toString(), puesto.getText().toString(),
                        sucursal.getText().toString());

                realtimeDatabase.child("Empleado").child(empleadoNuevo.matricula).setValue(empleadoNuevo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EmpleadosActivity.this, "AGREGADO EXITOSAMENTE",
                                        Toast.LENGTH_SHORT).show();
                                nombre.setText(""); matricula.setText(""); puesto.setText(""); sucursal.setText("");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EmpleadosActivity.this, "ERROR AL AGREGAR", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirEliminar();
            }
        });

        empleados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                matricula.setText(datos.get(position).matricula);
                nombre.setText(datos.get(position).nombre);
                puesto.setText(datos.get(position).puesto);
                sucursal.setText(datos.get(position).sucursal);
            }
        });

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirConsultar();
            }
        });
    }

    private void pedirEliminar() {
        final EditText id = new EditText(this);
        id.setHint("MATRICULA A ELIMINAR");
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("ELIMINAR EMPLEADO").setMessage("MATRICULA A ELIMINAR:").setView(id).setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminar(id.getText().toString());
            }
        }).setNegativeButton("Cancelar", null).show();
    }

    private void eliminar(String id) {
        realtimeDatabase.child("Empleado").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EmpleadosActivity.this, "ELIMINADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EmpleadosActivity.this, "ERROR AL ELIMINAR", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pedirConsultar() {
        final EditText id = new EditText(this);
        id.setHint("MATRICULA A BUSCAR");
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("BUSCAR EMPLEADO").setMessage("MATRICULA A BUSCAR:").setView(id).setPositiveButton("BUSCAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                consultar(id.getText().toString());
            }
        }).setNegativeButton("Cancelar", null).show();
    }

    private void consultar(String i) {
        FirebaseDatabase.getInstance().getReference().child("Empleado").child(i)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Empleado sempleado = dataSnapshot.getValue(Empleado.class);

                        if(sempleado!=null) {
                            matricula.setText(sempleado.matricula);
                            nombre.setText(sempleado.nombre);
                            puesto.setText(sempleado.puesto);
                            sucursal.setText(sempleado.sucursal);
                        } else {
                            Toast.makeText(EmpleadosActivity.this, "No se encontró dato a mostrar", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void cargarSelect() {
        if (datos.size()==0) return;
        String nombres[] = new String[datos.size()];

        for(int i = 0; i<nombres.length; i++){
            Empleado e = datos.get(i);
            nombres[i] = e.matricula + "\n" + e.nombre + "\n" + e.puesto;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(EmpleadosActivity.this, android.R.layout.simple_list_item_1, nombres);
        empleados.setAdapter(adapter);
    }
}
