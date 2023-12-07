package com.example.signingoogle.data

import android.net.Uri

data class UserData(
    var name: String? = null,
    var email: String? = null,
    var uId: String? = null,
    var photo: String? = null
) {
    constructor() : this(null, null, null, null)
}
