package com.example.audiovisuales.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.Entities.Docentes
import com.example.audiovisuales.Entities.Implementos
import com.example.audiovisuales.R
import com.example.audiovisuales.appInside.RegistrarDocenteActivity
import com.example.audiovisuales.appInside.RegistroImplementoActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class implementosAdapter(
    private val mContext: Context,
    private val listaImplementos: List<Implementos>
) : ArrayAdapter<Implementos>(mContext, 0, listaImplementos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var db= AudiovisualesDataBase.getDatabase(mContext)
        var layout = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.item_implemento, parent, false)
        val implemento = listaImplementos[position]

        val txtNombreImplement= layout.findViewById<TextView>(R.id.txtNombreImplementoItem);
        val txtDescriptImplement= layout.findViewById<TextView>(R.id.txtDescripImplementoItem);
        val btnBorrarImplement= layout.findViewById<ImageButton>(R.id.btnBorrarImplementoItem);

        txtNombreImplement.text= implemento.nombre
        txtDescriptImplement.text= implemento.caracteristicas

        btnBorrarImplement.setOnClickListener {

            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de eliminar este docente?")

            builder.setPositiveButton("Sí") { dialog, which ->

                GlobalScope.launch(Dispatchers.IO) {

                    db.implementosDao().borrarImplemento(implemento.codigo)
                    val intent = Intent(mContext, RegistroImplementoActivity::class.java)
                    mContext.startActivity(intent)

                }
            }

            builder.setNegativeButton("No") { dialog, which ->
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        return layout
    }
}