package net.azarquiel.diccionario

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import net.azarquiel.diccionario.model.Palabra
import net.azarquiel.diccionario.utilidades.Util
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, TextToSpeech.OnInitListener  {

    private lateinit var palabrasTotal: ArrayList<Palabra>
    private lateinit var palabrasOriginal: ArrayList<Palabra>
    private lateinit var adapter: CustomAdapter
    private lateinit var searchView: SearchView
    private var banderaEspañola:Boolean=true

    companion object {
        private var tts: TextToSpeech? = null
        fun speakOut(palabra: Palabra, banderaEspañola:Boolean) {
            if(banderaEspañola){
                tts!!.language = Locale("es")
                tts!!.speak(palabra.español, TextToSpeech.QUEUE_ADD, null)
                tts!!.language = Locale.US
                tts!!.speak(palabra.ingles, TextToSpeech.QUEUE_ADD, null)
            }else{
                tts!!.language = Locale.US
                tts!!.speak(palabra.ingles, TextToSpeech.QUEUE_ADD, null)
                tts!!.language = Locale("es")
                tts!!.speak(palabra.español, TextToSpeech.QUEUE_ADD, null)
            }
        }
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            var result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Languaje us no suportado")
            } else {
            }
            result = tts!!.setLanguage(Locale("es"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Languaje spa no suportado")
            } else {
            }
        } else {
            Log.e("TTS", "Fallo en inicialización de TTS")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tts = TextToSpeech(this, this)
        setSupportActionBar(toolbar)
        Util.inyecta(this)
        loadData()
        showData()
    }

    private fun showData() {
        adapter=CustomAdapter(this,R.layout.rowpalabras,palabrasTotal,banderaEspañola)
        rvPalabras.layoutManager=LinearLayoutManager(this)
        rvPalabras.adapter=adapter
    }

    private fun loadData() {
        val preferenciasIngles = getSharedPreferences("ingles", Context.MODE_PRIVATE)
        val preferenciasEspañol= getSharedPreferences("espanol", Context.MODE_PRIVATE)
        val palabraIngles=preferenciasIngles.all
        palabrasOriginal= ArrayList()
        for(entry in palabraIngles.entries){
            val palEsp=preferenciasEspañol.getString(entry.key,"nosta")
            val palabra=Palabra(entry.key.toInt(),palEsp.toLowerCase(),entry.value.toString().toLowerCase())
            palabrasOriginal.add(palabra)
        }
        palabrasTotal=ArrayList()
        palabrasTotal.addAll(palabrasOriginal)
        Util.ordenarArray(palabrasTotal,banderaEspañola)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        // ************* <Filtro> ************
        val searchItem = menu.findItem(R.id.search)
        searchView= searchItem.actionView as SearchView
        searchView.setQueryHint("Search...")
        searchView.setOnQueryTextListener(this)
        // ************* </Filtro> ************
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_flag -> {
                cambioBandera(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        palabrasTotal.clear()

        if(banderaEspañola)
            palabrasTotal.addAll(palabrasOriginal.filter { p -> p.español.contains(p0!!) })
        else
            palabrasTotal.addAll(palabrasOriginal.filter { p -> p.ingles.contains(p0!!) })

        Util.ordenarArray(palabrasTotal,banderaEspañola)
        adapter.notifyDataSetChanged()
        return false
    }

    fun cambioBandera(item: MenuItem){
        banderaEspañola=!banderaEspañola
        if(banderaEspañola){
            item.setIcon(R.drawable.flag2)
        }else {
            item.setIcon(R.drawable.flag1)
        }
        Util.ordenarArray(palabrasTotal,banderaEspañola)
        adapter.banderaEspañola=banderaEspañola
        adapter.notifyDataSetChanged()
    }

    fun speaker(view: View){
        val palabraPulsada=view.tag as Palabra
        speakOut(palabraPulsada,banderaEspañola)
    }
}
