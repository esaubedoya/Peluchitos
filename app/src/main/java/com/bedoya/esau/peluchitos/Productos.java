package com.bedoya.esau.peluchitos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ESAU on 5/05/2016.
 */
public class Productos  extends SQLiteOpenHelper{

    private static final String DATA_BASE_NAME="PeluchesBD";
    private static final int DATA_VERSION=1;


    String sqlCreate="CREATE TABLE Peluches(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, cantidad INTEGER, valor INTEGER)";
    String inicial="INSERT INTO Peluches(nombre,cantidad,valor)VALUES('Ironman','10','15000')," +
                    "('Viuda Negra','10','12000'),('Capitan America','10','15000'),('Hulk','10','12000')," +
                    "('Bruja Escarlata','10','15000'),('Spiderman','10','10000')";
    String sqlCreategan="CREATE TABLE Ganancia(ganancia Integer)";
    String valcero="INSERT INTO Ganancia(ganancia)VALUES('0')";

    public Productos(Context context) {
        super(context, DATA_BASE_NAME,null,DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreate);
        db.execSQL(inicial);
        db.execSQL(sqlCreategan);
        db.execSQL(valcero);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Peluches");
        db.execSQL(sqlCreate);
        db.execSQL("DROP TABLE IF EXISTS Ganancia");
        db.execSQL(sqlCreategan);
    }
}
