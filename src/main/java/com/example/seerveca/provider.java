package com.example.seerveca;

public class provider {
    private String name;
    private String phone;
    private  String firm;
    private  String service;

    public provider(){
        name=null;
        phone=null;
        firm=null;
        service=null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
