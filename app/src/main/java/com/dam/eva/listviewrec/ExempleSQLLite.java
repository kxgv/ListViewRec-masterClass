package com.dam.eva.listviewrec;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ExempleSQLLite  extends AppCompatActivity implements View.OnClickListener {

///data/data/com.dam.eva.listviewrec/databases/BDAlumnos


    Button actualizar;
    Button borrar;
    Button insertar;
    EditText editText;
    SQLiteDatabase db;
    Button consultar;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqllite);

        actualizar = (Button)findViewById(R.id.actualizar);
        borrar = (Button)findViewById(R.id.borrar);
        insertar = (Button)findViewById(R.id.insertar);
        editText = (EditText)findViewById(R.id.editText);
        consultar = (Button)findViewById(R.id.consultar);
        textView = (TextView)findViewById(R.id.resultado);

        actualizar.setOnClickListener(this);
        borrar.setOnClickListener(this);
        insertar.setOnClickListener(this);
        consultar.setOnClickListener(this);


        //Obrim  'BDAlumnos' en mode escritura
        BDAlumnos alumnos =
                new BDAlumnos(this, "BDAlumnos", null, 2);

        db = alumnos.getWritableDatabase();

        //Comrpuebo que la BD se abre correctamente
        if(db != null)
        {

            for(int i=1; i<=10; i++)
            {

                int codigo = i;
                String nombreAlumno = "Alumno" + i;

                //hard-code
                db.execSQL("INSERT INTO Alumnos (nombre) " +
                        "VALUES ('" + nombreAlumno + "')");
            }
            //db.close();
        }
    }

    @Override
    public void onClick(View v) {

        try {
            switch (v.getId()){
                case R.id.actualizar:
                    //Actualizar un registro
                    db.execSQL("UPDATE Alumnos SET nombre='" + editText.getText() + "' WHERE codigo=5" );
                    break;
                case R.id.borrar:
                    //Eliminar un registro
                    db.execSQL("DELETE FROM Alumnos WHERE nombre='" + editText.getText() + "'");
                    break;

                case R.id.insertar:
                    //Insertar un registro
                    try {
                        String sql="INSERT INTO Alumnos (nombre) VALUES ('" + editText.getText() + "')";
                        db.execSQL(sql);
                        Log.d("test", "onClick:insertat ok "+sql);

                        // db.execSQL("INSERT INTO Alumnos (nombre,direccion) VALUES ('" + editText.getText() +
//                                "','direccion de prueba') ");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.d("test", "onClickerror: "+e.getLocalizedMessage());
                    } catch (Exception e) {
                        Log.d("test",  "onClick: "+e.getMessage());

                    }

                    break;

                case R.id.consultar:
                    String[] args = new String[] {editText.getText().toString()};
                    Cursor c = db.rawQuery(" SELECT * FROM Alumnos WHERE nombre=? ", args );
                    if (c!=null) {
                        if (c.moveToFirst()) {
                            textView.setText("");
                            do {
                                Integer codigo = 1; //c.getInt(0);
                                String nombre = c.getString(1);
                                String direccion ="dir"; //c.getString(2);
                                String sql="Codigo: " + String.valueOf(codigo) +
                                        " Nombre: " + nombre + " Dirección: " + direccion + "\n";
                                textView.append(sql);

                            } while (c.moveToNext());
                        }
                    } else textView.setText("cursor nul");

                    break;

                default:

                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("test", "onClick: "+e.getMessage());
        } catch (Exception e){
            Log.d("test", "onClick: "+e.getMessage());

        }
    }



    /*public boolean estaAlumne(String nomAlumne) {
        SQLiteDatabase db = this.getReadableDatabase();


        String[] projection = {
                TemperaturesContract.NOMBRE_COLUMNA_NOMCIUTAT,
                TemperaturesContract.NOMBRE_COLUMNA_HORES,
                TemperaturesContract.NOMBRE_COLUMNA_TEMPS

        };

        String selection = TemperaturesContract.NOMBRE_COLUMNA_NOMCIUTAT + " = ?";
        String[] selectionArgs = {nomAlumne};


        Cursor cursor = db.query(
                TemperaturesContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst() == false) {
            return false;
        } else {
            return true;
        }


    }*/

}

