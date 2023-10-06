package com.ebenz.userservice.service;

import com.ebenz.userservice.dto.DepartmentDto;
import com.ebenz.userservice.dto.ResponseDto;
import com.ebenz.userservice.dto.UserDto;
import com.ebenz.userservice.entity.User;
import com.ebenz.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    //@Autowired
    //private RestTemplate restTemplate;
    @Autowired
    private WebClient webClient;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public ResponseDto getUser(Long userId) {
        ResponseDto responseDto = new ResponseDto();
        User user = userRepository.findById(userId).get();
        UserDto userDto = mapToUser(user);

        //ResponseEntity<DepartmentDto> responseEntity = restTemplate.
          //      getForEntity("http://localhost:8080/api/departments/" + user.getDepartmentId(),
            //            DepartmentDto.class);

       // DepartmentDto departmentDto = responseEntity.getBody();

       // System.out.println(responseEntity.getStatusCode());

        DepartmentDto departmentDto = webClient.get()
                        .uri("http://localhost:8080/api/departments" + user.getDepartmentId())
                                .retrieve().bodyToMono(DepartmentDto.class)
                        .block();

                responseDto.setUser(userDto);
                responseDto.setDepartment(departmentDto);
                return responseDto;
    }

    private UserDto mapToUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
