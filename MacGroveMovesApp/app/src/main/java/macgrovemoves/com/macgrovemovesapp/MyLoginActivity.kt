package macgrovemoves.com.macgrovemovesapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View

class MyLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mylogin)

        val registerButtonID = findViewById<Button>(R.id.register_button)
        registerButtonID.setOnClickListener(View.OnClickListener {
            goToRegistration()
            // goToHomepage()
        })
    }

    /** Start homepage activity */
    private fun goToHomepage(){
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
    }

    /** Start registration activity */
    private fun goToRegistration(){
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }
}
