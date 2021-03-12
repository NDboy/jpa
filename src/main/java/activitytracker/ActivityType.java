package activitytracker;

import java.util.Random;

public enum ActivityType {
    BIKING(0), HIKING(1), RUNNING(2), BASKETBALL(3);

    private int value;

    ActivityType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ActivityType randomActivityType() {
        Random random = new Random();
        return ActivityType.values()[random.nextInt(4)];
    }

}
