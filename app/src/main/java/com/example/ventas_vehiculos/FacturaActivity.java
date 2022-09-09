package com.example.ventas_vehiculos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FacturaActivity extends AppCompatActivity {
    EditText jetcodigo,jetfecha,jetplaca;
    TextView etmar,etmode,etval;
    CheckBox jbcactivo2;
    ClsOpenHelper admins=new ClsOpenHelper( this, "concensionario.db", null,1);
    String codigo,fecha,placa;
    long resp;
    int sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        //  ocultar tt
        getSupportActionBar().hide();

        jetcodigo=findViewById(R.id.etcod);
        jetfecha=findViewById(R.id.etfecha);
        jetplaca=findViewById(R.id.etdigite);
        jbcactivo2=findViewById(R.id.cbactivo2);
        etmar=findViewById(R.id.etmar);
        etmode=findViewById(R.id.etmode);
        etval=findViewById(R.id.etval);

        sw = 0;

    }

    public void Guardar (View view){

        placa=jetplaca.getText().toString();
        codigo=jetcodigo.getText().toString();
        fecha=jetfecha.getText().toString();
        if (placa.isEmpty() || codigo.isEmpty() || fecha.isEmpty()){
            Toast.makeText(this, "todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
        else {
            SQLiteDatabase database=admins.getReadableDatabase();
            ContentValues registro =new ContentValues();
            ContentValues registroVehiculo =new ContentValues();

            registro.put("fecha",fecha);
            registro.put("cod_factura",codigo);
            registro.put("placa", Integer.parseInt(placa));

                resp=database.insert( "TblFactura", null,registro);


            if (resp>0){
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                registroVehiculo.put("activo","no");
                database.update("TblVehiculo", registroVehiculo, "placa= " + placa, null);
                limpiar_campos();

            }
            else {
                Toast.makeText(this, "Error guardado registro", Toast.LENGTH_SHORT).show();
            }
            database.close();
        }

    }

    public void Buscar (View view){
        placa=jetplaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "La placa es requerida", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
        else {
            SQLiteDatabase database=admins.getReadableDatabase();
            Cursor fila=database.rawQuery("select * from TblVehiculo where placa = '" + placa+ "'",null);
            if (fila.moveToNext()){
                sw = 1;
                jetcodigo.setText(fila.getString(1));
                jetfecha.setText(fila.getString(2));
                jetplaca.setText(fila.getString(3));

                if (fila.getString(4).equals("si"))
                    jbcactivo2.setChecked(true);

                else
                    jbcactivo2.setChecked(false);

            }
            else {
                Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
                database.close();
            }
        }


    }
    public void ConsultaVehiculo (View view){
        placa=jetplaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "La placa es requerida", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
        else {
            SQLiteDatabase database=admins.getReadableDatabase();
            Cursor fila=database.rawQuery("select * from TblVehiculo where placa = '" + placa+ "'",null);
            if (fila.moveToNext()){
                sw = 1;
                etmar.setText("Marca: " + fila.getString(1));
                etmode.setText("Modelo: " +fila.getString(2));
                etval.setText("Valor: " +fila.getString(3));
            }
            else {
                Toast.makeText(this, "Vehiculo no registrado", Toast.LENGTH_SHORT).show();
                database.close();
                etmar.setText("Marca:");
                etmode.setText("Modelo:");
                etval.setText("Valor:");

            }
        }


    }
    public void AnularFactura (View view){
        if (sw == 0){
            Toast.makeText(this,"Primero debe consultar", Toast.LENGTH_SHORT).show();
            jetplaca.requestFocus();
        }
        else {
            SQLiteDatabase database=admins.getWritableDatabase();
            ContentValues registro= new ContentValues();
            registro.put("activo","no");
            resp=database.update("TblFactura", registro, "cod_factura = ´" + codigo + "´", null );
            if (resp > 0) {
                Toast.makeText(this, "Registro anulado", Toast.LENGTH_SHORT).show();
                limpiar_campos();
            }
            else
                Toast.makeText(this, "Error anulado registro", Toast.LENGTH_SHORT).show();
            database.close();
        }
    }
    public void Cancelar (View view){
        limpiar_campos();
    }
    private  void  limpiar_campos(){
        jetfecha.setText("");
        jetcodigo.setText("");
        jetplaca.setText("");
        jbcactivo2.setChecked(true);

        sw= 0;
    }


    public void  Regresar(View view){
        Intent intmain =new Intent( this,MainActivity.class);
        startActivity(intmain);
    }
}