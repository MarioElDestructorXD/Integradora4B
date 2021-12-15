package mx.edu.utez.integradora4b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import mx.edu.utez.integradora4b.databinding.ActivityMainBinding
import mx.edu.utez.integradora4b.databinding.ActivityRegistroBinding
import org.json.JSONArray
import org.json.JSONObject

class Registro : AppCompatActivity() {

    private lateinit var binding:ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnScanner.setOnClickListener{ initScanner() }


        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtDescripcion = findViewById<EditText>(R.id.edtDescripcion)
        val spCategory = findViewById<Spinner>(R.id.spCategory)
        val edtCantidad = findViewById<EditText>(R.id.edtcantidad)
        val edtCodigo = findViewById<EditText>(R.id.edtCodigo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar2)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar2)

        val datos = listOf(
            "Despensa",
            "Aparatos Electronicos"
        )

        val adaptador = ArrayAdapter(this@Registro, android.R.layout.simple_list_item_1,datos)
        spCategory.adapter = adaptador

        val cola = Volley.newRequestQueue(this@Registro)
        val url = "http://192.168.0.65:4000/products/create"
        val error = Response.ErrorListener { error ->
            Toast.makeText(this@Registro, error.message.toString(),Toast.LENGTH_SHORT).show()
        }
        val listener = Response.Listener<JSONObject>{ resultado ->
            //Indicar al usuario el resultado
            if("status" in resultado.names().toString() && resultado.getInt("status") != 400){
                Toast.makeText(this@Registro,"Registro Exitoso", Toast.LENGTH_SHORT).show()
                //Limpiar Formulario
                edtNombre.setText("")
                edtDescripcion.setText("")
                spCategory.setSelection(0)
                edtCantidad.setText("")
                edtCodigo.setText("")

            }else{
                Toast.makeText(this@Registro,"No se pudo realizar el registro", Toast.LENGTH_SHORT).show()
            }

        }

        btnCancelar.setOnClickListener{

            val intent = Intent(this@Registro, MainActivity::class.java)
            finish(); overridePendingTransition(0, 0); startActivity(intent); overridePendingTransition(0, 0);
        }

        btnGuardar.setOnClickListener{
            //Recuperar Datos
            val nombre = edtNombre.text.toString()
            val descripcion = edtDescripcion.text.toString()
            val category = spCategory.selectedItem.toString()
            val cantidad = edtCantidad.text.toString()
            val codigo = edtCodigo.text.toString()

            //Validaciones
            if(nombre != "" && descripcion != "" && category != "" && cantidad != "" && codigo!= ""){


            //Crear Body

                val body = JSONObject()

                body.put("name",nombre)
                body.put("descripcion",descripcion)
                body.put("categoria",category)
                body.put("cantidad",cantidad)
                body.put("codigo",codigo)

                //Hacer petición/Añadir a la cola
                val peticion = JsonObjectRequest(Request.Method.POST, url, body, listener, error)
                cola.add(peticion)
            }else{
                Toast.makeText(this@Registro,"Faltan datos", Toast.LENGTH_SHORT).show()
            }
        }




    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val edtCodigo = findViewById<EditText>(R.id.edtCodigo)
        if(result != null){
            if(result.contents == null){
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }else{
                edtCodigo.setText("${result.contents}")
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}