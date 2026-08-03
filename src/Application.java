public class Application {
    private static Application instance;
    private final ApplicationController applicationController;

    // No class can instantiate Application
    private Application() {
        this.applicationController = new ApplicationController();
    }

    // An instance of the Application is only created once and every caller receives the same instance
    public static Application getInstance() {
        if (Application.instance == null) Application.instance = new Application();
        return Application.instance;
    }

    public void run() {
        applicationController.execute();
    }
}
