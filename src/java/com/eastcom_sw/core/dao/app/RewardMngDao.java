package com.eastcom_sw.core.dao.app;

import com.eastcom_sw.common.dao.jpa.Dao;
import com.eastcom_sw.common.entity.Page;
import com.eastcom_sw.core.entity.RewardInfoEntity;

/**
 * Created by smile on 2017/12/26.
 */
public interface RewardMngDao extends Dao<RewardInfoEntity> {
    public Page getRewardInfo(String startTime,String endTime,int start,int limit);
}
