public static void main() {
    try {
       Application app = Application.getInstance();
       app.run();
    } catch (Exception e) {
        System.out.println( "[FATAL_0x001A] 💀 An unrecoverable error has occurred and the application must close.");
    }

}
