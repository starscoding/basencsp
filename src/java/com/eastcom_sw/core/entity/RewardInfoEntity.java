package com.eastcom_sw.core.entity;

import javax.persistence.*;

/**
 * Created by smile on 2017/12/26.
 */
@Entity
@Table(name = "reward_info", schema = "ncsp", catalog = "")
public class RewardInfoEntity {
    private String id;
    private Integer money;
    private String rewardTime;
    private String videoId;
    private String recordTime;

    @Id
    @Column(name = "ID", nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "money", nullable = true, precision = 0)
    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    @Basic
    @Column(name = "reward_time", nullable = true, length = 20)
    public String getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(String rewardTime) {
        this.rewardTime = rewardTime;
    }

    @Basic
    @Column(name = "video_id", nullable = true, length = 32)
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Basic
    @Column(name = "record_time", nullable = true, length = 20)
    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RewardInfoEntity that = (RewardInfoEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (money != null ? !money.equals(that.money) : that.money != null) return false;
        if (rewardTime != null ? !rewardTime.equals(that.rewardTime) : that.rewardTime != null) return false;
        if (videoId != null ? !videoId.equals(that.videoId) : that.videoId != null) return false;
        if (recordTime != null ? !recordTime.equals(that.recordTime) : that.recordTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (money != null ? money.hashCode() : 0);
        result = 31 * result + (rewardTime != null ? rewardTime.hashCode() : 0);
        result = 31 * result + (videoId != null ? videoId.hashCode() : 0);
        result = 31 * result + (recordTime != null ? recordTime.hashCode() : 0);
        return result;
    }
}
