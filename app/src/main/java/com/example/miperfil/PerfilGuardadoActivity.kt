package com.example.miperfil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PerfilGuardadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_guardado)

        val tvResumenNombres = findViewById<TextView>(R.id.tvResumenNombres)
        val tvResumenCorreo = findViewById<TextView>(R.id.tvResumenCorreo)
        val tvResumenTelefono = findViewById<TextView>(R.id.tvResumenTelefono)
        val tvResumenFecha = findViewById<TextView>(R.id.tvResumenFecha)
        val tvResumenDireccion = findViewById<TextView>(R.id.tvResumenDireccion)
        val tvResumenPermiso = findViewById<TextView>(R.id.tvResumenPermiso)

        val nombres = intent.getStringExtra("NOMBRES") ?: ""
        val correo = intent.getStringExtra("CORREO") ?: ""
        val telefono = intent.getStringExtra("TELEFONO") ?: ""
        val fecha = intent.getStringExtra("FECHA_NAC") ?: ""
        val direccion = intent.getStringExtra("DIRECCION") ?: ""
        val permisoCamara = intent.getBooleanExtra("PERMISO_CAMARA", false)

        tvResumenNombres.text = "Nombres y apellidos: $nombres"
        tvResumenCorreo.text = "Correo electrónico: $correo"
        tvResumenTelefono.text = "Teléfono: $telefono"
        tvResumenFecha.text = "Fecha de nacimiento: $fecha"
        tvResumenDireccion.text = "Dirección: $direccion"
        tvResumenPermiso.text = if (permisoCamara)
            "Foto: permiso de cámara concedido"
        else
            "Foto: no se concedió acceso a la cámara"

        val btnRegresarInicio = findViewById<Button>(R.id.btnRegresarInicio)
        val btnNuevoPerfil = findViewById<Button>(R.id.btnNuevoPerfil)

        btnRegresarInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        btnNuevoPerfil.setOnClickListener {
            val intent = Intent(this, RegistroPerfilActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}