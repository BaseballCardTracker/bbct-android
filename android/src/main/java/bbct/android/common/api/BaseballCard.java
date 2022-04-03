package bbct.android.common.api;

import com.google.gson.annotations.Expose;

public class BaseballCard {
    public Integer id;
    @Expose
    public Boolean autographed;
    @Expose
    public String condition;
    @Expose
    public String brand;
    @Expose
    public Integer year;
    @Expose
    public String number;
    @Expose
    public Integer value;
    @Expose
    public Integer quantity;
    @Expose
    public String player;
    @Expose
    public String team;
    @Expose
    public String position;

    public BaseballCard(
        boolean autographed, String condition, String brand,
        int year, String number, int value, int quantity,
        String player, String team, String position
    ) {
        this.autographed = autographed;
        this.condition = condition;
        this.brand = brand;
        this.year = year;
        this.number = number;
        this.quantity = quantity;
        this.value = value;
        this.player = player;
        this.team = team;
        this.position= position;
    }

    public BaseballCard(bbct.android.common.database.BaseballCard card) {
        this.autographed = card.autographed;
        this.condition = card.condition;
        this.brand = card.brand;
        this.year = card.year;
        this.number = card.number;
        this.quantity = card.quantity;
        this.value = card.value;
        this.player = card.playerName;
        this.team = card.team;
        this.position= card.position;
    }
}
