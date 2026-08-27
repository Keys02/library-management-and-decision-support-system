public class Application {
    private static Application instance;
    private final ApplicationController applicationController;
    private WebServer webServer;

    // No class can instantiate Application
    private Application() {
        this.applicationController = new ApplicationController();
    }

    // An instance of the Application is only created once and every caller receives the same instance
    public static Application getInstance() {
        if (Application.instance == null) Application.instance = new Application();
        return Application.instance;
    }

    public ApplicationController getApplicationController() {
        return applicationController;
    }

    public void run() {
        // Start WebServer on port 8080
        webServer = new WebServer(applicationController, 8080);
        webServer.start();

        // Run application logic / console
        applicationController.execute();
    }
}
