package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.DropdownItemBinding

class AgeAdapter(
    private val ages: List<Int>,
    private val selectedAge: Int?,
    private val onAgeSelected: (Int?) -> Unit
) : RecyclerView.Adapter<AgeAdapter.AgeViewHolder>() {

    inner class AgeViewHolder(val binding: DropdownItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeViewHolder {
        val binding = DropdownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AgeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AgeViewHolder, position: Int) {
        val age = ages[position]
        holder.binding.dropdownText.text = age.toString()
        holder.binding.isSelected = age == selectedAge
        holder.binding.isFirst = position == 0

        holder.binding.root.setOnClickListener {
            onAgeSelected(age)
        }
    }

    override fun getItemCount() = ages.size
}
