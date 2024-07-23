package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.navigation_community -> {
                    startActivity(Intent(this, CommunityActivity::class.java))
                    true
                }
                R.id.navigation_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                R.id.navigation_my -> {
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.navigation_my
    }
}