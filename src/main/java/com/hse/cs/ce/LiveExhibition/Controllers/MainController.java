package com.hse.cs.ce.LiveExhibition.Controllers;

import com.hse.cs.ce.LiveExhibition.Models.*;
import com.hse.cs.ce.LiveExhibition.Repositories.*;
import com.hse.cs.ce.LiveExhibition.Services.AmazonClient;
import com.hse.cs.ce.LiveExhibition.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
public class MainController implements ErrorController {

    @Autowired
    private ExhibitionsRepository exhibitionsRepository;
    @Autowired
    private OrganizersRepository organizersRepository;
    @Autowired
    private ExhibitorsRepository exhibitorsRepository;
    @Autowired
    private ExhibitorsApplicationRepository exhibitorsApplicationRepository;
    @Autowired
    private ModeratorsRepository moderatorsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExhibitorsGoodsRepository exhibitorsGoodsRepository;
    @Autowired
    private ExhibitionTagRepository exhibitionTagRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AmazonClient amazonClient;


    @Value("${jsa.s3.bucketName}")
    private String bucket;
    private UserDetails userDetails;
    private final MessageSource messageSource;


    @Autowired
    public MainController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /*private static final String[] LANGUAGES = { "AL", "AK", "AZ", "AR", "CA",
            "CO", "CT", "DE", "DC", "FL", "GA", "HI", "ID", "IL", "IN", "IA",
            "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT",
            "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR",
            "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WV",
            "WI", "WY" };*/
    /*
    <sec:authorize access="isAuthenticated()">
                    <a class="btn btn-outline-primary me-2 mx-2" href="/personalAccount" role="button">Личный кабинет</a>
                    <a class="btn btn-outline-primary me-2 mx-2" href="/logout" role="button">Выйти</a>
                </sec:authorize>
     */



    public String uploadFile(MultipartFile file) {
        return amazonClient.uploadFile(file);
    }


    public ResponseEntity<ByteArrayResource> getFile(String fileName)
    {
        fileName = fileName.substring(fileName.lastIndexOf('/')+1);
        byte[] data = amazonClient.getFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok().contentLength(data.length)
                .header("Content-type", "Photo")
                .header("Content-description", "filename=\"" + fileName + "\"")
                .body(resource);
    }

    public String deleteFile(String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }


