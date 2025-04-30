import controller.ConnectionController;
import receiver.ReceiverController;
import sender.SenderController;
import device_searcher.BroadcastHandler;
import device_searcher.ConnectionRequestHandler;
import device_searcher.DeviceSearcher;
import types.Device;
import utils.InputProvider;
import utils.CloseableInterrupter;

import java.io.IOException;
import java.net.*;
import java.util.List;

import static device_searcher.DeviceSearcher.searchForDevices;

public class Application {
    private static boolean running = true;
    private static boolean connected = false;

    public static final int TCP_PORT = 6789; // TCP port for connection for this app
    public static final int UDP_PORT = 9876; // UDP port used for this app (should be same for all users, so
    private static final String BROADCAST_MESSAGE = "DISCOVER_DEVICE";
    private static final String DEVICE_INFO_PREFIX = "DEVICE_INFO:";

    // indicates whether this device is already connected to another device currently
    public static boolean getConnectionState() {
        return connected;
    }
    public static boolean getRunningState() {
        return running;
    }

    private static void prompt()
    {
        System.out.println("Choose options: (enter '1' for option 1, vice versa) ");
        System.out.println("0. search active local devices");
        System.out.println("1. view connection requests ");
        System.out.println("2. exit");
    }

    private static void clearOutput() {
        try {
            Runtime.getRuntime().exec("cls");
        } catch (IOException e) {

        }
    }

    public static void main(String[] args) {
        running = true;
        connected = false;


        DeviceSearcher.init(BROADCAST_MESSAGE, DEVICE_INFO_PREFIX);
        // background jobs, do not need a reference
        InputProvider.init();
        BroadcastHandler.init(UDP_PORT, TCP_PORT, BROADCAST_MESSAGE, DEVICE_INFO_PREFIX);
        ConnectionRequestHandler.init(TCP_PORT);


        ConnectionController connectionController = new ConnectionController(ConnectionRequestHandler.getConnectionRequests());


        while (running) {
            prompt();
            String input = InputProvider.getInput().trim().toLowerCase();
            int option;
            clearOutput();
            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please input a number!");
                continue;
            }

            switch (option) {
                case 0: {
                    while (true) {
                        List<Device> devices = searchForDevices(UDP_PORT);
                        System.out.println();
                        if (devices.isEmpty()) {
                            System.out.println("No active local devices found. (input 'r' to search again, 'n' to quit searching)");
                        } else {
                            System.out.println("would you like to connect to any of these devices? (if yes, input their index (e.g. '3'), if no, input 'r' to search again, or 'n' to quit searching.)");
                        }
                        input = InputProvider.getInput().trim().toLowerCase();
                        if (input.equals("r")) {
                            clearOutput();
                            continue;
                        }

                        int index;
                        try {
                            index = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            break;
                        }

                        if (index < 0 || index >= devices.size()) {
                            System.out.println("Invalid index!");
                            break;
                        }
                        clearOutput();
                        Socket clientSocket = connectionController.connect(devices.get(index));
                        SenderController sender = new SenderController();
                        sender.start(clientSocket);
                        break;
                    }
                    break;
                }
                case 1: {
                    while (true) {
                        ConnectionRequestHandler.clearClosedRequests();
                        List<Device> devices = connectionController.getRequestedDevices();
                        if (devices.isEmpty()) {
                            System.out.println("No requests yet (input 'r' to reload, or 'n' to return)");
                        } else {
                            System.out.println("would you like to accept any of these connections? (if yes, input their index (e.g. '3'), if no, input 'r' to reload, or 'n' to return.");
                        }

                        input = InputProvider.getInput().trim().toLowerCase();
                        if (input.equals("r")) {
                            clearOutput();
                            continue;
                        }

                        int index;
                        try {
                            index = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            break;
                        }

                        if (index < 0 || index >= devices.size()) {
                            System.out.println("Invalid index!");
                            break;
                        }

                        clearOutput();
                        Socket clientSocket = connectionController.acceptConnection(devices.get(index));
                        ReceiverController receiver = new ReceiverController();
                        receiver.start(clientSocket);
                        break;
                    }
                    break;
                }
                case 2: {
                    running = false;
                    System.out.println("terminating...");
                    InputProvider.terminate();
                    BroadcastHandler.terminate();
                    ConnectionRequestHandler.terminate();
                    CloseableInterrupter.closeAll();
                    break;
                }
            }
        }


    }
}
