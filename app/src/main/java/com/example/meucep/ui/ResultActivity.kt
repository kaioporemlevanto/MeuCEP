package com.example.meucep.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.meucep.R
import com.example.meucep.databinding.ActivityResultBinding
import com.example.meucep.model.CepResponse


class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cepData = intent.getSerializableExtra("EXTRA_CEP_DATA") as? CepResponse
        val flagUrl = intent.getStringExtra("EXTRA_FLAG_URL")

        if (cepData != null) {
            populateFields(cepData, flagUrl)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun populateFields(cep: CepResponse, flagUrl: String?) {
        binding.txtResultCep.text = "${getString(R.string.lbl_cep)} ${cep.cep}"
        binding.txtResultStreet.text = "${getString(R.string.lbl_street)} ${cep.logradouro}"
        binding.txtResultComplement.text = "Complemento: ${cep.complemento.ifEmpty { "-" }}"
        binding.txtResultNeighborhood.text = "${getString(R.string.lbl_neighborhood)} ${cep.bairro}"
        binding.txtResultCity.text = "${getString(R.string.lbl_city)} ${cep.localidade} - ${cep.uf}"
        binding.txtStateName.text = cep.uf

        flagUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .into(binding.imgStateFlag)
        }
    }
}
