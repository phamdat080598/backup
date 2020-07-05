package dcv.finaltest.hmiapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {
    private EditText mActionView;
    private EditText mPackageView;
    private TextView mLogging;
    private Button mButton;
    private MainActivity.IActivityCallback myCallback = new MainActivity.IActivityCallback() {
        @Override
        public void onOnConnected(boolean isSuccess) {
            if (isSuccess) {
                mLogging.append("Service connected...\n");
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            } else {
                mLogging.append("Service connected failed!!!!!\n");
            }

        }

        @Override
        public void onDisconnected() {

        }
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ((mActionView.getText().length() > 0) &&(mPackageView.getText().length() > 0))
                mButton.setEnabled(true);
            else
                mButton.setEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActionView = view.findViewById(R.id.actionID);
        mPackageView = view.findViewById(R.id.packageID);
        mActionView.addTextChangedListener(watcher);
        mPackageView.addTextChangedListener(watcher);
        mLogging = view.findViewById(R.id.logging);
        mButton = view.findViewById(R.id.button_first);
        mButton.setEnabled((mActionView.getText().length() > 0) &&(mPackageView.getText().length() > 0));
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                mLogging.append("Start connecting to " + mPackageView.getText().toString() + " service...\n" );
                activity.connectToStudentService(mActionView.getText().toString(), mPackageView.getText().toString(), myCallback);
            }
        });
    }
}