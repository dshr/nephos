public class NephosWeather{

    static View mainGUI;

    public static void main(String[] args) {
        mainGUI = new View();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainGUI.createAndShowGUI();
                mainGUI.changeSize();
            }
        });
    }
}
