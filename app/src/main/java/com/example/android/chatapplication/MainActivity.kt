package com.example.android.chatapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.android.chatapplication.databinding.ActivityMainBinding
import com.example.android.chatapplication.models.ProfileDetails
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val Pd = ProfileDetails()
        val email = intent.getStringExtra(NewLoginActivity.KEY)
        val image = intent.getStringExtra(NewLoginActivity.KEY1)
        val name = intent.getStringExtra(NewLoginActivity.KEY2)

//        val email1 = Pd.email
//        val image1 = Pd.img
//        val name1 = Pd.userName


        setSupportActionBar(binding.appBarMain.toolbar)

        val loginbtn : Button = findViewById(R.id.loginButtonNavHeader)
        val addchannelbtn : ImageButton = findViewById(R.id.addChannelButton)
        val sendmessagebtn : ImageButton = findViewById(R.id.sendMessageBtn)

        binding.navDraawerHeaderInclude.userNameNavHeader.text = name?:"Unknown User"
        binding.navDraawerHeaderInclude.userEmailNavHeader.text = email?:"Email"
        Glide.with(this)
            .load(image ?:R.drawable.profiledefault)
            .into(binding.navDraawerHeaderInclude.userImageNavHeader)


        loginbtn.setOnClickListener {
            val loginIntent = Intent(this, NewLoginActivity::class.java)
            startActivity(loginIntent)
        }

        addchannelbtn.setOnClickListener {

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

}