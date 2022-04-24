package com.tokyonth.weather.service

import android.app.Notification
import android.os.UserHandle
import android.os.Parcel
import android.service.notification.StatusBarNotification

class WeatherSystemBarService : StatusBarNotification {
    constructor(
        pkg: String?,
        opPkg: String?,
        id: Int,
        tag: String?,
        uid: Int,
        initialPid: Int,
        score: Int,
        notification: Notification?,
        user: UserHandle?,
        postTime: Long
    ) : super(pkg, opPkg, id, tag, uid, initialPid, score, notification, user, postTime) {
    }

    constructor(`in`: Parcel?) : super(`in`) {}
}