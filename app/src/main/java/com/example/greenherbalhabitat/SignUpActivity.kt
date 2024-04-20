package com.example.greenherbalhabitat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.greenherbalhabitat.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private val binding:ActivitySignUpBinding by lazy{
        ActivitySignUpBinding.inflate(layoutInflater)

    }
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //initialize Firebase Auth
        auth=FirebaseAuth.getInstance()


        binding.signInButton.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        binding.registerButton.setOnClickListener {


            //get text from edit text field
            val email = binding.email.text.toString()
            val username = binding.UsernameSignup.text.toString()
            val password = binding.passwordSignup.text.toString()
            val repeatPassword = binding.repeatPassword.text.toString()

            //check if any feild is blank
            if (email.isEmpty()||username.isEmpty()||password.isEmpty()||repeatPassword.isEmpty()){

                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
            }else if (password!=repeatPassword){
                Toast.makeText(this,"Repeat password must be same",Toast.LENGTH_SHORT).show()
            }
            else{

                auth.createUserWithEmailAndPassword(email,password)

                    .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Registered Successfully",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,LoginActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this,"Registration Failed : ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }

                    }


            }

        }
    }
}