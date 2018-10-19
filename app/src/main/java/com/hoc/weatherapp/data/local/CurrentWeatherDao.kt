package com.hoc.weatherapp.data.local

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import com.hoc.weatherapp.data.models.entity.CityAndCurrentWeather
import com.hoc.weatherapp.data.models.entity.CurrentWeather
import com.hoc.weatherapp.utils.debug
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class CurrentWeatherDao {
    @Query("SELECT * FROM current_weathers, cities WHERE city_id = :cityId")
    abstract fun getCityAndCurrentWeatherByCityId(cityId: Long): Flowable<CityAndCurrentWeather>

    @Query("SELECT * FROM current_weathers, cities ORDER BY city_id")
    abstract fun getAllCityAndCurrentWeathers(): Flowable<List<CityAndCurrentWeather>>

    @Insert(onConflict = OnConflictStrategy.FAIL)
    abstract fun insertCurrentWeather(currentWeather: CurrentWeather)

    @Update
    abstract fun updateCurrentWeather(currentWeather: CurrentWeather)

    @Delete
    abstract fun deleteCurrentWeather(currentWeather: CurrentWeather)

    open fun upsert(currentWeather: CurrentWeather) {
        try {
            debug("Insert currentWeather=$currentWeather", "@@@")
            insertCurrentWeather(currentWeather)
        } catch (e: SQLiteConstraintException) {
            debug("Insert fail --> update currentWeather=$currentWeather", "@@@")
            updateCurrentWeather(currentWeather)
        }
    }

    @Query("SELECT * FROM current_weathers, cities WHERE city_id = :cityId")
    abstract fun getCityAndCurrentWeatherByCityIdAsSingle(cityId: Long): Single<CityAndCurrentWeather>
}