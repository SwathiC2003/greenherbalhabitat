package com.example.greenherbalhabitat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import androidx.activity.viewModels
import com.example.greenherbalhabitat.CartListener
import com.example.greenherbalhabitat.R
import com.example.greenherbalhabitat.adapters.AdapterCartProducts
import com.example.greenherbalhabitat.databinding.ActivityUsersMainBinding
import com.example.greenherbalhabitat.roomdb.CartProductTable
import com.example.greenherbalhabitat.viewmodels.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.greenherbalhabitat.databinding.BsCartProductsBinding



class UsersMainActivity : AppCompatActivity() , CartListener  {
    private lateinit var binding: ActivityUsersMainBinding
    private val viewModel : UserViewModel by viewModels()
    private lateinit var cartProductList: List<CartProductTable>
    private lateinit var adapterCartProducts: AdapterCartProducts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllCartProducts()

        getTotalItemCountInCart()

        onNextButtonClicked()

        onCartClicked()
    }

    private fun onNextButtonClicked() {
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, OrderPlaceActivity::class.java))
        }
    }

    private fun getAllCartProducts() {
        viewModel.getAll().observe(this){
            cartProductList = it
        }
    }

    private fun onCartClicked() {
        binding.llItemCart.setOnClickListener{
            val bsCartProductsBinding = BsCartProductsBinding.inflate(LayoutInflater.from(this))

            val bs = BottomSheetDialog(this)
            bs.setContentView(bsCartProductsBinding.root)

            bsCartProductsBinding.tvNumberOfProductCount.text=binding.tvNumberOfProductCount.text
            bsCartProductsBinding.btnNext.setOnClickListener {
                startActivity(Intent(this, OrderPlaceActivity::class.java))
            }
            adapterCartProducts=AdapterCartProducts()
            bsCartProductsBinding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartProductList)

            bs.show()

        }

    }

    private fun getTotalItemCountInCart() {
        viewModel.fetchTotalCartItemCount().observe(this){
            if (it > 0){
                binding.llCart.visibility=View.VISIBLE
                binding.tvNumberOfProductCount.text=it.toString()
            }
            else{
                binding.llCart.visibility=View.GONE

            }
        }

    }

    override fun showCartLayout(itemCount : Int) {
        val previousCount = binding.tvNumberOfProductCount.text.toString().toInt()
        val updatedCount = previousCount+itemCount

        if (updatedCount > 0){
            binding.llCart.visibility=View.VISIBLE
            binding.tvNumberOfProductCount.text=updatedCount.toString()

        }
        else {
            binding.llCart.visibility=View.GONE
            binding.tvNumberOfProductCount.text="0"

        }

    }

    override fun savingCartItemCount(itemCount: Int) {
        viewModel.fetchTotalCartItemCount().observe(this){
            viewModel.savingCartItemCount(it+itemCount)
        }

    }
}