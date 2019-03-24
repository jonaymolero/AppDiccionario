package net.azarquiel.diccionario

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.rowpalabras.view.*
import net.azarquiel.diccionario.model.Palabra


/**
 * Created by pacopulido on 9/10/18.
 */
class CustomAdapter(val context: Context,
                    val layout: Int,
                    val dataList: List<Palabra>,
                    var banderaEspañola:Boolean) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(viewlayout: View, val context: Context, val adapter: CustomAdapter) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Palabra){
            if(adapter.banderaEspañola){
                itemView.tvarriba.text=dataItem.español
                itemView.tvabajo.text=dataItem.ingles
            }else{
                itemView.tvarriba.text=dataItem.ingles
                itemView.tvabajo.text=dataItem.español
            }
            itemView.setTag(dataItem)
        }
    }
}