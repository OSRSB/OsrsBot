package net.runelite.rsb.botLauncher;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Scanner;

import static net.runelite.rsb.botLauncher.Application.*;

@Slf4j
public class CLIHandler {

    /**
     * Starts a new thread which handles the command line arguments passed while the program is running.
     */
    public static void handleCLI() {
        Scanner input = new Scanner(System.in);
        new Thread(() -> {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] command = line.split(" ");
                log.debug("CLI command: {}", Arrays.toString(command));
                switch (command[0].toLowerCase()) {
                    case "runscript":
                        if (command.length < 4) {
                            log.warn("Usage: runscript <botIndex> <account> <scriptName>");
                            break;
                        }
                        int runIdx = parseBotIndex(command[1]);
                        if (runIdx < 0) break;
                        BotLiteInterface runBot = getBotAt(runIdx);
                        if (runBot == null) break;
                        runBot.runScript(command[2], command[3]);
                        break;
                    case "stopscript":
                        if (command.length < 2) {
                            log.warn("Usage: stopscript <botIndex>");
                            break;
                        }
                        int stopIdx = parseBotIndex(command[1]);
                        if (stopIdx < 0) break;
                        BotLiteInterface stopBot = getBotAt(stopIdx);
                        if (stopBot == null) break;
                        stopBot.stopScript();
                        break;
                    case "addbot":
                        addBot(true);
                        break;
                    case "checkstate":
                        for (BotLiteInterface botInstance : bots) {
                            log.info("Bot classloader: {}", botInstance.getClass().getClassLoader());
                        }
                        break;
                    default:
                        log.warn("Unknown command: {}. Valid commands: runscript, stopscript, addbot, checkstate", command[0]);
                        break;
                }
            }
        }, "CLI-Handler").start();
    }

    private static int parseBotIndex(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            log.warn("Bot index '{}' is not a valid integer", raw);
            return -1;
        }
    }

    private static BotLiteInterface getBotAt(int index) {
        BotLiteInterface[] bots = Application.getBots();
        if (index < 0 || index >= bots.length) {
            log.warn("Bot index {} is out of range (0-{})", index, bots.length - 1);
            return null;
        }
        return bots[index];
    }
}
