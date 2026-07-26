package com.jparkbro.auth.impl.di

import com.jparkbro.auth.impl.email.login.EmailLoginViewModel
import com.jparkbro.auth.impl.email.signup.EmailSignupViewModel
import com.jparkbro.auth.impl.login.LoginViewModel
import com.jparkbro.auth.impl.password.reset.PasswordResetViewModel
import com.jparkbro.auth.impl.password.verification.PasswordVerificationViewModel
import com.jparkbro.auth.impl.preferencesetup.PreferenceSetupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::EmailLoginViewModel)
    viewModelOf(::EmailSignupViewModel)
    viewModelOf(::PasswordVerificationViewModel)
    viewModelOf(::PasswordResetViewModel)
    viewModelOf(::PreferenceSetupViewModel)
}
