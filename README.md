# TelematicsApp-Android-Dashboard
Dashboard module of TelematicsApp-Android

## Description

This library is created by DATA MOTION PTE. LTD. and allows you to integrate with our UserStatistic API and Dashboard view in a few steps.

## Credentials

Before you start using the framework, make sure you registered a company account in [DataHub](https://app.damoov.com/). If you are new, please refer to the [documentation](https://docs.telematicssdk.com/docs/setting-up-the-company-account) and register your company account in DataHub. [Sing Up](https://app.damoov.com/user/registration)

## Setup

1. Download from this repository [dashboard_framework](https://github.com/Mobile-Telematics/TelematicsApp-Android-Dashboard/tree/prerelease/dashboard_framework) folder
2. Move this folder to the project folder next to the `app` folder
![](https://github.com/Mobile-Telematics/TelematicsApp-Android-Dashboard/blob/prerelease/img_readme/img.png)
3. In your `settings.gradle (Project settings)`  add `include ':dashboard_module'`
2. Import module:
``` groovy
    dependencies {
        ...
        implementation project(':dashboard_module')
    }
```
## Methods
### Initialize

Getting the object and setting the context for the module.
``` kotlin
    val dashboardModule = DashboardModule.getInstance()
    dashboardModule.initialize(context)  
```

### Set Here Map Api key

You will need an api key to display a preview of the last trip on the map. 
``` kotlin
    dashboardModule.setHereMapApiKey(hereApiKey)
```

### Instructions on how to get api key: 

Step 1: After creating your HERE account, open your project in https://developer.here.com/projects </br>
In the REST table click "Generate App".

![](https://github.com/Mobile-Telematics/TelematicsApp-Android/blob/master/img_readme/here_step_1.png)

Step 2: Click "Create API key"

![](https://github.com/Mobile-Telematics/TelematicsApp-Android/blob/master/img_readme/here_step_2.png)

Copy API KEY and paste it to `setHereMapApiKey` method.

### Set credentials

``` kotlin
    val credentials = Credentials(deviceToken, accessToken)
    dashboardModule.setCredentials(credentials)
```

`deviceToken` - is the main individual SDK user identifier. This identifier is used as a key across all our services.

`accessToken` - or JSON Web Token (JWT) is the main UserService API key, that allows you to get user individual statistics and user scoring by UserService APIs calls.

### Set Dashboard mileage limit

A value that determines when the dashboard will be unlocked to display statistics.

``` kotlin
    dashboardModule.setDashboardMileageLimitKm(10)
```

### Get Dashboard fragment

Getting a fragment to display in your container.

``` kotlin
    val fragment = dashboardModule.getDashboardFragment()
```

Here's an example of using the DashboardModule.


``` kotlin
    private fun showDashboard(deviceToken: String, accessToken: String, hereMapKey: String) {
    
        //initialize
        val dashboardModule = DashboardModule.getInstance()
        dashboardModule.initialize(this)
        
        //set here map api key
        dashboardModule.setHereMapApiKey(hereMapKey)
        
        //set credentials
        val credentials = Credentials(deviceToken, accessToken)
        dashboardModule.setCredentials(credentials)
        
        //set distance limit
        dashboardModule.setDashboardMileageLimitKm(10)
        
        //get dashboard fragment
        val dashboardFragment = dashboardModule.getDashboardFragment()
        
        // show fragment
        val container = R.id.container
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(container, dashboardFragment)
        transaction.commit()
    }
```

You can also check full exaple in this repository: [DashboardDemoApp](https://github.com/Mobile-Telematics/TelematicsApp-Android-Dashboard/tree/main/DemoApp)


Happy coding!


## Links

[Official product Web-page](https://app.damoov.com/)

[Official API services web-page](https://www.damoov.com/telematics-api/)

[Official API references](https://docs.telematicssdk.com/reference)

[Official ZenRoad web-page](https://www.damoov.com/telematics-app/)

[Official ZenRoad app for iOS](https://apps.apple.com/jo/app/zenroad/id1563218393)

[Official ZenRoad app for Android](https://play.google.com/store/apps/details?id=com.telematicssdk.zenroad&hl=en&gl=US)

[Official ZenRoad app for Huawei](https://appgallery.huawei.com/#/app/C104163115)

###### Copyright © 2020-2022 DATA MOTION PTE. LTD. All rights reserved.


