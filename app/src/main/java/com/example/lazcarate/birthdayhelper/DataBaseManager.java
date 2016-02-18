package com.example.lazcarate.birthdayhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lazcarate on 31/1/16.
 */
public class DataBaseManager {

    //Definimos constantes con el nombre de la tabla y columnas

    public static final String C_TABLA = "misCumples";
    public static final String C_COLUMNA_ID = "_id";
    public static final String C_COLUMNA_TIPONOTIF = "TipoNotif";
    public static final String C_COLUMNA_MENSAJE = "Mensaje";
    public static final String C_COLUMNA_TFNO = "Telefono";
    public static final String C_COLUMNA_FECHNAC = "FechaNacimiento";
    public static final String C_COLUMNA_NOMBRE = "Nombre";
    public static final String columnas [] = {C_COLUMNA_ID, C_COLUMNA_TIPONOTIF, C_COLUMNA_MENSAJE,
            C_COLUMNA_TFNO, C_COLUMNA_FECHNAC, C_COLUMNA_NOMBRE};
    public static final String CREA_TABLA = "CREATE TABLE " +C_TABLA+ " ("
            + C_COLUMNA_ID + " integer PRIMARY KEY, "
            + C_COLUMNA_TIPONOTIF + " text, "
            + C_COLUMNA_MENSAJE + " text, "
            + C_COLUMNA_TFNO + " text, "
            + C_COLUMNA_FECHNAC + " text, "
            + C_COLUMNA_NOMBRE + " text);";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + C_TABLA;

    private DbHelper dbc;
    private SQLiteDatabase db;
    private Context context;


    public DataBaseManager(Context context) {

        this.context = context;
    }
    public DataBaseManager abrirBd(){

        dbc = new DbHelper(context);
        db = dbc.getWritableDatabase();
        return this;

    }
    public void cerrarBd(){
        dbc.close();
    }

    public void insertarContacto(int id, String notif, String msg, String tfno, String fnac, String name) {

        db.insert(C_TABLA, C_COLUMNA_FECHNAC, generarContentValues(id, notif, msg, tfno, fnac, name));

    }

    private ContentValues generarContentValues(int id, String notif, String msg, String tfno, String fnac, String name){
        ContentValues cv = new ContentValues();
        cv.put(DataBaseManager.C_COLUMNA_ID, id);
        cv.put(DataBaseManager.C_COLUMNA_TIPONOTIF, notif);
        cv.put(DataBaseManager.C_COLUMNA_MENSAJE, msg);
        cv.put(DataBaseManager.C_COLUMNA_TFNO, tfno);
        cv.put(DataBaseManager.C_COLUMNA_FECHNAC, fnac);
        cv.put(DataBaseManager.C_COLUMNA_NOMBRE, name);
        return cv;
    }

    public int actualizarContactos(long _id, String noti, String fnac){

        ContentValues cvAct = new ContentValues();
        cvAct.put(C_COLUMNA_TIPONOTIF, noti);
        cvAct.put(C_COLUMNA_FECHNAC, fnac);
        int i = db.update(C_TABLA, cvAct, C_COLUMNA_ID + " = " + _id, null);
        return i;
    }

    public Cursor getAll(){

        return db.query(C_TABLA, columnas, null, null, null, null, null);
    }
    public Cursor getNombre(String fnac){

        Cursor cn = db.query(C_TABLA, new String[] {C_COLUMNA_NOMBRE}, C_COLUMNA_FECHNAC + " = ? " , new String[]{fnac}, null, null, null);

        return cn;

    }
    public Cursor getTipoNotif(String fnac){

        return db.query(C_TABLA, new String[] {C_COLUMNA_TIPONOTIF}, C_COLUMNA_FECHNAC + " = ? " , new String[]{fnac}, null, null, null);

    }
    public Cursor getNumber(String fnac){

        return db.query(C_TABLA, new String[] {C_COLUMNA_TFNO}, C_COLUMNA_FECHNAC + " = ? " , new String[]{fnac}, null, null, null);

    }


}
