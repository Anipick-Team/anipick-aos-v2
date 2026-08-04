package com.jparkbro.auth.impl.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.auth.api.AuthNavKey
import com.jparkbro.auth.api.navigateToLogin
import com.jparkbro.auth.api.navigateToPreferenceSetup
import com.jparkbro.auth.impl.email.login.EmailLoginRoot
import com.jparkbro.auth.impl.email.signup.EmailSignupRoot
import com.jparkbro.auth.impl.login.LoginRoot
import com.jparkbro.auth.impl.password.reset.PasswordResetRoot
import com.jparkbro.auth.impl.password.verification.PasswordVerificationRoot
import com.jparkbro.auth.impl.preferencesetup.PreferenceSetupRoot
import com.jparkbro.home.api.navigateToHomeMain
import kr.agromarket.at.core.navigation.Navigator

const val LOGIN_CONTENT_KEY = "AuthNavKey.Login"
const val EMAIL_LOGIN_CONTENT_KEY = "AuthNavKey.Email.Login"
const val EMAIL_SIGNUP_CONTENT_KEY = "AuthNavKey.Email.Signup"
const val PASSWORD_VERIFICATION_CONTENT_KEY = "AuthNavKey.Password.Verification"
const val PASSWORD_RESET_CONTENT_KEY = "AuthNavKey.Password.Reset"
const val PREFERENCE_SETUP_CONTENT_KEY = "AuthNavKey.PreferenceSetup"

@OptIn(ExperimentalSharedTransitionApi::class)
fun EntryProviderScope<NavKey>.authEntry(
    navigator: Navigator,
    sharedTransitionScope: SharedTransitionScope,
) {
    entry<AuthNavKey.Login>(clazzContentKey = { LOGIN_CONTENT_KEY }) {
        LoginRoot(
            sharedTransitionScope = sharedTransitionScope,
            onNavigateToHome = navigator::navigateToHomeMain,
            onNavigateToEmailLogin = { navigator.navigate(AuthNavKey.Email.Login) },
            onNavigateToEmailSignup = { navigator.navigate(AuthNavKey.Email.Signup) },
            onNavigateToPreferenceSetup = navigator::navigateToPreferenceSetup,
        )
    }
    entry<AuthNavKey.Email.Login>(clazzContentKey = { EMAIL_LOGIN_CONTENT_KEY }) {
        EmailLoginRoot(
            onNavigateToHome = navigator::navigateToHomeMain,
            onNavigateToPreferenceSetup = navigator::navigateToPreferenceSetup,
            onNavigateToEmailSignup = { navigator.navigate(AuthNavKey.Email.Signup) },
            onNavigateToPasswordVerification = { navigator.navigate(AuthNavKey.Password.Verification) },
            onBackClick = navigator::goBack,
        )
    }
    entry<AuthNavKey.Email.Signup>(clazzContentKey = { EMAIL_SIGNUP_CONTENT_KEY }) {
        EmailSignupRoot(
            onNavigateToPreferenceSetup = navigator::navigateToPreferenceSetup,
            onBackClick = navigator::goBack,
        )
    }
    entry<AuthNavKey.Password.Verification>(clazzContentKey = { PASSWORD_VERIFICATION_CONTENT_KEY }) {
        PasswordVerificationRoot(
            onNavigateToLogin = navigator::navigateToLogin,
            onNavigateToPasswordReset = { email -> navigator.navigate(AuthNavKey.Password.Reset(email)) },
            onBackClick = navigator::goBack,
        )
    }
    entry<AuthNavKey.Password.Reset>(clazzContentKey = { PASSWORD_RESET_CONTENT_KEY }) { key ->
        PasswordResetRoot(
            email = key.email,
            onNavigateToEmailLogin = { navigator.navigateAndClearStack(AuthNavKey.Email.Login) },
            onBackClick = navigator::goBack,
        )
    }
    entry<AuthNavKey.PreferenceSetup>(clazzContentKey = { PREFERENCE_SETUP_CONTENT_KEY }) {
        PreferenceSetupRoot(
            onNavigateToHome = navigator::navigateToHomeMain,
        )
    }
}