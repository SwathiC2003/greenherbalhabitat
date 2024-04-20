package com.example.greenherbalhabitat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.greenherbalhabitat.R
import com.example.greenherbalhabitat.adapters.AdapterCartProducts
import com.example.greenherbalhabitat.databinding.ActivityOrderPlaceBinding
import com.example.greenherbalhabitat.viewmodels.UserViewModel

class OrderPlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderPlaceBinding
    private val viewModel : UserViewModel by viewModels()
    private lateinit var adapterCartProducts: AdapterCartProducts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

       onPlaceOrderButtonClicked()

        backToUserMainActivity()

        getAllCartProducts()
    }

    private fun onPlaceOrderButtonClicked() {
        binding.btnNext.setOnClickListener{
            Toast.makeText(this,"Order Placed Successfully",Toast.LENGTH_SHORT).show()
        }
    }


    private fun backToUserMainActivity() {
        binding.tbOrderFragment.setNavigationOnClickListener {
            startActivity(Intent(this, UsersMainActivity::class.java))
            finish()
        }
    }

    private fun getAllCartProducts() {
        viewModel.getAll().observe(this){
            viewModel.getAll().observe(this){cartProductList->

                adapterCartProducts=AdapterCartProducts()
                binding.rvProductsItems.adapter=adapterCartProducts
                adapterCartProducts.differ.submitList(cartProductList)

                var totalPrice = 0

                for (products in cartProductList){
                    val price = products.productPrice?.substring(1)?.toInt()  //₹14
                    val itemCount = products.productCount!!
                    totalPrice += (price?.times(itemCount)!!)
                }

                binding.tvSubTotal.text= totalPrice.toString()

                if (totalPrice < 200){
                    binding.tvDeliveryCharge.text="₹15"
                    totalPrice += 15
                }

                binding.tvGrandTotal.text=totalPrice.toString()

            }
        }
    }


}