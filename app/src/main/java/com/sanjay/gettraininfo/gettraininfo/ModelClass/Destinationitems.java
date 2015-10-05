package com.sanjay.gettraininfo.gettraininfo.ModelClass;

/**
 * Created by Dinesh on 10/4/2015.
 */
public class Destinationitems {
    private int id ;
    private String Destinationname,sourcename;

    public Destinationitems(){

    }

    public Destinationitems(int id, String destinationname, String sourcename) {
        this.id = id;
        Destinationname = destinationname;
        this.sourcename = sourcename;
    }

    public Destinationitems(int id, String destinationname) {
        this.id = id;
        Destinationname = destinationname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestinationname() {
        return Destinationname;
    }

    public void setDestinationname(String destinationname) {
        Destinationname = destinationname;
    }

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }
}
