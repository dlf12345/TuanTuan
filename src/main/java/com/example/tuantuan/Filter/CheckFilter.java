package com.example.tuantuan.Filter;

import com.alibaba.fastjson.JSONObject;
import com.example.tuantuan.domain.R;
import com.example.tuantuan.utils.BaseContext;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//定义一个过滤器过滤前端请求,拦截所有请求
@WebFilter(urlPatterns = "/*")
public class CheckFilter implements Filter {
    //定义一个url路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        //（1）获取前端请求URL
//        注意获取URL的方法为getRequestURI()
        String requestURI = httpServletRequest.getRequestURI();
        System.out.println(requestURI);
//        (2)定义不进行拦截的请求
        String[] urls = new String[]{
          "/employee/login",
                "/employee/logout",
//                前端页面静态资源请求不拦截
                "/backend/**",
                "/front/**",
//                手机用户登录，发送验证码不进行拦截
                "/user/sendMsg",
                "/user/login"
        };
//        (3)判断请求是否需要拦截
        if(check(requestURI,urls)==false){
            //（3-1）如果需要拦截，判断Session中是否有数据从而判断后台用户是否登录
            if(httpServletRequest.getSession().getAttribute("employ")!=null){
                //如果用户已经登录
//                获取session中的用户id保存到Threadlocal中
                Long uid = (Long)httpServletRequest.getSession().getAttribute("employ");
                BaseContext.setCurrentId(uid);
//                对请求放行
                filterChain.doFilter(servletRequest, servletResponse);
                return;//结束方法
            }
            //（3-2）如果需要拦截，判断Session中是否有数据从而判断手机用户是否登录
            if(httpServletRequest.getSession().getAttribute("user")!=null){
                //如果用户已经登录
//                获取session中的用户id保存到Threadlocal中
//                Long uid = (Long)httpServletRequest.getSession().getAttribute("user");
//                BaseContext.setCurrentId(uid);
//                对请求放行
                filterChain.doFilter(servletRequest, servletResponse);
                return;//结束方法
            }
//            如果用户没有登录
            R r = R.error("NOTLOGIN");
            //将封装给前端的数据对象装华为JSON格式数据返回给前端
            String s = JSONObject.toJSONString(r);
            httpServletResponse.getWriter().write(s);
            return;
        }











//        （4）如果不需要拦截，直接放行
        filterChain.doFilter(servletRequest, servletResponse);
        return;//结束方法

    }
    //定义一个方法判断前端请求是否包含于不拦截的请求数组中
    public boolean check(String requestUrl,String[] urls){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if(match){
                return true;
            }

        }
        return false;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
