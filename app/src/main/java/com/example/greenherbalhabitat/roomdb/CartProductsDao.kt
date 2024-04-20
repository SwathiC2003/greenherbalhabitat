package com.example.greenherbalhabitat.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartProduct(products: CartProductTable)

    @Update
    fun updateCartProduct(products: CartProductTable)

    @Query("SELECT * FROM CartProductTable")
    fun getAllCartProducts() : LiveData<List<CartProductTable>>


    @Query("DELETE FROM CartProductTable WHERE productId = :productId ")
    fun deleteCartProduct(productId : String)

}