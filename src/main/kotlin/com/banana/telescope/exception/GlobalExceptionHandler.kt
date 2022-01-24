package com.banana.telescope.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException


@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException::class)
    protected fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            reason = e.reason!!,
            status = e.status.value(),
            error = e.status.name
        )
        return ResponseEntity<ErrorResponse>(response, e.status)
    }

    @ExceptionHandler(ServletRequestBindingException::class)
    protected fun handleServletRequestBindingException(e: ServletRequestBindingException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            reason = e.localizedMessage,
            status = 400,
            error = HttpStatus.BAD_REQUEST.name
        )
        return ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            reason = e.localizedMessage,
            status = 500,
            error = HttpStatus.INTERNAL_SERVER_ERROR.name
        )
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}