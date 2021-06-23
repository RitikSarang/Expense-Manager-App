package com.example.expense.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.expense.R
import com.example.expense.data.model.SingleUser
import com.example.expense.ui.viewmodel.BoardingViewModel
import kotlinx.android.synthetic.main.fragment_onboarding.*


class OnBoardingFragment : Fragment(R.layout.fragment_onboarding) {

    companion object {
        const val LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL"
        const val LOGGED = "LOGGED_SUCCESS"
    }


    private val viewModel : BoardingViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(LOGIN_SUCCESSFUL, false)

        val preferences = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)


        textInputEditText_name.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                textInputLayout_name.error = "Name field can't be empty"
            } else {
                textInputLayout_name.error = null
            }
        }
        textInputEditText_budget.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                textInputLayout_budget.error = "Monthly budget field can't be empty"
            } else {
                textInputLayout_budget.error = null
            }
        }
        btn_continue.setOnClickListener {
            saveData(savedStateHandle,preferences)
        }

    }


    private fun saveData(savedStateHandle: SavedStateHandle,preferences: SharedPreferences) {
        val name = textInputEditText_name.text.toString()
        val budget = textInputEditText_budget.text.toString()
        val income = textInputEditText_income.text.toString()

        validateField(name, budget, income, savedStateHandle,preferences)

    }
    private fun validateField(name: String, budget: String, income: String, savedStateHandle: SavedStateHandle, preferences: SharedPreferences) {
        if (name.isNotBlank() && budget.isNotBlank()) {
            // when fields are not empty
            if (textInputEditText_income.text.toString().isNotBlank()) {
                //UserLoginInfo.user = User(name, budget.toInt(), income.toInt())

                viewModel.insertUser(
                    SingleUser(
                        viewModel.getid.value!!,
                        name,
                        budget.toInt(),
                        income.toInt()
                    )
                )
                //getUser
                savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                with(preferences.edit()){
                    putString(LOGGED,"SUCCESS")
                        .commit()
                }
                findNavController().popBackStack()
            }else{
                viewModel.insertUser(SingleUser(viewModel.getid.value!!, name, budget.toInt()))
                savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                with(preferences.edit()){
                    putString(LOGGED,"SUCCESS")
                        .commit()
                }
                findNavController().popBackStack()
            }
        } else {
            //when fields are empty
            if (name.isBlank()) {
                textInputLayout_name.error = "Name field can't be empty"
            } else {
                textInputLayout_name.error = null
            }

            if (budget.isBlank()) {
                textInputLayout_budget.error = "Monthly budget field can't be empty"
            } else {
                textInputLayout_budget.error = null
            }
        }
    }



    /*private fun validateFields(name: String, budget: Int): Boolean {
        var check = false
        if (name.isBlank()) {
            textInputLayout_name.error = "Name field can't be empty"
        } else {
            textInputLayout_name.error = null
        }

        if (budget.toString().isNotBlank()) {
            textInputLayout_budget.error = "Monthly budget field can't be empty"
        } else {
            textInputLayout_budget.error = null
        }

        if (name.isNotBlank() && budget.toString().isNotBlank()) {
            check = true
        }
        return check
    }*/

}
