package com.example.appcuriosity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/*
* Fragment per la selezione delle checkbox contente le preferende degli argomenti delle curiosità
* */
class PreferenceFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var myAdapter : AdapterClass
    private lateinit var firebaseDB : FirebaseDatabase

    lateinit var titleList: Array<String>
    private lateinit var myUserId: String
    private var isFirst : Int = 0

    private var scienza :Boolean = false
    private var natura :Boolean = false
    private var storia :Boolean = false
    private var arte :Boolean = false
    private var corpo :Boolean = false
    private var viaggi :Boolean = false
    private var cibo :Boolean = false


    /* base output per Fragment
    * Toast.makeText(requireContext(), "Tutto ok", Toast.LENGTH_SHORT).show()
    * */
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
        recycleView.isClickable = true

        myUserId = requireArguments().getString("userId").toString()
        isFirst = requireArguments().getInt("isFirst")

        initializeCheckboxValues(myUserId)

        if (myUserId.isEmpty())
            Toast.makeText(requireContext(), "User is Empty", Toast.LENGTH_SHORT).show()

        /* Gestione del click della CheckBox
         * riassegnando il valore di true o false alle variabili booleane corrispondenti ad ogni checkbox
         * */
        myAdapter.onCheckboxClick = { task, isChecked ->

            when (task.dataTitle) {
                "Scienza e Tecnologia" -> {
                    scienza = updateBooleanCheck(scienza)
                    upDateOnDB(myUserId, "scienza", scienza)}
                "Natura e Ambiente" -> {
                    natura = updateBooleanCheck(natura)
                    upDateOnDB(myUserId, "natura", natura)}
                "Storia e Cultura" -> {
                    storia = updateBooleanCheck(storia)
                    upDateOnDB(myUserId, "storia", storia)}
                "Arte e Intrattenimento" -> {
                    arte = updateBooleanCheck(arte)
                    upDateOnDB(myUserId, "arte", arte)}
                "Corpo e Mente" -> {
                    corpo = updateBooleanCheck(corpo)
                    upDateOnDB(myUserId, "corpo", corpo)}
                "Viaggi e Esplorazione"-> {
                    viaggi = updateBooleanCheck(viaggi)
                    upDateOnDB(myUserId, "viaggi", viaggi)}
                "Cibo e Cucina"-> {
                    cibo = updateBooleanCheck(cibo)
                    upDateOnDB(myUserId, "cibo", cibo)}
            }
            if(isFirst>=0){
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("userId", myUserId)
                intent.putExtra("isFirst", 0)
                startActivity(intent)
            }
        }
    }


    private fun updateBooleanCheck(value:Boolean) : Boolean{
        if(value == false) {return true}
        else {return false}
    }

    /*
    * motodo che esegue upgrade al Db del relativo campo selezionato
    * */
    private fun upDateOnDB(myUserId: String, variabile: String, newValue: Boolean){
        firebaseDB = FirebaseDatabase.getInstance("https://appcuriosity-5688a-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = firebaseDB.getReference("Users/$myUserId/$variabile")
        myRef.setValue(newValue)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Dati aggiornati", Toast.LENGTH_SHORT).show() // Operazione riuscita
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Errore Update", Toast.LENGTH_SHORT).show() // Gestione dell'errore
            }
    }

    /*
    * metodo per l'inizializzazione delle checkBox con i valori persenti nel DB
    * */
    private fun initializeCheckboxValues(myUserId: String) {
        val firebaseDB = FirebaseDatabase.getInstance("https://appcuriosity-5688a-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = firebaseDB.getReference("Users/$myUserId")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val key = childSnapshot.key.toString()
                    val value = childSnapshot.value
                    // Assicurati che il valore sia un Boolean
                    if (value is Boolean) {
                        when (key) {
                            "scienza" -> scienza = value
                            "natura" -> natura = value
                            "storia" -> storia = value
                            "arte" -> arte = value
                            "corpo" -> corpo = value
                            "viaggi" -> viaggi = value
                            "cibo" -> cibo = value
                        }
                    }

                }
                // Aggiorna l'interfaccia utente con i nuovi valori delle checkbox
                myAdapter.setCheckboxStates(scienza, natura, storia, arte, corpo, viaggi, cibo)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Errore durante il recupero dei dati", Toast.LENGTH_SHORT).show()
            }
        })
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