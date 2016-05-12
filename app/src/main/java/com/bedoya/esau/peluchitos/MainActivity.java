package com.bedoya.esau.peluchitos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    TextView tresul;
    EditText enom, ecant, evu;
    Button bnuevo, bact, bbucar, beliminar, binven, bventas,bgan,blimpiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tresul=(TextView) findViewById(R.id.resultado);
        enom=(EditText)findViewById(R.id.eNombre);
        ecant=(EditText)findViewById(R.id.eCantidad);
        evu=(EditText) findViewById(R.id.ePrecio);
        bnuevo=(Button)findViewById(R.id.bNuevo);
        bact=(Button)findViewById(R.id.bActualizar);
        bbucar=(Button)findViewById(R.id.bBuscar);
        beliminar=(Button)findViewById(R.id.bEliminar);
        binven=(Button)findViewById(R.id.bInvent);
        bventas=(Button)findViewById(R.id.bVentas);
        bgan=(Button)findViewById(R.id.bGana);
        blimpiar=(Button)findViewById(R.id.bLimpiar);

        Productos productos=new Productos(this);
        db=productos.getWritableDatabase();

        bnuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tnombre = enom.getText().toString();
                if(tnombre.isEmpty()|| ecant.getText().toString().isEmpty()||evu.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Escriba nombre, cantidad y precio ", Toast.LENGTH_SHORT).show();
                }else {
                    int cant = Integer.valueOf(ecant.getText().toString());
                    int val = Integer.valueOf(evu.getText().toString());
                    ContentValues nuevoRegistro = new ContentValues();
                    nuevoRegistro.put("nombre", tnombre);
                    nuevoRegistro.put("cantidad", cant);
                    nuevoRegistro.put("valor", val);
                    db.insert("Peluches", null, nuevoRegistro);
                    limpiar();
                }
            }
        });
        beliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = enom.getText().toString();
                if (nom.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Escriba el nombre", Toast.LENGTH_SHORT).show();
                } else{
                    db.delete("Peluches", "nombre=?", new String[]{nom});
                    limpiar();
                }
            }
        });
        bact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomb=enom.getText().toString();

                if(nomb.isEmpty()){
                    Toast.makeText(MainActivity.this, "Escriba nombre ", Toast.LENGTH_SHORT).show();
                }else if(ecant.getText().toString().isEmpty() && evu.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Escriba cantidad y/o valor ", Toast.LENGTH_SHORT).show();
                }else if(ecant.getText().toString().isEmpty()&& !evu.getText().toString().isEmpty()){
                    int valor=Integer.valueOf(evu.getText().toString());
                    ContentValues nuevoValor=new ContentValues();
                    nuevoValor.put("valor", valor);
                    db.update("Peluches", nuevoValor, "nombre=?", new String[]{nomb});
                }else if(evu.getText().toString().isEmpty() && !ecant.getText().toString().isEmpty()){
                    int cant=Integer.valueOf(ecant.getText().toString());
                    ContentValues nuevoValor=new ContentValues();
                    nuevoValor.put("cantidad",cant);
                    db.update("Peluches", nuevoValor, "nombre=?", new String[]{nomb});
                }else{
                    int cant=Integer.valueOf(ecant.getText().toString());
                    int valor=Integer.valueOf(evu.getText().toString());
                    ContentValues nuevoValor=new ContentValues();
                    nuevoValor.put("cantidad",cant);
                    nuevoValor.put("valor", valor);
                    db.update("Peluches", nuevoValor, "nombre=?", new String[]{nomb});
                }
                limpiar();
            }
        });
        bbucar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre=enom.getText().toString();
                String[] campos= new String[]{"id", "nombre", "cantidad","valor"};
                String[] args= new String[]{nombre};
                limpiar();
                if(!nombre.isEmpty()) {
                    Cursor c = db.query("Peluches", campos, "nombre=?", args, null, null, null);
                    if (c.moveToFirst()) {
                        tresul.setText("");
                        do {
                            String code = c.getString(0);
                            String nomb = c.getString(1);
                            int cant = c.getInt(2);
                            int val = c.getInt(3);
                            tresul.append(" " + code + " " + nomb + " " + cant + " " + val + "\n");
                        } while (c.moveToNext());
                    }
                }else
                    Toast.makeText(MainActivity.this, "Escriba el nombre ", Toast.LENGTH_SHORT).show();

            }
        });
        binven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c=db.rawQuery("SELECT id, nombre, cantidad, valor FROM Peluches", null);
                tresul.setText("");
                if (c.moveToFirst())
                    do {
                        String codigo=c.getString(0);
                        String nombre=c.getString(1);
                        int cantidad=c.getInt(2);
                        int valor=c.getInt(3);
                        tresul.append(" " + codigo +" "+nombre + " " + cantidad+" " +valor +" "+ "\n");
                    }while (c.moveToNext());
            }
        });
        blimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enom.setText("");
                ecant.setText("");
                evu.setText("");
                tresul.setText("");
            }
        });
        bventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = enom.getText().toString();
                String cantve = ecant.getText().toString();
                int codven, cac,vu, idven, gv=0;
                String nven;

                if(nom.isEmpty() || cantve.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Escriba nombre y cantidad ", Toast.LENGTH_SHORT).show();
                }else {


                    String[] campos = new String[]{"id", "nombre", "cantidad", "valor"};
                    String[] args = new String[]{nom};
                    Cursor c = db.query("Peluches", campos, "nombre=?", args, null, null, null);
                    if (c.moveToFirst()) {
                        tresul.setText("");
                        do {
                            idven = c.getInt(0);
                            nven = c.getString(1);
                            cac = c.getInt(2);
                            vu = c.getInt(3);

                        } while (c.moveToNext());
                        if (Integer.valueOf(cantve) > cac) {
                            Toast.makeText(MainActivity.this, "Cantidad Insuficiente, hay " + cac + " " + nven, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Venta exitosa  por valor de $" + Integer.valueOf(cantve) * vu, Toast.LENGTH_SHORT).show();
                            ContentValues nuevoValor = new ContentValues();
                            nuevoValor.put("cantidad", cac - Integer.valueOf(cantve));
                            db.update("Peluches", nuevoValor, "nombre=?", new String[]{nven});
                            ContentValues nReG = new ContentValues();
                            gv=Integer.valueOf(cantve) * vu;
                            nReG.put("ganancia",gv );
                            db.insert("Ganancia",null,nReG);
                            if ((cac - Integer.valueOf(cantve)) <= 5) {
                                Toast.makeText(MainActivity.this, "Se está agotando el producto " + nven + " ", Toast.LENGTH_SHORT).show();
                                String titulo = "PRODUCTO AGOTADO", contenido = "Hacer pedido";
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                                builder.setContentTitle(titulo)
                                        .setContentText(contenido)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentInfo("Existencias menores a 5 unds");
                                Intent noIntent = new Intent(MainActivity.this, MainActivity.class);
                                PendingIntent contIntent = PendingIntent.getActivity(MainActivity.this, 0, noIntent, 0);
                                builder.setContentIntent(contIntent);
                                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                nm.notify(1, builder.build());
                            }
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Producto no encontrado ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        bgan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer total=0;
                Cursor c = db.rawQuery("SELECT ganancia FROM Ganancia",null);
                tresul.setText("");
                if(c.moveToFirst())
                    do {
                        int gain=c.getInt(0);
                        total+=gain;
                    } while (c.moveToNext());
                    tresul.setText("TOTAL GANANCIAS :" +total+ "\n");
                }
        });

    }
    public void limpiar(){
        enom.setText("");
        ecant.setText("");
        evu.setText("");
        tresul.setText("");
    }

}
