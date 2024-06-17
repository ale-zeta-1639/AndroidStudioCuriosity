package com.example.appcuriosity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragment per il setup del tempo di ricezione delle notifiche
 * Bullet-List per la scelta del formato ora \ minuti
 * Drop-Menu per la quantità di tempo
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firebaseDB: FirebaseDatabase

    private lateinit var myUserId: String
    private var isFirst : Int = 0
    private lateinit var autoComplete: AutoCompleteTextView
    private lateinit var radio: RadioGroup
    private lateinit var buttonConferma: Button
    private var selectedTime: String? = null
    private var isHour: Boolean = true // Default is hours


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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myUserId = requireArguments().getString("userId").toString()
        isFirst = requireArguments().getInt("isFirst")
        val hours = resources.getStringArray(R.array.ora)
        val min = resources.getStringArray(R.array.minuti)

        radio = view.findViewById<RadioGroup>(R.id.radioGroup)
        autoComplete = view.findViewById(R.id.auto_complete)
        buttonConferma = view.findViewById(R.id.button_conferma)

        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item,hours)
        autoComplete.setAdapter(adapter)

        radio.check(R.id.radioButtonOre)
        radio.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioButtonOre -> {
                    autoComplete.setText("")
                    autoComplete.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_item,hours))
                    isHour = true
                }
                R.id.radioButtonMin -> {
                    autoComplete.setText("")
                    autoComplete.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_item,min))
                    isHour = false
                }
            }
        }

        autoComplete.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->
            selectedTime = adapterView.getItemAtPosition(i).toString()
        }

        initializeSettings(myUserId)

        buttonConferma.setOnClickListener {
            updateSettingsInDB(myUserId, "valoreOra", selectedTime)
            updateSettingsInDB(myUserId, "formatoOra", isHour)
            if(isFirst>0){
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("userId", myUserId)
                intent.putExtra("isFirst", 1)
                startActivity(intent)
            }
        }

    }

    /*
    * metodo per l'inizializzazione dei valori con quelli presenti su DB
    * */
    private fun initializeSettings(myUserId: String) {
        firebaseDB = FirebaseDatabase.getInstance("https://appcuriosity-5688a-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = firebaseDB.getReference("Users/$myUserId")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val selectedTime = dataSnapshot.child("valoreOra").value as String
                val isHour = dataSnapshot.child("formatoOra").value as Boolean

                if (selectedTime.isNotEmpty() && isHour != null) {
                    if (isHour) {
                        radio.check(R.id.radioButtonOre)
                    } else {
                        radio.check(R.id.radioButtonMin)
                    }
                    autoComplete.setText("$selectedTime")
                }else{
                    Toast.makeText(requireContext(), "Valori base non presenti", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Errore durante il recupero dei dati", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*
    * metodo per l'upgrade su dp a seguito della pressione del button
    * */
    private fun updateSettingsInDB(myUserId: String, key: String, value: Any?) {
        val myRef = firebaseDB.getReference("Users/$myUserId/$key")
        myRef.setValue(value)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Dati aggiornati", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Errore durante l'aggiornamento", Toast.LENGTH_SHORT).show()
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