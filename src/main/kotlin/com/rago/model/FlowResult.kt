package com.rago.model

data class FlowResult<T>(var data: T? = null, var error: String? = null, var code: Int = 0)