package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Quest extends Component {
    private final String type;

    // seconds of work for a /single/ actor.
    public float workRemaining = 4;
    public int gold=0;
    public boolean dangerous=false;

    public Quest(String type) {
        this.type = type;
    }
}
