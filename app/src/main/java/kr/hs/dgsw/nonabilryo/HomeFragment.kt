package kr.hs.dgsw.nonabilryo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView: BottomNavigationView = view.findViewById(R.id.bottom_navigation_view)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Handle Home navigation
                    true
                }
                R.id.navigation_community -> {
                    // Handle Community navigation
                    true
                }
                R.id.navigation_chat -> {
                    // Handle Chat navigation
                    true
                }
                R.id.navigation_my -> {
                    // Handle My navigation
                    true
                }
                else -> false
            }
        }

        // Set default fragment or action
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }
}