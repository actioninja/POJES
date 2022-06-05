package net.criticalaction.pojes.test;

import net.criticalaction.pojes.EventReceiver;

public class TestReceiver {
    public int count = 0;

    @EventReceiver
    public void increaseCount(TestEvent testEvent) {
        count += 1;
        testEvent.count += 1;
    }
}
