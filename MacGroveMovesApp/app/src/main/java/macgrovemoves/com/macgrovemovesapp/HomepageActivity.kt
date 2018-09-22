package macgrovemoves.com.macgrovemovesapp

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_homepage.*
import com.firebase.client.Firebase
import com.firebase.client.DataSnapshot
import com.firebase.client.ValueEventListener
import com.firebase.client.FirebaseError
import com.google.firebase.auth.FirebaseAuth

class HomepageActivity : AppCompatActivity() {

    /** main */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        Firebase.setAndroidContext(this)
        // val deviceID = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val uid:String = user.uid
            val myFirebase = Firebase("https://macgrovemoves.firebaseio.com/Users/" + uid)

            // add trips buttons
            p_walk.setOnClickListener { addTrips("walk", myFirebase) }
            p_run.setOnClickListener { addTrips("run", myFirebase) }
            p_bike.setOnClickListener { addTrips("bike", myFirebase) }
            p_transit.setOnClickListener { addTrips("transit", myFirebase) }

            // subtract trips buttons
            m_walk.setOnClickListener { subtractTrips("walk", myFirebase) }
            m_run.setOnClickListener { subtractTrips("run", myFirebase) }
            m_bike.setOnClickListener { subtractTrips("bike", myFirebase) }
            m_transit.setOnClickListener { subtractTrips("transit", myFirebase) }

