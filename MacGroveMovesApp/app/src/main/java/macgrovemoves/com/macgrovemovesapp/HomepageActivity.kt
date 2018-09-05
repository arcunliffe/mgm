package macgrovemoves.com.macgrovemovesapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_homepage.*
import com.firebase.client.Firebase
import com.firebase.client.DataSnapshot
import com.firebase.client.ValueEventListener
import com.firebase.client.FirebaseError
import com.google.firebase.auth.FirebaseAuth

class HomepageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        Firebase.setAndroidContext(this)
        val deviceID = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        val user = FirebaseAuth.getInstance().currentUser
        val uid = ""
        if (user != null) {
            val uid = user.uid
            Toast.makeText(this, "uid " + uid, Toast.LENGTH_LONG).show()
        } else{
            Toast.makeText(this, "user is null", Toast.LENGTH_SHORT).show()
        }
        //TODO: Change input to uid
        val myFirebase = Firebase("https://macgrovemoves.firebaseio.com/Users/" + deviceID)

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

        /** Update number of trips from server */
        updateNTripsFromServer(myFirebase)

        /**
        /** Send number of trips to server on button click */
        button.setOnClickListener(View.OnClickListener {

            // walk
            val walkKey = myFirebase.child("walk")
            walkKey.setValue(parseTripText(text_walktrips.text.toString()))

            // run
            val runKey = myFirebase.child("run")
            runKey.setValue(parseTripText(text_runtrips.text.toString()))

            // bike
            val bikeKey = myFirebase.child("bike")
            bikeKey.setValue(parseTripText(text_biketrips.text.toString()))

            // transit
            val transitKey = myFirebase.child("transit")
            transitKey.setValue(parseTripText(text_transittrips.text.toString()))

            Toast.makeText(this, "Updating trip count", Toast.LENGTH_SHORT).show()

        })
        */

    }

    /**Parse trips string to get current number of trips */
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

        // update value on server
        if (nTrips != -1){
            val activityKey = myFirebase.child(activity_type)
            activityKey.setValue(nTrips.toString())
            Toast.makeText(this, "Updating trip count", Toast.LENGTH_SHORT).show()
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

        // update value on server
        if (updateServerVal) {
            // update value on server
            val activityKey = myFirebase.child(activity_type)
            activityKey.setValue(nTrips.toString())
            Toast.makeText(this, "Updating trip count", Toast.LENGTH_SHORT).show()
        }
    }

}


