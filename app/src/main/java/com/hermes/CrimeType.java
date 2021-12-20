package com.hermes;

public enum CrimeType {

    ASSAULT(R.string.assault, R.color.assault),
    ROBBERY(R.string.robbery, R.color.robbery),
    AUTO_THEFT(R.string.auto_theft, R.color.auto_theft),
    MUGGING(R.string.mugging, R.color.mugging),
    MURDER(R.string.murder, R.color.murder),
    SEXUAL_ASSAULT(R.string.sexual_assault, R.color.sexual_assault);

    private int hueRes;
    private int nameRes;


    CrimeType(int nameRes, int hueRes) {
        this.nameRes = nameRes;
        this.hueRes = hueRes;
    }

    public int getHueRes() {
        return hueRes;
    }

    public int getNameRes() {
        return nameRes;
    }


}
