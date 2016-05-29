/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author s155679
 */
public class line_model {

    private TemporalVertex TV1, TV2;
    private float ta;

    /**
     *
     * @param TV1
     * @param TV2
     * @param ta
     */
    public line_model(TemporalVertex TV1, TemporalVertex TV2, float ta) {
        this.TV2 = TV2;
        this.TV1 = TV1;
        this.ta = ta;
    }

    public void setTV1(TemporalVertex TV) {
        this.TV1 = TV;
    }

    public void setTV2(TemporalVertex TV) {
        this.TV2 = TV;
    }

    public void setTa(float ta) {
        this.ta = ta;
    }

    public float getta() {
        return ta;
    }

    public TemporalVertex getTV1() {
        return TV1;
    }

    public TemporalVertex getTV2() {
        return TV2;
    }
}
