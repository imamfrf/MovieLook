package com.imamfrf.movielook.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.androidnetworking.AndroidNetworking
import com.imamfrf.movielook.R
import com.imamfrf.movielook.ui.main.favorite.FavoriteFragment
import com.imamfrf.movielook.ui.main.home.HomeFragment
import com.imamfrf.movielook.utils.TabAdapter
import com.ogaclejapan.smarttablayout.SmartTabLayout

class MainActivity : AppCompatActivity() {

    companion object {
        val homeFragment = HomeFragment()
        val favoriteFragment = FavoriteFragment()
    }

    private lateinit var adapter: TabAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerTab: SmartTabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidNetworking.initialize(applicationContext)

        viewPager = findViewById(R.id.view_pager_main)

        adapter = TabAdapter(supportFragmentManager)
        adapter.addFragment(homeFragment, "Home")
        adapter.addFragment(favoriteFragment, "Favorite")


        viewPager.adapter = adapter

        viewPagerTab = findViewById(R.id.tab_layout_main)
        viewPagerTab.setViewPager(viewPager)
    }


}
