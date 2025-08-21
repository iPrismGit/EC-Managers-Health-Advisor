package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.iprism.ecmhealthadvisor.adapters.HospitalTariffsAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityHospitalTariffsListBinding
import com.iprism.ecmhealthadvisor.interfaces.OnWhiteBoardClickListener

class HospitalTariffsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalTariffsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHospitalTariffsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleBack()
        setupHospitalTariffsAdapter()
    }

    private fun setupHospitalTariffsAdapter() {
        var hospitalTariffsAdapter = HospitalTariffsAdapter(this)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.tariffsRv.adapter = hospitalTariffsAdapter
        binding.tariffsRv.layoutManager = linearLayoutManager
        hospitalTariffsAdapter.setupListener(object  : OnWhiteBoardClickListener{
            override fun onItemClick(boardId: String) {
                var intent = Intent(this@HospitalTariffsListActivity, HospitalTariffSingleListActivity::class.java)
                startActivity(intent)
            }

        })
    }

    private fun handleBack() {
        binding.backImg.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

}