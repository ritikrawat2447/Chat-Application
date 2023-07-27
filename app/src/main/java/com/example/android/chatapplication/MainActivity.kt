package com.example.android.chatapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.android.chatapplication.databinding.ActivityMainBinding
import com.example.android.chatapplication.models.ProfileDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val Pd = ProfileDetails()
        val email = intent.getStringExtra(NewLoginActivity.KEY)
        val image = intent.getStringExtra(NewLoginActivity.KEY1)
        val name = intent.getStringExtra(NewLoginActivity.KEY2)
        val isLoggedIn = intent.getStringExtra(NewLoginActivity.KEY3)


        setSupportActionBar(binding.appBarMain.toolbar)

        hideKeyboard()

        val loginbtn : Button = findViewById(R.id.loginButtonNavHeader)
        val addchannelbtn : ImageButton = findViewById(R.id.addChannelButton)
        val sendmessagebtn : ImageButton = findViewById(R.id.sendMessageBtn)

        binding.navDraawerHeaderInclude.userNameNavHeader.text = name?:"Unknown User"
        binding.navDraawerHeaderInclude.userEmailNavHeader.text = email?:"Email"
        Glide.with(this)
            .load(image ?:R.drawable.profiledefault)
            .into(binding.navDraawerHeaderInclude.userImageNavHeader)

        if ( email != null ) {
            binding.navDraawerHeaderInclude.loginButtonNavHeader.text = "Log Out"
        }


        loginbtn.setOnClickListener {
            if ( email == null ) {
                val loginIntent = Intent(this, NewLoginActivity::class.java)
                startActivity(loginIntent)
            }else {
                Toast.makeText(this,"Enter EMail !!!!",Toast.LENGTH_SHORT).show()
                signOut()

            }
        }

        addchannelbtn.setOnClickListener {
            if ( isLoggedIn != null ){
                val builder =  AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog,null)

                builder.setView(dialogView)
                    .setPositiveButton("Add"){ dialogInterface,i ->
                        val nameTextField = dialogView.findViewById<EditText>(R.id.addChannelName)
                        val descTextField = dialogView.findViewById<EditText>(R.id.addChannelDiscription)

                        val channelName = nameTextField.text.toString()
                        val channelDesc = descTextField.text.toString()

                        hideKeyboard()


                    }
                    .setNegativeButton("Cancel"){ dialogInterface,i ->
                        hideKeyboard()
                    }
                    .show()
            }
        }

        sendmessagebtn.setOnClickListener {

        }





        // the below line of code is for the message button clicked , which shows snackbar(like toast) , we did not need it .
//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
    @Override fun OnBackPressed(){

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
        startActivity(Intent(this, MainActivity::class.java))
        finish()

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