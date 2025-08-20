package com.iprism.ecmhealthadvisor.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.iprism.ecmcorporatemarketing.interfaces.OnDoctorItemClickListener
import com.iprism.ecmhealthadvisor.adapters.MyTasksAdapter
import com.iprism.ecmhealthadvisor.databinding.ActivityMyTasksBinding

class MyTasksActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMyTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleBack()
        setupMyTasksAdapter()
    }

    private fun setupMyTasksAdapter() {
        var myTasksAdapter = MyTasksAdapter(this)
        var linearLayoutManager = GridLayoutManager(this, 4)
        binding.myTasksRv.adapter = myTasksAdapter
        binding.myTasksRv.layoutManager = linearLayoutManager
        myTasksAdapter.setupListener(object  : OnDoctorItemClickListener{
            override fun onItemClick(categoryId: String, doctorId: String) {
                var intent = Intent(this@MyTasksActivity, MyTaskProgressActivity::class.java)
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