package com.dam.eva.listviewrec;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TemperaturesContract {

    private TemperaturesContract() {

    }

    public static final String TABLE_NAME = "openWeather";
    public static final String NOMBRE_COLUMNA_NOMCIUTAT = "Nom";
    public static final String NOMBRE_COLUMNA_HORES = "Hores";
    public static final String NOMBRE_COLUMNA_TEMPS = "Temps";


    SQLiteDatabase db = this.getReadableDatabase();
    String [] projection = (
            TemperaturesContract.NOMBRE_COLUMNA_NOMCIUTAT,
            TemperaturesContract.NOMBRE_COLUMNA_HORES,
            TemperaturesContract.NOMBRE_COLUMNA_TEMPS
            );


            Cursor cursor = db.query(TABLE_NAME);
            while (cursor.moveToNext()){
                String hora = cursor.getString(1);
                String temperatura = cursor.getString(2);

                if(tempInt >= 20) calorFred= "hot";
                else calorFred = "cold";
                ciutat = new Temp(hora, temperatura);
                mostrar.add(ciutat);
        } cursor.close();
    String selection = TemperaturesContract.NOMBRE_COLUMNA_NOMCIUTAT + "=?";
    String [] selection;

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TemperaturesContract.TABLE_NAME + " (" +
            TemperaturesContract.NOMBRE_COLUMNA_NOMCIUTAT + " TEXT," +
            TemperaturesContract.NOMBRE_COLUMNA_HORES + " TEXT,"+
            TemperaturesContract.NOMBRE_COLUMNA_TEMPS + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TemperaturesContract.TABLE_NAME;

    }
