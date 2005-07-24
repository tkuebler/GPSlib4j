package com.diddlebits.gpslib4j;

public class FloatSpecification {
    private double min;
    private double max;
    private double precision;
    
    public FloatSpecification(double xMin, double xMax, double xPrecision) {
        min=xMin;
        max=xMax;
        precision=xPrecision;
    }
    
    public double toPrecision(double value) {
        return Math.round(value/precision)*precision;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getPrecision() {
        return precision;
    }

    public String validate(double value) {
        if(value>max) {
            return "Out of range ("+value+">"+max+")";
        }
        if(value<min) {
            return "Out of range ("+value+"<"+min+")";
        }
        return null;
    }

    public boolean areEqual(double a, double b) {
        return toPrecision(a)==toPrecision(b);
    }
}
