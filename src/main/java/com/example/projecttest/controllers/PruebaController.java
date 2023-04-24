package com.example.projecttest.controllers;

import com.example.projecttest.models.Users;
import com.example.projecttest.services.AuthorityService;
import com.example.projecttest.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
//@RequestMapping(value = "/admin")
public class PruebaController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService usersService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping(value = "/helloAdmin")
    public String holaAdmin(Authentication authentication){
        System.out.println(authentication.getAuthorities());
        return "admin/holaAdmin";
    }

    @GetMapping(value = "/helloUser")
    public String holaUser(Authentication authentication, Model model, @Valid Users users, BindingResult result){
        model.addAttribute("msg_success", "¡Bienvenido al sistema!");
        System.out.println(authentication.getAuthorities());
        return "user/holaUser";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        System.out.println("entrree");
        try {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, null, null);
            redirectAttributes.addFlashAttribute("msg_success", "¡Sesión cerrada! Hasta luego");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un error al cerrar la sesión, intenta nuevamente");
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Users users, Model modelo) {
        return "admin/create";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String adminSignup( @Valid Users users, BindingResult result, @RequestParam("confirmarContraseña") String confirmarContraseña,
                               Model model,  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()){
            return "admin/create";
        }else {
            if (!users.getPassword().equals(confirmarContraseña)) {
                result.rejectValue("password", "error.password", "Las contraseñas no coinciden");
                return "admin/create";
            } else if (usersService.existByUsername(users.getUsername())) {
                result.rejectValue("username", "error.username", "El nombre de usuario ya existe en el sistema");
                return "admin/create";
            }

            users.setPassword(passwordEncoder.encode(users.getPassword()));
            users.addRole(authorityService.findByAuthority("ROLE_USER"));

            boolean res = usersService.save(users);
            if (res) {
                redirectAttributes.addFlashAttribute("msg_success", "Usuario registrado correctamente");
                return "redirect:/helloAdmin";
            } else {
                redirectAttributes.addFlashAttribute("msg_error", "Ocurrió un error al registrar al Enlace");
            }

        }
        //}
        return "redirect:/helloAdmin";
    }

    @RequestMapping(value = "/lista", method = RequestMethod.GET)
    public String findAllEnlaces(Model model) {
        List<Users> users = usersService.findAll();
        model.addAttribute("listUsers", users);
        return "user/lista";
    }
}
