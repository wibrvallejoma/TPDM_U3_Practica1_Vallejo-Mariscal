package com.example.tpdm_u3_practica1_vallejo;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientesActivity extends AppCompatActivity {
    ListView clientes;
    private DatabaseReference realtimeDatabase;
    Button insertar, eliminar, consultar;
    EditText domicilio, matricula, nombre, sucursal;

    List<Cliente> datos;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        insertar = findViewById(R.id.clienteInsertar);
        eliminar = findViewById(R.id.clienteEliminar);
        consultar = findViewById(R.id.clienteConsultar);

        nombre = findViewById(R.id.clienteNombre);
        matricula = findViewById(R.id.clienteMatricula);
        domicilio = findViewById(R.id.clienteDomicilio);
        sucursal = findViewById(R.id.clienteSucursal);

        clientes = findViewById(R.id.lista_clientes);

        realtimeDatabase = FirebaseDatabase.getInstance().getReference();


        //Llenar datos
        realtimeDatabase.child("Cliente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datos = new ArrayList<>();

                if(dataSnapshot.getChildrenCount()<=0){
                    Toast.makeText(ClientesActivity.this, "ERROR: No hay datos",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                for(final DataSnapshot snap : dataSnapshot.getChildren()){
                    realtimeDatabase.child("Cliente").child(snap.getKey()).addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Cliente c = dataSnapshot.getValue(Cliente.class);

                                    if (c != null) {
                                        datos.add(c);
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

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Cliente clienteNuevo = new Cliente(matricula.getText().toString(),
                         nombre.getText().toString(),domicilio.getText().toString(),
                        sucursal.getText().toString());

                realtimeDatabase.child("Cliente").child(clienteNuevo.matricula).setValue(clienteNuevo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ClientesActivity.this, "AGREGADO EXITOSAMENTE",
                                        Toast.LENGTH_SHORT).show();
                                nombre.setText(""); matricula.setText(""); domicilio.setText(""); sucursal.setText("");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ClientesActivity.this, "ERROR AL AGREGAR", Toast.LENGTH_SHORT).show();
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

        clientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                matricula.setText(datos.get(position).matricula);
                nombre.setText(datos.get(position).nombre);
                domicilio.setText(datos.get(position).domicilio);
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
        FirebaseDatabase.getInstance().getReference().child("Cliente").child(i)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Cliente scliente = dataSnapshot.getValue(Cliente.class);

                        if(scliente!=null) {
                            matricula.setText(scliente.matricula);
                            nombre.setText(scliente.nombre);
                            domicilio.setText(scliente.domicilio);
                            sucursal.setText(scliente.sucursal);
                        } else {
                            Toast.makeText(ClientesActivity.this, "No se encontró dato a mostrar", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void pedirEliminar() {

            final EditText id = new EditText(this);
            id.setHint("MATRICULA A ELIMINAR");
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("ELIMINAR CLIENTE").setMessage("MATRICULA A ELIMINAR:").setView(id).setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    eliminar(id.getText().toString());
                }
            }).setNegativeButton("Cancelar", null).show();



    }

    private void eliminar(String id) {
        realtimeDatabase.child("Cliente").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ClientesActivity.this, "ELIMINADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ClientesActivity.this, "ERROR AL ELIMINAR", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void cargarSelect(){
        if (datos.size()==0) return;
        String nombres[] = new String[datos.size()];
        
        for(int i = 0; i<nombres.length; i++){
            Cliente c = datos.get(i);
            nombres[i] = c.matricula + "\n" + c.nombre;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ClientesActivity.this, android.R.layout.simple_list_item_1, nombres);
        clientes.setAdapter(adapter);
    }


}
