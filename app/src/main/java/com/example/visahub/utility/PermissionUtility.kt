package com.example.visahub.utility

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

object PermissionUtility {
    fun hasContactPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.READ_CONTACTS
        )
}