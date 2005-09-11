package com.diddlebits.gpslib4j;

public class FloatSpecification {
    private double min;
    private double max;
    private double precision;
    private boolean nullAllowed;
    
    public FloatSpecification(double xMin, double xMax, double xPrecision, boolean xNullAllowed) {
        min=xMin;
        max=xMax;
        precision=xPrecision;
        nullAllowed=xNullAllowed;
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
    
    public String format2String(double value) {
        String result=String.valueOf(toPrecision(value));
        int afterColon=(int)-Math.floor(Math.log10(precision));
        if(afterColon>0) {
            int posColon=result.lastIndexOf(".");
            if(posColon>=0 && posColon+1+afterColon<result.length()) {
                result=result.substring(0,posColon+1+afterColon);
            }
        }
        return result;
    }

    public boolean areEqual(double a, double b) {
        return toPrecision(a)==toPrecision(b);
    }
    
    public boolean isNullAllowed() {
        return nullAllowed;
    }
    
    public int getMaxStringLength() {
        int result;
        if(Math.abs(min)<Math.abs(max))
            result=(int)Math.floor(Math.log10(Math.abs(min)))+1;
        else
            result=(int)Math.floor(Math.log10(Math.abs(max)))+1;
            
        if(min<0) {
            result+=1;
        }
        
        if(precision<1) {
            result+=(int)Math.floor(-Math.log10(precision));
        }
        
        return result;
    }

}
