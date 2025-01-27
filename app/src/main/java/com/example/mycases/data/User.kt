package com.example.mycases.data

data class User(
    val balance: Double,
    val level: Int,
    val caseList: List<Pair<String, List<Int>>>
)
