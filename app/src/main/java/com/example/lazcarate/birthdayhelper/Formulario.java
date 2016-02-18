package com.example.lazcarate.birthdayhelper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Formulario extends AppCompatActivity {

    public final static int PICK_CONTACT_REQUEST = 1;
    private DataBaseManager mngBd;
/*
Al llamar a esta actividad directamente se recogen los datos del intent y se colocan en sus campos
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        mngBd = new DataBaseManager(this);
        mngBd.abrirBd();
        EditText ediNombre = (EditText) findViewById(R.id.editTextNombre);
        EditText ediTelefono = (EditText) findViewById(R.id.editTextPhone);
        ImageView imgFoto = (ImageView) findViewById(R.id.imgFoto);
        Intent intent = this.getIntent();
        byte[] byteArray = getIntent().getByteArrayExtra("Foto");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ediNombre.setText(intent.getStringExtra("Nombre"));
        ediTelefono.setText(intent.getStringExtra("Telefono"));
        imgFoto.setImageBitmap(bmp);

/*
Este método responde a un boton que nos lleva a la aplicacion de contactos de Android, al no estar sincronizado
con ninguna cuenta de correo realmente no hace nada mas.
 */
    }
    public void irAgenda(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivity(intent);
    }
    /*
    Nos crea el dialogo para que el usuario ponga su fecha de nacimiento.
     */
    public void irCalendario(View v)
    {
        final TextView textView = (TextView)findViewById(R.id.editTextFechNac);

        // Obtener fecha actual
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        // Mostrar Date Picker Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        Calendar fecha = Calendar.getInstance();
                        fecha.set(year, monthOfYear, dayOfMonth);

                        //formato de la fecha
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                        // Mostrar fecha seleccionada en el TextView
                        textView.setText(dateFormat.format(fecha.getTime()));

                    }}, anio, mes, dia);
        datePickerDialog.show();
    }
/*
Responde a la pulsacion del boton guardar, actualizando el contacto con los nuevos datos y mandando
como resultado del intent los campos de tipo notificacion y Fecha de nacimiento.
 */
    public void actContactos(View view){

        String msgTipoNotif;
        String msgEnvio;
        EditText edfnac = (EditText) findViewById(R.id.editTextFechNac);
        CheckBox edTipoN = (CheckBox) findViewById(R.id.checkBoxEnvSms);
        if(edTipoN.isChecked()){
            msgTipoNotif = "S";
            msgEnvio = "Aviso: Enviar SMS";
        }else{
            msgTipoNotif = "N";
            msgEnvio = "Aviso: Solo Notificacion";
        }
        Intent i = getIntent();
        String idAct = i.getStringExtra("Ident");
        mngBd.actualizarContactos(Long.parseLong(idAct), msgTipoNotif, edfnac.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("MensajeNotificacion", msgEnvio);
        intent.putExtra("FechaN", edfnac.getText().toString());
        //intent.putExtra("idevuelto", idAct);
        setResult(RESULT_OK, intent);
        finish();
    }


}