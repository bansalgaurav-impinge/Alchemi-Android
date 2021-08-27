package com.app.alchemi.models


data class LinkToken(
        val code: Int,
        val `data`: LintToken,
        val message: String
)

data class LintToken(
        val link_token: String
)
