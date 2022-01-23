package com.example.visahub.repository

import androidx.lifecycle.MutableLiveData
import com.example.visahub.data.VisaDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DatabaseRepository {

    private val mDatabase = FirebaseDatabase
        .getInstance("https://visa-hub-default-rtdb.asia-southeast1.firebasedatabase.app/")

    val visaDetailsByCountry : ArrayList<VisaDetails> = arrayListOf()
    val visaDetails =  MutableLiveData<ArrayList<VisaDetails>>()

    fun getListVisaData(country: String){
        val mRef = mDatabase.getReference("visaList/$country")
        mRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                visaDetailsByCountry.clear()
                for (snap in snapshot.children){
                    val detail  = snap.getValue(VisaDetails::class.java)
                    visaDetailsByCountry.add(detail!!)
                }
                visaDetails.value = visaDetailsByCountry
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    /*fun getVisaData(){
       mDatabase.child("visaList").child("india").get().addOnSuccessListener {
           Log.i("firebase", "Got value ${it.value}")
       }.addOnFailureListener{
           Log.e("firebase", "Error getting data", it)
       }
   }*/
}