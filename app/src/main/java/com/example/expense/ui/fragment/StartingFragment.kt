package com.example.expense.ui.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.example.expense.R
import kotlinx.android.synthetic.main.fragment_starting.*

@Suppress("DEPRECATION")
class StartingFragment : Fragment(R.layout.fragment_starting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_continue.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }
}