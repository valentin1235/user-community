package community.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String payload = request.getHeader("Authorization");

        if (payload != null) {
            String[] values = payload.split(" ");

            request.setAttribute("accountType", values[0]);
            request.setAttribute("userId", Long.parseLong(values[1]));
        } else {
            request.setAttribute("accountType", "none");
            request.setAttribute("userId", -1);
        }

        return true;
    }
}
