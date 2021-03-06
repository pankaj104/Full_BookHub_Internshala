package abc.com.bookhub.activity

import abc.com.bookhub.fragment.DashboardFragment
import abc.com.bookhub.fragment.ProfileFragment
import abc.com.bookhub.R
import abc.com.bookhub.fragment.AboutFragment
import abc.com.bookhub.fragment.FavouritesFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar

// androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationView: NavigationView
    lateinit var frameLayout: FrameLayout
    var previousMenuItem: MenuItem?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frame)
        navigationView=findViewById(R.id.navigationView)
        setUpToolbar()
       openDashboard()


        val actionBarDrawerToggle= ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if (previousMenuItem !=null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem= it

            when(it.itemId){
                R.id.dasboard -> {

                    openDashboard()
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame, DashboardFragment())
//                        .addToBackStack("Dashboard")
//                        .commit()
//
//                    drawerLayout.closeDrawers()
//                    supportActionBar?.title="Dashboard"
                }

                R.id.favorites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouritesFragment())

                        .commit()

                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Favorites"
                }

                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())

                        .commit()

                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Profile"
                }
                R.id.about_app -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutFragment())

                        .commit()

                    drawerLayout.closeDrawers()
                    supportActionBar?.title="About App"
                }
            }

            return@setNavigationItemSelectedListener true
        }

    }
    fun setUpToolbar()

    {
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Hub"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val  id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard(){
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, DashboardFragment())
            transaction.commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dasboard)
    }

    override fun onBackPressed() {
        val frag= supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment -> openDashboard()
            else-> super.onBackPressed()
        }
    }

}