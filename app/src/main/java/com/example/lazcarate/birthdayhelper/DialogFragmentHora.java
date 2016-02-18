package com.example.lazcarate.birthdayhelper;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by lazcarate on 3/2/16.
 */
public class DialogFragmentHora extends DialogFragment implements TimePickerDialog.OnTimeSetListener{



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Iniciar con el tiempo actual
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                (TimePickerDialog.OnTimeSetListener) getActivity(),//La clase que lo llame implementara la interfaz
                hour,
                minute,
                DateFormat.is24HourFormat(getActivity()));
        tpd.setTitle("Hora de Notificacion");

        // Retornar en nueva instancia del dialogo selector de tiempo
        return tpd;

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    }

}

