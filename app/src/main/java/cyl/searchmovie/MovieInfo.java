package cyl.searchmovie;

public class MovieInfo {
    private String title;
    private String director;
    private String imagelink;
    private String date;
    private String actor;
    private String rating;
    private String urllink;

    public MovieInfo(String title, String director, String imagelink, String date, String actor, String rating, String urllink) {
        this.title = title;
        this.director = director;
        this.imagelink = imagelink;
        this.date = date;
        this.actor = actor;
        this.rating = rating;
        this.urllink = urllink;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getImagelink() {
        return imagelink;
    }

    public String getDate() {
        return date;
    }

    public String getActor() {
        return actor;
    }

    public String getRating() {
        return rating;
    }

    public String getUrllink() {
        return urllink;
    }
}
