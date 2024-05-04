package com.example.demo.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.TokenResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        String token = servletRequest.getHeader("TOKEN");

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "null token");
        }

        TokenResponse tokenResponse = authService.getToken();

        if (tokenResponse.getToken().equals(token)) {
                User user = userRepository.findFirstByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not found"));
                
                if (tokenResponse.getExpiredToken().equals(user.getExpiredToken() < System.currentTimeMillis())) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "expired token");
                }
                
                return user;
        } else {
            throw new  ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }
}
