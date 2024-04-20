package com.example.greenherbalhabitat

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.greenherbalhabitat.adapters.AdapterProduct
import com.example.greenherbalhabitat.databinding.FragmentSearchingBinding
import com.example.greenherbalhabitat.databinding.ItemViewProductBinding
import com.example.greenherbalhabitat.models.Product
import com.example.greenherbalhabitat.roomdb.CartProductTable
import com.example.greenherbalhabitat.viewmodels.UserViewModel
import kotlinx.coroutines.launch


class SearchingFragment : Fragment() {
    val viewModel: UserViewModel by viewModels()
    private lateinit var binding: FragmentSearchingBinding
    private lateinit var adapterProduct: AdapterProduct
    private var cartListener: CartListener?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchingBinding.inflate(layoutInflater)

        getAllTheProducts()
        backToHomeFragment()
        return binding.root
    }

    private fun backToHomeFragment() {
        binding.searchEt.setOnClickListener{
            findNavController().navigate(R.id.action_searchingFragment_to_homeFragment)

        }

    }

    private fun getAllTheProducts() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllTheProducts().collect(){
                if (it.isEmpty()) {
                    binding.rvProducts.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                } else {
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }
                adapterProduct = AdapterProduct(
                    ::onAddButtonClicked,
                    ::onIncrementButtonClicked,
                    ::onDecrementButtonClicked
                )

                binding.rvProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
                binding.shimmerViewContainer.visibility = View.GONE
            }
        }
    }
    private fun onAddButtonClicked(product: Product, productBinding: ItemViewProductBinding){
        productBinding.tvAdd.visibility=View.GONE

        productBinding.llProductCount.visibility=View.VISIBLE


        //step 1.
        var itemCount = productBinding.tvProductCount.text.toString().toInt()
        itemCount++
        productBinding.tvProductCount.text = itemCount.toString()

        cartListener?.showCartLayout(1)



        //step 2.
        product.itemCount = itemCount
        lifecycleScope.launch {
            cartListener?.savingCartItemCount(1)
            saveProductInRoomDb(product)
            viewModel.updateItemCount(product , itemCount)

        }

    }


    private fun onIncrementButtonClicked(product: Product, productBinding: ItemViewProductBinding){
        var itemCountInc = productBinding.tvProductCount.text.toString().toInt()
        itemCountInc++

        if (product.productStock!! + 1 > itemCountInc){
            productBinding.tvProductCount.text = itemCountInc.toString()

            cartListener?.showCartLayout(1)

            //step 2.
            product.itemCount = itemCountInc
            lifecycleScope.launch {
                cartListener?.savingCartItemCount(1)
                saveProductInRoomDb(product)
                viewModel.updateItemCount(product , itemCountInc)

            }

        }
        else{
            Utils.showToast(requireContext(),"Can't add more item of this")

        }


    }
    fun onDecrementButtonClicked(product: Product, productBinding: ItemViewProductBinding){
        var itemCountDec = productBinding.tvProductCount.text.toString().toInt()
        itemCountDec--

        product.itemCount = itemCountDec
        lifecycleScope.launch {
            cartListener?.savingCartItemCount(-1)
            saveProductInRoomDb(product)
            viewModel.updateItemCount(product , itemCountDec)

        }

        if (itemCountDec > 0){
            productBinding.tvProductCount.text = itemCountDec.toString()

        }
        else{
            lifecycleScope.launch { viewModel.deleteCartProduct(product.productRandomId!!) }
            Log.d("VV" , product.productRandomId!!)
            productBinding.tvAdd.visibility=View.VISIBLE
            productBinding.llProductCount.visibility=View.GONE
            productBinding.tvProductCount.text = "0"


        }
        cartListener?.showCartLayout(-1)

        //step 2.


    }

    private fun saveProductInRoomDb(product: Product) {

        val cartProduct = CartProductTable(
            productId = product.productRandomId!!,
            productTitle = product.productTitle,
            productQuantity = product.productQuantity.toString() + product.productUnit.toString(),
            productPrice = "₹" + "${product.productPrice}",
            productCount = product.itemCount,
            productStock = product.productStock,
            productImage = product.productImageUris?.get(0)!!,
            productCategory = product.productCategory,
            adminUid = product.adminUid

        )

        lifecycleScope.launch { viewModel.insertCartProduct(cartProduct) }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CartListener){
            cartListener = context
        }
        else{
            throw ClassCastException("Please Implement Cart Listener")
        }
    }

}