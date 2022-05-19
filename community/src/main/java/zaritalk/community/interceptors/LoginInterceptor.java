package zaritalk.community.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import zaritalk.community.app.domain.AccountTypeConvertor;
import zaritalk.community.enums.EAccountType;
import zaritalk.community.exceptions.NotAuthorized;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String payload = request.getHeader("Authorization");

        if (payload == null) {
            throw new NotAuthorized("Not Authorized");
        }

        String[] values = payload.split(" ");
        if (AccountTypeConvertor.convertStringToAttribute(values[0]) == EAccountType.NONE) {
            throw new NotAuthorized("Not Authorized");
        }

        request.setAttribute("accountType", values[0]);
        request.setAttribute("userId", Long.parseLong(values[1]));

        return true;
    }
}
