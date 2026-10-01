package com.mato.studio.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mato.studio.core.common.ResultState
import com.mato.studio.network.response.Currency
import com.mato.studio.network.response.TransactionModel
import com.mato.studio.repo.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ActiveSheet {
    NONE,
    DEPOSIT,
    WITHDRAW
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    val balance: StateFlow<Double> = repository.observeBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val transactions: StateFlow<List<TransactionModel>> = repository.observeTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeSheet = MutableStateFlow(ActiveSheet.NONE)
    val activeSheet: StateFlow<ActiveSheet> = _activeSheet.asStateFlow()

    private val _selectedCurrency = MutableStateFlow(Currency.USD)
    val selectedCurrency: StateFlow<Currency> = _selectedCurrency.asStateFlow()

    private val _amountInput = MutableStateFlow("")
    val amountInput: StateFlow<String> = _amountInput.asStateFlow()

    private val _operationState = MutableStateFlow<ResultState<Double>>(ResultState.Idle)
    val operationState: StateFlow<ResultState<Double>> = _operationState.asStateFlow()

    private val _isHistoryExpanded = MutableStateFlow(false)
    val isHistoryExpanded: StateFlow<Boolean> = _isHistoryExpanded.asStateFlow()

    fun openDepositSheet() {
        _amountInput.value = ""
        _selectedCurrency.value = Currency.USD
        _operationState.value = ResultState.Idle
        _activeSheet.value = ActiveSheet.DEPOSIT
    }

    fun openWithdrawSheet() {
        _amountInput.value = ""
        _selectedCurrency.value = Currency.INR
        _operationState.value = ResultState.Idle
        _activeSheet.value = ActiveSheet.WITHDRAW
    }

    fun closeSheet() {
        _activeSheet.value = ActiveSheet.NONE
        _amountInput.value = ""
        _operationState.value = ResultState.Idle
    }

    fun selectCurrency(currency: Currency) {
        _selectedCurrency.value = currency
    }

    fun onNumpadKey(key: String) {
        val current = _amountInput.value
        when (key) {
            "." -> {
                if (current.isEmpty()) {
                    _amountInput.value = "0."
                } else if (!current.contains(".")) {
                    _amountInput.value = "$current."
                }
            }
            "00" -> {
                if (current.isNotEmpty() && current != "0") {
                    if (current.contains(".")) {
                        val decimalPart = current.substringAfter(".")
                        if (decimalPart.length < 2) {
                            _amountInput.value = "$current${"0".repeat(2 - decimalPart.length)}"
                        }
                    } else if (current.length < 9) {
                        _amountInput.value = "${current}00"
                    }
                }
            }
            else -> {

                if (current == "0") {
                    _amountInput.value = key
                } else if (current.contains(".")) {
                    val decimalPart = current.substringAfter(".")
                    if (decimalPart.length < 2) {
                        _amountInput.value = "$current$key"
                    }
                } else {
                    if (current.length < 9) {
                        _amountInput.value = "$current$key"
                    }
                }
            }
        }
        _operationState.value = ResultState.Idle
    }

    fun onNumpadBackspace() {
        val current = _amountInput.value
        if (current.isNotEmpty()) {
            _amountInput.value = current.dropLast(1)
        }
        _operationState.value = ResultState.Idle
    }

    fun onNumpadClear() {
        _amountInput.value = ""
        _operationState.value = ResultState.Idle
    }

    fun confirmOperation() {
        val amount = _amountInput.value.toDoubleOrNull() ?: 0.0
        if (amount <= 0) {
            _operationState.value = ResultState.Error("Please enter a valid amount")
            return
        }

        viewModelScope.launch {
            _operationState.value = ResultState.Loading
            when (_activeSheet.value) {
                ActiveSheet.DEPOSIT -> {
                    val result = repository.deposit(_selectedCurrency.value, amount)
                    result.fold(
                        onSuccess = { convertedInr ->
                            _operationState.value = ResultState.Success(convertedInr)
                            _activeSheet.value = ActiveSheet.NONE
                            _amountInput.value = ""
                        },
                        onFailure = { error ->
                            _operationState.value = ResultState.Error(
                                error.message ?: "Deposit failed. Please check network and try again."
                            )
                        }
                    )
                }
                ActiveSheet.WITHDRAW -> {
                    val result = repository.withdraw(amount)
                    result.fold(
                        onSuccess = { newBalance ->
                            _operationState.value = ResultState.Success(newBalance)
                            _activeSheet.value = ActiveSheet.NONE
                            _amountInput.value = ""
                        },
                        onFailure = { error ->
                            _operationState.value = ResultState.Error(
                                error.message ?: "Withdrawal failed. Insufficient balance."
                            )
                        }
                    )
                }
                ActiveSheet.NONE -> {}
            }
        }
    }

    fun toggleHistoryExpand() {
        _isHistoryExpanded.value = !_isHistoryExpanded.value
    }
}
