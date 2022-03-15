package com.iraimjanov.trafficlaws.models

class RoadSign {
    var id: Int? = null
    var name: String? = null
    var about: String? = null
    var type: String? = null
    var image: String? = null
    var like: String? = null

    constructor(
        id: Int?,
        name: String?,
        about: String?,
        type: String?,
        image: String?,
        like: String?,
    ) {
        this.id = id
        this.name = name
        this.about = about
        this.type = type
        this.image = image
        this.like = like
    }

    constructor(name: String?, about: String?, type: String?, image: String?, like: String?) {
        this.name = name
        this.about = about
        this.type = type
        this.image = image
        this.like = like
    }

    constructor()
}


