package com.tom.stocktable

import android.app.Application
import com.tom.stocktable.database.AppDatabase

class StockTableApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}