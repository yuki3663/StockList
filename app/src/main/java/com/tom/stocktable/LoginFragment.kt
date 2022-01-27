package com.tom.stocktable

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tom.stocktable.databinding.FragmentLoginBinding
import com.tom.stocktable.viewModel.AccountDaoViewModel
import com.tom.stocktable.viewModel.AccountDaoViewModelFactory
import com.tom.stocktable.viewModel.LoginViewModel

class LoginFragment : Fragment() {
    private val dataBaseViewModel: AccountDaoViewModel by activityViewModels {
        AccountDaoViewModelFactory(
            (activity?.application as StockTableApplication).database.accountDao()
        )
    }
    private val loginViewModel: LoginViewModel by activityViewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeUserName()
        observeIsSignPass()
        observeAlertMessage()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginFragment = this
    }

    fun onClickCreate() {
        findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
    }

    fun onClickSignIn() {
        loginViewModel.CheckSignIn(binding)
    }

    private fun observeUserName() {
        loginViewModel.GetUserName().observe(this.viewLifecycleOwner) { UserName ->
            if (!UserName.isEmpty()) {
                val intent = Intent(activity, LobbyActivity::class.java)
                val bundle = Bundle()
                bundle.putString("name", UserName) //Your id
                intent.putExtras(bundle) //Put your id to your next Intent
                startActivity(intent)
                showAlert("Get UserData success")
            }
        }
    }
    private fun observeIsSignPass() {
        loginViewModel.GetIsSignPass().observe(this.viewLifecycleOwner) { IsSignPass ->
            if (IsSignPass) {
                dataBaseViewModel.retrieveAccount(binding.inputEmail.text.toString())
                    .observe(this.viewLifecycleOwner){ account ->
                        if(account == null){
                            showAlert("Can't find account.")
                        }else if(account.password != binding.inputPassword.text.toString()){
                            showAlert("Password is wrong.")
                        }else{
                            val intent = Intent(activity, LobbyActivity::class.java)
                            startActivity(intent)
                        }
                    }
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}