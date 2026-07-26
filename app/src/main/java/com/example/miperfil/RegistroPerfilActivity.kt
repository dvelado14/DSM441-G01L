package com.example.miperfil

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class RegistroPerfilActivity : AppCompatActivity() {

    private lateinit var etNombres: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etDireccion: EditText
    private lateinit var tvEstadoPermiso: TextView

    private var permisoCamaraConcedido = false

    private val CAMERA_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_perfil)

        etNombres = findViewById(R.id.etNombres)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etDireccion = findViewById(R.id.etDireccion)
        tvEstadoPermiso = findViewById(R.id.tvEstadoPermiso)

        // Formateador automático para la fecha (dd/mm/aaaa)
        etFechaNacimiento.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Solo agregar la barra si el usuario está escribiendo (count > before)
                if (count > before) {
                    if (s?.length == 2 || s?.length == 5) {
                        etFechaNacimiento.append("/")
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val btnTomarFoto = findViewById<Button>(R.id.btnTomarFoto)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnTomarFoto.setOnClickListener {
            solicitarPermisoCamara()
        }

        btnGuardar.setOnClickListener {
            if (validarFormulario()) {
                enviarDatos()
            }
        }
    }

    private fun solicitarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            permisoCamaraConcedido = true
            tvEstadoPermiso.text = "Permiso de cámara concedido. (Foto simulada tomada)"
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permisoCamaraConcedido = true
                tvEstadoPermiso.text = "Permiso de cámara concedido. (Foto simulada tomada)"
            } else {
                permisoCamaraConcedido = false
                tvEstadoPermiso.text = "No se puede acceder a la cámara. Permiso denegado."
            }
        }
    }

    private fun validarFormulario(): Boolean {
        val nombres = etNombres.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val fecha = etFechaNacimiento.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()

        if (nombres.isEmpty() || correo.isEmpty() || telefono.isEmpty() ||
            fecha.isEmpty() || direccion.isEmpty()
        ) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validación de teléfono: exactamente 8 dígitos
        if (!telefono.matches(Regex("^[0-9]{8}$"))) {
            Toast.makeText(this, "El número de teléfono debe tener exactamente 8 dígitos", Toast.LENGTH_SHORT).show()
            return false
        }

        val fechaRegex = Regex("^\\d{2}/\\d{2}/\\d{4}$")
        if (!fecha.matches(fechaRegex)) {
            Toast.makeText(this, "Formato de fecha inválido. Usa dd/mm/aaaa", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun enviarDatos() {
        val intent = Intent(this, PerfilGuardadoActivity::class.java).apply {
            putExtra("NOMBRES", etNombres.text.toString().trim())
            putExtra("CORREO", etCorreo.text.toString().trim())
            putExtra("TELEFONO", etTelefono.text.toString().trim())
            putExtra("FECHA_NAC", etFechaNacimiento.text.toString().trim())
            putExtra("DIRECCION", etDireccion.text.toString().trim())
            putExtra("PERMISO_CAMARA", permisoCamaraConcedido)
        }
        startActivity(intent)
    }
}
