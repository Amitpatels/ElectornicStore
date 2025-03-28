package com.lcwd.electronic.store.ElectronicStore.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //before api hit it will check the jwt header

        //Authorization : Bearer ahashasdsadhsadgsahsh
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header {} : "+requestHeader);

        String userName = null;
        String token = null;

        if(requestHeader != null &&  requestHeader.startsWith("Bearer")){
            token = requestHeader.substring(7);

            try{
                userName = jwtHelper.getUsernameFromToken(token);
                logger.info("Token username : {} ", userName);
            }catch (IllegalArgumentException ex){
                logger.info("Illegal Argument while fetching username !! "+ ex.getMessage());
            }catch (ExpiredJwtException ex){
                logger.info("Given jwt is expired !!"+ex.getMessage());
            }catch (MalformedJwtException ex){
                logger.info("Some changes has done in token !! Invalid Token "+ex.getMessage());
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }else {
            logger.info("Invalid header!!");
        }

        if(userName!=null && SecurityContextHolder.getContext().getAuthentication() == null){

           UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

           //valid token
            if(userName.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)){
                //token valid
                //setting authentication under security context
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request,response);
    }
}
