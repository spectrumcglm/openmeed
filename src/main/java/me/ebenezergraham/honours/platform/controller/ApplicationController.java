package me.ebenezergraham.honours.platform.controller;

import io.swagger.annotations.Example;
import me.ebenezergraham.honours.platform.model.Issue;
import me.ebenezergraham.honours.platform.model.Project;
import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.payload.LoginRequest;
import me.ebenezergraham.honours.platform.repository.ActivatedRepository;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.security.CustomUserDetailsService;
import me.ebenezergraham.honours.platform.security.UserPrincipal;
import me.ebenezergraham.honours.platform.services.EmailService;
import me.ebenezergraham.honours.platform.services.FileService;
import me.ebenezergraham.honours.platform.services.IncentiveService;
import me.ebenezergraham.honours.platform.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Controller
@RequestMapping("/api/v1")
public class ApplicationController {

  private final RewardRepository rewardRepository;
  private final AllocatedIssueRepository allocatedIssueRepository;
  private final ActivatedRepository activatedRepository;
  private final IncentiveService incentiveService;

  public ApplicationController(ActivatedRepository activatedRepository,
                               RewardRepository rewardRepository,
                               AllocatedIssueRepository allocatedIssueRepository,
                               IncentiveService incentiveService,
                               CustomUserDetailsService userService) {
    this.incentiveService = incentiveService;
    this.rewardRepository = rewardRepository;
    this.allocatedIssueRepository = allocatedIssueRepository;
    this.activatedRepository = activatedRepository;

  }

  @PostMapping("/issue")
  public ResponseEntity<?> assignIssue(@Valid @RequestBody String url, Authentication authentication) {

    Issue issue = new Issue();
    issue.setIssue(url);
    issue.setContributor(((UserPrincipal) authentication.getPrincipal()).getUsername());
    Issue res = allocatedIssueRepository.save(issue);
    return ResponseEntity.ok(res);
  }

  @PostMapping("/projects")
  public ResponseEntity<?> assignIssue(@Valid @RequestBody String[] projects) {
    List<String> project = Arrays.asList(projects);
    project.forEach(repository -> {
      activatedRepository.save(new Project(repository));
    });
    return ResponseEntity.ok().build();
  }

  @GetMapping("/projects")
  public ResponseEntity<?> findAllActivatedProjects() {
    return ResponseEntity.of(Optional.of(activatedRepository.findAll()));
  }

  @PostMapping("issue/incentive")
  public ResponseEntity<?> assignIncentive(@Valid @RequestBody Reward reward) {
    if (incentiveService.storeIncentive(reward) != null) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.noContent().build();
  }
}
