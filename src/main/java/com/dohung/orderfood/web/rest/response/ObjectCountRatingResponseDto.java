package com.dohung.orderfood.web.rest.response;

public class ObjectCountRatingResponseDto {

    private Integer fiveStar;
    private Integer fourStar;
    private Integer threeStar;
    private Integer secondStar;
    private Integer oneStart;

    public ObjectCountRatingResponseDto() {}

    public ObjectCountRatingResponseDto(Integer fiveStar, Integer fourStar, Integer threeStar, Integer secondStar, Integer oneStart) {
        this.fiveStar = fiveStar;
        this.fourStar = fourStar;
        this.threeStar = threeStar;
        this.secondStar = secondStar;
        this.oneStart = oneStart;
    }

    public Integer getFiveStar() {
        return fiveStar;
    }

    public void setFiveStar(Integer fiveStar) {
        this.fiveStar = fiveStar;
    }

    public Integer getFourStar() {
        return fourStar;
    }

    public void setFourStar(Integer fourStar) {
        this.fourStar = fourStar;
    }

    public Integer getThreeStar() {
        return threeStar;
    }

    public void setThreeStar(Integer threeStar) {
        this.threeStar = threeStar;
    }

    public Integer getSecondStar() {
        return secondStar;
    }

    public void setSecondStar(Integer secondStar) {
        this.secondStar = secondStar;
    }

    public Integer getOneStart() {
        return oneStart;
    }

    public void setOneStart(Integer oneStart) {
        this.oneStart = oneStart;
    }

    @Override
    public String toString() {
        return (
            "ObjectCountRatingResponseDto{" +
            "fiveStar=" +
            fiveStar +
            ", fourStar=" +
            fourStar +
            ", threeStar=" +
            threeStar +
            ", secondStar=" +
            secondStar +
            ", oneStart=" +
            oneStart +
            '}'
        );
    }
}
