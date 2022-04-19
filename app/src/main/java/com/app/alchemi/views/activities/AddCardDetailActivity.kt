package com.app.alchemi.views.activities

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.app.alchemi.R
import com.app.alchemi.roomdatabase.AppDatabase
import com.app.alchemi.roomdatabase.CardDetail
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.CreditCardUtils
import com.app.alchemi.utils.CreditCardUtils.CARD_NUMBER_FORMAT
import com.app.alchemi.utils.CreditCardUtils.CARD_NUMBER_FORMAT_AMEX
import com.app.alchemi.utils.CreditCardUtils.EXTRA_VALIDATE_EXPIRY_DATE
import com.app.alchemi.utils.ViewUtils
import kotlinx.android.synthetic.main.add_card_detail_layout.*
import kotlinx.android.synthetic.main.toolbar_layout_11.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.util.*


class AddCardDetailActivity: AppCompatActivity(){
    private var mValidateCard = true
    var selectedCardType="0"
    var listSize=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_card_detail_layout)
        databaseInit()
        if (intent!=null){
            selectedCardType= intent.getStringExtra(Constants.KEY_TITLE).toString()
            if (intent.getStringExtra(Constants.KEY_TITLE)=="0"){
                ivSelectedCard.setImageResource(R.drawable.ic_matercard)
                etCardNumber.hint = "5463 1235 7523 1005"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    etCardNumber.setAutofillHints("5463 1235 7523 1005")
                }
                // etCardNumber.autofillHints="5463 1235 7523 1005"
            }else{
                ivSelectedCard.setImageResource(R.drawable.visa_card)
                etCardNumber.hint = "4463 1235 7523 1005"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    etCardNumber.setAutofillHints("4463 1235 7523 1005")
                }
            }
          //  intent.getStringExtra(Constants.KEY_TITLE)
            intent.getStringExtra(Constants.KEY_DESCRIPTION)
        }
    }


    override fun onResume() {
        super.onResume()
        toolbar_title.text = getString(R.string.add_card_detail)
        ivBack.setOnClickListener {
            onBackPressed()
        }
        getSavedCardList()
        tvAddCard.setOnClickListener {
            getSavedCardList()
                validateView()

        }

        etCardHolderName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                scrollView.postDelayed({
                    val lastChild: View = scrollView.getChildAt(scrollView.childCount - 1)
                    val bottom: Int = lastChild.bottom + scrollView.paddingBottom
                    val sy: Int = scrollView.scrollY
                    val sh: Int = scrollView.height
                    val delta = bottom - (sy + sh)
                    scrollView.smoothScrollBy(0, delta)
                }, 50)
            }
        }

