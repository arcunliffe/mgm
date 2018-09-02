package macgrovemoves.com.macgrovemovesapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_homepage.*

class HomepageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // add trips buttons
        p_walk.setOnClickListener { addTrips("walk") }
        p_run.setOnClickListener { addTrips("run") }
        p_bike.setOnClickListener { addTrips("bike") }
        p_transit.setOnClickListener { addTrips("transit") }

        // subtract trips buttons
        m_walk.setOnClickListener { subtractTrips("walk") }
        m_run.setOnClickListener { subtractTrips("run") }
        m_bike.setOnClickListener { subtractTrips("bike") }
        m_transit.setOnClickListener { subtractTrips("transit") }

    }

    /**Parse trips string to get current number of trips*/
    private fun parseTripText(text: String): Int {
        val separateWords = text.split(" ".toRegex())
        return separateWords[0].toInt()
    }

    /**Update text to add trips*/
    private fun addTrips(activity_type: String){
        // walk
        if (activity_type == "walk"){
            var nTrips = parseTripText(text_walktrips.text.toString())
            nTrips += 1
            var newTripsString = nTrips.toString() + " trip"
            if (nTrips != 1){
                newTripsString += "s"
            }
            text_walktrips.text = newTripsString
        // run
        } else if (activity_type == "run"){
            var nTrips = parseTripText(text_runtrips.text.toString())
            nTrips += 1
            var newTripsString = nTrips.toString() + " trip"
            if (nTrips != 1){
                newTripsString += "s"
            }
            text_runtrips.text = newTripsString
        // bike
        } else if (activity_type == "bike"){
            var nTrips = parseTripText(text_biketrips.text.toString())
            nTrips += 1
            var newTripsString = nTrips.toString() + " trip"
            if (nTrips != 1){
                newTripsString += "s"
            }
            text_biketrips.text = newTripsString
        // transit
        } else {
            var nTrips = parseTripText(text_transittrips.text.toString())
            nTrips += 1
            var newTripsString = nTrips.toString() + " trip"
            if (nTrips != 1){
                newTripsString += "s"
            }
            text_transittrips.text = newTripsString
        }
    }


    /**Update text to subtract trips*/
    private fun subtractTrips(activity_type: String){
        // walk
        if (activity_type == "walk"){
            var nTrips = parseTripText(text_walktrips.text.toString())
            nTrips = maxOf(nTrips - 1, 0)
            var newTripsString = nTrips.toString() + " trip"
            if (nTrips != 1){
                newTripsString += "s"
            }
            text_walktrips.text = newTripsString
            // run
        } else if (activity_type == "run"){
            var nTrips = parseTripText(text_runtrips.text.toString())
            nTrips = maxOf(nTrips - 1, 0)
            var newTripsString = nTrips.toString() + " trip"
            if (nTrips != 1){
                newTripsString += "s"
            }
            text_runtrips.text = newTripsString
            // bike
        } else if (activity_type == "bike"){
            var nTrips = parseTripText(text_biketrips.text.toString())
            nTrips = maxOf(nTrips - 1, 0)
            var newTripsString = nTrips.toString() + " trip"
            if (nTrips != 1){
                newTripsString += "s"
            }
            text_biketrips.text = newTripsString
            // transit
        } else {
            var nTrips = parseTripText(text_transittrips.text.toString())
            nTrips = maxOf(nTrips - 1, 0)
            var newTripsString = nTrips.toString() + " trip"
            if (nTrips != 1){
                newTripsString += "s"
            }
            text_transittrips.text = newTripsString
        }
    }
}
