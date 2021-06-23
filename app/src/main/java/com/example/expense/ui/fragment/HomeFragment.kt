package com.example.expense.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.expense.DebitFragment
import com.example.expense.R
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentNavBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentNavBackStackEntry!!.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(OnBoardingFragment.LOGIN_SUCCESSFUL).observe(
            currentNavBackStackEntry
        ) {
            if (!it) {
                //when user pressed back button on onBoarding fragment then exit the app
                //activity?.finishAffinity()
                val startDes = findNavController().graph.startDestination
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(startDes, true)
                    .build()
                findNavController().navigate(startDes, null, navOptions)
            }
        }

        setHasOptionsMenu(true)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTabBar()
        val preferences = this.requireActivity()
            .getSharedPreferences("pref", Context.MODE_PRIVATE)

        val check = preferences.getString(OnBoardingFragment.LOGGED, null)
        if (check != "SUCCESS") {
            findNavController().navigate(R.id.onboardingFragment)
        }

        fb_addGroceryItem.setOnClickListener {
            findNavController().navigate(R.id.addTransactionFragment)
        }

    }

    private fun setUpTabBar() {

        val adapter = FragmentPagerItemAdapter(
            childFragmentManager,
            FragmentPagerItems.with(activity)
                .add("All", AllFragment::class.java)
                .add("Cash", CashFragment::class.java)
                .add("Credit", CreditFragment::class.java)
                .add("Debit", DebitFragment::class.java)
                .create()
        )

        viewpager.adapter = adapter
        viewpagertab.setViewPager(viewpager)




    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.menu_cal -> {
                //Toast.makeText(context, "Claender clicked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.calenderFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        })
    }
}