package com.eastcom_sw.core.dao.app;

import com.eastcom_sw.common.dao.jpa.DaoSupport;
import com.eastcom_sw.common.entity.Page;
import com.eastcom_sw.core.entity.RewardInfoEntity;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 * Created by smile on 2017/12/26.
 */
@SuppressWarnings("JpaQlInspection")
@Component
public class RewardMngDaoImpl extends DaoSupport<RewardInfoEntity> implements RewardMngDao {

    @PersistenceContext(unitName = "defaultPU")
    public EntityManager em;

    private JdbcTemplate jdbcTemplate;

    @Resource(name = "dataSource")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Page getRewardInfo(String startTime, String endTime,int start,int limit) {
        Object[] args = {};
        String hql = "from RewardInfoEntity r where r.rewardTime>=? and r.rewardTime<=?";
        args = ArrayUtils.add(args,startTime);
        args = ArrayUtils.add(args,endTime);
        log.info(hql);
        Page page = this.pagedQuery(hql,start,limit,args);
//        List<RewardInfoEntity> rs = new ArrayList<RewardInfoEntity>();
//        List list = page.getElements();
//        try {
//            if(list!=null && list.size()>0){
//                for (int i = 0; i < list.size(); i++) {
//                    Object[] obj = (Object[]) list.get(i);
//                    RewardInfoEntity tmp = new RewardInfoEntity();
//                    tmp.setId(obj[0] == null ? "" : obj[0].toString());
//                    tmp.setMoney(Integer.parseInt(obj[1] == null ? "0" : obj[1].toString()));
//                    tmp.setRewardTime(obj[2] == null ? "" : obj[2].toString());
//                    tmp.setVideoId(obj[3] == null ? "" : obj[3].toString());
//                    tmp.setRecordTime(obj[4] == null ? "" : obj[4].toString());
//                    rs.add(tmp);
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        page.setElements(rs);
//        log.info(">>>>>>>"+String.valueOf(list.size()));
        return page;
    }
}
