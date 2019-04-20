package com.example.tpdm_u3_practica1_vallejo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientesActivity extends AppCompatActivity {
    ListView clientes;
    private DatabaseReference realtimeDatabase;
    List<Cliente> datos;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        clientes = findViewById(R.id.lista_clientes);

        realtimeDatabase = FirebaseDatabase.getInstance().getReference();

        //Llenar datos
        realtimeDatabase.child("cliente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datos = new ArrayList<>();

                if(dataSnapshot.getChildrenCount()<=0){
                    Toast.makeText(ClientesActivity.this, "ERROR: No hay datos",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                for(final DataSnapshot snap : dataSnapshot.getChildren()){
                    realtimeDatabase.child("cliente").child(snap.getKey()).addValueEventListener(
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

            private void cargarSelect(){
                if (datos.size()==0) return;
                String nombres[] = new String[datos.size()];

                for(int i = 0; i<nombres.length; i++){
                    Cliente c = datos.get(i);
                    nombres[i] = c.nombre;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ClientesActivity.this, android.R.layout.simple_list_item_1, nombres);
                clientes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cliente, menu);
        return true;
    }
}
