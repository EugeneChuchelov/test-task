package com.haulmont.testtask.entity;

public class RecipeStatistics {
    private long standard;

    private long cito;

    private long statim;

    public long getStandard() {
        return standard;
    }

    public void setStandard(long standard) {
        this.standard = standard;
    }

    public long getCito() {
        return cito;
    }

    public void setCito(long cito) {
        this.cito = cito;
    }

    public long getStatim() {
        return statim;
    }

    public void setStatim(long statim) {
        this.statim = statim;
    }

    public long getTotal() {
        return standard + cito + statim;
    }
}
