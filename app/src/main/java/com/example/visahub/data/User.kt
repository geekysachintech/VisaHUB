package com.example.visahub.data

import java.io.Serializable

data class User(
    val uid: String?=null,
    val name: String?=null,
    val email: String?=null,
    val mobileNo: String?=null,
    var isAuthenticated: Boolean = false,
    var isNew: Boolean? = false,
    var isCreated: Boolean = false

) : Serializable {

    /*constructor() : this("","","", "") {

    }*/

}