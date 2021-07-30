package com.app.alchemi.models

import com.google.gson.annotations.SerializedName

data class SignUpModel (
    @SerializedName("success") val success : Boolean,
    @SerializedName("user") val user : UserSignUp,
    @SerializedName("error") val error : String,
    @SerializedName("token") val token : String,
    @SerializedName("message") val message: String
)
data class UserSignUp (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("email") val email : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("role") val role : String,
    @SerializedName("image") val image: String,
    @SerializedName("balance") val balance : Double,
    @SerializedName("otp") val otp : Int,
    @SerializedName("status") val status : Int,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("updated_at") val updated_at : String
)

