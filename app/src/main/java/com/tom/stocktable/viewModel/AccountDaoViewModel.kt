package com.tom.stocktable.viewModel

import androidx.lifecycle.*
import com.tom.stocktable.database.account.Account
import com.tom.stocktable.database.account.AccountDao
import kotlinx.coroutines.launch

class AccountDaoViewModel(private val accountDao: AccountDao): ViewModel()  {

    fun checkAccount(email: String, password: String): String {
        val accountData = accountDao.getAccount(email).asLiveData().value
        if(accountData == null){
            return "Can't find account."
        } else if(accountData.password != password){
            return "Password is wrong."
        }
        return "OK"
    }
    fun retrieveAccount(email: String): LiveData<Account> {
        return accountDao.getAccount(email).asLiveData()
    }
    fun addNewAccount(email: String, password: String, userName: String) {
        val newItem = getNewAccountEntry(email, password, userName)
        insertItem(newItem)
    }
    private fun insertItem(account: Account) {
        viewModelScope.launch {
            accountDao.insert(account)
        }
    }
    private fun getNewAccountEntry(email: String, password: String, userName: String): Account {
        return Account(
            email = email,
            password = password,
            userName = userName
        )
    }

}
class AccountDaoViewModelFactory(private val account: AccountDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountDaoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountDaoViewModel(account) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}