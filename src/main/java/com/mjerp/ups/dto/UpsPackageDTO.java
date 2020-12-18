package com.mjerp.ups.dto;

import java.io.Serializable;

/**
 * @author tim
 */
public class UpsPackageDTO implements Serializable {

    private String dimensionsHeight;
    private String dimensionsLength;
    private String dimensionsWidth;
    private String packageWeight;

    public String getDimensionsHeight() {
        return dimensionsHeight;
    }

    public void setDimensionsHeight(String dimensionsHeight) {
        this.dimensionsHeight = dimensionsHeight;
    }

    public String getDimensionsLength() {
        return dimensionsLength;
    }

    public void setDimensionsLength(String dimensionsLength) {
        this.dimensionsLength = dimensionsLength;
    }

    public String getDimensionsWidth() {
        return dimensionsWidth;
    }

    public void setDimensionsWidth(String dimensionsWidth) {
        this.dimensionsWidth = dimensionsWidth;
    }

    public String getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(String packageWeight) {
        this.packageWeight = packageWeight;
    }
}
