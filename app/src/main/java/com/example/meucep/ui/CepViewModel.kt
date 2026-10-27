package com.example.meucep.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meucep.data.CepRepository
import com.example.meucep.model.CepResponse
import kotlinx.coroutines.launch

class CepViewModel(private val repository: CepRepository) : ViewModel() {
    private val _cepData = MutableLiveData<CepResponse?>()
    val cepData: LiveData<CepResponse?> = _cepData

    private val _flagUrl = MutableLiveData<String>()
    val flagUrl: LiveData<String> = _flagUrl

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun searchCep(cep: String) {
        // 1. Limpa a bandeira antiga antes de começar a nova pesquisa
        _flagUrl.value = ""

        viewModelScope.launch {
            try {
                val response = repository.fetchCep(cep)
                if (response.isSuccessful && response.body() != null) {
                    val cepData = response.body()!!

                    // 2. Verifica se o ViaCEP retornou um CEP vazio/inexistente
                    if (cepData.cep.isEmpty()) {
                        _error.postValue("CEP não encontrado.")
                        return@launch // Interrompe a função aqui para não abrir a tela
                    }

                    // 3. Se o CEP for válido, busca a bandeira
                    try {
                        val flagsResponse = repository.fetchFlags()
                        if (flagsResponse.isSuccessful && flagsResponse.body() != null) {
                            val flags = flagsResponse.body()!!
                            val estado = flags.find { it.uf.equals(cepData.uf, ignoreCase = true) }

                            estado?.let { _flagUrl.postValue(it.flagUrlRounded) }
                        }
                    } catch (e: Exception) {
                        // Ignora erro da bandeira
                    }

                    // 4. Só avisa a MainActivity para abrir a tela após tudo estar pronto
                    _cepData.postValue(cepData)

                } else {
                    _error.postValue("CEP não encontrado.")
                }
            } catch (e: Exception) {
                _error.postValue("Erro de conexão.")
            }
        }
    }
}
