# Project Setup Guide (Android on Windows)

## System Requirements
- `Java`: JDK 17 (jbr-17.x.yy, e.g: Eclipse Temurin 17.x.yy -> https://adoptium.net/temurin/releases/?version=17)
- `Gradle`: v7.5 (open .../gradle/wrapper/gradle-wrapper.properties and set the required version in distributionUrl, e.g: `distributionUrl=https\://services.gradle.org/distributions/gradle-7.5-bin.zip`)
- `IDE`: IntelliJ IDEA or equivalent
- `Android SDK (emulator)`: v35.0.2, device Pixel_2_API_36 -> https://developer.android.com/tools/releases/platform-tools
- `Appium (server)`: v2.17.1 -> https://appium.io/docs/en/2.0/quickstart/install/
- `Appium UiAutomator2 Driver`: v4.1.5 -> https://appium.io/docs/en/2.0/quickstart/uiauto2-driver/
- `Appium Images Plugin`: v4.1.5 -> https://github.com/appium/appium/tree/master/packages/images-plugin
- `Appium Inspector` (for test development only): https://github.com/appium/appium-inspector/releases
- `Allure CLI` (logs and test reports): https://github.com/allure-framework/allure2/releases

## Project Structure and Configuration
Clone the [repository (dev branch)](https://bitbucket.nexicloud.it/projects/MYP/repos/msmypaymentsapp-automation/browse?at=refs%2Fheads%2Fdev) in the folder `C:\mypayments\msmypaymentsapp-automation`, in order to obtain the following structure:
- `java/core`: core components of the test suite.
- `java/flows`: UI test flows and related test data.
- `java/util`: utilities to handle system environment, external resources (email, localise, etc.) and other non test-related functionalities. 
- `resources/apks`: empty folder used to store the required testing version(s) of MPA (a .gitkeep file is used to track the empty folder on git). In order to inspect the app UI components, the apk should contain a build of MPA where DexGuard is disabled. 
- `resources/properties`: environmental variables.
- `resources/snapshots`: empty folder used to store the required AVD snapshots, which can be created using the Android emulator (a .gitkeep file is used to track the empty folder on git). The name of a androidSnapshot should describe the status of MPA and AVD when the androidSnapshot was captured:
  - **ioAppiumSettings**: androidSnapshot where the app ioAppiumSettings has been installed. It is an app required by Appium to connect its driver to the Android emulator.
  - **ioAppiumSettings-MPA**: androidSnapshot where ioAppiumSettings and MPA have been installed. When opening MPA, the first login (registration) page should be shown.
  - **ioAppiumSettings-MPA-Overview**: androidSnapshot where ioAppiumSettings and MPA have been installed, and the first login (registration) procedure has been performed (either manually or automatically). Therefore, when opening MPA the Overview page should be shown.
- `resources/screenshots`: screenshots taken on test failures and attached to Allure reports.
- `resources/reports`: Allure HTML reports generated after test execution. Open index.html via local server or Allure CLI to load report correctly on the browser and avoid 404 errors (e.g: via IntelliJ right-click -> Open In -> Browser -> _your choice_ or by starting a local server within the report folder) .

## Maintenance
- `resources`: keep the items up-to-date, according to the latest MPA release and related test cases/data.
- `Test Automation`: refer to the [designated Confluence page](https://nexigroup-germany.atlassian.net/wiki/spaces/SMEP/pages/367362327/Testing+Automation).

## How to run Appium for test development and troubleshooting
1. **Run Appium server**. In a terminal write: ``` appium ```.
2. **Launch Android emulator**. In a terminal write: ``` %USER_HOME%\AppData\Local\Android\Sdk\emulator\emulator.exe\emulator.exe -avd Pixel_2_API_36 ```.
3. **Launch Appium Inspector app**. 
   1. _**Appium Server**_ : use default settings (leave Remote Host, Port and Path blank).
   2. _**Capability Builder**_ : use the following Capability set:
    ```json
    {
      "platformName": "Android",
      "appium:deviceName": "Pixel_2_API_36",
      "appium:automationName": "UiAutomator2",
      "appium:app": "path/***.apk",
      "appium:avdLaunchTimeout": 120000
    }
    ```


## CI/CD
### call from fastlane
sample:
```
Dir.chdir(test_project_path) do
sh("mvn clean test -DapkPath=#{apk_path}")
end
```


set env variable (MacOS)
export ANDROID_HOME=/Users/lorenzodemarchi/Library/Android/sdk
export JAVA_HOME=/Applications/Android\ Studio.app/Contents/jbr/Contents/Home

set env variable (Win)
%ANDROID_HOME%\emulator
%ANDROID_HOME%\tools
%ANDROID_HOME%\tools\bin
%ANDROID_HOME%\platform-tools
# --------------------------------------

# Project Setup Guide (Android on MacOS)

## System Requirements
- `Java`: JDK 17 (jbr-17.0.14) ->
- `Gradle`: v7.5
- `IDE`: IntelliJ IDEA or equivalent
- `Android SDK (emulator)`: v35.0.2, device Pixel_2_API_36 -> https://developer.android.com/tools/releases/platform-tools
- `Appium (server)`: v2.17.1 -> https://appium.io/docs/en/2.0/quickstart/install/
- `Appium UiAutomator2 Driver`: v4.1.5 -> https://appium.io/docs/en/2.0/quickstart/uiauto2-driver/
- `Appium Images Plugin`: v4.1.5 -> https://github.com/appium/appium/tree/master/packages/images-plugin
- `Appium Inspector` (for test development only): https://github.com/appium/appium-inspector/releases
- `Allure CLI` (logs and test reports): https://github.com/allure-framework/allure2/releases

## Installation
Open Terminal and run these commands to install the needed software:

-- Java

- brew install openjdk@17
- echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
- source ~/.zshrc
- java -version  # check the installed version

-- Gradle
- brew install gradle@7.5
- export PATH="/opt/gradle/gradle-7.5/bin:$PATH" >> ~/.zshrc
- source ~/.zshrc
- gradle -v  # check the installed version


-- `IDE`: IntelliJ IDEA or equivalent

-- `Android SDK (emulator)`: v35.0.2, device Pixel_2_API_36 -> https://developer.android.com/tools/releases/platform-tools
-  brew install --cask android-sdk
- export ANDROID_HOME=$HOME/Library/Android/sdk
- export PATH=$ANDROID_HOME/platform-tools:$PATH
- echo 'export ANDROID_HOME=$HOME/Library/Android/sdk' >> ~/.zshrc
- echo 'export PATH=$ANDROID_HOME/platform-tools:$PATH' >> ~/.zshrc
- source ~/.zshrc
- adb version  # check installed version

-- Installing emulator:

- Open Android Studio → AVD Manager
- Create device Pixel_2_API_36

-- Installing NPM
- brew install node
- node -v # check version
- npm -v # check version

-- `Appium (server)`: v2.17.1 -> https://appium.io/docs/en/2.0/quickstart/install/
- npm install -g appium@2.17.1
- appium -v  # check installed version

-- `Appium UiAutomator2 Driver`: v4.1.5 -> https://appium.io/docs/en/2.0/quickstart/uiauto2-driver/
- appium driver install uiautomator2@4.1.5

-- `Appium Images Plugin`: v4.1.5 -> https://github.com/appium/appium/tree/master/packages/images-plugin
- npm install -g @appium/images-plugin@4.1.5

-- `Appium Inspector` (for test development only): https://github.com/appium/appium-inspector/releases

-- `Allure CLI` (logs and test reports): https://github.com/allure-framework/allure2/releases
- brew install allure
- allure --version  # check version


## Project Structure and Configuration
Clone the [repository (dev branch)](https://bitbucket.nexicloud.it/projects/MYP/repos/msmypaymentsapp-automation/browse?at=refs%2Fheads%2Fdev) in the folder `C:\mypayments\msmypaymentsapp-automation`, in order to obtain the following structure:
- `java/core`: core components of the test suite.
- `java/flows`: UI test flows and related test data.
- `java/util`: utilities to handle system environment, external resources (email, localise, etc.) and other non test-related functionalities.
- `resources/apks`: empty folder used to store the required testing version(s) of MPA (a .gitkeep file is used to track the empty folder on git). In order to inspect the app UI components, the apk should contain a build of MPA where DexGuard is disabled.
- `resources/properties`: environmental variables.
- `resources/snapshots`: empty folder used to store the required AVD snapshots, which can be created using the Android emulator (a .gitkeep file is used to track the empty folder on git). The name of a androidSnapshot should describe the status of MPA and AVD when the androidSnapshot was captured:
    - **ioAppiumSettings**: androidSnapshot where the app ioAppiumSettings has been installed. It is an app required by Appium to connect its driver to the Android emulator.
    - **ioAppiumSettings-MPA**: androidSnapshot where ioAppiumSettings and MPA have been installed. When opening MPA, the first login (registration) page should be shown.
    - **ioAppiumSettings-MPA-Overview**: androidSnapshot where ioAppiumSettings and MPA have been installed, and the first login (registration) procedure has been performed (either manually or automatically). Therefore, when opening MPA the Overview page should be shown.
- `resources/screenshots`: screenshots taken on test failures and attached to Allure reports.
- `resources/reports`: Allure HTML reports generated after test execution. Open index.html via local server or Allure CLI to load report correctly on the browser and avoid 404 errors (e.g: via IntelliJ right-click -> Open In -> Browser -> _your choice_ or by starting a local server within the report folder) .

## Maintenance
- `resources`: keep the items up-to-date, according to the latest MPA release and related test cases/data.
- `Test Automation`: refer to the [designated Confluence page](https://nexigroup-germany.atlassian.net/wiki/spaces/SMEP/pages/367362327/Testing+Automation).

## How to run Appium for test development and troubleshooting
1. **Run Appium server**. In a terminal write: ``` appium ```.
2. **Launch Android emulator**. In a terminal write: ``` %USER_HOME%\AppData\Local\Android\Sdk\emulator\emulator.exe\emulator.exe -avd Pixel_2_API_36 ```.
3. **Launch Appium Inspector app**.
    1. _**Appium Server**_ : use default settings (leave Remote Host, Port and Path blank).
    2. _**Capability Builder**_ : use the following Capability set:
    ```json
    {
      "platformName": "Android",
      "appium:deviceName": "Pixel_2_API_36",
      "appium:automationName": "UiAutomator2",
      "appium:app": "path/***.apk",
      "appium:avdLaunchTimeout": 120000
    }
    ```


## CI/CD
### call from fastlane
sample:
```
Dir.chdir(test_project_path) do
sh("mvn clean test -DapkPath=#{apk_path}")
end
```


set env variable (MacOS)
export ANDROID_HOME=/Users/lorenzodemarchi/Library/Android/sdk
export JAVA_HOME=/Applications/Android\ Studio.app/Contents/jbr/Contents/Home

set env variable (Win)
%ANDROID_HOME%\emulator
%ANDROID_HOME%\tools
%ANDROID_HOME%\tools\bin
%ANDROID_HOME%\platform-tools
# --------------------------------------

# Project Setup Guide (iOS on MacOS)

Automated testing framework for the **MyPayments iOS application**, built with **Appium 2**, **Java**, and **Maven**. This project follows the **Page Object Model (POM)** design pattern for maintainable and scalable test automation.

---

## 📁 Project Structure

```
MyPayments-Appium2-iOS
├── pom.xml                               # Maven project configuration file
├── Appium-log.txt                        # Log output from Appium test runs
├── src
│   ├── main
│   │   └── java
│   │       ├── drivers
│   │       │   └── Drivers.java          # Appium driver setup and lifecycle management
│   │       ├── pages
│   │       │   └── ios
│   │       │       ├── BasePage.java     # Common iOS page functions
│   │       │       ├── LoginPage.java    # Page object for the login screen
│   │       │       └── SignUpPage.java   # Page object for the signup screen
│   │       ├── properties
│   │       │   ├── DriversProperties.java# Driver-related property reader
│   │       │   └── MpaProperties.java    # App-specific property reader
│   │       ├── utils
│   │       │   ├── ios
│   │       │   │   ├── iOSUtils.java     # Utility methods specific to iOS
│   │       │   │   └── Utils.java        # General utility methods
│   │       │   └── services
│   │       │       └── AppiumService.java# Controls Appium server programmatically
│   │       └── resources
│   │       ├── app.MyPayments Pre-Prod.app # Pre-production iOS app binary
│   │       ├── drivers.properties        # Config for driver behavior
│   │       └── mpa.properties            # App-level configuration
│   │
│   └── test
│       └── java
│           └── ios
│               └── RegistrationTest.java # iOS registration screen test case
│
└── target                                # Compiled code and test reports
```

---

## 🚀 Getting Started

### 📦 Prerequisites

Make sure the following dependencies are installed and accessible via terminal:

```bash
java -version
# openjdk version "23.0.2" 2025-01-21

mvn -v
# Apache Maven 3.9.9

appium -v
# 2.17.1
appium driver install xcuitest
```

Other requirements:

- Xcode with iOS simulators
  - force full xcode usage: sudo xcode-select --switch /Applications/Xcode.app 
- Node.js & npm
- WebDriverAgent installed (for real device testing)

---

## 🧪 Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Specific Test

```bash
mvn -Dtest=RegistrationTest test
```

---

## 🛠 Features

- ✅ Page Object Model (POM) for modular test structure
- ✅ Appium 2.x with programmatic server control
- ✅ Configurable with `.properties` files
- ✅ Supports real device & simulator
- ✅ Clean logging and teardown processes

---

## 📂 Key Resources

- `drivers.properties` – Driver configuration
- `mpa.properties` – Application-specific configs
- `Appium-log.txt` – Log output for test execution

---

## 👨‍💻 Author

Shuai




