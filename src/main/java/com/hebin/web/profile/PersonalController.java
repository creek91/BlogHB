package com.hebin.web.profile;

import com.hebin.po.User;
import com.hebin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class PersonalController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String indexId(RedirectAttributes attributes,
                          HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            attributes.addFlashAttribute("message", "请先登录！");
            return "redirect:/login";
        }
        return "profile/index";
    }

    @GetMapping("/blogs")
    public String userBlog(RedirectAttributes attributes,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            attributes.addFlashAttribute("message", "请先登录！");
            return "redirect:/login";
        }
        return "profile/blogs";
    }

    @GetMapping("/messages")
    public String userMessage(RedirectAttributes attributes,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            attributes.addFlashAttribute("message", "请先登录！");
            return "redirect:/login";
        }
        return "profile/messages";
    }

}
