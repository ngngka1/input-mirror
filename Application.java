import sender.Sender;
import receiver.Receiver;

import java.util.Scanner;


public class Application {
    private static String ipAddress = "";
    private static int port = 443;
    private static final boolean dev = true;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you the sender or receiver? (s/r): ");
        String role = scanner.nextLine().trim().toLowerCase();

        boolean isSender = role.equals("s");
        boolean isReceiver = role.equals("r");
        Sender sender;
        Receiver receiver;

        if (!dev) {
            if (isSender) {
                System.out.print("Enter the receiver's IP address: (skip to use )");
                ipAddress = scanner.nextLine().trim();

                System.out.print("Enter the port number: (skip to use 443)");
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    port = Integer.parseInt(input);
                }

            } else if (isReceiver) {
                System.out.print("Enter the port number: (skip to use 443)");
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    port = Integer.parseInt(input);
                }
            } else {
                System.out.println("Invalid role. Please enter 's' for sender or 'r' for receiver.");
            }
        }
        scanner.close();
        if (isSender) {
            sender = new Sender(ipAddress, port);
            sender.start();
        } else if (isReceiver) {
            receiver = new Receiver(port);
            receiver.start();
        }
    }
}
