package WindowStates;

import ApplicationDefaults.WindowPreferences;

public class MissionSelectPreferences extends WindowPreferences {
    public MissionSelectPreferences() {
        super(new MissionSelect(), WindowStateName.MISSION_SELECT,true);
    }
}

