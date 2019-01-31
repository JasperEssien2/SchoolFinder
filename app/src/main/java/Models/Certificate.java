package Models;

import Models.Image;

public class Certificate {
    private String id, name, description;
    private Image imageOfCert;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImageOfCert() {
        return imageOfCert;
    }

    public void setImageOfCert(Image imageOfCert) {
        this.imageOfCert = imageOfCert;
    }
}
