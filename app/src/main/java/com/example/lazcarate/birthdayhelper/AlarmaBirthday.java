package com.example.lazcarate.birthdayhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lazcarate on 6/2/16.
 */
public class AlarmaBirthday extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
/*
Creamos el intent que lanza el servicio.
 */
        Intent intLanzar = new Intent(context, NotificacionService.class);
        context.startService(intLanzar);

    }
}
