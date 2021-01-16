package tv.olaris.android

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var navFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navFragment.navController
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        appBarConfiguration =  AppBarConfiguration(navController.graph, drawerLayout)

        var navView = findViewById<NavigationView>(R.id.navigation_view)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("menu", item.itemId.toString())

         when(item.itemId) {
            R.id.item_manage_servers -> {
                navController.navigate(R.id.fragmentServerList)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                return true
            }  else -> super.onOptionsItemSelected(item)
        }
    }
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
                return true
            }  else -> super.onOptionsItemSelected(item)
    }}*/

    override fun onSupportNavigateUp(): Boolean {
        val navFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navFragment.navController.navigateUp()
                || super.onSupportNavigateUp()
    }

}