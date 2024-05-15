package com.example.audiovisuales.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.audiovisuales.Entities.Prestamos
import com.example.audiovisuales.Entities.PrestamosWithDetails
import com.example.audiovisuales.R

class prestamoAdapter(
    private val mContext: Context,
    private val listaPrestamos: List<PrestamosWithDetails>

) : ArrayAdapter<PrestamosWithDetails>(mContext, 0, listaPrestamos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var layout = convertView
        val prestamo = listaPrestamos[position]

        if (layout == null) {
            layout = LayoutInflater.from(mContext).inflate(R.layout.item_prestamo, parent, false)
        }

        val txtNameDocente = layout!!.findViewById<TextView>(R.id.txtNameDocente)
        val txtFecha = layout.findViewById<TextView>(R.id.txtFecha)
        val txtEstado = layout.findViewById<TextView>(R.id.txtEstado)

        if (prestamo.prestamo.estado==1){
            txtEstado.setTextColor(Color.parseColor("#22B14C"))
            txtEstado.text="Devuelto"
        }else if(prestamo.prestamo.estado==0){
            txtEstado.setTextColor(Color.parseColor("#FF9672"))
            txtEstado.text="En espera..."
        }

        // Establecer el texto del TextView de nombre del docente y fecha
        txtNameDocente.text = prestamo.docente.nombres
        txtFecha.text = prestamo.prestamo.fechaHora

        return layout
    }

}



