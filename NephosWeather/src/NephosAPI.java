// package nephos;
import dme.forecastiolib.*;
import com.eclipsesource.json.*;
import java.text.*;

public class NephosAPI{
	

	private static String currentSummary;
	private static String currentIcon;
	private static int currentTemperature;
	private static int currentApparentTemperature;
	private static double currentPrecipProbability;
	private static double currentPrecipIntensity;
	private static double currentCloudCover;
	private static double currentWindSpeed;
	private static double currentHumidity;

	private static ForecastIO nephosForecast;
	private static FIOCurrently currentConditions;
	private static FIOHourly hoursConditions;
	private static FIODaily weekConditions;
	

	public NephosAPI() {

		String apikey = "7574e01b3ae61840c3751468897c932d";
		String longitude = "51.5085300";
		String latitude = "-0.1257400"; // LONDON COORDINATES longitude:51.5085300	latitude:-0.1257400

		nephosForecast = new ForecastIO(apikey);
		nephosForecast.setUnits(ForecastIO.UNITS_SI);
		nephosForecast.getForecast(longitude,latitude);


		currentConditions = new FIOCurrently(nephosForecast);
		currentSummary = currentConditions.get().getByKey("summary");
		String[] summaryParts = currentSummary.split("\"");
		currentSummary = summaryParts[1].toLowerCase();
		currentIcon = currentConditions.get().getByKey("icon");
		String[] iconParts = currentIcon.split("\"");
		currentIcon = iconParts[1].toLowerCase();
		
		System.out.println(currentIcon);
		currentTemperature = (int)(Double.parseDouble(currentConditions.get().getByKey("temperature")));
		currentApparentTemperature = (int)(Double.parseDouble(currentConditions.get().getByKey("apparentTemperature")));
		currentPrecipProbability = Double.parseDouble(currentConditions.get().getByKey("precipProbability"));
		currentPrecipIntensity = Double.parseDouble(currentConditions.get().getByKey("precipIntensity"));
		currentCloudCover = Double.parseDouble(currentConditions.get().getByKey("cloudCover"));
		System.out.println(currentCloudCover);
		currentWindSpeed = Double.parseDouble(currentConditions.get().getByKey("windSpeed"));
		currentHumidity = Double.parseDouble(currentConditions.get().getByKey("humidity"));

		hoursConditions = new FIOHourly(nephosForecast);
		weekConditions = new FIODaily(nephosForecast);
	}

	public String getCurrentSummary(){
		return currentSummary;
	}

	public String getCurrentIcon(){
		return currentIcon;
	}

	public int getCurrentTemperature(){
		return currentTemperature;
	}

	public int getCurrentApparentTemperature(){
		return currentApparentTemperature;
	}

	public double getCurrentPrecipProbability(){
		return currentPrecipProbability;
	}

	public double getCurrentPrecipIntensity(){
		return currentPrecipIntensity;
	}

	public double getCurrentCloudCover(){
		return currentCloudCover;
	}

	public double getCurrentWindSpeed(){
		return currentWindSpeed;
	}

	public double getCurrentHumidity(){
		return currentHumidity;
	}

	public String getSummaryAtHour(int hour){
		return hoursConditions.getHour(hour).getByKey("summary");
	}

	public String getIconAtHour(int hour){
		return hoursConditions.getHour(hour).getByKey("icon");
	}

	public int getTemperatureAtHour(int hour){
		return (int)(Double.parseDouble(hoursConditions.getHour(hour).getByKey("temperature")));
	}

	public int getApparentTemperatureAtHour(int hour){
		return (int)(Double.parseDouble(hoursConditions.getHour(hour).getByKey("apparentTemperature")));
	}

	public double getPrecipProbabilityAtHour(int hour){
		return Double.parseDouble(hoursConditions.getHour(hour).getByKey("precipProbability"));
	}

	public double getPrecipIntensitytyAtHour(int hour){
		return Double.parseDouble(hoursConditions.getHour(hour).getByKey("precipIntensity"));
	}

	public double getCloudCoverAtHour(int hour){
		return Double.parseDouble(hoursConditions.getHour(hour).getByKey("cloudCover"));
	}

	public double getWindSpeedAtHour(int hour){
		return Double.parseDouble(hoursConditions.getHour(hour).getByKey("windSpeed"));
	}

	public double getHumidityAtHour(int hour){
		return Double.parseDouble(hoursConditions.getHour(hour).getByKey("humidity"));
	}

	public int getMinTemperatureAtDay(int day){
		return (int)(Double.parseDouble(weekConditions.getDay(day).getByKey("temperatureMin")));
	}

	public int getMaxTemperatureAtDay(int day){
		return (int)(Double.parseDouble(weekConditions.getDay(day).getByKey("temperatureMax")));
	}

	public double precipProbabilityAtDay(int day){
		return Double.parseDouble(weekConditions.getDay(day).getByKey("precipProbability"));
	}

	public double precipIntensityAtDay(int day){
		return Double.parseDouble(weekConditions.getDay(day).getByKey("precipIntensity"));
	}

	public int getTemperatureAtDay(int day){
		return (int)((Double.parseDouble(weekConditions.getDay(day).getByKey("temperatureMin")))+(Double.parseDouble(weekConditions.getDay(day).getByKey("temperatureMax"))))/2;
	}

}