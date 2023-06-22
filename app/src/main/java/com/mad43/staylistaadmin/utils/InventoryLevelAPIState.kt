package com.mad43.staylistaadmin.utils

import com.mad43.staylistaadmin.product.data.entity.InventoryLevelRoot
import com.mad43.staylistaadmin.product.data.entity.ProductModel

sealed class InventoryLevelAPIState {
    open class OnSuccess(var inventoryLevelRoot: InventoryLevelRoot) : InventoryLevelAPIState()

    class OnFail(val errorMessage: Throwable) : InventoryLevelAPIState()
    object Loading : InventoryLevelAPIState()
}
