package com.example.android.chatapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.android.chatapplication.databinding.ActivityCreateUserBinding
import com.example.android.chatapplication.models.ProfileDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class CreateUserActivity : AppCompatActivity() {

//    var userAvatar = "profileDefault"
//    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    private lateinit var binding : ActivityCreateUserBinding
    private var imageUrl : Uri? = null
    private lateinit var databaseReference : DatabaseReference

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK ){
            imageUrl = it.data!!.data
            binding.userProfilePhoto.setImageURI(imageUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        val createuserbtn : Button = findViewById(R.id.createUserBtn)
//        val profilePhoto : ImageView = findViewById(R.id.userProfilePhoto)


        binding.createSpinner.visibility = View.INVISIBLE

        binding.createUserBtn.setOnClickListener {
            val userName = binding.createUserNameTxt.text.toString()
            val email = binding.createEmailTxt.text.toString()
            val password = binding.createPasswordTxt.text.toString()

            if ( userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && imageUrl != null ) {
                enableSpinner(true)
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firrebaseUser: FirebaseUser = task.result!!.user!!

                            Toast.makeText(
                                this,
                                "You are registered Successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                            uploadImage(userName,email)
                        } else {
                            enableSpinner(false)
                            Toast.makeText(
                                this,
                                task.exception!!.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener {
                        enableSpinner(false)
                        Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(this,"All fields should be filled ",Toast.LENGTH_LONG).show()
                    enableSpinner(false)
                }
        }

        binding.userProfilePhoto.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong try again", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean ){
//        val createSpinner : ProgressBar = findViewById(R.id.createSpinner2)
        val UserProfile : ImageView = findViewById(R.id.userProfilePhoto)
////        val backgroundcolorbtn : Button = findViewById(R.id.backgroundColorBtn)
        val createuserbtn : Button = findViewById(R.id.createUserBtn)

        if ( enable ){
            binding.createSpinner.visibility = View.VISIBLE
        }else {
            binding.createSpinner.visibility = View.INVISIBLE
        }
        UserProfile.isEnabled = !enable
//        backgroundcolorbtn.isEnabled = !enable
        createuserbtn.isEnabled = !enable
    }

    fun uploadImage(userName : String , email : String){
        val fileName = UUID.randomUUID().toString()+".jpg"
        val refstorage = FirebaseStorage.getInstance().reference.child("ProfileImages/$fileName")
        refstorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData (userName, image.toString() , email)
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Something went wrong with storage", Toast.LENGTH_SHORT).show()

            }
    }
    fun storeData(userName : String , url : String , email : String){
//            }

        val db = FirebaseFirestore.getInstance()

        val data = hashMapOf<String,Any>( // create hm to store data in db .
            "img" to url,
            "userName" to userName,
            "email" to email
        )
        db.collection("Login Credentials").document(getCurrentUUID()).set(data)
            .addOnSuccessListener {

                Toast.makeText(this,"User Data Stored",Toast.LENGTH_SHORT).show()
                enableSpinner(false)
                val loginIntent = Intent(this, NewLoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(loginIntent)
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
    }

    fun getCurrentUUID() : String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if ( currentUser != null ){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

}