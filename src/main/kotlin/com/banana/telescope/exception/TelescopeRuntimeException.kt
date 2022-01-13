package com.banana.telescope.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException


sealed class TelescopeRuntimeException {
    class RemoteServerDownException :
        ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "all api server not available.")

    class InvalidParameterException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)

}