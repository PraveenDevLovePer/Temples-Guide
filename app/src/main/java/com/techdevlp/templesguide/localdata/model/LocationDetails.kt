package com.techdevlp.templesguide.localdata.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocationDetails(
    @SerializedName("address")
    val address: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("zip_code")
    val zipCode: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("current_lat")
    val currentLat: Double?,
    @SerializedName("current_lng")
    val currentLng: Double?
) : Serializable