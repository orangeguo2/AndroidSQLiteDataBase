package com.example.contentprovider

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
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
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        var toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    public fun addRecord(view: View) {
        var values = ContentValues()
        if (!(name.text.toString().isEmpty())
            && (!(nickname.text.toString().isEmpty()))) {
            values.put(CustomContentProvider.NAME, name.text.toString())
            values.put(CustomContentProvider.NICK_NAME, nickname.text.toString())
            contentResolver.insert(CustomContentProvider.CONTENT_URI, values)
            Toast.makeText(baseContext, "Please Inserted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(baseContext, "Please enter the records first", Toast.LENGTH_LONG).show()
        }
    }

    public fun showAllRecords(view: View) {
        val URL = "content://com.androidatc.provider/nicknames"
        var friends = Uri.parse(URL)
        var c = contentResolver.query(friends, null, null, null, "name")
        var result = "Content Provider Results:"
        if (!c!!.moveToFirst()) {
            Toast.makeText(this, result + " no content yet!", Toast.LENGTH_LONG).show()
        } else {
            do {
                result += "\n${c.getString(c.getColumnIndex(CustomContentProvider.
                NAME))} with id ${c.getString(c.getColumnIndex(CustomContentProvider.
                NICK_NAME))}"
            } while (c.moveToNext())
            if (!result.isEmpty())
                Toast.makeText(this, result, Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this, "No Records present", Toast.LENGTH_LONG).show()
        }
    }

    public fun deleteAllRecords(view: View) {
        val URL = "content://com.androidatc.provider/nicknames"
        var friends = Uri.parse(URL)
        var count = contentResolver.delete(friends, null, null)
        var countNum = "$count records are deleted."
        Toast.makeText(baseContext, countNum, Toast.LENGTH_LONG).show()
    }

}
