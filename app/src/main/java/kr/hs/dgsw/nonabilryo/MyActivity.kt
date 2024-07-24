package kr.hs.dgsw.nonabilryo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import kr.hs.dgsw.nonabilryo.R

class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my)

        val gridView: GridView = findViewById(R.id.List_view)
        val gridAdapter = GridAdapter(this)
        gridView.adapter = gridAdapter

    }
}