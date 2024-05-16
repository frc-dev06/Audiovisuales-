package com.example.audiovisuales.appInside.reportes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.R
import com.example.audiovisuales.appInside.primaryActivity
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReportesMainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reportes_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.containerRepoDoc)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRepoDia= findViewById<Button>(R.id.btnRepoDia)
        val btnRepoDoc= findViewById<Button>(R.id.btnRepoDocentes)
        val btnRepoImp=findViewById<Button>(R.id.btnRepoImplementos)
        val btnback= findViewById<ImageButton>(R.id.btnBackToMenuRepor)

        val btnReportPDF= findViewById<Button>(R.id.btnReportPDF)


        btnRepoDia.setOnClickListener {
            goToRepoDia()
        }
        btnRepoDoc.setOnClickListener {
            goToRepoDocente()
        }
        btnRepoImp.setOnClickListener {
            goToRepoImplemento()
        }
        btnback.setOnClickListener {
            back()
        }


        btnReportPDF.setOnClickListener {
            if (checkPermission()) {
                generatePdf()
            } else {
                requestPermission()
            }
        }

    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generatePdf() {
        val document = Document(PageSize.A4.rotate())
        val pdfPath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        val file = File(pdfPath, "PrestamosReport.pdf")

        try {
            PdfWriter.getInstance(document, FileOutputStream(file))
            document.open()

            val boldFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLUE)

            val title = Paragraph("Reporte de Préstamos", boldFont)
            title.alignment = Element.ALIGN_CENTER
            document.add(title)

            val paragraphSub=Paragraph("Uniminuto CRZ -- ${obtenerFechaHoraActual()}")
            paragraphSub.alignment=Element.ALIGN_CENTER
            document.add(paragraphSub)

            val paragraphIntro= Paragraph("Este documento contiene un reporte detallado de los préstamos realizados.")
            paragraphIntro.alignment=Element.ALIGN_CENTER
            document.add(paragraphIntro)

            document.add(Paragraph("\n"))

            val table = PdfPTable(10)


            table.addCell("ID Préstamo")
            table.addCell("Cédula Encargado")
            table.addCell("Nombre Encargado")
            table.addCell("Código Implemento")
            table.addCell("Nombre Implemento")
            table.addCell("Cédula Docente")
            table.addCell("Nombre Docente")
            table.addCell("Fecha y Hora")
            table.addCell("Estado")
            table.addCell("Observaciones")

            val db = AudiovisualesDataBase.getDatabase(this)

            GlobalScope.launch(Dispatchers.IO) {
                val prestamosList = db.prestamosDao().seleccionarDetallesPrestamo()
                runOnUiThread {
                    for (prestamo in prestamosList) {
                        table.addCell(prestamo.prestamo.idPrestamo.toString())
                        table.addCell(prestamo.prestamo.cedulaEncargado)
                        table.addCell(prestamo.encargado.nombres)
                        table.addCell(prestamo.prestamo.codigoImplemento)
                        table.addCell(prestamo.implemento.nombre)
                        table.addCell(prestamo.prestamo.cedulaDocente)
                        table.addCell(prestamo.docente.nombres)
                        table.addCell(prestamo.prestamo.fechaHora)
                        table.addCell(if (prestamo.prestamo.estado == 0) "Activo" else "Devuelto")
                        table.addCell(prestamo.prestamo.observaciones)
                    }
                    document.add(table)
                    document.close()

                    Toast.makeText(applicationContext, "PDF generado en: $pdfPath/PrestamosReport.pdf", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al generar PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                generatePdf()
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerFechaHoraActual(): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        return fechaHoraActual.format(formato)
    }

    fun back(){
        var intent= Intent(this, primaryActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    fun goToRepoDia(){
        var intent= Intent(this, ReportesDia::class.java)
        startActivity(intent)
        this.finish()
    }
    fun goToRepoDocente(){
        var intent= Intent(this, ReportesDocente::class.java)
        startActivity(intent)
        this.finish()
    }
    fun goToRepoImplemento(){
        var intent= Intent(this, ReportesImplemento::class.java)
        startActivity(intent)
        this.finish()
    }




}