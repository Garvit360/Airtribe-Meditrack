package com.airtribe.meditrack.entity;

public abstract class MedicalEntity implements Cloneable {
    private final String id;

    protected MedicalEntity(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public abstract String getEntityType();

    public void printSummary(){
        System.out.println(getEntityType() + " Id: " + id);
    }

    @Override
    protected MedicalEntity clone() throws CloneNotSupportedException{
        return (MedicalEntity) super.clone();
    }
}
