package com.example.audiovisuales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.audiovisuales.Configuracion.AudiovisualesDataBase
import com.example.audiovisuales.Entities.Encargados
import com.example.audiovisuales.appInside.primaryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

class signUpActivity : AppCompatActivity() {
    var cedula:Int?= null;
    var nombre:  String?= null;
    var clave: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //llamar base de datos
        val db= AudiovisualesDataBase.getDatabase(this)

        val txtCedulaSignUp= findViewById<EditText>(R.id.txtCedulaSignUp);
        val txtNombreSignUp= findViewById<EditText>(R.id.txtNombreSignUp);
        val txtContraseñaSignUp= findViewById<EditText>(R.id.txtContraseñaSignUp);

        val btnSignUp= findViewById<Button>(R.id.btnSignUp);
        val btnSalirSignUp= findViewById<ImageButton>(R.id.btnSalirSignUp);

        fun obtenerDatos(){
            if (!txtCedulaSignUp.text.isEmpty() && !txtNombreSignUp.text.isEmpty() && !txtContraseñaSignUp.text.isEmpty()){
                cedula=txtCedulaSignUp.text.toString().toInt();
                nombre= txtNombreSignUp.text.toString();
                clave= txtContraseñaSignUp.text.toString();
            }else{
                Toast.makeText(this, "Llena los campos", Toast.LENGTH_LONG).show();
            }
        }

        btnSalirSignUp.setOnClickListener {
            exitSignUp()
        }
        btnSignUp.setOnClickListener {
            obtenerDatos();
            validacionSignUp(cedula, nombre, clave, db)
        }

    }

    fun validacionSignUp(cedula:Int?, nombre:String?, clave:String?, db:AudiovisualesDataBase){

        if (cedula != null && nombre != null && clave !=null){
            if (clave.length > 6){
                    var claveHash= hashPassword(clave)
                    val nuevoEncargado= Encargados(cedula.toString(), nombre, "Encargado TI", claveHash);
                    // se debe usar corrutinas para evitar problemas
                GlobalScope.launch(Dispatchers.IO){
                    db.encargadosDao().insertarEncargado(nuevoEncargado)
                }
                    Toast.makeText(this, "Registro exitoso $nombre", Toast.LENGTH_LONG).show();
                    exitSignUp()
            }else{
                Toast.makeText(this, "la contrseña debe tener mas de 6 caracteres", Toast.LENGTH_LONG).show();
            }
        }else{

            Toast.makeText(this, "algo salio mal", Toast.LENGTH_LONG).show();
        }

    }

    fun exitSignUp(){
        var intent= Intent(this, LoginActivity::class.java);
        startActivity(intent);
        this.finish();
    }

    fun successfulLogin(){
        var intent= Intent(this, primaryActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    fun hashPassword(password: String): String {
        try {
            // Crear instancia de MessageDigest para SHA-256
            val digest = MessageDigest.getInstance("SHA-256")

            // Obtener el hash de la contraseña
            val hashBytes = digest.digest(password.toByteArray())

            // Convertir bytes a hexadecimal
            val hexString = StringBuilder()
            for (byte in hashBytes) {
                val hex = (byte and 0xFF.toByte()).toString(16)
                if (hex.length == 1) {
                    hexString.append('0')
                }
                hexString.append(hex)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ""
        }
    }
}