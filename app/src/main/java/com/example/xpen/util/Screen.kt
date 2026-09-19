package com.example.xpen.util

sealed class Screen(val route: String, val title: String) {
    object Entry : Screen("entry", "Xpen")
    object Today : Screen("today", "Today")
    object ThisWeek : Screen("this_week", "This Week")
    object ThisMonth : Screen("this_month", "This Month")
    object LastMonth : Screen("last_month", "Last Month")
    object Last3Months : Screen("last_3_months", "Last 3 Months")
}