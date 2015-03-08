==================================================
 README for "RICOH THETA SDK for Android"

 Version :0.3.0
==================================================

This file is an explanation document for RICOH THETA SDK for Android.
ricoh-theta4j and r-exif4j are libraries for creating apps for Android.
ricoh-theta-sample-for-android is a sample app created using the libraries mentioned above.

----------------------------------------

* Contents of this Document

    * Terms of Service
    * Files included in the archive
    * Required environment for development
    * How to Use
    * Latest information
    * Troubleshooting
    * Trademarks
    * Update History

----------------------------------------

* Terms of Service

    Terms of service are included in the LICENSE.txt (LICENSE_ja.txt) file.
    It is assumed that you have agreed to these terms of service when you start using RICOH THETA SDK.

----------------------------------------

* Files included in the archive

    README.txt: This file (English)
    README_ja.txt: This file (Japanese)
    LICENSE.txt: Terms of service file (English)
    LICENSE_ja.txt: Terms of service file (Japanese)
    ricoh-theta-sample-for-android
    ┣ libs: Sample project library 
    ┣ res: Sample project resource file
    ┣ src: Sample application source
    ┗ doc: Sample application Javadoc
    lib
    ┣ bin
    ┃  ┣ ricoh-theta4j.0.2.0.jar: Library about operations of RICOH THETA
    ┃  ┗ r-exif4j.0.2.0.jar: Library that enables acquisition of EXIF information from spherical images shot using RICOH THETA
    ┣ src
    ┃  ┣ ricoh-theta4j: The source of ricoh-theta4j
    ┃  ┗ r-exif4j: The source of r-exif4j
    ┗ doc
       ┣ ricoh-theta4j: Javadoc for ricoh-theta4j
       ┗ r-exif4j: Javadoc for r-exif4j

----------------------------------------

* Required environment for development

    [About RICOH THETA]
      Dedicated RICOH THETA library that fulfills the following conditions.

      * Hardware
          RICOH THETA (2013 launch Model)
          RICOH THETA (Model:RICOH THETA m15)
      * Firmware
          Version 1.21 or higher
          (Method for checking and updating the firmware:  https://theta360.com/en/support/manual/content/pc/pc_05.html)


    [About the Development Environment for Sample Application]
      Operation of the sample application has been verified under the following conditions.

      * Verified operating environment
          Nexus 5 
      * Development/Build Environment
          Eclipse Luna
          Android SDK (API Level 19)

----------------------------------------

* How to Use

    [Operating the Sample Application]
        1. Import ricoh-theta-sample-for-android.0.2.0 as a project into Eclipse, and build it in the Android device.
        2. Connect the RICOH THETA to the Android device using Wi-Fi.
            (Usage instructions, connecting the camera to a smartphone: https://theta360.com/en/support/manual/content/prepare/prepare_06.html)
        3. Viewer section within the sample program is created using OpenGL ES2.0.
           Use a device that is compatible with the operation.
        4. The sample application can be operated

    [Using your own app with RICOH THETA SDK]
        1. Pass the classpaths of ricoh-theta4j.0.2.0.jar and r-exif4j.0.2.0.jar through your own app.
        2. Mount based on the sample application and information described below.

    [More detailed information]
        See the contents of the provided library source and Javadoc, as well as documents on the Internet for further information.

        https://developers.theta360.com/en/docs/

----------------------------------------

* Latest information

    The latest information is released on the "RICOH THETA Developers" website.

    https://developers.theta360.com/

----------------------------------------

* Troubleshooting

    FAQs are available on the forums.

    https://developers.theta360.com/en/forums/viewforum.php?f=6

----------------------------------------

* Trademarks

    The products and services described in this document are the trademarks or registered trademarks of their respective owners.

    * Android and Nexus are the trademarks or registered trademarks of Google Inc.
    * Eclipse is the trademark or registered trademark of Eclipse Foundation, Inc. in the United States and other countries.
    * Wi-Fi is the trademark of Wi-Fi Alliance.

    All other trademarks belong to their respective owners.

----------------------------------------

* Update History

    02/20/2015 0.3.0 Spherical viewer function added to the sample app
    11/06/2014 0.2.0 Documents are translated
    10/28/2014 0.1.0 Initial release
