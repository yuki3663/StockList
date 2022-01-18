package com.tom.stocktable.viewModel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tom.stocktable.databinding.FragmentLoginBinding
import com.tom.stocktable.databinding.FragmentRegistrationBinding
import com.tom.stocktable.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {
    private val IsRegisterPass = MutableLiveData<Boolean>()
    private val UserName = MutableLiveData<String>()
    private val AlertMessage = MutableLiveData<String?>()
    fun GetIsRegisterPass(): LiveData<Boolean> {
        return IsRegisterPass
    }

    fun GetUserName(): LiveData<String> {
        return UserName
    }

    fun GetAlertMessage(): LiveData<String?> {
        return AlertMessage
    }

    fun CleanAlertMessage() {
        AlertMessage.postValue("")
    }

    fun SendRegister(binding: FragmentRegistrationBinding) {
        val email: String = binding.regInputEmail.getText().toString().trim()
        val password: String = binding.regInputPassword.getText().toString().trim()
        val firstName: String = binding.regInputName.getText().toString().trim()
        val lastName = "Lee"
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            AlertMessage.postValue("Email Address is required...!")
            binding.regInputEmail.requestFocus()
            return
        }
        if (password.isEmpty()) {
            AlertMessage.postValue("Password is required...!")
            binding.regInputPassword.requestFocus()
            return
        }
        if (password.length < 6) {
            AlertMessage.postValue("Password length must be 6 char...!")
            binding.regInputPassword.requestFocus()
            return
        }
        if (firstName.isEmpty()) {
            AlertMessage.postValue("Name is required...!")
            binding.regInputName.requestFocus()
            return
        }

    }

    fun SendSignIn(binding: FragmentLoginBinding) {
        val email: String = binding.inputEmail.getText().toString().trim()
        val password: String = binding.inputPassword.getText().toString().trim()
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            AlertMessage.postValue("Email Address is required...!")
            binding.inputEmail.requestFocus()
            return
        }
        if (password.length < 6) {
            AlertMessage.postValue("Password length must be 6 char...!")
            binding.inputPassword.requestFocus()
            return
        }
    }
}