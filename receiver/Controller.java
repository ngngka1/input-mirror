package receiver;

public class Controller {
    public static void redirect(String[] parsedData) {
        if (parsedData.length > 1) {
            CursorController.moveTo(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1]));
        } else {
            KeyboardController.pressHotkey(parsedData[0]);
        }
    }
}
