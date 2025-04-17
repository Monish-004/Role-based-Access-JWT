//package com.example.Spring.security.serviceImplementation;
//
//import com.example.Spring.security.enumeration.Role;
//import com.example.Spring.security.repository.MyApplicationRepository;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.security.Key;
//import java.security.NoSuchAlgorithmException;
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import static javax.crypto.Cipher.SECRET_KEY;
//
//@Service
//public class JwtService
//{
//
//    @Autowired
//    private MyApplicationRepository repository;
//
//    // For Our Understanding, Don't use in real time project [Manually Hardcoded our secretKey, but it is wrong way]
//
//    private  String SECRET_KEY = "L2Z4cDgyNzByMWN4dzNhc3U3bGJmbmQ2Z3QxZ3JxM3ByNXFqZGFtc2FncGg=";
//
//
//    private String secretKey;
//
//    public JwtService()
//    {
//        secretKey = generateSecretKey();
//    }
//
//
//    // Generating a SecretKey, this is a correct Way. Use in Real-Time project also.
//    public String generateSecretKey()
//    {
//        try
//        {
//            KeyGenerator keyGen = KeyGenerator.getInstance( "HmacSHA256"); // It Says Generate a key for this Algorithm
//            SecretKey secretKey = keyGen.generateKey(); // Generating a key and storing into secretKey
//            System.out.println("Hi");
//            System.out.println("Secret Key : " + secretKey.toString());
//            return Base64.getEncoder().encodeToString(secretKey.getEncoded()); // We are Encoding that secretKey
//        }
//        catch (NoSuchAlgorithmException e)
//        {
//            throw new RuntimeException("Error generating secret key", e);
//        }
//    }
//
//    public String generateToken (String username) {
//        Map<String, Object> claims = new HashMap<>();
//        return Jwts.builder()
//                .setClaims (claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration (new Date(System.currentTimeMillis() + 1000*60*3))
//                //.and()
//                .signWith(getKey(), SignatureAlgorithm. HS256).compact();
//    }
//
//
//    // For this Key[Inbuilt class], byte type should be return.
//    private Key getKey()
//    {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // We are decoding our secret key.
//        System.out.println("Key is Decoded");
//        System.out.println(Keys.hmacShaKeyFor(keyBytes));
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String extractUserName(String token)
//    {
//        // Extracting a username from Jwt Token
//        return extractClaim(token, Claims::getSubject);
//    }
//
//
//    private <T> T extractClaim(String token, Function<Claims, T> claimResolver)
//    {
//        final Claims claims = extractRoles(token);
//        return claimResolver.apply(claims);
//    }
//
////    private Claims extractAllClaims(String token)
////    {
////        return Jwts.parserBuilder()
////                .setSigningKey(getKey())
////                .build().parseClaimsJws(token).getBody();
////    }
//
////    public boolean validateToken(String token, UserDetails userDetails)
////    {
////        final String userName = extractUserName(token);
////        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
////    }
//
//    private boolean isTokenExpired(String token)
//    {
//        return extractExpiration (token). before (new Date());
//    }
//    private Date extractExpiration (String token)
//    {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    // AI GIVEN
//
//    public Set<Role> getRolesForUser(String username)
//    {
//        // Fetch roles from the database or repository (example, assuming you have a user repository)
//        return repository.findByUsername(username).getRoles();
//    }
//
//    public List<String> extractRoles(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(getKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        // Get roles from the JWT claims
//        return claims.get("roles", List.class);  // Extract roles from "roles" claim
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String userName = extractUserName(token);
//
//        // Extract roles from token
//        List<String> rolesFromToken = extractRoles(token);
//
//        // Extract roles from UserDetails
//        List<String> rolesFromUserDetails = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        // Compare username and roles
//        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token) &&
//                rolesFromToken.containsAll(rolesFromUserDetails));
//    }
//
//    public String generateToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//
//        // Get user roles from your database or service
//        Set<Role> roles = getRolesForUser(username);  // Assume you have a method to get roles for the user
//
//        // Convert roles to a list of strings like "ROLE_ADMIN", "ROLE_DOCTOR"
//        List<String> roleNames = roles.stream()
//                .map(role -> "ROLE_" + role.name())
//                .collect(Collectors.toList());
//
//        // Add roles to the claims
//        claims.put("roles", roleNames);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))  // 3 minutes expiration
//                .signWith(getKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(getKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        // Extract roles from the JWT claims
//        List<String> roles = claims.get("roles", List.class);
//        return roles.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
//
//
//
//
//}




package com.example.Spring.security.serviceImplementation;

import org.springframework.security.core.authority.SimpleGrantedAuthority;


import com.example.Spring.security.enumeration.Role;
import com.example.Spring.security.repository.MyApplicationRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Autowired
    private MyApplicationRepository repository;

    private String secretKey;

    public JwtService() {
        this.secretKey = generateSecretKey();
    }

    // Generating a SecretKey (correct way)
    public String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256"); // Generate a key for this algorithm
            SecretKey secretKey = keyGen.generateKey(); // Generate and store the key
            return Base64.getEncoder().encodeToString(secretKey.getEncoded()); // Encode secret key
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    // Get the signing key
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);  // Decode the secret key
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate Token with username and roles
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        // Get roles from the repository
        Set<Role> roles = getRolesForUser(username);

        // Convert roles to strings and add to claims
        List<String> roleNames = roles.stream()
                .map(role -> "ROLE_" + role.name())  // Prefix with 'ROLE_' as per Spring Security convention
                .collect(Collectors.toList());

        // Add roles to claims
        claims.put("roles", roleNames);

        // Create and return the token
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))  // 3 minutes expiration
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract roles from the JWT token
    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class);  // Extract the "roles" claim from the token
    }

    // Extract username from the JWT token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);  // Extract the subject (username) from the token
    }

    // Helper method to extract claims
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Validate the token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);

        // Extract roles from token
        List<String> rolesFromToken = extractRoles(token);

        // Extract roles from UserDetails
        List<String> rolesFromUserDetails = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Compare username and roles
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token) &&
                rolesFromToken.containsAll(rolesFromUserDetails);
    }

    // Get roles for user from the database
    public Set<Role> getRolesForUser(String username) {
        return repository.findByUsername(username).getRoles();  // Assuming repository returns roles for the user
    }

    // Get authorities (roles) from token
    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token) {
        List<String> roles = extractRoles(token);  // Extract roles from the token
        return roles.stream()
                .map(SimpleGrantedAuthority::new)  // Convert each role to SimpleGrantedAuthority
                .collect(Collectors.toList());
    }
}

