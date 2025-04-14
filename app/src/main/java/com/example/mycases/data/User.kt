package com.example.mycases.data

import kotlin.properties.Delegates

object User {
    val balance: Double by Delegates.observable(0.0) { _, _, newValue ->
        syncWithFirebase()
    }

    val level: Int by Delegates.observable(0) { _, _, newValue ->
        syncWithFirebase()
    }
//    var inventoryList: MutableList<Inventory>
//val caseList: List<Pair<String, List<Int>>>,  ...

    private fun syncWithFirebase() {

    }












}
