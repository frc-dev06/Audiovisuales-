package com.example.audiovisuales.adapters

import android.content.Context
import android.content.Intent
import android.provider.Settings.Global
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.Entities.Docentes
import com.example.audiovisuales.R
import com.example.audiovisuales.appInside.RegistrarDocenteActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class docenteAdapter(
    private val mContext: Context,
    private val listaDocentes: List<Docentes>
) : ArrayAdapter<Docentes>(mContext, 0, listaDocentes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var layout = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.item_docente, parent, false)
        val docente = listaDocentes[position]

        var db= AudiovisualesDataBase.getDatabase(mContext)
        val txtNombreDocente = layout.findViewById<TextView>(R.id.txtNombreDocenteItem)
        val txtCedulaDocente = layout.findViewById<TextView>(R.id.txtCedulaDocenteItem)
        val btnDelete= layout.findViewById<ImageButton>(R.id.btnDeleteDocente)

        txtNombreDocente.text = docente.nombres
        txtCedulaDocente.text = docente.cedulaDocente

        btnDelete.setOnClickListener {

            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de eliminar este docente?")

            builder.setPositiveButton("Sí") { dialog, which ->

                GlobalScope.launch(Dispatchers.IO) {

                    db.docentesDao().borrarDocente(docente.cedulaDocente)
                    val intent = Intent(mContext, RegistrarDocenteActivity::class.java)
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
