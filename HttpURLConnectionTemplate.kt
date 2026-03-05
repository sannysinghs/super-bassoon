// ============================================
// DELETE REQUEST TEMPLATE
// ============================================
fun makeDeleteRequest(urlString: String): String {
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection

    try {
        connection.apply {
            requestMethod = "DELETE"
            connectTimeout = 10000
            readTimeout = 10000

            setRequestProperty("Accept", "application/json")
        }

        val responseCode = connection.responseCode

        return if (responseCode in 200..299) {
            "Success: $responseCode"
        } else {
            "Error $responseCode: ${connection.responseMessage}"
        }
    } finally {
        connection.disconnect()
    }
}

// ============================================
// ADVANCED: GET REQUEST WITH HEADERS & AUTH
// ============================================
fun makeAuthenticatedGetRequest(
    urlString: String,
    authToken: String,
    customHeaders: Map<String, String> = emptyMap()
): String {
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection

    try {
        connection.apply {
            requestMethod = "GET"
            connectTimeout = 15000
            readTimeout = 15000

            // Standard headers
            setRequestProperty("User-Agent", "MyApp/1.0")
            setRequestProperty("Accept", "application/json")

            // Authentication
            setRequestProperty("Authorization", "Bearer $authToken")

            // Custom headers
            customHeaders.forEach { (key, value) ->
                setRequestProperty(key, value)
            }
        }

        val responseCode = connection.responseCode

        return if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                reader.readText()
            }
        } else {
            "Error $responseCode: ${connection.responseMessage}"
        }
    } finally {
        connection.disconnect()
    }
}

// ============================================
// UTILITY: Form URL Encoded POST
// ============================================
fun makeFormPostRequest(urlString: String, formData: Map<String, String>): String {
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection

    try {
        connection.apply {
            requestMethod = "POST"
            connectTimeout = 10000
            readTimeout = 10000
            doOutput = true

            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        }

        // Encode form data
        val formBody = formData.entries.joinToString("&") { (key, value) ->
            "$key=${java.net.URLEncoder.encode(value, "UTF-8")}"
        }

        OutputStreamWriter(connection.outputStream).use { writer ->
            writer.write(formBody)
            writer.flush()
        }

        val responseCode = connection.responseCode

        return if (responseCode in 200..299) {
            BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                reader.readText()
            }
        } else {
            "Error $responseCode: ${connection.responseMessage}"
        }
    } finally {
        connection.disconnect()
    }
}

// ============================================
// EXECUTION ORDER EXPLANATION
// ============================================
/*
 * IMPORTANT: Understanding when the network call happens
 *
 * 1. url.openConnection()
 *    → Creates HttpURLConnection object
 *    → NO network call
 *
 * 2. connection.requestMethod = "GET"
 *    → Configures request method
 *    → NO network call
 *
 * 3. connection.setRequestProperty(...)
 *    → Sets headers
 *    → NO network call
 *
 * 4. connection.responseCode (or inputStream, or connect())
 *    → THIS triggers the actual HTTP request
 *    → Network call happens HERE
 *    → Server responds
 *    → Connection uses all previously configured settings
 *
 * This is why you can set timeouts and headers AFTER openConnection()
 * but BEFORE accessing responseCode or inputStream.
 */

// ============================================
// EXAMPLE USAGE
// ============================================
fun main() {
    // GET request
    val getResponse = makeGetRequest("https://api.example.com/users")
    println("GET Response: $getResponse")

    // POST request
    val jsonBody = """{"name": "John", "email": "john@example.com"}"""
    val postResponse = makePostRequest("https://api.example.com/users", jsonBody)
    println("POST Response: $postResponse")

    // Authenticated request
    val authResponse = makeAuthenticatedGetRequest(
        urlString = "https://api.example.com/protected",
        authToken = "your-token-here",
        customHeaders = mapOf("X-Custom-Header" to "value")
    )
    println("Auth Response: $authResponse")

    // Form POST
    val formResponse = makeFormPostRequest(
        urlString = "https://api.example.com/login",
        formData = mapOf("username" to "user", "password" to "pass")
    )
    println("Form Response: $formResponse")
}
