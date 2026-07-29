package com.jparkbro.auth.impl.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.auth.api.AuthNavKey
import com.jparkbro.auth.impl.email.login.EmailLoginRoot
import com.jparkbro.auth.impl.email.signup.EmailSignupRoot
import com.jparkbro.auth.impl.login.LoginRoot
import com.jparkbro.auth.impl.password.reset.PasswordResetRoot
import com.jparkbro.auth.impl.password.verification.PasswordVerificationRoot
import com.jparkbro.auth.impl.preferencesetup.PreferenceSetupRoot
import com.jparkbro.home.api.navigateToHomeMain
import kr.agromarket.at.core.navigation.Navigator

@OptIn(ExperimentalSharedTransitionApi::class)
fun EntryProviderScope<NavKey>.authEntry(
    navigator: Navigator,
    sharedTransitionScope: SharedTransitionScope,
) {
    entry<AuthNavKey.Login> {
        LoginRoot(
            sharedTransitionScope = sharedTransitionScope,
            onNavigateToHome = navigator::navigateToHomeMain,
            onNavigateToEmailLogin = { navigator.navigate(AuthNavKey.Email.Login) },
            onNavigateToEmailSignup = { navigator.navigate(AuthNavKey.Email.Signup) },
            onNavigateToPreferenceSetup = { navigator.navigate(AuthNavKey.PreferenceSetup) },
        )
    }
    entry<AuthNavKey.Email.Login> {
        EmailLoginRoot(
            onNavigateToHome = navigator::navigateToHomeMain,
            onNavigateToPreferenceSetup = { navigator.navigate(AuthNavKey.PreferenceSetup) },
            onNavigateToEmailSignup = { navigator.navigate(AuthNavKey.Email.Signup) },
            onNavigateToPasswordVerification = { navigator.navigate(AuthNavKey.Password.Verification) },
            onBackClick = navigator::goBack,
        )
    }
    entry<AuthNavKey.Email.Signup> {
        EmailSignupRoot()
    }
    entry<AuthNavKey.Password.Verification> {
        PasswordVerificationRoot()
    }
    entry<AuthNavKey.Password.Reset> {
        PasswordResetRoot()
    }
    entry<AuthNavKey.PreferenceSetup> {
        PreferenceSetupRoot()
    }
}