package no.hvl.dat108.participant.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.persistence.Embedded;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import no.hvl.dat108.participant.Participant;
import no.hvl.dat108.participant.ParticipantService;
import no.hvl.dat108.participant.password.Password;
import no.hvl.dat108.participant.password.PasswordService;

@Controller
public class RegistrationController {

	@Autowired ParticipantService participantService;
	
	@GetMapping("/registration")
	public String getRegistrationForm() {
		return "registration";
	}
	
	@PostMapping("/registration")
	public String recieveRegistration(
			@ModelAttribute("registration") @Valid RegistrationForm registration, 
			BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return showInvalidRegistrationMessage(model);
		}
		
		if(participantService.phoneExists(registration.getPhone())) {
			return showParticipantAlreadyExistsMessage(model);
		}
		
		return registerAndShowResult(registration, model);
	}
	
	private String showInvalidRegistrationMessage(Model model) {
		model.addAttribute("errorMessage", "Registration details are invalid");
		
		return "registration";
	}
	
	private String showParticipantAlreadyExistsMessage(Model model) {
		model.addAttribute("errorMessage", "A participant for that phonenumber already exists");
		
		return "registration";
	}
	
	private String registerAndShowResult(RegistrationForm registration, Model model) {
		Participant registered = participantService.registerNewParticipant(registration);
		
		model.addAttribute("participant", registered);
		
		return "registration_successful";
	}
	
	 @GetMapping("/registration_successful") 
	 public String getSuccessfulRegistration() {
		 return "registration_successful";
	 }
	
}
