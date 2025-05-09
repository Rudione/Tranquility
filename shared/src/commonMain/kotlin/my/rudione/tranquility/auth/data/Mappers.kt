package my.rudione.tranquility.auth.data

import my.rudione.tranquility.auth.domain.model.AuthResultData

internal fun AuthResponseData.toAuthResultData(): AuthResultData{
    return AuthResultData(id, name, bio, avatar, token, followersCount, followingCount)
}