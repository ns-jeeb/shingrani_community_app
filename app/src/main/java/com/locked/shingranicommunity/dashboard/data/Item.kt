package com.locked.shingranicommunity.dashboard.data

data class Item(
    var id: String? = null,
    var updatedAt: String? = null,
    var createdAt: String? = null,
    var owner: String? = null,
    var creator: String? = null,
    var app: String? = null,
    var template: String? = null,
    var v: String? = null,
    var fields: List<Field>? = null,
    var icon: Icon? = null,
    var colorDarker: String? = null,
    var colorOriginal: String? = null
){
}

data class Field(

    var name: String? = null,
    var value: String? = null
    ) {
}

data class Icon( var url: String? = null) {



}


class SingleTone private constructor() {
    private var item: Item? = null

    companion object {
        private var instance: SingleTone? = null

        fun getInstance(): SingleTone {
            if (instance == null) {
                instance = SingleTone()
            }
            return instance as SingleTone
        }

    }
    fun setItem(itm:Item){
        this.item = itm
    }

}
