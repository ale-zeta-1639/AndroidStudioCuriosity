package com.example.appcuriosity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private var userId: String = ""

    /* base output
    * Toast.makeText(this, "tutto OK", Toast.LENGTH_SHORT).show()
    * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        userId = intent.getStringExtra("userId").toString()  //ricezione dell'id utente dall'activity precedente

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle ( this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState == null) {
            // Creazione dell'istanza del Fragment e impostazione degli argomenti
            val homeFragment = HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("userId", userId)
                }
            }
            // Aggiunta del Fragment iniziale
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_home -> {
                val homeFragment = HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString("userId", userId)
                    }
                }

                supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment).commit()
            }

            R.id.nav_settings -> {
                val settingsFragment = SettingsFragment().apply {
                    arguments = Bundle().apply {
                        putString("userId", userId)
                    }
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, settingsFragment)
                    .commit()
            }

            R.id.nav_preference -> {
                val preferenceFragment = PreferenceFragment().apply {
                    arguments = Bundle().apply {
                        putString("userId", userId)
                    }
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, preferenceFragment)
                    .commit()
            }

            R.id.nav_logout -> Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}