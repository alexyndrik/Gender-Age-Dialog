package com.example.myapplication.ui

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.Gender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val ageList = (16..30).map { it }

    private val userViewModel: UserViewModel by viewModels()
    private val allowedViewModel: AllowedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        lifecycleScope.launch {
            userViewModel.userPreferences.collect { prefs ->
                prefs.gender.let { savedGender ->
                    binding.maleBtn.isChecked = savedGender == Gender.MALE
                    binding.femaleBtn.isChecked = savedGender == Gender.FEMALE
                    binding.gender = savedGender?.value
                }

                prefs.age.let { savedAge ->
                    binding.ages.spinnerText.text = savedAge?.toString() ?: "--"
                    binding.age = savedAge
                }
            }
        }

        allowedViewModel.allowed.observe(this) {
            Toast.makeText(this, "allowed: $it", Toast.LENGTH_SHORT).show()
        }

        binding.maleBtn.setOnClickListener { userViewModel.updateGender(if (binding.maleBtn.isChecked) Gender.MALE else null) }
        binding.femaleBtn.setOnClickListener { userViewModel.updateGender(if (binding.femaleBtn.isChecked) Gender.FEMALE else null) }

        binding.ages.root.setOnClickListener { showAgePopup(binding.ages.root, binding.age) }

        binding.continueBtn.setOnClickListener {
            allowedViewModel.getAllowed(binding.gender!!, binding.age!!)
        }
    }

    private fun showAgePopup(anchor: View, selectedAge: Int?) {
        val popupView = LayoutInflater.from(this).inflate(R.layout.popup_age_list, null)
        val recyclerView = popupView.findViewById<RecyclerView>(R.id.recyclerView)

        val itemHeightInDp = 30
        val popupHeightInPx = ((itemHeightInDp * 6 + 16) * Resources.getSystem().displayMetrics.density).toInt()

        val popupWindow = PopupWindow(popupView, anchor.width, popupHeightInPx, true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = AgeAdapter(ageList, selectedAge) {
            userViewModel.updateAge(it)
            popupWindow.dismiss()
        }
        recyclerView.adapter = adapter

        val firstPosition = ageList.indexOf(selectedAge) - 2
        recyclerView.scrollToPosition(firstPosition)

        popupWindow.elevation = 10f
        popupWindow.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        popupWindow.isOutsideTouchable = true

        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        val anchorX = location[0]
        val anchorY = location[1]
        val dy = (8 * Resources.getSystem().displayMetrics.density).toInt()
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, anchorX, anchorY - dy)
    }

}