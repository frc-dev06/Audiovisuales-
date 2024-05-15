package com.example.audiovisuales.appInside

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.Entities.Prestamos
import com.example.audiovisuales.Entities.PrestamosWithDetails
import com.example.audiovisuales.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PrestamoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prestamo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val prestamo= intent.getSerializableExtra("prestamo") as PrestamosWithDetails
        val db= AudiovisualesDataBase.getDatabase(this)

        val btnBack= findViewById<ImageButton>(R.id.btnBackToPrestamo);

        val nameEncargado= findViewById<TextView>(R.id.txtNombreEncargadoPres);
        val cedEncargado= findViewById<TextView>(R.id.txtCedulaEncargadoPres);

        val nameDocente= findViewById<TextView>(R.id.txtNombreDocentePres);
        val cedDocente= findViewById<TextView>(R.id.txtCedulaDocentePres);

        val implemento= findViewById<TextView>(R.id.txtImplementoPres);
        val fecha= findViewById<TextView>(R.id.txtFechaPres);

        val salon= findViewById<TextView>(R.id.txtSalonPres);
        val observacion= findViewById<TextView>(R.id.txtObservacionesPres);

        val btnEstado= findViewById<Button>(R.id.btnEstadoPres);

        val btnEliminarReg= findViewById<ImageButton>(R.id.btnEliminarRegistro);

        fun llenarCampos(){
            nameEncargado.text= prestamo.encargado.nombres;
            cedEncargado.text= prestamo.encargado.cedulaEncargado

            nameDocente.text= prestamo.docente.nombres
            cedDocente.text= prestamo.docente.cedulaDocente

            implemento.text= prestamo.implemento.nombre
            fecha.text= prestamo.prestamo.fechaHora

            salon.text= "No Aplica..."
            observacion.text= prestamo.prestamo.observaciones

            if (prestamo.prestamo.estado==1){
                btnEstado.setBackgroundColor(Color.parseColor("#22B14C"))
            }else if( prestamo.prestamo.estado==0){
                btnEstado.setBackgroundColor(Color.parseColor("#ff0000"))
            }

        }

        llenarCampos()

        btnBack.setOnClickListener {
            backToPrestamos()
        }

        btnEliminarReg.setOnClickListener {
            confirmacionEliminacion(prestamo.prestamo.idPrestamo.toString(), db)
        }

        btnEstado.setOnClickListener {
            if (prestamo.prestamo.estado ==0){
                GlobalScope.launch(Dispatchers.IO){
                    db.prestamosDao().actualizarEstadoPrestamo(prestamo.prestamo.idPrestamo.toLong(), 1)
                }
            }else if (prestamo.prestamo.estado==1){
                GlobalScope.launch(Dispatchers.IO){
                    db.prestamosDao().actualizarEstadoPrestamo(prestamo.prestamo.idPrestamo.toLong(), 0)
                }
            }

            backToPrestamos()
        }

    }

    fun backToPrestamos(){
        var intent= Intent(this, PrestamosMainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun confirmacionEliminacion(id:String, db: AudiovisualesDataBase) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de realizar esta acción?")

        // Configurar el botón "Sí" para confirmar
        builder.setPositiveButton("Sí") { dialog, which ->
            GlobalScope.launch (Dispatchers.IO){
                db.prestamosDao().borrarPrestamo(id.toLong())

                runOnUiThread{
                    Toast.makeText(applicationContext, "Registro ${id} eliminado", Toast.LENGTH_SHORT).show()
                }
            }
            backToPrestamos()

        }

        // Configurar el botón "No" para cancelar
        builder.setNegativeButton("No") { dialog, which ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}