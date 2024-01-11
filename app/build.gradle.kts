@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.googleServices)
}

android {
    namespace = "com.techdevlp.templesguide"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.techdevlp.templesguide"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.bundles.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    //splash screen
    implementation (libs.androidx.core.splashscreen)

    //retrofit
    implementation(libs.converter.gson)

    //Navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    //preference data store
    implementation(libs.androidx.datastore.preferences)

    //Location permission
    implementation(libs.google.accompanist.permissions)

    //Location service
    implementation(libs.google.location.service)

    //Google services
    implementation(libs.google.services)
    implementation(libs.google.services.auth)

    //Firebase analytics
    implementation(libs.firebase.analytics.ktx )
    implementation(libs.firebase.auth.ktx )
    implementation(platform(libs.firebase.bom) )

//    implementation 'com.google.android.gms:play-services-auth:20.7.0'
//    implementation("com.google.firebase:firebase-analytics")


}