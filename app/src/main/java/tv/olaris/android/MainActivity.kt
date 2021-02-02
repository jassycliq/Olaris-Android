package tv.olaris.android

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var navFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navFragment.navController

        drawerLayout = findViewById(R.id.drawer_layout)

        var navView = findViewById<NavigationView>(R.id.navigation_view)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(this, navController, appBarConfiguration)

        navView.setNavigationItemSelectedListener(this)

        val menu = navView.menu

        lifecycleScope.launch {
            //TODO: This is super duper horrible, can we do this differently?
            OlarisApplication.applicationContext().serversRepository.allServers.collect {
                for (s in it) {
                    if (OlarisApplication.applicationContext().checkServer(s)) {
                        val submenu = menu.addSubMenu(s.name)
                        var id = 0

                        submenu.add(0, id, 0, "Dashboard").setOnMenuItemClickListener {
                            navigateTo(R.id.dashboard, s.id)
                            true
                        }


                        id = "${s.id}1".toInt()
                        submenu.add(0, id, 0, "Movies").setOnMenuItemClickListener {
                            navigateTo(R.id.movieLibraryFragment, s.id)
                            true
                        }

                        id = "${s.id}2".toInt()
                        submenu.add(0, id, 0, "TV Shows").setOnMenuItemClickListener {
                            navigateTo(R.id.fragmentShowLibrary, s.id)
                            true
                        }
                    }
                }

                if (it.isNotEmpty() && !OlarisApplication.applicationContext().initialNavigation) {
                    OlarisApplication.applicationContext().initialNavigation = true
                    val s = it.first()
                    if (OlarisApplication.applicationContext().checkServer(s)) {
                        val bundle = bundleOf("serverId" to s.id)
                        navController.graph.startDestination = R.id.dashboard
                        val navOptions =
                            NavOptions.Builder().setPopUpTo(R.id.fragmentServerList, false).build()
                        navController.navigate(
                            R.id.action_fragmentServerList_to_dashboard,
                            bundle,
                            navOptions
                        )
                    }
                }
            }
        }

    }

    private fun navigateTo(fragment: Int, serverId: Int) {
        val bundle = bundleOf("serverId" to serverId)

        navController.navigate(fragment, bundle)

        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("menu", item.itemId.toString())

        when (item.itemId) {
            R.id.item_manage_servers -> {
                navController.navigate(R.id.fragmentServerList)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navFragment.navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}