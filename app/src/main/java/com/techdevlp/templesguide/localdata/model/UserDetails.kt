package com.techdevlp.templesguide.localdata.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserDetails(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("email_address")
    val emailAddress: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?,
    @SerializedName("profile_pic_url")
    val profilePicUrl: String
) : Serializable