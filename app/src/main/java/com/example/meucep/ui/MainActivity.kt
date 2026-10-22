package com.example.meucep.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.viewModels
import com.example.meucep.databinding.ActivityMainBinding
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CepViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnSearch.setOnClickListener {
            val cep = binding.edtCep.text.toString().trim()
            if (cep.length >= 8) {
                viewModel.searchCep(cep)
            } else {
                Toast.makeText(this, "Por favor, digite um CEP válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.cepData.observe(this) { cepResponse ->
            cepResponse?.let {
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("EXTRA_CEP_DATA", it as Serializable)
                    putExtra("EXTRA_FLAG_URL", viewModel.flagUrl.value)
                }
                startActivity(intent)
            }
        }
        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
