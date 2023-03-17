package com.example.android.chatapplication

const val BASE_URL = "https://chatty-chat-rawat.herokuapp.com/" // copy paste from Heroku Domain for online Dbs
// for offline Dbs (Local Host) const val BASE_URL = "http://10.0.2.2:3005/v1/"
const val URL_REGISTER = "${BASE_URL}account/register"
const val URL_LOGIN = "{BASE_URL}account/login"
const val URL_CREATE_USER = "{BASE_URL}user /add"

// Broadcast constants
const val BROADCASTCAST_USER_DATA_CAHNGE = "BROADCASTCAST_USER_DATA_CHANGE"