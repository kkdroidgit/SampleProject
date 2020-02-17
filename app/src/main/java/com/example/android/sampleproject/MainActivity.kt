package com.example.android.sampleproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.android.sampleproject.model.Bloggers
import com.example.android.sampleproject.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import io.branch.referral.Branch
import io.branch.referral.BranchError
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    private var content: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Branch.enableDebugMode()
        Branch.getAutoInstance(this)

        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        content = findViewById(R.id.container)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = FragmentHome.newInstance()
        addFragment(fragment)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val fragment = FragmentHome.newInstance()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_sms -> {
                    val fragment =
                        FragmentTwo()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    val fragment =
                        FragmentThree()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_featured -> {
                    val fragment =
                        FragmentFour()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        Branch.enableSimulateInstalls()
        Branch.getInstance().initSession(
            { referringParams: JSONObject?, error: BranchError? ->
                if (error == null) {
                    Log.i("BRANCH SDK PARA", referringParams.toString())

                    val blogName = referringParams?.get("blogger_name")
                    val blogUrl = referringParams?.get("blog_url")
                    val blogImage = referringParams?.get("blogger_image")
                    val blogAbout = referringParams?.get("blogger_about")

                    val blogObject = Bloggers(
                        blogImage.toString(),
                        blogName.toString(), blogUrl.toString(), blogAbout.toString()
                    )

                    val intent = Intent(this, BloggerDetailActivity::class.java)
                    intent.putExtra("EXTRA", blogObject)
                    startActivity(intent)

                } else {
                    Log.i("BRANCH SDK", error.message)
                }

            }, this.intent.data, this
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        Branch.getInstance().reInitSession(this@MainActivity) {
                referringParams: JSONObject?, error: BranchError? ->
            if (error == null) {
                Log.i("BRANCH SDK PARA", referringParams.toString())
            } else {
                Log.i("BRANCH SDK", error.message)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(this, BloggersActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_messages -> {
                Toast.makeText(this, "Messages clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_bookmark -> {
                Toast.makeText(this, "bookmark clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_dark -> {
                Toast.makeText(this, "dark clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_help -> {
                Toast.makeText(this, "help clicked", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}