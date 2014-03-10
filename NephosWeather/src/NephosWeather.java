public class NephosWeather{
    public static View mainGUI;
    public static void main(String[] args) {
        // javax.swing.SwingUtilities.invokeLater(new Runnable() {
            // public void run() {

                mainGUI = new View();
                NephosAPI weather = new NephosAPI();
                mainGUI.createAndShowGUI();
                mainGUI.changeSize();

            // }
        // });
    }
}
