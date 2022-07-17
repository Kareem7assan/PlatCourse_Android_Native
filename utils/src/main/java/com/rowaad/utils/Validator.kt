package com.rowaad.utils

object Validator {

    /**
     * method to check if String is Email.
     */
    fun isEmail(email: String): Boolean {
        val p = "[a-zA-Z0-9._\\-]+@[a-zA-Z0-9._\\-]+\\.+[a-z]+".toRegex()
        return email.matches(p)
    }

    /**
     * method to check if String is valid password.
     */
    fun isPassword(password: String): Boolean {
        return password.trim().length > 8
    }


    /**
     * method to check if String is valid confirm password.
     */
    fun isConfirmPassword(password: String,confirmPassword: String): Boolean {
        return password.trim() == confirmPassword.trim()
    }

    /**
     * method to check if String is Saudi Arabian Phone Number.
     */
    fun isPhone(phone: String): Boolean {
        return phone.trim().length == 9
    }

    /**
     * method to check if String is Number.
     */
    fun isNumeric(number: String): Boolean {
        val p = "^[0-9]+$".toRegex()
        return number.matches(p)
    }

    /**
     * method to check if user name is longer than 3 letters
     */
    fun isRealName(name: String): Boolean {
        return name.length > 2
    }
}