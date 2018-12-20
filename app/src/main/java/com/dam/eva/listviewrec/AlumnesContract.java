package com.dam.eva.listviewrec;


public class AlumnesContract {

    private AlumnesContract() {

    }

    public static final String TABLE_NAME = "dadesAlumnes";
    public static final String NOMBRE_COLUMNA_NOM = "nombre";
    public static final String NOMBRE_COLUMNA_DIRECCION = "direccion";

    String sqlCreate = "CREATE TABLE Alumnos (codigo INTEGER, nombre TEXT)";
    String sqlCreate2 = "CREATE TABLE Alumnos (codigo INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, direccion TEXT)";


    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + AlumnesContract.TABLE_NAME + " (" +
            AlumnesContract.NOMBRE_COLUMNA_NOM + " TEXT," +
            AlumnesContract.NOMBRE_COLUMNA_DIRECCION + " TEXT)";


    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AlumnesContract.TABLE_NAME;

}
