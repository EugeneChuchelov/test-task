package com.haulmont.testtask.entity;

public class RecipeStatistics {
    private long standard;

    private long cito;

    private long statim;

    public void setStandard(long standard) {
        this.standard = standard;
    }

    public void setCito(long cito) {
        this.cito = cito;
    }

    public void setStatim(long statim) {
        this.statim = statim;
    }

    public long getStandard() {
        return standard;
    }

    public long getCito() {
        return cito;
    }

    public long getStatim() {
        return statim;
    }

    public long getTotal(){
        return standard + cito + statim;
    }
}
