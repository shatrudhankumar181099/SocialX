package com.example.taskforinternship

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    private var mFirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseStore = FirebaseFirestore.getInstance()
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        firebaseAuth = FirebaseAuth.getInstance()

        val registration_btn = findViewById<Button>(R.id.btnRegister)
        registration_btn.setOnClickListener {
            val name = findViewById<EditText>(R.id.EnterName).text.toString()
            val phone = findViewById<EditText>(R.id.phonenumber).text.toString()
            val email = findViewById<EditText>(R.id.EnterEmail2).text.toString()
            val password = findViewById<EditText>(R.id.EntPassword).text.toString()

            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener {
                        registrationProcess ->
                        if (registrationProcess.isSuccessful){

                            val currentUserId = mFirebaseAuth.currentUser!!.uid
                            val userDetailsToStore = User(currentUserId, name, phone, email)
                            registerUser(userDetailsToStore)
                        }
                    }
                )
        }
        



        btnRegister.setOnClickListener{
            SignUpUser()
        }
        btnsignin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun registerUser(userData: User) {
        firebaseStore.collection("users").document().set(userData).addOnSuccessListener {
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener{
                e -> Log.e(javaClass.simpleName, "Error While Registering.",e)
            }

    }


    private fun SignUpUser(){
        val email: String = EnterEmail2.text.toString()
        val person: String = EnterName.text.toString()
        val password: String = EntPassword.text.toString()

        if (email.isBlank() || person.isBlank() || password.isBlank()){
            Toast.makeText(this,"Can't be blank", Toast.LENGTH_SHORT).show()
            return
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this, "Login Succesful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                else{
                    Toast.makeText(this, "Error creating user" ,Toast.LENGTH_SHORT).show()
                }
            }


    }
}

