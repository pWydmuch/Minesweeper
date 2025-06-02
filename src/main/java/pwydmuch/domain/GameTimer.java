package pwydmuch.domain;

import java.util.function.Consumer;

public class GameTimer {
    private final Consumer<Integer> onTick;
    private int time;
    private boolean isRunning;

    public GameTimer(Consumer<Integer> onTick) {
        this.onTick = onTick;
        this.time = 0;
        this.isRunning = false;
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            new Thread(() -> {
                while (isRunning) {
                    try {
                        Thread.sleep(1000);
                        time++;
                        onTick.accept(time);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }).start();
        }
    }

    public void stop() {
        isRunning = false;
    }

    public int getTime() {
        return time;
    }

    public void reset() {
        time = 0;
        onTick.accept(time);
    }
} 