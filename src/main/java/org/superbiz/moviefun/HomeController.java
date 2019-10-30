package org.superbiz.moviefun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Controller
@Transactional
public class HomeController {

    @GetMapping("/")
    public String doIndex() {
        return "index";
    }

    @Autowired
    MoviesBean moviesBean;

    @GetMapping("/setup")
    public ModelAndView doSetup() {
        Map<String, Object> model = new HashMap<>();
        moviesBean.addMovie(new Movie("Wedding Crashers", "David Dobkin", "Comedy", 7, 2005));
        moviesBean.addMovie(new Movie("Starsky & Hutch", "Todd Phillips", "Action", 6, 2004));
        moviesBean.addMovie(new Movie("Shanghai Knights", "David Dobkin", "Action", 6, 2003));
        moviesBean.addMovie(new Movie("I-Spy", "Betty Thomas", "Adventure", 5, 2002));
        moviesBean.addMovie(new Movie("The Royal Tenenbaums", "Wes Anderson", "Comedy", 8, 2001));
        moviesBean.addMovie(new Movie("Zoolander", "Ben Stiller", "Comedy", 6, 2001));
        moviesBean.addMovie(new Movie("Shanghai Noon", "Tom Dey", "Comedy", 7, 2000));


        model.put("movies", moviesBean.getMovies());
        ModelAndView modelAndView = new ModelAndView("setup", model);
        return modelAndView;
    }
}
