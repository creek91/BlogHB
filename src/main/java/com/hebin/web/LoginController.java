package com.hebin.web;

import com.hebin.dto.AccessTokenDTO;
import com.hebin.po.User;
import com.hebin.provider.GithubProvider;
import com.hebin.service.UserService;
import com.hebin.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by limi on 2017/10/15.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        System.out.println(user + "==========" + username + "==============" + password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String toSignUp() {
        return "/signup";
    }

    @PostMapping("/signup")
    public String signUp(@RequestParam String username,
                         @RequestParam String nickname,
                         @RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String confirmPassword,
                         HttpSession session,
                         RedirectAttributes attributes,
                         Model model) {

        User user = userService.checkUser(username);

        model.addAttribute("nickname", nickname);
        model.addAttribute("email", email);

        if (user != null) {
            username=username+"789";
            model.addAttribute("username", username);
            model.addAttribute("message", "已存在相同用户名账户，请修改用户名");
            return "/signup";
        }

        model.addAttribute("username", username);

        if (username == null || username.equals("")) {
            model.addAttribute("message", "请输入用户名");
            return "/signup";
        } else if (!password.equals(confirmPassword)) {
            model.addAttribute("message", "前后两次密码不一致");
            return "/signup";
        } else {
            System.out.println(username + "=======" + password);
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(MD5Utils.md5Code(password));
            user.setAvatar("https://picsum.photos/id/1/100/100?image=1005");
            user.setCreateTime(new Date());
            user.setNickname(nickname);
            user.setType(1);
            user.setUpdateTime(new Date());
            userService.saveUser(user);

            session.setAttribute("user", user);
            return "/login";
        }
    }

}
