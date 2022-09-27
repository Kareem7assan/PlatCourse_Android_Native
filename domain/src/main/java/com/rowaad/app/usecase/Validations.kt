package com.rowaad.app.usecase

object Validations {
    fun isValidPhone(phone: String):Boolean = true.takeUnless { phone.isBlank() || phone.length < 9 || phone.contains(".")} ?: false
    fun isValidName(name: String):Boolean = true.takeUnless { name.isBlank() } ?: false
    fun isValidUserName(name: String):Boolean = true.takeIf { name.matches("^(?=.{3,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$".toRegex()) } ?: false
    fun isValidIban(iban: String):Boolean = true.takeUnless { iban.isBlank() || iban.length < 24} ?: false
    fun isValidTitle(title: String):Boolean = true.takeUnless { title.isBlank() } ?: false
    fun isValidBody(body: String):Boolean = true.takeUnless { body.length < 3 } ?: false
    fun isValidMail(email: String):Boolean = true.takeIf { email.matches("[a-zA-Z0-9._\\-]+@[a-zA-Z0-9._\\-]+\\.+[a-z]+".toRegex()) } ?: false
    fun isValidId(id: String):Boolean = true.takeIf { id.isNotEmpty() } ?: false
    fun isValidBirthDate(birthDate: String?=null):Boolean = true.takeUnless { birthDate.isNullOrBlank() } ?: false
    fun isValidNational(nationalId: Int?=null):Boolean = true.takeUnless { nationalId==null || nationalId==0 } ?: false
    fun isValidLocation(location: String?=null):Boolean = true.takeUnless { location.isNullOrBlank() } ?: false
    fun isValidPass(pass: String):Boolean = true.takeUnless { pass.isBlank() || pass.length < 8 } ?: false
    fun isValidCode(code: String):Boolean = true.takeUnless { code.isBlank() || code.length < 4 } ?: false
    fun isPassMatched(pass: String,confirmPass: String):Boolean = true.takeUnless { confirmPass.isBlank() || pass!=confirmPass } ?: false
}