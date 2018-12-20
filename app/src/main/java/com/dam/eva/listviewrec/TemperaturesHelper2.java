package com.dam.eva.listviewrec;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TemperaturesHelper2 extends SQLiteOpenHelper {

    //Definimos la version de la base de datos, en este caso es la 1
    public static final int DATABASE_VERSION = 1;
    //Definimos el nombre de la base de datos
    public static final String DATABASE_NAME = "OpenWeather.db";

    //Definimos el constructor de la clase
    public TemperaturesHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TemperaturesContract.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(TemperaturesContract.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public boolean estaCiutatDescarregada(String nomCiutat) {
        SQLiteDatabase db = this.getReadableDatabase();


        //db.query...

        //if (cursor.moveToFirst() és fals, o altres maneres...
            return false;

    }

    public void guarda(String nomCiutat, List<Temp> blocs) throws ParseException {

        SQLiteDatabase db = this.getWritableDatabase();
        //blocs = new ModificaDadesGuardarBDD(blocs).formatearParaBDD();

        ContentValues values = null;
        for (int i = 0; i < blocs.size(); i++) {
            values = new ContentValues();
            values.put(TemperaturesContract.NOMBRE_COLUMNA_NOMCIUTAT, nomCiutat);
            values.put(TemperaturesContract.NOMBRE_COLUMNA_HORES, blocs.get(i).getData());
            values.put(TemperaturesContract.NOMBRE_COLUMNA_TEMPS, blocs.get(i).getTempe().replace(",","."));

            db.insert(TemperaturesContract.TABLE_NAME, null, values);
        }
    }

    public List<Temp> llegeix(String nomCiutat) throws ParseException {

        List<Temp> mostrar = new ArrayList<Temp>();
        Temp ciutat;

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                TemperaturesContract.NOMBRE_COLUMNA_NOMCIUTAT,
                TemperaturesContract.NOMBRE_COLUMNA_HORES,
                TemperaturesContract.NOMBRE_COLUMNA_TEMPS

        };

        String selection = TemperaturesContract.NOMBRE_COLUMNA_NOMCIUTAT + " = ?";
        String[] selectionArgs = {nomCiutat};


        return mostrar;

    }

    public boolean estaActualitzada(String nomCiutat) throws ParseException {

        SQLiteDatabase db = this.getReadableDatabase();
        String hora="";
        Date fechaBBDD= new Date();
        Date fechanow = new Date();


        return true;

    }


    public void eliminaDades(String nomCiutat) {

        SQLiteDatabase db = this.getWritableDatabase();



        //db.delete...
    }
}
