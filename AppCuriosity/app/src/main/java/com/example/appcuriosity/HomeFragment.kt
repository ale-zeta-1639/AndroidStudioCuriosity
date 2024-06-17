package com.example.appcuriosity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"

class HomeFragment : Fragment() {
    private var param1: Boolean? = false

    private lateinit var pieChartView: PieChartView
    private var countConosciuti : Float = 0f
    private var countSconosciuti : Float = 0f

    private lateinit var firebaseDB: FirebaseDatabase
    private lateinit var myUserId: String
    private lateinit var buttonGenera: Button

    var mainActivityListener: MainActivityListener? = null // Riferimento all'interfaccia MainActivityListener

    /* base output per Fragment
    * Toast.makeText(requireContext(), "Tutto ok", Toast.LENGTH_SHORT).show()
    * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getBoolean(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myUserId = requireArguments().getString("userId").toString()
        pieChartView = view.findViewById(R.id.pieChartView)
        buttonGenera = view.findViewById(R.id.btnAction)

        initializeSettings(myUserId)

        buttonGenera.setOnClickListener {
            showAlertDialog()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Assicurati che l'Activity ospitante implementi l'interfaccia
        if (context is MainActivityListener) {
            mainActivityListener = context
        } else {
            throw RuntimeException("$context must implement MainActivityListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainActivityListener = null // Evita memory leaks
    }

    /*
    * metodo che si occupa di prelevare i dati dal DB
    * */
    private fun initializeSettings(myUserId: String) {
        firebaseDB = FirebaseDatabase.getInstance("https://appcuriosity-5688a-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = firebaseDB.getReference("Users/$myUserId")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                countConosciuti = (dataSnapshot.child("countConosciuti").getValue(Double::class.java) ?: 0.0).toFloat()
                countSconosciuti = (dataSnapshot.child("countSconosciuti").getValue(Double::class.java) ?: 0.0).toFloat()
                pieChartView.setCounts(countConosciuti, countSconosciuti)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "#Err_H1 Errore durante il recupero dei dati", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*
    * metodo che si occupa di aggiornare il DB
    * */
    private fun updateOnDB(myUserId: String, key: String, value: Any?){
        val myRef = firebaseDB.getReference("Users/$myUserId/$key")
        myRef.setValue(value)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Dati aggiornati", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "#Err_H2 Errore durante l'aggiornamento", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updatePieChart() {
        pieChartView.setCounts(countConosciuti, countSconosciuti)
    }

    fun updateConosciuti(){
        countConosciuti++
        updateOnDB(myUserId,"countConosciuti", countConosciuti)
        updatePieChart()
    }

    fun updateSconosciuti(){
        countSconosciuti++
        updateOnDB(myUserId,"countSconosciuti", countSconosciuti)
        updatePieChart()
    }

    /*
    * metodo che si occupa della realizzazione dell allertDialog con la sua costruzione e gestione dei tasti
    * */
    private fun showAlertDialog() {
        val returnedString = mainActivityListener?.onAlertDialogShown() // Chiamata al metodo dell'interfaccia
        val context = requireContext()
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Lo sapevi che ...")
        alertDialogBuilder.setMessage(returnedString)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton("Lo sapevo") { dialog, which ->
            Toast.makeText(context, "OK button clicked", Toast.LENGTH_SHORT).show()
            updateConosciuti()
            dialog.dismiss() // Chiude l'AlertDialog
        }

        alertDialogBuilder.setNegativeButton("Non lo sapevo") { dialog, which ->
            Toast.makeText(context, "NO button clicked", Toast.LENGTH_SHORT).show()
            updateSconosciuti()
            dialog.dismiss() // Chiude l'AlertDialog
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Boolean) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, param1)
                }
            }
    }
}