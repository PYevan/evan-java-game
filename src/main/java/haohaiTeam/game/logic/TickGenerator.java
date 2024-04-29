package haohaiTeam.game.logic;

import haohaiTeam.game.input.CommandListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TickGenerator {
    private static ScheduledExecutorService executor;
    private static int tickRate;
    private static CommandListener commandListener;

    public TickGenerator() {
        tickRate = 100;
        executor = Executors.newSingleThreadScheduledExecutor(); // Create a thread for tick time tracking
    }

    public static void start() {
        executor.scheduleAtFixedRate(() -> {
            if (commandListener != null) {
                commandListener.onTick();
            }
        }, 0, tickRate, TimeUnit.MILLISECONDS);
    }

    public static void setCommandListener(CommandListener commandListener) {
        TickGenerator.commandListener = commandListener;
    }

    public void stop() {
        executor.shutdown();
    }

    public void addTickListener(Runnable listener) {
        // Implement if needed
    }


    public void removeTickListener(Runnable listener) {
        // Implement if needed
    }
}