//        val rawCardNumber: String = cardNumber.replace(CreditCardUtils.SPACE_SEPERATOR, "")
//        val cardType: CreditCardUtils.CardType = CreditCardUtils.selectCardType(rawCardNumber)
     //   etCardNumber.addTextChangedListener(FourDigitCardFormatWatcher())

        /***
         *  format card number
         */
        etCardNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, arg1: Int, arg2: Int,
                arg3: Int
            ) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                val cursorPosition: Int = etCardNumber.selectionEnd
                val previousLength: Int = etCardNumber.text.length

                val cardNumber = CreditCardUtils.handleCardNumber(s.toString())
                val modifiedLength = cardNumber.length

                etCardNumber.removeTextChangedListener(this)
                etCardNumber.setText(cardNumber)
                val rawCardNumber = cardNumber.replace(CreditCardUtils.SPACE_SEPERATOR, "")
               val cardType: CreditCardUtils.CardType = CreditCardUtils.selectCardType(rawCardNumber)
                val maxLengthWithSpaces: Int =
                    (if (cardType === CreditCardUtils.CardType.AMEX_CARD) CARD_NUMBER_FORMAT_AMEX else CARD_NUMBER_FORMAT).length
                etCardNumber.setSelection(if (cardNumber.length > maxLengthWithSpaces) maxLengthWithSpaces else cardNumber.length)
                etCardNumber.addTextChangedListener(this)

                if (modifiedLength in (cursorPosition + 1)..previousLength) {
                    etCardNumber.setSelection(cursorPosition)
                }
               if(cardType== CreditCardUtils.CardType.UNKNOWN_CARD){
                   tvCardErrorMessage.visibility= View.VISIBLE
               }else if(selectedCardType=="0"&& cardType== CreditCardUtils.CardType.VISA_CARD){
                   tvCardErrorMessage.visibility= View.VISIBLE
               }else if(selectedCardType=="1"&& cardType== CreditCardUtils.CardType.MASTER_CARD){
                   tvCardErrorMessage.visibility= View.VISIBLE
               }
                else{
                   tvCardErrorMessage.visibility= View.GONE
               }

            }
        })

        /***
         *  Format card expiry date
         */



        etExpiryDate.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                var text = s.toString().replace(CreditCardUtils.SLASH_SEPERATOR, "")

                val month: String
                var year = ""
                if (text.length >= 2) {
                    month = text.substring(0, 2)
                    if (text.length > 2) {
                        year = text.substring(2)
                    }

                    if (mValidateCard) {
                        val mm = month.toInt()
                        if (mm <= 0 || mm >= 13) {
                            tvExpiryDateError.visibility= View.VISIBLE
                            tvExpiryDateError.text = getString(R.string.error_invalid_month)
                            return
                        }
                        if (text.length >= 4) {
                            val yy = year.toInt()
                            val calendar = Calendar.getInstance()
                            val currentYear = calendar[Calendar.YEAR]
                            val currentMonth = calendar[Calendar.MONTH] + 1
                            val millenium = currentYear / 1000 * 1000
                            if (yy + millenium < currentYear) {
                                tvExpiryDateError.visibility= View.VISIBLE
                                tvExpiryDateError.text = getString(R.string.error_card_expired)
                                return
                            } else if (yy + millenium == currentYear && mm < currentMonth) {
                                tvExpiryDateError.visibility= View.VISIBLE
                                tvExpiryDateError.text= getString(R.string.error_card_expired)
                                return
                            }else{
                                tvExpiryDateError.visibility= View.GONE
                            }
                        }
                    }
                } else {
                    month = text
                }

                val previousLength: Int = etExpiryDate.text.length
                val cursorPosition: Int = etExpiryDate.selectionEnd

                text = CreditCardUtils.handleExpiration(month, year)

                etExpiryDate.removeTextChangedListener(this)
                etExpiryDate.setText(text)
                etExpiryDate.setSelection(text.length)
                etExpiryDate.addTextChangedListener(this)

                val modifiedLength = text.length

                if (modifiedLength in (cursorPosition + 1)..previousLength) {
                    etExpiryDate.setSelection(cursorPosition)
                }

            }
        })


    }

    /**
     * validate View
     */
     fun validateView(){
        ViewUtils.hideKeyBoard(this)
        val cardNumber = etCardNumber.text.toString().trim()
        val cardExpiryDate = etExpiryDate.text.toString().trim()
        val cardCVV = etCvv.text.toString().trim()
        val cardHolderName = etCardHolderName.text.toString().trim()

        if (cardNumber.isEmpty()) {
            ViewUtils.showSnackBar(llLayout,getString(R.string.enter_your_card))
        } else if (cardNumber.length<19) {
            ViewUtils.showSnackBar(llLayout,getString(R.string.enter_valid_card_number))
        }else if(tvCardErrorMessage.visibility == View.VISIBLE){
            ViewUtils.showSnackBar(llLayout,getString(R.string.enter_valid_card_number))
        }

        else if (cardExpiryDate.isEmpty()) {
            ViewUtils.showSnackBar(llLayout,getString(R.string.enter_card_expiry_date))
        }else if(tvExpiryDateError.visibility == View.VISIBLE){
            ViewUtils.showSnackBar(llLayout,getString(R.string.kindly_check_your_card_expiry_date))
        }
        else if (cardCVV.isEmpty()) {
            ViewUtils.showSnackBar(llLayout,getString(R.string.enter_cvv_number))
        }
        else if (cardCVV.length<3) {
            ViewUtils.showSnackBar(llLayout,getString(R.string.enter_valid_cvv_number))
        }
        else if (cardHolderName.isEmpty()) {
            ViewUtils.showSnackBar(llLayout,getString(R.string.enter_card_holder_name))
        }
        else if (!ViewUtils.verifyAvailableNetwork(this)) {
            ViewUtils.showSnackBar(llLayout,getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            if (listSize<=5){
                insertCardDetail(CardDetail(0,
                    AlchemiApplication.alchemiApplication!!.getUUID(),
                    cardHolderName,
                    cardNumber,
                    cardExpiryDate,
                    selectedCardType))
                Handler(Looper.getMainLooper()).postDelayed({
                    Constants.SEKECTED_ACTIVITY=1
                    finish()

            },300)
            }else{
                Toast.makeText(this,getString(R.string.you_cannot_add_more_than_cards),Toast.LENGTH_LONG).show()
            }
            }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(EXTRA_VALIDATE_EXPIRY_DATE, mValidateCard)
        super.onSaveInstanceState(outState)
    }

    fun databaseInit(): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "cardDetails"
        ).build()

//        val cardDao = db.cardDao()
//        val cards: List<CardDetail> = cardDao.getAll()
    }
    fun insertCardDetail(cardDetail: CardDetail){
        GlobalScope.launch(Dispatchers.IO) {
            val db=  databaseInit()
            val cardDao = db.cardDao()
            cardDao.insertAll(cardDetail)
        }
        Toast.makeText(this@AddCardDetailActivity,getString(R.string.card_saved_successfully),Toast.LENGTH_LONG).show()
        EventBus.getDefault().post(Constants.KEY_REFRESH_LOCAL_DATA)

    }
     fun getSavedCardList() {

        GlobalScope.launch(Dispatchers.IO) {
            val db=  databaseInit()
            val cardDao = db.cardDao()
            val cards: List<CardDetail> = cardDao.getAll()
            listSize=cards.size
        }
    }



}