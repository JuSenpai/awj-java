package project;

import project.app.Application;

public class MainClass {
    public static void main(String[] args) throws Exception {
        // launch the app
        Application app = new Application();

        // decide what to do with it; initialize with GUI or simply handle an image
        if (args.length < 1) {
            app.init();
        } else {
            app.handle(args[0]);
        }
    }
}
