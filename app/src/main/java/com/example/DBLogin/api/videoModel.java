package com.example.DBLogin.api;

public class videoModel {
    private String id;
    private String judul;
    private String kategori;
    private String video;
    private String desc;
    private String image;

    public videoModel(String id, String judul, String kategori, String video, String desc, String image) {
        this.id = id;
        this.judul = judul;
        this.kategori = kategori;
        this.video = video;
        this.desc = desc;
        this.image = image;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getVideo() { return video; }
    public void setVideo(String video) { this.video = video; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
