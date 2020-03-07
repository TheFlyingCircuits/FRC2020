package frc.robot.util;

import lombok.Getter;

public enum Buttons {
    TRIGGER(1),
    TOP_CENTER(2),
    TOP_RIGHT(3),
    TOP_LEFT(4); // TODO verify this


    @Getter public final int id;

    Buttons(int id) {
        this.id = id;
    }
}
