package mx.edu.utez.integradora4b

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Modificar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar)

        val edtNombre = findViewById<EditText>(R.id.edtNombre2)
        val edtDescripcion = findViewById<EditText>(R.id.edtDescripcion2)
        val spState = findViewById<Spinner>(R.id.spState2)
        val edtLat = findViewById<EditText>(R.id.edtlat2)
        val edtLong = findViewById<EditText>(R.id.edtlon2)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar2)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar2)

        val datos = listOf(
            "Despensa",
            "Aparatos Electronicos"
        )

        val adaptador = ArrayAdapter(this@Modificar, android.R.layout.simple_list_item_1,datos)
        spState.adapter = adaptador

        val cola = Volley.newRequestQueue(this@Modificar)
        val url = "https://utez-appdesign-u2.herokuapp.com/place"
        val error = Response.ErrorListener { error ->
            Toast.makeText(this@Modificar, error.message.toString(), Toast.LENGTH_SHORT).show()
        }
        val listener = Response.Listener<JSONObject>{ resultado ->
            //Indicar al usuario el resultado
            if("insertId" in resultado.names().toString() && resultado.getInt("insertId") != 0){
                Toast.makeText(this@Modificar,"Modificación Exitoso", Toast.LENGTH_SHORT).show()
                //Limpiar Formulario
                edtNombre.setText("")
                edtDescripcion.setText("")
                spState.setSelection(0)
                edtLat.setText("")
                edtLong.setText("")
            }else{
                Toast.makeText(this@Modificar,"No se pudo realizar la Modificación", Toast.LENGTH_SHORT).show()
            }


        }

        btnCancelar.setOnClickListener{
            finish()
        }

        btnGuardar.setOnClickListener{
            //Recuperar Datos
            val nombre = edtNombre.text.toString()
            val descripcion = edtDescripcion.text.toString()
            val state = spState.selectedItem.toString()
            val latitud = edtLat.text.toString()
            val longitud = edtLong.text.toString()

            //Validaciones
            if(nombre != "" && descripcion != "" && state != "" && latitud != "" && longitud!= ""){

                //Crear Body
                val body = JSONObject()
                body.put("name",nombre)
                body.put("description",descripcion)
                body.put("state",state)
                body.put("lat",latitud)
                body.put("lon",longitud)

                //Hacer petición/Añadir a la cola
                val peticion = JsonObjectRequest(Request.Method.POST, url, body, listener, error)
                cola.add(peticion)
            }else{
                Toast.makeText(this@Modificar,"Faltan datos", Toast.LENGTH_SHORT).show()
            }




        }




    }
}