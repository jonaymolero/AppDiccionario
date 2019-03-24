package net.azarquiel.diccionario.utilidades

import android.content.Context
import android.util.Log
import android.widget.Toast
import net.azarquiel.diccionario.model.Palabra
import java.io.*

object Util {
    private lateinit var context: Context

    fun inyecta(context: Context) {
        this.context = context
        if (!File("/data/data/${context.packageName}/shared_prefs/spanish.xml").exists()) {
            Toast.makeText(context,"Copiando Palabras....", Toast.LENGTH_LONG).show()
            copiarXMLs()
        }
    }
    private fun copiarXMLs() {
        creaDirectorio()
        copiar("espanol.xml")
        copiar("ingles.xml")
    }

    private fun creaDirectorio() {
        val file = File("/data/data/${context.packageName}/shared_prefs")
        file.mkdir()
    }

    private fun copiar(fileXML: String) {
        val ruta = ("/data/data/${context.packageName}/shared_prefs/$fileXML")

        var input: InputStream? = null
        var output: OutputStream? = null
        try {
            input = context.assets.open(fileXML)
            output = FileOutputStream(ruta)
            copyFile(input, output)
            input!!.close()
            output.close()
        } catch (e: IOException) {
            Log.e("Traductor", "Fallo en la copia del archivo desde el asset", e)
        }
    }

    private fun copyFile(input: InputStream?, output: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        read = input!!.read(buffer)
        while (read != -1) {
            output.write(buffer, 0, read)
            read = input!!.read(buffer)
        }
    }

    fun ordenarArray(palabrasTotal:ArrayList<Palabra>, bandera:Boolean){
        if(bandera){
            palabrasTotal.sortBy { palabra -> palabra.español }
        }else{
            palabrasTotal.sortBy { palabra -> palabra.ingles }
        }
    }
}