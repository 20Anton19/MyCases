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
//val caseList: List<Pair<String, List<Int>>>,

    private fun syncWithFirebase() {

    }











    /*
    может пускай так Kilowatt, 5,15,2,34,3 и так по position in case можем однозначно определить что 5 таких-то ножей выпало например

    блять так юзер это вообще не таблица это одна запись сука. Это в фаербейз сука таблица, а на телефоне мы тока одного юзера знаем


    пусть тогда это будет какой то синглтон мб я хз, мне нужны бля буквально 3 поля для всего

    бля рил даже не надо пользователя записывать в room, нахуя



    может не синглтон, а засунуть юзера в weaponViewModel, его все равно все видят

    ещё надо хранить id пользователя в shared reference
     */
}
