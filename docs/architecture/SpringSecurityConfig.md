# Spring Security Config

## Authentication Logic
Uses BCryptPasswordEncoder to secure user passwords in the local database. 
Form login handles sign in, placing authenticated user information in the SecurityContextHolder.
