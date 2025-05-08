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
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.model.Age
import com.example.myapplication.data.model.Gender
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.SpinnerDropdownItemBindingImpl
import com.example.myapplication.network.SocketManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val SERVER_ADDRESS = "challenge.ciliz.com"
private const val SERVER_PORT = 2222

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val socketManager = SocketManager(SERVER_ADDRESS, SERVER_PORT)
    private lateinit var binding: ActivityMainBinding

    private val ageList = ArrayList<String>((16..30).map { it.toString() })

    private val genderViewModel: GenderViewModel by viewModels()
    private val ageViewModel: AgeViewModel by viewModels()
    private val allowedViewModel: AllowedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initGenderButtons()
        initAgesSpinner()

        allowedViewModel.allowed.observe(this) {
            Toast.makeText(this, "allowed: $it", Toast.LENGTH_SHORT).show()
        }

        binding.continueBtn.setOnClickListener {
            allowedViewModel.getAllowed(socketManager, binding.gender!!, binding.age!!)
        }
    }

    private fun initGenderButtons() {
        lifecycleScope.launch {
            genderViewModel.gender.collect { gender ->
                binding.maleBtn.isChecked = gender == Gender.MALE
                binding.femaleBtn.isChecked = gender == Gender.FEMALE
                binding.gender = gender?.value
            }
        }

        binding.maleBtn.setOnClickListener { genderViewModel.selectGender(if (binding.maleBtn.isChecked) Gender.MALE else null) }
        binding.femaleBtn.setOnClickListener { genderViewModel.selectGender(if (binding.femaleBtn.isChecked) Gender.FEMALE else null) }
    }

    private fun initAgesSpinner() {
        ageList.add(0, "--")

        val aa = object : ArrayAdapter<String>(this, R.layout.spinner_selected_item, R.id.spinner_text, ageList) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = SpinnerDropdownItemBindingImpl.inflate(LayoutInflater.from(context))
                view.dropdownText.text = getItem(position).toString()
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
                    ageViewModel.selectAge(if (position == 0) null else Age(ageList[position].toInt()))
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
            gravity = Gravity.CENTER
        }

        lifecycleScope.launch {
            ageViewModel.age.collect { savedAge ->
                savedAge?.let {
                    val index = ageList.indexOfFirst { it == savedAge.value.toString() }
                    if (index >= 0) binding.ages.setSelection(index)
                }
                binding.age = savedAge?.value
            }
        }
    }

}