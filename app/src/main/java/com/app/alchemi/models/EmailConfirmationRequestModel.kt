package com.app.alchemi.models

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class EmailConfirmationRequestModel (
    @SerializedName("success") val success : Boolean,
    @SerializedName("user") val user : UserSignUp,
    @SerializedName("error") val error : String,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message: String,
    @Nullable
    val `data`: ReferralCode
)
data class ReferralCode(val referral_code:String)


