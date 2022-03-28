package com.example.projet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.projet.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        drawerLayout = binding.homeDrawerLayout
        val navController = this.findNavController(R.id.homeNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.homeNavView, navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.homeNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onPause() {
        val intent = Intent(this.applicationContext, AuthenticationActivity::class.java)
        startActivity(intent)
        super.onPause()
    }

}