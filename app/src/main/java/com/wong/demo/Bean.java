package com.wong.demo;

public class Bean {
    private String name;
    private String job;
    public Bean(){}
    public Bean(String name, String job){
        this.name = name;
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name+"#"+this.job;
    }
}
