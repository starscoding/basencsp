package com.eastcom_sw.core.web.app;

import com.eastcom_sw.common.entity.Page;
import com.eastcom_sw.common.web.BaseController;
import com.eastcom_sw.core.service.app.RewardMngService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by smile on 2017/12/26.
 */

@Controller
@RequestMapping(value = "rewardMng")
public class RewardMngController extends BaseController {

    @Autowired
    private RewardMngService rewardMngService;

    @RequestMapping(value = "getRewardInfo")
    public void getRewardInfo(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "start", defaultValue = "0") String s,
                              @RequestParam(value = "limit", defaultValue = "25") String l) {
        int limit = Integer.parseInt(l);
        int start = Integer.parseInt(s);
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        log.info("startTime:"+startTime+",endTime:"+endTime);
        Page page = rewardMngService.getRewardInfo(startTime,endTime,start,limit);

        this.addResultInfo(SUCCESS,"query success!",page);
        this.responseResult(response,this.getResultJSONStr());
    }
}
