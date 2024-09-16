package components;

import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;

public class GameTimer {
    private Timer timer;
    private long delay;
    private long startTime;
    private boolean isRunning;

    public GameTimer(long delay) {
        this.delay = delay;
        this.timer = new Timer("GameTimer");
        this.isRunning = false;
    }

    public void start() {
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
            return 0;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getTimef() {
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        long startTimeInSeconds = startTime / 1000;
        long elapsedTime = currentTimeInSeconds - startTimeInSeconds;
        long remainingTime = Math.max((delay / 1000) - elapsedTime, 0); // Приводим delay к секундам

        long minutes = remainingTime / 60;
        long seconds = remainingTime % 60;

        // Форматируем вывод, чтобы секунды всегда отображались с двумя цифрами
        return String.format("%d:%02d", minutes, seconds);
    }
}