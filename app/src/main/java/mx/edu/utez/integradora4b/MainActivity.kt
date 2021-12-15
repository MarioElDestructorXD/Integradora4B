package mx.edu.utez.integradora4b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lvLista = findViewById<ListView>(R.id.lvLista)

        val cola = Volley.newRequestQueue(this@MainActivity)

        val url = "http://192.168.0.65:4000/products"
        val url22 = "http://192.168.0.65:4000/products/delete"


        val listener = Response.Listener<JSONObject>{ response ->
            val datos = mutableListOf<Evento>()

           Log.d("Datos",response.toString())
            val `object` = JSONObject(response.toString())
            val jsonArray: JSONArray = `object`.getJSONArray("listProducts")
            Log.d("Response",response.toString())

            for (i in 0 until jsonArray.length()){
                datos.add(
                    Evento(
                        jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).getString("descripcion"),
                        jsonArray.getJSONObject(i).getString("categoria"),
                        jsonArray.getJSONObject(i).getInt("cantidad"),
                        jsonArray.getJSONObject(i).getString("codigo")
                    )
                )
            }

            Log.d("Datos","LLegue hasta el adapter")

            val adaptador = CustomAdapter(this@MainActivity, R.layout.layout_evento, datos)
            lvLista.adapter = adaptador
        }

        val error = Response.ErrorListener { error ->
           Toast.makeText(this@MainActivity, error.message.toString(), Toast.LENGTH_SHORT).show()
            //Log.d("Valores", error.message.toString())
        }

        val peticion = JsonObjectRequest(Request.Method.GET, url, null, listener, error)
        cola.add(peticion)

        lvLista.setOnItemClickListener { parent, view, position, id ->
            val builder = AlertDialog.Builder(this@MainActivity)
            val builder2 = AlertDialog.Builder(this@MainActivity)

            builder.setTitle("ACCIONES")

            //ELIMINAR REGISTRO
            builder.setPositiveButton("Eliminar"){dialog, button ->
                builder2.setMessage("¿Estás seguro de borrar el Producto?")
                builder2.setPositiveButton("Si"){dialog, button ->

                    val evento = parent.adapter.getItem(position) as Evento
                    val urlBorrado = url22 + "/" + evento.id.toString()
                    val listener = Response.Listener<JSONObject> {
                        Toast.makeText(this@MainActivity, "Producto Borrado", Toast.LENGTH_SHORT).show()
                        finish(); overridePendingTransition(0, 0); startActivity(intent); overridePendingTransition(0, 0);
                    }
                    val peticion = JsonObjectRequest(Request.Method.DELETE,urlBorrado,null,listener,error)
                    cola.add(peticion)
                }
                builder2.setNegativeButton("No"){dialog, button ->
                    dialog.dismiss()
                }
                builder2.show()
            }

            builder.setNegativeButton("Cancelar"){dialog, button ->
                dialog.dismiss()
            }

            builder.show()


        }

    }

    // Añadir el menú al activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnuSalir -> finish()
            R.id.mnuNuevo ->
             {
                val intent = Intent(this@MainActivity, Registro::class.java)
                 finish(); overridePendingTransition(0, 0); startActivity(intent); overridePendingTransition(0, 0);
            }

        }
        return super.onOptionsItemSelected(item)
    }



}

