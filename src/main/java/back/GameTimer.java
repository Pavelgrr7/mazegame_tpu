package back;

import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;

public class GameTimer {
    private Timer timer;
    private long delay;
    private long startTime;
    private boolean isRunning = false;

    public GameTimer(long delay) {
        this.delay = delay;
        this.timer = new Timer("GameTimer");
        start();
    }

    private void start() {
        if (!isRunning) {
            this.startTime = System.currentTimeMillis();
            createTimer();
            isRunning = true;
        }
    }

    private void createTimer() {
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("Task completed: " + new Date());
                isRunning = false;
            }
        };
        timer.schedule(task, delay);
    }

    public long getTimeRemaining() {
        if (isRunning) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            return Math.max(delay - elapsedTime, 0);
        } else {
            return -1;
        }
    }

    public String getTimeRemainingf() {

        if (isRunning) {
            return getTimef();
        } else {
            isRunning = false;
            return "00:00";
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    private String getTimef() {
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        long startTimeInSeconds = startTime / 1000;
        long elapsedTime = currentTimeInSeconds - startTimeInSeconds;
        long remainingTime = Math.max((delay / 1000) - elapsedTime, 0);

        long minutes = remainingTime / 60;
        long seconds = remainingTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
