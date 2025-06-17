package cn.iocoder.forum.interceptor;

import cn.iocoder.forum.config.AppConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    @Value("${forum-sys.login.url}")
    private String defaultUrl;

    /**
     * 前置处理(对请求的预处理)
     * @return true : 继续流程 <br/> false : 流程中断
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取Session
        HttpSession session = request.getSession(false);
        // 2. 判断Session是否为空
        if (session != null && session.getAttribute(AppConfig.USER_SESSION) != null) {
            // 用户为登录状态，允许通过
            return true;
        }
        // 3. 校验URL是否正确
        if (!defaultUrl.startsWith("/")) {
            defaultUrl = "/" + defaultUrl;
        }
        // 4. 校验不通过，跳转到登录界面
        response.sendRedirect(defaultUrl);
        // 中断流程
        return false;
    }
}
