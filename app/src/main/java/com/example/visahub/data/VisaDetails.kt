package com.example.visahub.data

import java.io.Serializable
import java.util.*

data class VisaDetails(
    val about: String ?= null,
    val name: String ?= null,
    val other: String ?= null,
    val processingTime: String ?= null,
    val qualificaton: String ?= null,
    val type: String ?= null,
    val validity: String ?= null,
    val continent: String ?= null,
    val currency: String ?= null,
    val imageUrl: String ?= null
) : Serializable