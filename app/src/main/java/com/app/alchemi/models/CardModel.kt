package com.app.alchemi.models

import java.math.BigDecimal

data class CardModel(
    val code: Int,
    val `data`: CardData,
    val message: String,
    val valid: Boolean
)
data class Card(
    val aco_stake_rewards: String,
    val airport_lounge: Boolean,
    val annual_fee: BigDecimal,
    val card_benefit_with_out_stake: CardBenefitWithOutStake,
    val card_benefit_with_stake: CardBenefitWithStake,
    val card_cashback: Any,
    val card_image: String,
    val aco_token_purchase: String,
    val contactless_payment: Boolean,
    val delivery_fee: BigDecimal,
    val details: String,
    val id: Int,
    val is_pay_anywhere: Boolean,
    val is_user_active: Boolean,
    val limits_detail: String,
    val limits_fee: BigDecimal,
    val material: String,
    val monthly_fee: BigDecimal,
    val name: String,
    val stake_detail: String,
    val staking_status: Boolean,
    val terms_and_conditions: String
)
data class CardBenefitWithOutStake(
    val card_cashback: BigDecimal,
    val cro_stake_rewards: Boolean,
    val cro_stake_rewards_detail: String,
    val netflix_reimbursement: Boolean,
    val netflix_reimbursement_detail: String,
    val spotify_reimbursement: Boolean,
    val spotify_reimbursement_detail: String
)
data class CardBenefitWithStake(
    val card_cashback: BigDecimal,
    val cro_stake_rewards: Boolean,
    val cro_stake_rewards_detail: String,
    val netflix_reimbursement: Boolean,
    val netflix_reimbursement_detail: String,
    val spotify_reimbursement: Boolean,
    val spotify_reimbursement_detail: String
)
data class CardData(
    val cards: List<Card>
)