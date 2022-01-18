package com.tom.stocktable

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.tom.stocktable.R
import com.tom.stocktable.databinding.FragmentLoginBinding
import com.tom.stocktable.viewModel.LoginViewModel

class LoginFragment : Fragment() {

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
        ObserveUserName()
        ObserveAlertMessage()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.loginFragment = this
    }

    fun onClickCreate() {
        findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
    }

    fun onClickSignIn() {
        val intent = Intent(activity, LobbyActivity::class.java)
        val bundle = Bundle()
        bundle.putString("name", "UserName") //Your id
        intent.putExtras(bundle) //Put your id to your next Intent
        startActivity(intent)
        showAlert("Get UserData success")
    }

    private fun ObserveUserName() {
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

    private fun ObserveAlertMessage() {
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