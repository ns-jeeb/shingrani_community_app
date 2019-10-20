package com.locked.shingranicommunity.dashboard.announncement;

public class Announcement {

    private String name = "";
    private String type = "";
    private boolean hidden = false;
    private boolean sensitive = false;
    private boolean important = false;
    private String required = "";
    private String value = "";
    private String example = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isSensitive() {
        return sensitive;
    }

    public void setSensitive(boolean sensitive) {
        this.sensitive = sensitive;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
