package com.example.greenherbalhabitat.viewmodels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greenherbalhabitat.models.Product
import com.example.greenherbalhabitat.roomdb.CartProductTable
import com.example.greenherbalhabitat.roomdb.CartProductsDao
import com.example.greenherbalhabitat.roomdb.CartProductsDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class UserViewModel(application: Application) : AndroidViewModel(application) {

    //initializations
    val sharedPreferences : SharedPreferences = application.getSharedPreferences("My_Pref" , MODE_PRIVATE )
    val cartProductDao : CartProductsDao = CartProductsDatabase.getDatabaseInstance(application).cartProductsDao()

    //Room DB
    suspend fun insertCartProduct(products: CartProductTable){
        cartProductDao.insertCartProduct(products)
    }

    fun getAll()  : LiveData<List<CartProductTable>> {
        return cartProductDao.getAllCartProducts()
    }

    suspend fun updateCartProduct(products: CartProductTable){
        cartProductDao.updateCartProduct(products)
    }

    suspend fun deleteCartProduct(productId : String){
        cartProductDao.deleteCartProduct(productId)
    }

    //Firebase call
    fun fetchAllTheProducts(): Flow<List<Product>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admin").child("AllProducts")

        val eventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()
                for (product in snapshot.children){
                    val prod = product.getValue(Product::class.java)
                    products.add(prod!!)



                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {
                //on handel
            }

        }

        db.addValueEventListener(eventListener)

        awaitClose{db.removeEventListener(eventListener)
        }
    }

    fun getCategoryProduct(category :String) : Flow<List<Product>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admin")
            .child("ProductCategory/${category}")

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()
                for (product in snapshot.children) {
                    val prod = product.getValue(Product::class.java)
                    products.add(prod!!)
                }
                trySend(products)

            }

            override fun onCancelled(error: DatabaseError) {
                //handle canceled
            }

        }
        db.addValueEventListener(eventListener)

        awaitClose {
            db.removeEventListener(eventListener)

        }
    }

    fun updateItemCount(product: Product , itemCount: Int){
        FirebaseDatabase.getInstance().getReference("Admin").child("AllProducts/${product.productRandomId}").child("itemCount").setValue(itemCount)
        FirebaseDatabase.getInstance().getReference("Admin").child("ProductCategory/${product.productCategory}/${product.productRandomId}").child("itemCount").setValue(itemCount)
        FirebaseDatabase.getInstance().getReference("Admin").child("ProductType/${product.productType}/${product.productRandomId}").child("itemCount").setValue(itemCount)
    }

    fun logOutUser(){
        FirebaseAuth.getInstance().signOut()
    }




    //sharePreferences
    fun savingCartItemCount(itemCount : Int){
        sharedPreferences.edit().putInt("itemCount", itemCount).apply()
    }
    fun fetchTotalCartItemCount() :MutableLiveData<Int>{
        val totalItemCount = MutableLiveData<Int>()
        totalItemCount.value=sharedPreferences.getInt("itemCount", 0)
        return totalItemCount

    }
}
