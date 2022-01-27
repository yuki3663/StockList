package com.tom.stocktable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tom.stocktable.databinding.FragmentRegistrationBinding
import com.tom.stocktable.viewModel.AccountDaoViewModel
import com.tom.stocktable.viewModel.AccountDaoViewModelFactory
import com.tom.stocktable.viewModel.LoginViewModel

class RegistrationFragment : Fragment() {
    private val dataBaseViewModel: AccountDaoViewModel by activityViewModels {
        AccountDaoViewModelFactory(
            (activity?.application as StockTableApplication).database.accountDao()
        )
    }
    private val loginViewModel: LoginViewModel by activityViewModels()
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeIsRegisterPass()
        observeAlertMessage()
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registrationFragment = this
    }

    fun onClickRegister() {
        loginViewModel.CheckRegister(binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun observeIsRegisterPass() {
        loginViewModel.GetIsRegisterPass().observe(this.viewLifecycleOwner) { IsRegisterPass ->
            if (IsRegisterPass) {
                dataBaseViewModel.addNewAccount(
                    binding.regInputEmail.text.toString(),
                    binding.regInputPassword.text.toString(),
                    binding.regInputName.text.toString()
                )
                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                showAlert("Registration success")
            }
        }
    }

    private fun observeAlertMessage() {
        loginViewModel.GetAlertMessage().observe(this.viewLifecycleOwner) { AlertMessage ->
            if (!AlertMessage.isNullOrBlank()) {
                showAlert(AlertMessage)
            }
        }
    }

    private fun showAlert(msg: String) {
        AlertDialog.Builder(this.requireContext())
            .setMessage(msg)
            .setPositiveButton("OK", null)
            .create().show()
        loginViewModel.CleanAlertMessage()
    }
}