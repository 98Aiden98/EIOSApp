package Models
import java.time.LocalDateTime

class Token {
    var access_token: String? = null
    var token_type: String? = null
    var expires_in: Int = 0
    var refresh_token: String? = null
    private val auto_expires = 600 // Auto expire token
    private val create_date = LocalDateTime.now()

    fun isExpired(): Boolean {
        return LocalDateTime.now().second-create_date.second < auto_expires
    }
}