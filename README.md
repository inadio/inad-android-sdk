# Inad SDK Example Project for Android
======================================
## About
inad.io is a dedicated incentive adnetwork which provides a win-win solution to easily get revenue and improve user satisfastion for your mobile app. For more information see [the website](http://inad.io/)

## Examples
This Android application project provides an example of the inad.io SDK integration. It is compiled with Android 4.0 (API level 15) and supports any device running this Android version or higher. When integrating the SDK with your application, please make sure to use the latest SDK version, which can be downloaded from our developers portal. Please also follow the integration manual which comes with the SDK.

## Download SDK

Use Gradle:
```groovy
compile 'io.inad.sdk:inad:1.1.5'{
  exclude group: 'glide-parent'
}
```
or Maven:
```xml
<dependency>
  <groupId>io.inad.sdk</groupId>
  <artifactId>inad</artifactId>
  <version>1.1.5</version>
  <type>pom</type>
</dependency>
```

## Integrate SDK
Get up and running in 4 easy steps:
###1) Implement the Offerwall Listener
The Inad SDK fires several events to inform you of ad availability. By implementing theOfferwallListener you will receive Offerwall events.
The SDK will notify your listener of all possible events listed below:
```java
OfferwallListener mOfferwallListener = new OfferwallListener() {

        /**
         * Invoked when the Offerwall is prepared and ready to be shown to the user
         */
        @Override
        public void onOfferwallInitSuccess() {}

        /**
         * Invoked when the Offerwall does not load
         */
        @Override
        public void onOfferwallInitFail(AdError adError) {}

        /**
         * Invoked when the Offerwall successfully loads for the user,
         * after calling the 'showOfferwall' method
         */
        @Override
        public void onOfferwallOpened() {}

        /**
         * Invoked when the method 'showOfferWall' is called and the OfferWall fails to load.
         * @param adError - A AdError Object which represents the reason of 'showOfferwall' failure.
         */
        @Override
        public void onOfferwallShowFail(AdError adError) {}

        /**
         * Invoked each time the user completes an Offer.
         * Award the user with the credit amount corresponding to the value of the ‘credits’
         * parameter.
         * @param credits - The number of credits the user has earned.
         * @param totalCredits - The total number of credits ever earned by the user.
         * @param totalCreditsFlag - In some cases, we won’t be able to provide the exact
         * amount of credits since the last event (specifically if the user clears
         * the app’s data). In this case the ‘credits’ will be equal to the ‘totalCredits’,
         * and this flag will be ‘true’.
         * @return boolean - true if you received the callback and rewarded the user,
         * otherwise false.
         */
        @Override
        public boolean onOfferwallAdCredited(BigDecimal credits, BigDecimal totalCredits, boolean totalCreditsFlag) {
            return false;
        }

        /**
         * Invoked when the method 'getOfferWallCredits' fails to retrieve the user's credit balance info.
         * @param adError - A AdError object which represents the reason of 'getOffereallCredits' failure.
         */
        @Override
        public void onGetOfferwallCreditsFail(AdError adError) {}

        /**
         * Invoked when the user is about to return to the application after closing the Offerwall.
         */
        @Override
        public void onOfferwallClosed() {}
    }
```
###2) Initialize the Offerwall Unit
Once the Offerwall Ad Unit is initialized, you will able to call functions on it. We recommend initializing the Offerwall on application launch.
```java
public class YourActivity extends Activity {
    public static final String AdUniqueCode = "YOUR_AD_UNIQUE_CODE";
    private InAdNetwork mInAdNetworkInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yourxml);

        // your call back data, it could be user id.
        String callBackData = "UserID";
        // create the inad instance - this should be called when the activity starts
        mInAdNetworkInstance = InAdNetwork.getInstance(getBaseContext());
        // set the Inad offerwall listener
        // should call before init offerwall
        mInAdNetworkInstance.setOfferwallListener(mOfferwallListener);
        // init the inad offerwall
        mInAdNetworkInstance.initOfferwall(this, AdUniqueCode, callBackData);
        // or use below method to enable debug mode which will show many test offerwall apps.
        // mInAdNetworkInstance.initOfferwall(this, AdUniqueCode, callBackData, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // call the inad onResume method
        if (mInAdNetworkInstance != null)
            mInAdNetworkInstance.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // call the inad onPause method
        if (mInAdNetworkInstance != null)
            mInAdNetworkInstance.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // call the inad onDestroy method
        if (mInAdNetworkInstance != null)
            mInAdNetworkInstance.onDestroy(this);
    }
  }
```
###3) Present the Offerwall
After you receive the onOfferwallInitSuccess callback, you are ready to show the Offerwall to your users. When you want to show the Offerwall (typically done after a user clicks on some in-app button), you do so by calling the showOfferwall method on mInAdNetworkInstance:
```java
  // check if offerwall is available
    if (mInAdNetworkInstance.isOfferwallAvailable()) {
        // show offerwall
        mInAdNetworkInstance.showOfferwall();
    }
```
###4) Reward the User
Inad.io supports two methods to reward your users. Select one of the following:
  ####1) Server-Side Callbacks:
  The default setting in your Inad account notifies you of user’s completions or rewards via the callback within the client of your app. Alternatively, you can turn on server-to-server callbacks to receive notifications to your back-end server. Once you select server-to-server callbacks you will not receive client-side notifications.
  We recommend turning on server-to-server callbacks for Offerwall instead of client-side callbacks, as the authenticity of the callback can be verified. With server-to-server callbacks, you will have better control over the rewarding process as the user navigates out of your app to complete the offer.
  ####2) Client-Side Callbacks
  The callback method is:
  ```java
      @Override
          public boolean onOfferwallAdCredited(BigDecimal credits, BigDecimal totalCredits, boolean totalCreditsFlag) {
              // do your code
              return false;
          }
  ```

## Documentation

Check out our [developers site](http://inad.io/Home/Publisher)
for documentation on using the Inad SDK.

## GitHub issue tracker

Please report bugs or issues to [GitHub's issue tracker](https://github.com/inadio/inad-android-sdk/issues).

## License

[Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html)

## Contact
For any question or assistance, please contact us at support@inad.io
