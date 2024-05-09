package com.example.appcuriosity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PreferenceFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var myAdapter : AdapterClass

    lateinit var titleList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View? {
        return inflater.inflate(R.layout.fragment_preference, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataList = arrayListOf<DataClass>()

        titleList = arrayOf(
            "Scienza e Tecnologia",
            "Natura e Ambiente",
            "Storia e Cultura",
            "Arte e Intrattenimento",
            "Corpo e Mente",
            "Viaggi e Esplorazione",
            "Cibo e Cucina",
        )

        getData()

        recycleView = view.findViewById(R.id.listPreferences)
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.setHasFixedSize(true)
        myAdapter = AdapterClass(dataList)
        recycleView.adapter = myAdapter
    }

    private fun getData(){
        for ( i in titleList.indices){
            val dataClass = DataClass(titleList[i])
            dataList.add(dataClass)
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PreferenceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}