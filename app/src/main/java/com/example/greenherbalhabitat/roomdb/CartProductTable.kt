package com.example.greenherbalhabitat.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartProductTable")
data class CartProductTable(
    @PrimaryKey
    val productId : String = "random",
    val productTitle : String ? = null,
    val productQuantity : String?=null,
    val productPrice : String ? = null,
    val productCount : Int ? = null,
    val productStock : Int ? = null,
    val productImage : String ? = null,
    val productCategory : String ? = null,
    val adminUid : String ? = null,

    )