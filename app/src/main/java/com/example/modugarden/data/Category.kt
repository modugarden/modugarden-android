package com.example.modugarden.data

import com.example.modugarden.R

enum class Category(
    val category: String,
    val image: Int
) {
    GARDENING("식물 가꾸기", R.drawable.ic_potted_plant),
    PLANTERIOR("플랜테리어", R.drawable.ic_house_with_garden),
    TRIP("여행/나들이", R.drawable.ic_tent)
}