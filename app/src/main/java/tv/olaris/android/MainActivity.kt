package tv.olaris.android

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var navFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navFragment.navController
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        var navView = findViewById<NavigationView>(R.id.navigation_view)
        navView.setNavigationItemSelectedListener(this)
        val menu = navView.menu


        setupActionBarWithNavController(navController, appBarConfiguration)
        lifecycleScope.launch {
            //TODO: This is super duper horrible, can we do this differently?
            OlarisApplication.applicationContext().serversRepository.allServers.collect {
                for (s in it) {
                    val submenu = menu.addSubMenu(s.name)
                    var id = "${s.id}1".toInt()

                    submenu.add(0, id, 0, "Movies").setOnMenuItemClickListener(
                        MenuItem.OnMenuItemClickListener {
                            val bundle = bundleOf("server_id" to s.id)

                            navController.navigate(R.id.movieLibraryFragment, bundle)
                            drawerLayout.closeDrawer(GravityCompat.START)
                            true
                        }
                    )
                    id = "${s.id}2".toInt()
                    submenu.add(0, id, 0, "TV Shows").setOnMenuItemClickListener(
                        MenuItem.OnMenuItemClickListener {
                            val bundle = bundleOf("serverId" to s.id)

                            navController.navigate(R.id.fragmentShowLibrary, bundle)
                            drawerLayout.closeDrawer(GravityCompat.START)
                            true
                        }
                    )

                }
            }
        }
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
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
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