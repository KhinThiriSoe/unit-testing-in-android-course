package com.techyourchance.mockitofundamentals.exercise5.eventbus

import com.techyourchance.mockitofundamentals.exercise5.users.User

data class UserDetailsChangedEvent(val user: User)