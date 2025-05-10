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
import com.example.myapplication.model.Gender
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.SpinnerDropdownItemBindingImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val ageList = ArrayList<String>((16..30).map { it.toString() })

    private val userViewModel: UserViewModel by viewModels()
    private val allowedViewModel: AllowedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initGenderButtons()
        initAgesSpinner()

        lifecycleScope.launch {
            userViewModel.userPreferences.collect { prefs ->
                prefs.gender.let { savedGender ->
                    binding.maleBtn.isChecked = savedGender == Gender.MALE
                    binding.femaleBtn.isChecked = savedGender == Gender.FEMALE
                    binding.gender = savedGender?.value
                }

                prefs.age.let { savedAge ->
                    val index = ageList.indexOfFirst { it == savedAge.toString() }
                    if (index >= 0) binding.ages.setSelection(index)
                    binding.age = savedAge
                }
            }
        }

        allowedViewModel.allowed.observe(this) {
            Toast.makeText(this, "allowed: $it", Toast.LENGTH_SHORT).show()
        }

        binding.continueBtn.setOnClickListener {
            allowedViewModel.getAllowed(binding.gender!!, binding.age!!)
        }
    }

    private fun initGenderButtons() {
        binding.maleBtn.setOnClickListener { userViewModel.updateGender(if (binding.maleBtn.isChecked) Gender.MALE else null) }
        binding.femaleBtn.setOnClickListener { userViewModel.updateGender(if (binding.femaleBtn.isChecked) Gender.FEMALE else null) }
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
                    userViewModel.updateAge(if (position == 0) null else ageList[position].toInt())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
            gravity = Gravity.CENTER
        }
    }

}