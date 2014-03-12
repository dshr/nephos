Nephos - Group 18:

To compile and run:
Unix/Mac:
javac -d bin -sourcepath src -cp 'lib/*:.' src/NephosWeather.java
java -cp bin:lib/*:. NephosWeather

Windows:
javac -d bin -sourcepath src -cp lib/*;. src/NephosWeather.java
java -cp bin:lib/*;. NephosWeather


Using the app (key features):
- The initial view is pretty self explanatory. There is a main panel with weather details, alongside buttons on the top right and left
- The top right button leads to settings
- The top left leads to a calendar view (weather view for the week)
- Clicking on the center of the screen leads you to a clock view. From this screen, pressing the right key on the keyboard 
  and swiping a red object left/right will change the "time" on the clock three hours forwards or backwards respectively
- Alternatively, holding down the mouse and circling the dark circle around the clock gives you the weather for a period of 48 hours
  before and after the current time.
- Pressing the spacebar from any view will cause a real time switch between phone/tablet layouts