package com.coursecampus.athleteconnect.data.mapper

import com.coursecampus.athleteconnect.data.local.entity.OpportunityEntity
import com.coursecampus.athleteconnect.data.model.Opportunity
import com.coursecampus.athleteconnect.data.model.OpportunityType

fun OpportunityEntity.toDomain(): Opportunity {
    return Opportunity(
        id = id,
        title = title,
        organization = organization,
        type = OpportunityType.valueOf(type),
        sport = sport,
        location = location,
        deadline = deadline,
        description = description,
        requirements = requirements,
        applied = applied,
        imageUrl = imageUrl,
        contactEmail = contactEmail,
        website = website,
        salary = salary,
        duration = duration,
        benefits = benefits
    )
}

fun Opportunity.toEntity(): OpportunityEntity {
    return OpportunityEntity(
        id = id,
        title = title,
        organization = organization,
        type = type.name,
        sport = sport,
        location = location,
        deadline = deadline,
        description = description,
        requirements = requirements,
        applied = applied,
        imageUrl = imageUrl,
        contactEmail = contactEmail,
        website = website,
        salary = salary,
        duration = duration,
        benefits = benefits
    )
}

