package com.eventstoredb_demo;

// This class is used by the writer
// do not remove

public class TestEvent {
    private String id;
    private String importantData;

    public TestEvent(){
    }

    public TestEvent(String id, String importantData){
        this.id = id;
        this.importantData = importantData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getImportantData() {
        return importantData;
    }

    public void setImportantData(String importantData){
        this.importantData = importantData;
    }
}