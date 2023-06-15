package com.mad43.staylistaadmin.utils

sealed class ValidateState{
    class OnValidateSuccess(val message: Int) : ValidateState()
    class OnValidateError(val message: Int ,val place : String) : ValidateState()
    object BeforeValidation : ValidateState()
}
