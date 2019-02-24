package com.github.vincebrees.bitcoinmarket.data.remote.pojo

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Vincent ETIENNE on 23/02/2019.
 */
@RunWith(MockitoJUnitRunner::class)
class RestBitcoinResponseTest{

    @Test
    fun mappingToEntityGeneralData(){
        val listRestCoordonate = arrayListOf<RestCoordonate>()
        listRestCoordonate.add(RestCoordonate(1, 2.0))

        val pojo = RestBitcoinResponse("status", "name", "unit", "period", "description", listRestCoordonate)
        val entity = pojo.toEntity()

        assertTrue(entity.status == pojo.status)
        assertTrue(entity.title == pojo.name)
        assertTrue(entity.unit == pojo.unit)
        assertTrue(entity.period == pojo.period)
        assertTrue(entity.description == pojo.description)
    }

    @Test
    fun mappingToEntityListWithValue(){
        val listRestCoordonate = arrayListOf<RestCoordonate>()
        val restCoordonate = RestCoordonate(1, 2.0)
        listRestCoordonate.add(restCoordonate)

        val pojo = RestBitcoinResponse("status", "name", "unit", "period", "description", listRestCoordonate)
        val entity = pojo.toEntity()

        val coordonate = entity.values[0]
        assertTrue(restCoordonate.x == coordonate.x)
        assertTrue(restCoordonate.y == coordonate.y)
    }

    @Test
    fun mappingToEntityWithNullValues(){

        val pojo = RestBitcoinResponse(null, null, null, null, null, null)
        val entity = pojo.toEntity()
        assertTrue(entity.status == "")
        assertTrue(entity.title == "")
        assertTrue(entity.unit == "")
        assertTrue(entity.period == "")
        assertTrue(entity.description == "")
        assertTrue(entity.values.isEmpty())
    }
}