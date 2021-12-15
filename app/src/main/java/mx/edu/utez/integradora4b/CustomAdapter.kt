package mx.edu.utez.integradora4b

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

class CustomAdapter(val context: Context, val layout : Int , val datos : List<Evento>) : BaseAdapter() {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return datos.size
    }

    override fun getItem(position: Int): Any {
        return datos [position]
    }

    override fun getItemId(position: Int): Long {
        //Si su clase Evento tuviera un Id se pondría
        //return datos [position].id.toLong()
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(layout,parent,false)
        val txtTitulo = view.findViewById<TextView>(R.id.txtTitulo)
        val txtDescripcion = view.findViewById<TextView>(R.id.txtDescripcion)
        val llTipoEvento = view.findViewById<LinearLayout>(R.id.IITipoEvento)
        val evento = getItem(position) as Evento

        txtTitulo.text = evento.name
        txtDescripcion.text = evento.descripcion

        var color = Color.parseColor("#000000")
        if(evento.categoria == "Despensa"){
            color = Color.parseColor("#f20727")
        }
        if(evento.categoria == "Aparatos Electronicos"){
            color = Color.parseColor("#17fc03")
        }

        llTipoEvento.setBackgroundColor(color)

        return  view
    }

}