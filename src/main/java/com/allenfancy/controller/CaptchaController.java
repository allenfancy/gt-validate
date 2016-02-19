package com.allenfancy.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.allenfancy.config.GeeValidatorConfig;
import com.allenfancy.lib.GeeValidatorLibary;


@Controller
@RequestMapping(value="/captcher")
public class CaptchaController {

	@RequestMapping(value="/startCapcha",method={RequestMethod.GET})
	public @ResponseBody Object startCapcha(HttpServletRequest req,HttpServletResponse res){
		GeeValidatorLibary gvl = new GeeValidatorLibary(GeeValidatorConfig.getCaptcha_id(), GeeValidatorConfig.getPrivate_key());
		String resStr = "{}";

		//进行验证预处理
		int gtServerStatus = gvl.preProcess();
		
		//将服务器状态设置到session中
		req.getSession().setAttribute(gvl.gtServerStatusSessionKey, gtServerStatus);
		
 		resStr = gvl.getResponseStr();
 		return resStr;
	}
	
	@RequestMapping(value="/validateLogin",method={RequestMethod.POST})
	public String doLogin(HttpServletRequest req,HttpServletResponse res){
		GeeValidatorLibary gvl = new GeeValidatorLibary(GeeValidatorConfig.getCaptcha_id(), GeeValidatorConfig.getPrivate_key());
		String challenge = req.getParameter(GeeValidatorLibary.fn_geetest_challenge);
		String validate = req.getParameter(GeeValidatorLibary.fn_geetest_validate);
		String seccode = req.getParameter(GeeValidatorLibary.fn_geetest_seccode);
			
		//从session中获取gt-server状态
		int gt_server_status_code = (Integer) req.getSession().getAttribute(gvl.gtServerStatusSessionKey);
		
		int gtResult = 0;

		if (gt_server_status_code == 1) {
			//gt-server正常，向gt-server进行二次验证
			gtResult = gvl.enhencedValidateRequest(challenge, validate, seccode);
			System.out.println(gtResult);
		} else {
			// gt-server非正常情况下，进行failback模式验证	
			System.out.println("failback:use your own server captcha validate");
			gtResult = gvl.failbackValidateRequest(challenge, validate, seccode);
			System.out.println(gtResult);
		}

		if (gtResult == 1) {
			// 验证成功
			return "success";
		}
		else {
			// 验证失败
			return "error";
		}

	}
}
