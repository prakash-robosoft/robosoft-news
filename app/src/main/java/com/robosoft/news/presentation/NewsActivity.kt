package com.robosoft.news.presentation

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.robosoft.news.R


class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Adds Bookmark and Search menu items
        menuInflater.inflate(R.menu.menu_items, menu)
        return true
    }
}
