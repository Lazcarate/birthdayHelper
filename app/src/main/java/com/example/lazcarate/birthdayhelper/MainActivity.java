package com.example.lazcarate.birthdayhelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TimePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, TimePickerDialog.OnTimeSetListener{

    private ListView l;
    private DataBaseManager dbm;
    private final static int REQUEST_CODE = 1;
    private List<Agenda> listado;
    private AgendaArrayAdapter aga;
    private String nome, phone, idActual;
    private Bitmap photo;
    private Agenda agendaActual;
    private int hora, minos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbm = new DataBaseManager(this);//Gestor de la base de datos
        dbm.abrirBd();//Abrimos la BD
        listado = new ArrayList<Agenda>();
        l = (ListView) findViewById(R.id.lstContactos);
        l.setOnItemClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getData();
        setAlarma(hora, minos);
    }
/*
Método que se encarga de coger los datos del Contacts Provider por medio de un ContentResolver que nos devuelve un
Cursor despues de hacer un query soble el objeto Uri que representa la tabla de Contactos. En el primer cursor obtenemos
el id y el nombre y preguntamos si tienen numero de telefono, si es ok hacemos otra consulta esta vez sobre la tabla
CommonDataKinds pidiendo los numeros de telefono pasando como condicion los id obtenidos en la anterior consulta.
Hacemos lo mismo para sacar la foto esta vez sacandolo através de un stream que luego decodificamos.
Almacenado cada campo en una variable instanciamos un objeto Agenda que añadiremos al arraylist de agendas y que pasaremos
al adaptador para construir el Listview.
Al mismo tiempo he decidido insertar los campos en la base de Datos dejando null los valores que no conocemos.
 */
    private void getData() {

            String numberPhone = null;
            Bitmap photo = null;
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor crp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (crp.moveToNext()) {
                            numberPhone = crp.getString(crp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        crp.close();
                        try {
                            InputStream photpStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id)));
                            if (photpStream != null) {
                                photo = BitmapFactory.decodeStream(photpStream);
                                photpStream.close();
                            }
                        } catch (IOException e) {
                        }
                            Agenda ag = new Agenda(id, name, numberPhone, photo, null, null);
                            dbm.insertarContacto(Integer.parseInt(id), null, getResources().getString(R.string.msgFelicitacion),
                                    numberPhone, null, name);
                            listado.add(ag);
                    }
                }
            }

        aga = new AgendaArrayAdapter(this, R.layout.fila_lista, listado);
        aga.notifyDataSetChanged();//Actualiza el adaptador
        l.setAdapter(aga);
        l.refreshDrawableState();//Refresca el ListView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //Mostramos el Dialogo de tiempo
        if (id == R.id.action_settings) {
            DialogFragmentHora dfh = new DialogFragmentHora();
            dfh.show(getFragmentManager(), "Dialogo ;Hora");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*
Al pulsar el item del ListView obtenemos el objeto pulsado en esa posicion y sacamos sus atributos.
Luego se los mandamos a la actividad Formulario por intent esperando una respuesta que serán el
contenido de los campos que el usuario decide (si enviar sms y fecha de nacimiento.)
 */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        agendaActual = (Agenda) parent.getItemAtPosition(position);
        nome = agendaActual.getNombre();
        phone = agendaActual.getTelefono();
        photo = agendaActual.getFoto();
        idActual = agendaActual.getId();
        listado.remove(agendaActual);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Intent intent = new Intent(MainActivity.this, Formulario.class);
        intent.putExtra("Nombre", nome);
        intent.putExtra("Telefono", phone);
        intent.putExtra("Ident", idActual);
        intent.putExtra("Foto", byteArray);
        //startActivity(intent);
        startActivityForResult(intent, REQUEST_CODE);
    }
/*
Obtenemos la respuesta de la actividad Formulario y recogemos los datos que nos faltaban, los he añadido al
adaptador creando un nuevo objeto y borrando previamente el anterior.
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String fnacActual = data.getStringExtra("FechaN");
            String msgEnvActual = data.getStringExtra("MensajeNotificacion");
            listado.remove(agendaActual);
            aga.add(new Agenda(idActual, nome, phone, photo, msgEnvActual, fnacActual));
            aga.notifyDataSetChanged();
            l.refreshDrawableState();
        }
    }
/*
Recogemos en variables la hora puesta por el usuario en el dialogo.
 */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        this.hora = hourOfDay;
        this.minos = minute;
    }
    /*
    Creamos el Manejador de Alarmas para que se dispare a una hora que pasamos por parametro y que no es otra que
    la recogida en las variables del método onTimeSet, le pasamos un intent para que inicie la clase que extiende de
    BroadcastReciver y que lleva el método que nos indicara que hace cuando salte la alarma.
     */
    public void setAlarma(int hora, int minutos){

        AlarmManager alarmManager;
        PendingIntent alarmIntent;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minutos);

        Intent intent = new Intent(this, AlarmaBirthday.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);


        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
