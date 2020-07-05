package dcv.finaltest.hmiapplication;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import dcv.finaltest.configuration.IConfigurationService;
import dcv.finaltest.property.IPropertyService;
import dcv.finaltest.property.PropertyEvent;

public class SecondFragment extends Fragment {
    private static final String TAG = "SecondFragment";

    BarChart mBarChart;
    Switch mDistanceUnitView;
    Switch mConsumptionUnitView;
    Button mResetView;
    EditText mDistanceValueView;
    ImageView mErrorImge;

    List<Double> lsData;
    Timer timer;
    TimerTask timerTask;
    Handler mHandler;

    IServiceInterface mStudentService;
    MainActivity mainActivity;
    IConfigurationService mConfigurationService;
    IPropertyService mPropertyService;

    HMIListener mHmiListener = new HMIListener();


    class HMIListener extends IHMIListener.Stub {

        @Override
        public void onDistanceUnitChanged(final int distanceUnit) throws RemoteException {
            // 0:KM, 1:MILE
            Log.d(TAG, "onDistanceUnitChanged: " + distanceUnit);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (distanceUnit == 0)// KM
                        mDistanceUnitView.setChecked(false);
                    else if (distanceUnit == 1) //MILE
                        mDistanceUnitView.setChecked(true);
                }
            });
        }

        @Override
        public void onDistanceChanged(final double distance) throws RemoteException {
            Log.d(TAG, "onDistanceChanged: " + distance);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDistanceValueView.setText("" + distance);
                }
            });
        }

        @Override
        public void OnConsumptionUnitChanged(final int consumptionUnit) throws RemoteException {
            // 0:KM/L, 1:L/100KM
            Log.d(TAG, "OnConsumptionUnitChanged: " + consumptionUnit);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (consumptionUnit == 0)// KM/L
                        mConsumptionUnitView.setChecked(false);
                    else if (consumptionUnit == 1) //L/100KM
                        mConsumptionUnitView.setChecked(true);
                }
            });
        }

        @Override
        public void onConsumptionChanged(double[] consumptionList) throws RemoteException {
            String msgLog = "onConsumptionChanged: ";
            final List<Double> lsData = new ArrayList<Double>();
            for (int i = 0; i  < consumptionList.length; i ++) {
                lsData.add(consumptionList[i]);
                msgLog += consumptionList[i] + ", ";
            }
            Log.d(TAG, msgLog);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateData(lsData);
                }
            });
        }

        @Override
        public void onError(final boolean isError) throws RemoteException {
            Log.d(TAG, "onError: " + isError);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                   if (isError) {
                        mErrorImge.setVisibility(View.VISIBLE);
                        mDistanceValueView.setEnabled(false);
                        mDistanceUnitView.setEnabled(false);
                        mConsumptionUnitView.setEnabled(false);
                   } else {
                       mErrorImge.setVisibility(View.INVISIBLE);
                       mDistanceValueView.setEnabled(true);
                       mDistanceUnitView.setEnabled(true);
                       mConsumptionUnitView.setEnabled(true);
                   }
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lsData = new ArrayList<>();
        Random ran = new Random();
        for (int i = 0; i < 15; i ++)
            lsData.add(ran.nextDouble() * 10);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    void updateData(List<Double> lsData ) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < lsData.size(); i ++) {
            entries.add(new BarEntry(((float) lsData.get(i).doubleValue()), i));
        }

        BarDataSet bardataset = new BarDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("15");
        labels.add("14");
        labels.add("13");
        labels.add("12");
        labels.add("11");
        labels.add("10");
        labels.add("9");
        labels.add("8");
        labels.add("7");
        labels.add("6");
        labels.add("5");
        labels.add("4");
        labels.add("3");
        labels.add("2");
        labels.add("1");

        BarData data = new BarData(labels, bardataset);
        mBarChart.setData(data); // set the data and list of labels into chart
        bardataset.setColor(Color.GRAY);
        mBarChart.animateY(0);
    }

    private View.OnClickListener mResetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onResetClickListener ");
            if (mPropertyService != null) {
                PropertyEvent event = new PropertyEvent(IPropertyService.PROP_RESET, PropertyEvent.STATUS_AVAILABLE, 0, true);
                try {
                    mPropertyService.setProperty(IPropertyService.PROP_RESET, event);
                } catch (RemoteException e) {
                    Log.e(TAG, "onResetClickListener error" + e.getMessage());
                }

            }
        }
    };

    private CompoundButton.OnCheckedChangeListener mDistanceUnitChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "mDistanceUnitChangeListener: " + isChecked);

            if (mStudentService != null) {
                try {
                    mStudentService.setDistanceUnit(isChecked ? 1 : 0);
                } catch (RemoteException e) {
                    Log.e(TAG, "DistanceUnitChangeListener error" + e.getMessage());
                }
            }

        }
    };

    private CompoundButton.OnCheckedChangeListener mConsumptionUnitChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "mConsumptionUnitChangeListener: " + isChecked);

            if (mPropertyService != null) {
                try {
                    mStudentService.setConsumptionUnit(isChecked ? 1 : 0);
                } catch (RemoteException e) {
                    Log.e(TAG, "mConsumptionUnitChangeListener error" + e.getMessage());
                }
            }

        }
    };

    private void updateCapability() {
        try {
            TestCapability capability = mStudentService.getCapability();
            if (capability.isConsumptionSupported()) {
                mConsumptionUnitView.setEnabled(true);
            }
            if (capability.isDistanceSupported()) {
                mDistanceUnitView.setEnabled(true);
            }
            if (capability.isResetSupported()) {
                mResetView.setEnabled(true);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "updateCapability falied");
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBarChart = (BarChart) view.findViewById(R.id.barchart);
        mDistanceUnitView = (Switch) view.findViewById(R.id.distanceUnit);
        mConsumptionUnitView = (Switch) view.findViewById(R.id.consumptionUnit);
        mDistanceValueView = (EditText) view.findViewById(R.id.distanceValue);
        mResetView = (Button) view.findViewById(R.id.resetBtn);
        mErrorImge = (ImageView)  view.findViewById(R.id.errorImg);

        mErrorImge.setVisibility(View.INVISIBLE);
        mDistanceValueView.setEnabled(false);
        mDistanceUnitView.setEnabled(false);
        mConsumptionUnitView.setEnabled(false);

        mHandler = new Handler(Looper.getMainLooper());
        mainActivity = (MainActivity) getActivity();
        mStudentService = mainActivity.getStudentService();
        mPropertyService = mainActivity.getPropertyService();
        mConfigurationService = mainActivity.getConfigurationService();
        updateCapability();

        mResetView.setOnClickListener(mResetClickListener);
        mDistanceUnitView.setOnCheckedChangeListener(mDistanceUnitChangeListener);
        mConsumptionUnitView.setOnCheckedChangeListener(mConsumptionUnitChangeListener);

        try {
            mStudentService.registerListener(mHmiListener);
        } catch (RemoteException e) {
            Toast.makeText(mainActivity.getApplicationContext(), "registerListener to student service error", Toast.LENGTH_LONG).show();
        }


//        timer = new Timer();
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        synchronized (lsData) {
//                            List<Double> old = new ArrayList<>(lsData);
//                            lsData.clear();
//                            for (int i = 0; i < 14; i ++) {
//                                lsData.add(old.get(i + 1));
//                            }
//                            Random ran = new Random();
//                            lsData.add(ran.nextDouble() * 10);
//                            updateData(lsData);
//                        }
//                    }
//                });
//            }
//        };
//        timer.schedule(timerTask, 500, 5000);
    }

}