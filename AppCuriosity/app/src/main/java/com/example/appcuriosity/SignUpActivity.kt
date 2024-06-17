package com.example.appcuriosity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appcuriosity.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDB : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpButton.setOnClickListener{
            val email = binding.signUpEmail.text.toString()
            val password = binding.signUpPassword.text.toString()
            val confirmPassword = binding.signUpConfirmPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() {
                        if(it.isSuccessful){
                            saveData(email)
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginRedirectText.setOnClickListener{
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    /*
    * salvataggio dei dati con realizzazione gerarchia del DB
    * */
    private fun saveData(email:String){
        val user = User(email)
        user.setFirst(true)

        firebaseDB = FirebaseDatabase.getInstance("https://appcuriosity-5688a-default-rtdb.europe-west1.firebasedatabase.app/")
        firebaseDB.getReference("Users").child(firebaseAuth.currentUser!!.uid).setValue(user).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Sign Up completed", Toast.LENGTH_SHORT).show()
                }
        }.addOnFailureListener{e ->
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}