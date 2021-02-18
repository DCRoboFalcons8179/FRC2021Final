package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class controls {
    //Get the Joystick
    Joystick joy;

    //constructor
    public controls(int address){
        joy = new Joystick(address);

        // Analog Buttons

        axis1 = joy.getRawAxis(1);
        axis2 = joy.getRawAxis(2);
        axis3 = joy.getRawAxis(3);
        axis4 = joy.getRawAxis(4);
        axis5 = joy.getRawAxis(5);
        axis6 = joy.getRawAxis(6);


        // Digital Buttons

        // INITALIZE VS DECLARE THE VARIABLES



    }
    //Xbox indexes based on following chart
    //https://lh3.googleusercontent.com/proxy/evdqSkui7r-NKGg678aqL05sNj5gMnzkqam7rQmciUfh8sOb1SfrTTj5B454JXrq3lqrNm7kXVO7f03I_XpCbpRWYESWMEmfrXdibQhkIdqW43bhsDrd214bhbqXQ85-aQyB__lg0eEsiWvDzuodr2zApp92
    
    // Generic Digital Buttons
    public boolean button1  = joy.getRawButton(1);
    public boolean button2  = joy.getRawButton(2);
    public boolean button3  = joy.getRawButton(3);
    public boolean button4  = joy.getRawButton(4);
    public boolean button5  = joy.getRawButton(5);
    public boolean button6  = joy.getRawButton(6);
    public boolean button7  = joy.getRawButton(7);
    public boolean button8  = joy.getRawButton(8);
    public boolean button9  = joy.getRawButton(9);
    public boolean button10 = joy.getRawButton(10);
    public boolean button11 = joy.getRawButton(11);
    public boolean button12 = joy.getRawButton(12);


    // Analog Buttons

    public double axis1 = joy.getRawAxis(1);
    public double axis2 = joy.getRawAxis(2);
    public double axis3 = joy.getRawAxis(3);
    public double axis4 = joy.getRawAxis(4);
    public double axis5 = joy.getRawAxis(5);
    public double axis6 = joy.getRawAxis(6);


    // If button state has changed

    public boolean button1_isPressed = false;
    public boolean button2_isPressed = false;
    public boolean button3_isPressed = false;
    public boolean button4_isPressed = false;
    public boolean button5_isPressed = false;
    public boolean button6_isPressed = false;
    public boolean button7_isPressed = false;
    public boolean button8_isPressed = false;
    public boolean button9_isPressed = false;
    public boolean button10_isPressed = false;
    public boolean button11_isPressed = false;
    public boolean button12_isPressed = false;
    public boolean button13_isPressed = false;
    public boolean button14_isPressed = false;
    public boolean button15_isPressed = false;





    public void refreshValues() {


        // Button Holders
        boolean button1_temp  = joy.getRawButton(1);
        boolean button2_temp  = joy.getRawButton(2);
        boolean button3_temp  = joy.getRawButton(3);
        boolean button4_temp  = joy.getRawButton(4);
        boolean button5_temp  = joy.getRawButton(5);
        boolean button6_temp  = joy.getRawButton(6);
        boolean button7_temp  = joy.getRawButton(7);
        boolean button8_temp  = joy.getRawButton(8);
        boolean button9_temp  = joy.getRawButton(9);
        boolean button10_temp = joy.getRawButton(10);
        boolean button11_temp = joy.getRawButton(11);
        boolean button12_temp = joy.getRawButton(12);
    
        // Comparer

        if (button1 == button1_temp) {
            button1_isPressed = 0;
        }
        else {
            button1_isPressed = 1;
        }

        if (button2 == button2_temp) {
            button2_isPressed = 0;
        }
        else {
            button2_isPressed = 1;
        }

        if (button3 == button3_temp) {
            button3_isPressed = 0;
        }
        else {
            button3_isPressed = 1;
        }

        if (button4 == button4_temp) {
            button4_isPressed = 0;
        }
        else {
            button4_isPressed = 1;
        }

        if (button5 == button5_temp) {
            button5_isPressed = 0;
        }
        else {
            button5_isPressed = 1;
        }

        if (button6 == button6_temp) {
            button6_isPressed = 0;
        }
        else {
            button6_isPressed = 1;
        }

        if (button7 == button7_temp) {
            button7_isPressed = 0;
        }
        else {
            button7_isPressed = 1;
        }

        if (button8 == button8_temp) {
            button8_isPressed = 0;
        }
        else {
            button8_isPressed = 1;
        }

        if (button9 == button9_temp) {
            button9_isPressed = 0;
        }
        else {
            button9_isPressed = 1;
        }

        if (button10 == button10_temp) {
            button10_isPressed = 0;
        }
        else {
            button10_isPressed = 1;
        }

        if (button11 == button11_temp) {
            button11_isPressed = 0;
        }
        else {
            button11_isPressed = 1;
        }

        if (button12 == button12_temp) {
            button12_isPressed = 0;
        }
        else {
            button12_isPressed = 1;
        }

        if (button13 == button13_temp) {
            button13_isPressed = 0;
        }
        else {
            button13_isPressed = 1;
        }

        if (button14 == button14_temp) {
            button14_isPressed = 0;
        }
        else {
            button14_isPressed = 1;
        }

        if (button15 == button15_temp) {
            button15_isPressed = 0;
        }
        else {
            button15_isPressed = 1;
        }

        // Analog Buttons
    
        axis1 = joy.getRawAxis(1);
        axis2 = joy.getRawAxis(2);
        axis3 = joy.getRawAxis(3);
        axis4 = joy.getRawAxis(4);
        axis5 = joy.getRawAxis(5);
        axis6 = joy.getRawAxis(6);




    }




}
