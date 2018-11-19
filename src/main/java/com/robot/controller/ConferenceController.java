package com.robot.controller;

import com.robot.entity.RegistrationForm;
import com.robot.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * @author Ning
 * @date 2018/9/27
 */
@Controller
@RequestMapping("/conference")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    /**
     * 取得相应的展会信息
     * @author Ning
     * @data 2018/9/27
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping(value = "/getRelevantConference", method = RequestMethod.GET)
    public String getHoldingConference(Integer relevantId){
        return conferenceService.getRelevantConference(relevantId);
    }


    /**
     * 取得相应的会议信息
     * @author Ning
     * @data 2018/9/27
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping(value = "/getRelevantMeeting", method = RequestMethod.GET)
    public String getRelevantMeeting(Integer relevantId){
        return conferenceService.getRelevantMeting(relevantId);
    }


    /**
     * 报名
     * @author Ning
     * @data 2018/9/27
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping(value = "/attend", method = RequestMethod.POST)
    public String writeForm(RegistrationForm registrationForm){
            return conferenceService.enroll(registrationForm);
    }
}
