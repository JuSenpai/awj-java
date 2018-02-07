package project.app;

import project.image.Grayscale;
import project.system.ClipboardFile;
import project.system.WinRegistry;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Application {
    public static final String APP_REGISTRY_KEY_LOCATION = "SOFTWARE\\Classes\\SystemFileAssociations\\image\\shell\\CopyGrayscale";
    public static final String APP_COMMAND = "\"C:\\ProgramData\\Oracle\\Java\\javapath\\javaw.exe\" -jar \"" + System.getProperty("user.dir") + "\\out\\artifacts\\AWJProject_jar\\AWJProject.jar\" \"%1\"";

    public void init() throws Exception {
        // creates the key for the option in the windows context menu
        WinRegistry.createKey(WinRegistry.HKEY_CURRENT_USER, Application.APP_REGISTRY_KEY_LOCATION);
        WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, Application.APP_REGISTRY_KEY_LOCATION, "", "Copy Grayscale To Clipboard");
        WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, Application.APP_REGISTRY_KEY_LOCATION, "Icon", System.getProperty("user.dir") + "\\src\\gray-scale-icon-md.ico");

        // creates the command key
        WinRegistry.createKey(WinRegistry.HKEY_CURRENT_USER, Application.APP_REGISTRY_KEY_LOCATION + "\\command");
        WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, Application.APP_REGISTRY_KEY_LOCATION + "\\command", "", Application.APP_COMMAND);

        this.sendAppToTrayAndListen();

        while(true) {}
    }

    public void sendAppToTrayAndListen() {
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + "\\src\\icon16.png");

            PopupMenu menu = new PopupMenu();
            MenuItem exitButton = new MenuItem("Exit");
            exitButton.addActionListener(e -> {
                try {
                    exit();
                } catch(Exception ex) {
                    System.out.println(ex.getMessage());
                }
            });
            menu.add(exitButton);

            trayIcon = new TrayIcon(image, "Copy to Grayscale", menu);
            trayIcon.addActionListener(e -> {});

            try {
                tray.add(trayIcon);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void handle(String image) {
        BufferedImage gs = Grayscale.Convert(image);

        // get filename and extension out of the path
        StringBuilder extensionBuilder = new StringBuilder();
        int iter = image.length() - 1;
        while (image.charAt(iter) != '.') {
            extensionBuilder.append(image.charAt(iter--));
        }
        extensionBuilder.append('.');
        String extension = extensionBuilder.reverse().toString();

        // create a temporary file and write the resulting grayscale image to it
        File file = null;
        try {
            file = File.createTempFile(image.substring(0, image.length() - extension.length()) + "-grayscale", extension);
            if (gs != null) {
                ImageIO.write(gs, "png", file);
            }
        } catch(Exception ex) {}

        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        ArrayList<File> files = new ArrayList<>();
        files.add(file);
        ClipboardFile f = new ClipboardFile(files);
        cb.setContents(f, f);
    }

    public void exit() throws Exception {
        WinRegistry.deleteKey(WinRegistry.HKEY_CURRENT_USER, Application.APP_REGISTRY_KEY_LOCATION + "\\command");
        WinRegistry.deleteKey(WinRegistry.HKEY_CURRENT_USER, Application.APP_REGISTRY_KEY_LOCATION);
        System.exit(0);
    }
}
