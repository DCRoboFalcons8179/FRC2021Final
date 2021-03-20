package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controls {
    //Get the Joystick
    Joystick joy;

    //constructor
    public Controls(int address){
        joy = new Joystick(address);
        
        // Analog Controls
        int axises = joy.getAxisCount();
        analog = new double[axises];

        // Digital Buttons
        int buttons = joy.getButtonCount();
        button = new boolean[buttons];
        button_isPressed = new boolean[buttons];
    }
    //Xbox indexes based on following chart
    //https://lh3.googleusercontent.com/proxy/evdqSkui7r-NKGg678aqL05sNj5gMnzkqam7rQmciUfh8sOb1SfrTTj5B454JXrq3lqrNm7kXVO7f03I_XpCbpRWYESWMEmfrXdibQhkIdqW43bhsDrd214bhbqXQ85-aQyB__lg0eEsiWvDzuodr2zApp92

    // Generic Digital Buttons
    public boolean[] button;

    // Analog Buttons
    public double[] analog;

    // If button state has changed
    public boolean[] button_isPressed;

    public void refreshValues() {
        for(int i = 0; i < analog.length; i++){
            analog[i] = joy.getRawAxis(i);
        }
        for(int i = 0; i < button.length; i++){
            button[i] = joy.getRawButton(i);
            button_isPressed[i] = joy.getRawButtonPressed(i);
        }
    }
}