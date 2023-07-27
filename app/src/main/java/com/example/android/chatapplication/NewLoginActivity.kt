package com.example.android.chatapplication

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.android.chatapplication.databinding.ActivityCreateUserBinding
import com.example.android.chatapplication.databinding.ActivityLoginBinding
import com.example.android.chatapplication.models.ProfileDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewLoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private var imageUrl : Uri? = null
    private lateinit var dataBaseReference : DatabaseReference

    companion object{
        const val KEY = "LoginActivity.mail"
        const val KEY1 = "LoginActivity.image"
        const val KEY2 = "LoginActivity.name"
        const val KEY3 = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createSpinner.visibility = View.INVISIBLE


//        Toast.makeText(this, uniqueId, Toast.LENGTH_SHORT).show()


        binding.loginloginbtn.setOnClickListener {

            val email = binding.loginemailtxt.text.toString()
            val password = binding.loginpasswordtxt.text.toString()
            hideKeyboard()
            if ( email.isNotEmpty() && password.isNotEmpty() ) {
                enableSpinner(true)
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
//                            val firrebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this,
                                "You are Logged In",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.loginemailtxt.text.clear()
                            binding.loginpasswordtxt.text.clear()
                            getData()
                        } else {
                            enableSpinner(false)
                            Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .addOnFailureListener{
                        enableSpinner(false)
                        Toast.makeText(this,"Error Occured", Toast.LENGTH_SHORT).show()
                    }

            }else{
                Toast.makeText(this,"All fields should be filled ", Toast.LENGTH_LONG).show()
                enableSpinner(false)
            }
        }
        binding.loginnewuserbtn.setOnClickListener {
            val createUserIntent = Intent(this, CreateUserActivity::class.java)
            startActivity(createUserIntent)
        }
    }

    fun getData( ){

        val db = Firebase.firestore
        val docRef = db.collection("Login Credentials").document(getCurrentUUID())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val loggedInUser = document.toObject(ProfileDetails::class.java)!!
                    val Pd = ProfileDetails(loggedInUser.email,loggedInUser.img,loggedInUser.userName)
                    Toast.makeText(this,"DocumentSnapshot data: ${document.data}" ,Toast.LENGTH_SHORT).show()
                    enableSpinner(false)
                    val mainIntent = Intent(this, MainActivity::class.java)
                    mainIntent.putExtra(KEY,loggedInUser.email)
                    mainIntent.putExtra(KEY1,loggedInUser.img)
                    mainIntent.putExtra(KEY2,loggedInUser.userName)
                    mainIntent.putExtra(KEY3,"LOGIN")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainIntent)
                } else {
                    Toast.makeText(this,"No such document",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"get failed with " + " "+ exception,Toast.LENGTH_SHORT).show()
            }
    }

    fun enableSpinner(enable: Boolean ){

        if ( enable ){
            binding.createSpinner.visibility = View.VISIBLE
        }else{
            binding.createSpinner.visibility = View.INVISIBLE
        }
        binding.loginnewuserbtn.isEnabled = !enable
        binding.loginloginbtn.isEnabled = !enable
    }

    fun getCurrentUUID() : String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if ( currentUser != null ){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if ( inputManager.isAcceptingText ){
            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
        }
    }
}