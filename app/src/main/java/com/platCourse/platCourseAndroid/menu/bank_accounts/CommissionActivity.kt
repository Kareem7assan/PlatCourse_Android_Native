package com.platCourse.platCourseAndroid.menu.bank_accounts

import com.rowaad.app.base.BaseActivity
import com.platCourse.platCourseAndroid.R
import com.rowaad.utils.extention.replaceFragmentToActivity

class CommissionActivity : BaseActivity(R.layout.activity_commission) {

    override fun init() {
        replaceFragmentToActivity(BankAccountsFragment.newInstance(),false)
    }

}