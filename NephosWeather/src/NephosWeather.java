public class NephosWeather{
    public static View mainGUI;
    public static void main(String[] args) {
                NephosAPI weather = new NephosAPI();
                mainGUI = new View(weather);
                mainGUI.createAndShowGUI();
                // mainGUI.changeSize();
    }
}
