package macgrovemoves.com.macgrovemovesapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.firebase.client.Firebase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val myFirebaseAuth = FirebaseAuth.getInstance()

        val signUpButton = findViewById<Button>(R.id.r_sign_up_button)
        val emailText = findViewById<TextView>(R.id.r_email)
        val passwordText = findViewById<TextView>(R.id.r_password)

        signUpButton.setOnClickListener(View.OnClickListener {
            val userEmail = emailText.text.toString()
            val userPassword = passwordText.text.toString()

            if (!isPasswordValid(userPassword)){
                Toast.makeText(this, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show()
            } else if (!isEmailValid(userEmail)){
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            } else {
                createNewUser(userEmail, userPassword, myFirebaseAuth)
            }
        })

    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    /** Register a new user in Firebase authentication service */
    private fun createNewUser(email: String, password: String,
                           firebaseAuth: FirebaseAuth){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                        val homeIntent = Intent(this, HomepageActivity::class.java)
                        startActivity(homeIntent)
                    } else {
                        Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}
