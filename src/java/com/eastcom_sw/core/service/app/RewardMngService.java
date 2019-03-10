package com.eastcom_sw.core.service.app;

import com.eastcom_sw.common.entity.Page;
import com.eastcom_sw.common.service.BaseService;
import com.eastcom_sw.core.dao.app.RewardMngDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by smile on 2017/12/26.
 */
@Component
@Transactional(readOnly = true)
public class RewardMngService extends BaseService{

    @Autowired
    private RewardMngDao rewardMngDao;

    public Page getRewardInfo(String startTime,String endTime,int start,int limit){
        return rewardMngDao.getRewardInfo(startTime,endTime,start,limit);
    }
}
