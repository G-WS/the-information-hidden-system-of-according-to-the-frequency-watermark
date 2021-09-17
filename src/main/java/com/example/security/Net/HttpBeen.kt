package com.example.security.Net

import okhttp3.Response
import java.lang.Exception

data class HttpBeen(val response: Response?, val exception: String) {
    override fun toString(): String {
        return "HttpBeen[response$response  exception $exception]"
    }
}
