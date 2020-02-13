package com.hebin.web.profile;


import com.hebin.po.Blog;
import com.hebin.po.User;
import com.hebin.service.BlogService;
import com.hebin.service.TagService;
import com.hebin.service.TypeService;
import com.hebin.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class UserBlogController {

    private static final String INPUT = "profile/blogs-input";
    private static final String LIST = "profile/blogs";
    private static final String REDIRECT_LIST = "redirect:/profile/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;


    @GetMapping("/blogs")
    public String userBlog(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog,
                       Model model,
                       RedirectAttributes attributes,
                       HttpSession session) {

        User user = (User) session.getAttribute("user");
       /* if (user == null) {
            attributes.addFlashAttribute("message", "请先登录！");
            return "redirect:/login";
        }
*/
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlogByUser(user.getId(),pageable));

        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog,
                         HttpSession session,
                         Model model) {

        User user = (User) session.getAttribute("user");
        /*if (user == null) {
            model.addAttribute("message", "请先登录！");
            return "redirect:/login";
        }*/
        model.addAttribute("page", blogService.listBlogByUser(user.getId(),pageable, blog));
        return "profile/blogs :: blogList";
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return "profile/blogs-input";
    }


    @PostMapping("/blogs")
    public String post(Blog blog,
                       RedirectAttributes attributes,
                       HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog b ;
        if(blog.getId()==null){
            b=blogService.saveBlog(blog);
        }else {
            b=blogService.updateBlog(blog.getId(),blog);
        }
        if (b == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/profile/blogs";
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.initTagIds();
        model.addAttribute("blog", blog);
        return  "profile/blogs-input";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBLog(id);
        attributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;
    }
}
