package io.inad.example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import io.inad.InAdNetwork;
import io.inad.ads.AdError;
import io.inad.sdk.OfferwallListener;

/**
 * Created by Marlon on 8/16/2016.
 */
public class DemoActivity extends Activity implements OfferwallListener {

    private final String TAG = "DemoActivity";
    public static final String AdUniqueCode = "FAD24DB4-53E1-4A4F-A968-CEAAFBB64033"; //example
    public static final String CallBackData = "CallBackData"; // your call back data

    private TextView mOfferwallCredit;
    private Button mOfferwallButton;
    private InAdNetwork mInAdNetworkInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);
        mOfferwallCredit = (TextView) findViewById(R.id.offerwall_credits);
        mOfferwallButton = (Button) findViewById(R.id.button_offerwall);

        // create the inad instance - this should be called when the activity starts
        mInAdNetworkInstance = InAdNetwork.getInstance(getBaseContext());
        // set the Inad offerwall listener
        // should call before init offerwall
        mInAdNetworkInstance.setOfferwallListener(this);
        // init the inad offerwall
        // mInAdNetworkInstance.initOfferwall(this, AdUniqueCode, CallBackData);
        // or use below method to enable debug mode
        mInAdNetworkInstance.initOfferwall(this, AdUniqueCode, CallBackData, true);

        mOfferwallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if offerwall is available
                if (mInAdNetworkInstance.isOfferwallAvailable()) {
                    // show offerwall
                    mInAdNetworkInstance.showOfferwall();
                }
            }
        });
    }

    /**
     * Handle the button state according to the status of the inad products
     */
    private void updateButtonsState() {
        if (mInAdNetworkInstance != null) {
            handleOfferwallButtonState(mInAdNetworkInstance.isOfferwallAvailable());
        } else {
            handleOfferwallButtonState(false);
        }
    }

    /**
     * Set the inad offerwall button state according to the product's state
     *
     * @param available if the offerwall is available
     */
    public void handleOfferwallButtonState(final boolean available) {
        final String text;
        final int color;
        if (available) {
            color = Color.BLUE;
            text = getResources().getString(R.string.show) + " " + getResources().getString(R.string.offerwall);
        } else {
            color = Color.BLACK;
            text = getResources().getString(R.string.initializing) + " " + getResources().getString(R.string.offerwall);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOfferwallButton.setTextColor(color);
                mOfferwallButton.setText(text);
                mOfferwallButton.setEnabled(available);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // call the inad onResume method
        if (mInAdNetworkInstance != null)
            mInAdNetworkInstance.onResume(this);
        updateButtonsState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // call the inad onPause method
        if (mInAdNetworkInstance != null)
            mInAdNetworkInstance.onPause(this);
        updateButtonsState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // call the inad onDestroy method
        if (mInAdNetworkInstance != null)
            mInAdNetworkInstance.onDestroy(this);
    }

    // --------- Inad Offerwall Listener ---------

    @Override
    public void onOfferwallInitSuccess() {
        // called when the offerwall has initiated successfully
        Log.d(TAG, "onOfferwallInitSuccess");
        handleOfferwallButtonState(true);
    }

    @Override
    public void onOfferwallInitFail(AdError adError) {
        // called when the offerwall has failed to init
        // you can get the error data by accessing the adError object
        // adError.getErrorCode();
        // adError.getErrorMessage();
        Log.d(TAG, "onOfferwallInitFail" + " " + adError);
    }

    @Override
    public void onOfferwallOpened() {
        // called when the offerwall has opened
        Log.d(TAG, "onOfferwallOpened");
    }

    @Override
    public void onOfferwallShowFail(AdError adError) {
        // called when the offerwall failed to show
        // you can get the error data by accessing the adError object
        // adError.getErrorCode();
        // adError.getErrorMessage();
        Log.d(TAG, "onOfferwallShowFail" + " " + adError);
    }

    @Override
    public boolean onOfferwallAdCredited(BigDecimal credits, BigDecimal totalCredits, boolean totalCreditsFlag) {
        mOfferwallCredit.setText("Credits: " + credits.longValue());
        Toast.makeText(getBaseContext(), "Credits: " + credits.longValue(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "onOfferwallAdCredited" + " credits:" + credits + " totalCredits:" + totalCredits + " totalCreditsFlag:" + totalCreditsFlag);
        return true;
    }

    @Override
    public void onGetOfferwallCreditsFail(AdError adError) {
        // you can get the error data by accessing the adError object
        // adError.getErrorCode();
        // adError.getErrorMessage();
        Log.d(TAG, "onGetOfferwallCreditsFail" + " " + adError);
    }

    @Override
    public void onOfferwallClosed() {
        // called when the offerwall has closed
        Log.d(TAG, "onOfferwallClosed");
    }
}
