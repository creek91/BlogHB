package com.hebin.web;

import com.hebin.dto.AccessTokenDTO;
import com.hebin.dto.GithubUser;
import com.hebin.po.User;
import com.hebin.provider.GithubProvider;
import com.hebin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class OAuthLoginController {

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;



    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpSession session) {

        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);

        GithubUser providerUser = githubProvider.getUser(accessToken);

        if (providerUser!=null){

            User user=new User();
            if (providerUser.getEmail()!=null){
                user.setEmail(providerUser.getEmail());
            }
            user.setAvatar(providerUser.getAvatar_url());
            user.setUsername(providerUser.getName());
            user.setAccountFrom("GitHub");
            user.setAccountId(providerUser.getId());

            userService.createOrUpdateUser(user);

            //登录成功，写cookie和session
            session.setAttribute("user",user);
            return "redirect:/";
        }else {
            //登陆失败，重新登陆
            return "redirect:/";
        }








    }
}
