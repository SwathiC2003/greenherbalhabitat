package com.example.greenherbalhabitat

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

object Utils {
    private var dialog: AlertDialog?=null

    fun showToast(context: Context, message : String){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

}