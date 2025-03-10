package WindowStates;

import ApplicationDefaults.WindowPreferences;

public class LoginPreferences extends WindowPreferences {
    public LoginPreferences() {
        super(new Login(), WindowStateName.LOGIN,true);
    }
}
