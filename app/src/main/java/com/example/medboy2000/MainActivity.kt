package com.example.medboy2000

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CallbackListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floating_action_button.setOnClickListener { showDialog()}
    }

    private fun showDialog() {
        val dialogFragment = ImportMedicationDialog(this)
        dialogFragment.show(supportFragmentManager, "signature")
    }

    override fun onDataReceived(data: String) {
        textView.text = data
    }
}