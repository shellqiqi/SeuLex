package seu;

import seu.io.LexFile;

public class App {
    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.out.println("Please input a file.");
                return;
            }
            LexFile lexFile = new LexFile(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
