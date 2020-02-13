package com.hebin.web.admin;

import com.hebin.po.User;
import com.hebin.service.UserService;
import com.hebin.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by limi on 2017/10/15.
 */
@Controller
@RequestMapping("/admin")
public class SigninController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signup() {
        return "admin/signup";
    }

    @PostMapping("/signin")
    public String signin(@RequestParam String username,
                         @RequestParam String password,
                         HttpSession session,
                         RedirectAttributes attributes) {

        User user = userService.checkUser(username);

        if (user != null) {
//            System.out.println(username + "=======" + password);
            attributes.addFlashAttribute("message", "已存在相同用户名账户，请修改用户名");
            return "redirect:/admin/signup";
        } else if (username == null || username.equals("")) {
//            System.out.println(username + "=======" + password);
            attributes.addFlashAttribute("message", "请输入用户名");
            return "redirect:/admin/signup";
        } else {
            System.out.println(username + "=======" + password);
            user=new User();
            user.setUsername(username);
            user.setPassword(MD5Utils.md5Code(password));
            user.setAvatar("https://picsum.photos/id/1/100/100?image=1005");
            user.setCreateTime(new Date());
            user.setEmail("123456@qq.com");
            user.setNickname("ss");
            user.setType(1);
            user.setUpdateTime(new Date());
            userService.saveUser(user);

            session.setAttribute("user", user);
            return "admin/login";
        }
    }

}
