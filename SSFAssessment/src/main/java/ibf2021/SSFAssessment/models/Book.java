package ibf2021.SSFAssessment.models;

public class Book {
    private String worksID;
    private String title;
    private String thumbnailURL;
    private String excerpt;
    private String description;

    // Empty Constructor
    public Book() {
    }

    // Getters and Setters
    public String getExcerpt() {
        return this.excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailURL() {
        return this.thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getWorksID() {
        return this.worksID;
    }

    public void setWorksID(String worksID) {
        this.worksID = worksID;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
