package com.example.lab9_database

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var myDBAdapter : MyDBAdapter? = null
    private val mAllFaculties = arrayOf("Engineering", "Business", "Arts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initializeViews()
        initializeDatabase()
        loadList()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem):Boolean{
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initializeViews(){
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        faculties_spinner.adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, mAllFaculties)

        add_student.setOnClickListener { myDBAdapter?.insertStudent(student_name.text.toString(), faculties_spinner.selectedItemPosition + 1)
            loadList()
        }

        delete_engineer.setOnClickListener {
            myDBAdapter?.deleteAllEngineers()
            loadList()
        }

    }

    private fun initializeDatabase(){
        myDBAdapter = MyDBAdapter(this@MainActivity)
        myDBAdapter?.open()
    }

    private fun loadList(){
        val allStudents: List<String> = myDBAdapter!!.selectAllStudents()
        val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, allStudents)
        student_list.adapter = adapter
    }
}
