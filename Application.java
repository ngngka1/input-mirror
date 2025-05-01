import controller.ConnectionController;
import receiver.ReceiverController;
import sender.SenderController;
import device_searcher.BroadcastHandler;
import device_searcher.ConnectionRequestHandler;
import device_searcher.DeviceSearcher;
import types.Device;
import utils.HotkeyManager;
import utils.ThreadedScanner;
import utils.CloseableInterrupter;

import java.net.*;
import java.util.List;

//import static device_searcher.DeviceSearcher.searchForDevices;

public class Application {
    private static boolean running = true;
    private static boolean connected = false;

    public static final int TCP_PORT = 6789; // TCP port for connection for this app
    public static final int UDP_PORT = 9876; // UDP port used for this app (should be same for all users, so
    private static final String BROADCAST_MESSAGE = "DISCOVER_DEVICE";
    private static final String DEVICE_INFO_PREFIX = "DEVICE_INFO"; // device information will be passed in the following format:
                                                                    // DEVICE_INFO:hostname:hostAddress:hostTcpPort

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

//    private static void clearOutput() {
//        try {
//            Runtime.getRuntime().exec("cls");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        running = true;
        connected = false;

        // threaded scanner so user can provide inputs whenever they want, instead of needing to wait for
        // application's prompt before inputting, also allowing for
        ThreadedScanner threadedScanner = new ThreadedScanner();
        new Thread(threadedScanner).start();

        // static background jobs, do not need a reference
        BroadcastHandler.init(UDP_PORT, TCP_PORT, BROADCAST_MESSAGE, DEVICE_INFO_PREFIX);
        ConnectionRequestHandler.init(TCP_PORT);

        DeviceSearcher deviceSearcher = new DeviceSearcher(BROADCAST_MESSAGE, DEVICE_INFO_PREFIX);
        ConnectionController connectionController = new ConnectionController(ConnectionRequestHandler.getConnectionRequests());


        while (running) {
            try {

                prompt();
                String input = threadedScanner.getInput().trim().toLowerCase();
                int option;
//                clearOutput();
                try {
                    option = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Please input a number!");
                    continue;
                }

                switch (option) {
                    case 0: {
                        boolean searching = true;
                        while (searching) {
                            deviceSearcher.initAvailableDevices();
                            deviceSearcher.printLocalDevicesNonBlocking(UDP_PORT);
                            System.out.println();

                            boolean retry = false;
                            int index = -1;
                            while (true) {
                                input = threadedScanner.getInput().trim().toLowerCase();
                                if (input.equals("r")) {
                                    retry = true;
                                    break;
                                }
                                if (input.equals("n")) {
                                    searching = false;
                                    break;
                                }

                                try {
                                    index = Integer.parseInt(input);
//                                    if (index < 0 || index >= devices.size()) {
//                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Please input a number!");
                                    continue;
                                }
                                break;
                            }
                            if (retry) {continue;}
                            deviceSearcher.stopDevicePrinter();
                            if (!searching) {break;}

                            Device device;
                            try {
                                device = deviceSearcher.getAvailableDevices().get(index);
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("Invalid index!");
                                continue;
                            }
    //                        clearOutput();
                            Socket clientSocket = connectionController.connect(device);
                            if (clientSocket != null) {
                                SenderController sender = new SenderController(clientSocket);
                                HotkeyManager.hook(sender);
                                sender.start();
                            }
                            searching = false;
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

                            input = threadedScanner.getInput().trim().toLowerCase();
                            if (input.equals("r")) {
//                                clearOutput();
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

//                            clearOutput();
                            Socket clientSocket = connectionController.acceptConnection(devices.get(index));
                            if (clientSocket != null) {
                                ReceiverController receiver = new ReceiverController(clientSocket);
                                receiver.start();
                            }
                            break;
                        }
                        break;
                    }
                    case 2: {
                        running = false;
                        System.out.println("terminating...");
                        threadedScanner.terminate();
                        BroadcastHandler.terminate();
                        ConnectionRequestHandler.terminate();
                        CloseableInterrupter.closeAll();
                        break;
                    }
                    default: {
                        System.out.println("Invalid option!");
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Unexpected Exception: " + e.toString());
            }
        }
    }
}