    @GetMapping("/")
    public String home1(Model model) {

        return "home";
    }
    @GetMapping("/home")
    public String home2(Model model) {

        return "home";
    }
    @GetMapping("/login")
    public String login(Model model) {

        return "login";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
    @GetMapping("/error")
    public String error(Locale locale, Model model, HttpServletRequest request, Exception ex) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorDesc", "Ошибка 404. Страница не найдена");
                model.addAttribute("errorNote", "К сожалению, мы не можем найти страницу, которвую Вы ищите. Пожалуйста, проверьте правильность написания адреса страницы.");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorDesc", "Ошибка 500. Внутренняя ошибка сервера");
                model.addAttribute("errorNote", "На сервере произошла непредвиденная ошибка. Пожалуйста, попробуйте зайти позже.");
            }
            else
                model.addAttribute("errorDesc", statusCode);
        }
        return "/error";
    }

    @GetMapping("/personalAccount")
    public String personalAccount(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getPrincipal() == "anonymousUser")
            return "home";
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if(user.get().getUserType().equals("1"))
            return "redirect:/organizersPersonalAccount";
        if(user.get().getUserType().equals("2"))
            return "redirect:/exhibitorsPersonalAccount";
        if(user.get().getUserType().equals("4"))
            return "redirect:/moderatorsPersonalAccount";
        return "home";
    }
    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        System.out.println("registration");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            System.out.println("ошибка");
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            System.out.println("Пароли не совпадают");
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        if (!userService.saveUser(userForm)){
            System.out.println("Пользователь с таким именем уже существует");
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }
        System.out.println("registration");
        return "redirect:/";
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/organizersPersonalAccount")
    public String organizersPersonalAccount(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Organizers> organizer = organizersRepository.findByEmail(userDetails.getUsername());
        model.addAttribute("organizer", organizer.get());
        return "organizersPersonalAccount";
    }

    @GetMapping("/organizersPersonalAccountEdit/{id}")
    public String organizersPersonalAccountEdit(@PathVariable(name = "id") String organizerId, Model model) {
        Optional<Organizers> organizer = organizersRepository.findById(Long.parseLong(organizerId));
        model.addAttribute("organizer", organizer.get());
        return "organizersPersonalAccountEdit";
    }
    @PostMapping("/organizersPersonalAccountEdit/{id}")
    public String organizersPersonalAccountEdit(@ModelAttribute("orgChangeForm") @Valid Organizers orgChangeForm,@PathVariable(name = "id") String organizerId,
                                               Model model) {
        Optional<Organizers> organizer = organizersRepository.findById(Long.parseLong(organizerId));
        organizer.get().setFullName(orgChangeForm.getFullName());
        organizer.get().setCompany(orgChangeForm.getCompany());
        organizersRepository.save(organizer.get());
        return "redirect:/organizersPersonalAccount";
    }

    @GetMapping("/organizersExhibitions")
    public String organizersExhibitions(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Organizers> organizer = organizersRepository.findByEmail(userDetails.getUsername());
        Iterable<Exhibitions> exhibitions = exhibitionsRepository.findByOrganizer(organizer.get());
        model.addAttribute("exhibitions", exhibitions);
        return "organizersExhibitions";
    }

    @GetMapping("/organizersExhibitionsEdit/{id}")
    public String organizersExhibitionsEdit(@PathVariable(name = "id") String exhibitionId, Model model) {
        Optional<Exhibitions> exhibition = exhibitionsRepository.findById(Long.parseLong(exhibitionId));
        model.addAttribute("exhibition", exhibition.get());
        return "organizersExhibitionsEdit";
    }

    @PostMapping("/organizersExhibitionsEdit/{id}")
    public String organizersExhibitionsEdit(@ModelAttribute("exhibitionChangeForm") @Valid Exhibitions exhibitionChangeForm, @RequestParam String TagName, @PathVariable(name = "id") String exhibitionId,
                                            Model model) {
        Optional<Exhibitions> exhibition = exhibitionsRepository.findById(Long.parseLong(exhibitionId));
        exhibition.get().setName(exhibitionChangeForm.getName());
        exhibition.get().setShortDescription(exhibitionChangeForm.getShortDescription());
        exhibition.get().setDescription(exhibitionChangeForm.getDescription());
        Optional<ExhibitionTag> tag = exhibitionTagRepository.findByName(TagName);
        if(tag.isPresent())
            exhibition.get().setTag(tag.get());
        else
            exhibition.get().setTag(new ExhibitionTag(TagName));
        exhibitionsRepository.save(exhibition.get());
        return "redirect:/organizersExhibitions";
    }

    @GetMapping("/organizersExhibitionAdd")
    public String organizersExhibitionAdd(Model model) {
        return "organizersExhibitionAdd";
    }

    @PostMapping("/organizersExhibitionAdd")
    public String organizersExhibitionAddPost(@ModelAttribute("exhibitionForm") @Valid Exhibitions exhibitionForm,
                                              @RequestParam String BeginningDateS, @RequestParam String EndDateS,
                                              @RequestParam String TagName, BindingResult bindingResult, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Organizers> organizer = organizersRepository.findByEmail(userDetails.getUsername());
        Date beginningDate = Date.from(LocalDate.parse(BeginningDateS).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.parse(EndDateS).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Optional<ExhibitionTag> tag = exhibitionTagRepository.findByName(TagName);
        Exhibitions exhibition = new Exhibitions(exhibitionForm.getName(), exhibitionForm.getShortDescription(), exhibitionForm.getDescription(),
                beginningDate, endDate, organizer.get(), null);
        if(tag.isPresent())
            exhibition.setTag(tag.get());
        else
            exhibition.setTag(new ExhibitionTag(TagName));
        exhibitionsRepository.save(exhibition);
        return "redirect:/organizersExhibitions";
    }

    @GetMapping("/organizersApplicationsAccept/{id}")
    public String organizersApplicationsAccept(@PathVariable(name = "id") String applicationId, Model model) {
        Optional<ExhibitorsApplication> application = exhibitorsApplicationRepository.findById(Long.parseLong(applicationId));
        application.get().Accepted(true);
        exhibitorsRepository.save(application.get().getExhibitor());
        return "organizersApplications";
    }

    @GetMapping("/organizersApplications")
    public String organizersApplications(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Iterable<ExhibitorsApplication> exhibitorsApplications = exhibitorsApplicationRepository.findApplicationByOrganizerEmailNotAccepted(userDetails.getUsername());
        model.addAttribute("exhibitorsApplications", exhibitorsApplications);
        return "organizersApplications";
    }

    @GetMapping("/organizerInfo/{id}")
    public String organizerInfo(@PathVariable(name = "id") String orgId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Organizers> organizer = organizersRepository.findById(Long.parseLong(orgId));
        model.addAttribute("organizer", organizer.get());
        return "organizerInfo";
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/exhibitorsPersonalAccount")
    public String exhibitorsPersonalAccount(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Exhibitors> exhibitor = exhibitorsRepository.findByEmail(userDetails.getUsername());
        model.addAttribute("exhibitor", exhibitor.get());
        return "exhibitorsPersonalAccount";
    }

    @GetMapping("/exhibitorsPersonalAccountEdit/{id}")
    public String exhibitorsPersonalAccountEdit(@PathVariable(name = "id") String exhibitorId, Model model) {
        Optional<Exhibitors> exhibitor = exhibitorsRepository.findById(Long.parseLong(exhibitorId));
        model.addAttribute("exhibitor", exhibitor.get());
        return "exhibitorsPersonalAccountEdit";
    }

    @PostMapping("/exhibitorsPersonalAccountEdit/{id}")
    public String exhibitorsPersonalAccountEdit(@ModelAttribute("exhChangeForm") @Valid Exhibitors exhChangeForm,
                                                @PathVariable(name = "id") String exhibitorId,
                                                Model model) {
        Optional<Exhibitors> exhibitor = exhibitorsRepository.findById(Long.parseLong(exhibitorId));
        exhibitor.get().setFullName(exhChangeForm.getFullName());
        exhibitor.get().setCompany(exhChangeForm.getCompany());
        exhibitor.get().setDescription(exhChangeForm.getDescription());
        exhibitorsRepository.save(exhibitor.get());
        return "redirect:/exhibitorsPersonalAccount";
    }

    @GetMapping("/exhibitorsGoods")
    public String exhibitorsGoods(Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Exhibitors> exhibitor = exhibitorsRepository.findByEmail(userDetails.getUsername());
       // ResponseEntity<ByteArrayResource> photo = getFile(exhibitor.get().getGoods().get(0).getPhoto());
        model.addAttribute("goods", exhibitor.get().getGoods());
        return "exhibitorsGoods";
    }

    @GetMapping("/exhibitorsGoodsAdd")
    public String exhibitorsGoodsAdd(Model model) {
        return "exhibitorsGoodsAdd";
    }


    @RequestMapping(value = "/exhibitorsGoodsAdd", headers = "content-type=multipart/*", method = RequestMethod.POST)
    public String exhibitorsGoodsAddPost(@ModelAttribute("productAddForm") @Valid ExhibitorsGoods productAddForm,
                                                       @RequestParam("file") MultipartFile file,
                                                       Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails) auth.getPrincipal();
        Optional<Exhibitors> exhibitor = exhibitorsRepository.findByEmail(userDetails.getUsername());
        String filepath = uploadFile(file);
        ExhibitorsGoods product  = new ExhibitorsGoods(exhibitor.get(), productAddForm.getName(), productAddForm.getDescription(), filepath);
        exhibitorsGoodsRepository.save(product);
        return "redirect:/exhibitorsGoods";
    }

    @GetMapping("/exhibitorsExhibitions")
    public String exhibitorsExhibitions(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Exhibitors> exhibitor = exhibitorsRepository.findByEmail(userDetails.getUsername());
        List<ExhibitorsApplication> exhibitions = exhibitorsApplicationRepository.findAcceptedApplicationByExhibitor(exhibitor.get().getId());
        model.addAttribute("exhibitorsApplications", exhibitions);
        return "exhibitorsExhibitions";
    }

    @GetMapping("/exhibitorsApplications")
    public String exhibitorsApplications(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Exhibitors> exhibitor = exhibitorsRepository.findByEmail(userDetails.getUsername());
        List<ExhibitorsApplication> applications = exhibitorsApplicationRepository.findNotAcceptedApplicationByExhibitor(exhibitor.get().getId());
        model.addAttribute("exhibitorsApplications", applications);
        return "exhibitorsApplications";
    }

    @GetMapping("/exhibitorsApplicationsAdd")
    public String exhibitorsApplicationsAdd(Model model) {
        Iterable<Exhibitions> exhibitions = exhibitionsRepository.findAllAccepted();
        model.addAttribute("exhibitions", exhibitions);
        return "exhibitorsApplicationsAdd";
    }

    @PostMapping("/exhibitorsApplicationsAdd")
    public String exhibitorsApplicationsAddPost(@ModelAttribute("applicationForm") @Valid ExhibitorsApplication applicationForm, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Exhibitors> exhibitor = exhibitorsRepository.findByEmail(userDetails.getUsername());
        ExhibitorsApplication application = new ExhibitorsApplication(exhibitor.get(), applicationForm.getExhibition(), applicationForm.getDescription());
        exhibitorsApplicationRepository.save(application);
        return "redirect:/exhibitorsApplications";
    }

    @GetMapping("/exhibitorInfo/{id}")
    public String exhibitorInfo(@PathVariable(name = "id") String exhId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Exhibitors> exhibitor = exhibitorsRepository.findById(Long.parseLong(exhId));

        model.addAttribute("exhibitor", exhibitor.get());
        return "exhibitorInfo";
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/admin/userlist")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "admin";
    }
    @PostMapping("/admin")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            userService.deleteUser(userId);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/get/{userId}")
    public String  getUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergetList(userId));
        return "admin";
    }

    @GetMapping("/exhibitionInfo/{id}")
    public String exhibitionInfo(@PathVariable(name = "id") String exhId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Exhibitions> exhibition = exhibitionsRepository.findById(Long.parseLong(exhId));
        List<ExhibitorsApplication> applications = exhibitorsApplicationRepository.findAcceptedByExhibition(exhibition.get().getId());
        model.addAttribute("apps", applications);
        model.addAttribute("exhibition", exhibition.get());
        return "exhibitionInfo";
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/moderatorsPersonalAccount")
    public String moderatorsPersonalAccount(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails)auth.getPrincipal();
        Optional<Moderators> moderator = moderatorsRepository.findByEmail(userDetails.getUsername());
        model.addAttribute("moderator", moderator.get());
        return "moderatorsPersonalAccount";
    }

    @GetMapping("/moderatorsExhibitions")
    public String moderatorsExhibitions(Model model) {
        Iterable<Exhibitions> exhibitions = exhibitionsRepository.findAll();
        model.addAttribute("exhibitions", exhibitions);
        return "moderatorsExhibitions";
    }

    @GetMapping("/moderatorsExhibitorsApplications")
    public String moderatorsExhibitorsApplications(Model model) {
        Iterable<ExhibitorsApplication> exhibitorsApplications = exhibitorsApplicationRepository.findAll();
        model.addAttribute("exhibitorsApplications", exhibitorsApplications);
        return "moderatorsExhibitorsApplications";
    }

    @GetMapping("/moderatorOrganizersApplicationsAccept/{id}")
    public String moderatorOrganizersApplicationsAccept(@PathVariable(name = "id")
                                                                    String orgApplicationId, Model model) {
        Optional<Exhibitions> exhibition = exhibitionsRepository.findById(Long.parseLong(orgApplicationId));
        exhibition.get().setApplication(false);
        exhibitionsRepository.save(exhibition.get());
        return "redirect:/moderatorsExhibitions";
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

