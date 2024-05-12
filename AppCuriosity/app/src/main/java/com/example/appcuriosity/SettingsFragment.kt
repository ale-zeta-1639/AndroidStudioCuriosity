package com.example.appcuriosity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.RadioGroup
import com.example.appcuriosity.databinding.FragmentSettingsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val itemHours = listOf("1","2","3","4","5","6","7","8","9","10","11","12")
        //val itemMin = listOf("1","2","3","4","5","10","15","20","30","45")
        val hours = resources.getStringArray(R.array.ora)
        val min = resources.getStringArray(R.array.minuti)

        val radio = view.findViewById<RadioGroup>(R.id.radioGroup)
        val autoComplete : AutoCompleteTextView = view.findViewById(R.id.auto_complete)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item,hours)
        autoComplete.setAdapter(adapter)

        radio.check(R.id.radioButtonOre)

        radio.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioButtonOre -> {
                    autoComplete.setText("Seleziona Tempo")
                    autoComplete.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_item,hours))
                }
                R.id.radioButtonMin -> {
                    autoComplete.setText("Seleziona Tempo")
                    autoComplete.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_item,min))
                }
            }
        }

        autoComplete.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView, view, i, l ->
            val itemSelected = adapterView.getItemAtPosition(i)
        }

    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}