            // Update number of trips from server
            updateNTripsFromServer(myFirebase)

        } else{
            Toast.makeText(this, "user is null", Toast.LENGTH_SHORT).show()
        }


    }

    /** Create menu */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_homepage, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /** Menu options */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.logout -> {
                val myAuth = FirebaseAuth.getInstance()
                myAuth.signOut()
                val intent = Intent(this, MyLoginActivity::class.java)
                startActivity(intent)
            }
            R.id.about -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://macgrove.org/what-we-do/mgm/"))
                startActivity(browserIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** Parse trips string to get current number of trips */
    private fun parseTripText(text: String): Int {
        val separateWords = text.split(" ".toRegex())
        return separateWords[0].toInt()
    }

    /** Make trip text */
    private fun makeTripText(nTrips: String): String {
        var newTripsString = nTrips + " trip"
        if (nTrips != "1"){
            newTripsString += "s"
        }
        return newTripsString
    }

    private fun countTotalTrips(): Int{
        val totalTrips = parseTripText(text_walktrips.text.toString()) + parseTripText(text_runtrips.text.toString()) + parseTripText(text_transittrips.text.toString()) + parseTripText(text_biketrips.text.toString())
        return totalTrips
    }

    /** Make cumulative trips text */
    private fun makeCumulativeTripText() {
        val totalTrips = countTotalTrips()
        if (totalTrips < 15) {
            val remainingTrips = 15 - totalTrips
            cum_trips.text = "Trips until next raffle entry: "  + remainingTrips.toString()
        }
        else if (totalTrips < 30){
            val remainingTrips = 30 - totalTrips
            cum_trips.text = "Trips until next raffle entry: "  + remainingTrips.toString()
        }
        else if (totalTrips < 45) {
            val remainingTrips = 45 - totalTrips
            cum_trips.text = "Trips until next raffle entry: "  + remainingTrips.toString()
        }
        else {
            cum_trips.text = "Challenge completed. Congratulations!"
        }
        updateMedal(totalTrips)
    }

    /** Update medal */
    private fun updateMedal(totalTrips: Int){
        val goldImage = findViewById<ImageView>(R.id.gold)
        val silverImage = findViewById<ImageView>(R.id.silver)
        val bronzeImage = findViewById<ImageView>(R.id.bronze)
        if (totalTrips >= 45){
            // all medals
            goldImage.setImageResource(R.drawable.gold_64_t)
            silverImage.setImageResource(R.drawable.silver_64_t)
            bronzeImage.setImageResource(R.drawable.bronze_64_t)
        }
        else if (totalTrips >= 30){
            // silver & bronze medals
            goldImage.setImageResource(R.drawable.gray_64)
            silverImage.setImageResource(R.drawable.silver_64_t)
            bronzeImage.setImageResource(R.drawable.bronze_64_t)
        }
        else if (totalTrips >= 15){
            // bronze medal
            goldImage.setImageResource(R.drawable.gray_64)
            silverImage.setImageResource(R.drawable.gray_64)
            bronzeImage.setImageResource(R.drawable.bronze_64_t)
        }
        else {
            // no medals
            goldImage.setImageResource(R.drawable.gray_64)
            silverImage.setImageResource(R.drawable.gray_64)
            bronzeImage.setImageResource(R.drawable.gray_64)
        }

    }

    /** Pop-up to submit raffle entry */

    /** Get number of trips from server and update on app */
    private fun updateNTripsFromServer(myFirebase: Firebase){
        myFirebase.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val map = dataSnapshot.value
                if (map is Map<*, *>){
                    text_walktrips.text = makeTripText(map["walk"].toString())
                    text_runtrips.text = makeTripText(map["run"].toString())
                    text_biketrips.text = makeTripText(map["bike"].toString())
                    text_transittrips.text = makeTripText(map["transit"].toString())

                    // display total trips left
                    makeCumulativeTripText()

                    // set up medal image
                    val totalTrips = countTotalTrips()
                    updateMedal(totalTrips)
                }
            }

            override fun onCancelled(firebaseError: FirebaseError){
                // do nothing
            }
        })
    }

    /** Update app text to add trips */
    private fun addTrips(activity_type: String, myFirebase: Firebase){

        var nTrips = -1

        // walk
        if (activity_type == "walk"){
            nTrips = parseTripText(text_walktrips.text.toString())
            nTrips += 1
            text_walktrips.text = makeTripText(nTrips.toString())

        // run
        } else if (activity_type == "run"){
            nTrips = parseTripText(text_runtrips.text.toString())
            nTrips += 1
            text_runtrips.text = makeTripText(nTrips.toString())

        // bike
        } else if (activity_type == "bike"){
            nTrips = parseTripText(text_biketrips.text.toString())
            nTrips += 1
            text_biketrips.text = makeTripText(nTrips.toString())

        // transit
        } else {
            nTrips = parseTripText(text_transittrips.text.toString())
            nTrips += 1
            text_transittrips.text = makeTripText(nTrips.toString())

        }

        // total
        makeCumulativeTripText()

        // update value on server
        if (nTrips != -1){
            val activityKey = myFirebase.child(activity_type)
            activityKey.setValue(nTrips.toString())
            // Toast.makeText(this, "Updating trip count", Toast.LENGTH_SHORT).show()
        }
    }

    /**Update app text to subtract trips */
    private fun subtractTrips(activity_type: String, myFirebase: Firebase){
        var updateServerVal = false
        var nTrips = -1

        // walk
        if (activity_type == "walk"){
            nTrips = parseTripText(text_walktrips.text.toString())

            // only do something if nTrips isn't already 0
            if (nTrips != 0) {
                nTrips -= 1
                text_walktrips.text = makeTripText(nTrips.toString())
                updateServerVal = true
            }

            // run
        } else if (activity_type == "run"){
            nTrips = parseTripText(text_runtrips.text.toString())

            // only do something if nTrips isn't already 0
            if (nTrips != 0){
                nTrips -= 1
                text_runtrips.text = makeTripText(nTrips.toString())
                updateServerVal = true
            }
            // bike
        } else if (activity_type == "bike"){
            nTrips = parseTripText(text_biketrips.text.toString())

            // only do something if nTrips isn't already 0
            if (nTrips != 0){
                nTrips -= 1
                text_biketrips.text = makeTripText(nTrips.toString())
                updateServerVal = true
            }
            // transit
        } else {
            nTrips = parseTripText(text_transittrips.text.toString())

            // only do something if nTrips isn't already 0
            if (nTrips != 0){
                nTrips -= 1
                text_transittrips.text = makeTripText(nTrips.toString())
                updateServerVal = true
            }
        }

        // total
        makeCumulativeTripText()

        // update value on server
        if (updateServerVal) {
            // update value on server
            val activityKey = myFirebase.child(activity_type)
            activityKey.setValue(nTrips.toString())
            // Toast.makeText(this, "Updating trip count", Toast.LENGTH_SHORT).show()
        }
    }

}


