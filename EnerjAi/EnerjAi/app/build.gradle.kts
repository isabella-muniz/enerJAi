plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.enerjai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.enerjai"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("org.json:json:20210307")
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    implementation("com.google.firebase:firebase-auth:22.1.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("com.google.firebase:firebase-firestore:24.7.1")
    implementation("com.google.android.gms:play-services-base:18.0.1")
    implementation("com.google.android.gms:play-services-auth:19.2.0")
    implementation("com.google.android.gms:play-services-tasks:18.0.1")
    
}