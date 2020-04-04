package com.locked.shingranicommunity.members

data class ShingraniMember(
    val __v: Int,
    val _id: String,
    val app: String,
    val email: String,
    val state: String,
    var user: User?,
    var message: String,
    val token: String,
    var errorType: String ="",
    var errors: List<Error>?
){
    fun setError(error: String){
        message = error
    }
    fun hasError() : Boolean {
        return errorType != null || !errors?.isEmpty()!!
    }
}

data class User(
    val __v: Int,
    val _id: String,
    val name: String,
    val privilege: Privilege,
    val username: String
)
data class Privilege(
    val administrator: Boolean,
    val publicCreation: Boolean
)
data class SuccessMessage(
        val message: String,
        val user_id: String
)

data class ErrorMessage(
    val errorType: String,
    val errors: List<Error>,
    val message: String
)

data class Error(
    val code: Int,
    val message: String
)
data class LoginResponse(
        val message: String,
        val token: String,
        val user: User,
        val errorType: String,
        val errors: List<Error>
){
    fun hasError() : Boolean {
        return errorType != null || !errors.isEmpty()
}

}



