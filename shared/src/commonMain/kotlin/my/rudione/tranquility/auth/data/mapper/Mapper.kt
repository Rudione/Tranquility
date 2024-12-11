package my.rudione.tranquility.auth.data.mapper

import my.rudione.tranquility.auth.data.model.AuthResponseData
import my.rudione.tranquility.auth.domain.model.AuthResultData

internal fun AuthResponseData.toAuthResultData(): AuthResultData {
    return AuthResultData(
        id,
        name,
        bio,
        avatar,
        token,
        followersCount,
        followingCount
    )
}