package com.example.audiovisuales

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
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

class LoginActivity : AppCompatActivity() {
     var nombre: String?=null
     var cedula : Int?= null
     var clave: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.containerRepoDoc)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //llamar base de datos
        val db= AudiovisualesDataBase.getDatabase(this)

        val txtCedula= findViewById<EditText>(R.id.txtCedula);
        val txtClave= findViewById<EditText>(R.id.txtContraseña);

        val txtResupuesta= findViewById<TextView>(R.id.txtRespuesta);

        var btnIngresar= findViewById<Button>(R.id.btnIngresar);
        var btnCrear= findViewById<Button>(R.id.btnCrear);
        var btnExitLogin= findViewById<ImageButton>(R.id.btnExitLogin);

        fun obtenerDatos(){
            if (!txtCedula.text.isEmpty() && !txtClave.text.isEmpty()){
                cedula= txtCedula.text.toString().toInt();
                clave= txtClave.text.toString();
            }else{
                Toast.makeText(this, "Llena los campos", Toast.LENGTH_LONG).show()
            }
        }

        btnCrear.setOnClickListener {
            changeSignUp()
        }
        btnExitLogin.setOnClickListener {
            exitLogin()
        }

        btnIngresar.setOnClickListener {
            obtenerDatos()
            validacionLogin(cedula, clave, txtResupuesta, db);

        }


    }

    private fun validacionLogin(cedula: Int?, clave: String?, txtRespuesta: TextView, db: AudiovisualesDataBase) {
        var claveInputHash = clave?.let { hashPassword(it) }

        if (cedula != null && clave != null) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val usuario = db.encargadosDao().seleccionarEncargadoPorCedula(cedula.toString())

                    runOnUiThread {
                        if (usuario != null && cedula.toString() == usuario.cedulaEncargado && claveInputHash == usuario.clave) {
                            successfulLogin(usuario)
                        } else {
                            txtRespuesta.text = "Revisa tus credenciales"
                            txtRespuesta.setTextColor(Color.parseColor("#ff0000"))
                        }
                    }
                } catch (ex: Exception) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "El usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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



    fun changeSignUp(){
        var intent= Intent(this, signUpActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    fun exitLogin(){
        var intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun successfulLogin(usuario: Encargados) {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putString("nombre", usuario.nombres)
        editor.putString("cedulaEncargado", usuario.cedulaEncargado)
        editor.apply()

        runOnUiThread {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, primaryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}