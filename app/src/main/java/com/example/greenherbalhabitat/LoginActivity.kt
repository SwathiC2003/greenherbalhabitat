package com.example.greenherbalhabitat


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.greenherbalhabitat.activity.UsersMainActivity
import com.example.greenherbalhabitat.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initialize Firebase Auth
        auth=FirebaseAuth.getInstance()



        binding.loginButton.setOnClickListener {
            val userName = binding.userName.text.toString()
            val password = binding.password.text.toString()

            if(userName.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()

            }else{
                auth.signInWithEmailAndPassword(userName,password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Logged in successfully",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, UsersMainActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this,"Login failed!: ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
    }
}