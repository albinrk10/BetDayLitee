package com.albin.betdaylite.infrastructure.repository

import com.albin.betdaylite.core.constants.BetConstants
import com.albin.betdaylite.domain.entities.UserWallet
import com.albin.betdaylite.domain.repository.WalletRepository
import com.albin.betdaylite.infrastructure.local.dao.BetDao
import com.albin.betdaylite.infrastructure.local.dao.WalletDao
import com.albin.betdaylite.infrastructure.local.entity.WalletEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao,
    private val betDao: BetDao
) : WalletRepository {

    override fun getWallet(): Flow<UserWallet> {
        return combine(
            walletDao.getWallet().onStart {
                // Verificar si existe la billetera; si no, inicializar
            },
            betDao.getActiveBetsCount()
        ) { entity, activeCount ->
            if (entity == null) {
                val initial = WalletEntity(
                    id = 1,
                    balance = BetConstants.INITIAL_WALLET_BALANCE,
                    totalWagered = 350.0,
                    totalWon = 520.0
                )
                walletDao.setWallet(initial)
                UserWallet(
                    balance = initial.balance,
                    totalWagered = initial.totalWagered,
                    totalWon = initial.totalWon,
                    activeBetsCount = activeCount
                )
            } else {
                UserWallet(
                    balance = entity.balance,
                    totalWagered = entity.totalWagered,
                    totalWon = entity.totalWon,
                    activeBetsCount = activeCount
                )
            }
        }
    }

    override suspend fun deductBalance(amount: Double): Result<Unit> {
        return try {
            walletDao.deductBalance(amount)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun depositFunds(amount: Double): Result<Unit> {
        return try {
            walletDao.depositFunds(amount)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
