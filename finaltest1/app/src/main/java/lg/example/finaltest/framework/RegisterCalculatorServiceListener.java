package lg.example.finaltest.framework;

import dcv.finaltest.hmiapplication.IHMIListener;

public interface RegisterCalculatorServiceListener {
    void onRegisterCalculatorSuccess(IHMIListener listener);
    void unRegisterCalculatorSuccess();
}
