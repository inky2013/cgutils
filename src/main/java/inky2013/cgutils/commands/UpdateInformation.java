package inky2013.cgutils.commands;

import java.io.File;

public class UpdateInformation {

    public String name;
    public String saveLocation;
    public String url;

    public UpdateInformation() {}
    public UpdateInformation(String name, String saveLocation, String url) {
        this.name = name;
        this.saveLocation = saveLocation;
        this.url = url;
    }
}
