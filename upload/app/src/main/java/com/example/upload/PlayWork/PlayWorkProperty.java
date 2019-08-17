package com.example.upload.PlayWork;

public class PlayWorkProperty {
    String name;
    String value;
    String ischecked = "_";
    Boolean ischeckbox;
    Boolean istitle;

    PlayWorkProperty(String name, String value, Boolean ischeckbox, String ischecked){
        this.name = name;
        this.value = value;
        this.ischeckbox = ischeckbox;
        this.istitle = Boolean.FALSE;
        this.ischecked = ischecked;
    }

    PlayWorkProperty(String name, String value){
        this.name = name;
        this.value = value;
        this.ischeckbox =  Boolean.FALSE;
        this.istitle = Boolean.FALSE;
    }

    PlayWorkProperty(String name){//제목
        this.name = name;
        this.value = "";
        this.ischeckbox = Boolean.FALSE;
        this.istitle = Boolean.TRUE;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue(){
        return value;
    }

    public Boolean getIscheckbox(){
        return ischeckbox;
    }

    public String getIschecked(){
        return ischecked;
    }

    public Boolean getIstitle(){
        return istitle;
    }
}
