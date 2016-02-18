package com.example.lazcarate.birthdayhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lazcarate on 31/1/16.
 */
public class AgendaArrayAdapter extends ArrayAdapter<Agenda>{

    List<Agenda>listA;

    public AgendaArrayAdapter(Context context, int resource, List<Agenda> objects) {

        super(context, 0, objects);
        listA = new ArrayList<Agenda>();
        this.listA= objects;
    }
     class ViewHolder {

         protected ImageView imgFoto;
         protected TextView nombreTxt;
         protected TextView txtTfno;
         protected TextView txtAviso;
         protected TextView txtFechNac;
     }
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder vh;

       LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       //Declaramos una vista y la igualamos a la que pueda tener en la variable convertview
       View listItemView = convertView;
       if(convertView == null) {
           //Inflamos el xml de la fila
           listItemView = layoutInflater.inflate(R.layout.fila_lista, parent, false);
           vh = new ViewHolder();
           vh.nombreTxt = (TextView) listItemView.findViewById(R.id.laynombre);
           vh.txtTfno = (TextView) listItemView.findViewById(R.id.laytelefono);
           vh.imgFoto = (ImageView) listItemView.findViewById(R.id.layfoto);
           vh.txtAviso = (TextView) listItemView.findViewById(R.id.layAviso);
           vh.txtFechNac = (TextView) listItemView.findViewById(R.id.layFechaNac);
           listItemView.setTag(vh);
       }
       else{
           //Si no es nulo, osea que ya hay otro
           vh = (ViewHolder)listItemView.getTag();//Los obtenemos y se lo asignamos al objeto ViewHolder
       }
       //Metemos los datos en cada view extraidos del arraylist
       Agenda item = getItem(position);
       vh.nombreTxt.setText(listA.get(position).getNombre());
       vh.txtTfno.setText(listA.get(position).getTelefono());
       vh.imgFoto.setImageBitmap(listA.get(position).getFoto());
       vh.txtAviso.setText(listA.get(position).getAviso());
       vh.txtFechNac.setText(listA.get(position).getFechaN());

       return listItemView;
   }

    }

