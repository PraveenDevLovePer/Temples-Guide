plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        create("release") {
            storeFile =
                file("C:\\Users\\Praveen\\AndroidStudioProjects\\jks\\templestrip\\templestrip.jks")
            storePassword = "temples_trip"
            keyAlias = "Temples_Trip"
            keyPassword = "temples_trip"
        }
    }
    namespace = "com.techdevlp.templesguide"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.techdevlp.templesguide"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
        buildConfig = true
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(platform(libs.compose.bom))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(platform(libs.compose.bom))
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
    implementation(libs.coroutines.play.services)
    implementation(libs.google.services.auth)

    //Material design
    implementation(libs.material.v2)
    implementation (libs.androidx.material.v121)

    //FireStore database
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.firestore)

    //Coil for image loading
    implementation(libs.coil.compose)

    //For livedata
    implementation (libs.androidx.runtime)
    implementation (libs.androidx.runtime.livedata)

    //Ml model for language change
    implementation (libs.firebase.ml.natural.language)
    implementation (libs.firebase.ml.natural.language.translate.model)

    //Open street maps
    implementation (libs.osmdroid.android)

    //Google admob
    implementation (libs.play.services.ads)

}