package net.runelite.rsb.botLauncher;

import java.util.Arrays;
import java.util.Scanner;

import static net.runelite.rsb.botLauncher.Application.*;

public class CLIHandler {

    /**
     * Starts a new thread which handles the command line arguments passed while the program is running.
     * The switch case provides an easy-to-read implementation in which commands are available for usage.
     */
    public static void handleCLI() {
        new Thread(() -> {
            Scanner input = new Scanner(System.in);
            while(input.hasNext()) {
                String[] command = input.nextLine().split(" ");
                System.out.println(Arrays.toString(command));
                switch (command[0].toLowerCase()) {
                    case "runScript":
                        BotLiteInterface botInterface = Application.getBots()[Integer.parseInt(command[1])];
                        botInterface.runScript(command[2], command[3]);
                        break;
                    case "addBot":
                        addBot(true);
                        break;
                    case "checkState":
                        for (BotLiteInterface botInstance : bots) {
                            System.out.println(botInstance.getClass().getClassLoader());
                        }
                        break;
                    default:
                        break;
                }
            }
        }).start();
    }
}
