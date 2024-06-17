package com.example.appcuriosity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.appcuriosity.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var isFirst : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {

            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        Log.d("LoginActivity", "User authenticated successfully")

                        searchEmailInDatabase(email){ userId ->
                            if (userId != null) {
                                if(isFirst){
                                    val Intent = Intent(this, SplashActivity::class.java).putExtra("userId", userId)
                                    startActivity(Intent)
                                }else{
                                    val mainIntent = Intent(this, MainActivity::class.java)
                                    mainIntent.putExtra("userId", userId)
                                    mainIntent.putExtra("isFirst", -1)
                                    startActivity(mainIntent)
                                }
                            } else {
                                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }else{
                        Toast.makeText(this, "User not found, check the fields", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        /*
        * creazione di un AlertDialog personalizzato da layout per la richiesta del forgot password, quindi con invio main
        * */
        binding.forgotPassword.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val userEmail = view.findViewById<EditText>(R.id.editBox)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener{
                compareEmail(userEmail)
                dialog.dismiss()
            }
            if(dialog.window!=null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }

        binding.signUpRedirectText.setOnClickListener{
            val signupIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signupIntent)
        }
    }

    private fun compareEmail(email:EditText){
        if(email.text.toString().isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                Toast.makeText(this,"Check you email", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /*
    * metodo che si occupa di confrontare la mail inserita nei campi con le mail presenti sul DB
    * return : userID
    * */
    private fun searchEmailInDatabase(email: String,  callback: (String?) -> Unit) {
        val database = FirebaseDatabase.getInstance("https://appcuriosity-5688a-default-rtdb.europe-west1.firebasedatabase.app/")
        val usersRef = database.getReference("Users") // Nodo principale "users"

        Log.d("LoginActivity", "Siamo nel Search")
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userFound: String? = null

                for (userSnapshot in dataSnapshot.children) {
                    val userEmail = userSnapshot.child("mail").getValue(String::class.java)
                    if (userEmail == email) {
                        // Email trovata, esegui l'azione necessaria
                        //println("Email trovata: $userEmail in ${userSnapshot.key}")
                        userFound = userSnapshot.key
                        val first = userSnapshot.child("first").getValue(Boolean::class.java)
                        if (first != null) {
                            isFirst = first
                        }
                        break
                    }
                }
                callback(userFound)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
}