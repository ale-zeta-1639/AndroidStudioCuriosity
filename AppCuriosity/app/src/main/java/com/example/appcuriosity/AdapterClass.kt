package com.example.appcuriosity


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterClass(private val dataList: ArrayList<DataClass>): RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {

    var onItemClick: ((DataClass) -> Unit)? = null
    var onCheckboxClick: ((DataClass, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.check_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvTitle.text = currentItem.dataTitle
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



        // Esempio:
        // itemView.checkboxScienza.isChecked = scienza
        // itemView.checkboxNatura.isChecked = natura
        // itemView.checkboxStoria.isChecked = storia
        // Imposta le altre checkbox con i valori booleani corrispondenti
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvTitle:TextView = itemView.findViewById(R.id.preference_checkbox)
        val checkBox:CheckBox = itemView.findViewById(R.id.preference_checkbox)
    }
}