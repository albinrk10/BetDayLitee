package com.albin.betdaylite.domain.usecases

import com.albin.betdaylite.domain.entities.UserWallet
import com.albin.betdaylite.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWalletBalanceUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    operator fun invoke(): Flow<UserWallet> {
        return walletRepository.getWallet()
    }
}

class DepositFundsUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke(amount: Double): Result<Unit> {
        if (amount <= 0) {
            return Result.failure(IllegalArgumentException("El depósito debe ser mayor a 0"))
        }
        return walletRepository.depositFunds(amount)
    }
}
