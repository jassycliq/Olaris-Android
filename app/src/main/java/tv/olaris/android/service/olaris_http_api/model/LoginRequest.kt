package tv.olaris.android.service.olaris_http_api.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)
