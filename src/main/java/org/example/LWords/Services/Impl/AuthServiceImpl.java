package org.example.LWords.Services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.LWords.Entities.ActivityStatistic;
import org.example.LWords.dto.Requests.AuthRequest;
import org.example.LWords.Entities.ERole;
import org.example.LWords.Entities.Role;
import org.example.LWords.Entities.User;
import org.example.LWords.Security.JwtUtil;
import org.example.LWords.Security.MyUserDetails;
import org.example.LWords.Services.AuthService;
import org.example.LWords.dto.Responses.JwtResponse;
import org.example.LWords.repos.ActivityStatisticRepository;
import org.example.LWords.repos.RoleRepository;
import org.example.LWords.repos.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.AnnotatedType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final private AuthenticationProvider authenticationProvider;
    final private JwtUtil jwtUtil;
    final private UserRepository userRepository;
    final private RoleRepository roleRepository;
    final private ActivityStatisticRepository activityStatisticRepository;

    @Override
    public JwtResponse authenticateUser(AuthRequest authRequest) {
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        User user = userDetails.getUser();
        Iterable<ActivityStatistic> activityStatistics = activityStatisticRepository.findByUser(user);
        int numberOfWeek = LocalDateTime.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        if(numberOfWeek != user.getStatisticNumberOfWeek()){
            user.setStatisticNumberOfWeek(numberOfWeek);
            for (ActivityStatistic statistic:
                    activityStatistics) {
                statistic.setTotalCorrectAnsCount(0);
                statistic.setTotalIncorrectAnsCount(0);
                statistic.setTotalLearnedWordsCount(0);
                activityStatisticRepository.save(statistic);
            }
        }

        String jwt = jwtUtil.generateJwtToken(userDetails);
        return new JwtResponse(
                userDetails.getId(),
                jwt, userDetails.getUsername(), roles, userDetails.getProgressLength(), userDetails.getTranslationCount());
    }

    @Override
    public Boolean createUser(AuthRequest authRequest) {
        if(userRepository.existsByUsername(authRequest.getUserName()))
            return false;
        else{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = encoder.encode(authRequest.getPassword());
            LocalDateTime date = LocalDateTime.now();
            int numberOfWeek = date.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
            User user = new User(authRequest.getUserName(), password, LocalDateTime.now(),numberOfWeek);
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.USER));
            user.setRoles(roles);
            userRepository.save(user);

            for(int dayOfWeek=1;dayOfWeek<8;dayOfWeek++){
                ActivityStatistic activityStatistic = new ActivityStatistic(user, DayOfWeek.of(dayOfWeek));
                activityStatisticRepository.save(activityStatistic);
            }
            return true;
        }
    }
}
