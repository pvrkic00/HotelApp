package com.example.pvrki.bazaprobav2;

public class RezevacijaDataModel {
    String napomena,name,brojSobe;
    Integer ID;


    public RezevacijaDataModel(Integer ID,String napomena) {
        this.napomena = napomena;
        this.ID = ID;
    }
    public RezevacijaDataModel(Integer ID,String name,String napomena,String brojSobe)
    {
        this.name=name;
        this.ID=ID;
        this.napomena=napomena;
        this.brojSobe=brojSobe;
    }

    public String getNapomena() {
        return napomena;
    }

    public Integer getID() {
        return ID;
    }

    public String getName(){ return name; }

    public String getBrojSobe() {
        return brojSobe;
    }
}
