package co.edu.unbosque.taller5.dtos;

public class WalletHistory {

    private int id;

    private String userApp;

    private String type;

    private float fcoins;

    private String timeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserApp() {
        return userApp;
    }

    public void setUserApp(String userApp) {
        this.userApp = userApp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getFcoins() {
        return fcoins;
    }

    public void setFcoins(float fcoins) {
        this.fcoins = fcoins;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
