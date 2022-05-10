package com.shop.navac.smspannel2.retrofit_models

data class SMS(
    var sender: String,
    var receiver: String,
    var text: String,
    var timeout: String,
    var save_time: String,
)
