package com.example.lazcarate.birthdayhelper;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lazcarate on 3/2/16.
 */
public class NotificacionService extends Service{

    final String TAG = "CumpleañosService";
    public boolean enEjecucion = false;
    private DataBaseManager dbm;
    private Tester tester;
    String nome, tfnonum, notif;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbm = new DataBaseManager(this);
        Log.i(TAG, "iniciando Servicio");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!enEjecucion) {
            enEjecucion = true;
            Log.i(TAG, "Servicio arrancado");
            // Creamos un objeto de la clase que extiende de Thread y arrancarlo
            tester = new Tester();
            tester.start();

        } else {
            Log.i(TAG, "El servicio ya estaba arrancado");
        }
        return START_STICKY;//Crea de nuevo el servicio despues de haber sido destruido.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Servicio destruido");
        if (enEjecucion) {
            tester.interrupt();
        }
    }

    //Este método crea y envia la notificacion, se debe llamar desde onStartCommand

    public void creaNotificacion() {
        int notif = 1;
        //Construimos la notificacion
        NotificationCompat.Builder constructorNotif = new NotificationCompat.Builder(this);
        constructorNotif.setSmallIcon(R.drawable.birthday_icon);
        constructorNotif.setContentTitle("Aviso de Cumpleaños");
        constructorNotif.setContentText("Hoy es el cumpleaños de " + nome);
        //Enviamos la notificacion
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notif, constructorNotif.build());

    }

    public class Tester extends Thread {
        @Override
        public void run() {

            while (enEjecucion) {
                if (testFecha()) {
                    creaNotificacion();
                    comprobarNotificacion();
                }
            }
        }
/*
Método que comprueba las fechas de la Bd con la fecha actual, obtenida la fecha de nacimiento extraemos
el nombre, tipo de notif y numero de telefono de ese contacto para luego añadirlo */
        public boolean testFecha() {

            boolean esFecha = false;
            String fde = getFechaActual();
            List<String> fechNaci = new ArrayList<>();
            dbm.abrirBd();
            Cursor cursor = dbm.getAll();
            if (cursor.moveToFirst()) {
                do {
                    fechNaci.add(cursor.getString(cursor.getColumnIndex(DataBaseManager.C_COLUMNA_FECHNAC)));
                }
                while (cursor.moveToNext());
            }
            for (String f : fechNaci) {
                String fn = f.substring(0, 6);
                if (fn.equals(fde)) {
                    esFecha = true;
                    Cursor cn = dbm.getNombre(f);
                    if (cn.moveToFirst()) {
                        do {
                            nome = cn.getString(cn.getColumnIndex(DataBaseManager.C_COLUMNA_NOMBRE));
                        } while (cn.moveToNext());
                    }
                    Cursor tfn = dbm.getNumber(f);
                    if (tfn.moveToFirst()) {
                        do {
                            tfnonum = tfn.getString(tfn.getColumnIndex(DataBaseManager.C_COLUMNA_TFNO));
                        } while (tfn.moveToNext());
                    }
                    Cursor cr = dbm.getTipoNotif(f);
                    if (cr.moveToFirst()) {
                        do {
                            notif = cr.getString(cr.getColumnIndex(DataBaseManager.C_COLUMNA_TIPONOTIF));
                        } while (cr.moveToNext());
                    }
                }
            }
            return esFecha;
        }
        /*
        Metodo que obtiene la fecha actual en formato dia y mes
         */
        public String getFechaActual() {

            final Calendar c = Calendar.getInstance();
            int anio = c.get(Calendar.YEAR);
            int mes = c.get(Calendar.MONTH);
            int dia = c.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM");
            return dateFormat.format(c.getTime());//05-feb
        }
        
/*
En funcion del tipo de notificacion enviaremos el sms o no.
 */
        public boolean comprobarNotificacion() {
            boolean isSms = false;
            if (notif.equals("S")) {
                isSms = true;
                enviarSMs();
            }
            return isSms;
        }
/*
Metodo que envia el sms
 */

        private void enviarSMs() {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(tfnonum, null, getResources().getString(R.string.msgFelicitacion), null, null);
            } catch (Exception e) {
                Log.i("MENSAJE", "Error al mandar");
            }
        }
    }
}



