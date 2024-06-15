package com.example.appcuriosity


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterClass(private val dataList: ArrayList<DataClass>): RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

    var onCheckboxClick: ((DataClass, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.check_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvTitle.text = currentItem.dataTitle

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = currentItem.isChecked
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            currentItem.isChecked = isChecked
            onCheckboxClick?.invoke(currentItem, isChecked)
        }
    }

    /*
    Imposta lo stato delle checkbox in base ai valori booleani
    * */
    fun setCheckboxStates(scienza: Boolean, natura: Boolean, storia: Boolean, arte: Boolean, corpo: Boolean, viaggi: Boolean, cibo: Boolean) {
        dataList.forEach { data ->
            when (data.dataTitle) {
                "Scienza e Tecnologia" -> data.isChecked = scienza
                "Natura e Ambiente" -> data.isChecked = natura
                "Storia e Cultura" -> data.isChecked = storia
                "Arte e Intrattenimento" -> data.isChecked = arte
                "Corpo e Mente" -> data.isChecked = corpo
                "Viaggi e Esplorazione" -> data.isChecked = viaggi
                "Cibo e Cucina" -> data.isChecked = cibo
            }
        }
        notifyDataSetChanged() // Notifica l'aggiornamento dei dati
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvTitle:TextView = itemView.findViewById(R.id.preference_checkbox)
        val checkBox:CheckBox = itemView.findViewById(R.id.preference_checkbox)
    }
}