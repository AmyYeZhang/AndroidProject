package com.example.finalproject.soccer;

public class SoccerDetail {

    private long id;
    private String title;
    private String team1;
    private String team2;
    private String videoUrl;
    private String date;
    private String competition;
    private String thumbnailUrl;

    public SoccerDetail(long id, String title, String team1, String team2, String videoUrl, String date, String competition, String thumbnailUrl){
        this.id = id;
        this.title = title;
        this.team1 = team1;
        this.team2 = team2;
        this.videoUrl = videoUrl;
        this.date = date;
        this.competition = competition;
        this.thumbnailUrl = thumbnailUrl;
    }

    public long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getTeam1(){
        return team1;
    }

    public String getTeam2(){
        return team2;
    }

    public String getVideoUrl(){
        return videoUrl;
    }

    public String getDate(){
        return date;
    }

    public String getCompetition(){
        return competition;
    }

    public String getThumbnailUrl(){
        return thumbnailUrl;
    }
}
