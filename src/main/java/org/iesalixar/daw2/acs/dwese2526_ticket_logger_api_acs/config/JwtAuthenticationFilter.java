package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.services.CustomUserDetailsService;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Extraer el encabezado Authorization de la solicitud
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username; // Username extraido del jwt

        // 2. Verificar si el ecabezado Authorization esta presente y tiene un token valido
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            // Si el encabezado no esta presente o no comienza por "Bearer ", pasa la solicitrud al siguiente filtro
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token JWT del encabezado (sin el prefijo "Bearer ")
        jwt = authHeader.substring(7); // Elimina los 7 caracteres ("Bearer ")

        // 4. Extraer el nombre de usuario (claim "sub") del token
        username = jwtUtil.extractUsername(jwt);

        // 5. Verificar si:
        // - El nombre de usuario extraido no es nulo
        // - No hay una autenticacion existente en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 6. Cargar los detalles del usuario desde el servicio personalizado
            var userDetails = userDetailsService.loadUserByUsername(username);
            // 7. Validar el token JWT con el nombre de usuario del usuario cargado
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                // 8. Extraer los claims del token (como los roles)
                Claims claims = jwtUtil.extractAllClaims(jwt);
                //9. Extraer los roles del claim "roles" y convertirlos en GrantedAuthority
                List<String> roles = claims.get("roles", List.class); // Obtiene la lista de roles del token
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new) // Convierte cada rol en SimpleGrantedAuthority
                        .toList();
                // 10. Crear un objeto UsernamePasswordAuthenticationToken con los detalles del usuario y sus roles
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                // 11. Configurar los detalles adiccionales de la solicitud actual (por ejemplo, direccion IP)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 12. Establecer la autenticacion en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 13. Continuar con el siguiente filtro en la cadena de filtros
        filterChain.doFilter(request,response);
    }
}
