package com.iot.cardgame;

/**
 * Created by iao on 15. 5. 28.
 */
public class GameTimeThread extends Thread {
    private int time;

    public GameTimeThread(int time) {
        this.time = time;
    }

    @Override
    public void run() {
        super.run();

    }
}
