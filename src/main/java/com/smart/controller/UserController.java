package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void addUserData(Model model, Principal principal)
	{
		String userName = principal.getName();
		System.out.println("USER NAME" + userName);

		User user = userRepository.getUserByEmail(userName);

		System.out.println("USER:" + user);

		model.addAttribute("user", user);
	}

	//home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		model.addAttribute("title","Home");
		return "user/userDashboard";
	}
	
	// add contact
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact", new Contact());
		return "user/contact_form";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute("contact") Contact contact,
			@RequestParam("profileImage") MultipartFile profileImage, 
			Principal principal, HttpSession session)
	{
		
		try
		{
			System.out.println("Contact: "+ contact);
			
			String name = principal.getName();
			
			User user = this.userRepository.getUserByEmail(name);
			
			
			if(profileImage.isEmpty())
			{
				System.out.println("File is empty");
				contact.setImage("contact.png");
			}
			else
			{
				// getting image name and storing in image	 folder
				contact.setImage(profileImage.getOriginalFilename());
				
				File file = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(file.getAbsoluteFile() + File.separator + profileImage.getOriginalFilename());
				
				Files.copy( profileImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			
			contact.setUser(user);
			user.getContacts().add(contact);
			
			this.userRepository.save(user);
			
			System.out.println("Added to database");
			
			session.setAttribute("message", new Message("Contact Saved!!", "alert-success"));
		}
		catch(Exception e) {
			System.out.println("Error :"+ e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("An Error Occured!!", "alert-danger"));
		}
		
		return "user/contact_form";
	}
	
	// per page 5
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") int page,Model model,Principal principal)
	{
		model.addAttribute("title","User Contacts");
		
		//List<Contact> contacts = this.contactRepository.findByUserId(702);
		//System.out.println(contacts);
		
		String userName = principal.getName();
		
		User user = this.userRepository.getUserByEmail(userName);
		
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);
		
		System.out.println(contacts);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		
		return "user/showContacts";
	}
	
	@GetMapping("/{cId}/contact")
	public String showContactDetails(@PathVariable("cId")int cId,Model model,Principal principal)
	{
		
		
		Contact contact =  this.contactRepository.findById(cId).get();
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByEmail(userName);
		
		if(user.getId() == contact.getUser().getId()) // making user to see only contacts of his.
		{
			model.addAttribute("title", contact.getName());
			model.addAttribute("contact", contact);
		}
		
		return "user/contactDetail";
	}
	
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") int cId,Principal principal, HttpSession session)
	{
		Contact contact = this.contactRepository.findById(cId).get();
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByEmail(userName);
		
		user.getContacts().remove(contact);
		
		this.userRepository.save(user);
		session.setAttribute("message", new Message("Contact Deleted!!","alert-success"));
		
		return "redirect:/user/show-contacts/0";
	}
	
	@PostMapping("/updateContact/{cId}")
	public String updateContact(@PathVariable("cId") int cId, Model model, Principal principal)
	{
		model.addAttribute("title", "Update Contact");
		
		Contact contact = this.contactRepository.findById(cId).get();
		model.addAttribute("contact", contact);
		return "user/updateContact";
	}
	
	@PostMapping("/process-update")
	public String processUpdate(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile profileImage,HttpSession session, Principal principal)
	{
		try
		{
			int cId = (int)contact.getcId();
			Contact oldContact	= this.contactRepository.findById(cId).get();
			if(!profileImage.isEmpty())
			{
				//delete old file
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContact.getImage());
				file1.delete();
				
				// add new file
				File file = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(file.getAbsoluteFile() + File.separator + profileImage.getOriginalFilename());
				
				Files.copy( profileImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(profileImage.getOriginalFilename());
			}
			else
			{
				contact.setImage(oldContact.getImage());
			}
			
			String userName = principal.getName();
			User user = this.userRepository.getUserByEmail(userName);
			contact.setUser(user);
			this.contactRepository.save(contact); 
			
			session.setAttribute("message", new Message("Contact Updated!!", "alert-success"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong!!", "alert-danger"));
		}
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	
	//profile
	
	@GetMapping("/profile")
	public String showProfile(Model model)
	{
		model.addAttribute("title","Profile");
		return "user/profile";
	}
	
	
	@GetMapping("/settings")
	public String Settings(Model model)
	{
		model.addAttribute("title", "Settings");
		 return "user/settings";
	}
	
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass, Principal principal,
			HttpSession session)
	{
		User user = this.userRepository.getUserByEmail(principal.getName());
		if(this.bCryptPasswordEncoder.matches(oldPass, user.getPassword()))
		{
			user.setPassword(this.bCryptPasswordEncoder.encode(newPass));
			
			this.userRepository.save(user);
			session.setAttribute("message", new Message(" Password Changed!!", "alert-success")); 	
		}
		else
		{
			session.setAttribute("message", new Message("Old Password Did not match", "alert-danger"));
			return "redirect:/user/settings";
		}
		
		return "redirect:/user/index";
	}
	
	
	
}
