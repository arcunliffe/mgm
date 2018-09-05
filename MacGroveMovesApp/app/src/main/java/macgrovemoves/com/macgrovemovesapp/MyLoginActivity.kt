package macgrovemoves.com.macgrovemovesapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult

class MyLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mylogin)
        val myAuth = FirebaseAuth.getInstance()

        /** if user is already signed in, go directly to homepage */
        // TODO: recommendation is to put the below check in a splash screen
        if (myAuth.currentUser != null){
            goToHomepage()
        }

        /** button to register a new user */
        val registerButtonID = findViewById<Button>(R.id.register_button)
        registerButtonID.setOnClickListener(View.OnClickListener {
            goToRegistration()
        })

        /** button to sign in an existing user */
        val signInButtonID = findViewById<Button>(R.id.email_sign_in_button)
        val emailText = findViewById<TextView>(R.id.email)
        val passwordText = findViewById<TextView>(R.id.password)
        signInButtonID.setOnClickListener(View.OnClickListener {
            myAuth.signInWithEmailAndPassword(emailText.text.toString(), passwordText.text.toString())
                  .addOnCompleteListener(this, OnCompleteListener<AuthResult>{ task ->
                if (task.isSuccessful){
                    goToHomepage()
                } else{
                    Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show()
                }
            })
        })
    }

    /** Start homepage activity */
    private fun goToHomepage(){
        val homeIntent = Intent(this, HomepageActivity::class.java)
        startActivity(homeIntent)
        finish()
    }

    /** Start registration activity */
    private fun goToRegistration(){
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }
}
