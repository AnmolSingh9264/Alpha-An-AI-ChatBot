package com.anmol.alpha.model;

public class model {
    String send,recieve;
    String index;
    public model(String send,String recieve,String index){
        this.send=send;
        this.recieve=recieve;
        this.index=index;
    }
    public model(){

    }
    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getRecieve() {
        return recieve;
    }

    public void setRecieve(String recieve) {
        this.recieve = recieve;
    }
}
