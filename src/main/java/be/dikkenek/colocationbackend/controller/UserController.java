package be.dikkenek.colocationbackend.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "UserController", urlPatterns = "/api/users/*")
public class UserController extends HttpServlet
{

}
