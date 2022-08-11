package com.company.springmvcweb;


import com.company.springmvcweb.data.*;
import com.company.springmvcweb.data.CourseRepository;
import com.company.springmvcweb.dto.CourseSearchDto;
import com.company.springmvcweb.dto.LoginSearchDto1;
import com.company.springmvcweb.dto.SaveDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ParticipantController {

    private CourseRepository repo;
    private ParticipantRepository repo1;
    private Integer userId;

    public ParticipantController() {
        repo = new CourseRepository();
        repo1 = new ParticipantRepository();
    }

    @GetMapping("/courses")
    public String owner(Model model) {
        userId = 0;
        var items = repo.getAllCourses();

        model.addAttribute("title", "Courses");
        model.addAttribute("courses", items);
        return "courses";
    }


    @PostMapping("/courses")
    public String searchCourses(@ModelAttribute("searchDto") CourseSearchDto dto, Model model) {
        userId = 0;
        var items = repo.getCoursesPerIndustryAndOrLevel(dto);

        model.addAttribute("title", "Courses");
        model.addAttribute("courses", items);

        return "courses";
    }

    @GetMapping("/user/courses")
    public String allCourses(Model model) {

        var items = repo.getAllCourses();

        model.addAttribute("title", "Courses");
        model.addAttribute("courses", items);

        var participant = repo1.getParticipant(userId);
        model.addAttribute("userId", userId);

        return "user_allcourses";
    }


    @PostMapping("/user/courses")
    public String allCourses(@ModelAttribute("searchDto") CourseSearchDto dto, Model model) {

        var items = repo.getCoursesPerIndustryAndOrLevel(dto);

        model.addAttribute("title", "Courses");
        model.addAttribute("courses", items);

        var participant = repo1.getParticipant(userId);
        model.addAttribute("userId", userId);

        return "user_allcourses";
    }

    @GetMapping("/participants")
    public String participant(Model model) {

        var items = repo.getAllParticipants();

        model.addAttribute("title", "Participants");
        model.addAttribute("participants", items);

        return "participants";
    }


    @GetMapping("/courses/{id}")
    public String detailCourse(@PathVariable int id, Model model) {

        var course = repo.getCourseById(id);
        model.addAttribute("title", course != null ? course.getTitle() : "");
        model.addAttribute("course", course);
        model.addAttribute("id", id);
        model.addAttribute("freeSlots", course.getFreeSlots());

        var participant = repo1.getParticipant(userId);
        model.addAttribute("userId", userId);

        if (userId == 0) {
            return "courses_details";
        }
        if (course.getFreeSlots() == 0) {
            return "courses_details_2";
        }


        return "courses_details_u";
    }

    @GetMapping("/courses/{id}/participants")
    public String getParticipants(@PathVariable int id, Model model) {

        var course = repo.getCourseById(id);
        model.addAttribute("title", course != null ? course.getTitle() : "");
        model.addAttribute("course", course);
        model.addAttribute("id", id);
        model.addAttribute("freeSlots", course.getFreeSlots());

        var participant = repo1.getParticipant(userId);
        model.addAttribute("userId", userId);

        if (userId == 0) {
            return "courses_details";
        }
        if (course.getFreeSlots() == 0) {
            return "courses_details_2";
        }


        return "courses_details_u";
    }

    @GetMapping("/courses/{id}/cancel")
    public String cancelCourses(@PathVariable int id, Model model) {

        var course = repo.getCourseById(id);
        model.addAttribute("title", course != null ? course.getTitle() : "");
        model.addAttribute("course", course);
        model.addAttribute("id", id);

        var participant = repo1.getParticipant(userId);
        repo1.cancelCourse(id, userId);

        return "courses_details_cancel";
    }


    @PostMapping("/courses/{id}/register")
    public String registerForCourses(@PathVariable int id, Model model) {

        var course = repo.getCourseById(id);

        if (userId == 0) {
            return "sign_up_or_log_in";
        } else {

            var participant = repo1.getParticipant(userId);
      //  if ()

            model.addAttribute("title", course != null ? course.getTitle() : "");
            model.addAttribute("course", course);
            model.addAttribute("participant", participant);
            model.addAttribute("name", participant.getName());
            model.addAttribute("surname", participant.getSurname());
            model.addAttribute("e-mail", participant.geteMail());

            if (repo1.registerForCourseValidation(0, id, userId)){
                repo1.registerForCourse(0, id, userId);
            }else{
                return "courses_details_3";
            }


            return "courses_details_register";
        }
    }


    @GetMapping("/courses/{id}/cancel/confirm")
    public String cancelCoursesConfirm(@PathVariable int id, Model model) {

        return "courses_details_cancel_conf";
    }

    @GetMapping("/courses/{id}/register/confirm")
    public String confirmation(@PathVariable int id, Model model) {

        return "courses_details_register_confirm";
    }


    @GetMapping("/log_in")
    public String logIn(Model model) {


        var items = repo.getAllParticipants();

        model.addAttribute("title", "Log in");
        model.addAttribute("participants", items);


        return "log_in";
    }

    @GetMapping("/sign_up")
    public String signUp(Model model) {
        model.addAttribute("title", "Sign up");

        return "sign_up";
    }



    @PostMapping("/user")
    public String logIn(@ModelAttribute("loginSearchDto") LoginSearchDto1 dto, Model model) {
        var user = repo1.logIn(dto.geteMail(), dto.getPassword());
        userId = repo1.getParticipantIdFromEmail(dto.geteMail());


        model.addAttribute("title", "User");
        model.addAttribute("user", user);

        var items = repo.getAllCourses();

        model.addAttribute("title", "Courses");
        model.addAttribute("courses", items);

        if (user == null) {
            model.addAttribute("loginFailed", true);
            return "log_in";
        }

        if (user.isAdmin()) {
            return "admin";
        }

        return "user";
    }

    @PostMapping("/user/signup")
    public String signUp(@ModelAttribute("saveDto") SaveDto dto, Model model) {

        var participant = repo1.checkIfEmailIsRegistered(dto.geteMail());
        var validator = new PasswordValidator();


        model.addAttribute("Title", "Sign Up");


        if (participant) {
            model.addAttribute("emailRegistered", true);
            return "sign_up";
        }

        if (!validator.validateMatch(dto.getPassword(), dto.getPassword1())) {
            model.addAttribute("dontMatch", true);
            return "sign_up";
        }

        if (!validator.validateLength(dto.getPassword())) {
            model.addAttribute("length", true);
            return "sign_up";
        }

        if (!validator.validateCapitalLettersAndNumbers(dto.getPassword())) {
            model.addAttribute("lettersAndNumbers", true);
            return "sign_up";
        }

        var newParticipant = new Participant(0, dto.getName(), dto.getSurname(), dto.geteMail(), dto.getPassword(), false);
        var participantId = repo1.register(newParticipant);
        var user = repo1.getParticipant(participantId);

        model.addAttribute("user", user);

        return "user_signup";
    }


    @GetMapping("/user_courses")
    public String viewRegisteredCourses(Model model) {

        var user = repo1.getParticipant(userId);

        model.addAttribute("title", "User");
        model.addAttribute("user", user);
        model.addAttribute("name", user.getName());

        var items = repo.getCoursesPerParticipant(userId);


        var numberOfCourses = 0;
        for (var item:items) {
            ++numberOfCourses;
        }

        model.addAttribute("title", "Courses");
        model.addAttribute("courses", items);
        model.addAttribute("number", numberOfCourses);

        return "user_courses";
    }

    @PostMapping("/user_courses")
    public String cancelCourse(Model model) {

        return "user_courses";
    }

    @GetMapping("/user")
    public String getUser(Model model) {

        var items = repo.getAllParticipants();


        model.addAttribute("title", "User");
        model.addAttribute("participants", items);

        return "user";
    }

    @GetMapping("/admin")
    public String admin(Model model) {

        var courses = repo.getAllCourses();

        model.addAttribute("title", "Admin");
        model.addAttribute("courses", courses);
        return "admin";

    }

    @GetMapping("/admin/courses/{id}")
    public String adminCourses(@PathVariable int id, Model model) {

        var courses = repo.getAllCourses();

        model.addAttribute("title", "Admin");
        model.addAttribute("courses", courses);
        var course = repo.getCourseById(id);
        model.addAttribute("title", course != null ? course.getTitle() : "");
        model.addAttribute("course", course);
        model.addAttribute("id", id);
        model.addAttribute("freeSlots", course.getFreeSlots());

        return "courses_details_4";

    }

    @GetMapping("/courses/{id}/allparticipants")
    public String getParticipantsPerCourse(@PathVariable int id, Model model) {

        var course = repo.getCourseById(id);
        var participants = repo1.getParticipantsPerCourse(id);
        var adminId = 1;
        var user = repo1.getParticipant(adminId);

        model.addAttribute("title", "Participants");
        model.addAttribute("participants", participants);
        model.addAttribute("user", user);
        model.addAttribute("course", course);
        model.addAttribute("courseId", id);

        return "participants";
    }
}





