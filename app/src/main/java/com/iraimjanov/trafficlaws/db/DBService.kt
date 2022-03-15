package com.iraimjanov.trafficlaws.db

import com.iraimjanov.trafficlaws.models.RoadSign

interface DBService {
    fun addRoadSign(roadSign: RoadSign): Boolean
    fun showRoadSign(): ArrayList<RoadSign>
    fun updateRoadSign(roadSign: RoadSign): Boolean
    fun deleteRoadSign(roadSign: RoadSign): Boolean
}