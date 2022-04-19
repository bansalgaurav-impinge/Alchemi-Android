package com.app.alchemi.utils

import java.util.*
import java.util.regex.Pattern

object CreditCardUtils {
    // private static final String PATTERN_AMEX = "^3(4|7)[0-9 ]*";
    private const val PATTERN_VISA = "^4[0-9 ]*"
    private const val PATTERN_MASTER = "^5[0-9 ]*"

    // private static final String PATTERN_DISCOVER = "^6[0-9 ]*";
    const val MAX_LENGTH_CARD_NUMBER = 16
    const val MAX_LENGTH_CARD_NUMBER_AMEX = 15
    const val CARD_NUMBER_FORMAT = "XXXX XXXX XXXX XXXX"
    const val CARD_NUMBER_FORMAT_AMEX = "XXXX XXXXXX XXXXX"
    const val EXTRA_CARD_NUMBER = "card_number"
    const val EXTRA_CARD_CVV = "card_cvv"
    const val EXTRA_CARD_EXPIRY = "card_expiry"
    const val EXTRA_CARD_HOLDER_NAME = "card_holder_name"
    const val EXTRA_CARD_SHOW_CARD_SIDE = "card_side"
    const val EXTRA_VALIDATE_EXPIRY_DATE = "expiry_date"
    const val EXTRA_ENTRY_START_PAGE = "start_page"
    const val CARD_SIDE_FRONT = 1
    const val CARD_SIDE_BACK = 0
    const val CARD_NUMBER_PAGE = 0
    const val CARD_EXPIRY_PAGE = 1
    const val CARD_CVV_PAGE = 2
    const val CARD_NAME_PAGE = 3
    const val SPACE_SEPERATOR = " "
    const val SLASH_SEPERATOR = "/"
    const val CHAR_X = 'X'
    fun selectCardType(cardNumber: String?): CardType {
        var pCardType = Pattern.compile(PATTERN_VISA)
        if (pCardType.matcher(cardNumber).matches()) return CardType.VISA_CARD
        pCardType = Pattern.compile(PATTERN_MASTER)
        return if (pCardType.matcher(cardNumber)
                .matches()
        ) CardType.MASTER_CARD else CardType.UNKNOWN_CARD
        //        pCardType = Pattern.compile(PATTERN_AMEX);
//        if(pCardType.matcher(cardNumber).matches())
//            return CardType.AMEX_CARD;
//        pCardType = Pattern.compile(PATTERN_DISCOVER);
//        if(pCardType.matcher(cardNumber).matches())
//            return CardType.DISCOVER_CARD;
    }

    fun selectCardLength(cardType: CardType): Int {
        return if (cardType == CardType.AMEX_CARD) MAX_LENGTH_CARD_NUMBER_AMEX else MAX_LENGTH_CARD_NUMBER
    }

    @JvmOverloads
    fun handleCardNumber(inputCardNumber: String, seperator: String? = SPACE_SEPERATOR): String {
        val unformattedText = inputCardNumber.replace(seperator!!, "")
        val cardType = selectCardType(inputCardNumber)
        val format =
            if (cardType == CardType.AMEX_CARD) CARD_NUMBER_FORMAT_AMEX else CARD_NUMBER_FORMAT
        val sbFormattedNumber = StringBuilder()
        var iIdx = 0
        var jIdx = 0
        while (iIdx < format.length && unformattedText.length > jIdx) {
            if (format[iIdx] == CHAR_X) sbFormattedNumber.append(unformattedText[jIdx++]) else sbFormattedNumber.append(
                format[iIdx]
            )
            iIdx++
        }
        return sbFormattedNumber.toString()
    }

    fun formatCardNumber(inputCardNumber: String, seperator: String?): String {
        val unformattedText = inputCardNumber.replace(seperator!!, "")
        val cardType = selectCardType(inputCardNumber)
        val format =
            if (cardType == CardType.AMEX_CARD) CARD_NUMBER_FORMAT_AMEX else CARD_NUMBER_FORMAT
        val sbFormattedNumber = StringBuilder()
        var iIdx = 0
        var jIdx = 0
        while (iIdx < format.length) {
            if (format[iIdx] == CHAR_X && unformattedText.length > jIdx) sbFormattedNumber.append(
                unformattedText[jIdx++]
            ) else sbFormattedNumber.append(format[iIdx])
            iIdx++
        }
        return sbFormattedNumber.toString()
            .replace(SPACE_SEPERATOR, SPACE_SEPERATOR + SPACE_SEPERATOR)
    }

    fun handleExpiration(month: String, year: String): String {
        return handleExpiration(month + year)
    }

    fun handleExpiration(dateYear: String): String {
        val expiryString = dateYear.replace(SLASH_SEPERATOR, "")
        var text: String
        if (expiryString.length >= 2) {
            var mm = expiryString.substring(0, 2)
            var yy: String
            text = mm
            try {
                if (mm.toInt() > 12) {
                    mm = "12" // Cannot be more than 12.
                }
            } catch (e: Exception) {
                mm = "01"
            }
            if (expiryString.length >= 4) {
                yy = expiryString.substring(2, 4)
                try {
                    yy.toInt()
                } catch (e: Exception) {
                    val calendar = Calendar.getInstance()
                    val year = calendar[Calendar.YEAR]
                    yy = year.toString().substring(2)
                }
                text = mm + SLASH_SEPERATOR + yy
            } else if (expiryString.length > 2) {
                yy = expiryString.substring(2)
                text = mm + SLASH_SEPERATOR + yy
            }
        } else {
            text = expiryString
        }
        return text
    }

    enum class CardType {
        UNKNOWN_CARD, AMEX_CARD, MASTER_CARD, VISA_CARD, DISCOVER_CARD
    }
}