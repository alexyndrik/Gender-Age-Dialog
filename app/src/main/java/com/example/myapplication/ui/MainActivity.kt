package com.example.myapplication.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.SpinnerDropdownItemBindingImpl
import com.example.myapplication.network.SocketManager
import dagger.hilt.android.AndroidEntryPoint

private const val SERVER_ADDRESS = "challenge.ciliz.com"
private const val SERVER_PORT = 2222

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val socketManager = SocketManager(SERVER_ADDRESS, SERVER_PORT)
    private lateinit var binding: ActivityMainBinding

    private val genderViewModel: GenderViewModel by viewModels()
    private val ageViewModel: AgeViewModel by viewModels()
    private val allowedViewModel: AllowedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initGenderButtons()
        initAgesSpinner()

        genderViewModel.gender.observe(this) { binding.gender = it }
        ageViewModel.age.observe(this) { binding.age = it}
        allowedViewModel.allowed.observe(this) {
            Toast.makeText(this, "allowed: $it", Toast.LENGTH_SHORT).show()
        }

        binding.continueBtn.setOnClickListener {
            allowedViewModel.getAllowed(socketManager, binding.gender!!, binding.age!!)
        }
    }

    private fun initGenderButtons() {
        binding.maleBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.femaleBtn.isChecked = false
                genderViewModel.setGender("m")
            } else genderViewModel.setGender(null)
        }

        binding.femaleBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.maleBtn.isChecked = false
                genderViewModel.setGender("f")
            } else genderViewModel.setGender(null)
        }
    }

    private fun initAgesSpinner() {
        val ages = ArrayList<String>()
        ages.add("--")
        for (i in 16..30) ages.add(i.toString())
        val aa = object : ArrayAdapter<String>(this, R.layout.spinner_selected_item, R.id.spinner_text, ages) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = SpinnerDropdownItemBindingImpl.inflate(LayoutInflater.from(context))
                view.dropdownText.text = getItem(position)
                view.isSelected = binding.ages.selectedItemPosition == position
                view.isFirst = position == 0
                return view.root
            }
        }
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item)

        with(binding.ages) {
            adapter = aa
            setSelection(0, false)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    ageViewModel.setAge(if (position == 0) null else ages[position].toInt())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
            gravity = Gravity.CENTER
        }
    }

}